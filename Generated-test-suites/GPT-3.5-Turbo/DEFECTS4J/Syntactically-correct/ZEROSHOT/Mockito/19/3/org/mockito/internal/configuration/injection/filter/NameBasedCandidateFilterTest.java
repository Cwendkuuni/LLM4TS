import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {
    
    private NameBasedCandidateFilter mockNameBasedCandidateFilter;
    private MockCandidateFilter nextMockCandidateFilter;
    private MockUtil mockMockUtil;
    
    @Before
    public void setup() {
        nextMockCandidateFilter = mock(MockCandidateFilter.class);
        mockNameBasedCandidateFilter = new NameBasedCandidateFilter(nextMockCandidateFilter);
        mockMockUtil = mock(MockUtil.class);
    }
    
    @Test
    public void TestfilterCandidate_whenMocksSizeGreaterThanOne_verifyMockUtilGetMockNameCalledForEachMock() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        // Act
        mockNameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        verify(mockMockUtil, times(2)).getMockName(any());
    }
    
    @Test
    public void TestfilterCandidate_whenMocksSizeGreaterThanOneAndMockNameMatches_verifyNextFilterCandidateCalledWithMockNameMatches() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        when(field.getName()).thenReturn("mock1");
        Object mockNameMatches = mock(Object.class);
        when(mockMockUtil.getMockName(mock1)).thenReturn(mockNameMatches);
        
        // Act
        mockNameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        verify(nextMockCandidateFilter).filterCandidate(eq(mockNameMatches), eq(field), eq(fieldInstance));
    }
    
    @Test
    public void TestfilterCandidate_whenMocksSizeGreaterThanOneAndMockNameDoesNotMatch_verifyNextFilterCandidateCalledWithAllMocks() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        when(field.getName()).thenReturn("mock3");
        
        // Act
        mockNameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        verify(nextMockCandidateFilter).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }
    
    @Test
    public void TestfilterCandidate_whenMocksSizeEqualsOne_verifyNextFilterCandidateCalledWithMocks() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        // Act
        mockNameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        verify(nextMockCandidateFilter).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }
}