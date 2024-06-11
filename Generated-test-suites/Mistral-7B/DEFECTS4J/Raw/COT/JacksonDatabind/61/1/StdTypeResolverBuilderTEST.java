import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdTypeResolverBuilderTest {

 private ObjectMapper mapper;
 private StdTypeResolverBuilder builder;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 builder = new StdTypeResolverBuilder();
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 StdTypeResolverBuilder instance = new StdTypeResolverBuilder();
 assertNotNull(instance);
 }

 @Test
 public void testNoTypeInfoBuilder() {
 // Test static method noTypeInfoBuilder
 StdTypeResolverBuilder instance = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertNotNull(instance);
 }

 @Test
 public void testInit() {
 // Test init method
 builder.init(JsonTypeInfo.Id.CLASS, null);
 assertNotNull(builder._idType);
 assertNull(builder._customIdResolver);
 assertNull(builder._typeProperty);
 assertFalse(builder._typeIdVisible);
 assertNull(builder._defaultImpl);

 builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
 assertSame(JsonTypeInfo.Id.MINIMAL_CLASS, builder._idType);

 builder.init(JsonTypeInfo.Id.NAME, null);
 assertSame(JsonTypeInfo.Id.NAME, builder._idType);

 builder.init(JsonTypeInfo.Id.CUSTOM, new MyTypeIdResolver());
 assertSame(JsonTypeInfo.Id.CUSTOM, builder._idType);
 assertNotNull(builder._customIdResolver);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullIdType() {
 builder.init(null, null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testBuildTypeSerializerNullIdType() {
 // Test null idType
 TypeSerializer result = builder.buildTypeSerializer(new MapperFactory(), null, null);
 assertNull(result);
 }

 @Test
 public void testBuildTypeSerializerWithIdType() {
 // Test buildTypeSerializer with valid idType
 JavaType baseType = mapper.getTypeFactory().constructType(String[].class);
 Collection<NamedType> subtypes = mapper.getTypeFactory()
 .constructTypes(new ObjectNode(mapper.createObjectNode())
 .put("foo", mapper.getTypeFactory().constructType(Integer.class))
 .put("bar", mapper.getTypeFactory().constructType(Long.class))
 .put("baz", mapper.getTypeFactory().constructType(Double.class)));
 TypeSerializer result = builder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes);
 assertNotNull(result);
 }

 @Test
 public void testBuildTypeDeserializerNullIdType() {
 // Test null idType
 TypeDeserializer result = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), null, null);
 assertNull(result);
 }

 @Test
 public void testBuildTypeDeserializerWithIdType() {
 // Test buildTypeDeserializer with valid idType
 JavaType baseType = mapper.getTypeFactory().constructType(String[].class);
 Collection<NamedType> subtypes = mapper.getTypeFactory()
 .constructTypes(new ObjectNode(mapper.createObjectNode())
 .put("foo", mapper.getTypeFactory().constructType(Integer.class))
 .put("bar", mapper.getTypeFactory().constructType(Long.class))
 .put("baz", mapper.getTypeFactory().constructType(Double.class)));
 TypeDeserializer result = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
 assertNotNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerUnsupportedInclusionType() {
 builder.init(JsonTypeInfo.Id.NONE, null);
 JavaType baseType = mapper.getTypeFactory().constructType(String[].class);
 Collection<NamedType> subtypes = mapper.getTypeFactory()
 .constructTypes(new ObjectNode(mapper.createObjectNode())
 .put("foo", mapper.getTypeFactory().constructType(Integer.class))
 .put("bar", mapper.getTypeFactory().constructType(Long.class))
 .put("baz", mapper.getTypeFactory().constructType(Double.class)));
 TypeDeserializer result = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testTypeProperty() {
 // Test typeProperty method
 builder.init(JsonTypeInfo.Id.NAME, null);
 builder.typeProperty("myTypeProperty");
 assertEquals("myTypeProperty", builder.getTypeProperty());
 }

 @Test
 public void testDefaultImpl() {
 // Test defaultImpl method
 builder.init(JsonTypeInfo.Id.NAME, null);
 builder.defaultImpl(String[].class);
 assertEquals(String[].class, builder.getDefaultImpl());
 }

 @Test
 public void testTypeIdVisibility() {
 // Test typeIdVisibility method
 builder.init(JsonTypeInfo.Id.NAME, null);
 builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());
 }
}

class MyTypeIdResolver extends TypeIdResolverBase {
 @Override
 public TypeId idFromValue(Serializable value, JavaType baseType) {
 return null;
 }

 @Override
 public JavaType resolvedType(TypeId id, JavaType baseType) {
 return null;
 }
}