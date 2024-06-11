import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Test
 public void testStdTypeResolverBuilderDefaultConstructor() {
 builder = new StdTypeResolverBuilder();
 assertNull(builder.getIdType());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getCustomIdResolver());
 }

 @Test
 public void testStdTypeResolverBuilderInitMethod() {
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 builder = new StdTypeResolverBuilder().init(idType, null);
 assertEquals(idType, builder.getIdType());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getCustomIdResolver());
 }

 @Test
 public void testStdTypeResolverBuilderInitMethodWithCustomIdResolver() {
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 TypeIdResolver customIdResolver = new MinimalClassNameIdResolver(Object.class, null);
 builder = new StdTypeResolverBuilder().init(idType, customIdResolver);
 assertEquals(idType, builder.getIdType());
 assertEquals(customIdResolver, builder.getCustomIdResolver());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getDefaultImpl());
 }

 @Test
 public void testStdTypeResolverBuilderNoTypeInfoBuilder() {
 builder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertNull(builder.getIdType());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getCustomIdResolver());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStdTypeResolverBuilderInitMethodNullIdType() {
 builder = new StdTypeResolverBuilder().init(null, null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testBuildTypeSerializerNullConfig() {
 SerializationConfig config = null;
 builder = new StdTypeResolverBuilder();
 assertNull(builder.buildTypeSerializer(config, null, null));
 }

 @Test
 public void testBuildTypeSerializerNullBaseType() {
 SerializationConfig config = new SerializationConfig();
 builder = new StdTypeResolverBuilder();
 assertNull(builder.buildTypeSerializer(config, null, null));
 }

 @Test
 public void testBuildTypeSerializerNullSubtypes() {
 SerializationConfig config = new SerializationConfig();
 builder = new StdTypeResolverBuilder();
 assertNull(builder.buildTypeSerializer(config, null, null));
 }

 @Test
 public void testBuildTypeSerializerDefaultIdType() {
 SerializationConfig config = new SerializationConfig();
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeSerializer serializer = builder.buildTypeSerializer(config, null, null);
 assertNotNull(serializer);
 }

 @Test
 public void testBuildTypeSerializerClassIdType() {
 SerializationConfig config = new SerializationConfig();
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeSerializer serializer = builder.buildTypeSerializer(config, Object.class.getTypeId().getClass(), null);
 assertNotNull(serializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerUnknownInclusionType() {
 SerializationConfig config = new SerializationConfig();
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.include(JsonTypeInfo.As.EXTERNAL_PROPERTY);
 TypeSerializer serializer = builder.buildTypeSerializer(config, null, null);
 fail("Expected IllegalStateException");
 }

 @Test
 public void testBuildTypeSerializerTypeIdVisibleTrue() {
 SerializationConfig config = new SerializationConfig();
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeIdVisibility(true);
 TypeSerializer serializer = builder.buildTypeSerializer(config, Object.class, null);
 assertNotNull(serializer);
 }

 @Test
 public void testBuildTypeSerializerTypeIdVisibleFalse() {
 SerializationConfig config = new SerializationConfig();
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeIdVisibility(false);
 TypeSerializer serializer = builder.buildTypeSerializer(config, Object.class, null);
 assertNotNull(serializer);
 }

 @Test
 public void testBuildTypeDeserializerNullConfig() {
 DeserializationConfig config = null;
 builder = new StdTypeResolverBuilder();
 assertNull(builder.buildTypeDeserializer(config, null, null));
 }

 @Test
 public void testBuildTypeDeserializerNullBaseType() {
 DeserializationConfig config = new DeserializationConfig();
 builder = new StdTypeResolverBuilder();
 assertNull(builder.buildTypeDeserializer(config, null, null));
 }

 @Test
 public void testBuildTypeDeserializerNullSubtypes() {
 DeserializationConfig config = new DeserializationConfig();
 builder = new StdTypeResolverBuilder();
 assertNull(builder.buildTypeDeserializer(config, null, null));
 }

 @Test
 public void testBuildTypeDeserializerDefaultIdType() {
 DeserializationConfig config = new DeserializationConfig();
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(config, Object.class, null);
 assertNotNull(deserializer);
 }

 @Test
 public void testBuildTypeDeserializerClassIdType() {
 DeserializationConfig config = new DeserializationConfig();
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(config, Object.class.getTypeId().getClass(), null);
 assertNotNull(deserializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerUnknownInclusionType() {
 DeserializationConfig config = new DeserializationConfig();
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.include(JsonTypeInfo.As.EXTERNAL_PROPERTY);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(config, Object.class, null);
 fail("Expected IllegalStateException");
 }

 @Test
 public void testBuildTypeDeserializerTypeIdVisibleTrue() {
 DeserializationConfig config = new DeserializationConfig();
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeIdVisibility(true);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(config, Object.class, null);
 assertNotNull(deserializer);
 }

 @Test
 public void testBuildTypeDeserializerTypeIdVisibleFalse() {
 DeserializationConfig config = new DeserializationConfig();
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeIdVisibility(false);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(config, Object.class, null);
 assertNotNull(deserializer);
 }

 @Test
 public void testDefaultImpl() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.defaultImpl(String.class);
 assertEquals(String.class, builder.getDefaultImpl());
 }

 @Test
 public void testDefaultImplNull() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.defaultImpl(null);
 assertNull(builder.getDefaultImpl());
 }

 @Test
 public void testTypeProperty() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typePropertyName");
 assertEquals("typePropertyName", builder.getTypeProperty());
 }

 @Test
 public void testTypePropertyNull() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty(null);
 assertNull(builder.getTypeProperty());
 }

 @Test
 public void testTypeIdVisibility() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());
 }

 @Test
 public void testTypeIdVisibilityFalse() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeIdVisibility(false);
 assertFalse(builder.isTypeIdVisible());
 }

 @Test
 public void testGetCustomIdResolver() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeIdResolver customIdResolver = new MinimalClassNameIdResolver(Object.class, null);
 builder.setCustomIdResolver(customIdResolver);
 assertEquals(customIdResolver, builder.getCustomIdResolver());
 }

 @Test
 public void testGetCustomIdResolverNull() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 assertNull(builder.getCustomIdResolver());
 }

 @Test
 public void testGetDefaultImpl() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.defaultImpl(String.class);
 assertEquals(String.class, builder.getDefaultImpl());
 }

 @Test
 public void testGetDefaultImplNull() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 assertNull(builder.getDefaultImpl());
 }

 @Test
 public void testGetIncludeAs() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.include(JsonTypeInfo.As.PROPERTY);
 assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());
 }

 @Test
 public void testGetIncludeAsNull() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 assertNull(builder.getIncludeAs());
 }

 @Test
 public void testGetIdType() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 assertEquals(JsonTypeInfo.Id.CLASS, builder.getIdType());
 }

 @Test
 public void testGetIdTypeNull() {
 builder = new StdTypeResolverBuilder();
 builder.init(null, null);
 assertNull(builder.getIdType());
 }

 @Test
 public void testGetTypeProperty() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeProperty("typePropertyName");
 assertEquals("typePropertyName", builder.getTypeProperty());
 }

 @Test
 public void testGetTypePropertyNull() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 assertNull(builder.getTypeProperty());
 }

 @Test
 public void testIsTypeIdVisible() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());
 }

 @Test
 public void testIsTypeIdVisibleFalse() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, null);
 builder.typeIdVisibility(false);
 assertFalse(builder.isTypeIdVisible());
 }
}