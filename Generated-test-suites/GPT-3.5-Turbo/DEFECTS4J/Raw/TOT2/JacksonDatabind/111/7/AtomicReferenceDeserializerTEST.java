import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.construct(Object.class),
                                                                               new ValueInstantiator.Base(null, null),
                                                                               null,
                                                                               new StdDeserializer<Object>(Object.class));
        
        assertNotNull(deserializer);
    }
    
    // Test case for getNullValue method
    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.construct(Object.class),
                                                                               new ValueInstantiator.Base(null, null),
                                                                               null,
                                                                               new StdDeserializer<Object>(Object.class));
        
        AtomicReference<Object> nullValue = deserializer.getNullValue(null);
        
        assertNotNull(nullValue);
        assertNull(nullValue.get());
    }
    
    // Test case for getEmptyValue method
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.construct(Object.class),
                                                                               new ValueInstantiator.Base(null, null),
                                                                               null,
                                                                               new StdDeserializer<Object>(Object.class));
        
        AtomicReference<Object> emptyValue = (AtomicReference<Object>) deserializer.getEmptyValue(null);
        
        assertNotNull(emptyValue);
        assertNull(emptyValue.get());
    }
    
    // Test case for referenceValue method
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.construct(Object.class),
                                                                               new ValueInstantiator.Base(null, null),
                                                                               null,
                                                                               new StdDeserializer<Object>(Object.class));
        
        AtomicReference<Object> reference = deserializer.referenceValue("test");
        
        assertNotNull(reference);
        assertEquals("test", reference.get());
    }
    
    // Test case for getReferenced method
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.construct(Object.class),
                                                                               new ValueInstantiator.Base(null, null),
                                                                               null,
                                                                               new StdDeserializer<Object>(Object.class));
        
        AtomicReference<Object> reference = new AtomicReference<Object>("test");
        
        assertEquals("test", deserializer.getReferenced(reference));
    }
    
    // Test case for updateReference method
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.construct(Object.class),
                                                                               new ValueInstantiator.Base(null, null),
                                                                               null,
                                                                               new StdDeserializer<Object>(Object.class));
        
        AtomicReference<Object> reference = new AtomicReference<Object>("test");
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, "updated test");
        
        assertEquals("updated test", updatedReference.get());
    }
    
    // Test case for supportsUpdate method
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.construct(Object.class),
                                                                               new ValueInstantiator.Base(null, null),
                                                                               null,
                                                                               new StdDeserializer<Object>(Object.class));
        
        assertTrue(deserializer.supportsUpdate(null));
    }
}