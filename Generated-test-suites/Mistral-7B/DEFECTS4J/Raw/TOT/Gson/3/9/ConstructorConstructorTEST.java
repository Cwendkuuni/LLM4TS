import static org.junit.Assert.*;
import static com.google.common.reflect.Types.*;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstance() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    // Test for a simple class
    TypeString typeString = new TypeToken<String>() {}.getType();
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(typeString);
    assertEquals("", stringConstructor.construct());

    // Test for a Collection
    TypeCollectionOfStrings typeCollectionOfStrings = new TypeToken<Collection<String>>() {}.getType();
    ObjectConstructor<Collection<String>> collectionConstructor = constructorConstructor.get(typeCollectionOfStrings);
    assertEquals(Collections.emptyList(), collectionConstructor.construct());

    // Test for a SortedSet
    TypeSortedSetOfStrings typeSortedSetOfStrings = new TypeToken<SortedSet<String>>() {}.getType();
    ObjectConstructor<SortedSet<String>> sortedSetConstructor = constructorConstructor.get(typeSortedSetOfStrings);
    assertEquals(Collections.emptySortedSet(), sortedSetConstructor.construct());

    // Test for a Map
    TypeMapOfStringsToStrings typeMapOfStringsToStrings = new TypeToken<Map<String, String>>() {}.getType();
    ObjectConstructor<Map<String, String>> mapConstructor = constructorConstructor.get(typeMapOfStringsToStrings);
    assertEquals(new LinkedHashMap<String, String>(), mapConstructor.construct());

    // Test for a SortedMap
    TypeSortedMapOfStringsToString typeSortedMapOfStringsToString = new TypeToken<SortedMap<String, String>>() {}.getType();
    ObjectConstructor<SortedMap<String, String>> sortedMapConstructor = constructorConstructor.get(typeSortedMapOfStringsToString);
    assertEquals(new TreeMap<String, String>(), sortedMapConstructor.construct());

    // Test for a custom class
    CustomClass customClassType = new TypeToken<CustomClass>() {}.getType();
    CustomClass customClass = new CustomClass();
    ObjectConstructor<CustomClass> customClassConstructor = new ObjectConstructor<CustomClass>() {
      @Override public CustomClass construct() {
        try {
          Constructor<CustomClass> constructor = CustomClass.class.getDeclaredConstructor();
          constructor.setAccessible(true);
          return constructor.newInstance(new Object[]{});
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    };
    assertNotNull(customClassConstructor);
    CustomClass constructedCustomClass = customClassConstructor.construct();
    assertEquals(customClass, constructedCustomClass);

    // Test for a primitive type
    Type intType = new TypeToken<Integer>() {}.getType();
    ObjectConstructor<Integer> intConstructor = constructorConstructor.get(intType);
    assertEquals(0, intConstructor.construct());

    // Test for null
    Type nullType = new TypeToken<Void>() {}.getType();
    ObjectConstructor<Void> nullConstructor = constructorConstructor.get(nullType);
    assertNull(nullConstructor.construct());

    // Test for an interface
    Type CollectionType = new TypeToken<Collection<?>>() {}.getType();
    ObjectConstructor<Collection<?>> collectionConstructor = constructorConstructor.get(CollectionType);
    assertEquals(new ArrayList<Object>(), collectionConstructor.construct());

    // Test for an unsupported type
    Type unsupportedType = new TypeToken<Object>() {}.getType();
    ObjectConstructor<Object> unsupportedConstructor = constructorConstructor.get(unsupportedType);
    assertNull(unsupportedConstructor);

    // Test for a class without a default constructor
    TypeEmptyClass typeEmptyClass = new TypeToken<EmptyClass>() {}.getType();
    ObjectConstructor<EmptyClass> emptyClassConstructor = constructorConstructor.get(typeEmptyClass);
    assertNull(emptyClassConstructor);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceWithException() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeString typeString = new TypeToken<String>() {}.getType();
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(typeString);
    stringConstructor.construct();
  }

  @Test(expected = JsonIOException.class)
  public void testGetInstanceWithJsonIOException() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeMapOfStringsToStrings typeMapOfStringsToStrings = new TypeToken<Map<String, String>>() {}.getType();
    ObjectConstructor<Map<String, String>> mapConstructor = constructorConstructor.get(typeMapOfStringsToStrings);
    mapConstructor.construct();

    // Throw JsonIOException intentionally
    throw new JsonIOException("Test error");
  }
}

class CustomClass {
  // empty class for testing
}

class EmptyClass {
  // empty class with no constructor
}