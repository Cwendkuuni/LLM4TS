import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {
    @Mock
    private Field mockField;

    @Mock
    private Object mockInstance;

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return null;
            }
        };
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mockInstance);

        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, mockField, mockInstance);

        assertNull(injecter);
        verifyZeroInteractions(mockField);
        verifyZeroInteractions(mockInstance);
    }
}