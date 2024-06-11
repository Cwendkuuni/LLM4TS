import static org.junit.Assert.*;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeToken;

public class ConstructorConstructorTest {
  @SuppressWarnings("unchecked")
  @Test
  public void testGet_TypeToken_ReturnsInstanceCreatorOrDefaultConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<String> stringInstanceCreator = new InstanceCreator<String>() {
      @Override public String createInstance(Type type) {
        return "default";
      }
    };
    instanceCreators.put(new TypeToken<String>() {}.getType(), stringInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<String> stringInstance = constructorConstructor.get(new TypeToken<String>() {}.getType());
    assertEquals("default", stringInstance.construct());

    ObjectConstructor<Number> numberInstance = constructorConstructor.get(new TypeToken<Number>() {}.getType());
    assertNull(numberInstance); // no instance creator or default constructor

    ObjectConstructor<ArrayList<String>> listInstance = constructorConstructor.get(new TypeToken<ArrayList<String>>() {}.getType());
    assertEquals(new ArrayList<>(), listInstance.construct());
  }

  @Test(expected = RuntimeException.class)
  public void testGet_TypeToken_ThrowsRuntimeException_WhenNoConstructorFound() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<?> noConstructor = constructorConstructor.get(new TypeToken<String>() {}.getType());
    noConstructor.construct(); // should throw RuntimeException
  }
}