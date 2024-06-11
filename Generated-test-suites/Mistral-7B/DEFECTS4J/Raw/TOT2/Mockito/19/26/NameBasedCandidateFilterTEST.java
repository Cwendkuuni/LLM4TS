
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import java.lang.reflect.Field;
import java.util.*;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter mockFilter;
    private NameBasedCandidateFilter filter;

    @Before
    public void setUp() {
        mockFilter = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(mockFilter);
    }

    @Test
    public void testConstructor() {
        assertNotNull(filter);
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        Object mock = mock(Object.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mockUtil.getMockName(mock)).thenReturn("mockName");
        when(field.getName()).thenReturn("mockName");

        OngoingInjecter result = filter.filterCandidate(asList(mock), field, fieldInstance);
        assertNotNull(result);
        assertEquals(mock, result.getCandidate());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mockUtil.getMockName(mock1)).thenReturn("mockName1");
        when(mockUtil.getMockName(mock2)).thenReturn("mockName2");
        when(field.getName()).thenReturn("mockName1");

        OngoingInjecter result = filter.filterCandidate(asList(mock1, mock2), field, fieldInstance);
        assertNotNull(result);
        assertEquals(mock1, result.getCandidate());
    }

    @Test
    public void testFilterCandidateWithConflictingMocks() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mockUtil.getMockName(mock1)).thenReturn("mockName1");
        when(mockUtil.getMockName(mock2)).thenReturn("mockName1");
        when(field.getName()).thenReturn("mockName2");

        OngoingInjecter result = filter.filterCandidate(asList(mock1, mock2), field, fieldInstance);
        assertNotNull(result);
        assertEquals(mock1, result.getCandidate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNoMatchingMock() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mockUtil.getMockName(mock1)).thenReturn("mockName1");
        when(mockUtil.getMockName(mock2)).thenReturn("mockName2");
        when(field.getName()).thenReturn("mockName3");

        filter.filterCandidate(asList(mock1, mock2), field, fieldInstance);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        Collection<Object> mocks = Collections.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullField() {
        Collection<Object> mocks = mock(Collection.class);
        Field field = null;
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
        fail("Expected NullPointerException");
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullMockCollection() {
        Collection<Object> mocks = null;
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
        fail("Expected NullPointerException");
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);
        fail("Expected NullPointerException");
    }
}
