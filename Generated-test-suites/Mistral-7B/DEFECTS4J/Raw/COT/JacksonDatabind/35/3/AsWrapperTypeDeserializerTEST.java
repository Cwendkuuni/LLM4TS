package com.example.aswrappertypedeserializer;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

 private static final String TYPE_ID = "com.example.MyType";
 private static final JavaType TYPE = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(MyType.class);
 private static final TypeIdResolver idResolver = ObjectMapper.getDefaultInstance().getTypeIdResolver();
 private static final AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(TYPE, idResolver, "_type", false, MyTypeImpl.class);

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 final Object result = deserializer.deserializeTypedFromObject(createTestParser(), createContext());
 assertEquals(MyType.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 final Object result = deserializer.deserializeTypedFromArray(createTestParser(), createContext());
 assertEquals(MyType.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 final Object result = deserializer.deserializeTypedFromScalar(createTestParser(), createContext());
 assertEquals(MyType.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 final Object result = deserializer.deserializeTypedFromAny(createTestParser(), createContext());
 assertEquals(MyType.class, result.getClass());
 }

 @Test(expected = IOException.class)
 public void testInvalidJsonFormat() throws IOException {
 final JsonParser parser = createTestParser();
 parser.nextToken(); // consume START_OBJECT
 parser.nextToken(); // consume FIELD_NAME
 parser.nextToken(); // consume invalid token
 final DeserializationContext context = createContext();
 deserializer.deserializeTypedFromObject(parser, context);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testMissingTypeId() throws IOException {
 final JsonParser parser = createTestParser();
 parser.nextToken(); // consume START_OBJECT
 parser.nextToken(); // consume FIELD_NAME
 final DeserializationContext context = createContext();
 deserializer.deserializeTypedFromObject(parser, context);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testInvalidTypeId() throws IOException {
 final JsonParser parser = createTestParser();
 parser.nextToken(); // consume START_OBJECT
 parser.nextToken(); // consume FIELD_NAME
 parser.nextToken(); // consume STRING
 parser.setValue("invalid.type.id");
 deserializer.deserializeTypedFromObject(parser, createContext());
 fail("Expected IOException");
 }

 @Test(expected = JsonMappingException.class)
 public void testUnknownTypeId() throws IOException {
 final JsonParser parser = createTestParser();
 parser.nextToken(); // consume START_OBJECT
 parser.nextToken(); // consume FIELD_NAME
 parser.nextToken(); // consume STRING
 parser.setValue(TYPE_ID);
 final DeserializationContext context = createContext();
 deserializer.deserializeTypedFromObject(parser, context);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testMergeTypeId() throws IOException {
 final JsonParser parser = createTestParser();
 parser.nextToken(); // consume START_OBJECT
 parser.nextToken(); // consume FIELD_NAME
 parser.nextToken(); // consume STRING
 parser.setValue(TYPE_ID);
 parser.nextToken(); // consume START_OBJECT
 parser.writeFieldName("_type");
 parser.writeString(TYPE_ID);
 parser.nextToken(); // consume END_OBJECT
 parser.nextToken(); // consume END_OBJECT
 final DeserializationContext context = createContext();
 final Object result = deserializer.deserializeTypedFromObject(parser, context);
 assertEquals(MyType.class, result.getClass());
 }

 private JsonParser createTestParser() {
 final ObjectMapper mapper = ObjectMapper.getDefaultInstance();
 final TokenBuffer buffer = new TokenBuffer(null, false);
 buffer.writeStartObject();
 buffer.writeFieldName("type");
 buffer.writeString(TYPE_ID);
 buffer.writeEndObject();
 return mapper.getFactory().createParser(buffer.asString());
 }

 private DeserializationContext createContext() {
 return new TestBaseContext();
 }

}

class MyType {

}

class MyTypeImpl {

}

class TestBaseContext extends DeserializationContext {

 @Override
 public BeanDescription getCurrentBeanDescription() {
 return null;
 }

 @Override
 public String getBaseName() {
 return "";
 }

 @Override
 public Object _valueInstantiate() {
 return null;
 }

 @Override
 public ObjectMapper _valueHandler() {
 return null;
 }

 @Override
 public DeserializationConfig getConfig() {
 return null;
 }

 @Override
 public BeanDescription _findBeanDescription(Class<?> beanType, String propertyName) {
 return null;
 }

 @Override
 public Object _valueInstantiate(BeanDescription beanDesc, java.lang.reflect.AnnotatedElement property) {
 return null;
 }

 @Override
 public void _reportError(MessageBase msg) throws IOException {
 fail("_reportError should not be called during testing");
 }

 @Override
 public void _reportError(MessageBase msg, Throwable cause) throws IOException {
 fail("_reportError should not be called during testing");
 }

}