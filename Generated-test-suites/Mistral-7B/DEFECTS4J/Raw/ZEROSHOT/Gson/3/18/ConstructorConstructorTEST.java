import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import com.google.gson.InstanceCreator;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

 private final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

 @Test
 public void testGetConstructor() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

 // Test for Map
 Type mapType = new TypeToken<Map<Integer, String>>() {}.getType();
 ObjectConstructor<Map<Integer, String>> mapConstructor = constructorConstructor.get(new TypeToken<Map<Integer, String>>() {}.getType());
 Map<Integer, String> map = mapConstructor.construct();
 Assert.assertNotNull(map);
 Assert.assertEquals(Collections.emptyMap().getClass(), map.getClass());

 // Test for SortedMap
 Type sortedMapType = new TypeToken<SortedMap<Integer, String>>() {}.getType();
 ObjectConstructor<SortedMap<Integer, String>> sortedMapConstructor = constructorConstructor.get(new TypeToken<SortedMap<Integer, String>>() {}.getType());
 SortedMap<Integer, String> sortedMap = sortedMapConstructor.construct();
 Assert.assertNotNull(sortedMap);
 Assert.assertEquals(TreeMap.class, sortedMap.getClass());

 // Test for List
 Type listType = new TypeToken<List<Integer>>() {}.getType();
 ObjectConstructor<List<Integer>> listConstructor = constructorConstructor.get(new TypeToken<List<Integer>>() {}.getType());
 List<Integer> list = listConstructor.construct();
 Assert.assertNotNull(list);
 Assert.assertEquals(ArrayList.class, list.getClass());

 // Test for Queue
 Type queueType = new TypeToken<Queue<Integer>>() {}.getType();
 ObjectConstructor<Queue<Integer>> queueConstructor = constructorConstructor.get(new TypeToken<Queue<Integer>>() {}.getType());
 Queue<Integer> queue = queueConstructor.construct();
 Assert.assertNotNull(queue);
 Assert.assertEquals(LinkedList.class, queue.getClass());

 // Test for Set
 Type setType = new TypeToken<Set<Integer>>() {}.getType();
 ObjectConstructor<Set<Integer>> setConstructor = constructorConstructor.get(new TypeToken<Set<Integer>>() {}.getType());
 Set<Integer> set = setConstructor.construct();
 Assert.assertNotNull(set);
 Assert.assertEquals(LinkedHashSet.class, set.getClass());

 // Test for EnumSet
 Type enumSetType = new TypeToken<EnumSet<MyEnum>>() {}.getType();
 ObjectConstructor<EnumSet<MyEnum>> enumSetConstructor = constructorConstructor.get(enumSetType);
 EnumSet<MyEnum> enumSet = enumSetConstructor.construct();
 Assert.assertNotNull(enumSet);
 Assert.assertEquals(EnumSet.noneOf(MyEnum.class).getClass(), enumSet.getClass());
 }

 @Test(expected = RuntimeException.class)
 public void testGetConstructorInvalidType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type invalidType = new TypeToken<StringBuilder>() {}.getType();
 ObjectConstructor<StringBuilder> sbConstructor = constructorConstructor.get(invalidType);
 }

 @Test
 public void testGetConstructorRawType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type listType = new TypeToken<List<Integer>>() {}.getType();
 ObjectConstructor<List<Integer>> listConstructor = constructorConstructor.get(listType.getRawType());
 List<Integer> list = listConstructor.construct();
 Assert.assertNotNull(list);
 Assert.assertEquals(ArrayList.class, list.getClass());
 }

 @Test
 public void testGetConstructorInstanceCreator() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 InstanceCreator<Map<Integer, String>> mapCreator = new InstanceCreator<Map<Integer, String>>() {
 @Override public Map<Integer, String> createInstance(Type type) {
 return new HashMap<Integer, String>();
 }
 };
 instanceCreators.put(new TypeToken<Map<Integer, String>>() {}.getType(), mapCreator);
 ObjectConstructor<Map<Integer, String>> mapConstructor = constructorConstructor.get(new TypeToken<Map<Integer, String>>() {}.getType());
 Map<Integer, String> map = mapConstructor.construct();
 Assert.assertNotNull(map);
 Assert.assertEquals(HashMap.class, map.getClass());
 }

 @Test
 public void testGetConstructorUnsupportedType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type unsupportedType = new TypeToken<MyUnsupportedType>() {}.getType();
 ObjectConstructor<MyUnsupportedType> constructor = constructorConstructor.get(unsupportedType);
 Assert.assertNull(constructor);
 }

 @Test
 public void testToString() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 String toString = constructorConstructor.toString();
 Assert.assertTrue(toString.contains("instanceCreators="));
 }
}

 class MyEnum {
 @SuppressWarnings("unused")
 public static final MyEnum VAL1 = new MyEnum();
 @SuppressWarnings("unused")
 public static final MyEnum VAL2 = new MyEnum();
 }

 class MyUnsupportedType {}