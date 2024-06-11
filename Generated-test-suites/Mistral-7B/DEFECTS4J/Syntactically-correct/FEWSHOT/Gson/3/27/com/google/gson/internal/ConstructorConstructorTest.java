package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

 private static final Map<Type, InstanceCreator<?>> INSTANCE_CREATORS = new LinkedHashMap<>();

 static {
 INSTANCE_CREATORS.put(String[].class, new StringArrayInstanceCreator());
 INSTANCE_CREATORS.put(Integer[].class, new IntArrayInstanceCreator());
 INSTANCE_CREATORS.put(Map.class, new MapInstanceCreator());
 INSTANCE_CREATORS.put(TreeMap.class, new TreeMapInstanceCreator());
 INSTANCE_CREATORS.put(LinkedHashMap.class, new LinkedHashMapInstanceCreator());
 INSTANCE_CREATORS.put(TreeSet.class, new TreeSetInstanceCreator<>());
 INSTANCE_CREATORS.put(LinkedHashSet.class, new LinkedHashSetInstanceCreator<>());
 INSTANCE_CREATORS.put(ArrayList.class, new ArrayListInstanceCreator<>());
 INSTANCE_CREATORS.put(LinkedList.class, new LinkedListInstanceCreator<>());
 }

 @Test
 public void testGetConstructorByTypeToken() {
 final ConstructorConstructor constructorConstructor = new ConstructorConstructor(INSTANCE_CREATORS);

 // Test with primitive types
 TypeToken<int[]> intArrayTypeToken = new TypeToken<int[]>() {};
 ObjectConstructor<int[]> intArrayConstructor = constructorConstructor.get(intArrayTypeToken);
 assertInstanceOf(ObjectConstructor.class, intArrayConstructor);
 assertInstanceOf(int[].class, intArrayConstructor.construct());

 // Test with custom classes
 class TestClass {
 int x;
 TestClass(int x) {
 this.x = x;
 }
 }

 TypeToken<TestClass> testClassTypeToken = new TypeToken<TestClass>() {};
 ObjectConstructor<TestClass> testClassConstructor = constructorConstructor.get(testClassTypeToken);
 assertInstanceOf(ObjectConstructor.class, testClassConstructor);
 TestClass testClassInstance = testClassConstructor.construct();
 assertEquals(0, testClassInstance.x);

 // Test with interfaces
 @SuppressWarnings("unchecked")
 class TestMap implements Map<String, Integer> {
 private final LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

 public int size() {
 return map.size();
 }

 // Implement other Map methods here
 }

 TypeToken<TestMap> testMapTypeToken = new TypeToken<TestMap>() {};
 ObjectConstructor<TestMap> testMapConstructor = constructorConstructor.get(testMapTypeToken);
 assertInstanceOf(ObjectConstructor.class, testMapConstructor);
 assertInstanceOf(TestMap.class, testMapConstructor.construct());

 // Test null return
 TypeToken<String> nullTypeToken = null;
 ObjectConstructor<String> nullConstructor = constructorConstructor.get(nullTypeToken);
 assertNull(nullConstructor);
 }

 @Test
 public void testGetConstructorByRawType() {
 final ConstructorConstructor constructorConstructor = new ConstructorConstructor(INSTANCE_CREATORS);

 // Test with primitive types
 TypeToken<int[]> intArrayTypeToken = new TypeToken<int[]>() {};
 ObjectConstructor<int[]> intArrayConstructor = constructorConstructor.get(intArrayTypeToken.getType());
 assertInstanceOf(ObjectConstructor.class, intArrayConstructor);
 assertInstanceOf(int[].class, intArrayConstructor.construct());

 // Test with custom classes
 class TestClass {
 int x;
 TestClass(int x) {
 this.x = x;
 }
 }

 ObjectConstructor<TestClass> testClassConstructor = constructorConstructor.get(TestClass.class);
 assertInstanceOf(ObjectConstructor.class, testClassConstructor);
 TestClass testClassInstance = testClassConstructor.construct();
 assertEquals(0, testClassInstance.x);

 // Test with interfaces
 @SuppressWarnings("unchecked")
 class TestMap implements Map<String, Integer> {
 private final LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

 public int size() {
 return map.size();
 }

 // Implement other Map methods here
 }

 ObjectConstructor<TestMap> testMapConstructor = constructorConstructor.get(TestMap.class);
 assertInstanceOf(ObjectConstructor.class, testMapConstructor);
 assertInstanceOf(TestMap.class, testMapConstructor.construct());

 // Test null return
 Class<?> nullClass = null;
 ObjectConstructor<Object> nullConstructor = constructorConstructor.get(nullClass);
 assertNull(nullConstructor);
 }

 @Test
 public void testNewDefaultConstructor() {
 final ConstructorConstructor constructorConstructor = new ConstructorConstructor(INSTANCE_CREATORS);

 // Test with primitive types
 final Class<int[]> intArrayClass = int[].class;
 ObjectConstructor<int[]> intArrayConstructor = constructorConstructor.newDefaultConstructor(intArrayClass);
 assertInstanceOf(ObjectConstructor.class, intArrayConstructor);
 assertInstanceOf(int[].class, intArrayConstructor.construct());

 // Test with custom classes
 class TestClass {
 int x;
 TestClass() {
 this.x = 0;
 }
 }

 ObjectConstructor<TestClass> testClassConstructor = constructorConstructor.newDefaultConstructor(TestClass.class);
 assertInstanceOf(ObjectConstructor.class, testClassConstructor);
 TestClass testClassInstance = testClassConstructor.construct();
 assertEquals(0, testClassInstance.x);

 // Test with interfaces
 @SuppressWarnings("unchecked")
 class TestMap implements Map<String, Integer> {
 private final LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

 public int size() {
 return map.size();
 }

 // Implement other Map methods here
 }

 ObjectConstructor<TestMap> testMapConstructor = constructorConstructor.newDefaultConstructor(TestMap.class);
 assertInstanceOf(ObjectConstructor.class, testMapConstructor);
 assertInstanceOf(TestMap.class, testMapConstructor.construct());

 // Test null return
 Class<?> nullClass = null;
 ObjectConstructor<Object> nullConstructor = constructorConstructor.newDefaultConstructor(nullClass);
 assertNull(nullConstructor);
 }

 @Test
 public void testNewDefaultImplementationConstructor() {
 final ConstructorConstructor constructorConstructor = new ConstructorConstructor(INSTANCE_CREATORS);

 // Test with Collection interface
 ObjectConstructor<Collection<Object>> collectionConstructor = constructorConstructor.newDefaultImplementationConstructor(Collection.class, Object[].class);
 assertInstanceOf(ObjectConstructor.class, collectionConstructor);
 assertInstanceOf(ArrayList.class, collectionConstructor.construct());

 // Test with Set interface
 ObjectConstructor<Set<Object>> setConstructor = constructorConstructor.newDefaultImplementationConstructor(Set.class, Object[].class);
 assertInstanceOf(ObjectConstructor.class, setConstructor);
 assertInstanceOf(LinkedHashSet.class, setConstructor.construct());

 // Test with SortedSet interface
 ObjectConstructor<SortedSet<Object>> sortedSetConstructor = constructorConstructor.newDefaultImplementationConstructor(SortedSet.class, Object[].class);
 assertInstanceOf(ObjectConstructor.class, sortedSetConstructor);
 assertInstanceOf(TreeSet.class, sortedSetConstructor.construct());

 // Test with Map interface
 ObjectConstructor<Map<String, Object>> mapConstructor = constructorConstructor.newDefaultImplementationConstructor(Map.class, String[].class, Object[].class);
 assertInstanceOf(ObjectConstructor.class, mapConstructor);
 assertInstanceOf(LinkedHashMap.class, mapConstructor.construct());

 // Test with SortedMap interface
 ObjectConstructor<SortedMap<String, Object>> sortedMapConstructor = constructorConstructor.newDefaultImplementationConstructor(SortedMap.class, String[].class, Object[].class);
 assertInstanceOf(ObjectConstructor.class, sortedMapConstructor);
 assertInstanceOf(TreeMap.class, sortedMapConstructor.construct());

 // Test with null return
 Class<?> nullClass = null;
 ObjectConstructor<Object> nullConstructor = constructorConstructor.newDefaultImplementationConstructor(nullClass, Object[].class);
 assertNull(nullConstructor);
 }

 @Test
 public void testNewUnsafeAllocator() {
 final ConstructorConstructor constructorConstructor = new ConstructorConstructor(INSTANCE_CREATORS);

 // Test with primitive types
 final Type intArrayType = new TypeToken<int[]>() {}.getType();
 ObjectConstructor<int[]> intArrayConstructor = constructorConstructor.newUnsafeAllocator(intArrayType, int[].class);
 assertInstanceOf(ObjectConstructor.class, intArrayConstructor);
 assertInstanceOf(int[].class, intArrayConstructor.construct());

 // Test with custom classes
 class TestClass {
 int x;
 TestClass() {
 this.x = 0;
 }
 }

 ObjectConstructor<TestClass> testClassConstructor = constructorConstructor.newUnsafeAllocator(TestClass.class, TestClass.class);
 assertInstanceOf(ObjectConstructor.class, testClassConstructor);
 TestClass testClassInstance = testClassConstructor.construct();
 assertEquals(0, testClassInstance.x);

 // Test with interfaces
 @SuppressWarnings("unchecked")
 class TestMap implements Map<String, Integer> {
 private final LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

 public int size() {
 return map.size();
 }

 // Implement other Map methods here
 }

 ObjectConstructor<TestMap> testMapConstructor = constructorConstructor.newUnsafeAllocator(TestMap.class, TestMap.class);
 assertInstanceOf(ObjectConstructor.class, testMapConstructor);
 assertInstanceOf(TestMap.class, testMapConstructor.construct());

 // Test null return
 Class<?> nullClass = null;
 ObjectConstructor<Object> nullConstructor = constructorConstructor.newUnsafeAllocator(nullClass, Object.class);
 assertNull(nullConstructor);
 }
}