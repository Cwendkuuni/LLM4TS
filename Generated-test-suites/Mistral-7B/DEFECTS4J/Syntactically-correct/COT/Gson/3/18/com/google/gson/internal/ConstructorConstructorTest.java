import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Type;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConstructorConstructorTest {

 private Map<Type, InstanceCreator<?>> instanceCreators = new ConcurrentHashMap<>();
 private ConstructorConstructor constructorConstructor;

 @Test
 public void testConstructorConstructor() {
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 }

 @Test
 public void testGet_TypeToken_TypeTokenMatchesRawType() {
 Type typeToken = new TypeToken<Map<String, String>>() {};
 ObjectConstructor<Map<String, String>> constructor = constructorConstructor.get(typeToken);
 assertEquals(Map.class, constructor.getType());
 assertEquals(String.class, constructor.getParameterTypes()[0]);
 assertEquals(String.class, constructor.getParameterTypes()[1]);
 }

 @Test
 public void testGet_TypeToken_TypeTokenMatchesInstanceCreator() {
 InstanceCreator<Map<String, String>> instanceCreator = new InstanceCreator<Map<String, String>>() {
 @Override
 public Map<String, String> createInstance(Type type) {
 return new LinkedHashMap<String, String>();
 }
 };
 instanceCreators.put(Collections.singletonMap(Map.class, instanceCreator));
 Type typeToken = new TypeToken<Map<String, String>>() {};
 ObjectConstructor<Map<String, String>> constructor = constructorConstructor.get(typeToken);
 assertEquals(instanceCreator, constructor.getInstanceCreator());
 }

 @Test
 public void testGet_TypeToken_ReturnsDefaultConstructor() {
 Type typeToken = new TypeToken<Object>() {};
 ObjectConstructor<Object> constructor = constructorConstructor.get(typeToken);
 assertNotNull(constructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_TypeToken_NoDefaultConstructor() {
 Type typeToken = new TypeToken<StringBuilder>() {};
 constructorConstructor.get(typeToken);
 }

 @Test
 public void testGet_TypeToken_ReturnsDefaultImplementationConstructor() {
 Type typeToken = new TypeToken<LinkedHashSet<String>>() {};
 ObjectConstructor<LinkedHashSet<String>> constructor = constructorConstructor.get(typeToken);
 assertEquals(LinkedHashSet.class, constructor.getType());
 }

 @Test
 public void testGet_TypeToken_ReturnsUnsafeConstructor() {
 Type typeToken = new TypeToken<SortedMap<String, String>>() {};
 ObjectConstructor<SortedMap<String, String>> constructor = constructorConstructor.get(typeToken);
 assertEquals(SortedMap.class, constructor.getType());
 }

 @Test(expected = RuntimeException.class)
 public void testGet_TypeToken_UnknownType() {
 Type typeToken = new TypeToken<Object>() {
 };
 typeToken.setType(new TypeToken<Integer>() {}.getType());
 constructorConstructor.get(typeToken);
 }

 @Test
 public void testGet_Class_TypeTokenMatchesRawType() {
 Type typeToken = new TypeToken<Map<String, String>>() {};
 ObjectConstructor<Map<String, String>> constructor = constructorConstructor.get(Map.class);
 assertEquals(Map.class, constructor.getType());
 }

 @Test
 public void testGet_Class_TypeTokenMatchesInstanceCreator() {
 InstanceCreator<Map<String, String>> instanceCreator = new InstanceCreator<Map<String, String>>() {
 @Override
 public Map<String, String> createInstance(Type type) {
 return new LinkedHashMap<String, String>();
 }
 };
 instanceCreators.put(Map.class, instanceCreator);
 ObjectConstructor<Map<String, String>> constructor = constructorConstructor.get(Map.class);
 assertEquals(instanceCreator, constructor.getInstanceCreator());
 }

 @Test
 public void testGet_Class_ReturnsDefaultConstructor() {
 ObjectConstructor<String> constructor = constructorConstructor.get(String.class);
 assertNotNull(constructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_Class_NoDefaultConstructor() {
 ObjectConstructor<StringBuilder> constructor = constructorConstructor.get(StringBuilder.class);
 }

 @Test
 public void testGet_Class_ReturnsDefaultImplementationConstructor() {
 ObjectConstructor<LinkedHashSet<String>> constructor = constructorConstructor.get(LinkedHashSet.class);
 }

 @Test
 public void testGet_Class_ReturnsUnsafeConstructor() {
 ObjectConstructor<SortedMap<String, String>> constructor = constructorConstructor.get(SortedMap.class);
 }
}