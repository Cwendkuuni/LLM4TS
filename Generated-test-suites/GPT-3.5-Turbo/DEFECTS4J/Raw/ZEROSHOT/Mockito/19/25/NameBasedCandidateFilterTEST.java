import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;

    @Before
    public void setup() {
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidate_SingleMock() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(filter.next).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("mock1");

        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(filter.next).filterCandidate(any(Collection.class), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatch() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("mock3");

        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(filter.next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_ConflictingNames() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("mock1");

        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(filter.next).filterCandidate(any(Collection.class), eq(field), eq(fieldInstance));
    }
}