
package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

public class PropertyAndSetterInjectionTest {
    
    // Helper method to check if two sets of objects are equal
    private <T> boolean setsAreEqual(Set<T> set1, Set<T> set2) {
        if (set1.size() != set2.size()) {
            return false;
        }
        
        for (T obj : set1) {
            if (!set2.contains(obj)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Test
    public void testProcessInjection_ReturnsTrue_WhenInjectionOccurs() throws Exception {
        // Setup
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        Object injectMocksFieldOwner = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new Object());
        
        // Execute
        boolean result = PropertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        
        // Verify
        assertTrue(result);
    }
    
    @Test
    public void testProcessInjection_ReturnsFalse_WhenNoInjectionOccurs() throws Exception {
        // Setup
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        Object injectMocksFieldOwner = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();
        
        // Execute
        boolean result = PropertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        
        // Verify
        assertFalse(result);
    }
    
    @Test
    public void testProcessInjection_InjectsAllMockCandidates() throws Exception {
        // Setup
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        Object injectMocksFieldOwner = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new Object());
        mockCandidates.add(new Object());
        Set<Object> originalCandidates = new HashSet<>(mockCandidates);
        
        // Execute
        PropertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        
        // Verify
        assertTrue(mockCandidates.isEmpty());
        assertTrue(setsAreEqual(originalCandidates, mockCandidates));
    }
    
    @Test
    public void testInjectMockCandidates_ReturnsTrue_WhenInjectionOccurs() throws Exception {
        // Setup
        Set<Object> mocks = new HashSet<>();
        mocks.add(new Object());
        Object instance = new Object();
        List<Field> orderedInstanceFields = new ArrayList<>();
        Field field = new Field();
        orderedInstanceFields.add(field);
        
        // Execute
        boolean result = PropertyAndSetterInjection.injectMockCandidates(mocks, instance, orderedInstanceFields);
        
        // Verify
        assertTrue(result);
    }
    
    @Test
    public void testInjectMockCandidates_ReturnsFalse_WhenNoInjectionOccurs() throws Exception {
        // Setup
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = new ArrayList<>();
        
        // Execute
        boolean result = PropertyAndSetterInjection.injectMockCandidates(mocks, instance, orderedInstanceFields);
        
        // Verify
        assertFalse(result);
    }
    
    @Test
    public void testInjectMockCandidates_InjectsMockCandidatesOnFields() throws Exception {
        // Setup
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = new ArrayList<>();
        Field field1 = new Field();
        Field field2 = new Field();
        orderedInstanceFields.add(field1);
        orderedInstanceFields.add(field2);
        
        // Execute
        PropertyAndSetterInjection.injectMockCandidates(mocks, instance, orderedInstanceFields);
        
        // Verify
        assertTrue(orderedInstanceFields.isEmpty());
        assertTrue(mocks.isEmpty());
    }
    
    @Test
    public void testInjectMockCandidates_DoesNotInjectMocksWhenNoCandidates() throws Exception {
        // Setup
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = new ArrayList<>();
        Field field = new Field();
        orderedInstanceFields.add(field);
        
        // Execute
        PropertyAndSetterInjection.injectMockCandidates(mocks, instance, orderedInstanceFields);
        
        // Verify
        assertFalse(orderedInstanceFields.isEmpty());
        assertFalse(mocks.isEmpty());
    }
    
    @Test
    public void testOrderedInstanceFieldsFrom_SortsFieldsFromSubTypeToSuperTypeThenByName() throws Exception {
        // Setup
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        List<Field> expectedFields = new ArrayList<>();
        Field field1 = new Field();
        Field field2 = new Field();
        expectedFields.add(field1);
        expectedFields.add(field2);
        
        // Execute
        List<Field> result = PropertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
        
        // Verify
        assertEquals(expectedFields, result);
    }
    
    @Test
    public void testOrderedInstanceFieldsFrom_ReturnsEmptyList_WhenNoFields() throws Exception {
        // Setup
        Class<?> awaitingInjectionClazz = Field.class;
        
        // Execute
        List<Field> result = PropertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
        
        // Verify
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testInjectMockCandidatesOnFields_ReturnsTrue_WhenInjectionOccurs() throws Exception {
        // Setup
        Set<Object> mocks = new HashSet<>();
        mocks.add(new Object());
        Object instance = new Object();
        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = new ArrayList<>();
        Field field = new Field();
        orderedInstanceFields.add(field);
        
        // Execute
        boolean result = PropertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
        
        // Verify
        assertTrue(result);
    }
    
    @Test
    public void testInjectMockCandidatesOnFields_ReturnsFalse_WhenNoInjectionOccurs() throws Exception {
        // Setup
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = new ArrayList<>();
        
        // Execute
        boolean result = PropertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
        
        // Verify
        assertFalse(result);
    }
    
    @Test
    public void testInjectMockCandidatesOnFields_InjectsMockCandidate() throws Exception {
        // Setup
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = new ArrayList<>();
        Field field = new Field();
        orderedInstanceFields.add(field);
        
        // Execute
        PropertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
        
        // Verify
        assertTrue(orderedInstanceFields.isEmpty());
        assertTrue(mocks.isEmpty());
    }
    
    @Test
    public void testInjectMockCandidatesOnFields_DoesNotInjectMocksWhenNoCandidates() throws Exception {
        // Setup
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = new ArrayList<>();
        Field field = new Field();
        orderedInstanceFields.add(field);
        
        // Execute
        PropertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
        
        // Verify
        assertFalse(orderedInstanceFields.isEmpty());
        assertFalse(mocks.isEmpty());
    }
    
    @Test
    public void testInitializeInjectMocksField_ReturnsFieldInitializationReport() throws Exception {
        // Setup
        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        Object fieldOwner = new PropertyAndSetterInjection();
        
        // Execute
        FieldInitializationReport result = PropertyAndSetterInjection.initializeInjectMocksField(field, fieldOwner);
        
        // Verify
        assertNotNull(result);
    }
    
    @Test(expected = Reporter.class)
    public void testInitializeInjectMocksField_ThrowsReporter_WhenFieldInitialisationThrowsException() throws Exception {
        // Setup
        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        Object fieldOwner = new PropertyAndSetterInjection();
        
        // Execute
        FieldInitializationReport result = PropertyAndSetterInjection.initializeInjectMocksField(field, fieldOwner);
        
        // Verify - Exception thrown
    }
    
    @Test
    public void testProcessInjection_CallsInitializeInjectMocksField() throws Exception {
        // Setup
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        Object injectMocksFieldOwner = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new Object());
        
        // Mock initializeInjectMocksField
        boolean initializeInjectMocksFieldCalled = false;
        PropertyAndSetterInjection.initializeInjectMocksField = (Field field, Object fieldOwner) -> {
            initializeInjectMocksFieldCalled = true;
            assertNotNull(field);
            assertEquals(Field.class, field.getClass());
            assertEquals(injectMocksFieldOwner, fieldOwner);
            return null;
        };
        
        // Execute
        boolean result = PropertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        
        // Verify
        assertTrue(initializeInjectMocksFieldCalled);
    }
}
