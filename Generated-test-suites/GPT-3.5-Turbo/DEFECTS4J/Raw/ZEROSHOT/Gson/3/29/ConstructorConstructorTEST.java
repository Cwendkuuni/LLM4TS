import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
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
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.ConstructorConstructor;

public class ConstructorConstructorTest {
  
  private ConstructorConstructor constructorConstructor;
  
  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
  }
  
  @Test
  public void testGet_TypeToken() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_WithInstanceCreator() {
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);
    InstanceCreator<TestClass> instanceCreator = new InstanceCreator<TestClass>() {
      @Override
      public TestClass createInstance(Type type) {
        return new TestClass();
      }
    };
    constructorConstructor = new ConstructorConstructor(
      new LinkedHashMap<Type, InstanceCreator<?>>() {{
        put(typeToken.getType(), instanceCreator);
      }}
    );
    ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_WithRawTypeInstanceCreator() {
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);
    InstanceCreator<Object> instanceCreator = new InstanceCreator<Object>() {
      @Override
      public Object createInstance(Type type) {
        return new TestClass();
      }
    };
    constructorConstructor = new ConstructorConstructor(
      new LinkedHashMap<Type, InstanceCreator<?>>() {{
        put(TestClass.class, instanceCreator);
      }}
    );
    ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_NewDefaultConstructor() {
    TypeToken<TestInterface> typeToken = TypeToken.get(TestInterface.class);
    ObjectConstructor<TestInterface> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_NewDefaultImplementationConstructor() {
    TypeToken<TestList> typeToken = TypeToken.get(TestList.class);
    ObjectConstructor<TestList> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_NewDefaultImplementationConstructor_EnumSet() {
    TypeToken<TestSet> typeToken = TypeToken.get(TestSet.class);
    ObjectConstructor<TestSet> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_NewDefaultImplementationConstructor_Set() {
    TypeToken<TestLinkedHashSet> typeToken = TypeToken.get(TestLinkedHashSet.class);
    ObjectConstructor<TestLinkedHashSet> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_NewDefaultImplementationConstructor_Queue() {
    TypeToken<TestQueue> typeToken = TypeToken.get(TestQueue.class);
    ObjectConstructor<TestQueue> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_NewDefaultImplementationConstructor_ArrayList() {
    TypeToken<TestArrayList> typeToken = TypeToken.get(TestArrayList.class);
    ObjectConstructor<TestArrayList> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_NewDefaultImplementationConstructor_TreeMap() {
    TypeToken<TestTreeMap> typeToken = TypeToken.get(TestTreeMap.class);
    ObjectConstructor<TestTreeMap> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_NewDefaultImplementationConstructor_LinkedHashMap() {
    TypeToken<TestLinkedHashMap> typeToken = TypeToken.get(TestLinkedHashMap.class);
    ObjectConstructor<TestLinkedHashMap> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_NewDefaultImplementationConstructor_LinkedTreeMap() {
    TypeToken<TestLinkedTreeMap> typeToken = TypeToken.get(TestLinkedTreeMap.class);
    ObjectConstructor<TestLinkedTreeMap> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_NewInvalidDefaultImplementationConstructor_ArrayList() {
    TypeToken<TestInvalidArrayList> typeToken = TypeToken.get(TestInvalidArrayList.class);
    ObjectConstructor<TestInvalidArrayList> objectConstructor = constructorConstructor.get(typeToken);
    assertNull(objectConstructor);
  }
  
  @Test
  public void testGet_NewUnsafeAllocator() {
    TypeToken<TestUnsafeAllocator> typeToken = TypeToken.get(TestUnsafeAllocator.class);
    ObjectConstructor<TestUnsafeAllocator> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
  }
}