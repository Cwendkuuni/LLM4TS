import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
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

import org.junit.Test;

public class ConstructorConstructorTest {

 private Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

 @Test
 public void testGetConstructor() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeStringType typeStringType = new TypeStringType();
 ObjectConstructor<String> constructor = constructorConstructor.get(new TypeToken<String>() {});
 assertNotNull(constructor);
 assertEquals("constructor for String returned", constructor.construct(), "test string");
 }

 @Test
 public void testGetInstanceCreatorForType() {
 Class<String> stringClass = String.class;
 String stringInstance = "test string";
 InstanceCreator<String> stringCreator = new StringCreator();
 instanceCreators.put(new TypeToken<String>() {}, stringCreator);
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<String> constructor = constructorConstructor.get(new TypeToken<String>() {});
 assertNotNull(constructor);
 assertEquals("constructor for String using instance creator returned", constructor.construct(), stringInstance);
 }

 @Test
 public void testGetInstanceCreatorForRawType() {
 Class<String> stringClass = String.class;
 String stringInstance = "test string";
 InstanceCreator<String> stringCreator = new StringCreator();
 instanceCreators.put(stringClass, stringCreator);
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<String> constructor = constructorConstructor.get(new TypeToken<String>() {});
 assertNotNull(constructor);
 assertEquals("constructor for String using raw type instance creator returned", constructor.construct(), stringInstance);
 }

 @Test(expected = RuntimeException.class)
 public void testFailToGetDefaultConstructor() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
 ObjectConstructor<String> constructor = constructorConstructor.get(new TypeToken<String>() {});
 assertNull(constructor);
 }

 @Test(expected = RuntimeException.class)
 public void testFailToGetDefaultImplementationConstructor() {
 Class<List<String>> listClass = new ArrayList<String>().getClass();
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
 ObjectConstructor<List<String>> constructor = constructorConstructor.get(new TypeToken<List<String>>() {});
 assertNull(constructor);
 }

 @Test
 public void testNewDefaultConstructorForCollection() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
 ObjectConstructor<Collection<String>> constructor = constructorConstructor.get(new TypeToken<Collection<String>>() {});
 assertNotNull(constructor);
 assertEquals("constructor for Collection returned", constructor.construct(), new ArrayList<String>());
 }

 @Test
 public void testNewDefaultImplementationConstructorForMap() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
 ObjectConstructor<Map<String, Integer>> constructor = constructorConstructor.get(new TypeToken<Map<String, Integer>>() {});
 assertNotNull(constructor);
 assertEquals("constructor for Map returned", constructor.construct(), new LinkedHashMap<String, Integer>());
 }

 @Test
 public void testNewUnsafeAllocatorForCustomClass() {
 Class<CustomClass> customClass = CustomClass.class;
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
 ObjectConstructor<CustomClass> constructor = constructorConstructor.get(new TypeToken<CustomClass>() {});
 assertNotNull(constructor);
 assertNotNull(constructor.construct());
 }

 @Test(expected = RuntimeException.class)
 public void testNewUnsafeAllocatorForUnsupportedClass() {
 Class<Object> objectClass = Object.class;
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
 ObjectConstructor<Object> constructor = constructorConstructor.get(new TypeToken<Object>() {});
 assertNull(constructor);
 }
}

class CustomClass {
 // empty class
}

class StringCreator implements InstanceCreator<String> {
 @Override public String createInstance(Type type) {
 return "test string";
 }
}

class TypeStringType extends TypeToken<String> {
 // empty class
}