package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoConfigurationException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.runners.MockitoJUnitRunnerDelegate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private MockCandidateFilter mockCandidateFilter;
    private ListUtil.Filter<Field> notFinalOrStatic;
    private SuperTypesLastSorter superTypesLastSorter;
    private FieldInitializationReport report;
    private Field field;
    private Object fieldOwner;

    @Rule
    public MockitoJUnitRunnerDelegate mockitoJUnitRunnerDelegate = new MockitoJUnitRunnerDelegate();

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        notFinalOrStatic = new ListUtil.Filter<Field>() {
            public boolean isOut(Field object) {
                return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
            }
        };
        superTypesLastSorter = new SuperTypesLastSorter();
    }

    @Test
    public void testProcessInjection_fieldAnnotatedWithInjectMocks_success() {
        Class<?> clazz = MockitoJUnitRunner.class;
        Object instance = MockitoJUnitRunner.class;
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(Mockito.mock(Clazz1.class), Mockito.mock(Clazz2.class));
        Field injectMocksField = Mockito.fieldColors().getField(clazz, "mocks");

        assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, instance, mockCandidates));

        assertNotNull(injectMocksField.get(instance));
        assertTrue(mockCandidates.isEmpty());
    }

    @Test(expected = MockitoConfigurationException.class)
    public void testProcessInjection_fieldAnnotatedWithInjectMocks_failure() {
        Class<?> clazz = MockitoJUnitRunner.class;
        Object instance = MockitoJUnitRunner.class;
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(Mockito.mock(Clazz1.class), Mockito.mock(Clazz2.class));
        Field injectMocksField = Mockito.fieldColors().getField(clazz, "nonExistentField");

        propertyAndSetterInjection.processInjection(injectMocksField, instance, mockCandidates);
    }

    @Test
    public void testInitializeInjectMocksField_success() {
        Class<?> clazz = MockitoJUnitRunner.class;
        Object instance = MockitoJUnitRunner.class;
        Field injectMocksField = Mockito.fieldColors().getField(clazz, "mocks");

        report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, instance);

        assertNotNull(report);
        assertNotNull(report.fieldInstance());
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksField_failure() {
        Class<?> clazz = MockitoJUnitRunner.class;
        Object instance = MockitoJUnitRunner.class;
        Field injectMocksField = Mockito.fieldColors().getField(clazz, "nonExistentField");

        propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, instance);
    }

    @Test
    public void testInjectMockCandidates_noMocksToInject() {
        Class<?> awaitingInjectionClazz = Object.class;
        Object instance = new Object();
        Set<Object> mocks = Sets.newMockSafeHashSet();

        assertFalse(propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
    }

    @Test
    public void testInjectMockCandidates_oneMockToInject() {
        Class<?> awaitingInjectionClazz = Clazz1.class;
        Object instance = new ClazzWithMocks();
        Set<Object> mocks = Sets.newMockSafeHashSet(Mockito.mock(Clazz1.class));

        assertTrue(propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));

        assertNotNull(instance.getClass1Field);
        assertTrue(mocks.isEmpty());
    }

    @Test
    public void testInjectMockCandidates_multipleMocksToInject() {
        Class<?> awaitingInjectionClazz = Clazz2.class;
        Object instance = new ClazzWithMocks();
        Set<Object> mocks = Sets.newMockSafeHashSet(Mockito.mock(Clazz1.class), Mockito.mock(Clazz2.class));

        assertTrue(propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));

        assertNotNull(instance.getClass1Field);
        assertNotNull(instance.getClass2Field);
        assertTrue(mocks.isEmpty());
    }

    @Test
    public void testInjectMockCandidatesOnFields_noMocksToInject() {
        Class<?> awaitingInjectionClazz = Object.class;
        Object instance = new Object();
        List<Field> orderedInstanceFields = Arrays.asList(Mockito.fieldColors().getField(awaitingInjectionClazz, "nonExistentField"));

        assertFalse(propertyAndSetterInjection.injectMockCandidatesOnFields(Collections.emptySet(), instance, false, orderedInstanceFields));
    }

    @Test
    public void testInjectMockCandidatesOnFields_oneMockToInject() {
        Class<?> awaitingInjectionClazz = Clazz2.class;
        Object instance = new ClazzWithMocks();
        List<Field> orderedInstanceFields = Arrays.asList(instance.getClass2Field);

        assertTrue(propertyAndSetterInjection.injectMockCandidatesOnFields(Collections.singleton(Mockito.mock(Clazz1.class)), instance, false, orderedInstanceFields));

        assertNotNull(instance.getClass1Field);
        assertNotNull(instance.getClass2Field);
        assertTrue(orderedInstanceFields.isEmpty());
    }

    @Test
    public void testInjectMockCandidatesOnFields_multipleMocksToInject() {
        Class<?> awaitingInjectionClazz = Clazz3.class;
        Object instance = new ClazzWithMocks();
        List<Field> orderedInstanceFields = Arrays.asList(instance.getClass3Field1, instance.getClass3Field2);

        assertTrue(propertyAndSetterInjection.injectMockCandidatesOnFields(Sets.newMockSafeHashSet(Mockito.mock(Clazz1.class), Mockito.mock(Clazz2.class)), instance, false, orderedInstanceFields));

        assertNotNull(instance.getClass1Field);
        assertNotNull(instance.getClass2Field);
        assertNotNull(instance.getClass3Field1);
        assertNotNull(instance.getClass3Field2);
        assertTrue(orderedInstanceFields.isEmpty());
    }

    private static class Clazz1 {
        // empty
    }

    private static class Clazz2 {
        private Object class1Field;

        @InjectMocks
        public void setClass1Field(Object class1) {
            this.class1Field = class1;
        }
    }

    private static class Clazz3 {
        private Object class1Field;
        private Object class2Field;
        private Object class3Field1;
        private Object class3Field2;

        @InjectMocks
        public void setClass3Field1(Object class3Field1) {
            this.class3Field1 = class3Field1;
        }

        @InjectMocks
        public void setClass3Field2(Object class3Field2) {
            this.class3Field2 = class3Field2;
        }
    }

    private static class ClazzWithMocks {
        @InjectMocks
        public ClazzWithMocks() {
            // empty constructor
        }

        @InjectMocks
        private Object class1Field;
        private Object class2Field;
        private Object class3Field1;
        private Object class3Field2;

        @Inject
        private void setClass1Field(Object class1) {
            this.class1Field = class1;
        }

        @Inject
        private void setClass2Field(Object class2) {
            this.class2Field = class2;
        }

        @Inject
        private void setClass3Field1(Object class3) {
            this.class3Field1 = class3;
        }

        @Inject
        private void setClass3Field2(Object class3) {
            this.class3Field2 = class3;
        }
    }
}