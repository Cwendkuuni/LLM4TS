import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer deserializer;
    
    @Before
    public void setUp() {
        deserializer = new AtomicReferenceDeserializer(null, null, null, null);
    }
    
    @Test
    public void testGetNullValue() {
        AtomicReference<Object> result = deserializer.getNullValue(null);
        assertNotNull(result);
        assertNull(result.get());
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReference<Object> result = deserializer.getEmptyValue(null);
        assertNotNull(result);
        assertNull(result.get());
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
        assertEquals(contents, result);
    }
    
    @Test
    public void testUpdateReference() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(null);
        AtomicReference<Object> result = deserializer.updateReference(reference, contents);
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        Boolean result = deserializer.supportsUpdate(null);
        assertTrue(result);
    }
}