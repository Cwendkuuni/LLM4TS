import org.junit.Test;
import org.junit.Assert.*;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
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

public class ConstructorConstructorTest {

 private ConstructorConstructor constructorConstructor;

 @Before
 public void setUp() {
 Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 }

 @Test
 public void testGet_TypeToken_ReturnsObjectConstructor() {
 TypeToken<String> typeToken = new TypeToken<String>() {};
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
 Assert.assertNotNull(objectConstructor);
 }

 @Test(expected = NullPointerException.class)
 public void testGet_NullTypeToken_ThrowsException() {
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(null);
 }

 @Test
 public void testGet_TypeToken_OfClass_ReturnsInstanceCreatorIfAvailable() {
 TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
 InstanceCreator<ArrayList<String>> instanceCreator = new ArrayListInstanceCreator<>();
 constructorCreator.instanceCreators.put(typeToken.getType(), instanceCreator);
 ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.get(typeToken);
 Assert.assertEquals(instanceCreator, objectConstructor);
 }

 @Test
 public void testGet_TypeToken_OfRawType_ReturnsInstanceCreatorIfAvailable() {
 TypeToken<Collection<String>> typeToken = new TypeToken<Collection<String>>() {};
 InstanceCreator<Collection<String>> instanceCreator = new CollectionInstanceCreator<>();
 constructorCreator.instanceCreators.put(Collection.class, instanceCreator);
 ObjectConstructor<Collection<String>> objectConstructor = constructorConstructor.get(typeToken);
 Assert.assertEquals(instanceCreator, objectConstructor);
 }

 @Test
 public void testGet_TypeToken_OfUnknownType_ReturnsDefaultConstructor() {
 TypeToken<MyUnknownType> typeToken = new TypeToken<MyUnknownType>() {};
 ObjectConstructor<MyUnknownType> objectConstructor = constructorConstructor.get(typeToken);
 Assert.assertNotNull(objectConstructor);
 }

 @Test
 public void testGet_TypeToken_OfUnknownType_WithDefaultConstructor() {
 TypeToken<MyUnknownType> typeToken = new TypeToken<MyUnknownType>() {};
 ObjectConstructor<MyUnknownType> objectConstructor = constructorConstructor.newDefaultConstructor(
 MyUnknownType.class);
 Assert.assertNotNull(objectConstructor);
 }

 @Test
 public void testGet_TypeToken_OfUnknownType_WithDefaultImplementation() {
 TypeToken<SortedMap<String, Integer>> typeToken = new TypeToken<SortedMap<String, Integer>>() {};
 ObjectConstructor<SortedMap<String, Integer>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(
 typeToken.getType(), MySortedMap.class);
 Assert.assertNotNull(objectConstructor);
 }

 @Test
 public void testGet_TypeToken_OfUnknownType_WithUnsafeAllocator() {
 TypeToken<MyUnknownType> typeToken = new TypeToken<MyUnknownType>() {};
 ObjectConstructor<MyUnknownType> objectConstructor = constructorConstructor.newUnsafeAllocator(
 typeToken.getType(), MyUnknownType.class);
 Assert.assertNotNull(objectConstructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_TypeToken_OfUnknownType_WithNoCreatorOrConstructor_ThrowsException() {
 TypeToken<MyUnknownType> typeToken = new TypeToken<MyUnknownType>() {};
 ObjectConstructor<MyUnknownType> objectConstructor = constructorConstructor.get(typeToken);
 }

 @Test
 public void testToString() {
 Map<Type, InstanceCreator<?>> instanceCreators = constructorConstructor.instanceCreators;
 String result = constructorConstructor.toString();
 String expected = instanceCreators.toString();
 assertEquals(expected, result);
 }

 class MyUnknownType {

 }

 class MySortedMap extends TreeMap<String, Integer> {

 }

 class ArrayListInstanceCreator implements InstanceCreator<ArrayList<String>> {
 @Override public ArrayList<String> createInstance(Type type) {
 return new ArrayList<String>();
 }
 }

 class CollectionInstanceCreator implements InstanceCreator<Collection<String>> {
 @Override public Collection<String> createInstance(Type type) {
 return new LinkedHashSet<String>();
 }
 }
}