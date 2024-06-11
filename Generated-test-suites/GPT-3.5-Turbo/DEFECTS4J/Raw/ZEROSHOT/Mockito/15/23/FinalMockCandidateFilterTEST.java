import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_withSingleMock_shouldInjectMock() throws Exception {
        // Arrange
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = FinalMockCandidateFilterTest.class.getDeclaredField("dummyField");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        boolean result = injecter.thenInject();

        assertTrue(result);
        assertSame(mocks.iterator().next(), field.get(fieldInstance));
    }

    @Test
    public void testFilterCandidate_withMultipleMocks_shouldNotInjectMock() throws Exception {
        // Arrange
        Collection<Object> mocks = Collections.nCopies(2, mock(Object.class));
        Field field = FinalMockCandidateFilterTest.class.getDeclaredField("dummyField");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        boolean result = injecter.thenInject();

        assertFalse(result);
        assertNull(field.get(fieldInstance));
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_withException_shouldThrowMockitoException() throws Exception {
        // Arrange
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = FinalMockCandidateFilterTest.class.getDeclaredField("dummyField");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        doThrow(Exception.class).when(mock(FieldSetter.class)).set(eq(fieldInstance), eq(field));

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        injecter.thenInject();
    }

    private Object dummyField;

}