import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {
    
    private AtomicReferenceDeserializer atomicReferenceDeserializer;
    
    @Before
    public void setUp() {
        JavaType fullType = TypeFactory.defaultInstance().constructType(AtomicReference.class);
        ValueInstantiator inst = new ValueInstantiator() {
            public AtomicReference<?> createUsingDefault(DeserializationContext ctxt) {
                return new AtomicReference<>();
            }
        };
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> deser = null;
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
    }
    
    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);
        assertEquals(new AtomicReference<>(), result);
    }
    
    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null, null);
        Object result = atomicReferenceDeserializer.getEmptyValue(ctxt);
        assertEquals(new AtomicReference<>(), result);
    }
    
    @Test
    public void testReferenceValue() {
        Object contents = "testContents";
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        assertEquals(new AtomicReference<>(contents), result);
    }
    
    @Test
    public void testGetReferenced() {
        Object contents = "testContents";
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        assertEquals(contents, result);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = "testContents";
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
        assertEquals(contents, reference.get());
        assertEquals(reference, result);
    }
    
    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        Boolean supportsUpdate = atomicReferenceDeserializer.supportsUpdate(config);
        assertTrue(supportsUpdate);
    }
}