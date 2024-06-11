import org.junit.Assert;
import org.junit.Test;
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
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ConstructorConstructorTest {

 private ConstructorConstructor constructorConstructor;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 }

 @Test
 public void testGet_TypeToken_ReturnsObjectConstructor() {
 TypeToken<String> typeToken = new TypeToken<String>() {};
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
 Assert.assertNotNull(objectConstructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_InvalidType_ThrowsRuntimeException() {
 Type invalidType = new TypeToken<Object>() {};
 ObjectConstructor<Object> objectConstructor = constructorConstructor.get(new TypeToken<Object>() {});
 Assert.fail("Should have thrown RuntimeException");
 }

 @Test
 public void testGet_Class_ReturnsObjectConstructor() {
 Class<String> rawType = String.class;
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(new TypeToken<String>() {}.getType());
 Assert.assertNotNull(objectConstructor);
 }

 @Test
 public void testGet_InvalidClass_ReturnsNull() {
 Class<Integer> rawType = Integer.class;
 ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(new TypeToken<Integer>() {}.getType());
 Assert.assertNull(objectConstructor);
 }

 @Test
 public void testGet_HasInstanceCreator_ReturnsInstanceCreatorConstructor() {
 Type stringType = String.class;
 InstanceCreator<String> stringInstanceCreator = new InstanceCreator<String>() {
 @Override public String createInstance(Type type) {
 return "Test String";
 }
 };
 constructorConstructor.instanceCreators.put(stringType, stringInstanceCreator);
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(new TypeToken<String>() {}.getType());
 Assert.assertTrue(objectConstructor instanceof InstanceCreatorConstructor);
 }

 @Test
 public void testGet_HasDefaultConstructor_ReturnsDefaultConstructor() {
 Class<String> rawType = String.class;
 Constructor<String> stringConstructor = rawType.getDeclaredConstructor();
 constructorConstructor = new ConstructorConstructor(constructorConstructor.instanceCreators);
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(new TypeToken<String>() {}.getType());
 Assert.assertTrue(objectConstructor instanceof DefaultConstructor);
 }

 @Test
 public void testGet_HasDefaultImplementationConstructor_ReturnsDefaultImplementationConstructor() {
 Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();
 ObjectConstructor<Map<String, Integer>> objectConstructor = constructorConstructor.get(mapType);
 Assert.assertTrue(objectConstructor instanceof DefaultImplementationConstructor);
 }

 @Test
 public void testGet_HasUnsafeAllocator_ReturnsUnsafeAllocatorConstructor() {
 Type intType = Integer.TYPE;
 ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(intType);
 Assert.assertTrue(objectConstructor instanceof UnsafeAllocatorConstructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_NoConstructor_ThrowsRuntimeException() {
 Type unknownType = new TypeToken<UnknownType>() {}.getType();
 ObjectConstructor<UnknownType> objectConstructor = constructorConstructor.get(unknownType);
 Assert.fail("Should have thrown RuntimeException");
 }

 @Test
 public void testToString() {
 Map<Type, InstanceCreator<?>> instanceCreators = constructorConstructor.instanceCreators;
 String expected = "{"
 + "\"instanceCreators\":{"
 + "\"Class\":{"
 + "\"String\":{"
 + "\"class\":\"java.lang.String\","
 + "\"instanceCreator\":{"
 + "\"class\":\"com.google.gson.internal.ConstructorConstructor$InstanceCreatorImpl\","
 + "\"createInstance\":{\"invoke\":{\"class\":\"java.lang.reflect.Method\",\"name\":\"createInstance\",\"parameterTypes\":[],\"returnType\":\"java.lang.Object\"}},"
 + "}},"
 + "\"LinkedHashMap\":{"
 + "\"class\":\"java.util.LinkedHashMap\""
 + "}}"
 + "}";
 Assert.assertEquals(expected, constructorConstructor.toString());
 }

 @Test
 public void testCreateInstance_InstanceCreator_ReturnsCreatedInstance() {
 TypeToken<String> typeToken = new TypeToken<String>() {};
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
 String instance = objectConstructor.construct();
 Assert.assertEquals("Test String", instance);
 }

 @Test(expected = RuntimeException.class)
 public void testCreateInstance_InvalidType_ThrowsRuntimeException() {
 TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
 ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(new TypeToken<String>() {}.getType());
 objectConstructor.construct();
 Assert.fail("Should have thrown RuntimeException");
 }

 @Test(expected = RuntimeException.class)
 public void testCreateInstance_NoConstructor_ThrowsRuntimeException() {
 Type unknownType = new TypeToken<UnknownType>() {}.getType();
 ObjectConstructor<UnknownType> objectConstructor = constructorConstructor.get(unknownType);
 objectConstructor.construct();
 Assert.fail("Should have thrown RuntimeException");
 }
}

class InstanceCreatorConstructor implements ObjectConstructor<String> {
 @Override public String construct() {
 return "InstanceCreator created instance";
 }
}

class DefaultConstructor implements ObjectConstructor<String> {
 @Override public String construct() {
 return "Default constructor created instance";
 }
}

class DefaultImplementationConstructor implements ObjectConstructor<Map<String, Integer>> {
 @Override public Map<String, Integer> construct() {
 return new TreeMap<>();
 }
}

class UnsafeAllocatorConstructor implements ObjectConstructor<Integer> {
 private final UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

 @Override public Integer construct() {
 try {
 return (Integer) unsafeAllocator.newInstance(Integer.TYPE);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }
}

class ObjectConstructor<T> {
 public abstract T construct();
}