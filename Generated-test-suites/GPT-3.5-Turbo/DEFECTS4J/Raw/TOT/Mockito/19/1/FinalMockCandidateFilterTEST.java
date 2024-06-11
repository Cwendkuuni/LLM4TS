import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter finalMockCandidateFilter;

    @Before
    public void setup() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void whenOneMockIsAvailable_setFieldUsingBeanPropertySetter() {
        ClassWithField mockFieldInstance = mock(ClassWithField.class);
        Field field = getFieldFromClass(ClassWithField.class);
        Object mock = mock(Object.class);
        Collection<Object> mockCollection = Collections.singleton(mock);

        finalMockCandidateFilter.filterCandidate(mockCollection, field, mockFieldInstance).thenInject();

        verify(mockFieldInstance).setMockObject(mock);
    }

    @Test
    public void whenOneMockIsAvailable_setFieldUsingFieldSetter() {
        ClassWithField mockFieldInstance = mock(ClassWithField.class);
        Field field = getFieldFromClass(ClassWithField.class);
        Object mock = mock(Object.class);
        Collection<Object> mockCollection = Collections.singleton(mock);

        when(new BeanPropertySetter(mockFieldInstance, field).set(mock)).thenReturn(false);

        finalMockCandidateFilter.filterCandidate(mockCollection, field, mockFieldInstance).thenInject();

        verify(mockFieldInstance).setMockFieldValue(mock);
    }

    @Test
    public void whenMultipleMocksAreAvailable_doNotSetField() {
        ClassWithField mockFieldInstance = mock(ClassWithField.class);
        Field field = getFieldFromClass(ClassWithField.class);
        Collection<Object> mockCollection = Collections.singleton(new Object());
        Collection<Object> multipleMocks = Arrays.asList(new Object(), new Object());

        finalMockCandidateFilter.filterCandidate(multipleMocks, field, mockFieldInstance).thenInject();

        verifyZeroInteractions(mockFieldInstance);
    }

    @Test
    public void whenExceptionOccurs_reportException() {
        ClassWithField mockFieldInstance = mock(ClassWithField.class);
        Field field = getFieldFromClass(ClassWithField.class);
        Object mock = mock(Object.class);
        Collection<Object> mockCollection = Collections.singleton(mock);

        doThrow(RuntimeException.class).when(new BeanPropertySetter(mockFieldInstance, field)).set(mock);

        final Object result = finalMockCandidateFilter.filterCandidate(mockCollection, field, mockFieldInstance).thenInject();

        verify(new Reporter()).cannotInjectDependency(field, mock, RuntimeException.class);
        assertNull(result);
    }

    private Field getFieldFromClass(Class<?> clazz) {
        try {
            return clazz.getDeclaredField("mockField");
        } catch (NoSuchFieldException e) {
            fail();
            return null;
        }
    }

    private static class ClassWithField {
        private Object mockField;

        public void setMockFieldValue(Object mock) {
            this.mockField = mock;
        }

        public void setMockObject(Object mock) {
            setMockFieldValue(mock);
        }
    }
}