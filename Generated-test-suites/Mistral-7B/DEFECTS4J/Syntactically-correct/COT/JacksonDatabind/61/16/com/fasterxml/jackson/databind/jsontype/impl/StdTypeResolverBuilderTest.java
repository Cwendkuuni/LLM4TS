import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.ser.std.AsArrayTypeSerializer;
import com.fasterxml.jackson.databind.ser.std.AsPropertyTypeSerializer;
import com.fasterxml.jackson.databind.ser.std.AsWrapperTypeSerializer;
import com.fasterxml.jackson.databind.deser.std.AsArrayTypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.AsExternalTypeDeserializer;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Test
 public void testConstructor() {
 StdTypeResolverBuilder instance = new StdTypeResolverBuilder();
 assertNotNull(instance);
 }

 @Test
 public void testNoTypeInfoBuilder() {
 builder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertNotNull(builder);
 }

 @Test
 public void testInit() {
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 TypeIdResolver idRes = new MinimalClassNameIdResolver(Object.class, new TypeFactory());
 builder = new StdTypeResolverBuilder();
 builder.init(idType, idRes);
 assertEquals(idType, builder._idType);
 assertEquals(idRes, builder._customIdResolver);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullIdType() {
 builder = new StdTypeResolverBuilder();
 builder.init(null, new MinimalClassNameIdResolver(Object.class, new TypeFactory()));
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testBuildTypeSerializerNullIdType() {
 builder = new StdTypeResolverBuilder();
 SerializationConfig config = new SerializationConfig();
 JavaType baseType = ObjectType.TYPE;
 Collection<NamedType> subtypes = null;
 TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);
 assertNull(result);
 }

 @Test
 public void testBuildTypeSerializerClassType() {
 builder = new StdTypeResolverBuilder();
 SerializationConfig config = new SerializationConfig();
 JavaType baseType = ObjectType.TYPE;
 Collection<NamedType> subtypes = new ArrayList<>();
 subtypes.add(new SimpleType(String.class));
 TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);
 assertNotNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerUnknownInclusionType() {
 builder = new StdTypeResolverBuilder();
 SerializationConfig config = new SerializationConfig();
 JavaType baseType = ObjectType.TYPE;
 Collection<NamedType> subtypes = new ArrayList<>();
 TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testBuildTypeDeserializerNullIdType() {
 builder = new StdTypeResolverBuilder();
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = ObjectType.TYPE;
 Collection<NamedType> subtypes = null;
 TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertNull(result);
 }

 @Test
 public void testBuildTypeDeserializerClassType() {
 builder = new StdTypeResolverBuilder();
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = ObjectType.TYPE;
 Collection<NamedType> subtypes = new ArrayList<>();
 subtypes.add(new SimpleType(String.class));
 TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertNotNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerUnknownInclusionType() {
 builder = new StdTypeResolverBuilder();
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = ObjectType.TYPE;
 Collection<NamedType> subtypes = new ArrayList<>();
 TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testSetterMethods() {
 builder = new StdTypeResolverBuilder();
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 TypeIdResolver idRes = new MinimalClassNameIdResolver(Object.class, new TypeFactory());
 builder.init(idType, idRes);
 builder.inclusion(JsonTypeInfo.As.PROPERTY);
 builder.typeProperty("typeProp");
 builder.defaultImpl(String.class);
 builder.typeIdVisibility(true);
 assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
 assertEquals("typeProp", builder._typeProperty);
 assertEquals(String.class, builder._defaultImpl);
 assertEquals(true, builder._typeIdVisible);
 }

 @Test
 public void testGetters() {
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.CLASS, new MinimalClassNameIdResolver(Object.class, new TypeFactory()));
 assertEquals(JsonTypeInfo.Id.CLASS, builder.get_idType());
 assertEquals(null, builder.getTypeProperty());
 assertEquals(false, builder.isTypeIdVisible());
 assertEquals(null, builder._customIdResolver);
 }

 @Test
 public void testIdResolver() {
 MapperConfig<?> config = new MapperConfig<>();
 JavaType baseType = ObjectType.TYPE;
 Collection<NamedType> subtypes = new ArrayList<>();
 TypeIdResolver result = builder.idResolver(config, baseType, subtypes, true, false);
 assertNotNull(result);
 }

 @Test
 public void testIdResolverNullIdType() {
 MapperConfig<?> config = new MapperConfig<>();
 JavaType baseType = ObjectType.TYPE;
 Collection<NamedType> subtypes = new ArrayList<>();
 try {
 builder.idResolver(config, baseType, subtypes, true, false);
 fail("Should throw IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }
}