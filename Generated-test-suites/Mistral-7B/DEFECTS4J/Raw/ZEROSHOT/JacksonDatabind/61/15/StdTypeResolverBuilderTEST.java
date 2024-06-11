import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new MapperFactory(new AnnotationIntrospector())
 .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)
 .build();
 builder = StdTypeResolverBuilder.noTypeInfoBuilder();
 }

 @Test
 public void testInitWithNullIdType() {
 try {
 builder.init(null, null);
 fail("initialization with null idType should throw exception");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testInitWithEmptyIdType() {
 try {
 builder.init(JsonTypeInfo.Id.EMPTY, null);
 fail("initialization with empty idType should throw exception");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testInitWithValidIdType() {
 builder.init(JsonTypeInfo.Id.CLASS, null);
 assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
 }

 @Test
 public void testInitWithCustomIdResolver() {
 TypeIdResolver customIdResolver = new TypeIdResolverBase() {
 @Override
 public TypeId idFromValue(Object value) {
 return TypeId.valueOf("customTypeId");
 }
 };
 builder.init(JsonTypeInfo.Id.NONE, customIdResolver);
 assertEquals(customIdResolver, builder._customIdResolver);
 }

 @Test
 public void testBuildTypeSerializerWithNoneTypeId() {
 TypeSerializer serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
 JsonType.getInstance(), null);
 assertNull(serializer);
 }

 @Test
 public void testBuildTypeSerializerWithClassTypeId() {
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeSerializer serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
 JsonType.getInstance(), null);
 assertNotNull(serializer);
 }

 @Test
 public void testBuildTypeDeserializerWithNoneTypeId() {
 TypeDeserializer deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
 JsonType.getInstance(), null);
 assertNull(deserializer);
 }

 @Test
 public void testBuildTypeDeserializerWithClassTypeId() {
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
 JsonType.getInstance(), null);
 assertNotNull(deserializer);
 }

 @Test
 public void testInclusionWrapperArray() {
 builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
 assertSame(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
 }

 @Test
 public void testInclusionProperty() {
 builder.inclusion(JsonTypeInfo.As.PROPERTY);
 assertSame(JsonTypeInfo.As.PROPERTY, builder._includeAs);
 }

 @Test
 public void testInclusionWrapperObject() {
 builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
 assertSame(JsonTypeInfo.As.WRAPPER_OBJECT, builder._includeAs);
 }

 @Test
 public void testInclusionExternalProperty() {
 builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
 assertSame(JsonTypeInfo.As.EXTERNAL_PROPERTY, builder._includeAs);
 }

 @Test
 public void testTypePropertyWithEmptyString() {
 builder.typeProperty("");
 assertEquals("", builder._typeProperty);
 }

 @Test
 public void testTypePropertyWithValidString() {
 builder.typeProperty("typePropertyName");
 assertEquals("typePropertyName", builder._typeProperty);
 }

 @Test
 public void testDefaultImplWithNullClass() {
 try {
 builder.defaultImpl(null);
 fail("initialization with null class should throw exception");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testDefaultImplWithValidClass() {
 builder.defaultImpl(String.class);
 assertEquals(String.class, builder._defaultImpl);
 }

 @Test
 public void testTypeIdVisibilityWithFalse() {
 builder.typeIdVisibility(false);
 assertFalse(builder._typeIdVisible);
 }

 @Test
 public void testTypeIdVisibilityWithTrue() {
 builder.typeIdVisibility(true);
 assertTrue(builder._typeIdVisible);
 }

 @Test
 public void testIdResolverWithClassIdType() {
 builder.init(JsonTypeInfo.Id.CLASS, null);
 TypeIdResolver idResolver = builder.idResolver(mapper.getSerializationConfig(),
 JsonType.getInstance(), null, true, false);
 assertInstanceOf(ClassNameIdResolver.class, idResolver);
 }

 @Test
 public void testIdResolverWithNameIdType() {
 builder.init(JsonTypeInfo.Id.NAME, null);
 TypeIdResolver idResolver = builder.idResolver(mapper.getSerializationConfig(),
 JsonType.getInstance(), null, true, false);
 assertInstanceOf(TypeNameIdResolver.class, idResolver);
 }

 @Test
 public void testIdResolverWithCustomIdType() {
 builder.init(JsonTypeInfo.Id.CUSTOM, null);
 TypeIdResolver idResolver = builder.idResolver(mapper.getSerializationConfig(),
 JsonType.getInstance(), null, true, false);
 assertNull(idResolver);
 }
}