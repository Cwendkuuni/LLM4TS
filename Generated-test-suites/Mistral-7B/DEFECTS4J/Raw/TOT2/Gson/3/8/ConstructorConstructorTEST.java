import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConstructorConstructorTest {

 private Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();

 @Test
 public void testGetInstanceForSimpleClass() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 ObjectConstructor<String> stringConstructor = constructorConstructor.get(stringTypeToken);
 String result = stringConstructor.construct();
 Assert.assertNotNull(result);
 Assert.assertEquals("", result);
 }

 @Test
 public void testGetInstanceForCollectionType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeToken<ArrayList<String>> listTypeToken = new TypeToken<ArrayList<String>>() {};
 ObjectConstructor<ArrayList<String>> listConstructor = constructorConstructor.get(listTypeToken);
 ArrayList<String> result = listConstructor.construct();
 Assert.assertNotNull(result);
 Assert.assertTrue(result.isEmpty());
 }

 @Test
 public void testGetInstanceForMapType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeToken<LinkedHashMap<String, Integer>> mapTypeToken = new TypeToken<LinkedHashMap<String, Integer>>() {};
 ObjectConstructor<LinkedHashMap<String, Integer>> mapConstructor = constructorConstructor.get(mapTypeToken);
 LinkedHashMap<String, Integer> result = mapConstructor.construct();
 Assert.assertNotNull(result);
 Assert.assertTrue(result.isEmpty());
 }

 @Test
 public void testGetInstanceForEnumSetType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 TypeToken<EnumSet<Colors>> enumSetTypeToken = new TypeToken<EnumSet<Colors>>() {};
 ObjectConstructor<EnumSet<Colors>> enumSetConstructor = constructorConstructor.get(enumSetTypeToken);
 EnumSet<Colors> result = enumSetConstructor.construct();
 Assert.assertNotNull(result);
 Assert.assertTrue(result.isEmpty());
 }

 @Test
 public void testGetInstanceForCustomClassWithDefaultConstructor() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type customClassType = MyCustomClass.class.getGenericSuperclass();
 TypeToken<MyCustomClass> customClassTypeToken = new TypeToken<MyCustomClass>() {};
 ObjectConstructor<MyCustomClass> customClassConstructor = constructorConstructor.get(customClassTypeToken);
 MyCustomClass result = customClassConstructor.construct();
 Assert.assertNotNull(result);
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceForCustomClassWithNoDefaultConstructor() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type customClassType = NoDefaultConstructorClass.class.getGenericSuperclass();
 TypeToken<NoDefaultConstructorClass> customClassTypeToken = new TypeToken<NoDefaultConstructorClass>() {};
 ObjectConstructor<NoDefaultConstructorClass> customClassConstructor = constructorConstructor.get(customClassTypeToken);
 customClassConstructor.construct(); // should throw a RuntimeException
 }

 @Test
 public void testGetInstanceForCustomClassWithInstanceCreator() {
 InstanceCreator<MyCustomClass> instanceCreator = mock(InstanceCreator.class);
 when(instanceCreator.createInstance(MyCustomClass.class)).thenReturn(new MyCustomClass());
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(Map.of(MyCustomClass.class, instanceCreator));
 TypeToken<MyCustomClass> customClassTypeToken = new TypeToken<MyCustomClass>() {};
 ObjectConstructor<MyCustomClass> customClassConstructor = constructorConstructor.get(customClassTypeToken);
 MyCustomClass result = customClassConstructor.construct();
 Assert.assertNotNull(result);
 }

 @Test
 public void testGetInstanceForRawType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type rawType = String[].class;
 TypeToken<String[]> rawTypeToken = new TypeToken<String[]>() {};
 ObjectConstructor<String[]> rawTypeConstructor = constructorConstructor.get(rawTypeToken);
 String[] result = rawTypeConstructor.construct();
 Assert.assertNotNull(result);
 Assert.assertArrayEquals(new String[0], result);
 }

 @Test
 public void testGetInstanceForParameterizedType() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 Type parameterizedType = new TypeToken<ArrayList<ArrayList<String>>>() {}.getType();
 ObjectConstructor<ArrayList<ArrayList<String>>> parameterizedTypeConstructor = constructorConstructor.get((Type) parameterizedType);
 ArrayList<ArrayList<String>> result = parameterizedTypeConstructor.construct();
 Assert.assertNotNull(result);
 Assert.assertTrue(result.isEmpty());
 }

 @Test
 public void testToString() {
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 String result = constructorConstructor.toString();
 Assert.assertTrue(result.contains("instanceCreators"));
 }
}