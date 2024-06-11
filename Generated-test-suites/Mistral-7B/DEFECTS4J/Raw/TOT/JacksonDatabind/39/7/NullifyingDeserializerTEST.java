package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

 @Test
 public void testDeserializeNullifyingObject() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(jsonFactory);
 String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
 StdDeserializerDeskedContext context = new StdDeserializerDeskedContext(NullifyingDeserializer.class, Object.class);
 JsonParser jsonParser = jsonFactory.createParser(jsonString);
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, context);
 // Assert null result
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingArray() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(jsonFactory);
 String jsonString = "[1, 2, 3, 4, 5]";
 StdDeserializerDeskedContext context = new StdDeserializerDeskedContext(NullifyingDeserializer.class, Object[].class);
 JsonParser jsonParser = jsonFactory.createParser(jsonString);
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(jsonParser, context);
 // Assert empty array result
 assertArrayEquals(new Object[0], result);
 }

 @Test
 public void testDeserializeNullifyingObjectWithType() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(jsonFactory);
 String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
 StdDeserializerDeskedContext context = new StdDeserializerDeskedContext(NullifyingDeserializer.class, String.class);
 JsonParser jsonParser = jsonFactory.createParser(jsonString);
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(), mapper.getTypeFactory().constructType(String.class));
 // Assert null result
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullifyingObjectWithInvalidToken() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(jsonFactory);
 String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
 StdDeserializerDeskedContext context = new StdDeserializerDeskedContext(NullifyingDeserializer.class, Object.class);
 JsonParser jsonParser = jsonFactory.createParser(jsonString);
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 deserializer.deserializeWithType(jsonParser, new DeserializationContext(), null);
 // Assert IOException is thrown
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeNullifyingObjectWithProcessingException() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(jsonFactory);
 String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
 StdDeserializerDeskedContext context = new StdDeserializerDeskedContext(NullifyingDeserializer.class, Object.class);
 JsonParser jsonParser = jsonFactory.createParser(jsonString);
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 ObjectReader reader = mapper.readerFor(Object.class);
 reader.setDeserializer(deserializer);
 Object result = reader.readValue(jsonParser);
 // Assert JsonProcessingException is thrown
 }
}