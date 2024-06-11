import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedCollections;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

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
        Collection<Object> mocks = ExtendedCollections.emptyList();
        Field field = getTestField();
        Object fieldInstance = getTestFieldInstance();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(nextMock, result);
    }

    @Test
    public void testFilterCandidate_CollectionContainsMatchingMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(getMockInstance());
        Field field = getTestField();
        Object fieldInstance = getTestFieldInstance();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mocks.get(0), result.getMock());
    }

    @Test
    public void testFilterCandidate_CollectionContainsNonMatchingMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        Field field = getTestField();
        Object fieldInstance = getTestFieldInstance();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(nextMock, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullField() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = getTestFieldInstance();

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = getTestField();
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullMockCollection() {
        Field field = getTestField();
        Object fieldInstance = getTestFieldInstance();

        filter.filterCandidate(null, field, fieldInstance);
    }

    private Field getTestField() {
        try {
            return getClass().getField("testField");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getTestFieldInstance() {
        return new Object();
    }

    private Object getMockInstance() {
        return mock(Object.class);
    }

}