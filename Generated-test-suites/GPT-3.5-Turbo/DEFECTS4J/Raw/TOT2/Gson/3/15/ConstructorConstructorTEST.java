import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ConstructorConstructorTest {
  
  @Test
  public void testGet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
  
    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(TypeToken.get(Object.class));
    
    assertNotNull(objectConstructor);
    assertNotNull(objectConstructor.construct());
  }
  
  @Test
  public void testNewDefaultConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
  
    ObjectConstructor<ArrayList> listConstructor = constructorConstructor.newDefaultConstructor(ArrayList.class);
    
    assertNotNull(listConstructor);
    assertNotNull(listConstructor.construct());
  }
  
  @Test
  public void testNewDefaultImplementationConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
  
    ObjectConstructor<LinkedHashMap> mapConstructor = constructorConstructor.newDefaultImplementationConstructor(
      new TypeToken<LinkedHashMap>(){}.getType(), LinkedHashMap.class);
    
    assertNotNull(mapConstructor);
    assertNotNull(mapConstructor.construct());
  }
  
  @Test
  public void testNewUnsafeAllocator() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
  
    ObjectConstructor<Object> unsafeConstructor = constructorConstructor.newUnsafeAllocator(
      new TypeToken<Object>(){}.getType(), Object.class);
    
    assertNotNull(unsafeConstructor);
    assertNotNull(unsafeConstructor.construct());
  }
  
  @Test
  public void testToString() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
  
    assertEquals(instanceCreators.toString(), constructorConstructor.toString());
  }
}