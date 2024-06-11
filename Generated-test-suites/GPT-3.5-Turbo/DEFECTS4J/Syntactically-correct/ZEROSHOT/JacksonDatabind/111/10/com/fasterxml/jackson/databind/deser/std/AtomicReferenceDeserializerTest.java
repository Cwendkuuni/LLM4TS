import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer atomicReferenceDeserializer;

    @Before
    public void setUp() {
        JavaType fullType = new JavaType(FullType.class);
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
  
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);
        assertNotNull(result);
        assertNull(result.get());
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();
        Object result = atomicReferenceDeserializer.getEmptyValue(ctxt);
        assertNotNull(result);
        assertTrue(result instanceof AtomicReference<?>);
        assertNull(((AtomicReference<?>) result).get());
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        reference.set(contents);
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        assertEquals(contents, result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();
        Boolean result = atomicReferenceDeserializer.supportsUpdate(config);
        assertNotNull(result);
        assertTrue(result);
    }

    private static class FullType extends JavaType {

        public FullType() {
            super();
        }
 
    }
  
}