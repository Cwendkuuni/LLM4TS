import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testConstructorConstructorWithEmptyMap() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(typeToken);

    assertNotNull(constructor);
    assertNotNull(constructor.construct());
  }

  @Test
  public void testConstructorConstructorWithNullMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(typeToken);

    assertNull(constructor);
  }

  @Test
  public void testConstructorConstructorWithValidInstanceCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<ArrayList<String>> instanceCreator = new InstanceCreator<ArrayList<String>>() {
      @Override public ArrayList<String> createInstance(Type type) {
        return new ArrayList<String>();
      }
    };
    instanceCreators.put(new TypeToken<ArrayList<String>>() {}, instanceCreator);
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(typeToken);

    assertNotNull(constructor);
    assertNotNull(constructor.construct());
  }

  @Test(expected = RuntimeException.class)
  public void testConstructorConstructorWithNullTypeToken() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<Object> constructor = constructorConstructor.get(null);

    constructor.construct();
  }

  @Test(expected = RuntimeException.class)
  public void testConstructorConstructorWithInvalidTypeToken() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<String[][][]> typeToken = new TypeToken<String[][][]>() {};
    ObjectConstructor<String[][][]> constructor = constructorConstructor.get(typeToken);

    constructor.construct();
  }

  @Test(expected = RuntimeException.class)
  public void testConstructorConstructorWithNoDefaultConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    Class<?> clazz = new Class<MyClass>() {
      private static final long serialVersionUID = 1L;

      static {
        // no constructor
      }
    }.getDeclaredClasses()[0];

    instanceCreators.put(new TypeToken<MyClass>() {}, new InstanceCreator<MyClass>() {
      @Override public MyClass createInstance(Type type) {
        return null;
      }
    });

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<MyClass> typeToken = new TypeToken<MyClass>() {};
    ObjectConstructor<MyClass> constructor = constructorConstructor.get(typeToken);

    constructor.construct();
  }

  @Test
  public void testConstructorConstructorWithDefaultConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<String[][]> typeToken = new TypeToken<String[][]>() {};
    ObjectConstructor<String[][]> constructor = constructorConstructor.get(typeToken);

    assertNotNull(constructor);
    assertNotNull(constructor.construct());
  }

  @Test
  public void testConstructorConstructorWithDefaultImplementationConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<SortedMap<String, Integer>> typeToken = new TypeToken<SortedMap<String, Integer>>() {};
    ObjectConstructor<SortedMap<String, Integer>> constructor = constructorConstructor.get(typeToken);

    assertNotNull(constructor);
    assertNotNull(constructor.construct());
  }

  @Test
  public void testConstructorConstructorWithUnsafeAllocator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<SortedMap<String, Integer>> typeToken = new TypeToken<SortedMap<String, Integer>>() {};
    ObjectConstructor<SortedMap<String, Integer>> constructor = constructorConstructor.get(typeToken);

    assertNotNull(constructor);
    assertNotNull(constructor.construct());
  }
}

class UnsafeAllocator {
  private static Unsafe UNSAFE;

  static {
    try {
      UNSAFE = sun.misc.Unsafe.getUnsafe();
    } catch (SecurityException e) {
      throw new RuntimeException(e);
    }
  }

  public static Object newInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
    try {
      UNSAFE.allocateInstance(clazz);
      return null;
    } catch (InstantiationException e) {
      throw e;
    } catch (IllegalAccessException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Unsafe create() {
    return UNSAFE;
  }
}

class MyClass {
  private static final long serialVersionUID = 1L;
}