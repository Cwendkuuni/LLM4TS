
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        JavaType fullType = null;
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);

        // add assertion
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = null;

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);

        // add assertion
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = atomicReferenceDeserializer.getEmptyValue(ctxt);

        // add assertion
    }
    
    @Test
    public void testReferenceValue() {
        Object contents = null;

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);

        // add assertion
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = null;

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = atomicReferenceDeserializer.getReferenced(reference);

        // add assertion
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = null;
        Object contents = null;

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);

        // add assertion
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = null;

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Boolean result = atomicReferenceDeserializer.supportsUpdate(config);

        // add assertion
    }
}

