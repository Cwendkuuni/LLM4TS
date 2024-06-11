import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockComponent;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;
import org.mockito.mock.MockComponent;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TypeBasedCandidateFilterTest {
    @MockComponent
    private MockCandidateFilter next;

    private TypeBasedCandidateFilter filter;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = new TypeBasedCandidateFilter(next);
        fieldInstance = Mockito.mock(Object.class);
    }

    @Test
    public void testFilterCandidate_EmptyMocks() {
        List<Object> mocks = new ArrayList<>();
        Field field = Mockito.mock(Field.class);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        List<Object> mocks = new ArrayList<>();
        Field field = Mockito.mock(Field.class);

        filter.filterCandidate(mocks, field, null);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField() {
        List<Object> mocks = new ArrayList<>();
        Object fieldInstance = Mockito.mock(Object.class);

        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldType() {
        List<Object> mocks = new ArrayList<>();
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        filter.filterCandidate(mocks, field, null);
    }

    @Test
    public void testFilterCandidate_NoMatches() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(AnotherClass.class));
        Field field = Mockito.mock(Field.class);
        field.getType().setAccessible(true);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Object.class));
        mocks.add(Mockito.mock(AnotherClass.class));
        Field field = Mockito.mock(Field.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mocks.get(0), result.getCandidate());
    }
}