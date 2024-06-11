import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.MockInjectionStrategy;
import org.mockito.internal.configuration.injection.MockInjectionStrategy.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;

    @Before
    public void setup() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection_withInjectMocksFieldNotInitialized_shouldThrowException() {
        // Given
        Field injectMocksField = getField("field", DummyClass.class);
        DummyClass injectMocksFieldOwner = new DummyClass();
        Set<Object> mockCandidates = new HashSet<>();

        // When
        try {
            propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
            // Then
            fail("Expected MockitoException to be thrown");
        } catch(MockitoException e) {
            assertEquals("Cannot initialize field 'field' for @InjectMocks annotation", e.getMessage());
        }
    }

    @Test
    public void testProcessInjection_withInjectMocksFieldInitializationException_shouldThrowException() throws NoSuchFieldException {
        // Given
        Field injectMocksField = getField("field", DummyClass.class);
        DummyClass injectMocksFieldOwner = new DummyClass();
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, null);
        Set<Object> mockCandidates = new HashSet<>();

        // When
        try {
            propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
            // Then
            fail("Expected MockitoException to be thrown");
        } catch(MockitoException e) {
            assertEquals("Field initialisation threw exception for field 'field'", e.getMessage());
        }
    }

    @Test
    public void testProcessInjection_withNoFieldsToInject_shouldReturnFalse() throws NoSuchFieldException {
        // Given
        Field injectMocksField = getField("field", DummyClass.class);
        DummyClass injectMocksFieldOwner = new DummyClass();
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, injectMocksFieldOwner);
        Set<Object> mockCandidates = new HashSet<>();

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertFalse(result);
    }

    @Test
    public void testProcessInjection_withOneFieldToInject_shouldInjectMockBySetterMethod() throws NoSuchFieldException {
        // Given
        DependencyClass dependencyClass = mock(DependencyClass.class);
        Field injectMocksField = getField("field", DummyClass.class);
        DummyClass injectMocksFieldOwner = new DummyClass();
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, injectMocksFieldOwner);
        injectMocksFieldOwner.setDependency(dependencyClass);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(dependencyClass);

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
        verify(dependencyClass, times(1)).setFieldInjected(eq(injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_withOneFieldToInjectAndNoSetter_shouldInjectMockByField() throws NoSuchFieldException {
        // Given
        DependencyClass dependencyClass = mock(DependencyClass.class);
        Field injectMocksField = getField("field", DummyClass.class);
        DummyClass injectMocksFieldOwner = new DummyClass();
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, injectMocksFieldOwner);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(dependencyClass);

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
        assertEquals(dependencyClass, injectMocksFieldOwner.getField());
    }

    @Test
    public void testProcessInjection_withMultipleFieldsToInject_shouldInjectMocksInOrder() throws NoSuchFieldException {
        // Given
        DependencyClass dependencyClass1 = mock(DependencyClass.class);
        DependencyClass dependencyClass2 = mock(DependencyClass.class);
        Field injectMocksField = getField("field", DummyClass.class);
        DummyClass injectMocksFieldOwner = new DummyClass();
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, injectMocksFieldOwner);
        injectMocksFieldOwner.setDependency(dependencyClass1);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(dependencyClass1);
        mockCandidates.add(dependencyClass2);

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
        verify(dependencyClass1, times(1)).setFieldInjected(eq(injectMocksFieldOwner));
        verify(dependencyClass2, times(0)).setFieldInjected(eq(injectMocksFieldOwner));
        assertEquals(dependencyClass1, injectMocksFieldOwner.getField());
        assertTrue(mockCandidates.isEmpty());
        assertEquals(Collections.emptyList(), orderedInstanceFieldsFrom(DummyClass.class));
    }

    @Test
    public void testProcessInjection_withOneFieldToInjectAndMultipleMocks_shouldInjectFirstMockFound() throws NoSuchFieldException {
        // Given
        DependencyClass dependencyClass1 = mock(DependencyClass.class);
        DependencyClass dependencyClass2 = mock(DependencyClass.class);
        Field injectMocksField = getField("field", DummyClass.class);
        DummyClass injectMocksFieldOwner = new DummyClass();
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, injectMocksFieldOwner);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(dependencyClass1);
        mockCandidates.add(dependencyClass2);

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertTrue(result);
        verify(dependencyClass1, times(1)).setFieldInjected(eq(injectMocksFieldOwner));
        verify(dependencyClass2, times(0)).setFieldInjected(eq(injectMocksFieldOwner));
        assertEquals(dependencyClass1, injectMocksFieldOwner.getField());
        assertTrue(mockCandidates.isEmpty());
        assertEquals(Collections.emptyList(), orderedInstanceFieldsFrom(DummyClass.class));
    }

    @Test
    public void testProcessInjection_withOneFieldToInjectAndNoMockFound_shouldNotInject() throws NoSuchFieldException {
        // Given
        Field injectMocksField = getField("field", DummyClass.class);
        DummyClass injectMocksFieldOwner = new DummyClass();
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, injectMocksFieldOwner);
        Set<Object> mockCandidates = new HashSet<>();

        // When
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        assertFalse(result);
        assertNull(injectMocksFieldOwner.getField());
    }

    private Field getField(String fieldName, Class<?> clazz) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            fail("Field '" + fieldName + "' not found");
            return null;
        }
    }

    // Supporting classes

    class DummyClass {
        private DependencyClass field;

        public void setDependency(DependencyClass dependencyClass) {
            this.field = dependencyClass;
        }

        public DependencyClass getField() {
            return field;
        }
    }

    class DependencyClass {
        public void setFieldInjected(DummyClass dummyClass) {}
    }

}