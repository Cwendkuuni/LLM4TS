import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Test
 public void testStdTypeResolverBuilderDefaultConstruction() {
 builder = new StdTypeResolverBuilder();
 assertNull(builder._customIdResolver);
 assertNull(builder._idType);
 assertNull(builder._includeAs);
 assertNull(builder._typeProperty);
 assertFalse(builder._typeIdVisible);
 assertNull(builder._defaultImpl);
 }

 @Test
 public void testStdTypeResolverBuilderInit() {
 Class<?> idType = JsonTypeInfo.Id.CLASS;
 builder = new StdTypeResolverBuilder().init(idType, null);
 assertEquals(idType, builder._idType);
 assertNull(builder._customIdResolver);
 assertNull(builder._includeAs);
 assertNull(builder._typeProperty);
 assertFalse(builder._typeIdVisible);
 assertNull(builder._defaultImpl);
 }

 @Test
 public void testStdTypeResolverBuilderInitWithCustomIdResolver() {
 TypeIdResolver customIdResolver = new SimpleTypeResolver(); // replace with actual custom id resolver
 builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, customIdResolver);
 assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
 assertEquals(customIdResolver, builder._customIdResolver);
 assertNull(builder._includeAs);
 assertNull(builder._typeProperty);
 assertFalse(builder._typeIdVisible);
 assertNull(builder._defaultImpl);
 }

 @Test
 public void testStdTypeResolverBuilderTypeSerializer() {
 MapperConfig<Object> config = new MapperConfig<Object>().build();
 JavaType baseType = config.constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
 TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertNotNull(serializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testStdTypeResolverBuilderTypeSerializerWithUnknownIncludeType() {
 MapperConfig<Object> config = new MapperConfig<Object>().build();
 JavaType baseType = config.constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.NONE, null);
 builder.buildTypeSerializer(config, baseType, subtypes);
 }

 @Test
 public void testStdTypeResolverBuilderTypeDeserializer() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = config.constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
 TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertNotNull(deserializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testStdTypeResolverBuilderTypeDeserializerWithUnknownIncludeType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = config.constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.NONE, null);
 builder.buildTypeDeserializer(config, baseType, subtypes);
 }

 @Test
 public void testStdTypeResolverBuilderInclusion() {
 JsonTypeInfo.As includeAs = As.PROPERTY;
 builder = new StdTypeResolverBuilder();
 builder.inclusion(includeAs);
 assertEquals(includeAs, builder._includeAs);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStdTypeResolverBuilderInclusionNull() {
 builder = new StdTypeResolverBuilder();
 builder.inclusion(null);
 }

 @Test
 public void testStdTypeResolverBuilderTypeProperty() {
 String typeProperty = "type";
 builder = new StdTypeResolverBuilder();
 builder.typeProperty(typeProperty);
 assertEquals(typeProperty, builder._typeProperty);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStdTypeResolverBuilderTypePropertyNull() {
 builder = new StdTypeResolverBuilder();
 builder.typeProperty(null);
 }

 @Test
 public void testStdTypeResolverBuilderDefaultImpl() {
 builder = new StdTypeResolverBuilder();
 Class<?> defaultImpl = String.class;
 builder.defaultImpl(defaultImpl);
 assertEquals(defaultImpl, builder._defaultImpl);
 }

 @Test
 public void testStdTypeResolverBuilderTypeIdVisibility() {
 builder = new StdTypeResolverBuilder();
 builder.typeIdVisibility(true);
 assertTrue(builder._typeIdVisible);
 }

 @Test
 public void testStdTypeResolverBuilderTypeIdVisibilityFalse() {
 builder = new StdTypeResolverBuilder();
 builder.typeIdVisibility(false);
 assertFalse(builder._typeIdVisible);
 }

 @Test
 public void testStdTypeResolverBuilderIdResolver() {
 MapperConfig<Object> config = new MapperConfig<Object>().build();
 JavaType baseType = config.constructType(String.class);
 Collection<NamedType> subtypes = null;
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
 TypeIdResolver idResolver = builder.idResolver(config, baseType, subtypes, true, false);
 assertNotNull(idResolver);
 }
}