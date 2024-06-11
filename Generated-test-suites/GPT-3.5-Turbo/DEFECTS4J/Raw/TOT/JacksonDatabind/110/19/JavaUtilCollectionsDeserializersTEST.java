import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JavaUtilCollectionsDeserializersTest {

  @Test
  public void testFindForCollection() {
    DeserializationContext ctxt = new DeserializationContext(null, null);
    JavaType type = JavaType.constructSimpleType(Object.class, null);
    
    // Test for CLASS_AS_ARRAYS_LIST
    try {
      JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
      assertNull(deserializer);
    } catch (JsonMappingException e) {
      fail("An exception occurred: " + e.getMessage());
    }
    
    // Test for CLASS_SINGLETON_LIST
    type = JavaType.constructSimpleType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST, null);
    try {
      JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
      assertNotNull(deserializer);
    } catch (JsonMappingException e) {
      fail("An exception occurred: " + e.getMessage());
    }
    
    // Test for CLASS_SINGLETON_SET
    type = JavaType.constructSimpleType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET, null);
    try {
      JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
      assertNotNull(deserializer);
    } catch (JsonMappingException e) {
      fail("An exception occurred: " + e.getMessage());
    }
    
    // Test for CLASS_UNMODIFIABLE_LIST
    type = JavaType.constructSimpleType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST, null);
    try {
      JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
      assertNotNull(deserializer);
    } catch (JsonMappingException e) {
      fail("An exception occurred: " + e.getMessage());
    }
    
    // Test for CLASS_UNMODIFIABLE_SET
    type = JavaType.constructSimpleType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET, null);
    try {
      JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
      assertNotNull(deserializer);
    } catch (JsonMappingException e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testFindForMap() {
    DeserializationContext ctxt = new DeserializationContext(null, null);
    JavaType type = JavaType.constructSimpleType(Object.class, null);
    
    // Test for CLASS_SINGLETON_MAP
    try {
      JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
      assertNull(deserializer);
    } catch (JsonMappingException e) {
      fail("An exception occurred: " + e.getMessage());
    }
    
    // Test for CLASS_UNMODIFIABLE_MAP
    type = JavaType.constructSimpleType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP, null);
    
    try {
      JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
      assertNotNull(deserializer);
    } catch (JsonMappingException e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }

}