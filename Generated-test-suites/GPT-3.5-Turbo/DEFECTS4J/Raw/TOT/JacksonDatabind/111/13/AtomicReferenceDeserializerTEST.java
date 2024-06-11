import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.getNullValue(null);
        
        Assert.assertNotNull(result);
        Assert.assertNull(result.get());
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = deserializer.getEmptyValue(null);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof AtomicReference);
        Assert.assertNull(((AtomicReference<?>) result).get());
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> result = deserializer.referenceValue(contents);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(contents, result.get());
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        Object result = deserializer.getReferenced(reference);
        
        Assert.assertEquals(contents, result);
        
        // Testing null case
        reference = new AtomicReference<>(null);
        result = deserializer.getReferenced(reference);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(contents, reference.get());
        Assert.assertEquals(contents, result.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Boolean result = deserializer.supportsUpdate(null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(Boolean.TRUE, result);
    }
}