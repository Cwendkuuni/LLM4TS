import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        // Prepare mocks
        List<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        
        // Prepare field and field instance
        Field field = getClass().getDeclaredField("mocks");
        Object fieldInstance = this;
        
        // Prepare expected results
        List<Object> expected = new ArrayList<>();
        expected.add(mock1);
        
        // Create NameBasedCandidateFilter
        MockCandidateFilter next = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return null;
            }
        };
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        
        // Test filterCandidate method
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Verify the result
        Assert.assertNotNull(result);
        Assert.assertEquals(expected, result.getMocks());
        Assert.assertEquals(field, result.getField());
        Assert.assertEquals(fieldInstance, result.getFieldInstance());
    }
}