import org.junit.Assert;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        // Given
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();

        // When
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        // Then
        Assert.assertEquals(objectIdReader.propertyName, property.getName());
        Assert.assertEquals(objectIdReader.getIdType(), property.getType());
        Assert.assertEquals(metadata, property.getMetadata());
        Assert.assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testCopyConstructor() {
        // Given
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);

        // When
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);

        // Then
        Assert.assertEquals(src.getName(), property.getName());
        Assert.assertEquals(src.getType(), property.getType());
        Assert.assertEquals(src.getMetadata(), property.getMetadata());
        Assert.assertEquals(deser, property.getValueDeserializer());
    }

    @Test
    public void testCopyConstructorWithNewName() {
        // Given
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName("newName");

        // When
        ObjectIdValueProperty property = src.withName(newName);

        // Then
        Assert.assertEquals(newName, property.getName());
        Assert.assertEquals(src.getType(), property.getType());
        Assert.assertEquals(src.getMetadata(), property.getMetadata());
        Assert.assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testCopyConstructorWithStringName() {
        // Given
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        String newName = "newName";

        // When
        ObjectIdValueProperty property = src.withName(newName);

        // Then
        Assert.assertEquals(new PropertyName(newName), property.getName());
        Assert.assertEquals(src.getType(), property.getType());
        Assert.assertEquals(src.getMetadata(), property.getMetadata());
        Assert.assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testWithValueDeserializer() {
        // Given
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);

        // When
        ObjectIdValueProperty property = src.withValueDeserializer(deser);

        // Then
        Assert.assertEquals(src.getName(), property.getName());
        Assert.assertEquals(src.getType(), property.getType());
        Assert.assertEquals(src.getMetadata(), property.getMetadata());
        Assert.assertEquals(deser, property.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        // Given
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        // When
        Annotation annotation = property.getAnnotation(null);

        // Then
        Assert.assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        // Given
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        // When
        AnnotatedMember member = property.getMember();

        // Then
        Assert.assertNull(member);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        // Given
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        // When
        property.deserializeAndSet(jp, ctxt, instance);

        // Then
        // Verify that there are no exceptions thrown
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        // Given
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        // When
        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);

        // Then
        // Verify that there are no exceptions thrown
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        // Given
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        // When
        property.set(instance, null);

        // Then
        // Verify that UnsupportedOperationException is thrown
    }

    @Test
    public void testSetAndReturn() throws IOException {
        // Given
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        // When
        Object result = property.setAndReturn(instance, null);

        // Then
        // Verify that there are no exceptions thrown
    }

}