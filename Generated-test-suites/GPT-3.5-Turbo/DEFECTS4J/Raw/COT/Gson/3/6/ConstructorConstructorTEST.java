import org.junit.Assert;
import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testGet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeToken<String> typeToken = TypeToken.get(String.class);

    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);

    Assert.assertNotNull(objectConstructor);
  }

  @Test
  public void testNewDefaultConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    Class<String> rawType = String.class;

    ObjectConstructor<String> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);

    Assert.assertNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    Type type = new TypeToken<ArrayList<String>>() {}.getType();
    Class<ArrayList> rawType = ArrayList.class;

    ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

    Assert.assertNotNull(objectConstructor);
  }

  @Test
  public void testNewUnsafeAllocator() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    Type type = new TypeToken<String>() {}.getType();
    Class<String> rawType = String.class;

    ObjectConstructor<String> objectConstructor = constructorConstructor.newUnsafeAllocator(type, rawType);

    Assert.assertNotNull(objectConstructor);
  }

  @Test
  public void testToString() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());

    String result = constructorConstructor.toString();

    Assert.assertNotNull(result);
  }

}