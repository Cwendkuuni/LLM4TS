import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
    }

    @Test
    public void filterCandidate_ValidMocksAndFields_CallsOngoingInjecter() {
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        verify(ongoingInjecter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void filterCandidate_EmptyMocks_ReturnsNull() {
        Collection<Object> mocks = Collections.emptySet();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertNull(result);
    }

    @Test
    public void filterCandidate_NullFieldToBeInjected_ReturnsNull() {
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, null, fieldInstance);

        Assert.assertNull(result);
    }

    @Test
    public void filterCandidate_NullFieldInstance_ReturnsNull() {
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, null);

        Assert.assertNull(result);
    }

    @Test
    public void filterCandidate_NullMocks_ReturnsNull() {
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(null, fieldToBeInjected, fieldInstance);

        Assert.assertNull(result);
    }
}