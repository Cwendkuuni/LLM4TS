import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<Object>();
        
        // Test when mocks size is 0
        Field field1 = new Field();
        Object fieldInstance1 = new Object();
        filter.filterCandidate(mocks, field1, fieldInstance1);

        // Test when mocks size is 1
        Object mock = new Object();
        mocks.add(mock);
        Field field2 = new Field();
        Object fieldInstance2 = new Object();
        filter.filterCandidate(mocks, field2, fieldInstance2);

        // Test when mocks size > 1
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        Field field3 = new Field();
        Object fieldInstance3 = new Object();
        filter.filterCandidate(mocks, field3, fieldInstance3);
    }
}