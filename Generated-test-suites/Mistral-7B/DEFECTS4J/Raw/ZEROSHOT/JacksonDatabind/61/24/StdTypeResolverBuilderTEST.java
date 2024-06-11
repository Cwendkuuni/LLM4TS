import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testStdTypeResolverBuilder_DefaultConstruction() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 assertNull(builder.getIdType());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getTypeIdVisible());
 assertNull(builder.getCustomIdResolver());
 }

 @Test
 public void testStdTypeResolverBuilder_Init() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 assertEquals(JsonTypeInfo.Id.CLASS, builder.getIdType());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getTypeIdVisible());
 assertNull(builder.getCustomIdResolver());

 builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
 assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, builder.getIdType());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getTypeIdVisible());
 assertNull(builder.getCustomIdResolver());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStdTypeResolverBuilder_InitNullIdType() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(null, null);
 }

 @Test
 public void testStdTypeResolverBuilder_Include() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.inclusion(JsonTypeInfo.As.PROPERTY);
 assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStdTypeResolverBuilder_IncludeNull() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.inclusion(null);
 }

 @Test
 public void testStdTypeResolverBuilder_TypeProperty() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.typeProperty("test");
 assertEquals("test", builder.getTypeProperty());
 }

 @Test
 public void testStdTypeResolverBuilder_DefaultImpl() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.defaultImpl(String.class);
 assertEquals(String.class, builder.getDefaultImpl());
 }

 @Test
 public void testStdTypeResolverBuilder_TypeIdVisible() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());
 }

 @Test
 public void testStdTypeResolverBuilder_BuildTypeSerializer() throws JsonProcessingException {
 ClassIdType baseType = mapper.getTypeFactory().constructType(String.class);
 TypeSerializer serializer = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null)
 .buildTypeSerializer(mapper.getSerializationConfig(), baseType, null);
 assertNull(serializer);

 baseType = mapper.getTypeFactory().constructType(Integer.class);
 serializer = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null)
 .buildTypeSerializer(mapper.getSerializationConfig(), baseType, null);
 assertNotNull(serializer);
 }

 @Test
 public void testStdTypeResolverBuilder_BuildTypeDeserializer() throws JsonProcessingException {
 ClassIdType baseType = mapper.getTypeFactory().constructType(String.class);
 TypeDeserializer deserializer = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null)
 .buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, null);
 assertNull(deserializer);

 baseType = mapper.getTypeFactory().constructType(Integer.class);
 deserializer = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null)
 .buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, null);
 assertNotNull(deserializer);
 }

 @Test
 public void testStdTypeResolverBuilder_CustomIdResolver() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeIdResolver customResolver = mapper.getTypeFactory().activator().getInjectableValue(TypeIdResolver.class);
 builder.init(JsonTypeInfo.Id.NAME, customResolver);
 assertEquals(customResolver, builder.getCustomIdResolver());
 }

}