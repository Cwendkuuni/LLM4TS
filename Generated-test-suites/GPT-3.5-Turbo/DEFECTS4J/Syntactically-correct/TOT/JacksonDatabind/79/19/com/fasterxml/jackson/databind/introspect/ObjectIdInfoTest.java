import org.junit.Test;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

  @Test
  public void testConstructorWithPropertyNameScopeGeneratorResolver() {
    PropertyName propertyName = new PropertyName("test");
    Class<?> scope = String.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
    Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

    // Assert the values of the properties
    assertEquals(propertyName, objectIdInfo.getPropertyName());
    assertEquals(scope, objectIdInfo.getScope());
    assertEquals(generator, objectIdInfo.getGeneratorType());
    assertEquals(resolver, objectIdInfo.getResolverType());
    assertFalse(objectIdInfo.getAlwaysAsId());
  }

  @Test
  public void testConstructorWithPropertyNameScopeGenerator() {
    PropertyName propertyName = new PropertyName("test");
    Class<?> scope = String.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

    // Assert the values of the properties
    assertEquals(propertyName, objectIdInfo.getPropertyName());
    assertEquals(scope, objectIdInfo.getScope());
    assertEquals(generator, objectIdInfo.getGeneratorType());
    assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    assertFalse(objectIdInfo.getAlwaysAsId());
  }

  @Test
  public void testConstructorWithStringScopeGenerator() {
    String propertyName = "test";
    Class<?> scope = String.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

    // Assert the values of the properties
    assertEquals(new PropertyName(propertyName), objectIdInfo.getPropertyName());
    assertEquals(scope, objectIdInfo.getScope());
    assertEquals(generator, objectIdInfo.getGeneratorType());
    assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    assertFalse(objectIdInfo.getAlwaysAsId());
  }

  @Test
  public void testWithAlwaysAsId() {
    PropertyName propertyName = new PropertyName("test");
    Class<?> scope = String.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
    boolean alwaysAsId = true;

    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId);

    // Assert the value of alwaysAsId
    assertTrue(objectIdInfo.getAlwaysAsId());

    // Test the withAlwaysAsId method by changing the state to false
    ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(false);

    // Assert the new value of alwaysAsId
    assertFalse(newObjectIdInfo.getAlwaysAsId());
  }

  @Test
  public void testToString() {
    PropertyName propertyName = new PropertyName("test");
    Class<?> scope = String.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
    boolean alwaysAsId = true;

    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId);

    // Assert the expected string representation
    assertEquals("ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
  }
}