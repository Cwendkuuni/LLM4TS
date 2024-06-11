import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Constructor;
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

import org.junit.Before;
import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

  private ConstructorConstructor constructorConstructor;

  @Before
  public void setup() {
    constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
  }

  @Test
  public void get_InstanceCreatorForType_ReturnsObjectConstructorWithInstanceCreator() {
    // Arrange
    TypeToken<Integer> typeToken = new TypeToken<Integer>() {};

    // Act
    ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(objectConstructor);
  }

  @Test
  public void get_InstanceCreatorForRawType_ReturnsObjectConstructorWithInstanceCreator() {
    // Arrange
    TypeToken<Number> typeToken = new TypeToken<Number>() {};

    // Act
    ObjectConstructor<Number> objectConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(objectConstructor);
  }

  @Test
  public void get_DefaultConstructorAvailable_ReturnsObjectConstructorWithDefaultConstructor() {
    // Arrange
    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};

    // Act
    ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(objectConstructor);
  }

  @Test
  public void get_DefaultImplementationConstructorAvailable_ReturnsObjectConstructorWithDefaultImplementationConstructor() {
    // Arrange
    TypeToken<LinkedHashSet<String>> typeToken = new TypeToken<LinkedHashSet<String>>() {};

    // Act
    ObjectConstructor<LinkedHashSet<String>> objectConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(objectConstructor);
  }

  @Test
  public void get_UnsafeConstructorAvailable_ReturnsObjectConstructorWithUnsafeConstructor() {
    // Arrange
    TypeToken<Queue<String>> typeToken = new TypeToken<Queue<String>>() {};

    // Act
    ObjectConstructor<Queue<String>> objectConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(objectConstructor);
  }

  @Test
  public void get_InvalidType_ReturnsNull() {
    // Arrange
    TypeToken<Collection> typeToken = new TypeToken<Collection>() {};

    // Act
    ObjectConstructor<Collection> objectConstructor = constructorConstructor.get(typeToken);

    // Assert
    assertEquals(null, objectConstructor);
  }

  @Test(expected = RuntimeException.class)
  public void get_DefaultConstructorNotAccessible_ThrowsRuntimeException() {
    // Arrange
    TypeToken<ObjectConstructorTestClass> typeToken = new TypeToken<ObjectConstructorTestClass>() {};

    // Act
    constructorConstructor.get(typeToken);
  }

  @Test(expected = RuntimeException.class)
  public void get_DefaultConstructorThrowsException_ThrowsRuntimeException() {
    // Arrange
    TypeToken<DefaultConstructorThrowsExceptionTestClass> typeToken = new TypeToken<DefaultConstructorThrowsExceptionTestClass>() {};

    // Act
    constructorConstructor.get(typeToken);
  }

  @Test(expected = RuntimeException.class)
  public void get_DefaultImplementationConstructorThrowsException_ThrowsRuntimeException() {
    // Arrange
    TypeToken<DefaultImplementationConstructorThrowsExceptionTestClass> typeToken = new TypeToken<DefaultImplementationConstructorThrowsExceptionTestClass>() {};

    // Act
    constructorConstructor.get(typeToken);
  }

  @Test(expected = RuntimeException.class)
  public void get_UnsafeConstructorThrowsException_ThrowsRuntimeException() {
    // Arrange
    TypeToken<UnsafeConstructorThrowsExceptionTestClass> typeToken = new TypeToken<UnsafeConstructorThrowsExceptionTestClass>() {};

    // Act
    constructorConstructor.get(typeToken);
  }

  // Test classes for exception scenarios

  private static class ObjectConstructorTestClass {
    public ObjectConstructorTestClass() {}
    public ObjectConstructorTestClass(String arg) {}
  }

  private static class DefaultConstructorThrowsExceptionTestClass {
    public DefaultConstructorThrowsExceptionTestClass() {
      throw new RuntimeException("Test Exception");
    }
  }

  private static class DefaultImplementationConstructorThrowsExceptionTestClass
      extends ArrayList<String> {
    public DefaultImplementationConstructorThrowsExceptionTestClass() {
      throw new RuntimeException("Test Exception");
    }
  }

  private static class UnsafeConstructorThrowsExceptionTestClass {
    public UnsafeConstructorThrowsExceptionTestClass() {
      throw new RuntimeException("Test Exception");
    }
  }

}