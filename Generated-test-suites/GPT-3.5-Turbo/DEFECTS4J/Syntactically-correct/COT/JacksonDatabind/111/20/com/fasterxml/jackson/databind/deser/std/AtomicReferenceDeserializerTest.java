
import org.junit.Test;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() throws Exception {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.getNullValue(null);
        assert result.get() == null;
    }

    @Test
    public void testGetEmptyValue() throws Exception {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.getEmptyValue(null);
        assert result.get() == null;
    }

    @Test
    public void testReferenceValue() throws Exception {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deserializer.referenceValue(new Object());
        assert result.get() != null;
    }

    @Test
    public void testGetReferenced() throws Exception {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = new AtomicReference<>();
        reference.set("test");
        Object result = deserializer.getReferenced(reference);
        assert result.equals("test");
    }

    @Test
    public void testUpdateReference() throws Exception {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = new AtomicReference<>();
        deserializer.updateReference(reference, "test");
        assert reference.get().equals("test");
    }

    @Test
    public void testSupportsUpdate() throws Exception {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        assert deserializer.supportsUpdate(null);
    }
}
