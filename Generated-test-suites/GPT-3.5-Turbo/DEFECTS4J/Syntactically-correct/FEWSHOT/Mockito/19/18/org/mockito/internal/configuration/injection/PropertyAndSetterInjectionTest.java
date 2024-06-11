package org.mockito.internal.configuration.injection;

import org.junit.Test;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjection() throws NoSuchFieldException {
        Field injectMocksField = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        Object injectMocksFieldOwner = Util.class;

        Set<Object> mockCandidates = new HashSet<Object>();
        mockCandidates.add("mock1");
        mockCandidates.add("mock2");
        mockCandidates.add("mock3");

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertTrue(result);
        assertEquals(2, mockCandidates.size());
    }

    @Test
    public void testInitializeInjectMocksField() throws NoSuchFieldException {
        Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        Object fieldOwner = Util.class;

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        FieldInitializationReport report = injection.initializeInjectMocksField(field, fieldOwner);

        assertEquals(fieldOwner, report.fieldInstance());
        assertEquals(field.getType(), report.fieldClass());
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksFieldWithException() throws NoSuchFieldException {
        Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        Object fieldOwner = null;

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        injection.initializeInjectMocksField(field, fieldOwner);
    }

    @Test
    public void testInjectMockCandidates() {
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        Set<Object> mocks = new HashSet<Object>();
        mocks.add("mock1");
        mocks.add("mock2");
        mocks.add("mock3");
        Object instance = new PropertyAndSetterInjection();

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        boolean result = injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        assertTrue(result);
        assertEquals(1, mocks.size());
    }

    @Test
    public void testInjectMockCandidatesOnFields() throws NoSuchFieldException {
        Set<Object> mocks = new HashSet<Object>();
        mocks.add("mock1");
        mocks.add("mock2");
        mocks.add("mock3");
        Object instance = new PropertyAndSetterInjection();

        List<Field> orderedInstanceFields = new ArrayList<Field>();
        orderedInstanceFields.add(Util.class.getDeclaredField("EMPTY_STRING_ARRAY"));

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        boolean result = injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        assertTrue(result);
        assertEquals(2, mocks.size());
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        List<Field> result = injection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertEquals(3, result.size());
    }
}