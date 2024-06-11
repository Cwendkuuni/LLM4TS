import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
 private final ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

 @Test
 public void testGetInstance() {
 Map<String, String> map = constructorConstructor.get(new TypeToken<Map<String, String>>() {}.getType()).construct();
 Assert.assertNotNull(map);

 EnumSet<DayOfWeek> days = constructorConstructor.get(new TypeToken<EnumSet<DayOfWeek>>() {}.getType()).construct();
 Assert.assertNotNull(days);

 ArrayList<String> list = constructorConstructor.get(new TypeToken<ArrayList<String>>() {}.getType()).construct();
 Assert.assertNotNull(list);

 LinkedList<String> linkList = constructorConstructor.get(new TypeToken<LinkedList<String>>() {}.getType()).construct();
 Assert.assertNotNull(linkList);

 LinkedHashSet<String> set = constructorConstructor.get(new TypeToken<LinkedHashSet<String>>() {}.getType()).construct();
 Assert.assertNotNull(set);

 TreeMap<String, String> sortedMap = constructorConstructor.get(new TypeToken<TreeMap<String, String>>() {}.getType()).construct();
 Assert.assertNotNull(sortedMap);

 TreeSet<String> sortedSet = constructorConstructor.get(new TypeToken<TreeSet<String>>() {}.getType()).construct();
 Assert.assertNotNull(sortedSet);
 }

 @Test(expected = RuntimeException.class)
 public void testGetNullInstance() {
 constructorConstructor.get(null);
 }

 @Test(expected = RuntimeException.class)
 public void testGetUnsupportedInstance() {
 constructorConstructor.get(new TypeToken<Integer>() {}.getType());
 }

 @Test
 public void testGetInstanceCreator() {
 instanceCreators.put(new TypeToken<ArrayList<String>>() {}.getType(), new ArrayListInstanceCreator());
 instanceCreators.put(new TypeToken<LinkedList<String>>() {}.getType(), new LinkedListInstanceCreator());

 ArrayList<String> list1 = constructorConstructor.get(new TypeToken<ArrayList<String>>() {}.getType()).construct();
 LinkedList<String> list2 = constructorConstructor.get(new TypeToken<LinkedList<String>>() {}.getType()).construct();

 Assert.assertNotNull(list1);
 Assert.assertNotNull(list2);
 Assert.assertNotSame(list1, list2);
 }

 @Test
 public void testToString() {
 Assert.assertEquals("{@com.google.gson.internal.ConstructorConstructor@3263e110}", constructorConstructor.toString());
 }

 static class ArrayListInstanceCreator implements InstanceCreator<ArrayList<String>> {
 @Override public ArrayList<String> createInstance(Type type) {
 return new ArrayList<String>();
 }
 }

 static class LinkedListInstanceCreator implements InstanceCreator<LinkedList<String>> {
 @Override public LinkedList<String> createInstance(Type type) {
 return new LinkedList<String>();
 }
 }
}