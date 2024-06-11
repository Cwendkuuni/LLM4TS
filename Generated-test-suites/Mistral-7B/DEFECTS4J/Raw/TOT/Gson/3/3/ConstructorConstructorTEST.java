import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorConstructorTest {

 private static final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

 @Test
 public void testGetInstanceForPrimitiveType() {
 TypeIntType type = new TypeToken<Integer>() {}.getType();
 ObjectConstructor<Integer> constructor = new ConstructorConstructor(instanceCreators).get(type);
 assertEquals(0, constructor.construct());
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceForNonExistentType() {
 TypeNonExistentType type = new TypeToken<NonExistentType>() {}.getType();
 ObjectConstructor<NonExistentType> constructor = new ConstructorConstructor(instanceCreators).get(type);
 constructor.construct(); // this will throw a RuntimeException
 }

 @Test
 public void testGetInstanceForCustomTypeWithInstanceCreator() {
 TypeCustomType type = new TypeToken<CustomType>() {}.getType();
 CustomType customType = new CustomType();
 InstanceCreator<CustomType> instanceCreator = new InstanceCreator<CustomType>() {
 @Override public CustomType createInstance(Type type) {
 return customType;
 }
 };
 instanceCreators.put(type, instanceCreator);
 ObjectConstructor<CustomType> constructor = new ConstructorConstructor(instanceCreators).get(type);
 assertEquals(customType, constructor.construct());
 }

 @Test
 public void testGetInstanceForCustomTypeWithoutInstanceCreator() {
 TypeCustomType type = new TypeToken<CustomType>() {}.getType();
 ObjectConstructor<CustomType> constructor = new ConstructorConstructor(instanceCreators).get(type);
 CustomType customType = constructor.construct();
 assertEquals(customType.getDefaultValue(), customType);
 }

 @Test
 public void testGetInstanceForCollectionTypes() {
 TypeArrayListType type = new TypeToken<ArrayList<Integer>>() {}.getType();
 ObjectConstructor<ArrayList<Integer>> constructor = new ConstructorConstructor(instanceCreators).get(type);
 ArrayList<Integer> list = constructor.construct();
 assertEquals(0, list.size());

 TypeTreeSetType typeTreeSet = new TypeToken<TreeSet<String>>() {}.getType();
 ObjectConstructor<TreeSet<String>> constructorTreeSet = new ConstructorConstructor(instanceCreators).get(typeTreeSet);
 TreeSet<String> set = constructorTreeSet.construct();
 assertFalse(set.isEmpty());
 }

 @Test
 public void testGetInstanceForMapTypes() {
 TypeTreeMapType type = new TypeToken<TreeMap<String, Integer>>() {}.getType();
 ObjectConstructor<TreeMap<String, Integer>> constructor = new ConstructorConstructor(instanceCreators).get(type);
 TreeMap<String, Integer> map = constructor.construct();
 assertFalse(map.isEmpty());
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceForUnknownType() {
 TypeUnknownType type = new TypeToken<UnknownType>() {}.getType();
 ObjectConstructor<UnknownType> constructor = new ConstructorConstructor(instanceCreators).get(type);
 constructor.construct(); // this will throw a RuntimeException
 }

 @Test
 public void testGetInstanceForArrayTypes() {
 TypeIntArrayType type = new TypeToken<int[]>() {}.getType();
 ObjectConstructor<int[]> constructor = new ConstructorConstructor(instanceCreators).get(type);
 int[] array = constructor.construct();
 assertEquals(new int[0], array);
 }

 @Test
 public void testGetInstanceForParameterizedTypes() {
 TypeParameterizedType type = new TypeToken<ArrayList<String>>() {}.getType();
 ObjectConstructor<ArrayList<String>> constructor = new ConstructorConstructor(instanceCreators).get(type);
 ArrayList<String> list = constructor.construct();
 assertFalse(list.isEmpty());
 }

 @Test
 public void testGetInstanceForEnumSetTypes() {
 TypeEnumSetType type = new TypeToken<EnumSet<CustomEnum>>() {}.getType();
 ObjectConstructor<EnumSet<CustomEnum>> constructor = new ConstructorConstructor(instanceCreators).get(type);
 assertEquals(EnumSet.noneOf(CustomEnum.class), constructor.construct());
 }

 @Test
 public void testGetInstanceForSortedMapTypes() {
 TypeSortedMapType type = new TypeToken<TreeMap<String, Integer>>() {}.getType();
 ObjectConstructor<TreeMap<String, Integer>> constructor = new ConstructorConstructor(instanceCreators).get(type);
 TreeMap<String, Integer> map = constructor.construct();
 assertFalse(map.isEmpty());
 }
}