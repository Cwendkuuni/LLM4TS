import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.*;

public class ConstructorConstructorTest {

  @Test
  public void testGet() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<String> typeToken = TypeToken.get(String.class);
    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);

    assertEquals("String constructor not created correctly", String.class, objectConstructor.construct().getClass());

  }

  @Test
  public void testNewDefaultConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());

    ObjectConstructor<ConstructorConstructor> objectConstructor = constructorConstructor.newDefaultConstructor(ConstructorConstructor.class);

    assertNotNull("Default constructor not created correctly", objectConstructor);

  }

  @Test
  public void testNewDefaultImplementationConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());

    ObjectConstructor<List<Object>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(
            new TypeToken<List<Object>>(){}.getType(), List.class);

    assertNotNull("Default implementation constructor not created correctly", objectConstructor);

  }

  @Test
  public void testNewUnsafeAllocator() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());

    ObjectConstructor<ConstructorConstructor> objectConstructor = constructorConstructor.newUnsafeAllocator(
            ConstructorConstructor.class, ConstructorConstructor.class);

    assertNotNull("Unsafe allocator not created correctly", objectConstructor);

  }

}