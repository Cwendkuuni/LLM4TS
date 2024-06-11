import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {
  
  @Test
  public void testConstructorWithObjectIdReaderAndMetadata() {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    PropertyMetadata metadata = new PropertyMetadata();
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

    assertEquals(objectIdReader.propertyName, property.getName());
    assertEquals(objectIdReader.getIdType(), property.getType());
    assertEquals(metadata, property.getMetadata());
    assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
    assertEquals(objectIdReader, property._objectIdReader);
  }
  
  @Test
  public void testWithNewName() {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    PropertyName newName = new PropertyName("newName");
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
    ObjectIdValueProperty newProperty = property.withName(newName);

    assertEquals(newName, newProperty.getName());
    assertEquals(property.getType(), newProperty.getType());
    assertEquals(property.getMetadata(), newProperty.getMetadata());
    assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
    assertEquals(property._objectIdReader, newProperty._objectIdReader);
  }
  
  @Test
  public void testWithValueDeserializer() {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    JsonDeserializer<?> deser = new JsonDeserializer<>();
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
    ObjectIdValueProperty newProperty = property.withValueDeserializer(deser);

    assertEquals(property.getName(), newProperty.getName());
    assertEquals(property.getType(), newProperty.getType());
    assertEquals(property.getMetadata(), newProperty.getMetadata());
    assertEquals(deser, newProperty.getValueDeserializer());
    assertEquals(property._objectIdReader, newProperty._objectIdReader);
  }
  
  @Test
  public void testGetAnnotation() {
    ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
    assertNull(property.getAnnotation(Object.class));
  }
  
  @Test
  public void testGetMember() {
    ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
    assertNull(property.getMember());
  }
  
  @Test
  public void testDeserializeAndSet() throws IOException {
    ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

    JsonParser parser = new JsonParser();
    DeserializationContext context = new DeserializationContext();
    Object instance = new Object();

    property.deserializeAndSet(parser, context, instance);

    // Add assertions here
  }
  
  @Test
  public void testDeserializeSetAndReturn() throws IOException {
    ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

    JsonParser parser = new JsonParser();
    DeserializationContext context = new DeserializationContext();
    Object instance = new Object();

    Object result = property.deserializeSetAndReturn(parser, context, instance);

    // Add assertions here
    
    return result;
  }
  
  @Test
  public void testSet() throws IOException {
    ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

    Object instance = new Object();
    Object value = new Object();

    property.set(instance, value);

    // Add assertions here
  }
  
  @Test
  public void testSetAndReturn() throws IOException {
    ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

    Object instance = new Object();
    Object value = new Object();

    Object result = property.setAndReturn(instance, value);

    // Add assertions here
    
    return result;
  }

  @Test
  public void testDeserializeSetAndReturnWithNullId() throws IOException {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

    JsonParser parser = new JsonParser();
    DeserializationContext context = new DeserializationContext();
    Object instance = new Object();

    assertNull(property.deserializeSetAndReturn(parser, context, instance));
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testSetAndReturnWithNullIdProperty() throws IOException {
    ObjectIdReader objectIdReader = new ObjectIdReader();
    ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

    Object instance = new Object();
    Object value = new Object();

    property.setAndReturn(instance, value);
  }
}