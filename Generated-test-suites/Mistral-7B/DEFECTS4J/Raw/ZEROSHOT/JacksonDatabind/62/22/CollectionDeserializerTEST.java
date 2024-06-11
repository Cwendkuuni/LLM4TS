package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.*;

@JacksonStdImpl
public class CollectionDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testCollectionDeserialization() throws IOException {
 CollectionDeserializer deserializer = createCollectionDeserializer();
 ObjectReader reader = mapper.readerForType(List.class);

 // Test empty collection
 Object result = deserializer.deserialize(mapper.createParser("[]"), null);
 assertTrue(result instanceof List);
 assertEquals(0, ((List) result).size());

 // Test deserialization of JSON array with simple objects
 String json = "[{\"name\":\"Alice\",\"age\":30},{\"name\":\"Bob\",\"age\":25}]";
 Object collection = deserializer.deserialize(mapper.createParser(json), null);
 assertTrue(collection instanceof List);
 List list = (List) collection;
 assertEquals(2, list.size());
 assertEquals("Alice", list.get(0).getClass().getDeclaredField("name").get(list.get(0)));
 assertEquals(30, list.get(0).getClass().getDeclaredField("age").get(list.get(0)));
 assertEquals("Bob", list.get(1).getClass().getDeclaredField("name").get(list.get(1)));
 assertEquals(25, list.get(1).getClass().getDeclaredField("age").get(list.get(1)));

 // Test deserialization of JSON array with objects containing references
 Object referencedObject = new Object() {{
 @JsonFormat(serializable = false)
 String name = "John";
 int id = 1;
 }};
 String jsonWithReferences = "[{\"id\":1},{\"id\":1, \"reference\":\"" + referencedObject.getClass().getName() + "\"}]";
 Object collectionWithReferences = deserializer.deserialize(mapper.createParser(jsonWithReferences), null);
 assertTrue(collectionWithReferences instanceof List);
 List listWithReferences = (List) collectionWithReferences;
 assertEquals(2, listWithReferences.size());
 Object firstObject = listWithReferences.get(0);
 Object secondObject = listWithReferences.get(1);
 assertSame(referencedObject, secondObject);

 // Test deserialization of JSON array with types
 TypeFactory typeFactory = mapper.getTypeFactory();
 JavaType javaType = typeFactory.constructCollectionType(ArrayList.class, String.class);
 String jsonWithTypes = "[\"Alice\",\"Bob\"]";
 Object collectionWithTypes = deserializer.deserialize(mapper.createParser(jsonWithTypes), javaType);
 assertTrue(collectionWithTypes instanceof List);
 List<String> listWithTypes = (List<String>) collectionWithTypes;
 assertEquals(2, listWithTypes.size());
 assertEquals("Alice", listWithTypes.get(0));
 assertEquals("Bob", listWithTypes.get(1));
 }

 private CollectionDeserializer createCollectionDeserializer() {
 JavaType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Object.class);
 JsonDeserializer<Object> valueDeserializer = mapper.deserializerForClass(String.class);
 TypeDeserializer valueTypeDeserializer = mapper.getTypeDeserializerFor(String.class, null);
 ValueInstantiator valueInstantiator = mapper.valueInstantiator();
 JsonDeserializer<Object> delegateDeserializer = null;
 Boolean unwrapSingle = Boolean.FALSE;
 return new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer,
 valueInstantiator, delegateDeserializer, unwrapSingle);
 }
}