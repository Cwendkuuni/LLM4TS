import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;

    @Before
    public void setUp() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = SomeClass.class.getDeclaredField("fieldName");
        SomeClass fieldInstance = new SomeClass();

        when(mock1.toString()).thenReturn("fieldName");
        when(mock2.toString()).thenReturn("mock");

        assertEquals(1, filter.filterCandidate(mocks, field, fieldInstance).size());
    }

    // Additional test cases for edge cases and exceptions can be added here

}