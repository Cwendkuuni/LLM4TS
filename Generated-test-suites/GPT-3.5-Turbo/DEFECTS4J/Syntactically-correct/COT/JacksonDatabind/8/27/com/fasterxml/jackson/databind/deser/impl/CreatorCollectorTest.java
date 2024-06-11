import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CreatorCollectorTest {
    
    private CreatorCollector creatorCollector;
    
    @Mock
    private AnnotatedWithParams annotatedWithParams;
    
    @Mock
    private CreatorProperty[] creatorProperties;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        creatorCollector = new CreatorCollector(null, true);
  
    }

    @Test
    public void testSetDefaultCreator() {
        creatorCollector.setDefaultCreator(annotatedWithParams);
        
        assertSame(annotatedWithParams, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }
    
    @Test
    public void testAddStringCreator() {
        creatorCollector.addStringCreator(annotatedWithParams, false);
        
        assertSame(annotatedWithParams, creatorCollector._creators[CreatorCollector.C_STRING]);
    }
    
    @Test
    public void testAddIntCreator() {
        creatorCollector.addIntCreator(annotatedWithParams, false);
        
        assertSame(annotatedWithParams, creatorCollector._creators[CreatorCollector.C_INT]);
    }
    
    @Test
    public void testAddLongCreator() {
        creatorCollector.addLongCreator(annotatedWithParams, false);
        
        assertSame(annotatedWithParams, creatorCollector._creators[CreatorCollector.C_LONG]);
    }
    
    @Test
    public void testAddDoubleCreator() {
        creatorCollector.addDoubleCreator(annotatedWithParams, false);
        
        assertSame(annotatedWithParams, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }
    
    @Test
    public void testAddBooleanCreator() {
        creatorCollector.addBooleanCreator(annotatedWithParams, false);
        
        assertSame(annotatedWithParams, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }
    
    @Test
    public void testAddDelegatingCreator() {
        creatorCollector.addDelegatingCreator(annotatedWithParams, false, creatorProperties);
        
        assertSame(annotatedWithParams, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertSame(creatorProperties, creatorCollector._delegateArgs);
    }
    
    @Test
    public void testAddPropertyCreator() {
        creatorCollector.addPropertyCreator(annotatedWithParams, false, creatorProperties);
        
        assertSame(annotatedWithParams, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertSame(creatorProperties, creatorCollector._propertyBasedArgs);
    }
    
    @Test
    public void testAddIncompeteParameter() {
        creatorCollector.addIncompeteParameter(null);
        
        assertNull(creatorCollector._incompleteParameter);
        
        creatorCollector.addIncompeteParameter(annotatedWithParams);
        
        assertSame(annotatedWithParams, creatorCollector._incompleteParameter);
    }
    
    @Test
    public void testHasDefaultCreator() {
        assertFalse(creatorCollector.hasDefaultCreator());
        
        creatorCollector.setDefaultCreator(annotatedWithParams);
        
        assertTrue(creatorCollector.hasDefaultCreator());
    }
    
    @Test
    public void testConstructValueInstantiator() {
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(null);
        
        assertTrue(valueInstantiator.canInstantiate());
    }
}