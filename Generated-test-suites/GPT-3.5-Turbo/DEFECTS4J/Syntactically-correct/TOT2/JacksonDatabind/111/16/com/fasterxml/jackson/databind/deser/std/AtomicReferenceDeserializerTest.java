import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationContext ctxt = null;
        
        AtomicReference<Object> result = deserializer.getNullValue(ctxt);
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationContext ctxt = null;
        
        Object result = deserializer.getEmptyValue(ctxt);
        
        assertNotNull(result);
        assertEquals(AtomicReference.class, result.getClass());
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = "test";
        
        AtomicReference<Object> result = deserializer.referenceValue(contents);
        
        assertNotNull(result);
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = new AtomicReference<>("test");
        
        Object result = deserializer.getReferenced(reference);
        
        assertEquals("test", result);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = new AtomicReference<>("test");
        Object contents = "updated";
        
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
        
        assertNotNull(result);
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationConfig config = null;
        
        Boolean result = deserializer.supportsUpdate(config);
        
        assertEquals(Boolean.TRUE, result);
    }

}