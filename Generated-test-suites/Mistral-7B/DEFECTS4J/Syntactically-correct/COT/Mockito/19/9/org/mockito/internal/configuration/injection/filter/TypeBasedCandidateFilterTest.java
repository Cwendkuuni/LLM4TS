import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Collections3;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testConstructor() {
        assertNotNull(filter);
        assertNull(filter.next);
    }

    @Test
    public void testFilterCandidate_EmptyMockCollection() {
        assertNull(filter.filterCandidate(Collections3.newArrayList(), spy(Field.class), spy(Object.class)));
    }

    @Test
    public void testFilterCandidate_FieldIsInstanceOfMockType() {
        List<Object> mocks = Collections3.newArrayList(mock(String.class), mock(Integer.class));
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);
        Class<?> fieldType = String.class;

        List<Object> result = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(result.contains(mock(String.class)));
    }

    @Test
    public void testFilterCandidate_FieldIsNotInstanceOfMockType() {
        List<Object> mocks = Collections3.newArrayList(mock(String.class), mock(Integer.class));
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);
        Class<?> fieldType = Integer.class;

        List<Object> result = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(result.contains(mock(String.class)));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMockCollectionThrowsException() {
        filter.filterCandidate(null, spy(Field.class), spy(Object.class));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldThrowsException() {
        List<Object> mocks = Collections3.newArrayList(mock(String.class), mock(Integer.class));
        filter.filterCandidate(mocks, null, spy(Object.class));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstanceThrowsException() {
        List<Object> mocks = Collections3.newArrayList(mock(String.class), mock(Integer.class));
        filter.filterCandidate(mocks, spy(Field.class), null);
    }
}