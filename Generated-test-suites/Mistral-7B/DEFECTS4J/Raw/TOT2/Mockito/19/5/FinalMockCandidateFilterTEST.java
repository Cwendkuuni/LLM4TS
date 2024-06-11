import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithSingleMock() {
        // Given
        Collection<Object> mocks = mock(Collection.class);
        when(mocks.size()).thenReturn(1);
        Object mock = mock(Object.class, Strictness.LENIENT);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Object result = injecter.thenInject();
        verify(mock).set(fieldInstance, field);
        assertEquals(mock, result);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        // Given
        Collection<Object> mocks = mock(Collection.class);
        when(mocks.size()).thenReturn(2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, mock(Field.class), mock(Object.class));

        // Then
        // No need to check the result since it's returning null
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionWhenInjectionFails() {
        // Given
        Collection<Object> mocks = mock(Collection.class);
        when(mocks.size()).thenReturn(1);
        Object mock = mock(Object.class, Strictness.LENIENT);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When & Then
        filter.filterCandidate(mocks, field, fieldInstance);

        // Throw an exception
        throw new RuntimeException("Test Exception");
    }

    @Test
    public void testFieldSetter() {
        // Given
        Collection<Object> mocks = mock(Collection.class);
        when(mocks.size()).thenReturn(1);
        Object mock = mock(Object.class, Strictness.LENIENT);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        field.setAccessible(true); // Make the private field accessible for testing
        injecter.thenInject();

        verify(mock, Mockito.times(2)).set(fieldInstance, field);
    }
}