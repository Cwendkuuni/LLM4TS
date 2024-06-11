import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter finalMockCandidateFilter;
    private Field field;
    private Object fieldInstance;
    private Collection<Object> mocks;

    @Before
    public void setup() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
        field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        fieldInstance = new Object();
        mocks = new ArrayList<>();
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        Object mock1 = mock(Object.class);
        mocks.add(mock1);

        OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertEquals(mock1, injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithNoMocks() {
        OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertNull(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateExceptionHandling() {
        Object mock1 = mock(Object.class);
        mocks.add(mock1);

        OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, null);

        assertNotNull(injecter);
        assertNull(injecter.thenInject());
    }

}