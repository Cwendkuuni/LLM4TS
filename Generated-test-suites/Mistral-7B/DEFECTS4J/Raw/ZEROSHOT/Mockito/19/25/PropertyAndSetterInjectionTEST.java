package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.*;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.TestUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private MockCandidateFilter mockCandidateFilter;
    private ListUtil.Filter<Field> notFinalOrStatic;
    private SuperTypesLastSorter superTypesLastSorter;

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        notFinalOrStatic = new ListUtil.Filter<Field>() {
            @Override
            public boolean isOut(Field object) {
                return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
            }
        };
        superTypesLastSorter = new SuperTypesLastSorter();
    }

    @Test
    public void testProcessInjection_InjectMocksAnnotatedField_MockCandidateFoundByTypeAndSetter() {
        Class<?> targetClass = new ClassA();
        Field injectMocksField = TestUtils.getDeclaredField(targetClass, "injectMocksField");
        Object injectMocksFieldOwner = Mockito.mock(ClassB.class);
        Set<Object> mockCandidates = newHashSet(Mockito.mock(ClassC.class), Mockito.mock(ClassD.class));

        FieldInitializationReport report = new FieldInitializationReport(injectMocksField, injectMocksFieldOwner);

        assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
        assertEquals(1, mockCandidates.size());
        assertNotNull(report.getMock());
        assertNull(report.getException());
    }

    @Test
    public void testProcessInjection_InjectMocksAnnotatedField_MockCandidateNotFound() {
        Class<?> targetClass = new ClassA();
        Field injectMocksField = TestUtils.getDeclaredField(targetClass, "injectMocksField");
        Object injectMocksFieldOwner = Mockito.mock(ClassB.class);
        Set<Object> mockCandidates = newHashSet(Mockito.mock(ClassE.class));

        FieldInitializationReport report = new FieldInitializationReport(injectMocksField, injectMocksFieldOwner);

        assertFalse(propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
        assertEquals(1, mockCandidates.size());
        assertNull(report.getMock());
        assertNull(report.getException());
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_InjectMocksAnnotatedField_MockCandidateInitializationFailed() {
        Class<?> targetClass = new ClassA();
        Field injectMocksField = TestUtils.getDeclaredField(targetClass, "injectMocksField");
        Object injectMocksFieldOwner = Mockito.mock(ClassB.class);
        Set<Object> mockCandidates = newHashSet(Mockito.mock(ClassF.class));

        FieldInitializationReport report = new FieldInitializationReport(injectMocksField, injectMocksFieldOwner);

        propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
    }

    @Test
    public void testProcessInjection_InjectMocksAnnotatedField_InjectedFieldRemovedFromList() {
        Class<?> targetClass = new ClassA();
        Field injectMocksField = TestUtils.getDeclaredField(targetClass, "injectMocksField");
        Object injectMocksFieldOwner = Mockito.mock(ClassB.class);
        Set<Object> mockCandidates = newHashSet(Mockito.mock(ClassC.class), Mockito.mock(ClassD.class));

        propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertEquals(1, mockCandidates.size());
        assertFalse(mockCandidates.contains(mockCandidates.iterator().next()));
    }

    @Test
    public void testInjectMockCandidates_MockCandidateFoundByTypeAndSetter() {
        Class<?> awaitingInjectionClazz = new ClassA();
        Set<Object> mocks = newHashSet(Mockito.mock(ClassC.class), Mockito.mock(ClassD.class));
        Object instance = Mockito.mock(ClassB.class);

        assertTrue(propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
        assertEquals(1, mocks.size());
    }

    @Test
    public void testInjectMockCandidates_MockCandidateNotFound() {
        Class<?> awaitingInjectionClazz = new ClassA();
        Set<Object> mocks = newHashSet(Mockito.mock(ClassE.class));
        Object instance = Mockito.mock(ClassB.class);

        assertFalse(propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
        assertEquals(1, mocks.size());
    }

    @Test
    public void testInjectMockCandidatesOnFields_MockCandidateFoundByTypeAndSetter() {
        Class<?> awaitingInjectionClazz = new ClassA();
        Set<Object> mocks = newHashSet(Mockito.mock(ClassC.class), Mockito.mock(ClassD.class));
        Object instance = Mockito.mock(ClassB.class);
        List<Field> orderedInstanceFields = Arrays.asList(new Field[] {TestUtils.getDeclaredField(awaitingInjectionClazz, "field1"), TestUtils.getDeclaredField(awaitingInjectionClazz, "field2")});

        assertTrue(propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        assertEquals(1, mocks.size());
    }

    @Test
    public void testInjectMockCandidatesOnFields_MockCandidateNotFound() {
        Class<?> awaitingInjectionClazz = new ClassA();
        Set<Object> mocks = newHashSet(Mockito.mock(ClassE.class));
        Object instance = Mockito.mock(ClassB.class);
        List<Field> orderedInstanceFields = Arrays.asList(new Field[] {TestUtils.getDeclaredField(awaitingInjectionClazz, "field1"), TestUtils.getDeclaredField(awaitingInjectionClazz, "field2")});

        assertFalse(propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        assertEquals(1, mocks.size());
    }

    @Test
    public void testOrderedInstanceFieldsFrom_OrderedFields() {
        Class<?> awaitingInjectionClazz = new ClassA();
        List<Field> expectedOrderedFields = Arrays.asList(new Field[] {TestUtils.getDeclaredField(awaitingInjectionClazz, "field1"), TestUtils.getDeclaredField(awaitingInjectionClazz, "field2")});

        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertEquals(expectedOrderedFields.size(), orderedInstanceFields.size());
        for (int i = 0; i < expectedOrderedFields.size(); i++) {
            assertEquals(expectedOrderedFields.get(i).getName(), orderedInstanceFields.get(i).getName());
        }
    }
}

class ClassA {
    @InjectMocks
    private Field injectMocksField;
    private Field field1;
    private Field field2;
}

class ClassB {}

class ClassC {}

class ClassD {}

class ClassE {}

class ClassF {
    public ClassF() throws Exception {
        throw new Exception("Initialization failed");
    }
}