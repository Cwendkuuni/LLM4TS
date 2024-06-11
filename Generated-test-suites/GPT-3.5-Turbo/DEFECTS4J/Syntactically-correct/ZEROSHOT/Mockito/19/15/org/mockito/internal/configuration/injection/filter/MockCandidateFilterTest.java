import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return mock(OngoingInjecter.class);
            }
        };
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(OngoingInjecter.class, ongoingInjecter.getClass());
    }

}