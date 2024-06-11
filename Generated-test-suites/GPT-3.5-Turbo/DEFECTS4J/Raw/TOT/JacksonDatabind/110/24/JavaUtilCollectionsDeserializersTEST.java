import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

  @Test
  public void testFindForCollection_SingletonList_Success() throws JsonMappingException {
    // Given
    ObjectMapper mapper = new ObjectMapper();
    JavaType type = TypeFactory.defaultInstance().constructType(
        JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);

    // When
    JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);

    // Then
    assertNotNull(deserializer);
  }

  @Test(expected = JsonMappingException.class)
  public void testFindForCollection_UnmodifiableList_Failure() throws JsonMappingException {
    // Given
    ObjectMapper mapper = new ObjectMapper();
    JavaType type = TypeFactory.defaultInstance().constructType(
        JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);

    // When
    JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);

    // Then
    // JsonMappingException should be thrown
  }

  @Test
  public void testFindForCollection_UnmodifiableSet_Success() throws JsonMappingException {
    // Given
    ObjectMapper mapper = new ObjectMapper();
    JavaType type = TypeFactory.defaultInstance().constructType(
        JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);

    // When
    JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);

    // Then
    assertNotNull(deserializer);
  }

  @Test(expected = JsonMappingException.class)
  public void testFindForCollection_SingletonSet_Failure() throws JsonMappingException {
    // Given
    ObjectMapper mapper = new ObjectMapper();
    JavaType type = TypeFactory.defaultInstance().constructType(
        JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);

    // When
    JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);

    // Then
    // JsonMappingException should be thrown
  }

  @Test(expected = JsonMappingException.class)
  public void testFindForCollection_UnknownType_Failure() throws JsonMappingException {
    // Given
    ObjectMapper mapper = new ObjectMapper();
    JavaType type = TypeFactory.defaultInstance().constructType(String.class);

    // When
    JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);

    // Then
    // JsonMappingException should be thrown
  }

  @Test
  public void testFindForMap_SingletonMap_Success() throws JsonMappingException {
    // Given
    ObjectMapper mapper = new ObjectMapper();
    JavaType type = TypeFactory.defaultInstance().constructType(
        JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);

    // When
    JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), type);

    // Then
    assertNotNull(deserializer);
  }

  @Test(expected = JsonMappingException.class)
  public void testFindForMap_UnmodifiableMap_Failure() throws JsonMappingException {
    // Given
    ObjectMapper mapper = new ObjectMapper();
    JavaType type = TypeFactory.defaultInstance().constructType(
        JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);

    // When
    JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), type);

    // Then
    // JsonMappingException should be thrown
  }

  @Test(expected = JsonMappingException.class)
  public void testFindForMap_UnknownType_Failure() throws JsonMappingException {
    // Given
    ObjectMapper mapper = new ObjectMapper();
    JavaType type = TypeFactory.defaultInstance().constructType(String.class);

    // When
    JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), type);

    // Then
    // JsonMappingException should be thrown
  }

}