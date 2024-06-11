import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
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

public class ConstructorConstructorTest {
  private ConstructorConstructor constructorConstructor;
  
  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor(instanceCreators);
  }
  
  @Test
  public void testGet_instanceCreatorReturnsNull_returnsNull() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    assertNull(objectConstructor);
  }
  
  @Test
  public void testGet_instanceCreatorReturnsNonNull_notNull() {
    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_rawTypeCreatorReturnsNull_returnsNull() {
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
    ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);
    assertNull(objectConstructor);
  }
  
  @Test
  public void testGet_rawTypeCreatorReturnsNonNull_notNull() {
    TypeToken<LinkedList<Integer>> typeToken = new TypeToken<LinkedList<Integer>>() {};
    ObjectConstructor<LinkedList<Integer>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_defaultConstructorReturnsNull_returnsNull() {
    TypeToken<Type> typeToken = TypeToken.get(Type.class);
    ObjectConstructor<Type> objectConstructor = constructorConstructor.get(typeToken);
    assertNull(objectConstructor);
  }
  
  @Test
  public void testGet_defaultConstructorReturnsNonNull_notNull() {
    TypeToken<Class<?>> typeToken = TypeToken.get(Class.class);
    ObjectConstructor<Class<?>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_defaultImplementationReturnsNull_returnsNull() {
    TypeToken<Set<Integer>> typeToken = new TypeToken<Set<Integer>>() {};
    ObjectConstructor<Set<Integer>> objectConstructor = constructorConstructor.get(typeToken);
    assertNull(objectConstructor);
  }
  
  @Test
  public void testGet_defaultImplementationReturnsNonNull_notNull() {
    TypeToken<TreeMap<Integer, String>> typeToken = new TypeToken<TreeMap<Integer, String>>() {};
    ObjectConstructor<TreeMap<Integer, String>> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testToString() {
    String expected = instanceCreators.toString();
    String result = constructorConstructor.toString();
    assertEquals(expected, result);
  }
}