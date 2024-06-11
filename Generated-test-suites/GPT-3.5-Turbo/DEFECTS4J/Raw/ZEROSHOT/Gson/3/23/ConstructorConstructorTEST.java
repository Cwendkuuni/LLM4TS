import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

import org.junit.Before;
import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {
  
  private ConstructorConstructor constructor;
  
  @Before
  public void setUp() {
    constructor = new ConstructorConstructor(null);
  }
  
  @Test
  public void testGet() {
    TypeToken typeToken = new TypeToken<ConstructorConstructorTest>() {};
    assertNotNull(constructor.get(typeToken));
  }
  
  @Test
  public void testNewDefaultConstructor() {
    Class clazz = ConstructorConstructor.class;
    assertNull(constructor.newDefaultConstructor(clazz));
  }
  
  @Test
  public void testNewDefaultImplementationConstructor() {
    Type type = new TypeToken<Collection<String>>() {}.getType();
    Class clazz = Collection.class;
    assertNotNull(constructor.newDefaultImplementationConstructor(type, clazz));
  }
  
  @Test
  public void testNewUnsafeAllocator() {
    Type type = new TypeToken<ConstructorConstructorTest>() {}.getType();
    Class clazz = ConstructorConstructorTest.class;
    assertNotNull(constructor.newUnsafeAllocator(type, clazz));
  }  
}