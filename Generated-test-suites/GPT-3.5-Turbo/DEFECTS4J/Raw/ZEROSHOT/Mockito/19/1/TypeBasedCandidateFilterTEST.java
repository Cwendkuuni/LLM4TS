import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {
    
    private TypeBasedCandidateFilter filter;
    
    @Before
    public void setUp() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return null;
            }
        };
        
        filter = new TypeBasedCandidateFilter(mockCandidateFilter);
    }
    
    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = getTypeBasedCandidateFilterField(); // Create the field to test with
        
        Object fieldInstance = new Object(); // Create the fieldInstance to test with
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertNull(ongoingInjecter);
    }
    
    private Field getTypeBasedCandidateFilterField() {
        try {
            return TypeBasedCandidateFilter.class.getDeclaredField("next");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}