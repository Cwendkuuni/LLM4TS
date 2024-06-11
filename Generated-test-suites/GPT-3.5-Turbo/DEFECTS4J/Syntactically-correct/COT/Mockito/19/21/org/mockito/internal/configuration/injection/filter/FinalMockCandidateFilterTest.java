
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = mock(Object.class);
        Field field = getField("fieldName");
        Object fieldInstance = new Object();

        // Act
        Object result = filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance).thenInject();

        // Assert
        assertEquals(mock, result);
        verify(new BeanPropertySetter(fieldInstance, field)).set(mock);
        verifyNoInteractions(new FieldSetter(fieldInstance, field));
        verifyNoInteractions(new Reporter());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = getField("fieldName");
        Object fieldInstance = new Object();

        // Act
        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        assertNull(result);
        verifyNoInteractions(new BeanPropertySetter(fieldInstance, field));
        verifyNoInteractions(new FieldSetter(fieldInstance, field));
        verifyNoInteractions(new Reporter());
    }

    @Test
    public void testFilterCandidate_exceptionDuringInjection() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = mock(Object.class);
        Field field = getField("fieldName");
        Object fieldInstance = new Object();
        Reporter reporter = mock(Reporter.class);

        // Act
        try {
            filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance).thenInject();
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            // Assert
            assertEquals("Expected exception message", e.getMessage());
            verify(new Reporter()).cannotInjectDependency(field, mock, e);
        }
    }

    private Field getField(String name) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}