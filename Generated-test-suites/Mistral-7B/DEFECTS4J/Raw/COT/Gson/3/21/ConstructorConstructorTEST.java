import static org.junit.Assert.*;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.Test;

public class ConstructorConstructorTest {

 private Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
 private ConstructorConstructor constructorConstructor;

 @Test
 public void testGetConstructor() throws Exception {
 // Test getting a constructor for a simple type
 Type intType = new TypeToken<Integer>() {}.getType();
 ObjectConstructor<Integer> constructor = constructorConstructor.get(TypeToken.get(intType));
 Integer result = constructor.construct();
 assertEquals(0, result.hashCode());

 // Test getting a constructor for a collection type
 Type listType = new TypeToken<ArrayList<String>>() {}.getType();
 ObjectConstructor<ArrayList<String>> listConstructor = constructorConstructor.get(TypeToken.get(listType));
 ArrayList<String> listResult = listConstructor.construct();
 assertTrue(listResult.isEmpty());

 // Test getting a constructor for a map type
 Type mapType = new TypeToken<LinkedHashMap<String, Integer>>() {}.getType();
 ObjectConstructor<LinkedHashMap<String, Integer>> mapConstructor = constructorConstructor.get(TypeToken.get(mapType));
 LinkedHashMap<String, Integer> mapResult = mapConstructor.construct();
 assertTrue(mapResult.isEmpty());
 }

 @Test(expected = RuntimeException.class)
 public void testGetConstructorWithNoCreatorOrMatchingConstructor() throws Exception {
 Type unknownType = new TypeToken<UnknownType>() {}.getType();
 ObjectConstructor<UnknownType> constructor = constructorConstructor.get(TypeToken.get(unknownType));
 fail("Expected a RuntimeException");
 }

 @Test
 public void testGetConstructorWithInstanceCreator() throws Exception {
 InstanceCreator<String> instanceCreator = new InstanceCreator<String>() {
 @Override public String createInstance(Type type) {
 return "test";
 }
 };
 instanceCreators.put(String.class, instanceCreator);
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type stringType = new TypeToken<String>() {}.getType();
 ObjectConstructor<String> stringConstructor = constructorConstructor.get(TypeToken.get(stringType));
 String result = stringConstructor.construct();
 assertEquals("test", result);
 }

 @Test
 public void testGetConstructorWithRawTypeInstanceCreator() throws Exception {
 InstanceCreator<String> instanceCreator = new InstanceCreator<String>() {
 @Override public String createInstance(Type type) {
 return "test";
 }
 };
 instanceCreators.put(String.class, instanceCreator);
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type stringType = new TypeToken<Object>() {}.getType();
 ObjectConstructor<String> stringConstructor = constructorConstructor.get(TypeToken.get(stringType));
 String result = stringConstructor.construct();
 assertEquals("test", result);
 }

 @Test(expected = RuntimeException.class)
 public void testGetConstructorWithUnsupportedType() throws Exception {
 Type unsupportedType = new TypeToken<Object>() {}.getType();
 ObjectConstructor<Object> constructor = constructorConstructor.get(TypeToken.get(unsupportedType));
 fail("Expected a RuntimeException");
 }

 @Test(expected = RuntimeException.class)
 public void testGetConstructorWithUnsafeFailure() throws Exception {
 UnsafeAllocator unsafeAllocator = new UnsafeAllocator() {
 @Override public Object newInstance(Type type) throws InstantiationException, IllegalAccessException {
 throw new InstantiationException();
 }
 };
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 constructorConstructor.setUnsafeAllocator(unsafeAllocator);
 Type stringType = new TypeToken<String>() {}.getType();
 ObjectConstructor<String> stringConstructor = constructorConstructor.get(TypeToken.get(stringType));
 fail("Expected a RuntimeException");
 }
}