import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Before
 public void setup() {
 builder = new StdTypeResolverBuilder();
 }

 // Testing constructor
 @Test
 public void testEmptyConstructor() {
 // Verify default initialization
 Assert.assertNull(builder.getDefaultImpl());
 Assert.assertEquals("", builder.getTypeProperty());
 Assert.assertFalse(builder.isTypeIdVisible());
 }

 // Testing noTypeInfoBuilder() method
 @Test
 public void testNoTypeInfoBuilder() {
 builder = StdTypeResolverBuilder.noTypeInfoBuilder();
 // Verify default initialization
 Assert.assertNull(builder.getDefaultImpl());
 Assert.assertEquals("", builder.getTypeProperty());
 Assert.assertFalse(builder.isTypeIdVisible());
 }

 // Testing init() method
 @Test
 public void testInit() {
 // Testing with valid arguments
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 TypeIdResolver idRes = new StdTypeResolverBuilder().init(idType, null);
 // Verify initialization
 Assert.assertEquals(idType, builder.getIdType());
 Assert.assertNull(builder.getCustomIdResolver());
 Assert.assertEquals("", builder.getTypeProperty());
 Assert.assertFalse(builder.isTypeIdVisible());
 Assert.assertNull(builder.getDefaultImpl());
 // Testing with null idType argument
 builder.init(null, null);
 // Verify exception is thrown
 Assert.assertThrows(IllegalArgumentException.class, () -> builder.buildTypeSerializer(null, null, null));
 }

 // Testing buildTypeSerializer() method
 @Test
 public void testBuildTypeSerializer() {
 SerializationConfig config = new SerializationConfig();
 JavaType baseType = null;
 Collection<NamedType> subtypes = null;
 TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 // Verify null return for JsonTypeInfo.Id.NONE
 Assert.assertNull(typeSerializer);
 // Testing with valid arguments
 idType = JsonTypeInfo.Id.CLASS;
 idRes = builder.idResolver(config, baseType, subtypes, true, false);
 builder.init(idType, idRes);
 typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 // Verify type serializer instance is not null
 Assert.assertNotNull(typeSerializer);
 // Testing with invalid JsonTypeInfo.Id value
 builder.init(JsonTypeInfo.Id.NONE, idRes);
 typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
 // Verify null return for invalid JsonTypeInfo.Id value
 Assert.assertNull(typeSerializer);
 }

 // Testing buildTypeDeserializer() method
 @Test
 public void testBuildTypeDeserializer() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = null;
 Collection<NamedType> subtypes = null;
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 // Verify null return for JsonTypeInfo.Id.NONE
 Assert.assertNull(typeDeserializer);
 // Testing with valid arguments
 idType = JsonTypeInfo.Id.CLASS;
 idRes = builder.idResolver(config, baseType, subtypes, false, true);
 builder.init(idType, idRes);
 typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 // Verify type deserializer instance is not null
 Assert.assertNotNull(typeDeserializer);
 // Testing with invalid JsonTypeInfo.Id value
 builder.init(JsonTypeInfo.Id.NONE, idRes);
 typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 // Verify null return for invalid JsonTypeInfo.Id value
 Assert.assertNull(typeDeserializer);
 }

 // Testing inclusion() method
 @Test
 public void testInclusion() {
 // Testing with null includeAs argument
 builder.inclusion(null);
 // Verify exception is thrown
 Assert.assertThrows(IllegalArgumentException.class, () -> builder.buildTypeSerializer(null, null, null));
 // Testing with valid JsonTypeInfo.As value
 JsonTypeInfo.As includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
 builder.inclusion(includeAs);
 // Verify initialization
 Assert.assertEquals(includeAs, builder.getIncludeAs());
 Assert.assertNull(builder.getTypeProperty());
 Assert.assertFalse(builder.isTypeIdVisible());
 Assert.assertNull(builder.getDefaultImpl());
 // Testing with invalid JsonTypeInfo.As value
 builder.inclusion(JsonTypeInfo.As.valueOf("INVALID_VALUE"));
 // Verify exception is thrown
 Assert.assertThrows(IllegalArgumentException.class, () -> builder.buildTypeSerializer(null, null, null));
 }

 // Testing typeProperty() method
 @Test
 public void testTypeProperty() {
 // Testing with null typeIdPropName argument
 builder.typeProperty(null);
 // Verify exception is thrown
 Assert.assertThrows(IllegalArgumentException.class, () -> builder.buildTypeSerializer(null, null, null));
 // Testing with empty typeIdPropName argument
 builder.typeProperty("");
 // Verify initialization
 Assert.assertEquals("", builder.getTypeProperty());
 Assert.assertNull(builder.getDefaultImpl());
 // Testing with valid typeIdPropName argument
 builder.typeProperty("typeIdPropName");
 // Verify initialization
 Assert.assertEquals("typeIdPropName", builder.getTypeProperty());
 Assert.assertNull(builder.getDefaultImpl());
 }

 // Testing defaultImpl() method
 @Test
 public void testDefaultImpl() {
 // Testing with null defaultImpl argument
 builder.defaultImpl(null);
 // Verify exception is thrown
 Assert.assertThrows(NullPointerException.class, () -> builder.buildTypeSerializer(null, null, null));
 // Testing with valid Class argument
 Class<?> defaultImpl = Object.class;
 builder.defaultImpl(defaultImpl);
 // Verify initialization
 Assert.assertEquals(defaultImpl, builder.getDefaultImpl());
 Assert.assertNull(builder.getTypeProperty());
 Assert.assertFalse(builder.isTypeIdVisible());
 }

 // Testing typeIdVisibility() method
 @Test
 public void testTypeIdVisibility() {
 // Testing with false isVisible argument
 builder.typeIdVisibility(false);
 // Verify initialization
 Assert.assertFalse(builder.isTypeIdVisible());
 Assert.assertNull(builder.getTypeProperty());
 Assert.assertNull(builder.getDefaultImpl());
 // Testing with true isVisible argument
 builder.typeIdVisibility(true);
 // Verify initialization
 Assert.assertTrue(builder.isTypeIdVisible());
 Assert.assertNull(builder.getTypeProperty());
 Assert.assertNull(builder.getDefaultImpl());
 }

 // Testing getDefaultImpl() method
 @Test
 public void testGetDefaultImpl() {
 // Testing with null defaultImpl
 Assert.assertNull(builder.getDefaultImpl());
 // Testing with custom defaultImpl
 Class<?> defaultImpl = Object.class;
 builder.defaultImpl(defaultImpl);
 // Verify initialization
 Assert.assertEquals(defaultImpl, builder.getDefaultImpl());
 }

 // Testing getTypeProperty() method
 @Test
 public void testGetTypeProperty() {
 // Testing with empty typeProperty
 String typeProperty = "";
 builder.typeProperty(typeProperty);
 // Verify initialization
 Assert.assertEquals(typeProperty, builder.getTypeProperty());
 // Testing with null typeProperty
 builder.typeProperty(null);
 // Verify initialization
 Assert.assertEquals("", builder.getTypeProperty());
 // Testing with custom typeProperty
 typeProperty = "typeIdPropName";
 builder.typeProperty(typeProperty);
 // Verify initialization
 Assert.assertEquals(typeProperty, builder.getTypeProperty());
 }

 // Testing isTypeIdVisible() method
 @Test
 public void testIsTypeIdVisible() {
 // Testing with false isVisible
 builder.typeIdVisibility(false);
 // Verify initialization
 Assert.assertFalse(builder.isTypeIdVisible());
 // Testing with true isVisible
 builder.typeIdVisibility(true);
 // Verify initialization
 Assert.assertTrue(builder.isTypeIdVisible());
 }

 // Testing helper method idResolver() for serialization
 @Test
 public void testIdResolverForSerialization() {
 MapperConfig<?> config = new MapperConfig<>();
 JavaType baseType = null;
 Collection<NamedType> subtypes = null;
 // Testing with null config
 Assert.assertThrows(NullPointerException.class, () -> builder.idResolver(null, baseType, subtypes, true, false));
 // Testing with valid arguments
 TypeIdResolver idRes = builder.idResolver(config, baseType, subtypes, true, false);
 // Verify id resolver instance is not null
 Assert.assertNotNull(idRes);
 }

 // Testing helper method idResolver() for deserialization
 @Test
 public void testIdResolverForDeserialization() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = null;
 Collection<NamedType> subtypes = null;
 // Testing with null config
 Assert.assertThrows(NullPointerException.class, () -> builder.idResolver(null, baseType, subtypes, false, true));
 // Testing with valid arguments
 TypeIdResolver idRes = builder.idResolver(config, baseType, subtypes, false, true);
 // Verify id resolver instance is not null
 Assert.assertNotNull(idRes);
 }
}