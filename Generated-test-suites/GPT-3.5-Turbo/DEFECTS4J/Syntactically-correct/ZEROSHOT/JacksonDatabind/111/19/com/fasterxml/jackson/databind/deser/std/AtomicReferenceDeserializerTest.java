import org.junit.Assert;
import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer deserializer;

    @Test
    public void testWithResolved() {
        // Arrange
        JavaType fullType = new JavaType();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();

        AtomicReferenceDeserializer expected = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);

        // Act
        AtomicReferenceDeserializer actual = deserializer.withResolved(typeDeser, valueDeser);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetNullValue() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        AtomicReference<Object> expected = new AtomicReference<Object>();

        // Act
        AtomicReference<Object> actual = deserializer.getNullValue(ctxt);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetEmptyValue() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        Object expected = new AtomicReference<Object>();

        // Act
        Object actual = deserializer.getEmptyValue(ctxt);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReferenceValue() {
        // Arrange
        Object contents = new Object();
        AtomicReference<Object> expected = new AtomicReference<Object>(contents);

        // Act
        AtomicReference<Object> actual = deserializer.referenceValue(contents);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetReferenced() {
        // Arrange
        AtomicReference<Object> reference = new AtomicReference<Object>();
        Object expected = reference.get();

        // Act
        Object actual = deserializer.getReferenced(reference);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testUpdateReference() {
        // Arrange
        AtomicReference<Object> reference = new AtomicReference<Object>();
        Object contents = new Object();
        AtomicReference<Object> expected = reference;
        expected.set(contents);

        // Act
        AtomicReference<Object> actual = deserializer.updateReference(reference, contents);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSupportsUpdate() {
        // Arrange
        DeserializationConfig config = new DeserializationConfig();
        Boolean expected = Boolean.TRUE;

        // Act
        Boolean actual = deserializer.supportsUpdate(config);

        // Assert
        Assert.assertEquals(expected, actual);
    }
}