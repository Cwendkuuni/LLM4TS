import org.junit.Test;
import org.junit.Assert;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = new ObjectMapper().deserializationContext();
        AtomicReferenceDeserializer atomicRefDeserializer = new AtomicReferenceDeserializer();
        
        AtomicReference<Object> result = atomicRefDeserializer.getNullValue(ctxt);
        
        Assert.assertNotNull(result);
        Assert.assertNull(result.get());
    }
    
    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new ObjectMapper().deserializationContext();
        AtomicReferenceDeserializer atomicRefDeserializer = new AtomicReferenceDeserializer();
        
        Object result = atomicRefDeserializer.getEmptyValue(ctxt);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof AtomicReference);
        Assert.assertNull(((AtomicReference<Object>) result).get());
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer atomicRefDeserializer = new AtomicReferenceDeserializer();
        Object contents = new Object();
        
        AtomicReference<Object> result = atomicRefDeserializer.referenceValue(contents);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(contents, result.get());
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer atomicRefDeserializer = new AtomicReferenceDeserializer();
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        
        Object result = atomicRefDeserializer.getReferenced(reference);
        
        Assert.assertEquals(contents, result);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer atomicRefDeserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = new AtomicReference<>();
        Object newContents = new Object();
        
        AtomicReference<Object> result =atomicRefDeserializer.updateReference(reference, newContents);
        
        Assert.assertEquals(newContents, result.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();
        AtomicReferenceDeserializer atomicRefDeserializer = new AtomicReferenceDeserializer();
        
        Boolean result = atomicRefDeserializer.supportsUpdate(config);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result);
    }
}