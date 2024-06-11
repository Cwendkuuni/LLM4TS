import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CreatorCollectorTest {
    
    @Mock
    private BeanDescription mockBeanDesc;
    
    @Mock
    private AnnotatedWithParams mockCreator;
    
    @Mock
    private DeserializationConfig mockDeserializationConfig;
    
    @Mock
    private JavaType mockJavaType;
    
    private CreatorCollector creatorCollector;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        creatorCollector = new CreatorCollector(mockBeanDesc, true);
    }
    
    @Test
    public void testDefaultValueInstantiator() throws IOException {
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(mockDeserializationConfig);
        
        assertNotNull(instantiator);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateUsingDefault());
        assertEquals(Object.class.getName(), instantiator.getValueTypeDesc());
        
        Object createdObject = instantiator.createUsingDefault(mockDeserializationContext);
        
        assertNotNull(createdObject);
    }
    
    @Test
    public void testAddDefaultCreator() {
        creatorCollector.setDefaultCreator(mockCreator);
        
        AnnotatedWithParams actualCreator = creatorCollector.verifyNonDup(mockCreator, CreatorCollector.C_DEFAULT);
        
        assertNotNull(actualCreator);
        assertEquals(mockCreator, actualCreator);
    }
    
    @Test
    public void testAddStringCreator() {
        creatorCollector.addStringCreator(mockCreator, true);
        
        AnnotatedWithParams actualCreator = creatorCollector.verifyNonDup(mockCreator, CreatorCollector.C_STRING, true);
        
        assertNotNull(actualCreator);
        assertEquals(mockCreator, actualCreator);
    }
    
    @Test
    public void testAddIntCreator() {
        creatorCollector.addIntCreator(mockCreator, true);
        
        AnnotatedWithParams actualCreator = creatorCollector.verifyNonDup(mockCreator, CreatorCollector.C_INT, true);
        
        assertNotNull(actualCreator);
        assertEquals(mockCreator, actualCreator);
    }

    @Test
    public void testAddLongCreator() {
        creatorCollector.addLongCreator(mockCreator, true);
        
        AnnotatedWithParams actualCreator = creatorCollector.verifyNonDup(mockCreator, CreatorCollector.C_LONG, true);
        
        assertNotNull(actualCreator);
        assertEquals(mockCreator, actualCreator);
    }

    @Test
    public void testAddDoubleCreator() {
        creatorCollector.addDoubleCreator(mockCreator, true);
        
        AnnotatedWithParams actualCreator = creatorCollector.verifyNonDup(mockCreator, CreatorCollector.C_DOUBLE, true);
        
        assertNotNull(actualCreator);
        assertEquals(mockCreator, actualCreator);
    }

    @Test
    public void testAddBooleanCreator() {
        creatorCollector.addBooleanCreator(mockCreator, true);
        
        AnnotatedWithParams actualCreator = creatorCollector.verifyNonDup(mockCreator, CreatorCollector.C_BOOLEAN, true);
        
        assertNotNull(actualCreator);
        assertEquals(mockCreator, actualCreator);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorProperty[] injectables = new CreatorProperty[]{};
        
        creatorCollector.addDelegatingCreator(mockCreator, true, injectables);
        
        AnnotatedWithParams actualCreator = creatorCollector.verifyNonDup(mockCreator, CreatorCollector.C_DELEGATE, true);
        
        assertNotNull(actualCreator);
        assertEquals(mockCreator, actualCreator);
        
        assertArrayEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorProperty[] properties = new CreatorProperty[]{};
        
        creatorCollector.addPropertyCreator(mockCreator, true, properties);
        
        AnnotatedWithParams actualCreator = creatorCollector.verifyNonDup(mockCreator, CreatorCollector.C_PROPS, true);
        
        assertNotNull(actualCreator);
        assertEquals(mockCreator, actualCreator);
        
        assertArrayEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        creatorCollector.addIncompeteParameter(mockAnnotatedParameter);
        
        assertNotNull(creatorCollector._incompleteParameter);
        assertEquals(mockAnnotatedParameter, creatorCollector._incompleteParameter);
    }
}