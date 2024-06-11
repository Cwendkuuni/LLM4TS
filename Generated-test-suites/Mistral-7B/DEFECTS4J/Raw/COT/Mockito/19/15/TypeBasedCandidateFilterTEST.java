import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.invocation.MockitoMethodInvocation;
import org.mockito.stub.MockitoStubInvocationHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

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
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = spy(new String("testString"));

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        OngoingInjecter expected = nextMock.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expected, actual);
    }

    @Test
    public void testFilterCandidate_CollectionContainsOneMockMatchingType() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(String.class);
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = new String("testString");

        mocks.add(mock);

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        Object expected = mock;

        assertEquals(expected, actual.getMock());
    }

    @Test
    public void testFilterCandidate_CollectionContainsMultipleMocksMatchingType() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = new String("testString");

        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        Object expected = mock1; // Can be either mock1 or mock2 as TypeBasedCandidateFilter does not differentiate

        assertEquals(expected, actual.getMock());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_CollectionIsNull() {
        Collection<Object> mocks = null;
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = new String("testString");

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_FieldIsNull() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(String.class);
        Field field = null;
        Object fieldInstance = new String("testString");

        mocks.add(mock);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_FieldInstanceIsNull() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(String.class);
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = null;

        mocks.add(mock);

        filter.filterCandidate(mocks, field, fieldInstance);
    }
}