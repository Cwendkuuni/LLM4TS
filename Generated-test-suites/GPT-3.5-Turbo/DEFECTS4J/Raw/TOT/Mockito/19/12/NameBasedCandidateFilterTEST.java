import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {
    
	private final MockUtil mockUtil = mock(MockUtil.class);
    private final MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
    private final NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

    @Test
    public void testFilterCandidate_OneMockAndMatch_ReturnsNextFilter() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = TestClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();
        
        when(mockUtil.getMockName(mock)).thenReturn("field");

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
        assertEquals(result, nextFilter);
        assertEquals(result.getMocks(), mocks);
        assertEquals(result.getField(), field);
        assertEquals(result.getFieldInstance(), fieldInstance);
    }

    @Test
    public void testFilterCandidate_OneMockAndNoMatch_ReturnsNextFilter() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = TestClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();
        
        when(mockUtil.getMockName(mock)).thenReturn("otherField");

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
        assertEquals(result, nextFilter);
        assertEquals(result.getMocks(), mocks);
        assertEquals(result.getField(), field);
        assertEquals(result.getFieldInstance(), fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocksAndSomeMatch_ReturnsNextFilterWithMatchedMocks() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Field field = TestClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();
        
        when(mockUtil.getMockName(mock1)).thenReturn("field");
        when(mockUtil.getMockName(mock2)).thenReturn("otherField");
        when(mockUtil.getMockName(mock3)).thenReturn("field");

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextFilter).filterCandidate(eq(Arrays.asList(mock1, mock3)), field, fieldInstance);
        assertEquals(result, nextFilter);
        assertEquals(result.getMocks(), Arrays.asList(mock1, mock3));
        assertEquals(result.getField(), field);
        assertEquals(result.getFieldInstance(), fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocksAndNoMatch_ReturnsNextFilterWithAllMocks() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Field field = TestClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();
        
        when(mockUtil.getMockName(mock1)).thenReturn("otherField");
        when(mockUtil.getMockName(mock2)).thenReturn("otherField");
        when(mockUtil.getMockName(mock3)).thenReturn("otherField");

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
        assertEquals(result, nextFilter);
        assertEquals(result.getMocks(), mocks);
        assertEquals(result.getField(), field);
        assertEquals(result.getFieldInstance(), fieldInstance);
    }

    @Test
    public void testFilterCandidate_NoMocks_ReturnsNextFilterWithNoMocks() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();

        Field field = TestClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
        assertEquals(result, nextFilter);
        assertEquals(result.getMocks(), mocks);
        assertEquals(result.getField(), field);
        assertEquals(result.getFieldInstance(), fieldInstance);
    }
    
    private static class TestClass {
        private Object field;
    }
}