import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    @Mock
    private Collection<Object> mocks;

    @Mock
    private Field fieldToBeInjected;

    private Object fieldInstance;

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockCandidateFilter = new MockCandidateFilterImpl();
    }

    @Test
    public void testFilterCandidate() {
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertEquals(0, ongoingInjecter.getMocks().size());
        assertEquals(fieldToBeInjected, ongoingInjecter.getFieldToBeInjected());
        assertEquals(fieldInstance, ongoingInjecter.getFieldInstance());
        verifyNoMoreInteractions(mocks);
    }

    class MockCandidateFilterImpl implements MockCandidateFilter {
        public OngoingInjecter filterCandidate(
                Collection<Object> mocks,
                Field fieldToBeInjected,
                Object fieldInstance
        ) {
            OngoingInjecter ongoingInjecter = new OngoingInjecter(mocks, fieldToBeInjected, fieldInstance);
            ongoingInjecter.getMocks();
            ongoingInjecter.getFieldToBeInjected();
            ongoingInjecter.getFieldInstance();
            return ongoingInjecter;
        }
    }

    class OngoingInjecter {
        private Collection<Object> mocks;
        private Field fieldToBeInjected;
        private Object fieldInstance;

        public OngoingInjecter(
                Collection<Object> mocks,
                Field fieldToBeInjected,
                Object fieldInstance
        ) {
            this.mocks = mocks;
            this.fieldToBeInjected = fieldToBeInjected;
            this.fieldInstance = fieldInstance;
        }

        public Collection<Object> getMocks() {
            return mocks;
        }

        public Field getFieldToBeInjected() {
            return fieldToBeInjected;
        }

        public Object getFieldInstance() {
            return fieldInstance;
        }
    }
}