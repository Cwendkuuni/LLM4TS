import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

  @Test
  public void deserialize_ReturnsCollectionOfString() throws IOException {
    // Arrange
    StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
    JsonParser parser = null;
    DeserializationContext ctxt = null;

    // Act
    Collection<String> result = deserializer.deserialize(parser, ctxt);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void deserialize_WithCustomValueDeserializer_ReturnsCollectionOfString() throws IOException {
    // Arrange
    JsonDeserializer<String> valueDeserializer = null;
    StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, valueDeserializer, null);
    JsonParser parser = null;
    DeserializationContext ctxt = null;

    // Act
    Collection<String> result = deserializer.deserialize(parser, ctxt);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void deserialize_WhenDelegateDeserializerNotNull_ReturnsCollectionOfString() throws IOException {
    // Arrange
    JsonDeserializer<Object> delegateDeserializer = null;
    ValueInstantiator valueInstantiator = new ValueInstantiator() {
      @Override
      public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
        return new Object();
      }
      @Override
      public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
        return new Object();
      }
    };
    StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, valueInstantiator, delegateDeserializer, null, null, null);
    JsonParser parser = null;
    DeserializationContext ctxt = null;

    // Act
    Collection<String> result = deserializer.deserialize(parser, ctxt);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void deserialize_WhenDelegateDeserializerNotNullAndValueDeserializerNotNull_ReturnsCollectionOfString() throws IOException {
    // Arrange
    JsonDeserializer<Object> delegateDeserializer = null;
    JsonDeserializer<String> valueDeserializer = null;
    ValueInstantiator valueInstantiator = new ValueInstantiator() {
      @Override
      public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
        return new Object();
      }
      @Override
      public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
        return new Object();
      }
    };
    StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, valueInstantiator, delegateDeserializer, valueDeserializer, null, null);
    JsonParser parser = null;
    DeserializationContext ctxt = null;

    // Act
    Collection<String> result = deserializer.deserialize(parser, ctxt);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void deserialize_WithResultCollection_ReturnsCollectionOfString() throws IOException {
    // Arrange
    StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
    JsonParser parser = null;
    DeserializationContext ctxt = null;
    Collection<String> result = new ArrayList<>();

    // Act
    Collection<String> actualResult = deserializer.deserialize(parser, ctxt, result);

    // Assert
    assertSame(result, actualResult);
  }

  @Test
  public void deserializeUsingCustom_ReturnsCollectionOfString() throws IOException {
    // Arrange
    JsonDeserializer<String> deser = null;
    JsonParser parser = null;
    DeserializationContext ctxt = null;
    Collection<String> result = new ArrayList<>();

    // Act
    Collection<String> actualResult = StringCollectionDeserializer.deserializeUsingCustom(parser, ctxt, result, deser);

    // Assert
    assertSame(result, actualResult);
  }

  @Test
  public void createContextual_ReturnsStringCollectionDeserializer() throws JsonMappingException {
    // Arrange
    DeserializationContext ctxt = null;
    BeanProperty property = null;
    JsonDeserializer<Object> delegateDeser = null;
    JsonDeserializer<String> valueDeser = null;
    NullValueProvider nuller = null;
    Boolean unwrapSingle = null;
    StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);

    // Act
    StringCollectionDeserializer actualDeserializer = deserializer.createContextual(ctxt, property);

    // Assert
    assertSame(deserializer, actualDeserializer);
  }

  @Test
  public void getContentDeserializer_ReturnsValueDeserializer() {
    // Arrange
    StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
    JsonDeserializer<String> valueDeser = null;

    // Act
    JsonDeserializer<Object> result = deserializer.getContentDeserializer(valueDeser);

    // Assert
    assertSame(valueDeser, result);
  }

  @Test
  public void getValueInstantiator_ReturnsValueInstantiator() {
    // Arrange
    ValueInstantiator valueInstantiator = new ValueInstantiator() {
      @Override
      public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
        return new Object();
      }
      @Override
      public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
        return new Object();
      }
    };
    StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, valueInstantiator, null);

    // Act
    ValueInstantiator result = deserializer.getValueInstantiator();

    // Assert
    assertSame(valueInstantiator, result);
  }

  @Test
  public void deserializeWithType_ReturnsObject() throws IOException {
    // Arrange
    JsonParser p = null;
    DeserializationContext ctxt = null;
    TypeDeserializer typeDeserializer = null;
    StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);

    // Act
    Object result = deserializer.deserializeWithType(p, ctxt, typeDeserializer);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void handleNonArray_WhenCanWrap_ReturnsCollectionWithString() throws IOException {
    // Arrange
    boolean canWrap = true;
    JsonDeserializer<String> valueDes = null;
    StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, valueDes, null);
    JsonParser p = null;
    DeserializationContext ctxt = null;
    Collection<String> result = new ArrayList<>();

    // Act
    Collection<String> actualResult = deserializer.handleNonArray(p, ctxt, result);

    // Assert
    assertSame(result, actualResult);
  }

  @Test (expected=JsonMappingException.class)
  public void handleNonArray_WhenCannotWrap_ThrowsJsonMappingException() throws IOException {
    // Arrange
    boolean canWrap = false;
    JsonDeserializer<String> valueDes = null;
    StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, valueDes, null);
    JsonParser p = null;
    DeserializationContext ctxt = null;
    Collection<String> result = new ArrayList<>();

    // Act
    deserializer.handleNonArray(p, ctxt, result);
  }
}