import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    @Mock
    private Object mockObject;

    private PropertyAndSetterInjection propertyAndSetterInjection;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        propertyAndSetterInjection = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection_ValidInjectMocksField_InjectsMocks() {
        // Arrange
        Class<?> injectMocksFieldOwner = this.getClass();
        Field injectMocksField = getField(injectMocksFieldOwner, "mockObject");

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockObject);

        // Act
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Assert
        assertTrue(injectionOccurred);
        assertNotNull(getFieldValue(injectMocksFieldOwner, "mockObject"));
        verify(mockObject).getClass(); // Verify that the mock was injected
    }

    @Test
    public void testProcessInjection_InvalidInjectMocksField_DoesNotInjectMocks() {
        // Arrange
        Class<?> injectMocksFieldOwner = this.getClass();
        Field injectMocksField = getField(injectMocksFieldOwner, "mockObject");

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new Object());

        // Act
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Assert
        assertFalse(injectionOccurred);
        assertNull(getFieldValue(injectMocksFieldOwner, "mockObject"));
        verifyZeroInteractions(mockObject); // Verify that the mock was not injected
    }

    @Test
    public void testProcessInjection_NoMockCandidates_DoesNotInjectMocks() {
        // Arrange
        Class<?> injectMocksFieldOwner = this.getClass();
        Field injectMocksField = getField(injectMocksFieldOwner, "mockObject");

        Set<Object> mockCandidates = new HashSet<>();

        // Act
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Assert
        assertFalse(injectionOccurred);
        assertNull(getFieldValue(injectMocksFieldOwner, "mockObject"));
        verifyZeroInteractions(mockObject); // Verify that the mock was not injected
    }

    @Test
    public void testInitializeInjectMocksField_ValidField_InitializesField() {
        // Arrange
        Class<?> fieldOwner = this.getClass();
        Field field = getField(fieldOwner, "mockObject");

        // Act
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(field, this);

        // Assert
        assertNotNull(report);
        assertEquals(fieldOwner, report.fieldClass());
        assertNotNull(report.fieldInstance());
    }

    @Test
    public void testInjectMockCandidates_ValidClass_InjectsMocks() {
        // Arrange
        Class<?> awaitingInjectionClazz = this.getClass();
        Object instance = this;

        Set<Object> mocks = new HashSet<>();
        mocks.add(mockObject);

        // Act
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Assert
        assertTrue(injectionOccurred);
        assertNotNull(getFieldValue(awaitingInjectionClazz, "mockObject"));
        verify(mockObject).getClass(); // Verify that the mock was injected
    }

    @Test
    public void testInjectMockCandidates_InvalidClass_DoesNotInjectMocks() {
        // Arrange
        Class<?> awaitingInjectionClazz = this.getClass();
        Object instance = this;

        Set<Object> mocks = new HashSet<>();
        mocks.add(new Object());

        // Act
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Assert
        assertFalse(injectionOccurred);
        assertNull(getFieldValue(awaitingInjectionClazz, "mockObject"));
        verifyZeroInteractions(mockObject); // Verify that the mock was not injected
    }

    @Test
    public void testInjectMockCandidates_NoMocks_DoesNotInjectMocks() {
        // Arrange
        Class<?> awaitingInjectionClazz = this.getClass();
        Object instance = this;

        Set<Object> mocks = new HashSet<>();

        // Act
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Assert
        assertFalse(injectionOccurred);
        assertNull(getFieldValue(awaitingInjectionClazz, "mockObject"));
        verifyZeroInteractions(mockObject); // Verify that the mock was not injected
    }

    @Test
    public void testOrderedInstanceFieldsFrom_ValidClass_ReturnsOrderedFields() {
        // Arrange
        Class<?> awaitingInjectionClazz = this.getClass();

        // Act
        List<Field> orderedFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Assert
        assertFalse(orderedFields.isEmpty());
        Field previousField = null;
        for (Field field : orderedFields) {
            if (previousField != null) {
                assertTrue(compareFields(previousField, field));
            }
            previousField = field;
        }
    }

    private Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            fail("Field not found: " + fieldName);
            return null;
        }
    }

    private Object getFieldValue(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Field not found or inaccessible: " + fieldName);
            return null;
        }
    }

    private boolean compareFields(Field field1, Field field2) {
        return field1.getDeclaringClass().equals(field2.getDeclaringClass())
                && field1.getName().compareTo(field2.getName()) < 0;
    }

}