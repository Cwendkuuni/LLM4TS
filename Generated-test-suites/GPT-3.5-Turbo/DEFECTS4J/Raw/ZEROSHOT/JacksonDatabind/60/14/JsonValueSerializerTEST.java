package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.JsonMappingExceptionWrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("serial")
@JacksonStdImpl
public class JsonValueSerializerTest {
  @Test
  public void testSerialize() throws JsonMappingException, IOException, Exception {
    AnnotatedMethod valueMethod = new AnnotatedMethod(JsonValueSerializer.class.getDeclaredMethod("serialize", Object.class, JsonGenerator.class, SerializerProvider.class));
    JsonSerializer<?> ser = null;
    JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, ser);
    Object bean = new Object();
    JsonGenerator gen = null;
    SerializerProvider prov = null;
    serializer.serialize(bean, gen, prov);
  }

  @Test
  public void testSerializeWithType() throws JsonMappingException, IOException, Exception {
    AnnotatedMethod valueMethod = new AnnotatedMethod(JsonValueSerializer.class.getDeclaredMethod("serializeWithType", Object.class, JsonGenerator.class, SerializerProvider.class, TypeSerializer.class));
    JsonSerializer<?> ser = null;
    JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, ser);
    Object bean = new Object();
    JsonGenerator gen = null;
    SerializerProvider provider = null;
    TypeSerializer typeSer0 = null;
    serializer.serializeWithType(bean, gen, provider, typeSer0);
  }

  @Test
  public void testGetSchema() throws JsonMappingException, Exception {
    AnnotatedMethod valueMethod = new AnnotatedMethod(JsonValueSerializer.class.getDeclaredMethod("getSchema", SerializerProvider.class, Type.class));
    JsonSerializer<?> ser = null;
    JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, ser);
    SerializerProvider provider = null;
    Type typeHint = null;
    serializer.getSchema(provider, typeHint);
  }

  @Test
  public void testAcceptJsonFormatVisitor() throws JsonMappingException, Exception {
    AnnotatedMethod valueMethod = new AnnotatedMethod(JsonValueSerializer.class.getDeclaredMethod("acceptJsonFormatVisitor", JsonFormatVisitorWrapper.class, JavaType.class));
    JsonSerializer<?> ser = null;
    JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, ser);
    JsonFormatVisitorWrapper visitor = null;
    JavaType typeHint = null;
    serializer.acceptJsonFormatVisitor(visitor, typeHint);
  }

  @Test
  public void test_acceptJsonFormatVisitorForEnum() throws JsonMappingException, Exception {
    AnnotatedMethod valueMethod = new AnnotatedMethod(JsonValueSerializer.class.getDeclaredMethod("_acceptJsonFormatVisitorForEnum", JsonFormatVisitorWrapper.class, JavaType.class, Class.class));
    JsonSerializer<?> ser = null;
    JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, ser);
    JsonFormatVisitorWrapper visitor = null;
    JavaType typeHint = null;
    Class<?> enumType = null;
    serializer._acceptJsonFormatVisitorForEnum(visitor, typeHint, enumType);
  }

  @Test
  public void testIsNaturalTypeWithStdHandling() throws JsonMappingException, Exception {
    AnnotatedMethod valueMethod = new AnnotatedMethod(JsonValueSerializer.class.getDeclaredMethod("isNaturalTypeWithStdHandling", Class.class, JsonSerializer.class));
    JsonSerializer<?> ser = null;
    JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, ser);
    Class<?> rawType = null;
    serializer.isNaturalTypeWithStdHandling(rawType, ser);
  }

}