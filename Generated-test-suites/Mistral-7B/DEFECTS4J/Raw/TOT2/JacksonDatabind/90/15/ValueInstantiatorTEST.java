package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.type.ClassIdResolver;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ValueInstantiatorTest {

 private static class TestValueInstantiator extends ValueInstantiator {
 private static final long serialVersionUID = 1L;

 @Override
 public boolean canCreateUsingDefault() { return true; }

 @Override
 public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
 return "default value";
 }

 @Override
 public boolean canCreateFromString() { return true; }

 @Override
 public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
 return "string value: " + value;
 }

 @Override
 public boolean canCreateFromInt() { return true; }

 @Override
 public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
 return "int value: " + value;
 }

 @Override
 public boolean canCreateFromBoolean() { return true; }

 @Override
 public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
 return "boolean value: " + value;
 }
 }

 @Test
 public void testValueInstantiatorBasic() throws IOException {
 DeserializationContext ctx = new TestDeserializationContext();
 ValueInstantiator vi = new TestValueInstantiator();

 assertEquals("default value", vi.createUsingDefault(ctx));

 String jsonString = "\"test\"";
 Object o = vi.createFromString(ctx, jsonString);
 assertEquals("string value: test", o);

 int intValue = 42;
 Object o2 = vi.createFromInt(ctx, intValue);
 assertEquals("int value: 42", o2);

 assertTrue(vi.canInstantiate());
 }

 @Test(expected = IOException.class)
 public void testValueInstantiatorMissing() throws IOException {
 DeserializationContext ctx = new TestDeserializationContext();
 ValueInstantiator vi = new TestValueInstantiator();

 try {
 vi.createUsingDefault(ctx);
 fail();
 } catch (IOException e) {
 // expected
 }

 try {
 vi.createFromString(ctx, "test");
 fail();
 } catch (IOException e) {
 // expected
 }

 try {
 vi.createFromInt(ctx, 42);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testValueInstantiatorEdgeCases() throws IOException {
 DeserializationContext ctx = new TestDeserializationContext();
 ValueInstantiator vi = new TestValueInstantiator();

 ClassIdResolver idResolver = new StandardClassIdResolver();
 AnnotationIntrospector ai = new StandardAnnotationIntrospector();
 JavaType type = idResolver.resolveType(ctx.getConfig(), TestValueInstantiator.class, false);

 Object o = vi.createFromObjectWith(ctx, new Object[] { ai.findValueHandler(ctx.getConfig(), ctx.getParser(), new AnnotatedClass(type), null), "test" });
 assertEquals("test", o);

 Object o2 = vi.createFromObjectWith(ctx, new Object[] { ai.findValueHandler(ctx.getConfig(), ctx.getParser(), new AnnotatedClass(type), null), 42, (byte) 0x01 });
 assertEquals("int value: 42", o2);

 Object o3 = vi.createFromObjectWith(ctx, new Object[] { ai.findValueHandler(ctx.getConfig(), ctx.getParser(), new AnnotatedClass(type), null), true, 42.0 });
 assertEquals("boolean value: true int value: 42", o3);
 }

 @Test
 public void testValueInstantiatorSupportsNull() throws IOException {
 DeserializationContext ctx = new TestDeserializationContext();
 ValueInstantiator vi = new TestValueInstantiator();

 assertEquals(null, vi.createUsingDefault(ctx));

 String jsonString = "null";
 Object o = vi.createFromString(ctx, jsonString);
 assertEquals(null, o);

 int intValue = 0;
 Object o2 = vi.createFromInt(ctx, intValue);
 assertEquals(null, o2);
 }

 @Test
 public void testValueInstantiatorCustomDeserializer() throws IOException {
 DeserializationContext ctx = new TestDeserializationContext();
 ValueInstantiator vi = new TestValueInstantiator();

 JavaType valueType = ctx.getTypeFactory().constructType(String.class);
 JsonDeserializer<String> deserializer = new JsonDeserializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "custom deserialized value";
 }
 };
 ctx.handleUnknownProperty("test", valueType, null, deserializer);

 Object o = vi.createFromObjectWith(ctx, new Object[] { ctx.getBeanDescription().findProperty("test"), "test" });
 assertEquals("custom deserialized value", o);
 }

 @Test
 public void testValueInstantiatorCustomTypeSerializer() throws IOException {
 DeserializationContext ctx = new TestDeserializationContext();
 ValueInstantiator vi = new TestValueInstantiator();

 TypeSerializer serializer = new TypeSerializer() {
 @Override
 public void serializeValue(SerializerProvider p, Object v, TypeSerializer s) throws IOException {
 if (v == null) {
 p.getOutputFactory().writeString("null");
 } else {
 p.getOutputFactory().writeString("custom serialized value");
 }
 }

 @Override
 public Object deserializeValue(DeserializationContext ctxt, TypeDeserializer deserializer, Object v) throws IOException {
 if (v == null) {
 return null;
 } else {
 return "custom deserialized value";
 }
 }
 };
 ctx.handleMissingValueSerializer(String.class, serializer);

 Object o = vi.createFromString(ctx, "\"null\"");
 assertEquals(null, o);

 Object o2 = vi.createFromString(ctx, "\"custom serialized value\"");
 assertEquals("custom deserialized value", o2);
 }

 @Test
 public void testValueInstantiatorMissingArguments() throws IOException {
 DeserializationContext ctx = new TestDeserializationContext();
 ValueInstantiator vi = new TestValueInstantiator();

 try {
 vi.createFromObjectWith(ctx, new Object[] {});
 fail();
 } catch (IOException e) {
 // expected
 }

 try {
 vi.createFromObjectWith(ctx, null);
 fail();
 } catch (IOException e) {
 // expected
 }

 try {
 vi.createFromObjectWith(ctx, new Object[] { new AnnotatedMethod(TestValueInstantiator.class, "createFromObjectWith", new Class[] { DeserializationContext.class, Object[].class }) });
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testValueInstantiatorCustomCreator() throws IOException {
 DeserializationContext ctx = new TestDeserializationContext();
 ValueInstantiator vi = new TestValueInstantiator();

 Class<?> customCreatorClass = ValueInstantiatorCustomCreator.class;
 AnnotatedClass ac = new AnnotatedClass(customCreatorClass);
 AnnotatedConstructor acConstructor = ac.getConstructors()[0];
 JsonDeserializer<Object> customCreator = new JsonDeserializer<Object>(Object.class) {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 AnnotatedMethod creatorMethod = acConstructor.findMethod("createFromBase");
 Object[] args = new Object[] {p, ctxt};
 return creatorMethod.invoke(new ValueInstantiatorCustomCreator(), args);
 }
 };
 ctx.handleBeanDeserializer(customCreator);

 Object o = vi.createFromObjectWith(ctx, new Object[] { ac, new Object[]{"test", 42, true} });
 assertEquals("custom value: test int value: 42 boolean value: true", o);
 }

 @Test
 public void testValueInstantiatorWriteNull() throws IOException {
 DeserializationContext ctx = new TestDeserializationContext();
 ValueInstantiator vi = new TestValueInstantiator();

 ObjectWriter ow = ctx.getObjectWriter();
 String json = ow.writeValueAsString(null);
 assertEquals("null", json);
 }
}

 class TestDeserializationContext extends DeserializationContext {
 @Override
 protected ObjectTree _buildValue(DeserializerProvider provider, AnnotatedClass rootType, JavaType rootTypeId, MapperConfig config, Object deserializer)
 throws IOException {
 return new RootValueFactory(provider, config).getRootValue(rootTypeId);
 }
 }

 class ValueInstantiatorCustomCreator {
 private static final long serialVersionUID = 1L;

 public Object createFromBase(DeserializationContext ctxt, Object[] args) throws IOException {
 String propertyName = (String) args[0];
 Object[] propertyValues = (Object[]) args[1];

 Object value = null;
 if (propertyName.equals("test")) {
 value = propertyValues[0];
 } else if (propertyName.equals("intValue")) {
 value = propertyValues[1];
 } else if (propertyName.equals("booleanValue")) {
 value = propertyValues[2];
 }

 return value;
 }
 }