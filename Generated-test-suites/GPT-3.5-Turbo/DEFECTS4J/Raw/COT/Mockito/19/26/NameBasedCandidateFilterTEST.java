
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;
    private MockUtil mockUtilMock;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        mockUtilMock = mock(MockUtil.class);
        filter = new NameBasedCandidateFilter(nextMock, mockUtilMock);
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void filterCandidate_withMatchingMockName_shouldReturnNextFilter() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        when(mockUtilMock.getMockName(mock1)).thenReturn("field");
        when(mockUtilMock.getMockName(mock2)).thenReturn("field");
        when(mockUtilMock.getMockName(mock3)).thenReturn("differentField");
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);
    }

    @Test
    public void filterCandidate_withNoMatchingMockName_shouldReturnNextFilter() {
        Object mock = mock(Object.class);
        when(mockUtilMock.getMockName(mock)).thenReturn("differentField");
        mocks.add(mock);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void filterCandidate_withConflictingFieldNames_shouldReturnNextFilter() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        when(mockUtilMock.getMockName(mock1)).thenReturn("field");
        when(mockUtilMock.getMockName(mock2)).thenReturn("differentField");
        mocks.add(mock1);
        mocks.add(mock2);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }
}