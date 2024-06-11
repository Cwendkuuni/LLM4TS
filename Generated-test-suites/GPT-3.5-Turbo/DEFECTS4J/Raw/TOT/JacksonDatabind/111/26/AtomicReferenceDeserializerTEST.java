import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);

        AtomicReference<Object> atomicReference = atomicReferenceDeserializer.getNullValue(null);

        Assert.assertNotNull(atomicReference);
        Assert.assertNull(atomicReference.get());
    }

    @Test
    public void testGetEmptyValue() throws JsonMappingException {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);

        AtomicReference<Object> atomicReference = atomicReferenceDeserializer.getEmptyValue(null);

        Assert.assertNotNull(atomicReference);
        Assert.assertNull(atomicReference.get());
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();

        AtomicReference<Object> atomicReference = atomicReferenceDeserializer.referenceValue(contents);

        Assert.assertNotNull(atomicReference);
        Assert.assertEquals(contents, atomicReference.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> atomicReference = new AtomicReference<>(contents);

        Object referenced = atomicReferenceDeserializer.getReferenced(atomicReference);

        Assert.assertEquals(contents, referenced);
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object initialContents = new Object();
        AtomicReference<Object> atomicReference = new AtomicReference<>(initialContents);
        Object updatedContents = new Object();

        AtomicReference<Object> updatedReference = atomicReferenceDeserializer.updateReference(atomicReference, updatedContents);

        Assert.assertNotNull(updatedReference);
        Assert.assertEquals(updatedContents, updatedReference.get());
        Assert.assertEquals(updatedReference, atomicReference);
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationConfig config = null;

        Boolean supportsUpdate = atomicReferenceDeserializer.supportsUpdate(config);

        Assert.assertEquals(Boolean.TRUE, supportsUpdate);
    }
}