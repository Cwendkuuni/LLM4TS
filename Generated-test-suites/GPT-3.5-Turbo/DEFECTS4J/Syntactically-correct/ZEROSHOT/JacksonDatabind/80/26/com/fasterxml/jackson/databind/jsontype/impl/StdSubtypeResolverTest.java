package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {
  
  private StdSubtypeResolver subtypeResolver;

  @Before
  public void setup() {
    subtypeResolver = new StdSubtypeResolver();
  }

  @Test
  public void testRegisterSubtypes() {
    NamedType namedType1 = new NamedType(Object.class, "Object");
    NamedType namedType2 = new NamedType(String.class, "String");

    subtypeResolver.registerSubtypes(namedType1, namedType2);

    LinkedHashSet<NamedType> registeredSubtypes = subtypeResolver._registeredSubtypes;

    Assert.assertEquals(2, registeredSubtypes.size());
    Assert.assertTrue(registeredSubtypes.contains(namedType1));
    Assert.assertTrue(registeredSubtypes.contains(namedType2));
  }

  @Test
  public void testRegisterSubtypesUsingClassArray() {
    Class<?>[] classes = {Object.class, String.class};

    subtypeResolver.registerSubtypes(classes);

    LinkedHashSet<NamedType> registeredSubtypes = subtypeResolver._registeredSubtypes;

    Assert.assertEquals(2, registeredSubtypes.size());
  }

  @Test
  public void testCollectAndResolveSubtypesByClassWithBaseTypeNull() {
    MapperConfig<?> config = null;
    AnnotatedMember property = null;
    JavaType baseType = null;

    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testCollectAndResolveSubtypesByClassWithBaseTypeNotNull() {
    MapperConfig<?> config = null;
    AnnotatedMember property = null;
    JavaType baseType = new JavaType(Object.class);

    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testCollectAndResolveSubtypesByClassWithType() {
    MapperConfig<?> config = null;
    AnnotatedMember property = null;
    JavaType baseType = new JavaType(String.class);

    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testCollectAndResolveSubtypesByClassWithConfigPropertyBaseType() {
    MapperConfig<?> config = null;
    AnnotatedMember property = new AnnotatedMember();
    JavaType baseType = null;

    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testCollectAndResolveSubtypesByClassWithConfigPropertyBaseTypeInvalidRawType() {
    MapperConfig<?> config = null;
    AnnotatedMember property = new AnnotatedMember();
    JavaType baseType = new JavaType(int.class);

    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testCollectAndResolveSubtypesByClassWithConfigPropertyBaseTypeValidRawType() {
    MapperConfig<?> config = null;
    AnnotatedMember property = new AnnotatedMember();
    JavaType baseType = new JavaType(String.class);

    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testCollectAndResolveSubtypesByClass() {
    MapperConfig<?> config = null;
    AnnotatedClass type = new AnnotatedClass(Object.class);

    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, type);

    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testCollectAndResolveSubtypesByTypeId() {
    MapperConfig<?> config = null;
    AnnotatedMember property = null;
    JavaType baseType = null;

    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testCollectAndResolveSubtypesByTypeIdWithType() {
    MapperConfig<?> config = null;
    AnnotatedMember property = null;
    JavaType baseType = new JavaType(String.class);

    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testCollectAndResolveSubtypesByTypeIdWithConfigPropertyBaseType() {
    MapperConfig<?> config = null;
    AnnotatedMember property = new AnnotatedMember();
    JavaType baseType = null;

    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testCollectAndResolveSubtypesByTypeIdWithConfigPropertyBaseTypeInvalidRawType() {
    MapperConfig<?> config = null;
    AnnotatedMember property = new AnnotatedMember();
    JavaType baseType = new JavaType(int.class);

    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testCollectAndResolveSubtypesByTypeIdWithConfigPropertyBaseTypeValidRawType() {
    MapperConfig<?> config = null;
    AnnotatedMember property = new AnnotatedMember();
    JavaType baseType = new JavaType(String.class);

    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }

  @Test
  public void testCollectAndResolveSubtypesByTypeIdWithAnnotatedClassBaseType() {
    MapperConfig<?> config = null;
    AnnotatedClass baseType = new AnnotatedClass(Object.class);

    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseType);

    Assert.assertNotNull(result);
    Assert.assertTrue(result.isEmpty());
  }
}