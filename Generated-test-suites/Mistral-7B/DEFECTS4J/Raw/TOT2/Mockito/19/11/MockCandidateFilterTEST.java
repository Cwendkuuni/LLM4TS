import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Field;

import static org.junit.Assert.assertSame;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = Mockito.spy(Mockito.any(MockCandidateFilter.class));
        mocks = new ArrayList<>();
        fieldToBeInjected = Mockito.spy(Mockito.any(Field.class));
        fieldInstance = Mockito.mock(Object.class);
    }

    @Test
    public void testFilterCandidate() {
        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(Mockito.mock(OngoingInjecter.class));

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(Mockito.mock(OngoingInjecter.class), result);
    }
}