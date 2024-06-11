import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeserializer, valueDeserializer);

        Assert.assertEquals(typeDeserializer, result.getTypeDeserializer());
        Assert.assertEquals(valueDeserializer, result.getValueDeserializer());
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext context = new DeserializationContext(null, null);
        AtomicReference<Object> nullValue = atomicReferenceDeserializer.getNullValue(context);

        Assert.assertNotNull(nullValue);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext context = new DeserializationContext(null, null);
        Object emptyValue = atomicReferenceDeserializer.getEmptyValue(context);

        Assert.assertNotNull(emptyValue);
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> referenceValue = atomicReferenceDeserializer.referenceValue(contents);

        Assert.assertEquals(contents, referenceValue.get());
    }

    @Test
    public void testGetReferenced() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);

        Object referenced = atomicReferenceDeserializer.getReferenced(reference);

        Assert.assertEquals(contents, referenced);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();

        AtomicReference<Object> updatedReference = atomicReferenceDeserializer.updateReference(reference, contents);

        Assert.assertEquals(contents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();

        Boolean supportsUpdate = atomicReferenceDeserializer.supportsUpdate(config);

        Assert.assertTrue(supportsUpdate);
    }
}