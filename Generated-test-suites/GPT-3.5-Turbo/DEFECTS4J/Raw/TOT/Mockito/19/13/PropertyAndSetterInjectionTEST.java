import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.MockInjectionStrategy;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {
    private PropertyAndSetterInjection injectionStrategy;
    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        injectionStrategy = new PropertyAndSetterInjection();
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
    }

    @Test
    public void testProcessInjection_successfulInjectionWithSetter() {
        // Arrange
        PropertyAndSetterInjectionTest.InjectMocksTestClass testObj = new PropertyAndSetterInjectionTest.InjectMocksTestClass();
        Field field = getFieldByName(testObj, "mockField");

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add("mockObject");

        // Act
        boolean result = injectionStrategy.processInjection(field, testObj, mockCandidates);

        // Assert
        assertTrue(result);
        assertEquals("mockObject", testObj.getMockField());
    }

    @Test
    public void testProcessInjection_successfulInjectionWithField() {
        // Arrange
        PropertyAndSetterInjectionTest.InjectMocksTestClass testObj = new PropertyAndSetterInjectionTest.InjectMocksTestClass();
        Field field = getFieldByName(testObj, "mockFieldWithNoSetter");

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add("mockObject");

        // Act
        boolean result = injectionStrategy.processInjection(field, testObj, mockCandidates);

        // Assert
        assertTrue(result);
        assertEquals("mockObject", testObj.mockFieldWithNoSetter);
    }

    @Test
    public void testProcessInjection_fieldNotInitialized_withNoArgConstructor() {
        // Arrange
        PropertyAndSetterInjectionTest.InjectMocksTestClass testObj = new PropertyAndSetterInjectionTest.InjectMocksTestClass();
        Field field = getFieldByName(testObj, "mockFieldNotInitialized");

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add("mockObject");

        // Act
        boolean result = injectionStrategy.processInjection(field, testObj, mockCandidates);

        // Assert
        assertTrue(result);
        assertEquals("mockObject", testObj.getMockFieldNotInitialized());
    }

    @Test
    public void testProcessInjection_failedInitializationWithException() {
        // Arrange
        PropertyAndSetterInjectionTest.InjectMocksTestClass testObj = new PropertyAndSetterInjectionTest.InjectMocksTestClass();
        Field field = getFieldByName(testObj, "mockFieldException");

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add("mockObject");

        // Act
        boolean result = injectionStrategy.processInjection(field, testObj, mockCandidates);

        // Assert
        assertFalse(result);
        assertNotNull(testObj.getMockFieldException());
        verify(new Reporter()).fieldInitialisationThrewException(field, MockitoException.class);
    }

    @Test
    public void testProcessInjection_multipleCandidatesForInjection() {
        // Arrange
        PropertyAndSetterInjectionTest.InjectMocksTestClass testObj = new PropertyAndSetterInjectionTest.InjectMocksTestClass();
        Field field = getFieldByName(testObj, "mockFieldMultipleCandidates");

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add("mockObject1");
        mockCandidates.add("mockObject2");

        // Act
        boolean result = injectionStrategy.processInjection(field, testObj, mockCandidates);

        // Assert
        assertFalse(result);
        assertEquals("mockObject1", testObj.getMockFieldMultipleCandidates());
        verify(new Reporter()).cannotInitializeForInjectMocksAnnotation(field.getName(), MockitoException.class);
    }

    @Test
    public void testInjectMockCandidates_onFieldInjection() throws NoSuchFieldException {
        // Arrange
        PropertyAndSetterInjectionTest.InjectMocksTestClass testObj = new PropertyAndSetterInjectionTest.InjectMocksTestClass();
        Field field = getFieldByName(testObj, "mockField");

        Set<Object> mocks = new HashSet<>();
        mocks.add("mockObject");

        boolean injectionOccurred = false;

        // Act
        boolean result = injectionStrategy.injectMockCandidatesOnFields(mocks, testObj, injectionOccurred, Collections.singletonList(field));

        // Assert
        assertTrue(result);
        assertEquals("mockObject", testObj.getMockField());
        assertFalse(mocks.contains("mockObject"));
    }

    @Test
    public void testInjectMockCandidates_noInjectionOccurred() throws NoSuchFieldException {
        // Arrange
        PropertyAndSetterInjectionTest.InjectMocksTestClass testObj = new PropertyAndSetterInjectionTest.InjectMocksTestClass();
        Field field = getFieldByName(testObj, "mockField");

        Set<Object> mocks = new HashSet<>();

        boolean injectionOccurred = false;

        // Act
        boolean result = injectionStrategy.injectMockCandidatesOnFields(mocks, testObj, injectionOccurred, Collections.singletonList(field));

        // Assert
        assertFalse(result);
    }

    @Test
    public void testOrderedInstanceFieldsFrom_orderedFieldsByHierarchy() {
        // Arrange
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjectionTest.InjectMocksTestClass.class;
        List<Field> expectedFields = new ArrayList<>();
        expectedFields.add(getFieldByName(awaitingInjectionClazz, "mockFieldWithNoSetter"));
        expectedFields.add(getFieldByName(awaitingInjectionClazz, "mockFieldNotInitialized"));
        expectedFields.add(getFieldByName(awaitingInjectionClazz, "mockFieldMultipleCandidates"));
        expectedFields.add(getFieldByName(awaitingInjectionClazz, "mockFieldException"));
        expectedFields.add(getFieldByName(awaitingInjectionClazz, "mockField"));

        // Act
        List<Field> orderedFields = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Assert
        assertEquals(expectedFields, orderedFields);
    }

    private Field getFieldByName(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private static class InjectMocksTestClass {
        private String mockField;
        private String mockFieldWithNoSetter;
        private String mockFieldNotInitialized;
        private String mockFieldException;
        private String mockFieldMultipleCandidates;

        public String getMockField() {
            return mockField;
        }

        public String getMockFieldWithNoSetter() {
            return mockFieldWithNoSetter;
        }

        public String getMockFieldNotInitialized() {
            return mockFieldNotInitialized;
        }

        public String getMockFieldException() {
            return mockFieldException;
        }

        public String getMockFieldMultipleCandidates() {
            return mockFieldMultipleCandidates;
        }
    }
}