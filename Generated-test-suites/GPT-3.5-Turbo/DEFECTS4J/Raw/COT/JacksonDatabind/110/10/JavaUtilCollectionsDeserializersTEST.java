import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

  @Test
  public void testFindForCollection() {
    try {
      DeserializationContext ctxt = null;
      JavaType type = null;
      JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
      assertEquals(null, result);
    } catch (JsonMappingException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testFindForMap() {
    try {
      DeserializationContext ctxt = null;
      JavaType type = null;
      JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
      assertEquals(null, result);
    } catch (JsonMappingException e) {
      e.printStackTrace();
    }
  }

}