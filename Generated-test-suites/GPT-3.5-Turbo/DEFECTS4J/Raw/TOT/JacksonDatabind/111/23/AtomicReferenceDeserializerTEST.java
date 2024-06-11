import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer atomicReferenceDeserializer;

    @Before
    public void setUp() {
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
    }

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        JsonDeserializer<?> valueDeserializer = new JsonDeserializer<>();
        
        atomicReferenceDeserializer.withResolved(typeDeserializer, valueDeserializer);
        
        assertNotNull(atomicReferenceDeserializer);
        assertEquals(typeDeserializer, atomicReferenceDeserializer._typeDeserializer);
        assertEquals(valueDeserializer, atomicReferenceDeserializer._valueDeserializer);
    }

    @Test
    public void testGetNullValue() {
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
        assertTrue(result instanceof AtomicReference);
        assertNull(((AtomicReference) result).get());
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

        assertNotNull(result);
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

}