import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.AnnotationEngine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        // Prepare mocks
        List<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        // Prepare filter
        MockCandidateFilter filter = new MockCandidateFilterImpl();
        
        // Filter candidates
        OngoingInjecter injecter = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        // Verify results
        verify(fieldToBeInjected).getName();
        verify(fieldInstance).getClass();
    }
    
    class MockCandidateFilterImpl implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
            OngoingInjecter injecter = mock(OngoingInjecter.class);
            return injecter;
        }
    }
}