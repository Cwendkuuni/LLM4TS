import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.mockito.Mockito;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        AtomicReference<Object> result = deser.getNullValue(ctxt);
        assert(result.get() == null);
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        AtomicReference<Object> result = deser.getEmptyValue(ctxt);
        assert(result.get() == null);
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> result = deser.referenceValue(contents);
        assert(result.get() == contents);
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = deser.getReferenced(reference);
        assert(result == reference.get());
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> result = deser.updateReference(reference, contents);
        assert(result.get() == contents);
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        Boolean result = deser.supportsUpdate(config);
        assert(result == Boolean.TRUE);
    }
}