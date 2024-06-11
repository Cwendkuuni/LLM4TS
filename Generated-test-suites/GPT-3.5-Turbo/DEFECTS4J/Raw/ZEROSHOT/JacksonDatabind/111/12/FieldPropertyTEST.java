package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class FieldPropertyTest {

  private FieldProperty fieldProperty;
  private BeanPropertyDefinition propDef;
  private JavaType type;
  private TypeDeserializer typeDeser;
  private Annotations contextAnnotations;
  private AnnotatedField field;

  @Before
  public void setUp() {
    propDef = Mockito.mock(BeanPropertyDefinition.class);
    type = Mockito.mock(JavaType.class);
    typeDeser = Mockito.mock(TypeDeserializer.class);
    contextAnnotations = Mockito.mock(Annotations.class);
    field = Mockito.mock(AnnotatedField.class);

    fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
  }

  @Test
  public void testConstructor() {
    Assert.assertEquals(propDef, fieldProperty._propDef);
    Assert.assertEquals(type, fieldProperty._type);
    Assert.assertEquals(typeDeser, fieldProperty._valueTypeDeserializer);
    Assert.assertEquals(contextAnnotations, fieldProperty._contextAnnotations);
    Assert.assertEquals(field, fieldProperty._annotated);
    Assert.assertEquals(field.getAnnotated(), fieldProperty._field);
  }

  @Test
  public void testDeserializeAndSet() {
    JsonParser p = Mockito.mock(JsonParser.class);
    DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
    Object instance = new Object();

    try {
        fieldProperty.deserializeAndSet(p, ctxt, instance);
    } catch (Exception e) {
        Assert.fail("An exception should not have been thrown");
    }

    Mockito.verify(p).hasToken(Mockito.any(JsonToken.class));
    Mockito.verifyZeroInteractions(ctxt);
    Mockito.verifyZeroInteractions(_nullProvider);
  }

  @Test
  public void testDeserializeSetAndReturn() {
    JsonParser p = Mockito.mock(JsonParser.class);
    DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
    Object instance = new Object();

    try {
        fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
    } catch (Exception e) {
        Assert.fail("An exception should not have been thrown");
    }

    Mockito.verify(p).hasToken(Mockito.any(JsonToken.class));
    Mockito.verifyZeroInteractions(ctxt);
    Mockito.verifyZeroInteractions(_nullProvider);
  }

  @Test
  public void testSet() {
    Object instance = new Object();
    Object value = new Object();

    try {
        fieldProperty.set(instance, value);
    } catch (Exception e) {
        Assert.fail("An exception should not have been thrown");
    }

    // Verify that the field value has been set correctly
    try {
        Field field = FieldProperty.class.getDeclaredField("_field");
        field.setAccessible(true);
        Assert.assertEquals(value, field.get(instance));
    } catch (Exception e) {
        Assert.fail("An exception should not have been thrown");
    }
  }

  @Test
  public void testSetAndReturn() {
    Object instance = new Object();
    Object value = new Object();

    try {
        fieldProperty.setAndReturn(instance, value);
    } catch (Exception e) {
        Assert.fail("An exception should not have been thrown");
    }

    // Verify that the field value has been set correctly
    try {
        Field field = FieldProperty.class.getDeclaredField("_field");
        field.setAccessible(true);
        Assert.assertEquals(value, field.get(instance));
    } catch (Exception e) {
        Assert.fail("An exception should not have been thrown");
    }
  }

  @Test
  public void testFixAccess() {
    DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
    fieldProperty.fixAccess(config);

    // Verify that the method `checkAndFixAccess` is called with the correct arguments
    Mockito.verify(ClassUtil)
            .checkAndFixAccess(fieldProperty._field, config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
  }

  @Test
  public void testGetAnnotation() {
    Class<A> acls = A.class;
    Assert.assertNull(fieldProperty.getAnnotation(acls));

    Annotation annotation = new Annotation();
    Mockito.when(field.getAnnotation(acls)).thenReturn(annotation);
    Assert.assertEquals(annotation, fieldProperty.getAnnotation(acls));
  }

  @Test
  public void testGetMember() {
    Assert.assertEquals(field, fieldProperty.getMember());
  }

  @Test
  public void testWithNullProvider() {
    NullValueProvider nva = Mockito.mock(NullValueProvider.class);
    Assert.assertEquals(fieldProperty, fieldProperty.withNullProvider(nva));
  }

  @Test
  public void testWithNullProviderSameObject() {
    Assert.assertEquals(fieldProperty, fieldProperty.withNullProvider(_nullProvider));
  }

  @Test
  public void testWithNullProviderDifferentObject() {
    NullValueProvider nva = Mockito.mock(NullValueProvider.class);
    SettableBeanProperty settableBeanProperty = new SettableBeanProperty(null, null, null, null) {
      @Override
      public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {}

      @Override
      public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
        return null;
      }

      @Override
      public void set(Object instance, Object value) throws IOException {}

      @Override
      public Object setAndReturn(Object instance, Object value) throws IOException {
        return null;
      }
    };
    SettableBeanProperty newProperty = (SettableBeanProperty)settableBeanProperty.withNullProvider(nva);
    Assert.assertNotEquals(settableBeanProperty, newProperty);
    Assert.assertEquals(nva, newProperty._nullProvider);
  }

  @Test
  public void testWithPropertyValueDeserializerSameObject() {
    JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
    Assert.assertEquals(fieldProperty, fieldProperty.withValueDeserializer(deser));
  }

  @Test
  public void testWithPropertyValueDeserializerDifferentObject() {
    JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
    SettableBeanProperty settableBeanProperty = new SettableBeanProperty(null, null, null, null) {
      @Override
      public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {}

      @Override
      public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
        return null;
      }

      @Override
      public void set(Object instance, Object value) throws IOException {}

      @Override
      public Object setAndReturn(Object instance, Object value) throws IOException {
        return null;
      }
    };
    SettableBeanProperty newProperty = (SettableBeanProperty)settableBeanProperty.withValueDeserializer(deser);
    Assert.assertNotEquals(settableBeanProperty, newProperty);
    Assert.assertEquals(deser, newProperty._valueDeserializer);
  }

  @Test
  public void testWithPropertyName() {
    PropertyName newName = Mockito.mock(PropertyName.class);
    Assert.assertEquals(fieldProperty, fieldProperty.withName(newName));
  }

  @Test
  public void testWithPropertyValueDeserializer() {
    SettableBeanProperty settableBeanProperty = new SettableBeanProperty(null, null, null, null) {
      @Override
      public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {}

      @Override
      public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
        return null;
      }

      @Override
      public void set(Object instance, Object value) throws IOException {}

      @Override
      public Object setAndReturn(Object instance, Object value) throws IOException {
        return null;
      }
    };
    JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
    SettableBeanProperty newProperty = (SettableBeanProperty)settableBeanProperty.withValueDeserializer(deser);
    Assert.assertNotEquals(settableBeanProperty, newProperty);
    Assert.assertEquals(deser, newProperty._valueDeserializer);
  }

  private class Annotation {}

  private class A {}

}