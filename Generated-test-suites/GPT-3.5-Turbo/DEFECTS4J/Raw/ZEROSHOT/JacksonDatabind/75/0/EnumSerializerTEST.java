import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import java.lang.reflect.Type;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import java.lang.reflect.Type;
import java.io.IOException;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {
  
  private EnumSerializer enumSerializer;
  
  @Before
  public void setup(){
    EnumValues enumValues = new EnumValues(enumValues.values(), enumValues.serializedValues(), enumValues.internalMap());
    enumSerializer = new EnumSerializer(enumValues, true);
  }
  
  @Test
  public void testSerialize(){
    Enum enumVal = Enum.TEST_VALUE_1;
    JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
    SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
    try {
      enumSerializer.serialize(enumVal, gen, serializers);
      gen.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetSchema(){
    SerializerProvider provider = new ObjectMapper().getSerializerProvider();
    Type typeHint = new Type(){};
    try{
      JsonNode node = enumSerializer.getSchema(provider, typeHint);
      assertNotNull(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testAcceptJsonFormatVisitor(){
    SerializerProvider provider = new ObjectMapper().getSerializerProvider();
    JavaType typeHint = new JavaType(){};
    try{
      JsonFormatVisitorWrapper visitor = null;
      enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
      assertNotNull(visitor);
    } catch (IOException e){
      e.printStackTrace();
    }
  }
  
}