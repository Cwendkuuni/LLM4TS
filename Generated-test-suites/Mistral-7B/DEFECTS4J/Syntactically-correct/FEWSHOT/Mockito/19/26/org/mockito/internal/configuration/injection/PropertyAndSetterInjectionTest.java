package org.mockito.internal.configuration.injection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.StubbingType;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

public class PropertyAndSetterInjectionTest {

    private Reporter reporter;
    private PropertyAndSetterInjection injectionStrategy;
    private Object testObject;

    @Before
    public void setup() {
        reporter = mock(Reporter.class);
        injectionStrategy = new PropertyAndSetterInjection();
        MockitoAnnotations.initMocks(this);
        testObject = new TestClass();
    }

    @After
    public void teardown() {
        reset(reporter);
    }

    @Test
    public void testProcessInjection_hierarchyDependsOnFieldAnnotatedWithInjectMocks() {
        // Given
        Field field = TestClass.class.getDeclaredField("fieldAnnotatedWithInjectMocks");
        Object mock = mock(DependsOnInjectMocksField.class, StubbingType.LENIENT);
        Set<Object> mockCandidates = newMockSafeHashSet(mock);

        // When
        boolean injectionOccurred = injectionStrategy.processInjection(field, testObject, mockCandidates);

        // Then
        verify(reporter).fieldInitialisationSuccessful(any(Field.class), any(Object.class), any(Object.class));
        assertFalse(injectionOccurred);
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void testProcessInjection_hierarchyDoesNotDependsOnFieldAnnotatedWithInjectMocks() {
        // Given
        Field field = TestClass.class.getDeclaredField("notAnnotatedField");
        Object mock = mock(TestDependentClass.class, StubbingType.LENIENT);
        Set<Object> mockCandidates = newMockSafeHashSet(mock);

        // When
        boolean injectionOccurred = injectionStrategy.processInjection(field, testObject, mockCandidates);

        // Then
        verifyNoInteractions(reporter);
        assertFalse(injectionOccurred);
    }

    @Test
    public void testProcessInjection_fieldIsAlreadyInitialized() {
        // Given
        Field field = TestClass.class.getDeclaredField("fieldAnnotatedWithInjectMocks");
        Object mock = mock(DependsOnInjectMocksField.class, StubbingType.LENIENT);
        TestClass testClass = new TestClass();
        TestClass.setFieldValue(testClass, "fieldAnnotatedWithInjectMocks", mock);
        Set<Object> mockCandidates = newMockSafeHashSet(mock);

        // When
        boolean injectionOccurred = injectionStrategy.processInjection(field, testClass, mockCandidates);

        // Then
        verifyNoMoreInteractions(reporter);
        assertFalse(injectionOccurred);
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_initializationThrowsException() {
        // Given
        Field field = TestClass.class.getDeclaredField("fieldAnnotatedWithInjectMocks");
        Set<Object> mockCandidates = newMockSafeHashSet();

        // When
        injectionStrategy.processInjection(field, testObject, mockCandidates);

        // Then
        throw new AssertionError(); // this test case is not tested, it only checks for the expected exception
    }

    @Test
    public void testInjectMockCandidates_noMocksAvailable() {
        // Given
        Class<?> awaitingInjectionClazz = TestClassWithNoMocks.class;
        Set<Object> mockCandidates = newMockSafeHashSet();
        Object instance = new TestClassWithNoMocks();

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, instance);

        // Then
        verifyNoInteractions(reporter);
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_mockCandidateFoundByType() {
        // Given
        Class<?> awaitingInjectionClazz = TestClassWithTypeBasedMock.class;
        Set<Object> mockCandidates = newMockSafeHashSet(mock(TestTypeBasedMock.class, StubbingType.LENIENT));
        Object instance = new TestClassWithTypeBasedMock();

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, instance);

        // Then
        verify(reporter, times(1)).fieldInitialisationSuccessful(any(Field.class), any(Object.class), any(Object.class));
        verify(reporter, times(1)).injectMock(any(Object.class), eq(instance));
        assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_mockCandidateFoundByName() {
        // Given
        Class<?> awaitingInjectionClazz = TestClassWithNameBasedMock.class;
        Set<Object> mockCandidates = newMockSafeHashSet(mock(TestNameBasedMock.class, StubbingType.LENIENT));
        Object instance = new TestClassWithNameBasedMock();

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, instance);

        // Then
        verify(reporter, times(1)).fieldInitialisationSuccessful(any(Field.class), any(Object.class), any(Object.class));
        verify(reporter, times(1)).injectMock(eq("nameBasedMock"), eq(instance));
        assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_multipleMockCandidatesByType() {
        // Given
        Class<?> awaitingInjectionClazz = TestClassWithMultipleTypeBasedMocks.class;
        Set<Object> mockCandidates = newMockSafeHashSet(mock(TestTypeBasedMock1.class, StubbingType.LENIENT), mock(TestTypeBasedMock2.class, StubbingType.LENIENT));
        Object instance = new TestClassWithMultipleTypeBasedMocks();

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, instance);

        // Then
        verify(reporter, times(1)).fieldInitialisationSuccessful(any(Field.class), any(Object.class), any(Object.class));
        verify(reporter, times(1)).injectMock(eq(TestTypeBasedMock1.class), eq(instance));
        assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_multipleMockCandidatesByName() {
        // Given
        Class<?> awaitingInjectionClazz = TestClassWithMultipleNameBasedMocks.class;
        Set<Object> mockCandidates = newMockSafeHashSet(mock(TestNameBasedMock1.class, StubbingType.LENIENT), mock(TestNameBasedMock2.class, StubbingType.LENIENT));
        Object instance = new TestClassWithMultipleNameBasedMocks();

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, instance);

        // Then
        verify(reporter, times(1)).fieldInitialisationSuccessful(any(Field.class), any(Object.class), any(Object.class));
        verify(reporter, times(1)).injectMock(eq("nameBasedMock1"), eq(instance));
        assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_noMockCandidateFound() {
        // Given
        Class<?> awaitingInjectionClazz = TestClassWithNoMocksToInject.class;
        Set<Object> mockCandidates = newMockSafeHashSet();
        Object instance = new TestClassWithNoMocksToInject();

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, instance);

        // Then
        verifyNoInteractions(reporter);
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_noMockCandidateFoundButUserWillProvideDependencies() {
        // Given
        Class<?> awaitingInjectionClazz = TestClassWithNoMocksToInjectButDependencies.class;
        Set<Object> mockCandidates = newMockSafeHashSet();
        Object instance = new TestClassWithNoMocksToInjectButDependencies();

        // When
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, instance);

        // Then
        verifyNoInteractions(reporter);
        assertFalse(injectionOccurred);
    }

    private static class TestClass {
        private static void setFieldValue(Object o, String fieldName, Object value) {
            try {
                Field field = o.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(o, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private static class DependsOnInjectMocksField {
            @InjectMocks
            private DependentField dependentField;
        }

        private static class DependentField {
        }

        @InjectMocks
        private static class TestClassWithInjectMocks {
            private DependentField fieldAnnotatedWithInjectMocks;
        }

        private static class TestClassWithNoMocksToInject extends TestClassWithInjectMocks {
            // empty
        }

        private static class TestClassWithNoMocksToInjectButDependencies extends TestClassWithInjectMocks {
            @Inject
            private DependentField dependentField;
        }

        private static class TestClassWithTypeBasedMock {
            @Mock
            private TestTypeBasedMock testTypeBasedMock;
        }

        private static class TestClassWithNameBasedMock {
            @Mock
            private TestNameBasedMock testNameBasedMock;
        }

        private static class TestClassWithMultipleTypeBasedMocks {
            @Mock
            private TestTypeBasedMock1 testTypeBasedMock1;

            @Mock
            private TestTypeBasedMock2 testTypeBasedMock2;
        }

        private static class TestClassWithMultipleNameBasedMocks {
            @Mock
            private TestNameBasedMock1 testNameBasedMock1;

            @Mock
            private TestNameBasedMock2 testNameBasedMock2;
        }

        private static class TestClassWithNoMocks {
            // empty
        }
    }
}