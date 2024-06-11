import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {
  
  private static class MockTypeIdResolver implements TypeIdResolver {
    @Override
    public void init(JavaType baseType) {}
    
    @Override
    public String idFromValue(Object value) {
      return null;
    }
    
    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
      return null;
    }
    
    @Override
    public String idFromBaseType() {
      return null;
    }
    
    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
      return null;
    }
    
    @Override
    public JavaType typeFromId(DatabindContext context, String id, Object extra)
        throws IOException {
      return null;
    }
    
    @Override
    public String getDescForKnownTypeIds() {
      return null;
    }
    
    @Override
    public JsonTypeInfo.Id getMechanism() {
      return null;
    }
  }
  
  private class MockTypeDeserializer extends TypeDeserializerBase {
    private static final long serialVersionUID = 1L;
    
    public MockTypeDeserializer(JavaType baseType, TypeIdResolver idRes,
        String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
      super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }
    
    @Override
    public TypeDeserializer forProperty(BeanProperty prop) {
      return null;
    }
    
    @Override
    public JsonTypeInfo.As getTypeInclusion() {
      return null;
    }
    
  }
  
  @Test
  public void testFindDeserializer_WhenDeserializerFound() throws IOException {
    JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
    TypeIdResolver idResolver = new MockTypeIdResolver();
    String typePropertyName = "";
    boolean typeIdVisible = true;
    JavaType defaultImpl = null;
    MockTypeDeserializer deserializer = new MockTypeDeserializer(baseType, idResolver, typePropertyName,
        typeIdVisible, defaultImpl);
    NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
    
    DeserializationContext ctxt = new DefaultDeserializationContext.Builder(ObjectMapper.builder(), new DefaultDeserializationConfig(new ObjectMapper().getDeserializationContext().getFactory())).build();
    
    deserializer._deserializers.put("testTypeId", nullifyingDeserializer);
    
    JsonDeserializer<Object> result = deserializer._findDeserializer(ctxt, "testTypeId");
    
    Assert.assertEquals(nullifyingDeserializer, result);
  }
  
  @Test
  public void testFindDeserializer_WhenDeserializerNotFound() throws IOException {
    JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
    TypeIdResolver idResolver = new MockTypeIdResolver();
    String typePropertyName = "";
    boolean typeIdVisible = true;
    JavaType defaultImpl = null;
    MockTypeDeserializer deserializer = new MockTypeDeserializer(baseType, idResolver, typePropertyName,
        typeIdVisible, defaultImpl);
        
    DeserializationContext ctxt = new DefaultDeserializationContext.Builder(ObjectMapper.builder(), new DefaultDeserializationConfig(new ObjectMapper().getDeserializationContext().getFactory())).build();
    
    JsonDeserializer<Object> result = deserializer._findDeserializer(ctxt, "testTypeId");
    
    Assert.assertEquals(null, result);
  }
  
  @Test
  public void testDeserializeWithNativeTypeId() throws IOException {
    JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
    TypeIdResolver idResolver = new MockTypeIdResolver();
    String typePropertyName = "";
    boolean typeIdVisible = true;
    JavaType defaultImpl = null;
    MockTypeDeserializer deserializer = new MockTypeDeserializer(baseType, idResolver, typePropertyName,
        typeIdVisible, defaultImpl);
        
    JsonParser jp = null;
    DeserializationContext ctxt = new DefaultDeserializationContext.Builder(ObjectMapper.builder(), new DefaultDeserializationConfig(new ObjectMapper().getDeserializationContext().getFactory())).build();
    Object result = deserializer._deserializeWithNativeTypeId(jp, ctxt);
    
    Assert.assertEquals(null, result);
  }
  
  @Test
  public void testHandleUnknownTypeId() throws IOException {
    JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
    TypeIdResolver idResolver = new MockTypeIdResolver();
    String typePropertyName = "";
    boolean typeIdVisible = true;
    JavaType defaultImpl = null;
    MockTypeDeserializer deserializer = new MockTypeDeserializer(baseType, idResolver, typePropertyName,
        typeIdVisible, defaultImpl);
        
    DeserializationContext ctxt = new DefaultDeserializationContext.Builder(ObjectMapper.builder(), new DefaultDeserializationConfig(new ObjectMapper().getDeserializationContext().getFactory())).build();
    JavaType result = deserializer._handleUnknownTypeId(ctxt, "testTypeId", idResolver, baseType);
    
    Assert.assertEquals(null, result);
  }
}