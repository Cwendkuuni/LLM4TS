import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedCollections;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.collections.ExtendedCollections.newArrayList;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsAssignableFromMockType_ThenAddMockToList() {
        Collection<Object> mocks = newArrayList();
        mocks.add(new MockType1());
        mocks.add(new MockType2());

        Field field = new FieldType();

        Object fieldInstance = new FieldInstance();

        List<Object> expectedMockMatches = newArrayList(new MockType1());

        List<Object> actualMockMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedMockMatches, actualMockMatches);
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsNotAssignableFromMockType_ThenDoNotAddMockToList() {
        Collection<Object> mocks = newArrayList();
        mocks.add(new MockType1());
        mocks.add(new MockType2());

        Field field = new FieldType();

        Object fieldInstance = new FieldInstance();

        List<Object> expectedMockMatches = newArrayList();

        List<Object> actualMockMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedMockMatches, actualMockMatches);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_WhenFilterFieldIsNull_ThenThrowIllegalArgumentException() {
        Collection<Object> mocks = newArrayList();
        Object fieldInstance = new FieldInstance();

        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_WhenNextFilterIsNull_ThenThrowIllegalArgumentException() {
        Collection<Object> mocks = newArrayList();
        Field field = new FieldType();
        Object fieldInstance = new FieldInstance();

        filter.filterCandidate(mocks, field, null);
    }

    @Test
    public void testFilterCandidate_WhenNextFilterReturnsNull_ThenReturnEmptyList() {
        Collection<Object> mocks = newArrayList();
        List<Object> expectedMockMatches = newArrayList();
        mocks.add(new MockType1());

        Field field = new FieldType();

        Object fieldInstance = new FieldInstance();

        doReturn(null).when(nextMock).filterCandidate(mocks, field, fieldInstance);

        List<Object> actualMockMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedMockMatches, actualMockMatches);
    }

    @Test
    public void testFilterCandidate_WhenNextFilterReturnsEmptyList_ThenReturnEmptyList() {
        Collection<Object> mocks = newArrayList();
        List<Object> expectedMockMatches = newArrayList();
        mocks.add(new MockType1());

        Field field = new FieldType();

        Object fieldInstance = new FieldInstance();

        doReturn(ExtendedCollections.emptyList()).when(nextMock).filterCandidate(mocks, field, fieldInstance);

        List<Object> actualMockMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedMockMatches, actualMockMatches);
    }

    @Test
    public void testFilterCandidate_WhenNextFilterReturnsListWithMock_ThenAddMockToListAndReturnIt() {
        Collection<Object> mocks = newArrayList();
        List<Object> expectedMockMatches = newArrayList(new MockType1());
        mocks.add(new MockType1());

        Field field = new FieldType();

        Object fieldInstance = new FieldInstance();

        List<Object> nextMockMatches = newArrayList(new MockType2());

        doReturn(nextMockMatches).when(nextMock).filterCandidate(mocks, field, fieldInstance);

        List<Object> actualMockMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedMockMatches, actualMockMatches);
    }
}