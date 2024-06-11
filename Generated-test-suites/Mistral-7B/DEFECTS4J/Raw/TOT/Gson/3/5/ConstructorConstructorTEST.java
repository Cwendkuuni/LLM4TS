import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConstructorConstructorTest {

 private Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

 @Test
 public void testGetInstanceOfArrayList() {
 Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
 ObjectConstructor<ArrayList<Integer>> constructor = constructorConstructor.get(new TypeToken<ArrayList<Integer>>() {}.getType());
 ArrayList<Integer> list = constructor.construct();
 assertNotNull(list);
 assertEquals(0, list.size());
 }

 @Test
 public void testGetInstanceOfTreeMap() {
 Type type = new TypeToken<TreeMap<String, Integer>>() {}.getType();
 ObjectConstructor<TreeMap<String, Integer>> constructor = constructorConstructor.get(new TypeToken<TreeMap<String, Integer>>() {}.getType());
 TreeMap<String, Integer> map = constructor.construct();
 assertNotNull(map);
 assertEquals(0, map.size());
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceOfNullType() {
 ObjectConstructor<Object> constructor = constructorConstructor.get(null);
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceOfUnknownType() {
 ObjectConstructor<String> constructor = constructorConstructor.get(new TypeToken<String>() {}.getType());
 }

 @Test
 public void testGetInstanceOfInstanceCreator() {
 Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
InstanceCreator<ArrayList<Integer>> instanceCreator = new ArrayListInstanceCreator();
 instanceCreators.put(type, instanceCreator);
 ObjectConstructor<ArrayList<Integer>> constructor = constructorConstructor.get(new TypeToken<ArrayList<Integer>>() {}.getType());
 assertEquals(instanceCreator, constructor.instanceCreator);
 }

 @Test
 public void testGetInstanceOfRawType() {
 Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
 ObjectConstructor<ArrayList<?>> constructor = constructorConstructor.get(type);
 assertEquals(ArrayList.class, constructor.getRawType());
 }

 @Test
 public void testGetInstanceOfDefaultConstructor() {
 Class<MyClass> rawType = MyClass.class;
 ObjectConstructor<MyClass> constructor = constructorConstructor.newDefaultConstructor(rawType);
 MyClass obj = constructor.construct();
 assertNotNull(obj);
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceOfNotFoundDefaultConstructor() {
 Class<MyClassWithNoDefaultConstructor> rawType = MyClassWithNoDefaultConstructor.class;
 ObjectConstructor<MyClassWithNoDefaultConstructor> constructor = constructorConstructor.newDefaultConstructor(rawType);
 }

 @Test
 public void testGetInstanceOfDefaultImplementationConstructor() {
 Type type = new TypeToken<ArrayList<String>>() {}.getType();
 ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.newDefaultImplementationConstructor(type, ArrayList.class);
 ArrayList<String> list = constructor.construct();
 assertEquals(new ArrayList<String>(), list);
 }

 @Test(expected = JsonIOException.class)
 public void testGetInstanceOfInvalidEnumSetType() {
 Type type = new TypeToken<EnumSet<MyEnum>>() {}.getType();
 ObjectConstructor<EnumSet<MyEnum>> constructor = constructorConstructor.get(type);
 }
}