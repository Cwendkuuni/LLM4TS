import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PropertyAndSetterInjectionTest {
    
    private PropertyAndSetterInjection injectionStrategy;
    
    @Before
    public void setup() {
        injectionStrategy = new PropertyAndSetterInjection();
    }
    
    @Test
    public void testProcessInjection() throws NoSuchFieldException {
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        Object injectMocksFieldOwner = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();
        
        boolean result = injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        
        assertTrue(result);
    }
    
    @Test
    public void testInitializeInjectMocksField() throws NoSuchFieldException {
        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        Object fieldOwner = new PropertyAndSetterInjection();
        
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(field, fieldOwner);
        
        assertEquals(fieldOwner, report.fieldInstance());
    }
    
    @Test
    public void testInjectMockCandidates() {
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        Set<Object> mocks = new HashSet<>();
        Object instance = new PropertyAndSetterInjection();
        
        boolean result = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
        
        assertTrue(result);
    }
    
    @Test
    public void testInjectMockCandidatesOnFields() throws NoSuchFieldException, IllegalAccessException {
        Set<Object> mocks = new HashSet<>();
        Object instance = new PropertyAndSetterInjection();
        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        
        boolean result = injectionStrategy.injectMockCandidatesOnFields(mocks, instance, false, field);
        
        assertTrue(result);
    }
    
    @Test
    public void testOrderedInstanceFieldsFrom() {
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        
        List<Field> fields = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClazz);
        
        assertEquals(1, fields.size());
    }
}