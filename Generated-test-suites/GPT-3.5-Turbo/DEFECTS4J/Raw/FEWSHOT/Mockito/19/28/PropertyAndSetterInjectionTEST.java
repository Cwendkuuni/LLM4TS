package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

public class PropertyAndSetterInjectionTest {
    
    private PropertyAndSetterInjection injectionStrategy;
    
    @Before
    public void setUp() {
        injectionStrategy = new PropertyAndSetterInjection();
    }
    
    @Test
    public void testProcessInjection() throws Exception {
        // Mock injectMocksField
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectionStrategy, newTypeBasedCandidateFilter());
        
        // Create injectMocksFieldOwner
        Object injectMocksFieldOwner = Mockito.mock(MockInjectionStrategy.class);
        
        // Create mockCandidates
        Set<Object> mockCandidates = new HashSet<>();
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(Integer.class);
        mockCandidates.add(mock1);
        mockCandidates.add(mock2);
        
        boolean result = injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        
        assertTrue(result);
    }
    
    @Test
    public void testInitializeInjectMocksField() throws Exception {
        // Create field
        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        field.setAccessible(true);
        
        // Create fieldOwner and fieldInstance
        Object fieldOwner = Mockito.mock(MockInjectionStrategy.class);
        Object fieldInstance = Mockito.mock(MockCandidateFilter.class);
        
        // Mock FieldInitializer
        FieldInitializationReport report = new FieldInitializationReport(null, null);
        FieldInitializer fieldInitializer = Mockito.mock(FieldInitializer.class);
        when(fieldInitializer.initialize()).thenReturn(report);
        PowerMockito.whenNew(FieldInitializer.class).withArguments(fieldOwner, field).thenReturn(fieldInitializer);
        
        // Call method
        FieldInitializationReport result = injectionStrategy.initializeInjectMocksField(field, fieldOwner);
        
        assertSame(report, result);
        verify(fieldInitializer).initialize();
    }
    
    @Test
    public void testInjectMockCandidates() throws Exception {
        // Create awaitingInjectionClazz, mocks, and instance
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        Set<Object> mocks = new HashSet<>();
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(Integer.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Object instance = Mockito.mock(MockInjectionStrategy.class);
        
        // Mock orderedInstanceFieldsFrom
        Field field1 = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        Field field2 = PropertyAndSetterInjection.class.getDeclaredField("notFinalOrStatic");
        List<Field> orderedInstanceFields = new ArrayList<>();
        orderedInstanceFields.add(field1);
        orderedInstanceFields.add(field2);
        PropertyAndSetterInjection spyInjectionStrategy = Mockito.spy(injectionStrategy);
        Mockito.doReturn(orderedInstanceFields).when(spyInjectionStrategy).orderedInstanceFieldsFrom(awaitingInjectionClazz);
        
        boolean result = spyInjectionStrategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
        
        assertTrue(result);
        assertEquals(0, mocks.size());
        assertEquals(0, orderedInstanceFields.size());
        verify(spyInjectionStrategy, times(2)).injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);
    }
    
    @Test
    public void testInjectMockCandidatesOnFields() throws Exception {
        // Create mocks, instance, injectionOccurred, and orderedInstanceFields
        Set<Object> mocks = new HashSet<>();
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(Integer.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Object instance = Mockito.mock(MockInjectionStrategy.class);
        boolean injectionOccurred = false;
        Field field1 = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        Field field2 = PropertyAndSetterInjection.class.getDeclaredField("notFinalOrStatic");
        List<Field> orderedInstanceFields = new ArrayList<>();
        orderedInstanceFields.add(field1);
        orderedInstanceFields.add(field2);
        
        boolean result = injectionStrategy.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
        
        assertTrue(result);
        assertEquals(0, mocks.size());
        assertEquals(0, orderedInstanceFields.size());
    }
    
    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        // Create awaitingInjectionClazz and fields
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        Field field1 = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        Field field2 = PropertyAndSetterInjection.class.getDeclaredField("notFinalOrStatic");
        List<Field> expected = new ArrayList<>();
        expected.add(field1);
        expected.add(field2);
        
        List<Field> result = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClazz);
        
        assertEquals(expected, result);
    }
    
    private TypeBasedCandidateFilter newTypeBasedCandidateFilter() {
        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        NameBasedCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter(finalMockCandidateFilter);
        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(nameBasedCandidateFilter);
        return typeBasedCandidateFilter;
    }
    
}