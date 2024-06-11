import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

public class AtomicReferenceDeserializerTest {
    @Test
    public void testWithResolved() {
        // TODO: Implement test case for withResolved()
    }

    @Test
    public void testGetNullValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = deserializer.getNullValue(null);
        assertNull(reference.get());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = deserializer.getEmptyValue(null);
        assertNull(reference.get());
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        Object contents = new Object();
        AtomicReference<Object> reference = deserializer.referenceValue(contents);
        assertSame(contents, reference.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        assertSame(contents, deserializer.getReferenced(reference));
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        Object contents = new Object();
        AtomicReference<Object> reference = deserializer.updateReference(new AtomicReference<>(), contents);
        assertSame(contents, reference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        Boolean supportsUpdate = deserializer.supportsUpdate(null);
        assertEquals(Boolean.TRUE, supportsUpdate);
    }
}