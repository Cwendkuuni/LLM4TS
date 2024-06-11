import static org.junit.Assert.*;
import org.junit.Test;
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
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;

public class ConstructorConstructorTest {

  @Test
  public void testConstructorConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    assertNotNull(constructorConstructor);
  }

  @Test
  public void testGetWithTypeToken() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<Object> typeToken = TypeToken.get(Object.class);
    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testGetWithTypeTokenUsingInstanceCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    instanceCreators.put(Object.class, new TestInstanceCreator<Object>());
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<Object> typeToken = TypeToken.get(Object.class);
    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testGetWithTypeTokenUsingRawTypeCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    instanceCreators.put(Object.class, new TestInstanceCreator<Object>());
    instanceCreators.put(String.class, new TestInstanceCreator<String>());
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testNewDefaultConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<Object> objectConstructor = constructorConstructor.newDefaultConstructor(Object.class);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testNewDefaultConstructorWithInvalidType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    ObjectConstructor<Object> objectConstructor = constructorConstructor.newDefaultConstructor(String.class);
    assertNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    Type type = new ParameterizedTypeImpl(String.class);
    Class<? super Object> rawType = Object.class;
    ObjectConstructor<Object> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNotNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructorWithInvalidType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    Type type = new ParameterizedTypeImpl(Number.class);
    Class<? super Object> rawType = Object.class;
    ObjectConstructor<Object> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
    assertNull(objectConstructor);
  }

  @Test
  public void testNewUnsafeAllocator() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
    Type type = new ParameterizedTypeImpl(String.class);
    Class<? super Object> rawType = Object.class;
    ObjectConstructor<Object> objectConstructor = constructorConstructor.newUnsafeAllocator(type, rawType);
    assertNotNull(objectConstructor);
  }

  class TestInstanceCreator<T> implements InstanceCreator<T> {
    @Override
    public T createInstance(Type type) {
      return null;
    }
  }

  class ParameterizedTypeImpl implements ParameterizedType {

    private final Class<?> clazz;

    public ParameterizedTypeImpl(Class<?> clazz) {
      this.clazz = clazz;
    }

    @Override
    public Type[] getActualTypeArguments() {
      return new Type[0];
    }

    @Override
    public Type getRawType() {
      return clazz;
    }

    @Override
    public Type getOwnerType() {
      return null;
    }
  }
}