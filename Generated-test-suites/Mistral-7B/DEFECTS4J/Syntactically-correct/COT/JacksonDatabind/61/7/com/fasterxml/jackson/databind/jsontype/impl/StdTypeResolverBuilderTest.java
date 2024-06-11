import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Test
 public void testConstructor() {
 builder = new StdTypeResolverBuilder();
 Assert.assertNull(builder.getIdType());
 Assert.assertNull(builder.getIncludeAs());
 Assert.assertNull(builder.getTypeProperty());
 Assert.assertNull(builder.getTypeIdVisible());
 Assert.assertNull(builder.getDefaultImpl());
 Assert.assertNull(builder.getCustomIdResolver());
 }

 @Test
 public void testNoTypeInfoBuilder() {
 builder = StdTypeResolverBuilder.noTypeInfoBuilder();
 Assert.assertNull(builder.getIdType());
 Assert.assertNull(builder.getIncludeAs());
 Assert.assertNull(builder.getTypeProperty());
 Assert.assertNull(builder.getTypeIdVisible());
 Assert.assertNull(builder.getDefaultImpl());
 Assert.assertNull(builder.getCustomIdResolver());
 }

 @Test
 public void testInit() {
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 builder = new StdTypeResolverBuilder();
 builder.init(idType, null);
 Assert.assertEquals(idType, builder.getIdType());
 Assert.assertNull(builder.getIncludeAs());
 Assert.assertEquals("", builder.getTypeProperty());
 Assert.assertFalse(builder.isTypeIdVisible());
 Assert.assertNull(builder.getDefaultImpl());
 Assert.assertNull(builder.getCustomIdResolver());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullIdType() {
 builder = new StdTypeResolverBuilder();
 builder.init(null, null);
 }

 @Test
 public void testBuildTypeSerializerNullConfigAndBaseType() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 Assert.assertNull(builder.buildTypeSerializer(null, null, null));
 }

 @Test
 public void testBuildTypeSerializer() throws Exception {
 MapperConfig config = new MapperConfig();
 JavaType baseType = new TypeReference<Integer>() {}.getType();
 Collection<NamedType> subtypes = null;
 TypeSerializer serializer = builder.init(JsonTypeInfo.Id.CLASS, null)
 .buildTypeSerializer(config, baseType, subtypes);
 Assert.assertNotNull(serializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerInvalidInclusion() {
 MapperConfig config = new MapperConfig();
 JavaType baseType = new TypeReference<Integer>() {}.getType();
 Collection<NamedType> subtypes = null;
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.buildTypeSerializer(config, baseType, subtypes);
 }

 @Test
 public void testBuildTypeDeserializerNullConfigAndBaseType() {
 MapperConfig config = new MapperConfig();
 JavaType baseType = new TypeReference<Integer>() {}.getType();
 Collection<NamedType> subtypes = null;
 TypeDeserializer deserializer = builder.init(JsonTypeInfo.Id.CLASS, null)
 .buildTypeDeserializer(config, baseType, subtypes);
 Assert.assertNull(deserializer);
 }

 @Test
 public void testBuildTypeDeserializer() throws Exception {
 MapperConfig config = new MapperConfig();
 JavaType baseType = new TypeReference<Integer>() {}.getType();
 Collection<NamedType> subtypes = null;
 TypeDeserializer deserializer = builder.init(JsonTypeInfo.Id.CLASS, null)
 .buildTypeDeserializer(config, baseType, subtypes);
 Assert.assertNotNull(deserializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerInvalidInclusion() {
 MapperConfig config = new MapperConfig();
 JavaType baseType = new TypeReference<Integer>() {}.getType();
 Collection<NamedType> subtypes = null;
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.buildTypeDeserializer(config, baseType, subtypes);
 }

 @Test
 public void testSerializationConfigNullAndBaseType() {
 JsonTypeInfo idType = new JsonTypeInfo.Builder()
 .id(JsonTypeInfo.Id.CLASS)
 .build();
 builder = new StdTypeResolverBuilder();
 builder.init(idType, null);
 TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
 Assert.assertNull(serializer);
 }

 @Test
 public void testSerializationConfig() throws Exception {
 JsonTypeInfo idType = new JsonTypeInfo.Builder()
 .id(JsonTypeInfo.Id.CLASS)
 .build();
 builder = new StdTypeResolverBuilder();
 builder.init(idType, null);
 TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), null, null);
 Assert.assertNotNull(serializer);
 }

 @Test
 public void testDeserializationConfigNullAndBaseType() {
 JsonTypeInfo idType = new JsonTypeInfo.Builder()
 .id(JsonTypeInfo.Id.CLASS)
 .build();
 builder = new StdTypeResolverBuilder();
 builder.init(idType, null);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(null, null, null);
 Assert.assertNull(deserializer);
 }

 @Test
 public void testDeserializationConfig() throws Exception {
 JsonTypeInfo idType = new JsonTypeInfo.Builder()
 .id(JsonTypeInfo.Id.CLASS)
 .build();
 builder = new StdTypeResolverBuilder();
 builder.init(idType, null);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(new MapperConfig(), null, null);
 Assert.assertNotNull(deserializer);
 }

 @Test
 public void testTypeIdResolverNullConfigAndBaseType() {
 MapperConfig config = new MapperConfig();
 JavaType baseType = new TypeReference<Integer>() {}.getType();
 Collection<NamedType> subtypes = null;
 TypeIdResolver idResolver = builder.idResolver(config, baseType, subtypes, true, false);
 Assert.assertNull(idResolver);
 }

 @Test
 public void testTypeIdResolver() throws Exception {
 MapperConfig config = new MapperConfig();
 JavaType baseType = new TypeReference<Integer>() {}.getType();
 Collection<NamedType> subtypes = null;
 TypeIdResolver idResolver = builder.idResolver(config, baseType, subtypes, true, false);
 Assert.assertNotNull(idResolver);
 }

}