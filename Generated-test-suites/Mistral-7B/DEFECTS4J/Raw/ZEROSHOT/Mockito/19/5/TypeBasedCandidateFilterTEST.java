import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.invocation.MethodType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter nextFilter;
    private TypeBasedCandidateFilter filter;

    @Before
    public void setUp() {
        nextFilter = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeMatchesAnyMock_returnsNextFilter() throws Exception {
        // Given
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Object fieldInstance = mock(Object.class);
        Field field = new Field("testField", String.class, mock(Class.class));

        mocks.add(mock1);
        mocks.add(mock2);

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(nextFilter, result);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeMatchesOnlyOneMock_returnsThatMock() throws Exception {
        // Given
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Object fieldInstance = mock(Object.class);
        Field field = new Field("testField", String.class, mock(Class.class));

        mocks.add(mock1);
        mocks.add(mock2);

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock1, result);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeMatchesNoMock_returnsNull() throws Exception {
        // Given
        List<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = new Field("testField", Integer.class, mock(Class.class));

        mocks.add(mock(String.class));

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(null, result);
    }

    @Test
    public void testFilterCandidate_whenNextFilterReturnsNull_returnsNull() throws Exception {
        // Given
        List<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = new Field("testField", String.class, mock(Class.class));

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // When nextFilter returns null
        Mockito.doReturn(null).when(nextFilter).filterCandidate(mock(Collection.class), mock(Field.class), fieldInstance);

        // Then
        Object result2 = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(null, result2);
    }

    @Test
    public void testFilterCandidate_whenNextFilterReturnsAnotherType_returnsNull() throws Exception {
        // Given
        List<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = new Field("testField", Integer.class, mock(Class.class));

        Object mock = mock(String.class);
        mocks.add(mock);

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // When nextFilter returns an object of another type
        Mockito.doReturn(mock(Object.class)).when(nextFilter).filterCandidate(mock(Collection.class), mock(Field.class), fieldInstance);

        // Then
        Object result2 = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(null, result2);
    }

    @Test
    public void testFilterCandidate_whenNextFilterReturnsAnotherClassType_returnsNull() throws Exception {
        // Given
        List<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = new Field("testField", Integer.class, mock(Class.class));

        Object mock = mock(String.class);
        mocks.add(mock);

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // When nextFilter returns an object of another class type
        Class<?> anotherClass = mock(Class.class);
        Mockito.doReturn(spy(new TypeBasedCandidateFilter(mock(MockCandidateFilter.class))))
                .when(nextFilter)
                .filterCandidate(mock(Collection.class), mock(Field.class), fieldInstance);
        Mockito.doReturn(anotherClass).when(nextFilter).getClass();

        // Then
        Object result2 = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(null, result2);
    }
}