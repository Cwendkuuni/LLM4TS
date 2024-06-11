package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;
import static org.junit.Assert.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection() {
        Field injectMocksField = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        Object injectMocksFieldOwner = new Util();
        Set<Object> mockCandidates = new HashSet<>();

        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertTrue(result);
    }

    @Test
    public void testInitializeInjectMocksField() throws NoSuchFieldException {
        Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        Object fieldOwner = new Util();

        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(field, fieldOwner);

        assertNotNull(report);
    }

    @Test
    public void testInjectMockCandidates() {
        Class<?> awaitingInjectionClazz = Util.class;
        Set<Object> mocks = new HashSet<>();
        Object instance = new Util();

        boolean result = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        assertFalse(result);
    }

    @Test
    public void testInjectMockCandidatesOnFields() throws NoSuchFieldException {
        Set<Object> mocks = new HashSet<>();
        Object instance = new Util();
        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = Arrays.asList(Util.class.getDeclaredField("EMPTY_STRING_ARRAY"));

        boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);

        assertFalse(result);
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        Class<?> awaitingInjectionClazz = Util.class;

        List<Field> result = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertNotNull(result);
    }

}