import static org.junit.Assert.*;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

public class ConstructorConstructorTest {

 private ConstructorConstructor constructorConstructor;

 @Before
 public void setUp() {
 Map<Type, InstanceCreator<?>> instanceCreatorsMap = new LinkedHashMap<>();
 InstanceCreator<String> stringInstanceCreator = new StringInstanceCreator();
 instanceCreatorsMap.put(String.class, stringInstanceCreator);
 constructorConstructor = new ConstructorConstructor(instanceCreatorsMap);
 }

 @Test
 public void testGet_TypeToken_ReturnsObjectConstructor() {
 TypeToken<Integer> integerTypeToken = new TypeToken<Integer>() {};
 ObjectConstructor<Integer> constructor = constructorConstructor.get(integerTypeToken);
 assertNotNull(constructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_TypeToken_NoInstanceCreator_ThrowsRuntimeException() {
 TypeToken<Queue<String>> queueTypeToken = new TypeToken<Queue<String>>() {};
 ObjectConstructor<Queue<String>> constructor = constructorConstructor.get(queueTypeToken);
 fail("Expected a RuntimeException to be thrown");
 }

 @Test
 public void testGet_TypeToken_RawTypeMatch_ReturnsObjectConstructor() {
 TypeToken<List<String>> listTypeToken = new TypeToken<List<String>>() {};
 ObjectConstructor<List<String>> constructor = constructorConstructor.get(listTypeToken);
 assertNotNull(constructor);
 }

 @Test
 public void testGet_TypeToken_DefaultConstructor_ReturnsObjectConstructor() {
 TypeToken<ArrayList> arrayListTypeToken = new TypeToken<ArrayList>() {};
 ObjectConstructor<ArrayList> constructor = constructorConstructor.get(arrayListTypeToken);
 assertNotNull(constructor);
 }

 @Test
 public void testGet_TypeToken_DefaultImplementationConstructor_ReturnsObjectConstructor() {
 TypeToken<TreeMap<String, Integer>> treeMapTypeToken = new TypeToken<TreeMap<String, Integer>>() {};
 ObjectConstructor<TreeMap<String, Integer>> constructor = constructorConstructor.get(treeMapTypeToken);
 assertNotNull(constructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_TypeToken_UnsafeAllocator_ThrowsRuntimeException() {
 TypeToken<SortedMap<String, String>> sortedMapTypeToken = new TypeToken<SortedMap<String, String>>() {};
 ObjectConstructor<SortedMap<String, String>> constructor = constructorConstructor.get(sortedMapTypeToken);
 fail("Expected a RuntimeException to be thrown");
 }

 @Test
 public void testGet_Type_ReturnsObjectConstructor() {
 Type stringType = String.class;
 ObjectConstructor<String> constructor = constructorConstructor.get(stringType);
 assertNotNull(constructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_Type_NoInstanceCreator_ThrowsRuntimeException() {
 Type queueType = Queue.class;
 ObjectConstructor<Queue> constructor = constructorConstructor.get(queueType);
 fail("Expected a RuntimeException to be thrown");
 }

 @Test
 public void testGet_Class_RawTypeMatch_ReturnsObjectConstructor() {
 Type listType = List.class;
 ObjectConstructor<List> constructor = constructorConstructor.get(listType);
 assertNotNull(constructor);
 }

 @Test
 public void testGet_Class_DefaultConstructor_ReturnsObjectConstructor() {
 Type ArrayListType = ArrayList.class;
 ObjectConstructor<ArrayList> constructor = constructorConstructor.get(ArrayListType);
 assertNotNull(constructor);
 }

 @Test
 public void testGet_Class_DefaultImplementationConstructor_ReturnsObjectConstructor() {
 Type TreeMapType = TreeMap.class;
 ObjectConstructor<TreeMap<String, Integer>> constructor = constructorConstructor.get(TreeMapType);
 assertNotNull(constructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_Class_UnsafeAllocator_ThrowsRuntimeException() {
 Type SortedMapType = SortedMap.class;
 ObjectConstructor<SortedMap<String, String>> constructor = constructorConstructor.get(SortedMapType);
 fail("Expected a RuntimeException to be thrown");
 }

 @Test
 public void testToString() {
 assertEquals("{@com.google.gson.internal.ConstructorConstructor@4436d9b2}", constructorConstructor.toString());
 }
}

class StringInstanceCreator implements InstanceCreator<String> {
 @Override public String createInstance(Type type) {
 return "default";
 }
}