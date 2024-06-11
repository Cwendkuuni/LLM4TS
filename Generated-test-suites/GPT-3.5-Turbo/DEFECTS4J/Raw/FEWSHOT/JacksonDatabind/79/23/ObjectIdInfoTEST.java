package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ObjectIdInfoTest {
  @Test
  public void testConstructorWithAllParams() {
    PropertyName propertyName = new PropertyName("name");
    Class<?> scope = Object.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
    Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

    assertEquals(propertyName, objectIdInfo.getPropertyName());
    assertEquals(scope, objectIdInfo.getScope());
    assertEquals(generator, objectIdInfo.getGeneratorType());
    assertEquals(resolver, objectIdInfo.getResolverType());
    assertEquals(false, objectIdInfo.getAlwaysAsId());
  }

  @Test
  public void testConstructorWithThreeParams() {
    PropertyName propertyName = new PropertyName("name");
    Class<?> scope = Object.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

    assertEquals(propertyName, objectIdInfo.getPropertyName());
    assertEquals(scope, objectIdInfo.getScope());
    assertEquals(generator, objectIdInfo.getGeneratorType());
    assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    assertEquals(false, objectIdInfo.getAlwaysAsId());
  }

  @Test
  public void testConstructorWithDeprecatedParams() {
    String name = "name";
    Class<?> scope = Object.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

    ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);

    assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
    assertEquals(scope, objectIdInfo.getScope());
    assertEquals(generator, objectIdInfo.getGeneratorType());
    assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    assertEquals(false, objectIdInfo.getAlwaysAsId());
  }

  @Test
  public void testWithAlwaysAsId() {
    PropertyName propertyName = new PropertyName("name");
    Class<?> scope = Object.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
    Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

    ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

    assertEquals(propertyName, updatedObjectIdInfo.getPropertyName());
    assertEquals(scope, updatedObjectIdInfo.getScope());
    assertEquals(generator, updatedObjectIdInfo.getGeneratorType());
    assertEquals(resolver, updatedObjectIdInfo.getResolverType());
    assertEquals(true, updatedObjectIdInfo.getAlwaysAsId());
  }
}