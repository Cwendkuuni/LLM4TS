import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Arrays;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_CollectionIsEmpty() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(eq(Collections.emptyList()), eq(field), eq(fieldInstance));
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_MockTypeMatches_ReturnsNextFilter() {
        Collection<Object> mocks = Arrays.asList(new MockType1(), new MockType2(), new MockType1());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        MockType1 mockType1 = mock(MockType1.class);
        when(mockType1.getClass()).thenReturn(MockType1.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(argThat(c -> c.contains(mockType1)), eq(field), eq(fieldInstance));
        assertEquals(result, sameInstance(nextMock.filterCandidate(argThat(c -> c.contains(mockType1)), field, fieldInstance)));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = Arrays.asList(new MockType1(), new MockType2());
        Field field = mock(Field.class);

        filter.filterCandidate(mocks, field, null);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMockCandidateFilter() {
        Collection<Object> mocks = Arrays.asList(new MockType1(), new MockType2());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullCollection() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(null, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_FieldsOfDifferentTypes() {
        Collection<Object> mocks = Arrays.asList(new MockType1(), new MockType2());
        Field field1 = mock(Field.class).withType(Integer.TYPE);
        Field field2 = mock(Field.class).withType(String.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result1 = filter.filterCandidate(mocks, field1, fieldInstance);
        OngoingInjecter result2 = filter.filterCandidate(mocks, field2, fieldInstance);

        verify(nextMock, times(2)).filterCandidate(argThat(c -> c.isEmpty()), eq(field1), eq(fieldInstance));
        verify(nextMock, times(2)).filterCandidate(argThat(c -> c.isEmpty()), eq(field2), eq(fieldInstance));

        assertNull(result1);
        assertNull(result2);
    }
}