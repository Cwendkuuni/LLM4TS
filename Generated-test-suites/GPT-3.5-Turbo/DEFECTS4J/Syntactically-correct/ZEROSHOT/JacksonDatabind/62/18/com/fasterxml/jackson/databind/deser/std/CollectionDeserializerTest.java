package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionDeserializerTest {

   @Test
   public void testDeserialize() throws IOException {
       CollectionDeserializer deserializer = new CollectionDeserializer(Collection.class, null, null, null);
       ObjectMapper mapper = new ObjectMapper();
       JsonFactory factory = new JsonFactory();
       JsonNode node = mapper.readTree(factory.createParser("[1, 2, 3]"));
       Collection<Object> result = deserializer.deserialize(node.traverse(mapper), mapper);
       List<Object> expected = new ArrayList<>();
       expected.add(1);
       expected.add(2);
       expected.add(3);
       assertEquals(result, expected);
   }

   @Test
   public void testDeserializeWithType() throws IOException {
       CollectionDeserializer deserializer = new CollectionDeserializer(Collection.class, null, null, null);
       ObjectMapper mapper = new ObjectMapper();
       JsonFactory factory = new JsonFactory();
       JsonNode node = mapper.readTree(factory.createParser("[1, 2, 3]"));
       Collection<Object> result = (Collection<Object>) deserializer.deserializeWithType(node.traverse(mapper), mapper, null);
       List<Object> expected = new ArrayList<>();
       expected.add(1);
       expected.add(2);
       expected.add(3);
       assertEquals(result, expected);
   }

   @Test
   public void testDeserializeEmpty() throws IOException {
       CollectionDeserializer deserializer = new CollectionDeserializer(Collection.class, null, null, null);
       ObjectMapper mapper = new ObjectMapper();
       JsonFactory factory = new JsonFactory();
       JsonNode node = mapper.readTree(factory.createParser("[]"));
       Collection<Object> result = deserializer.deserialize(node.traverse(mapper), mapper);
       assertTrue(result.isEmpty());
   }

   @Test
   public void testDeserializeNonArray() throws IOException {
       CollectionDeserializer deserializer = new CollectionDeserializer(Collection.class, null, null, null);
       ObjectMapper mapper = new ObjectMapper();
       JsonFactory factory = new JsonFactory();
       JsonNode node = mapper.readTree(factory.createParser("1"));
       Collection<Object> result = deserializer.deserialize(node.traverse(mapper), mapper);
       List<Object> expected = new ArrayList<>();
       expected.add(1);
       assertEquals(result, expected);
   }

   @Test
   public void testDeserializeNull() throws IOException {
       CollectionDeserializer deserializer = new CollectionDeserializer(Collection.class, null, null, null);
       ObjectMapper mapper = new ObjectMapper();
       JsonFactory factory = new JsonFactory();
       JsonNode node = mapper.readTree(factory.createParser("null"));
       Collection<Object> result = deserializer.deserialize(node.traverse(mapper), mapper);
       assertNull(result);
   }
}