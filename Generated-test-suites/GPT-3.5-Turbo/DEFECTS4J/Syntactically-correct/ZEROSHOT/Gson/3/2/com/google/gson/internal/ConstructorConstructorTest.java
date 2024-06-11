import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.InvocationTargetException;
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

public class ConstructorConstructorTest {
  
  @Test
  public void testGet() {
    ConstructorConstructor cc = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
    
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> stringObjectConstructor = cc.get(stringTypeToken);
    String stringObj = stringObjectConstructor.construct();
    assertNotNull(stringObj);
    
    TypeToken<Integer> intTypeToken = new TypeToken<Integer>() {};
    ObjectConstructor<Integer> intObjectConstructor = cc.get(intTypeToken);
    Integer intObj = intObjectConstructor.construct();
    assertNotNull(intObj);
  }
  
  @Test
  public void testNewDefaultConstructor() {
    ConstructorConstructor cc = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
    
    ObjectConstructor<ConstructorConstructor> constructorConstructorObjectConstructor = cc.newDefaultConstructor(ConstructorConstructor.class);
    ConstructorConstructor constructorConstructor = constructorConstructorObjectConstructor.construct();
    assertNotNull(constructorConstructor);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor() {
    ConstructorConstructor cc = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
    
    TypeToken<Set<Integer>> setTypeToken = new TypeToken<Set<Integer>>() {};
    ObjectConstructor<Set<Integer>> setObjectConstructor = cc.newDefaultImplementationConstructor(setTypeToken.getType(), Set.class);
    Set<Integer> setObj = setObjectConstructor.construct();
    assertNotNull(setObj);
    
    TypeToken<Queue<Integer>> queueTypeToken = new TypeToken<Queue<Integer>>() {};
    ObjectConstructor<Queue<Integer>> queueObjectConstructor = cc.newDefaultImplementationConstructor(queueTypeToken.getType(), Queue.class);
    Queue<Integer> queueObj = queueObjectConstructor.construct();
    assertNotNull(queueObj);
  }
  
  @Test
  public void testNewUnsafeAllocator() {
    ConstructorConstructor cc = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
    
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> stringObjectConstructor = cc.newUnsafeAllocator(stringTypeToken.getType(), String.class);
    String stringObj = stringObjectConstructor.construct();
    assertNotNull(stringObj);
  }
  
  @Test
  public void testToString() {
    ConstructorConstructor cc = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
    assertNotNull(cc.toString());
  }
  
}