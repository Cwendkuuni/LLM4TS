
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

  @Test
  public void testGetProperty() {
    // Given
    BeanProperty property = null;
    AnnotatedMember setter = null;
    JavaType type = null;
    KeyDeserializer keyDeser = null;
    JsonDeserializer<Object> valueDeser = null;
    TypeDeserializer typeDeser = null;

    // When
    SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
    BeanProperty actualProperty = sap.getProperty();

    // Then
    assertEquals(property, actualProperty);
  }

  @Test
  public void testHasValueDeserializer() {
    // Given
    BeanProperty property = null;
    AnnotatedMember setter = null;
    JavaType type = null;
    KeyDeserializer keyDeser = null;
    JsonDeserializer<Object> valueDeser = null;
    TypeDeserializer typeDeser = null;

    // When
    SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
    boolean actualHasValueDeserializer = sap.hasValueDeserializer();

    // Then
    assertFalse(actualHasValueDeserializer);
  }

  @Test
  public void testGetType() {
    // Given
    BeanProperty property = null;
    AnnotatedMember setter = null;
    JavaType type = null;
    KeyDeserializer keyDeser = null;
    JsonDeserializer<Object> valueDeser = null;
    TypeDeserializer typeDeser = null;

    // When
    SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
    JavaType actualType = sap.getType();

    // Then
    assertEquals(type, actualType);
  }

  @Test
  public void testDeserializeAndSet() {
    // Given
    BeanProperty property = null;
    AnnotatedMember setter = null;
    JavaType type = null;
    KeyDeserializer keyDeser = null;
    JsonDeserializer<Object> valueDeser = null;
    TypeDeserializer typeDeser = null;
    SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);

    // TODO: Complete the test case

    // When

    // Then

  }

  @Test
  public void testDeserialize() {
    // Given
    BeanProperty property = null;
    AnnotatedMember setter = null;
    JavaType type = null;
    KeyDeserializer keyDeser = null;
    JsonDeserializer<Object> valueDeser = null;
    TypeDeserializer typeDeser = null;
    SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
    JsonParser p = null;
    DeserializationContext ctxt = null;

    // TODO: Complete the test case

    // When

    // Then

  }

  @Test
  public void testSet() {
    // Given
    BeanProperty property = null;
    AnnotatedMember setter = null;
    JavaType type = null;
    KeyDeserializer keyDeser = null;
    JsonDeserializer<Object> valueDeser = null;
    TypeDeserializer typeDeser = null;
    SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
    Object instance = null;
    Object propName = null;
    Object value = null;

    // TODO: Complete the test case

    // When

    // Then

  }
}
