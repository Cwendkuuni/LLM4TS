package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.reflection.FieldAccessOrder;
import org.mockito.internal.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldUtil;
import org.mockito.internal.util.reflection.ReflectionUtils;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.ReflectionUtils.findAnnotatedElements;

public class DefaultInjectionEngineTest {

    @Rule
    public MockitoJUnit mockitoJUnitRule = new MockitoJUnit();

    private DefaultInjectionEngine defaultInjectionEngine;
    private Object testClassInstance;

    @Before
    public void setUp() {
        defaultInjectionEngine = new DefaultInjectionEngine();
        testClassInstance = createTestClassInstance();
    }

    @Test
    public void testInjectMocksOnFields() {
        Set<Field> injectMocksFields = findAnnotatedElements(testClassInstance.getClass(), AnnotatedElement.class, InjectMocks.class).stream()
                .map(e -> (Field) e).collect(Collectors.toSet());
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(ClassA.class));
        mocks.add(mock(ClassB.class));

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        for (Field field : injectMocksFields) {
            Object injectMocksFieldInstance = field.get(testClassInstance);
            assertEquals(mocks.toArray(new Object[0])[0], injectMocksFieldInstance);
        }
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFieldsWithException() {
        Set<Field> injectMocksFields = findAnnotatedElements(testClassInstance.getClass(), AnnotatedElement.class, InjectMocks.class).stream()
                .map(e -> (Field) e).collect(Collectors.toSet());
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(ClassA.class));
        mocks.add(mock(ClassB.class));
        mocks.add(mock(ClassC.class));

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        for (Field field : injectMocksFields) {
            Object injectMocksFieldInstance = field.get(testClassInstance);
            assertEquals(mocks.toArray(new Object[0])[0], injectMocksFieldInstance);
        }

        verify(defaultInjectionEngine).newFieldInitializer(eq(testClassInstance), any(Field.class)).initialize();
    }

    @Test
    public void testInjectMockCandidate() {
        Class<?> awaitingInjectionClazz = ClassD.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(ClassA.class));
        mocks.add(mock(ClassB.class));
        Object fieldInstance = createFieldInstance(awaitingInjectionClazz);

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verify(mocks).size();
        verifyZeroInteractions(awaitingInjectionClazz);
    }

    @Test
    public void testInjectMockCandidateWithMockFoundByName() {
        Class<?> awaitingInjectionClazz = ClassD.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(ClassA.class));
        mocks.add(mock(ClassB.class));
        mocks.add(mock(ClassC.class));
        mocks.add(mock(ClassD.class));

        Object fieldInstance = createFieldInstance(awaitingInjectionClazz);

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verify(mocks).get(3).equals(fieldInstance);
        verifyZeroInteractions(awaitingInjectionClazz);
    }

    @Test
    public void testInjectMockCandidateWithMockFoundByType() {
        Class<?> awaitingInjectionClazz = ClassD.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(ClassA.class));
        mocks.add(mock(ClassB.class));
        mocks.add(mock(ClassC.class));
        mocks.add(mock(ClassD.class));

        Object fieldInstance = createFieldInstance(awaitingInjectionClazz);

        when(mockCandidateFilter.filterCandidate(eq(mocks), any(Field.class), any())).thenReturn(mock(Field.class));

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verify(mocks).get(3).equals(fieldInstance);
        verifyZeroInteractions(awaitingInjectionClazz);
    }

    @Test
    public void testInjectMockCandidateWithNoMockFound() {
        Class<?> awaitingInjectionClazz = ClassD.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(ClassA.class));
        mocks.add(mock(ClassB.class));
        mocks.add(mock(ClassC.class));

        Object fieldInstance = createFieldInstance(awaitingInjectionClazz);

        when(mockCandidateFilter.filterCandidate(eq(mocks), any(Field.class), any())).thenReturn(null);

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verifyZeroInteractions(awaitingInjectionClazz);
        verifyZeroInteractions(mockCandidateFilter);
    }

    @Test
    public void testInjectMockCandidateWithPropertySetter() {
        Class<?> awaitingInjectionClazz = ClassD.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(ClassA.class));
        mocks.add(mock(ClassB.class));
        mocks.add(mock(ClassC.class));

        Object fieldInstance = createFieldInstance(awaitingInjectionClazz);
        Field field = ReflectionUtils.findField(awaitingInjectionClazz, "fieldD");
        Method setter = ReflectionUtils.findMethod(awaitingInjectionClazz, "setFieldD", field.getType());

        when(mockCandidateFilter.filterCandidate(eq(mocks), any(Field.class), any())).thenReturn(mock(Field.class));
        when(fieldInstance.getClass()).thenReturn(awaitingInjectionClazz);
        when(setter.invoke(fieldInstance, any())).thenAnswer(i -> fieldInstance);

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verify(mocks).get(3).equals(fieldInstance);
        verifyZeroInteractions(awaitingInjectionClazz);
    }

    @Test
    public void testInjectMockCandidateWithFieldInjection() {
        Class<?> awaitingInjectionClazz = ClassD.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(ClassA.class));
        mocks.add(mock(ClassB.class));
        mocks.add(mock(ClassC.class));

        Object fieldInstance = createFieldInstance(awaitingInjectionClazz);
        Field field = ReflectionUtils.findField(awaitingInjectionClazz, "fieldD");

        when(mockCandidateFilter.filterCandidate(eq(mocks), any(Field.class), any())).thenReturn(null);
        when(FieldUtil.isAccessible(field)).thenReturn(true);
        when(FieldUtil.get(field, fieldInstance)).thenReturn(mock(ClassD.class));

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verify(mocks).get(3).equals(fieldInstance);
        verifyZeroInteractions(awaitingInjectionClazz);
    }

    @Test
    public void testInjectMockCandidateWithNoMockAndNoException() {
        Class<?> awaitingInjectionClazz = ClassD.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(ClassA.class));
        mocks.add(mock(ClassB.class));

        Object fieldInstance = createFieldInstance(awaitingInjectionClazz);

        when(mockCandidateFilter.filterCandidate(eq(mocks), any(Field.class), any())).thenReturn(null);

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verifyZeroInteractions(mockCandidateFilter);
    }

    private Object createTestClassInstance() {
        return ReflectionUtils.newInstance(TestClass.class);
    }

    private Object createFieldInstance(Class<?> clazz) {
        return ReflectionUtils.newInstance(clazz);
    }

    private static class TestClass {
        @InjectMocks
        private ClassA classA;

        @InjectMocks
        private ClassB classB;

        @InjectMocks
        private ClassC classC;

        @InjectMocks
        private ClassD classD;

        private ClassD fieldD;

        public void setFieldD(ClassD fieldD) {
            this.fieldD = fieldD;
        }
    }

    private static class ClassA {
    }

    private static class ClassB {
    }

    private static class ClassC {
    }

    private static class ClassD {
    }
}