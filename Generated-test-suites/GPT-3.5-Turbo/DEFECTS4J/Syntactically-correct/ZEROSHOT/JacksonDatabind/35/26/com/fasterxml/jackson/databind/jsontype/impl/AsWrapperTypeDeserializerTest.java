package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {
  
  private AsWrapperTypeDeserializer deserializer;
  
  @Before
  public void setup() {
    JavaType bt = new JavaType();
    TypeIdResolver idRes = new TypeIdResolver();
    String typePropertyName = "type";
    boolean typeIdVisible = true;
    Class<?> defaultImpl = Object.class;
    deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
  }
  
  @Test
  public void testForProperty() {
    BeanProperty prop = new BeanProperty();
    TypeDeserializer result = deserializer.forProperty(prop);
    assertNotEquals(deserializer, result);
    assertEquals(AsWrapperTypeDeserializer.class, result.getClass());
  }
  
  @Test
  public void testGetTypeInclusion() {
    As result = deserializer.getTypeInclusion();
    assertEquals(As.WRAPPER_OBJECT, result);
  }
  
  @Test
  public void testDeserializeTypedFromObject() {
    JsonParser jp = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
    assertNotNull(result);
  }
  
  @Test
  public void testDeserializeTypedFromArray() {
    JsonParser jp = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object result = deserializer.deserializeTypedFromArray(jp, ctxt);
    assertNotNull(result);
  }
  
  @Test
  public void testDeserializeTypedFromScalar() {
    JsonParser jp = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);
    assertNotNull(result);
  }
  
  @Test
  public void testDeserializeTypedFromAny() {
    JsonParser jp = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object result = deserializer.deserializeTypedFromAny(jp, ctxt);
    assertNotNull(result);
  }
  
  @Test
  public void test_deserialize() {
    JsonParser p = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    TokenBuffer tb = new TokenBuffer(null, false);
    tb.writeStartObject(); // recreate START_OBJECT
    tb.writeFieldName(deserializer.getTypePropertyName());
    tb.writeString("typeId");
    p = JsonParserSequence.createFlattened(tb.asParser(p), p);
    Object result = deserializer._deserialize(p, ctxt);
    assertNotNull(result);
  }
}