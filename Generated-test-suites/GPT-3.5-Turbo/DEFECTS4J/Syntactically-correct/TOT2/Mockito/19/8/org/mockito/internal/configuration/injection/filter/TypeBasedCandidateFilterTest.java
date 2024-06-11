import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter candidateFilter;

    @Before
    public void setUp() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return new OngoingInjecter(null);
            }
        };
        candidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add(123);
        
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter ongoingInjecter = candidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        assertNotNull(ongoingInjecter);
        assertTrue(ongoingInjecter.getMocks().isEmpty());
    }

    @Test
    public void testFilterCandidateWithMatchingType() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add(new String("mock2"));
        
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter ongoingInjecter = candidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        assertNotNull(ongoingInjecter);
        assertTrue(ongoingInjecter.getMocks().size() == 2);
    }
}