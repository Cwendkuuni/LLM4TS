import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private MockUtil mockUtil;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        mockUtil = mock(MockUtil.class);
        filter = new NameBasedCandidateFilter(next);
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_withMatchingMockName_shouldReturnFilteredMocks() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        when(field.getName()).thenReturn("mockName");
        when(mockUtil.getMockName(mock1)).thenReturn("mockName");
        when(mockUtil.getMockName(mock2)).thenReturn("differentMockName");

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(anyList(), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_withNoMatchingMockName_shouldReturnAllMocks() {
        // Arrange
        Object mock = mock(Object.class);
        mocks.add(mock);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_withMultipleMatchingMockNames_shouldReturnFilteredMocks() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        when(field.getName()).thenReturn("mockName");
        when(mockUtil.getMockName(mock1)).thenReturn("mockName");
        when(mockUtil.getMockName(mock2)).thenReturn("mockName");
        when(mockUtil.getMockName(mock3)).thenReturn("differentMockName");

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(anyList(), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_withEmptyMocks_shouldReturnAllMocks() {
        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

}