import org.junit.Assert;
import org.junit.Test;
import com.google.gson.internal.ConstructorConstructor;
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
 public void setup() {
 InstanceCreator<Object> instanceCreator = new InstanceCreator<Object>() {
 @Override
 public Object createInstance(Type type) {
 return new Object();
 }
 };
 Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
 instanceCreators.put(TypeToken.get(String.class), instanceCreator);
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 }

 @Test
 public void testGet_TypeToken_ReturnsObjectConstructor() {
 TypeToken<String> typeToken = TypeToken.get(String.class);
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
 Assert.assertNotNull(objectConstructor);
 }

 @Test(expected = NullPointerException.class)
 public void testGet_NullTypeToken_ThrowsNullPointerException() {
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(null);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_UnknownType_ThrowsRuntimeException() {
 Type unknownType = new TypeToken<Object>() {}.getType();
 ObjectConstructor<Object> objectConstructor = constructorConstructor.get(TypeToken.get(unknownType));
 Assert.assertNull(objectConstructor);
 }

 @Test
 public void testGet_TypeToken_RawTypeMatch_ReturnsObjectConstructor() {
 TypeToken<String> typeToken = TypeToken.get(String.class);
 ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken.getType());
 Assert.assertNotNull(objectConstructor);
 }

 @Test
 public void testGet_TypeToken_NoDefaultConstructor_ReturnsNull() {
 Class<Object> clazz = Object.class;
 TypeToken<Object> typeToken = TypeToken.get(clazz);
 ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);
 Assert.assertNull(objectConstructor);
 }

 @Test
 public void testGet_TypeToken_DefaultConstructor_ReturnsDefaultConstructor() {
 Class<String> clazz = String.class;
 TypeToken<String> typeToken = TypeToken.get(clazz);
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
 Assert.assertNotNull(objectConstructor);
 }

 @Test
 public void testGet_TypeToken_DefaultImplementation_ReturnsDefaultImplementationConstructor() {
 Type type = new TypeToken<ArrayList<Object>>() {}.getType();
 ObjectConstructor<ArrayList<Object>> objectConstructor = constructorConstructor.get(TypeToken.get(type));
 Assert.assertNotNull(objectConstructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_TypeToken_UnsupportedType_ThrowsRuntimeException() {
 Type unsupportedType = new TypeToken<Object>() {}.getType();
 ObjectConstructor<Object> objectConstructor = constructorConstructor.get(unsupportedType);
 Assert.assertNull(objectConstructor);
 }

 @Test
 public void testGet_TypeToken_HasInstanceCreator_ReturnsInstanceCreator() {
 TypeToken<TreeSet<String>> typeToken = new TypeToken<TreeSet<String>>() {};
 InstanceCreator<TreeSet<String>> instanceCreator = new InstanceCreator<TreeSet<String>>() {
 @Override
 public TreeSet<String> createInstance(Type type) {
 return new TreeSet<String>();
 }
 };
 Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
 instanceCreators.put(typeToken.getType(), instanceCreator);
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<TreeSet<String>> objectConstructor = constructorConstructor.get(typeToken);
 Assert.assertNotNull(objectConstructor);
 Assert.assertEquals(instanceCreator, objectConstructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_TypeToken_UnregisteredInstanceCreator_ThrowsRuntimeException() {
 TypeToken<ArrayList<Object>> typeToken = TypeToken.get(ArrayList.class);
 ObjectConstructor<ArrayList<Object>> objectConstructor = constructorConstructor.get(typeToken);
 Assert.assertNull(objectConstructor);
 }

 @Test
 public void testToString() {
 Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 String toString = constructorConstructor.toString();
 Assert.assertTrue(toString.contains("instanceCreators="));
 }
}