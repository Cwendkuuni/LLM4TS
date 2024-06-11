import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

public class ConstructorConstructorTest {

  private ConstructorConstructor constructorConstructor;

  @Before
  public void setup() {
    constructorConstructor = new ConstructorConstructor(null);
  }

  @Test
  public void testGet_InstanceCreatorExists_ReturnInstanceCreatorObject() {
    // Arrange
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    TypeToken<CustomObject> typeToken = new TypeToken<CustomObject>() {};
    CustomInstanceCreator customInstanceCreator = new CustomInstanceCreator();
    instanceCreators.put(typeToken.getType(), customInstanceCreator);
    constructorConstructor = new ConstructorConstructor(instanceCreators);

    // Act
    ObjectConstructor<CustomObject> result = constructorConstructor.get(typeToken);

    // Assert
    assertEquals(customInstanceCreator, result);
  }

  @Test
  public void testGet_RawTypeInstanceCreatorExists_ReturnRawTypeInstanceCreatorObject() {
    // Arrange
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ClassToken<CustomObject> classToken = new ClassToken<>(CustomObject.class);
    CustomInstanceCreator customInstanceCreator = new CustomInstanceCreator();
    instanceCreators.put(classToken.getRawType(), customInstanceCreator);
    constructorConstructor = new ConstructorConstructor(instanceCreators);

    // Act
    ObjectConstructor<CustomObject> result = constructorConstructor.get(classToken);

    // Assert
    assertEquals(customInstanceCreator, result);
  }

  @Test
  public void testGet_DefaultConstructorExists_ReturnDefaultConstructorObject() {
    // Arrange
    ClassToken<DefaultConstructorClass> classToken = new ClassToken<>(DefaultConstructorClass.class);

    // Act
    ObjectConstructor<DefaultConstructorClass> result = constructorConstructor.get(classToken);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGet_DefaultImplementationConstructorExists_ReturnDefaultImplementationConstructorObject() {
    // Arrange
    TypeToken<Collection<String>> typeToken = new TypeToken<Collection<String>>() {};

    // Act
    ObjectConstructor<Collection<String>> result = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGet_UnsafeAllocatorConstructorExists_ReturnUnsafeAllocatorConstructorObject() {
    // Arrange
    TypeToken<CustomObject> typeToken = new TypeToken<CustomObject>() {};

    // Act
    ObjectConstructor<CustomObject> result = constructorConstructor.get(typeToken);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testNewDefaultConstructor_ValidConstructor_ReturnNewDefaultConstructor() {
    // Arrange
    ClassToken<DefaultConstructorClass> classToken = new ClassToken<>(DefaultConstructorClass.class);

    // Act
    ObjectConstructor<DefaultConstructorClass> result = constructorConstructor.newDefaultConstructor(classToken.getRawType());

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testNewDefaultConstructor_NoDefaultConstructor_ReturnNull() {
    // Arrange
    ClassToken<NoDefaultConstructorClass> classToken = new ClassToken<>(NoDefaultConstructorClass.class);

    // Act
    ObjectConstructor<NoDefaultConstructorClass> result = constructorConstructor.newDefaultConstructor(classToken.getRawType());

    // Assert
    assertNull(result);
  }

  @Test
  public void testNewDefaultImplementationConstructor_CollectionInterface_ReturnNewDefaultImplementationConstructor() {
    // Arrange
    TypeToken<Collection<Object>> typeToken = new TypeToken<Collection<Object>>() {};

    // Act
    ObjectConstructor<Collection<Object>> result = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), Collection.class);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testNewDefaultImplementationConstructor_MapInterface_ReturnNewDefaultImplementationConstructor() {
    // Arrange
    TypeToken<Map<Object, Object>> typeToken = new TypeToken<Map<Object, Object>>() {};

    // Act
    ObjectConstructor<Map<Object, Object>> result = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), Map.class);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testNewDefaultImplementationConstructor_SubtypeOfMap_ReturnNewDefaultImplementationConstructor() {
    // Arrange
    TypeToken<LinkedHashMap<Object, Object>> typeToken = new TypeToken<LinkedHashMap<Object, Object>>() {};

    // Act
    ObjectConstructor<LinkedHashMap<Object, Object>> result = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), LinkedHashMap.class);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testNewUnsafeAllocator_ValidType_ReturnNewUnsafeAllocator() {
    // Arrange
    TypeToken<CustomObject> typeToken = new TypeToken<CustomObject>() {};

    // Act
    ObjectConstructor<CustomObject> result = constructorConstructor.newUnsafeAllocator(typeToken.getType(), CustomObject.class);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testToString_ReturnStringRepresentationOfInstanceCreatorsMap() {
    // Arrange
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    instanceCreators.put(new TypeToken<CustomObject>() {}.getType(), new CustomInstanceCreator());
    constructorConstructor = new ConstructorConstructor(instanceCreators);

    // Act
    String result = constructorConstructor.toString();

    // Assert
    assertEquals(instanceCreators.toString(), result);
  }

  private static class CustomObject {
    public CustomObject() {}
  }

  private static class DefaultConstructorClass {
    public DefaultConstructorClass() {}

    public DefaultConstructorClass(int arg) {}
  }

  private static class NoDefaultConstructorClass {
    public NoDefaultConstructorClass(int arg) {}
  }

  private static class CustomInstanceCreator implements InstanceCreator<CustomObject> {
    @Override
    public CustomObject createInstance(Type type) {
      return new CustomObject();
    }
  }

}