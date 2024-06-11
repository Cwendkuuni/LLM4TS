import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.mockitosis.MockCandidate;
import org.mockito.mockitosis.MockCandidateFilter;
import org.mockito.mockitosis.OngoingMock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;
    private Collection<Object> mocks;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
        mocks = new ArrayList<>();
    }

    @Test
    public void testFilterCandidate_whenMocksMatchType_returnFilteredMock() {
        Object mock = mock(SomeClass.class);
        mocks.add(mock);

        Field field = new Field("fieldName", String.class, SomeClass.class);
        Object fieldInstance = mock(SomeClass.class);

        OngoingMock filteredMock = mock(SomeClass.class);

        when(next.filterCandidate(mocks, field, fieldInstance)).thenReturn(filteredMock);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(filteredMock, result);
    }

    @Test
    public void testFilterCandidate_whenNoMocksMatchType_returnNull() {
        Object mock1 = mock(DifferentClass.class);
        mocks.add(mock1);

        Object mock2 = mock(SomeClass.class);
        mocks.add(mock2);

        Field field = new Field("fieldName", String.class, SomeClass.class);
        Object fieldInstance = mock(SomeClass.class);

        OngoingMock filteredMock = mock(SomeClass.class);

        when(next.filterCandidate(mocks, field, fieldInstance)).thenReturn(filteredMock);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_whenNextFilterReturnsNull_returnNull() {
        Object mock = mock(SomeClass.class);
        mocks.add(mock);

        Field field = new Field("fieldName", String.class, SomeClass.class);
        Object fieldInstance = mock(SomeClass.class);

        when(next.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_whenFieldIsNull_throwIllegalArgumentException() {
        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_whenFieldInstanceIsNull_throwIllegalArgumentException() {
        filter.filterCandidate(mocks, field, null);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_whenMocksIsNull_throwNullPointerException() {
        filter.filterCandidate(null, field, fieldInstance);
    }
}