import org.junit.Assert;
import org.junit.Test;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.UnsafeAllocator;
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

  @Test
  public void testGetInstanceWithValidTypeToken() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<String> typeTokenString = new TypeToken<String>() {};
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(typeTokenString);

    String instance = stringConstructor.construct();
    Assert.assertNotNull(instance);
    Assert.assertEquals("", instance);
  }

  @Test
  public void testGetInstanceWithValidClass() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Class<String> stringClass = String.class;
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(new TypeToken<String>() {}.getType());

    String instance = stringConstructor.construct();
    Assert.assertNotNull(instance);
    Assert.assertEquals("", instance);
  }

  @Test
  public void testGetInstanceWithInstanceCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<String> stringInstanceCreator = s -> new String(s);
    instanceCreators.put(String.class, stringInstanceCreator);
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<String> stringConstructor = constructorConstructor.get(new TypeToken<String>() {}.getType());

    String instance = stringConstructor.construct();
    Assert.assertNotNull(instance);
    Assert.assertEquals("", instance);
  }

  @Test
  public void testGetInstanceWithDefaultConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Class<ArrayList<String>> arrayListClass = ArrayList.class;
    ObjectConstructor<ArrayList<String>> arrayListConstructor = constructorConstructor.get(new TypeToken<ArrayList<String>>() {}.getType());

    ArrayList<String> instance = arrayListConstructor.construct();
    Assert.assertNotNull(instance);
    Assert.assertTrue(instance.isEmpty());
  }

  @Test
  public void testGetInstanceWithDefaultImplementationConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Class<SortedMap<String, Object>> sortedMapClass = SortedMap.class;
    Type keyType = new TypeToken<String>() {}.getType();
    Type valueType = Object.class;
    Type parameterizedType = new TypeToken<SortedMap<String, Object>>() {}.getType();

    ObjectConstructor<SortedMap<String, Object>> sortedMapConstructor = constructorConstructor.get(parameterizedType);

    SortedMap<String, Object> instance = sortedMapConstructor.construct();
    Assert.assertNotNull(instance);
    Assert.assertTrue(instance instanceof TreeMap);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceWithNoConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Class<Number> numberClass = Number.class;
    ObjectConstructor<Number> numberConstructor = constructorConstructor.get(numberClass.getType());

    numberConstructor.construct();
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceWithUnreachableConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Class<String> stringClass = String.class;
    Constructor<String> unreachableConstructor = stringClass.getDeclaredConstructor("foo");
    unreachableConstructor.setAccessible(true);

    ObjectConstructor<String> stringConstructor = constructorConstructor.get(new TypeToken<String>() {}.getType());

    stringConstructor.construct();
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceWithInvocationTargetException() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Class<String> stringClass = String.class;
    Constructor<String> constructor = stringClass.getDeclaredConstructor("foo");
    constructor.setAccessible(true);

    ObjectConstructor<String> stringConstructor = constructorConstructor.get(new TypeToken<String>() {}.getType());

    try {
      stringConstructor.construct();
    } catch (InvocationTargetException e) {
      throw e.getTargetException();
    }
  }

  @Test(expected = IllegalAccessException.class)
  public void testGetInstanceWithIllegalAccessException() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Class<String> stringClass = String.class;
    Constructor<String> constructor = stringClass.getDeclaredConstructor("foo");
    constructor.setAccessible(false);

    ObjectConstructor<String> stringConstructor = constructorConstructor.get(new TypeToken<String>() {}.getType());

    stringConstructor.construct();
  }

  @Test(expected = AssertionError.class)
  public void testGetInstanceWithAssertionError() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Class<ArrayList<String>> arrayListClass = ArrayList.class;
    Constructor<ArrayList<String>> constructor = arrayListClass.getDeclaredConstructor("foo");
    constructor.setAccessible(true);

    ObjectConstructor<ArrayList<String>> arrayListConstructor = constructorConstructor.get(new TypeToken<ArrayList<String>>() {}.getType());

    arrayListConstructor.construct();
  }

  @Test
  public void testToString() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    String toString = constructorConstructor.toString();
    Assert.assertNotNull(toString);
  }
}