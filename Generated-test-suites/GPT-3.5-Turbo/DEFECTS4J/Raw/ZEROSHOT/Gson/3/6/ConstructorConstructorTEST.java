import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorConstructorTest {

  private ConstructorConstructor constructorConstructor;

  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor(null);
  }

  @Test
  public void testGet_withTypeToken_shouldReturnObjectConstructor() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    ObjectConstructor<String> constructor = constructorConstructor.get(typeToken);
    assertNotNull(constructor);
  }

  @Test
  public void testGet_withTypeTokenAndInstanceCreator_shouldReturnObjectConstructor() {
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
    InstanceCreator<Integer> instanceCreator = new InstanceCreator<Integer>() {
      @Override
      public Integer createInstance(Type type) {
        return new Integer(0);
      }
    };
    constructorConstructor = new ConstructorConstructor(Map.of(typeToken.getType(), instanceCreator));
    ObjectConstructor<Integer> constructor = constructorConstructor.get(typeToken);
    assertNotNull(constructor);
  }

  @Test
  public void testGet_withTypeTokenAndRawTypeInstanceCreator_shouldReturnObjectConstructor() {
    TypeToken<Double> typeToken = TypeToken.get(Double.class);
    InstanceCreator<Number> instanceCreator = new InstanceCreator<Number>() {
      @Override
      public Number createInstance(Type type) {
        return new Double(0);
      }
    };
    constructorConstructor = new ConstructorConstructor(Map.of(Double.class, instanceCreator));
    ObjectConstructor<Double> constructor = constructorConstructor.get(typeToken);
    assertNotNull(constructor);
  }

  @Test
  public void testGet_withTypeTokenAndDefaultConstructor_shouldReturnObjectConstructor() {
    TypeToken<Long> typeToken = TypeToken.get(Long.class);
    ObjectConstructor<Long> constructor = constructorConstructor.get(typeToken);
    assertNotNull(constructor);
  }

  @Test
  public void testGet_withTypeTokenAndDefaultImplementation_shouldReturnObjectConstructor() {
    TypeToken<Collection<String>> typeToken = new TypeToken<Collection<String>>() {};
    ObjectConstructor<Collection<String>> constructor = constructorConstructor.get(typeToken);
    assertNotNull(constructor);
  }

  @Test
  public void testGet_withTypeTokenAndDefaultMapImplementation_shouldReturnObjectConstructor() {
    TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};
    ObjectConstructor<Map<String, Integer>> constructor = constructorConstructor.get(typeToken);
    assertNotNull(constructor);
  }

  @Test
  public void testGet_withTypeTokenAndInvalidEnumSetType_shouldThrowJsonIOException() {
    TypeToken<EnumSet<Integer>> typeToken = new TypeToken<EnumSet<Integer>>() {};
    try {
      ObjectConstructor<EnumSet<Integer>> constructor = constructorConstructor.get(typeToken);
      fail("Expected JsonIOException to be thrown");
    } catch (JsonIOException e) {
      // Success
    }
  }
}