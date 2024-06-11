import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolver;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ConverterUtil;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 builder = new StdTypeResolverBuilder();
 }

 @Test
 public void testInitNullIdType() {
 try {
 builder.init(null, null);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testInitNullIdRes() {
 try {
 builder.init(JsonTypeInfo.Id.CLASS, null);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testBuildTypeSerializerNullIdType() {
 try {
 builder.buildTypeSerializer(null, JavaType.construct(), null);
 Assert.fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testBuildTypeSerializerClassInclusion() {
 TypeSerializer serializer = builder.init(JsonTypeInfo.Id.CLASS, null)
 .buildTypeSerializer(new SerializationConfig(), JavaType.construct(), null);
 assertNull(serializer);
 }

 @Test
 public void testBuildTypeSerializerWrongInclusion() {
 try {
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .includeAs(JsonTypeInfo.As.EXTERNAL_PROPERTY)
 .buildTypeSerializer(new SerializationConfig(), JavaType.construct(), null);
 Assert.fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testBuildTypeSerializerPropertyInclusion() {
 TypeSerializer serializer = builder.init(JsonTypeInfo.Id.CLASS, null)
 .includeAs(JsonTypeInfo.As.PROPERTY)
 .buildTypeSerializer(new SerializationConfig(), JavaType.construct(), null);
 assertNotNull(serializer);
 }

 @Test
 public void testBuildTypeSerializerWrapperArrayInclusion() {
 TypeSerializer serializer = builder.init(JsonTypeInfo.Id.CLASS, null)
 .includeAs(JsonTypeInfo.As.WRAPPER_ARRAY)
 .buildTypeSerializer(new SerializationConfig(), JavaType.construct(), null);
 assertNotNull(serializer);
 }

 @Test
 public void testBuildTypeSerializerWrapperObjectInclusion() {
 TypeSerializer serializer = builder.init(JsonTypeInfo.Id.CLASS, null)
 .includeAs(JsonTypeInfo.As.WRAPPER_OBJECT)
 .buildTypeSerializer(new SerializationConfig(), JavaType.construct(), null);
 assertNotNull(serializer);
 }

 @Test
 public void testBuildTypeSerializerExternalPropertyInclusion() {
 TypeSerializer serializer = builder.init(JsonTypeInfo.Id.CLASS, null)
 .includeAs(JsonTypeInfo.As.EXTERNAL_PROPERTY)
 .buildTypeSerializer(new SerializationConfig(), JavaType.construct(), null);
 assertNotNull(serializer);
 }

 @Test
 public void testBuildTypeDeserializerNullIdType() {
 try {
 builder.buildTypeDeserializer(null, JavaType.construct(), null);
 Assert.fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testBuildTypeDeserializerWrongInclusion() {
 try {
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .includeAs(JsonTypeInfo.As.EXTERNAL_PROPERTY)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 Assert.fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testBuildTypeDeserializerPropertyInclusion() {
 TypeDeserializer deserializer = builder.init(JsonTypeInfo.Id.CLASS, null)
 .includeAs(JsonTypeInfo.As.PROPERTY)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 assertNotNull(deserializer);
 }

 @Test
 public void testBuildTypeDeserializerWrapperArrayInclusion() {
 TypeDeserializer deserializer = builder.init(JsonTypeInfo.Id.CLASS, null)
 .includeAs(JsonTypeInfo.As.WRAPPER_ARRAY)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 assertNotNull(deserializer);
 }

 @Test
 public void testBuildTypeDeserializerWrapperObjectInclusion() {
 TypeDeserializer deserializer = builder.init(JsonTypeInfo.Id.CLASS, null)
 .includeAs(JsonTypeInfo.As.WRAPPER_OBJECT)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 assertNotNull(deserializer);
 }

 @Test
 public void testBuildTypeDeserializerExternalPropertyInclusion() {
 TypeDeserializer deserializer = builder.init(JsonTypeInfo.Id.CLASS, null)
 .includeAs(JsonTypeInfo.As.EXTERNAL_PROPERTY)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 assertNotNull(deserializer);
 }

 @Test
 public void testInitWithCustomIdResolver() {
 TypeIdResolver customIdResolver = mapper.getConfiguration().getTypeFactory()
 .constructValueType(TypeIdResolver.class);
 builder.init(JsonTypeInfo.Id.CLASS, customIdResolver);
 }

 @Test(expected = IllegalStateException.class)
 public void testInitWithNullCustomIdResolver() {
 try {
 builder.init(JsonTypeInfo.Id.CLASS, null);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIdResolver() {
 TypeIdResolver idResolver = builder.idResolver(mapper.getConfiguration(),
 JavaType.construct(), null, true, false);
 assertNotNull(idResolver);
 }

 @Test
 public void testTypeDeserializerWithDefaultImpl() {
 TypeDeserializer deserializer = builder.init(JsonTypeInfo.Id.CLASS, null)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 assertNotNull(deserializer);
 }

 @Test
 public void testTypeDeserializerWithNullDefaultImpl() {
 try {
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null, null);
 Assert.fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testTypeDeserializerWithWrongType() {
 try {
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null, String.class);
 Assert.fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testTypeDeserializerWithCustomClass() {
 Class<?> customClass = mapper.getTypeFactory().constructType(String[].class);
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .defaultImpl(customClass)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 }

 @Test
 public void testTypeDeserializerWithBooleanTypeIdVisible() {
 boolean isVisible = true;
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .typeIdVisibility(isVisible)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 }

 @Test
 public void testTypeDeserializerWithFalseBooleanTypeIdVisible() {
 boolean isVisible = false;
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .typeIdVisibility(isVisible)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 }

 @Test
 public void testTypeSerializerWithBooleanTypeIdVisible() {
 boolean isVisible = true;
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .typeIdVisibility(isVisible)
 .buildTypeSerializer(new SerializationConfig(), JavaType.construct(), null);
 }

 @Test
 public void testTypeSerializerWithFalseBooleanTypeIdVisible() {
 boolean isVisible = false;
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .typeIdVisibility(isVisible)
 .buildTypeSerializer(new SerializationConfig(), JavaType.construct(), null);
 }

 @Test
 public void testTypeSerializerWithCustomTypeProperty() {
 String typeProperty = "type";
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .typeProperty(typeProperty)
 .buildTypeSerializer(new SerializationConfig(), JavaType.construct(), null);
 }

 @Test
 public void testTypeSerializerWithNullTypeProperty() {
 try {
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .typeProperty(null)
 .buildTypeSerializer(new SerializationConfig(), JavaType.construct(), null);
 Assert.fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testTypeSerializerWithEmptyTypeProperty() {
 try {
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .typeProperty("")
 .buildTypeSerializer(new SerializationConfig(), JavaType.construct(), null);
 Assert.fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testTypeSerializerWithNonEmptyTypeProperty() {
 String typeProperty = "type";
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .typeProperty(typeProperty)
 .buildTypeSerializer(new SerializationConfig(), JavaType.construct(), null);
 }

 @Test
 public void testTypeDeserializerWithCustomTypeProperty() {
 String typeProperty = "type";
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .typeProperty(typeProperty)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 }

 @Test
 public void testTypeDeserializerWithNullTypeProperty() {
 try {
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .typeProperty(null)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 Assert.fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testTypeDeserializerWithEmptyTypeProperty() {
 try {
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .typeProperty("")
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 Assert.fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testTypeDeserializerWithNonEmptyTypeProperty() {
 String typeProperty = "type";
 builder.init(JsonTypeInfo.Id.CLASS, null)
 .typeProperty(typeProperty)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 }

 @Test
 public void testTypeDeserializerWithCustomClassTypeId() {
 Class<?> customClass = mapper.getTypeFactory().constructType(String[].class);
 builder.init(JsonTypeInfo.Id.CUSTOM, null)
 .defaultImpl(customClass)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 }

 @Test
 public void testTypeDeserializerWithNullDefaultImpl() {
 try {
 builder.init(JsonTypeInfo.Id.CUSTOM, null)
 .defaultImpl(null)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(), null);
 Assert.fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testTypeDeserializerWithWrongDefaultImplType() {
 try {
 builder.init(JsonTypeInfo.Id.CUSTOM, null)
 .defaultImpl(JavaType.construct(String.class))
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(int.class), null);
 Assert.fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testTypeDeserializerWithCustomClassTypeIdAndCustomClass() {
 Class<?> customClass = mapper.getTypeFactory().constructType(String[].class);
 builder.init(JsonTypeInfo.Id.CUSTOM, null)
 .defaultImpl(customClass)
 .buildTypeDeserializer(new DeserializationConfig(), JavaType.construct(customClass), null);
 }

 @Test
 public void testIdResolverWithNullBaseType() {
 try {
 builder.idResolver(null, null, null, true, false);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIdResolverWithNullSubtypes() {
 try {
 builder.idResolver(new SerializationConfig(), null, null, true, false);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIdResolverWithNullForSer() {
 try {
 builder.idResolver(new SerializationConfig(), JavaType.construct(), null, false, false);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIdResolverWithNullForDeser() {
 try {
 builder.idResolver(new SerializationConfig(), JavaType.construct(), null, true, false);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIdResolverWithCustomIdResolver() {
 TypeIdResolver customIdResolver = mapper.getConfiguration().getTypeFactory()
 .constructValueType(TypeIdResolver.class);
 builder.idResolver(new SerializationConfig(), JavaType.construct(), null, true, false, customIdResolver);
 }

 @Test
 public void testIdResolverWithNullCustomIdResolver() {
 try {
 builder.idResolver(new SerializationConfig(), JavaType.construct(), null, true, false, null);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}