import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

  @Test
  public void testConstructorConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    assertNotNull(constructorConstructor);
    assertEquals(instanceCreators, constructorConstructor.toString());
  }

  @Test
  public void testGet() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);

    TypeToken<ArrayList<String>> genericTypeToken = new TypeToken<ArrayList<String>>() {};
    ObjectConstructor<ArrayList<String>> genericObjectConstructor = constructorConstructor
        .get(genericTypeToken);
    assertNotNull(genericObjectConstructor);

    TypeToken<Integer> rawTypeToken = TypeToken.get(int.class);
    ObjectConstructor<Integer> rawObjectConstructor = constructorConstructor.get(rawTypeToken);
    assertNull(rawObjectConstructor);
  }

  @Test
  public void testNewDefaultConstructor() throws NoSuchMethodException, SecurityException {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new LinkedHashMap<Type, InstanceCreator<?>>());
    Class<?> rawType = String.class;
    Constructor<?> constructor = rawType.getDeclaredConstructor();
    ObjectConstructor<Object> objectConstructor = constructorConstructor
        .newDefaultConstructor(rawType);
    assertNotNull(objectConstructor);
    assertEquals(rawType, constructor.getDeclaringClass());
    assertEquals(rawType, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultConstructorNoSuchMethodException() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new LinkedHashMap<Type, InstanceCreator<?>>());
    Class<?> rawType = Integer.class;
    ObjectConstructor<Object> objectConstructor = constructorConstructor
        .newDefaultConstructor(rawType);
    assertNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new LinkedHashMap<Type, InstanceCreator<?>>());
    Type type = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { String.class };
      }

      @Override
      public Type getRawType() {
        return null;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    Class<?> rawType = ArrayList.class;
    ObjectConstructor<Object> objectConstructor = constructorConstructor
        .newDefaultImplementationConstructor(type, rawType);
    assertNotNull(objectConstructor);
    assertEquals(rawType, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructorInvalidEnumSetType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new LinkedHashMap<Type, InstanceCreator<?>>());
    Type type = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { null };
      }

      @Override
      public Type getRawType() {
        return null;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    Class<?> rawType = EnumSet.class;
    try {
      constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    } catch (JsonIOException e) {
      assertEquals("Invalid EnumSet type: null", e.getMessage());
    }
  }

  @Test
  public void testNewDefaultImplementationConstructorInvalidEnumSetType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new LinkedHashMap<Type, InstanceCreator<?>>());
    Type type = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { String.class };
      }

      @Override
      public Type getRawType() {
        return null;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    Class<?> rawType = EnumSet.class;
    try {
      constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    } catch (JsonIOException e) {
      assertEquals("Invalid EnumSet type: java.lang.String", e.getMessage());
    }
  }

  @Test
  public void testNewDefaultImplementationConstructorSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new LinkedHashMap<Type, InstanceCreator<?>>());
    Type type = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { String.class };
      }

      @Override
      public Type getRawType() {
        return null;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    Class<?> rawType = LinkedHashSet.class;
    ObjectConstructor<Object> objectConstructor = constructorConstructor
        .newDefaultImplementationConstructor(type, rawType);
    assertNotNull(objectConstructor);
    assertEquals(rawType, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructorQueue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new LinkedHashMap<Type, InstanceCreator<?>>());
    Type type = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { String.class };
      }

      @Override
      public Type getRawType() {
        return null;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    Class<?> rawType = LinkedList.class;
    ObjectConstructor<Object> objectConstructor = constructorConstructor
        .newDefaultImplementationConstructor(type, rawType);
    assertNotNull(objectConstructor);
    assertEquals(rawType, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructorCollection() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new LinkedHashMap<Type, InstanceCreator<?>>());
    Type type = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { String.class };
      }

      @Override
      public Type getRawType() {
        return null;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    Class<?> rawType = ArrayList.class;
    ObjectConstructor<Object> objectConstructor = constructorConstructor
        .newDefaultImplementationConstructor(type, rawType);
    assertNotNull(objectConstructor);
    assertEquals(rawType, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewDefaultImplementationConstructorMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new LinkedHashMap<Type, InstanceCreator<?>>());
    Type type = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { String.class };
      }

      @Override
      public Type getRawType() {
        return null;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    Class<?> rawType = LinkedHashMap.class;
    ObjectConstructor<Object> objectConstructor = constructorConstructor
        .newDefaultImplementationConstructor(type, rawType);
    assertNotNull(objectConstructor);
    assertEquals(rawType, objectConstructor.construct().getClass());
  }

  @Test
  public void testNewUnsafeAllocator() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new LinkedHashMap<Type, InstanceCreator<?>>());
    Type type = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { String.class };
      }

      @Override
      public Type getRawType() {
        return null;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    Class<?> rawType = String.class;
    ObjectConstructor<Object> objectConstructor = constructorConstructor
        .newUnsafeAllocator(type, rawType);
    assertNotNull(objectConstructor);
    assertEquals(rawType, objectConstructor.construct().getClass());
  }

  @Test
  public void testToString() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<Type, InstanceCreator<?>>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    assertEquals(instanceCreators.toString(), constructorConstructor.toString());
  }
}