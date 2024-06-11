import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

    @Test
    public void testFilterCandidate_singleMockPropertySetter_Success() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = getField("property");
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        assertSame(mocks.iterator().next(), result);
        verify(new BeanPropertySetter(fieldInstance, field)).set(mocks.iterator().next());
        verifyNoMoreInteractions(new BeanPropertySetter(fieldInstance, field));
        verifyZeroInteractions(new FieldSetter(fieldInstance, field));
    }

    @Test
    public void testFilterCandidate_singleMockPropertySetter_Failure() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = getField("property");
        Object fieldInstance = new Object();
        BeanPropertySetter setter = mock(BeanPropertySetter.class);
        when(setter.set(mocks.iterator().next())).thenReturn(false);

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        assertSame(mocks.iterator().next(), result);
        verify(setter).set(mocks.iterator().next());
        verify(new FieldSetter(fieldInstance, field)).set(mocks.iterator().next());
        verifyNoMoreInteractions(setter, new FieldSetter(fieldInstance, field));
    }

    @Test
    public void testFilterCandidate_singleMockFieldSetter_Success() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = getField("field");
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        assertSame(mocks.iterator().next(), result);
        verify(new FieldSetter(fieldInstance, field)).set(mocks.iterator().next());
        verifyNoMoreInteractions(new FieldSetter(fieldInstance, field));
        verifyZeroInteractions(new BeanPropertySetter(fieldInstance, field));
    }

    @Test
    public void testFilterCandidate_multipleMocks_NoInject() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = getField("property");
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        assertNull(result);
        verifyZeroInteractions(new BeanPropertySetter(fieldInstance, field), new FieldSetter(fieldInstance, field));
    }

    private Field getField(String name) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }

}