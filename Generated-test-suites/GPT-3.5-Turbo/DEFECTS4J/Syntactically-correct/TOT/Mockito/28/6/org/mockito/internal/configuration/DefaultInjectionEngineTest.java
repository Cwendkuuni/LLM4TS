import org.junit.Test;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    @Test
    public void injectMocksOnFields_InjectsMocksAndInitializesFields() {
        // Arrange
        DefaultInjectionEngine engine = new DefaultInjectionEngine();
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        Field field = mock(Field.class);
        injectMocksFields.add(field);

        // Act
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(field, times(1)).getType();
        verify(field, times(1)).isAssignableFrom(any(Class.class));
        verify(field, times(1)).getName();
        verify(field, times(1)).setAccessible(true);
    }

    @Test
    public void injectMockCandidate_InjectsMockIfOneCandidateExists() {
        // Arrange
        DefaultInjectionEngine engine = new DefaultInjectionEngine();
        Class<?> awaitingInjectionClazz = Object.class;
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = new Object();

        Field field = mock(Field.class);
        when(field.getType()).thenReturn(Object.class);
        when(field.isAssignableFrom(Object.class)).thenReturn(true);
        when(field.getName()).thenReturn("mockObject");

        mocks.add("mockObject");

        // Act
        engine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        // Assert
        verify(field, times(1)).getType();
        verify(field, times(1)).isAssignableFrom(Object.class);
        verify(field, times(1)).getName();
        verify(field, times(1)).setAccessible(true);
        verify(field, times(1)).getDeclaringClass();
        verify(field, times(1)).set(fieldInstance, "mockObject");
    }

    @Test
    public void orderedInstanceFieldsFrom_OrdersFieldsFromSubTypeToSuperType() {
        // Arrange
        DefaultInjectionEngine engine = new DefaultInjectionEngine();
        Class<?> awaitingInjectionClazz = Object.class;

        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field field3 = mock(Field.class);

        when(field1.getType()).thenReturn(String.class);
        when(field2.getType()).thenReturn(Integer.class);
        when(field3.getType()).thenReturn(Object.class);

        awaitingInjectionClazz = new ClassMock(field1, field2, field3);

        Field[] expectedFields = new Field[]{field3, field2, field1};

        // Act
        Field[] orderedFields = engine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Assert
        assertArrayEquals(expectedFields, orderedFields);
    }

    // Helper class for mocking classes
    private class ClassMock extends Class<Object> {
        private Field[] fields;

        public ClassMock(Field... fields) {
            this.fields = fields;
        }

        @Override
        public Field[] getDeclaredFields() {
            return fields;
        }
    }
}