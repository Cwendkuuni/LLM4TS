import static org.mockito.Mockito.*;
import org.junit.*;
import java.util.Collection;
import java.lang.reflect.Field;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        Assert.assertNotNull(result);
        Assert.assertEquals(mocks.iterator().next(), result);

    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        Assert.assertNull(result);

    }
}