import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMockInjection_setProperty() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = null;
        Object fieldInstance = null;
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        BeanPropertySetter mockPropertySetter = mock(BeanPropertySetter.class);
        when(mockPropertySetter.set(any())).thenReturn(true);
        FieldSetter fieldSetter = mock(FieldSetter.class);

        // Act
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = actual.thenInject();

        // Assert
        assertNotNull(result);
        verify(mockPropertySetter, times(1)).set(any());
        verify(fieldSetter, never()).set(any());
    }

    @Test
    public void testFilterCandidate_singleMockInjection_setField() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = null;
        Object fieldInstance = null;
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        BeanPropertySetter mockPropertySetter = mock(BeanPropertySetter.class);
        when(mockPropertySetter.set(any())).thenReturn(false);
        FieldSetter fieldSetter = mock(FieldSetter.class);

        // Act
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = actual.thenInject();

        // Assert
        assertNotNull(result);
        verify(mockPropertySetter, times(1)).set(any());
        verify(fieldSetter, times(1)).set(any());
    }

    @Test
    public void testFilterCandidate_multipleMocksInjection_noInjection() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = null;
        Object fieldInstance = null;
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));

        // Act
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = actual.thenInject();

        // Assert
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_invalidFieldInjection_exceptionThrown() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = null;
        Object fieldInstance = null;
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        BeanPropertySetter mockPropertySetter = mock(BeanPropertySetter.class);
        when(mockPropertySetter.set(any())).thenThrow(new RuntimeException());
        FieldSetter fieldSetter = mock(FieldSetter.class);

        // Act
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = actual.thenInject();

        // Assert
        assertNull(result);
        verify(mockPropertySetter, times(1)).set(any());
        verify(fieldSetter, never()).set(any());
    }
}