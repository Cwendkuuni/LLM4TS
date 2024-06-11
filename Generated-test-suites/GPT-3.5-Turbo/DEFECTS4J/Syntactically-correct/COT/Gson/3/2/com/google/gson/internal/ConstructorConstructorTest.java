
import org.junit.Test;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
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
import com.google.gson.JsonIOException;

public class ConstructorConstructorTest {
  
  @Test
  public void testGet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    ObjectConstructor<Integer> result = constructorConstructor.get(typeToken);
    assert(result == null);
  }
  
  @Test
  public void testNewDefaultConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    Class<Integer> rawType = Integer.class;
    ObjectConstructor<Integer> result = constructorConstructor.newDefaultConstructor(rawType);
    assert(result == null);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    Class<Integer> rawType = Integer.class;
    ObjectConstructor<Integer> result = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), rawType);
    assert(result == null);
  }
  
  @Test
  public void testNewUnsafeAllocator() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
    Class<Integer> rawType = Integer.class;
    ObjectConstructor<Integer> result = constructorConstructor.newUnsafeAllocator(typeToken.getType(), rawType);
    assert(result == null);
  }
  
  @Test
  public void testToString() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    String result = constructorConstructor.toString();
    assert(result != null);
  }
}
