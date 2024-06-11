import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ConstructorConstructorTest {

 private Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
 private final ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

 @Test
 public void testGet_ValidTypeToken() {
 TypeToken<String> typeToken = new TypeToken<String>() {};
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
 assertNotNull(objectConstructor);
 }

 @Test
 public void testGet_NullTypeToken() {
 assertNull(constructorConstructor.get(null));
 }

 @Test
 public void testGet_InvalidTypeToken() {
 TypeToken<Object> typeToken = new TypeToken<Object>() {};
 ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);
 assertNull(objectConstructor);
 }

 @Test
 public void testGet_TypeWithInstanceCreator() {
 instanceCreators.put(String.class, new StringCreator());
 TypeToken<String> typeToken = new TypeToken<String>() {};
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
 assertNotNull(objectConstructor);
 }

 @Test
 public void testGet_TypeWithRawTypeMatchForInstanceCreator() {
 instanceCreators.put(ArrayList.class, new ArrayListCreator<>());
 TypeToken<List<Integer>> typeToken = new TypeToken<List<Integer>>() {};
 ObjectConstructor<List<Integer>> objectConstructor = constructorConstructor.get(typeToken);
 assertNotNull(objectConstructor);
 }

 @Test
 public void testGet_TypeWithDefaultConstructor() {
 Class<String> rawType = String.class;
 ObjectConstructor<String> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);
 assertNotNull(objectConstructor);
 }

 @Test
 public void testGet_TypeWithDefaultImplementationConstructor() {
 Type type = new TypeToken<SortedSet<String>>() {}.getType();
 Class<SortedSet<String>> rawType = (Class<SortedSet<String>>)(type.getComponentType().getRawType());
 ObjectConstructor<SortedSet<String>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
 assertNotNull(objectConstructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_TypeWithNoConstructor() {
 Class<Object> rawType = Object.class;
 ObjectConstructor<Object> objectConstructor = constructorConstructor.get(new TypeToken<Object>() {}.getType());
 fail("Should throw RuntimeException for type with no constructor");
 }

 @Test
 public void testGet_TypeWithPrivateConstructor() {
 Class<PrivateConstructorClass> rawType = PrivateConstructorClass.class;
 ObjectConstructor<PrivateConstructorClass> objectConstructor = constructorConstructor.get(new TypeToken<PrivateConstructorClass>() {}.getType());
 assertNotNull(objectConstructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_TypeWithIllegalAccessException() {
 Class<IllegalAccessConstructorClass> rawType = IllegalAccessConstructorClass.class;
 ObjectConstructor<IllegalAccessConstructorClass> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);
 fail("Should throw RuntimeException for type with IllegalAccessException");
 }

 @Test(expected = InstantiationException.class)
 public void testGet_TypeWithInstantiationException() {
 Class<InstantiationExceptionConstructorClass> rawType = InstantiationExceptionConstructorClass.class;
 ObjectConstructor<InstantiationExceptionConstructorClass> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);
 fail("Should throw InstantiationException for type with InstantiationException");
 }

 @Test(expected = InvocationTargetException.class)
 public void testGet_TypeWithInvocationTargetException() {
 Class<InvocationTargetExceptionConstructorClass> rawType = InvocationTargetExceptionConstructorClass.class;
 ObjectConstructor<InvocationTargetExceptionConstructorClass> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);
 fail("Should throw InvocationTargetException for type with InvocationTargetException");
 }

 @Test
 public void testNewUnsafeAllocator_ValidType() {
 Type type = new TypeToken<StringBuilder>() {}.getType();
 ObjectConstructor<StringBuilder> objectConstructor = constructorConstructor.newUnsafeAllocator(type, StringBuilder.class);
 assertNotNull(objectConstructor.construct());
 }

 @Test(expected = RuntimeException.class)
 public void testNewUnsafeAllocator_InvalidType() {
 Type type = new TypeToken<Object>() {}.getType();
 ObjectConstructor<Object> objectConstructor = constructorConstructor.newUnsafeAllocator(type, Object.class);
 fail("Should throw RuntimeException for invalid type in newUnsafeAllocator");
 }

 @Test
 public void testToString() {
 String expected = "{\n" +
         "  InstanceCreator{type=class java.lang.String, creator=class com.google.gson.internal.StringCreator},\n" +
         "  InstanceCreator{type=class java.util.ArrayList, creator=class com.google.gson.internal.ArrayListCreator}\n" +
         "}";
 String actual = constructorConstructor.toString();
 assertEquals(expected, actual);
 }
}

class StringCreator implements InstanceCreator<String> {
 @Override public String createInstance(Type type) {
 return "Hello, World!";
 }
}

class ArrayListCreator<T> implements InstanceCreator<ArrayList<T>> {
 @Override public ArrayList<T> createInstance(Type type) {
 return new ArrayList<T>();
 }
}

class PrivateConstructorClass {
 private PrivateConstructorClass() {}
}

class IllegalAccessConstructorClass {
 private IllegalAccessConstructorClass(int i) {}
}

class InstantiationExceptionConstructorClass {
 private InstantiationExceptionConstructorClass() throws InstantiationException {
 throw new InstantiationException();
 }
}

class InvocationTargetExceptionConstructorClass {
 private InvocationTargetExceptionConstructorClass(RuntimeException e) throws InvocationTargetException {
 throw new InvocationTargetException(e);
 }
}