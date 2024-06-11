package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TestBase;

public class SetterlessPropertyTest extends TestBase {

 private static final Object MAP_VALUE = new Object() {};
 private static final Object COLLECTION_VALUE = new Object() {};

 @SuppressWarnings("unchecked")
 private static final SetterlessProperty<Map<String, Object>> mapProperty = new SetterlessProperty<>(
         new BeanPropertyDefinition(Map.class, "mapProperty"),
         JsonTypeFactory.defaultInstance.constructType(Map.class),
         null,
         null,
         (AnnotatedMethod) ReflectionUtil.findMethod(Map.class, "putAll, (Map<K,V> m, Map<K,V> map)"));

 @SuppressWarnings("unchecked")
 private static final SetterlessProperty<List<Object>> listProperty = new SetterlessProperty<>(
         new BeanPropertyDefinition(List.class, "listProperty"),
         JsonTypeFactory.defaultInstance.constructType(List.class),
         null,
         null,
         (AnnotatedMethod) ReflectionUtil.findMethod(List.class, "add, (List<E> list, E e)"));

 @Test
 public void testMapSetterlessProperty() throws Exception {
 Map<String, Object> map = new HashMap<>();
 ObjectMapper mapper = new ObjectMapper();

 // Test with empty input
 String jsonEmpty = "{}";
 JsonParser parser = mapper.getFactory().createParser(jsonEmpty);
 mapper.readValue(parser, Map.class, map);
 assertNotNull(map);
 assertEquals(0, map.size());
 mapProperty.fixAccess(mapper.getConfiguration());
 mapper.readValue(parser, mapProperty.getType(), map);
 assertNotNull(map);
 assertEquals(0, map.size());

 // Test with non-empty input
 String jsonMap = "{\"key1\": null, \"key2\": 123}";
 parser = mapper.getFactory().createParser(jsonMap);
 mapper.readValue(parser, Map.class, map);
 assertNotNull(map);
 assertEquals(2, map.size());
 mapProperty.fixAccess(mapper.getConfiguration());
 mapper.readValue(parser, mapProperty.getType(), map);
 assertNotNull(map);
 assertEquals(2, map.size());
 assertTrue(map.containsKey("key1"));
 assertNull(map.get("key1"));
 assertTrue(map.containsKey("key2"));
 assertEquals(123, map.get("key2"));

 // Test with null value
 String jsonMapNull = "{\"key1\": null}";
 parser = mapper.getFactory().createParser(jsonMapNull);
 mapper.readValue(parser, Map.class, map);
 assertNotNull(map);
 assertEquals(1, map.size());
 mapProperty.fixAccess(mapper.getConfiguration());
 mapper.readValue(parser, mapProperty.getType(), map);
 assertNotNull(map);
 assertEquals(1, map.size());
 assertTrue(map.containsKey("key1"));
 assertNull(map.get("key1"));

 // Test with empty JSON array
 String jsonArrayEmpty = "[ ]";
 parser = mapper.getFactory().createParser(jsonArrayEmpty);
 mapper.readValue(parser, List.class, map);
 assertNotNull(map);
 assertEquals(0, map.size());
 listProperty.fixAccess(mapper.getConfiguration());
 mapper.readValue(parser, listProperty.getType(), map);
 assertNotNull(map);
 assertEquals(0, map.size());

 // Test with non-empty JSON array
 String jsonArray = "[1, 2, 3]";
 parser = mapper.getFactory().createParser(jsonArray);
 mapper.readValue(parser, List.class, map);
 assertNotNull(map);
 assertEquals(3, map.size());
 listProperty.fixAccess(mapper.getConfiguration());
 mapper.readValue(parser, listProperty.getType(), map);
 assertNotNull(map);
 assertEquals(3, map.size());
 assertEquals(1, ((List<Object>) map).get(0).intValue());
 assertEquals(2, ((List<Object>) map).get(1).intValue());
 assertEquals(3, ((List<Object>) map).get(2).intValue());
 }

 @Test
 public void testListSetterlessProperty() throws Exception {
 List<Object> list = new ArrayList<>();
 ObjectMapper mapper = new ObjectMapper();

 // Test with empty input
 String jsonEmpty = "[]";
 JsonParser parser = mapper.getFactory().createParser(jsonEmpty);
 mapper.readValue(parser, List.class, list);
 assertNotNull(list);
 assertEquals(0, list.size());
 listProperty.fixAccess(mapper.getConfiguration());
 mapper.readValue(parser, listProperty.getType(), list);
 assertNotNull(list);
 assertEquals(0, list.size());

 // Test with non-empty input
 String jsonList = "[null, 123]";
 parser = mapper.getFactory().createParser(jsonList);
 mapper.readValue(parser, List.class, list);
 assertNotNull(list);
 assertEquals(2, list.size());
 listProperty.fixAccess(mapper.getConfiguration());
 mapper.readValue(parser, listProperty.getType(), list);
 assertNotNull(list);
 assertEquals(2, list.size());
 assertNull(list.get(0));
 assertEquals(123, list.get(1).intValue());

 // Test with null value
 String jsonListNull = "[null]";
 parser = mapper.getFactory().createParser(jsonListNull);
 mapper.readValue(parser, List.class, list);
 assertNotNull(list);
 assertEquals(1, list.size());
 listProperty.fixAccess(mapper.getConfiguration());
 mapper.readValue(parser, listProperty.getType(), list);
 assertNotNull(list);
 assertEquals(1, list.size());
 assertNull(list.get(0));

 // Test with empty JSON object
 String jsonObjectEmpty = "{}";
 parser = mapper.getFactory().createParser(jsonObjectEmpty);
 mapper.readValue(parser, Map.class, map);
 mapper.readValue(parser, mapProperty.getType(), map);
 assertNotNull(map);
 assertEquals(0, map.size());
 mapProperty.fixAccess(mapper.getConfiguration());
 mapper.readValue(parser, mapProperty.getType(), map);
 assertNotNull(map);
 assertEquals(0, map.size());

 // Test with non-empty JSON object
 String jsonObject = "{\"key\": null}";
 parser = mapper.getFactory().createParser(jsonObject);
 mapper.readValue(parser, Map.class, map);
 mapper.readValue(parser, mapProperty.getType(), map);
 assertNotNull(map);
 assertEquals(1, map.size());
 assertNull(map.get("key"));
 }

}