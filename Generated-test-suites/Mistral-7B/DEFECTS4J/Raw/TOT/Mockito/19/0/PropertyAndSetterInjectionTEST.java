import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.util.ClassUtil;
import org.mockito.internal.util.reflection.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private MockCandidateFilter mockCandidateFilter;
    private ListUtil.Filter<Field> notFinalOrStatic;
    private SuperTypesLastSorter superTypesLastSorter;
    private Class<?> testClass;

    @Before
    public void setUp() {
        injectionStrategy = new PropertyAndSetterInjection();
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        notFinalOrStatic = new ListUtil.Filter<Field>() {
            public boolean isOut(Field object) {
                return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
            }
        };
        superTypesLastSorter = new SuperTypesLastSorter();
        testClass = TestClassUnderTest.class;
    }

    @Test
    public void testProcessInjection_FieldWithInjectMocks_InjectsFieldWithMockCandidateAndSetsIt() throws Exception {
        // Given
        Object testInstance = new Object();
        Field injectMocksField = ReflectionUtils.getDeclaredField(testClass, "fieldToInject");
        injectMocksField.setAccessible(true);
        injectMocksField.set(testInstance, null);

        Object mockCandidate = Mockito.mock(MockitoTestClass.class);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockCandidate);

        // When
        boolean injectionOccurred = injectionStrategy.processInjection(injectMocksField, testInstance, mockCandidates);

        // Then
        assertFalse("Field injection should have occurred", !injectionOccurred);
        assertNotNull("Injected field should not be null", injectMocksField.get(testInstance));
        verify(mockCandidate).someMethod();
    }

    @Test
    public void testProcessInjection_FieldWithoutInjectMocks_DoesNotInjectAnything() throws Exception {
        // Given
        Object testInstance = new Object();
        Field fieldToIgnore = ReflectionUtils.getDeclaredField(testClass, "fieldToIgnore");
        fieldToIgnore.setAccessible(true);

        // When
        boolean injectionOccurred = injectionStrategy.processInjection(fieldToIgnore, testInstance, emptySet());

        // Then
        assertFalse("Field injection should not have occurred", injectionOccurred);
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_FieldWithNoCandidates_ThrowsException() throws Exception {
        // Given
        Object testInstance = new Object();
        Field injectMocksField = ReflectionUtils.getDeclaredField(testClass, "fieldWithNoCandidates");
        injectMocksField.setAccessible(true);

        // When
        Set<Object> mockCandidates = new HashSet<>();
        injectionStrategy.processInjection(injectMocksField, testInstance, mockCandidates);

        // Then
        // Since this test case is expected to throw a MockitoException, no further assertions are needed here.
    }

    @Test
    public void testProcessInjection_FieldWithCandidatesButNoInjectableOne_DoesNotInjectAnything() throws Exception {
        // Given
        Object testInstance = new Object();
        Field injectMocksField = ReflectionUtils.getDeclaredField(testClass, "fieldWithCandidatesButNoInjectableOne");
        injectMocksField.setAccessible(true);

        Object mockCandidate1 = Mockito.mock(MockitoTestClass.class);
        Object mockCandidate2 = Mockito.mock(MockitoTestClass.class);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockCandidate1);
        mockCandidates.add(mockCandidate2);

        // When
        boolean injectionOccurred = injectionStrategy.processInjection(injectMocksField, testInstance, mockCandidates);

        // Then
        assertFalse("Field injection should not have occurred", injectionOccurred);
    }

    @Test
    public void testProcessInjection_FieldWithCandidatesButOnlyOneInjectableOne_InjectsIt() throws Exception {
        // Given
        Object testInstance = new Object();
        Field injectMocksField = ReflectionUtils.getDeclaredField(testClass, "fieldWithCandidatesButOnlyOneInjectableOne");
        injectMocksField.setAccessible(true);

        Object mockCandidate1 = Mockito.mock(MockitoTestClass.class);
        Object mockCandidate2 = Mockito.mock(MockitoTestClass.class);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockCandidate1);
        mockCandidates.add(mockCandidate2);

        // When
        boolean injectionOccurred = injectionStrategy.processInjection(injectMocksField, testInstance, mockCandidates);

        // Then
        assertFalse("Field injection should have occurred", !injectionOccurred);
        assertNotNull("Injected field should not be null", injectMocksField.get(testInstance));
        verify(mockCandidate1).someMethod();
        assertNull("Second mock candidate should not be injected", injectMocksField.get(testInstance));
    }

    @Test
    public void testInitializeInjectMocksField_FieldWithNoConstructor_ThrowsException() throws Exception {
        // Given
        Object testInstance = new Object();
        Field injectMocksField = ReflectionUtils.getDeclaredField(testClass, "fieldWithNoConstructor");
        injectMocksField.setAccessible(true);
        injectMocksField.set(testInstance, null);

        // When
        Set<Object> mockCandidates = new HashSet<>();

        // Then
        MockitoException exception = assertThrows(MockitoException.class, () -> injectionStrategy.initializeInjectMocksField(injectMocksField, testInstance));

        // Since this test case is expected to throw a MockitoException, no further assertions are needed here.
    }

    @Test
    public void testInitializeInjectMocksField_FieldWithConstructor_InitializesItWithConstructor() throws Exception {
        // Given
        Object testInstance = new Object();
        Field injectMocksField = ReflectionUtils.getDeclaredField(testClass, "fieldWithConstructor");
        injectMocksField.setAccessible(true);
        injectMocksField.set(testInstance, null);

        Object constructorResult = new Object();
        Class<?> fieldType = injectMocksField.getType();
        Constructor<?> fieldConstructor = fieldType.getConstructor();

        Set<Object> mockCandidates = new HashSet<>();

        // When
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(injectMocksField, testInstance);

        // Then
        assertNotNull("Report should not be null", report);
        assertEquals("Reported field instance should be the constructed one", constructorResult, report.fieldInstance());
    }

    @Test
    public void testInjectMockCandidates_ClassWithNoInjectableFields_ReturnsFalse() throws Exception {
        // Given
        Object testInstance = new Object();
        Class<?> awaitingInjectionClazz = testClass;

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, new HashSet<>(), testInstance);

        // Then
        assertFalse("No fields were injected", injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_ClassWithInjectableField_InjectsIt() throws Exception {
        // Given
        Object testInstance = new Object();
        Class<?> awaitingInjectionClazz = TestClassWithInjectableField.class;

        Object mockCandidate1 = Mockito.mock(MockitoTestClass.class);
        Object mockCandidate2 = Mockito.mock(MockitoTestClass.class);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockCandidate1);
        mockCandidates.add(mockCandidate2);

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, testInstance);

        // Then
        assertTrue("At least one field was injected", injectionOccurred);
        verify(mockCandidate1).someMethod();
        assertNull("Second mock candidate should not be injected", injectionStrategy.getFieldValue(awaitingInjectionClazz, testInstance, "fieldToInject"));
    }

    @Test
    public void testInjectMockCandidatesOnFields_ClassWithInjectableField_InjectsIt() throws Exception {
        // Given
        Object testInstance = new Object();
        Class<?> awaitingInjectionClazz = TestClassWithInjectableField.class;

        Object mockCandidate1 = Mockito.mock(MockitoTestClass.class);
        Object mockCandidate2 = Mockito.mock(MockitoTestClass.class);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockCandidate1);
        mockCandidates.add(mockCandidate2);

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidatesOnFields(mockCandidates, testInstance, false, orderedInstanceFieldsFrom(awaitingInjectionClazz));

        // Then
        assertTrue("At least one field was injected", injectionOccurred);
        verify(mockCandidate1).someMethod();
        assertNull("Second mock candidate should not be injected", injectionStrategy.getFieldValue(awaitingInjectionClazz, testInstance, "fieldToInject"));
    }

    @Test
    public void testInjectMockCandidatesOnFields_ClassWithInjectedField_DoesNotInjectAgain() throws Exception {
        // Given
        Object testInstance = new Object();
        Class<?> awaitingInjectionClazz = TestClassWithInjectableField.class;

        Object mockCandidate1 = Mockito.mock(MockitoTestClass.class);
        Object mockCandidate2 = Mockito.mock(MockitoTestClass.class);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockCandidate1);
        mockCandidates.add(mockCandidate2);

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidatesOnFields(mockCandidates, testInstance, true, orderedInstanceFieldsFrom(awaitingInjectionClazz));

        // Then
        assertFalse("No further fields were injected", injectionOccurred);
    }

    @Test
    public void testOrderedInstanceFieldsFrom_ClassWithFields_ReturnsThemInCorrectOrder() throws Exception {
        // Given
        Class<?> awaitingInjectionClazz = TestClassWithFields.class;

        // When
        List<Field> orderedInstanceFields = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Then
        Field expectedFirstField = ReflectionUtils.getDeclaredField(awaitingInjectionClazz, "field1");
        Field expectedSecondField = ReflectionUtils.getDeclaredField(awaitingInjectionClazz, "field2");

        // The order of the fields should be from the super-type to the sub-type and then by field name
        assertEquals("First field should be the one with the earliest declaration in the class hierarchy", expectedFirstField, orderedInstanceFields.get(0));
        assertEquals("Second field should be the one with the later declaration in the class hierarchy", expectedSecondField, orderedInstanceFields.get(1));
    }

    private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        return injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    }
}