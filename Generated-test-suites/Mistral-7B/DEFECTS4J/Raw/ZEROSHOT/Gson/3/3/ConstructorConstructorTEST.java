import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import com.google.gson.InstanceCreator;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

  @Test
  public void testConstructorConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<String> stringInstanceCreator = new StringInstanceCreator();
    instanceCreators.put(String.class, stringInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    // Test with String type
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(stringTypeToken);
    assertNotNull(stringConstructor);
    String stringInstance = stringConstructor.construct();
    assertNull(stringInstance);

    // Test with ArrayList type
    TypeToken<ArrayList<Integer>> listTypeToken = new TypeToken<ArrayList<Integer>>() {};
    ObjectConstructor<ArrayList<Integer>> listConstructor = constructorConstructor.get(listTypeToken);
    assertNotNull(listConstructor);
    ArrayList<Integer> listInstance = listConstructor.construct();
    assertNotNull(listInstance);
    assertNull(listInstance.size());

    // Test with LinkedHashSet type
    TypeToken<LinkedHashSet<Long>> setTypeToken = new TypeToken<LinkedHashSet<Long>>() {};
    ObjectConstructor<LinkedHashSet<Long>> setConstructor = constructorConstructor.get(setTypeToken);
    assertNotNull(setConstructor);
    LinkedHashSet<Long> setInstance = setConstructor.construct();
    assertNotNull(setInstance);
    assertFalse(setInstance.isEmpty());

    // Test with EnumSet type
    Type enumType = EnumSet.class.getParameterizedType(EnumSet.OfString.class);
    TypeToken<EnumSet<String>> enumSetTypeToken = new TypeToken<EnumSet<String>>() {
      @Override public Type getType() {
        return enumType;
      }
    }.where(new TypeToken<String>() {});
    ObjectConstructor<EnumSet<String>> enumSetConstructor = constructorConstructor.get(enumSetTypeToken);
    assertNotNull(enumSetConstructor);
    EnumSet<String> enumSetInstance = enumSetConstructor.construct();
    assertNotNull(enumSetInstance);
    assertTrue(enumSetInstance.isEmpty());

    // Test with TreeMap type
    TypeToken<TreeMap<Integer, Float>> mapTypeToken = new TypeToken<TreeMap<Integer, Float>>() {};
    ObjectConstructor<TreeMap<Integer, Float>> mapConstructor = constructorConstructor.get(mapTypeToken);
    assertNotNull(mapConstructor);
    TreeMap<Integer, Float> mapInstance = mapConstructor.construct();
    assertNotNull(mapInstance);
    assertTrue(mapInstance.isEmpty());
  }

  private static class StringInstanceCreator implements InstanceCreator<String> {
    @Override public String createInstance(Type type) {
      return "";
    }
  }

  @Test(expected = RuntimeException.class)
  public void testConstructorNotFound() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<Integer> intTypeToken = new TypeToken<Integer>() {};
    ObjectConstructor<Integer> intConstructor = constructorConstructor.get(intTypeToken);
    intConstructor.construct();
  }

  @Test(expected = RuntimeException.class)
  public void testUnsupportedClass() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    Type unsupportedType = Object.class;
    ObjectConstructor<Object> unsupportedConstructor = constructorConstructor.get(new TypeToken<Object>() {}.where(new TypeToken<Object>() {}.where(new TypeToken<Object>() {}.where(new TypeToken<Object>() {}))));
    unsupportedConstructor.construct();
  }

  @Test(expected = RuntimeException.class)
  public void testUnsupportedConstructor() throws NoSuchMethodException, InstantiationException,
      InvocationTargetException, IllegalAccessException {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    Class<String> rawType = String.class;
    Class<String> type = String.class;
    Constructor<String> constructor = rawType.getDeclaredConstructor(String.class);
    constructor.setAccessible(true);
    ObjectConstructor<String> stringConstructor =
        new ObjectConstructor<String>() {
          @Override public String construct(String arg) {
            try {
              return constructor.newInstance(arg);
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }
        };
    constructorConstructor.instanceCreators.put(type, stringConstructor);
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> testedStringConstructor = constructorConstructor.get(stringTypeToken);
    testedStringConstructor.construct("test");
  }
}