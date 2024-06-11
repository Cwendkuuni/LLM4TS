package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.reflection.ReflectUtils.getDeclaredFields;
import static org.mockito.internal.util.reflection.ReflectUtils.setFieldValue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidate;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.TestFields;
import org.mockito.internal.util.reflection.TestObjects;
import org.mockito.internal.util.reflection.TestTypes;

public class PropertyAndSetterInjectionTest {

    private static final Class<?> TEST_CLASS = TestClassUnderTest.class;

    private MockCandidateFilter mockCandidateFilter;
    private ListUtil.Filter<Field> notFinalOrStatic;
    private PropertyAndSetterInjection propertyAndSetterInjection;

    @Before
    public void setUp() {
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        notFinalOrStatic = new ListUtil.Filter<Field>() {
            public boolean isOut(Field object) {
                return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
            }
        };
        propertyAndSetterInjection = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection_withNoInjectMocksAnnotatedField_noInjectionOccurs() {
        // given
        Object instance = TestObjects.newObject();
        Class<?> notInjectMocksClass = TestTypes.notInjectMocksClass();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(Mockito.mock(notInjectMocksClass));

        // when
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(null, instance, mockCandidates);

        // then
        assertFalse(injectionOccurred);
    }

    @Test
    public void testProcessInjection_withInjectMocksAnnotatedField_injectionOccurs() {
        // given
        Object instance = TestObjects.newObject();
        Field injectMocksField = TestFields.injectMocksAnnotatedField();
        Object injectMocksFieldOwner = TestObjects.newObject();
        setFieldValue(injectMocksFieldOwner, injectMocksField, instance);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(Mockito.mock(TestTypes.injectMocksType()));

        // when
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // then
        assertTrue(injectionOccurred);
    }

    @Test
    public void testInitializeInjectMocksField_withNoConstructor_throwsException() {
        // given
        Object instance = TestObjects.newObject();
        Field injectMocksField = TestFields.injectMocksAnnotatedField();
        Object injectMocksFieldOwner = TestObjects.newObject();

        // when
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

        // then
        assertNull(report);
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksField_withConstructorThatThrowsException_throwsException() {
        // given
        Object instance = TestObjects.newObject();
        Field injectMocksField = TestFields.injectMocksAnnotatedField();
        Object injectMocksFieldOwner = TestObjects.newObject();

        // when
        propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
    }

    @Test
    public void testInjectMockCandidates_noMockCandidatesFound_noInjectionOccurs() {
        // given
        Object instance = TestObjects.newObject();
        Class<?> awaitingInjectionClazz = TestTypes.awaitingInjectionClazz();
        Set<Object> mockCandidates = new HashSet<>();

        // when
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, instance);

        // then
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_oneMockCandidateFound_injectionOccurs() {
        // given
        Object instance = TestObjects.newObject();
        Class<?> awaitingInjectionClazz = TestTypes.awaitingInjectionClazz();
        Set<Object> mockCandidates = new HashSet<>(Arrays.asList(Mockito.mock(TestTypes.mockCandidateType())));

        // when
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, instance);

        // then
        assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidatesOnFields_noMockCandidatesFound_noInjectionOccurs() {
        // given
        Object instance = TestObjects.newObject();
        Class<?> awaitingInjectionClazz = TestTypes.awaitingInjectionClazz();
        Set<Object> mockCandidates = new HashSet<>();
        List<Field> orderedInstanceFields = getDeclaredFields(awaitingInjectionClazz);

        // when
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields);

        // then
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidatesOnFields_oneMockCandidateFound_injectionOccurs() {
        // given
        Object instance = TestObjects.newObject();
        Class<?> awaitingInjectionClazz = TestTypes.awaitingInjectionClazz();
        Set<Object> mockCandidates = new HashSet<>(Arrays.asList(Mockito.mock(TestTypes.mockCandidateType())));
        List<Field> orderedInstanceFields = getDeclaredFields(awaitingInjectionClazz);

        // when
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields);

        // then
        assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidatesOnFields_multipleMockCandidatesFound_injectionOccurs() {
        // given
        Object instance = TestObjects.newObject();
        Class<?> awaitingInjectionClazz = TestTypes.awaitingInjectionClazz();
        Set<Object> mockCandidates = new HashSet<>(Arrays.asList(Mockito.mock(TestTypes.mockCandidateType1()), Mockito.mock(TestTypes.mockCandidateType2())));
        List<Field> orderedInstanceFields = getDeclaredFields(awaitingInjectionClazz);

        // when
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields);

        // then
        assertTrue(injectionOccurred);
    }

    @Test
    public void testOrderedInstanceFieldsFrom_withNoFinalOrStaticFields_correctOrder() {
        // given
        Class<?> awaitingInjectionClazz = TestTypes.awaitingInjectionClazz();

        // when
        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // then
        List<String> fieldsNames = Arrays.asList(TestFields.field1Name(), TestFields.field2Name(), TestFields.field3Name());
        assertTrue(ListUtil.areEqual(FieldsNameComparator.INSTANCE, fieldsNames, orderedInstanceFields, true));
    }

    @Test
    public void testOrderedInstanceFieldsFrom_withFinalOrStaticFields_correctOrder() {
        // given
        Class<?> awaitingInjectionClazz = TestTypes.awaitingInjectionClazzWithFinalOrStaticFields();

        // when
        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // then
        List<String> fieldsNames = Arrays.asList(TestFields.field2Name(), TestFields.field1Name(), TestFields.field3Name());
        assertTrue(ListUtil.areEqual(FieldsNameComparator.INSTANCE, fieldsNames, orderedInstanceFields, true));
    }
}

class FieldsNameComparator implements Comparator<Field> {
    public static final FieldsNameComparator INSTANCE = new FieldsNameComparator();

    @Override
    public int compare(Field o1, Field o2) {
        return o1.getName().compareTo(o2.getName());
    }
}