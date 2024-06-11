package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

 private ConstructorConstructor constructorConstructor;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 constructorConstructor = new ConstructorConstructor(InstanceCreator.Factory.INSTANCE);
 }

 @Test
 public void testGetCollectionInstance() {
 Type collectionType = new TypeToken<Collection<String>>(){}.getType();
 ObjectConstructor constructor = constructorConstructor.get(new TypeToken<Collection<String>>(){});
 Collection<String> collection = constructor.construct();
 assertNotNull(collection);
 assertEquals(0, collection.size());
 }

 @Test
 public void testGetSetInstance() {
 Type setType = new TypeToken<Set<Integer>>(){}.getType();
 ObjectConstructor constructor = constructorConstructor.get(new TypeToken<Set<Integer>>(){});
 Set<Integer> set = constructor.construct();
 assertNotNull(set);
 assertEquals(0, set.size());
 }

 @Test
 public void testGetMapInstance() {
 Type mapType = new TypeToken<Map<String, Integer>>(){}.getType();
 ObjectConstructor constructor = constructorConstructor.get(new TypeToken<Map<String, Integer>>(){});
 Map<String, Integer> map = constructor.construct();
 assertNotNull(map);
 assertEquals(0, map.size());
 }

 @Test
 public void testGetSortedMapInstance() {
 Type sortedMapType = new TypeToken<SortedMap<String, Integer>>(){}.getType();
 ObjectConstructor constructor = constructorConstructor.get(new TypeToken<SortedMap<String, Integer>>(){});
 SortedMap<String, Integer> sortedMap = constructor.construct();
 assertNotNull(sortedMap);
 assertEquals(0, sortedMap.size());
 }

 @Test
 public void testGetEnumSetInstance() {
 Type enumSetType = new TypeToken<EnumSet<MyEnum>>(){}.getType();
 ObjectConstructor constructor = constructorConstructor.get(new TypeToken<EnumSet<MyEnum>>(){});
 EnumSet<MyEnum> enumSet = constructor.construct();
 assertNotNull(enumSet);
 assertEquals(0, enumSet.size());
 }

 @Test
 public void testGetListInstance() {
 Type listType = new TypeToken<ArrayList<Integer>>(){}.getType();
 ObjectConstructor constructor = constructorConstructor.get(new TypeToken<ArrayList<Integer>>(){});
 ArrayList<Integer> list = constructor.construct();
 assertNotNull(list);
 assertEquals(0, list.size());
 }

 @Test
 public void testGetLinkedHashMapInstance() {
 Type linkedHashMapType = new TypeToken<LinkedHashMap<String, Integer>>(){}.getType();
 ObjectConstructor constructor = constructorConstructor.get(new TypeToken<LinkedHashMap<String, Integer>>(){});
 LinkedHashMap<String, Integer> linkedHashMap = constructor.construct();
 assertNotNull(linkedHashMap);
 assertEquals(0, linkedHashMap.size());
 }

 @Test
 public void testGetLinkedTreeMapInstance() {
 Type linkedTreeMapType = new TypeToken<LinkedTreeMap<String, Integer>>(){}.getType();
 ObjectConstructor constructor = constructorConstructor.get(new TypeToken<LinkedTreeMap<String, Integer>>(){});
 LinkedTreeMap<String, Integer> linkedTreeMap = constructor.construct();
 assertNotNull(linkedTreeMap);
 assertEquals(0, linkedTreeMap.size());
 }

 @Test(expected = RuntimeException.class)
 public void testGetInvalidTypeInstance() {
 ObjectConstructor constructor = constructorConstructor.get(new TypeToken<Object>(){});
 constructor.construct();
 }

 @Test(expected = JsonIOException.class)
 public void testGetInvalidEnumSetTypeInstance() {
 Type enumSetType = new TypeToken<EnumSet<String>>(){}.getType();
 ObjectConstructor constructor = constructorConstructor.get(new TypeToken<EnumSet<String>>(){});
 constructor.construct();
 }
}