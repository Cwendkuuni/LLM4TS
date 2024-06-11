import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {
    
    private AtomicReferenceDeserializer deserializer;
    
    @Before
    public void setUp() {
        JavaType fullType = JavaType.builder().build();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
    }

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        
        AtomicReferenceDeserializer result = deserializer.withResolved(typeDeser, valueDeser);
        
        assertNotNull(result);
        assertEquals(typeDeser, result.getTypeDeserializer());
        assertEquals(valueDeser, result.getValueDeserializer());
    }
    
    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        
        AtomicReference<Object> result = deserializer.getNullValue(ctxt);
        
        assertNotNull(result);
        assertNull(result.get());
    }
    
    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();
        
        Object result = deserializer.getEmptyValue(ctxt);
        
        assertNotNull(result);
        assertTrue(result instanceof AtomicReference);
        assertNull(((AtomicReference<?>) result).get());
    }
    
    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        
        AtomicReference<Object> result = deserializer.referenceValue(contents);
        
        assertNotNull(result);
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testGetReferenced() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        
        Object result = deserializer.getReferenced(reference);
        
        assertNotNull(result);
        assertEquals(contents, result);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
        
        assertNotNull(result);
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();
        
        Boolean result = deserializer.supportsUpdate(config);
        
        assertNotNull(result);
        assertTrue(result);
    }
}