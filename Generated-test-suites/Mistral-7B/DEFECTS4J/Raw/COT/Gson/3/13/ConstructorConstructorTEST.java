import static org.junit.Assert.*;
import com.google.gson.internal.ConstructorConstructor;
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

public class ConstructorConstructorTest {

 private ConstructorConstructor constructorConstructor;

 @Before
 public void setup() {
 Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 }

 @Test
 public void testGetMethod_ValidTypeToken_ReturnsInstanceConstructor() {
 TypeToken<String> typeToken = new TypeToken<String>() {};
 ObjectConstructor<String> constructor = constructorConstructor.get(typeToken);
 assertNotNull(constructor);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMethod_NullTypeToken_ThrowsNullPointerException() {
 constructorConstructor.get(null);
 }

 @Test
 public void testGetMethod_TypeWithoutInstanceCreator_ReturnsDefaultConstructor() {
 Type type = new TypeToken<Integer>() {}.getType();
 ObjectConstructor<Integer> constructor = constructorConstructor.get(new TypeToken<Integer>() {}.getType());
 assertNotNull(constructor);
 assertInstanceOf(DefaultConstructor.class, constructor);
 }

 @Test
 public void testGetMethod_ClassType_ReturnsInstanceCreatorIfAvailable() {
 Class<String> rawType = String.class;
 InstanceCreator<String> instanceCreator = new StringCreator();
 constructorConstructor.instanceCreators.put(TypeToken.get(rawType), instanceCreator);

 ObjectConstructor<String> constructor = constructorConstructor.get(new TypeToken<String>() {}.getType());
 assertNotNull(constructor);
 assertInstanceOf(InstanceCreatorConstructor.class, constructor);
 }

 @Test
 public void testGetMethod_ClassTypeWithoutInstanceCreator_ReturnsDefaultConstructor() {
 Class<String> rawType = String.class;
 ObjectConstructor<String> constructor = constructorConstructor.get(new TypeToken<String>() {}.getType());
 assertNotNull(constructor);
 assertInstanceOf(DefaultConstructor.class, constructor);
 }

 @Test
 public void testGetMethod_UnknownType_ReturnsNull() {
 ObjectConstructor<Object> constructor = constructorConstructor.get(new TypeToken<Object>() {}.getType());
 assertNull(constructor);
 }

 @Test
 public void testNewDefaultConstructor_ValidClassType_ReturnsDefaultConstructor() {
 Class<String> rawType = String.class;
 ObjectConstructor<String> constructor = constructorConstructor.newDefaultConstructor(rawType);
 assertNotNull(constructor);
 assertInstanceOf(DefaultConstructor.class, constructor);
 }

 @Test(expected = NoSuchMethodException.class)
 public void testNewDefaultConstructor_InvalidClassType_ThrowsNoSuchMethodException() {
 Class<Object> rawType = Object.class;
 constructorConstructor.newDefaultConstructor(rawType);
 }

 @Test
 public void testNewDefaultImplementationConstructor_MapType_ReturnsLinkedTreeMapConstructor() {
 Type type = new TypeToken<Map<String, String>>() {}.getType();
 ObjectConstructor<Map<String, String>> constructor = constructorConstructor.newDefaultImplementationConstructor(type, Map.class);
 assertNotNull(constructor);
 assertInstanceOf(LinkedTreeMapConstructor.class, constructor);
 }

 @Test
 public void testNewDefaultImplementationConstructor_QueueType_ReturnsLinkedListConstructor() {
 Type type = new TypeToken<Queue<Integer>>() {}.getType();
 ObjectConstructor<Queue<Integer>> constructor = constructorConstructor.newDefaultImplementationConstructor(type, Queue.class);
 assertNotNull(constructor);
 assertInstanceOf(LinkedListConstructor.class, constructor);
 }

 @Test
 public void testNewDefaultImplementationConstructor_InvalidType_ReturnsNull() {
 Type type = new TypeToken<Object>() {}.getType();
 ObjectConstructor<Object> constructor = constructorConstructor.newDefaultImplementationConstructor(type, Object.class);
 assertNull(constructor);
 }

 @Test(expected = JsonIOException.class)
 public void testNewDefaultImplementationConstructor_InvalidEnumSetType_ThrowsJsonIOException() {
 Type type = new TypeToken<EnumSet<Object>>() {}.getType();
 ObjectConstructor<EnumSet<Object>> constructor = constructorConstructor.newDefaultImplementationConstructor(type, EnumSet.class);
 fail("Expected JsonIOException");
 }

 @Test
 public void testNewUnsafeAllocator_ValidType_ReturnsUnsafeAllocatorConstructor() {
 Type type = new TypeToken<String>() {}.getType();
 ObjectConstructor<String> constructor = constructorConstructor.newUnsafeAllocator(type, String.class);
 assertNotNull(constructor);
 assertInstanceOf(UnsafeAllocatorConstructor.class, constructor);
 }

 @Test(expected = RuntimeException.class)
 public void testNewUnsafeAllocator_InvalidType_ThrowsRuntimeException() {
 Type type = new TypeToken<Object>() {}.getType();
 ObjectConstructor<Object> constructor = constructorConstructor.newUnsafeAllocator(type, Object.class);
 fail("Expected RuntimeException");
 }

 @Test
 public void testToString_ReturnsCorrectStringRepresentation() {
 assertEquals(
     "Map<String, String>->LinkedTreeMap<String, String>,\n" +
     "Queue<Integer>->LinkedList<Integer>,\n" +
     "String->InstanceCreator<String>,\n" +
     "EnumSet<Object>->null,\n" +
     "Object->null,\n" +
     "default constructor for Object->null,\n" +
     "default implementation constructor for Object->null,\n" +
     "unsafe allocator for Object->null",
     constructorConstructor.toString());
 }
}

class DefaultConstructor<T> implements ObjectConstructor<T> {
 @Override public T construct() {
 throw new UnsupportedOperationException();
 }
}

class InstanceCreatorConstructor<T> implements ObjectConstructor<T> {
 private final InstanceCreator<T> instanceCreator;

 InstanceCreatorConstructor(InstanceCreator<T> instanceCreator) {
 this.instanceCreator = instanceCreator;
 }

 @Override public T construct() {
 return instanceCreator.createInstance(TypeToken.get(instanceCreator.getClass().getGenericInterfaces()[0]).getType());
 }
}

class LinkedTreeMapConstructor<T> implements ObjectConstructor<T> {
 @Override public T construct() {
 return new LinkedTreeMap<Object, Object>();
 }
}

class LinkedListConstructor<T> implements ObjectConstructor<T> {
 @Override public T construct() {
 return new LinkedList<Object>();
 }
}

class UnsafeAllocatorConstructor<T> implements ObjectConstructor<T> {
 private final UnsafeAllocator unsafeAllocator;

 UnsafeAllocatorConstructor(UnsafeAllocator unsafeAllocator) {
 this.unsafeAllocator = unsafeAllocator;
 }

 @Override public T construct() {
 try {
 return (T) unsafeAllocator.newInstance(TypeToken.get(getClass()).getType());
 } catch (Exception e) {
 throw new RuntimeException("Unable to invoke constructor for " + getClass().getName(), e);
 }
 }
}