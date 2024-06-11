import static org.junit.Assert.*;
import static com.google.common.reflect.Types.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.reflect.TypeToken;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceForArrayList() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<ArrayList<String>> instanceCreator = new InstanceCreator<ArrayList<String>>() {
      @Override public ArrayList<String> createInstance(Type type) {
        return new ArrayList<String>();
      }
    };
    instanceCreators.put(new TypeToken<ArrayList<String>>(){}, instanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(TypeToken.get(ArrayList.class));
    assertEquals(ArrayList.class, constructor.getType());
    ArrayList<String> list = constructor.construct();
    assertNotNull(list);
    assertTrue(list.isEmpty());
  }

  @Test
  public void testGetInstanceForTreeMap() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<TreeMap<String, Object>> instanceCreator = new InstanceCreator<TreeMap<String, Object>>() {
      @Override public TreeMap<String, Object> createInstance(Type type) {
        return new TreeMap<String, Object>();
      }
    };
    instanceCreators.put(new TypeToken<TreeMap<String, Object>>(){}, instanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<TreeMap<String, Object>> constructor = constructorConstructor.get(TypeToken.get(TreeMap.class));
    assertEquals(TreeMap.class, constructor.getType());
    TreeMap<String, Object> map = constructor.construct();
    assertNotNull(map);
    assertTrue(map.isEmpty());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceForUnknownType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<Object> constructor = constructorConstructor.get(TypeToken.get(String.class));
    constructor.construct();
    fail("Expected RuntimeException for unknown type");
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceForUnsupportedType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<EnumSet<String>> constructor = constructorConstructor.get(TypeToken.get(EnumSet.class));
    constructor.construct();
    fail("Expected RuntimeException for unsupported type");
  }

  @Test
  public void testGetInstanceForRawType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<ArrayList> instanceCreator = new InstanceCreator<ArrayList>() {
      @Override public ArrayList createInstance(Type type) {
        return new ArrayList<>();
      }
    };
    instanceCreators.put(getRawType(ArrayList.class), instanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<ArrayList> constructor = constructorConstructor.get(new TypeToken<ArrayList>() {});
    assertEquals(ArrayList.class, constructor.getType());
    ArrayList list = constructor.construct();
    assertNotNull(list);
    assertTrue(list.isEmpty());
  }

  @Test
  public void testGetInstanceForAbstractClass() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<ArrayList> instanceCreator = new InstanceCreator<ArrayList>() {
      @Override public ArrayList createInstance(Type type) {
        return new ArrayList<>();
      }
    };
    instanceCreators.put(ArrayList.class, instanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<ArrayList> constructor = constructorConstructor.get(new TypeToken<ArrayList>() {});
    assertEquals(ArrayList.class, constructor.getType());
    ArrayList list = constructor.construct();
    assertNotNull(list);
    assertTrue(list.isEmpty());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceForNullType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<Object> constructor = constructorConstructor.get((Type) null);
    constructor.construct();
    fail("Expected RuntimeException for null type");
  }

  @Test
  public void testGetInstanceForThrowingConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    Constructor<Object> constructor = Object.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    constructor.setException(new RuntimeException("Constructor throws an exception"));
    InstanceCreator<Object> instanceCreator = new InstanceCreator<Object>() {
      @Override public Object createInstance(Type type) throws Exception {
        constructor.newInstance();
        return null;
      }
    };
    instanceCreators.put(Object.class, instanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<Object> constructorObject = constructorConstructor.get(new TypeToken<Object>() {});
    try {
      constructorObject.construct();
      fail("Expected RuntimeException from constructor");
    } catch (RuntimeException e) {
      assertEquals("Constructor throws an exception", e.getMessage());
    }
  }

  @Test
  public void testGetInstanceForNoArgsConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<Object> instanceCreator = new InstanceCreator<Object>() {
      @Override public Object createInstance(Type type) {
        return new Object();
      }
    };
    instanceCreators.put(Object.class, instanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<Object> constructor = constructorConstructor.get(new TypeToken<Object>() {});
    assertEquals(Object.class, constructor.getType());
    Object object = constructor.construct();
    assertNotNull(object);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetInstanceForNullInstanceCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<Object> constructor = constructorConstructor.get((InstanceCreator<Object>) null);
    constructor.construct();
    fail("Expected IllegalStateException for null instance creator");
  }
}