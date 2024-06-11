import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

 private static ObjectMapper mapper;

 @BeforeClass
 public static void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testTypeResolverBuilder_NoTypeInfoBuilder() {
 StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
 TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
 JavaType.getTypeReference(String.class), null);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
 JavaType.getTypeReference(String.class), null);
 Assert.assertNull(typeSerializer);
 Assert.assertNull(typeDeserializer);
 }

 @Test
 public void testTypeResolverBuilder_Default() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
 JavaType.getTypeReference(String.class), null);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
 JavaType.getTypeReference(String.class), null);

 // Test serialization
 ObjectWriter objectWriter = mapper.writerForTypeValue(new StringTypeIdentifier("test"), String.class);
 String json = objectWriter.writeValueAsString(new Object());

 // Test deserialization
 JsonParser jsonParser = mapper.parserForObject(json);
 Object readValue = mapper.readValue(jsonParser, new TypeReference<Object>() {});

 Assert.assertSame(typeSerializer.serializeValue(mapper.getSerializationConfig(), null, readValue), json);
 Assert.assertEquals(typeDeserializer.deserialize(mapper.getDeserializationConfig(), null, typeSerializer), readValue);
 }

 @Test
 public void testTypeResolverBuilder_IncludeAsWrapperArray() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
 JavaType.getTypeReference(List.class), null);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
 JavaType.getTypeReference(List.class), null);

 Object someList = new ArrayList<>();
 ObjectMapper someMapper = new ObjectMapper();
 someMapper.registerModule(new SimpleModule().setSerializerImpl(ListSerializer.class, new ListSerializerImpl()));
 ObjectWriter objectWriter = someMapper.writerForTypeValue(new StringTypeIdentifier("test"), new TypeReference<List<Object>>() {});
 String json = objectWriter.writeValueAsString(someList);

 JsonParser jsonParser = mapper.parserForObject(json);
 List<Object> readValue = mapper.readValue(jsonParser, new TypeReference<List<Object>>() {});

 Assert.assertSame(typeSerializer.serializeValue(mapper.getSerializationConfig(), null, readValue.get(0)), json);
 Assert.assertEquals(typeDeserializer.deserialize(mapper.getDeserializationConfig(), null, typeSerializer), readValue);
 }

 // Add more test cases for other inclusion types and error scenarios here

}