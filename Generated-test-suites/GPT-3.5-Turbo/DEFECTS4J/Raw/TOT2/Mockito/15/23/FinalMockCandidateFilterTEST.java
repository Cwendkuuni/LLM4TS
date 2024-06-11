package org.mockito.internal.configuration.injection;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_shouldInject() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = SomeClass.class.getDeclaredField("someField");
        SomeClass fieldInstance = new SomeClass();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertTrue(injecter.thenInject());
        assertEquals(mock(Object.class), fieldInstance.getSomeField());
    }

    @Test
    public void testFilterCandidate_multipleMocks_shouldNotInject() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = SomeClass.class.getDeclaredField("someField");
        SomeClass fieldInstance = new SomeClass();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertFalse(injecter.thenInject());
        assertNull(fieldInstance.getSomeField());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exceptionThrown_shouldThrowMockitoException() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = SomeClass.class.getDeclaredField("someField");
        SomeClass fieldInstance = new SomeClass();

        // Mock behavior to throw exception when injecting
        when(field.get(anyObject())).thenThrow(new IllegalAccessException());

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);
    }
}