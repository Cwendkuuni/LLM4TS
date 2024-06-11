package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NumberSerializersTest {

 @BeforeAll
 static void setup() {
 // Initialize any static variables or setup any class level resources
 }

 @AfterAll
 static void tearDown() {
 // Perform any necessary cleanup or shutdown
 }

 @Test
 void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
 NumberSerializers.addAll(allDeserializers);

 assertEquals(10, allDeserializers.size());
 assertEquals(Integer.class.getName(), allDeserializers.keySet().toArray()[0].toString());
 assertEquals(Long.class.getName(), allDeserializers.keySet().toArray()[3].toString());
 }

 @Test
 void testIntegerSerializer() throws JsonProcessingException {
 Integer value = 123;
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(value);
 assertEquals("123", json);
 }

 @Test
 void testLongSerializer() throws JsonProcessingException {
 Long value = 456L;
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(value);
 assertEquals("456", json);
 }

 @Test
 void testShortSerializer() throws JsonProcessingException {
 Short value = (short) 789;
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(value);
 assertEquals("789", json);
 }

 @Test
 void testFloatSerializer() throws JsonProcessingException {
 Float value = 1.123F;
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(value);
 assertEquals("1.123", json);
 }

 @Test
 void testDoubleSerializer() throws JsonProcessingException {
 Double value = 2.345;
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(value);
 assertEquals("2.345", json);
 }

 @Test
 void testIntLikeSerializer() throws JsonProcessingException {
 Number value = 567;
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(value);
 assertEquals("567", json);
 }
}