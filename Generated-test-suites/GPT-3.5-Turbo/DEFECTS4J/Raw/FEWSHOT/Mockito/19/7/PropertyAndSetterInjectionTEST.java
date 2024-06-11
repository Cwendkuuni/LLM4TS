package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.Whitebox;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;

    @Before
    public void setup() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection() throws Exception {
        Field injectMocksField = UtilTest.class.getDeclaredField("emptyStringArray");
        Object injectMocksFieldOwner = UtilTest.class.newInstance();
        Set<Object> mockCandidates = (Set<Object>) Whitebox.getInternalState(propertyAndSetterInjection, "mockCandidates");
        mockCandidates.add("mockCandidate1");
        mockCandidates.add("mockCandidate2");

        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        verifyNoMoreInteractions(mockCandidates);
        assertFalse(result);
    }

    @Test
    public void testInitializeInjectMocksField() throws Exception {
        Field field = UtilTest.class.getDeclaredField("emptyStringArray");
        Object fieldOwner = UtilTest.class.newInstance();

        FieldInitializationReport result = propertyAndSetterInjection.initializeInjectMocksField(field, fieldOwner);

        assertNull(result);
    }

    @Test
    public void testInjectMockCandidates() throws Exception {
        Class<?> awaitingInjectionClazz = UtilTest.class;
        Set<Object> mocks = new HashSet<>();
        Object instance = UtilTest.class.newInstance();

        boolean result = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        verifyNoMoreInteractions(mocks);
        assertFalse(result);
    }

    @Test
    public void testInjectMockCandidatesOnFields() throws Exception {
        Set<Object> mocks = new HashSet<>();
        Object instance = UtilTest.class.newInstance();
        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = new ArrayList<>();
        orderedInstanceFields.add(UtilTest.class.getDeclaredField("emptyStringArray"));

        boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);

        verifyNoMoreInteractions(mocks);
        assertFalse(result);
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        Class<?> awaitingInjectionClazz = UtilTest.class;

        List<Field> result = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}