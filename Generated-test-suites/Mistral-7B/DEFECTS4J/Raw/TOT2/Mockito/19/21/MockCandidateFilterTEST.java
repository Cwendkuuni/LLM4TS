import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = Mockito.spy(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullCollection() {
        // Given
        Collection<Object> mocks = null;
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);

        // When
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldToBeInjected() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);

        // When
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = null;

        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);

        // When
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNotNull(result);
    }

}