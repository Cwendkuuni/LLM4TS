import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testStdTypeResolverBuilder_init() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 TypeIdResolver idResolver = TypeNameIdResolver.construct(new MapperConfig.Defaults(), Object.class, null, false, false);
 builder.init(idType, idResolver);
 assertEquals(idType, builder._idType);
 assertEquals(idResolver, builder._customIdResolver);
 }

 @Test
 public void testStdTypeResolverBuilder_noTypeInfoBuilder() {
 StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertNull(builder._idType);
 assertNull(builder._customIdResolver);
 }

 @Test
 public void testStdTypeResolverBuilder_buildTypeSerializer() throws JsonProcessingException {
 JsonTypeInfo jsonTypeInfo = new JsonTypeInfo.Builder()
 .use(JsonTypeInfo.Id.CLASS)
 .property("type")
 .build();
 JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = new ArrayList<>();
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(objectMapper.getSerializationConfig(), javaType, subtypes);
 assertNotNull(typeSerializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testStdTypeResolverBuilder_buildTypeSerializer_invalidIdType() throws JsonProcessingException {
 JsonTypeInfo jsonTypeInfo = new JsonTypeInfo.Builder()
 .use(JsonTypeInfo.Id.NONE) // should not be valid
 .property("type")
 .build();
 JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = new ArrayList<>();
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(jsonTypeInfo, null);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(objectMapper.getSerializationConfig(), javaType, subtypes);
 fail("Should have thrown an exception");
 }

 @Test
 public void testStdTypeResolverBuilder_buildTypeDeserializer() throws JsonProcessingException {
 JsonTypeInfo jsonTypeInfo = new JsonTypeInfo.Builder()
 .use(JsonTypeInfo.Id.CLASS)
 .property("type")
 .build();
 JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = new ArrayList<>();
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), javaType, subtypes);
 assertNotNull(typeDeserializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testStdTypeResolverBuilder_buildTypeDeserializer_invalidIdType() throws JsonProcessingException {
 JsonTypeInfo jsonTypeInfo = new JsonTypeInfo.Builder()
 .use(JsonTypeInfo.Id.NONE) // should not be valid
 .property("type")
 .build();
 JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
 Collection<NamedType> subtypes = new ArrayList<>();
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(jsonTypeInfo, null);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), javaType, subtypes);
 fail("Should have thrown an exception");
 }

 @Test
 public void testStdTypeResolverBuilder_inclusion() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.inclusion(JsonTypeInfo.As.PROPERTY);
 assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
 }

 @Test
 public void testStdTypeResolverBuilder_typeProperty() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.typeProperty("typeProperty");
 assertEquals("typeProperty", builder._typeProperty);
 }

 @Test
 public void testStdTypeResolverBuilder_defaultImpl() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.defaultImpl(String.class);
 assertEquals(String.class, builder._defaultImpl);
 }

 @Test
 public void testStdTypeResolverBuilder_typeIdVisibility() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.typeIdVisibility(true);
 assertTrue(builder._typeIdVisible);
 }

 @Test
 public void testStdTypeResolverBuilder_getIdType() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 assertEquals(JsonTypeInfo.Id.NONE, builder.getIdType());
 }

 @Test
 public void testStdTypeResolverBuilder_getIncludeAs() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, builder.getIncludeAs());
 }

 @Test
 public void testStdTypeResolverBuilder_getTypeProperty() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 assertEquals("", builder.getTypeProperty());
 }

 @Test
 public void testStdTypeResolverBuilder_isTypeIdVisible() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 assertFalse(builder.isTypeIdVisible());
 }
}