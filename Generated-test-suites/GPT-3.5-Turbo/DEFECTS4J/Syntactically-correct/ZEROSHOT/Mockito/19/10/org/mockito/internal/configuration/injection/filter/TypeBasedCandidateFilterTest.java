import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        // Test when mock type matches field type
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("Mock1");
        mocks.add("Mock2");
        mocks.add("Mock3");

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        assertEquals(3, ((List<Object>) ongoingInjecter.getOngoingInjections()).size());

        // Test when mock type does not match field type
        Collection<Object> mocks2 = new ArrayList<>();
        mocks2.add("Mock1");
        mocks2.add(5);
        mocks2.add(true);

        OngoingInjecter ongoingInjecter2 = filter.filterCandidate(mocks2, field, fieldInstance);

        assertNotNull(ongoingInjecter2);
        assertEquals(1, ((List<Object>) ongoingInjecter2.getOngoingInjections()).size());

        // Test when next filter is not null
        MockCandidateFilter nextFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                assertEquals(3, mocks.size());
                assertEquals(field, TypeBasedCandidateFilterTest.class.getDeclaredField("mocks"));
                assertEquals(fieldInstance, new Object());
                return new OngoingInjecter(new ArrayList<>());
            }
        };

        TypeBasedCandidateFilter filter2 = new TypeBasedCandidateFilter(nextFilter);

        Collection<Object> mocks3 = new ArrayList<>();
        mocks3.add("Mock1");
        mocks3.add("Mock2");
        mocks3.add("Mock3");

        OngoingInjecter ongoingInjecter3 = filter2.filterCandidate(mocks3, field, fieldInstance);

        assertNotNull(ongoingInjecter3);
        assertEquals(0, ((List<Object>) ongoingInjecter3.getOngoingInjections()).size());
    }
}