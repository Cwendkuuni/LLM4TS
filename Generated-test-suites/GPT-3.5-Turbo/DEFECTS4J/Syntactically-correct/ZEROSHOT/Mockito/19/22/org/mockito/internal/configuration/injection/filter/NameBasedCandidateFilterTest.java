import static org.junit.Assert.*;
import org.junit.Test;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter next = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return null;
            }
        };

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field();

        // Test case 1: Mocks size > 1, field name equals mock name
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        field.setName("mock1");
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, null);
        assertNotNull(result);
        
        // Test case 2: Mocks size > 1, field name does not equal mock name
        Object mock3 = new Object();
        Object mock4 = new Object();
        mocks.clear();
        mocks.add(mock3);
        mocks.add(mock4);
        field.setName("mock1");

        result = filter.filterCandidate(mocks, field, null);
        assertNotNull(result);

        // Test case 3: Mocks size <= 1
        mocks.clear();
        mocks.add(mock1);

        result = filter.filterCandidate(mocks, field, null);
        assertNotNull(result);
    }
}