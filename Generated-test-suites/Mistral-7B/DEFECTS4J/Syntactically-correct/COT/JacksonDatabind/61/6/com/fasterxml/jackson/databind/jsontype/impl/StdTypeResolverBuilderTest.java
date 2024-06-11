package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

  // Basic test cases for constructors and methods

  @Test
  public void testStdTypeResolverBuilderDefaultConstructor() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    assertNull(builder.getIdType());
    assertNull(builder.getIncludeAs());
    assertNull(builder.getTypeProperty());
    assertNull(builder.getDefaultImpl());
    assertNull(builder.getCustomIdResolver());
    assertFalse(builder.isTypeIdVisible());
  }

  @Test
  public void testStdTypeResolverBuilderInit() {
    JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.init(idType, null);
    assertEquals(idType, builder.getIdType());
    assertNull(builder.getIncludeAs());
    assertNull(builder.getTypeProperty());
    assertNull(builder.getDefaultImpl());
    assertNull(builder.getCustomIdResolver());
    assertFalse(builder.isTypeIdVisible());
  }

  @Test
  public void testStdTypeResolverBuilderNoTypeInfoBuilder() {
    StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
    assertNull(builder.getIdType());
    assertNull(builder.getIncludeAs());
    assertNull(builder.getTypeProperty());
    assertNull(builder.getDefaultImpl());
    assertNull(builder.getCustomIdResolver());
    assertFalse(builder.isTypeIdVisible());
  }

  // Test case for method: public TypeSerializer buildTypeSerializer(...)
  @Test
  public void testBuildTypeSerializer() {
    // Prepare test data
    MapperConfig<Object> config = new MapperConfig<>();
    JavaType baseType = config.getTypeFactory().constructType(String.class);
    Collection<NamedType> subtypes = Arrays.asList(new NamedType("subtype1", String.class), new NamedType("subtype2", Integer.class));

    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.init(JsonTypeInfo.Id.PROPERTY, null);

    TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);
    assertNotNull(result);
  }

  // Test case for method: public TypeDeserializer buildTypeDeserializer(...)
  @Test
  public void testBuildTypeDeserializer() {
    // Prepare test data
    DeserializationConfig config = new DeserializationConfig();
    JavaType baseType = config.getTypeFactory().constructType(String.class);
    Collection<NamedType> subtypes = Arrays.asList(new NamedType("subtype1", String.class), new NamedType("subtype2", Integer.class));

    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.init(JsonTypeInfo.Id.PROPERTY, null);

    TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);
    assertNotNull(result);
  }

  // Test case for method: public StdTypeResolverBuilder inclusion(...)
  @Test
  public void testInclusion() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    JsonTypeInfo.As includeAs = JsonTypeInfo.As.PROPERTY;
    builder.inclusion(includeAs);
    assertEquals(includeAs, builder.getIncludeAs());
  }

  // Test case for method: public StdTypeResolverBuilder typeProperty(...)
  @Test
  public void testTypeProperty() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    String typeIdPropName = "typeIdProp";
    builder.typeProperty(typeIdPropName);
    assertEquals(typeIdPropName, builder.getTypeProperty());
  }

  // Test case for method: public StdTypeResolverBuilder defaultImpl(...)
  @Test
  public void testDefaultImpl() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    Class<?> defaultImpl = String.class;
    builder.defaultImpl(defaultImpl);
    assertEquals(defaultImpl, builder.getDefaultImpl());
  }

  // Test case for method: public StdTypeResolverBuilder typeIdVisibility(...)
  @Test
  public void testTypeIdVisibility() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.typeIdVisibility(true);
    assertTrue(builder.isTypeIdVisible());
  }

  // Test cases for edge cases and exception handling

  // Test case for method: public TypeSerializer buildTypeSerializer(...) with null configuration
  @Test(expected = IllegalArgumentException.class)
  public void testBuildTypeSerializerWithNullConfig() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    JavaType baseType = null;
    Collection<NamedType> subtypes = null;
    builder.buildTypeSerializer(null, baseType, subtypes);
  }

  // Test case for method: public TypeSerializer buildTypeSerializer(...) with null baseType
  @Test(expected = IllegalArgumentException.class)
  public void testBuildTypeSerializerWithNullBaseType() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    MapperConfig<?> config = new MapperConfig<>();
    Collection<NamedType> subtypes = null;
    builder.buildTypeSerializer(config, null, subtypes);
  }

  // Test case for method: public TypeSerializer buildTypeSerializer(...) with null subtypes
  @Test(expected = IllegalArgumentException.class)
  public void testBuildTypeSerializerWithNullSubtypes() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    MapperConfig<?> config = new MapperConfig<>();
    JavaType baseType = null;
    builder.buildTypeSerializer(config, baseType, null);
  }

  // Test case for method: public TypeSerializer buildTypeSerializer(...) with non-standard inclusion type
  @Test(expected = IllegalStateException.class)
  public void testBuildTypeSerializerWithNonStandardInclusionType() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    MapperConfig<?> config = new MapperConfig<>();
    JavaType baseType = null;
    Collection<NamedType> subtypes = null;
    builder.buildTypeSerializer(config, baseType, subtypes, null, null);
  }

  // Test case for method: public TypeDeserializer buildTypeDeserializer(...) with null configuration
  @Test(expected = IllegalArgumentException.class)
  public void testBuildTypeDeserializerWithNullConfig() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    JavaType baseType = null;
    Collection<NamedType> subtypes = null;
    builder.buildTypeDeserializer(null, baseType, subtypes);
  }

  // Test case for method: public TypeDeserializer buildTypeDeserializer(...) with null baseType
  @Test(expected = IllegalArgumentException.class)
  public void testBuildTypeDeserializerWithNullBaseType() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    DeserializationConfig config = new DeserializationConfig();
    Collection<NamedType> subtypes = null;
    builder.buildTypeDeserializer(config, null, subtypes);
  }

  // Test case for method: public TypeDeserializer buildTypeDeserializer(...) with null subtypes
  @Test(expected = IllegalArgumentException.class)
  public void testBuildTypeDeserializerWithNullSubtypes() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    DeserializationConfig config = new DeserializationConfig();
    JavaType baseType = null;
    builder.buildTypeDeserializer(config, baseType, null);
  }

  // Test case for method: public TypeDeserializer buildTypeDeserializer(...) with non-standard inclusion type
  @Test(expected = IllegalStateException.class)
  public void testBuildTypeDeserializerWithNonStandardInclusionType() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    DeserializationConfig config = new DeserializationConfig();
    JavaType baseType = null;
    Collection<NamedType> subtypes = null;
    builder.buildTypeDeserializer(config, baseType, subtypes, null, null);
  }

  // Test case for method: protected TypeIdResolver idResolver(...) with null config
  @Test(expected = IllegalArgumentException.class)
  public void testIdResolverWithNullConfig() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    JavaType baseType = null;
    Collection<NamedType> subtypes = null;
    boolean forSer = true;
    boolean forDeser = false;
    builder.idResolver(null, baseType, subtypes, forSer, forDeser);
  }

  // Test case for method: protected TypeIdResolver idResolver(...) with null baseType
  @Test(expected = IllegalArgumentException.class)
  public void testIdResolverWithNullBaseType() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    MapperConfig<?> config = new MapperConfig<>();
    Collection<NamedType> subtypes = null;
    boolean forSer = true;
    boolean forDeser = false;
    builder.idResolver(config, null, subtypes, forSer, forDeser);
  }

  // Test case for method: protected TypeIdResolver idResolver(...) with null subtypes
  @Test(expected = IllegalArgumentException.class)
  public void testIdResolverWithNullSubtypes() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    MapperConfig<?> config = new MapperConfig<>();
    JavaType baseType = null;
    boolean forSer = true;
    boolean forDeser = false;
    builder.idResolver(config, baseType, null, forSer, forDeser);
  }

  // Test case for method: protected TypeIdResolver idResolver(...) with non-standard idType
  @Test(expected = IllegalStateException.class)
  public void testIdResolverWithNonStandardIdType() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    MapperConfig<?> config = new MapperConfig<>();
    JavaType baseType = null;
    Collection<NamedType> subtypes = null;
    boolean forSer = true;
    boolean forDeser = false;
    builder.idResolver(config, baseType, subtypes, forSer, forDeser);
  }
}