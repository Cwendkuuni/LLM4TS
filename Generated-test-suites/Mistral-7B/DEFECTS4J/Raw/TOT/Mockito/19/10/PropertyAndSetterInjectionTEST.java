import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injection;
    private Class<?> testClass;
    private Object testObject;
    private List<Field> orderedFields;
    private Set<Object> mockCandidates;

    @Before
    public void setUp() {
        testClass = new TestClass();
        testObject = new TestClass();
        injection = new PropertyAndSetterInjection();

        orderedFields = orderedInstanceFieldsFrom(testClass);
        mockCandidates = newMockSafeHashSet(getMockCandidates(testClass));
    }

    @Test
    public void testProcessInjection_Success() {
        Field injectMocksField = testClass.getField("injectMocksField");
        Object injectMocksFieldOwner = testObject;

        FieldInitializationReport report = injection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
        boolean injectionOccurred = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertNotNull(report);
        assertFalse(mockCandidates.isEmpty());
        assertNull(injectMocksField.get(injectMocksFieldOwner));
        assertNotNull(injectMocksField.get(testObject));
        verify(mockCandidates.iterator()).hasNext();
    }

    @Test
    public void testProcessInjection_Failure_NoMockCandidates() {
        Field injectMocksField = testClass.getField("injectMocksField");
        Object injectMocksFieldOwner = testObject;

        mockCandidates = new HashSet<>();

        injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertFalse(injectMocksField.isAccessible());
        assertNull(injectMocksField.get(testObject));
    }

    @Test
    public void testProcessInjection_Failure_MultipleMockCandidates() {
        Field injectMocksField = testClass.getField("injectMocksField");
        Object injectMocksFieldOwner = testObject;

        Set<Object> mockCandidates = newHashSetWithTwoMocks(getMockCandidates(testClass));

        injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertFalse(injectMocksField.isAccessible());
        assertNull(injectMocksField.get(testObject));
    }

    @Test
    public void testProcessInjection_Failure_MockCandidateIsStatic() {
        Field injectMocksField = testClass.getField("injectMocksField");
        Object injectMocksFieldOwner = testObject;

        Set<Object> mockCandidates = new MockSafeHashSet(getMockCandidateThatIsStatic(testClass));

        injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertFalse(injectMocksField.isAccessible());
        assertNull(injectMocksField.get(testObject));
    }

    @Test
    public void testProcessInjection_Failure_MockCandidateIsFinal() {
        Field injectMocksField = testClass.getField("injectMocksField");
        Object injectMocksFieldOwner = testObject;

        Set<Object> mockCandidates = new MockSafeHashSet(getMockCandidateThatIsFinal(testClass));

        injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertFalse(injectMocksField.isAccessible());
        assertNull(injectMocksField.get(testObject));
    }

    @Test
    public void testProcessInjection_Failure_MockCandidateIsNotInstanceOfType() {
        Field injectMocksField = testClass.getField("injectMocksField");
        Object injectMocksFieldOwner = testObject;

        Set<Object> mockCandidates = new MockSafeHashSet(getMockCandidateThatIsNotInstanceOfType(testClass));

        injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertFalse(injectMocksField.isAccessible());
        assertNull(injectMocksField.get(testObject));
    }

    @Test
    public void testProcessInjection_Failure_FieldIsFinalOrStatic() {
        Field injectMocksField = testClass.getField("injectMocksField");
        Object injectMocksFieldOwner = testObject;

        injectMocksField.setAccessible(true);
        injectMocksField.setFinal(true);

        Set<Object> mockCandidates = newMockSafeHashSet(getMockCandidates(testClass));

        injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertFalse(injectMocksField.isAccessible());
        assertNull(injectMocksField.get(testObject));
    }

    @Test
    public void testProcessInjection_Failure_FieldIsNotWritable() {
        Field injectMocksField = testClass.getField("injectMocksField");
        Object injectMocksFieldOwner = testObject;

        injectMocksField.setAccessible(true);

        Set<Object> mockCandidates = newMockSafeHashSet(getMockCandidates(testClass));

        injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertFalse(injectMocksField.isAccessible());
        assertNull(injectMocksField.get(testObject));
    }

    @Test
    public void testProcessInjection_Failure_MockCandidateIsNull() {
        Field injectMocksField = testClass.getField("injectMocksField");
        Object injectMocksFieldOwner = testObject;

        Set<Object> mockCandidates = new HashSet<>();

        injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertFalse(injectMocksField.isAccessible());
        assertNull(injectMocksField.get(testObject));
    }

    @Test
    public void testProcessInjection_Failure_MockInitializationThrowsException() {
        Field injectMocksField = testClass.getField("injectMocksField");
        Object injectMocksFieldOwner = testObject;

        Set<Object> mockCandidates = newMockSafeHashSet(getMockCandidateThatThrowsException(testClass));

        injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertFalse(injectMocksField.isAccessible());
        assertNull(injectMocksField.get(testObject));
    }

    private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        return injection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    }

    private Set<Object> getMockCandidates(Class<?> clazz) {
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(clazz));
        mocks.add(spy(clazz));
        return mocks;
    }

    private Object getMockCandidateThatIsStatic(Class<?> clazz) {
        return clazz.getDeclaredField("STATIC_MOCK").get(null);
    }

    private Object getMockCandidateThatIsFinal(Class<?> clazz) {
        return clazz.getDeclaredField("FINAL_MOCK").get(null);
    }

    private Object getMockCandidateThatIsNotInstanceOfType(Class<?> clazz) {
        return mock(OtherClass.class);
    }

    private Object getMockCandidateThatThrowsException(Class<?> clazz) {
        return new ExceptionThrowingMock();
    }

    private class TestClass {
        @InjectMocks
        private TestDependency testDependency;

        private Field injectMocksField;

        static class ExceptionThrowingMock {
            public void init() {
                throw new RuntimeException("Mock initialization failed");
            }
        }
    }
}