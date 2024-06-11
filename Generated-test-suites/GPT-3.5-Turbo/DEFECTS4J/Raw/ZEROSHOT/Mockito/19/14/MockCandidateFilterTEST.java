import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;

    @Before
    public void setUp() {
        filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected,
                    Object fieldInstance) {
                return Mockito.mock(OngoingInjecter.class);
            }
        };
    }

    @Test
    public void testFilterCandidate() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException {
        Collection<Object> mocks = new ArrayList<>();
        Field testField = MockCandidateFilterTest.class.getDeclaredField("filter");
        Object instance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, testField, instance);

        assertNotNull(injecter);
    }

}