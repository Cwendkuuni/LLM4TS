import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private Object testObject;

    @Before
    public void setUp() throws Exception {
        injectionStrategy = new PropertyAndSetterInjection();
        Class<?> testClass = TestClassWithInjectMocks.class;
        Field testField = testClass.getDeclaredField("injectMocksField");
        testField.setAccessible(true);
        testObject = testClass.newInstance();
        testField.set(testObject, new Object());
    }

    @Test
    public void testProcessInjectionWithNoMocks() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        assertFalse(injectionStrategy.processInjection(testField, testObject, mockCandidates));
    }

    @Test
    public void testProcessInjectionWithMockCandidates() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(new MockitoMock(), new AnotherMockitoMock());
        FieldInitializationReport report = injectionStrategy.processInjection(testField, testObject, mockCandidates);

        assertEquals(TestClassWithInjectedField.class, report.fieldClass());
        assertNotNull(report.fieldInstance());

        verify(new MockitoMock()).mock();
        verifyNoMoreInteractions(new AnotherMockitoMock());
    }

    @Test(expected = InvocationTargetException.class)
    public void testProcessInjectionWithException() throws Exception {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(new ThrowingMockitoMock());
        injectionStrategy.processInjection(testField, testObject, mockCandidates);
    }

    @Test
    public void testInitializeInjectMocksFieldWithNoException() throws Exception {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(new MockitoMock());
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(testField, testObject);

        assertEquals(TestClassWithInjectedField.class, report.fieldClass());
        assertNotNull(report.fieldInstance());

        verify(new MockitoMock()).mock();
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksFieldWithException() throws Exception {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(new ThrowingMockitoMock());
        injectionStrategy.initializeInjectMocksField(testField, testObject);
    }

    @Test
    public void testInjectMockCandidatesOnFieldsWithNoInjection() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(new MockitoMock());
        Class<?> awaitingInjectionClazz = TestClassWithNestedMocks.class;
        List<Field> orderedInstanceFields = List.of(awaitingInjectionClazz.getDeclaredFields()[0]);

        assertFalse(injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, new Object()));
    }

    @Test
    public void testInjectMockCandidatesOnFieldsWithInjection() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(new MockitoMock(), new AnotherMockitoMock());
        Class<?> awaitingInjectionClazz = TestClassWithNestedMocks.class;
        List<Field> orderedInstanceFields = List.of(awaitingInjectionClazz.getDeclaredFields()[0]);

        verify(new MockitoMock()).mock();
        verifyNoMoreInteractions(new AnotherMockitoMock());

        injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, new Object());

        verify(new AnotherMockitoMock()).mock();
    }

    @Test
    public void testInjectMockCandidatesOnFieldsWithException() {
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(new ThrowingMockitoMock());
        Class<?> awaitingInjectionClazz = TestClassWithNestedMocks.class;
        List<Field> orderedInstanceFields = List.of(awaitingInjectionClazz.getDeclaredFields()[0]);

        try {
            injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, new Object());
            fail();
        } catch (MockitoException e) {
            // pass
        }

        verify(new MockitoMock(), never()).mock();
        verify(new ThrowingMockitoMock(), never()).mock();
    }

    private static class TestClassWithInjectMocks {
        @InjectMocks
        private static Object injectMocksField;
    }

    private static class TestClassWithInjectedField {
    }

    private static class TestClassWithNestedMocks {
        private Object nestedField;
    }

    private static class MockitoMock {
        @SuppressWarnings("PMD.EmptyMethod")
        public void mock() {
        }
    }

    private static class AnotherMockitoMock {
        @SuppressWarnings("PMD.EmptyMethod")
        public void mock() {
        }
    }

    private static class ThrowingMockitoMock {
        @SuppressWarnings("PMD.EmptyMethod")
        public void mock() throws Exception {
            throw new Exception();
        }
    }
}