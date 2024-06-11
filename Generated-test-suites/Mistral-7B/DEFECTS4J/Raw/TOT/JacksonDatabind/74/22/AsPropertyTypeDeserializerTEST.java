package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson. core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @BeforeAll
 static void setup() {
 mapper.registerModule(new SimpleModule().addDeserializer(String.class, new StringDeserializer()));
 mapper.registerModule(new SimpleModule().addDeserializer(AsPropertyTypeDeserializer.class, new AsPropertyTypeDeserializerTestDeserializer()));
 }

 @Test
 void testDeserializeTypedFromObjectWithProperty() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("typePropertyName", "StringType");
 input.put("data", "test data");

 Object result = mapper.readerForType(AsPropertyTypeDeserializer.class).readValue(input);

 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) result;
 assertEquals(String.class, deserializer.getType());
 assertEquals("test data", deserializer.getProperty().get(0));

 As inclusion = deserializer.getTypeInclusion();
 assertEquals(As.PROPERTY, inclusion);
 }

 @Test
 void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("data", "[1, 2, 3]");

 Object result = mapper.readerForType(AsPropertyTypeDeserializer.class).readValue(input);

 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) result;
 assertEquals(List.class, deserializer.getType());
 assertEquals(Integer[].class, deserializer.getDefaultImplType());
 }

 @Test
 void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("typePropertyName", "CustomTypeId");
 input.put("data", "{\"typeId\":\"CustomType\"}");

 Object result = mapper.readerForType(AsPropertyTypeDeserializer.class).readValue(input);

 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) result;
 assertEquals("CustomType", deserializer.getTypeId());
 }

 @Test
 void testDeserializeTypedFromObjectWithMissingProperty() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("data", "test data");

 assertThrows(IOException.class, () -> mapper.readerForType(AsPropertyTypeDeserializer.class).readValue(input));
 }

 @Test
 void testDeserializeTypedFromObjectWithInvalidToken() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("typePropertyName", "StringType");
 input.put("data", "["); // missing ']'

 assertThrows(IOException.class, () -> mapper.readerForType(AsPropertyTypeDeserializer.class).readValue(input));
 }

 @Test
 void testDeserializeTypedFromArray() throws Exception {
 JsonNode input = mapper.readTree("[[1, 2, 3], [4, 5, 6]]");
 Object result = mapper.readerForType(AsPropertyTypeDeserializer.class).readValue(input);

 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) result;
 assertEquals(List.class, deserializer.getType());
 assertEquals(Integer[].class, deserializer.getDefaultImplType());
 }

 @Test
 void testDeserializeTypedFromScalar() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("data", "test data");

 Object result = mapper.readerForType(AsPropertyTypeDeserializer.class).readValue(input);

 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) result;
 assertEquals(String.class, deserializer.getType());
 assertEquals("test data", deserializer.getProperty().get(0));
 }

 @AfterAll
 static void tearDown() {
 mapper.close();
 }
}

class StringDeserializer extends StdDeserializer<String> {
 StringDeserializer() { this(null); }
 StringDeserializer(Class<String> t) { super(t); }

 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsString();
 }
}

class AsPropertyTypeDeserializerTestDeserializer extends AsPropertyTypeDeserializer {
 AsPropertyTypeDeserializerTestDeserializer() { super(null, null, null, false, null, As.PROPERTY); }
}