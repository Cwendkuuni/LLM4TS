import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceFromMap() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    // Test getting an instance of a simple type (String)
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(stringTypeToken);
    String instance = stringConstructor.construct();
    Assert.assertEquals("", instance);

    // Test getting an instance of a collection type (ArrayList)
    TypeToken<ArrayList<Integer>> listTypeToken = new TypeToken<ArrayList<Integer>>() {};
    ObjectConstructor<ArrayList<Integer>> listConstructor = constructorConstructor.get(listTypeToken);
    ArrayList<Integer> instanceList = listConstructor.construct();
    Assert.assertTrue(instanceList.isEmpty());

    // Test getting an instance of a map type (LinkedHashMap)
    TypeToken<LinkedHashMap<String, Integer>> mapTypeToken = new TypeToken<LinkedHashMap<String, Integer>>() {};
    ObjectConstructor<LinkedHashMap<String, Integer>> mapConstructor = constructorConstructor.get(mapTypeToken);
    LinkedHashMap<String, Integer> instanceMap = mapConstructor.construct();
    Assert.assertTrue(instanceMap.isEmpty());

    // Test getting an instance of a nested type
    TypeToken<TreeSet<LinkedList<String>>> nestedTypeToken = new TypeToken<TreeSet<LinkedList<String>>>() {};
    ObjectConstructor<TreeSet<LinkedList<String>>> nestedConstructor = constructorConstructor.get(nestedTypeToken);
    TreeSet<LinkedList<String>> nestedInstance = nestedConstructor.construct();
    Assert.assertTrue(nestedInstance.isEmpty());

    // Test getting an instance of an interface type (ArrayList)
    TypeToken<ArrayList<?>> interfaceTypeToken = new TypeToken<ArrayList<?>>() {};
    ObjectConstructor<ArrayList<?>> interfaceConstructor = constructorConstructor.get(interfaceTypeToken);
    ArrayList<?> interfaceInstance = interfaceConstructor.construct();
    Assert.assertTrue(interfaceInstance.isEmpty());

    // Test getting an instance of an enum set type (EnumSet.noneOf(String.class))
    Type enumSetType = new TypeToken<EnumSet<String>>() {}.getType();
    ObjectConstructor<EnumSet<String>> enumSetConstructor = constructorConstructor.get(new TypeToken<EnumSet<String>>() {}.getType());
    EnumSet<String> enumSetInstance = enumSetConstructor.construct();
    Assert.assertTrue(enumSetInstance.isEmpty());

    // Test getting an instance of a raw type
    ObjectConstructor<String[]> rawTypeConstructor = constructorConstructor.get(new TypeToken<String[]>() {}.getType());
    String[] rawTypeInstance = rawTypeConstructor.construct();
    Assert.assertNotNull(rawTypeInstance);
    Assert.assertEquals(0, rawTypeInstance.length);

    // Test getting an instance of a primitive type (int)
    TypeToken<Integer> primitiveTypeToken = new TypeToken<Integer>() {};
    ObjectConstructor<Integer> primitiveConstructor = constructorConstructor.get(primitiveTypeToken);
    Integer primitiveInstance = primitiveConstructor.construct();
    Assert.assertEquals(0, primitiveInstance.intValue());

    // Test getting an instance of a class with a private constructor
    class PrivateConstructorClass {
      private PrivateConstructorClass() {}
    }
    TypeToken<PrivateConstructorClass> privateConstructorClassToken = new TypeToken<PrivateConstructorClass>() {};
    ObjectConstructor<PrivateConstructorClass> privateConstructorClassConstructor = constructorConstructor.get(privateConstructorClassToken);
    PrivateConstructorClass privateConstructorClassInstance = privateConstructorClassConstructor.construct();
    Assert.assertNotNull(privateConstructorClassInstance);

    // Test getting an instance of a class with a protected constructor
    class ProtectedConstructorClass {
      protected ProtectedConstructorClass() {}
    }
    TypeToken<ProtectedConstructorClass> protectedConstructorClassToken = new TypeToken<ProtectedConstructorClass>() {};
    ObjectConstructor<ProtectedConstructorClass> protectedConstructorClassConstructor = constructorConstructor.get(protectedConstructorClassToken);
    ProtectedConstructorClass protectedConstructorClassInstance = protectedConstructorClassConstructor.construct();
    Assert.assertNotNull(protectedConstructorClassInstance);

    // Test getting an instance of a class with a package-private constructor
    class PackagePrivateConstructorClass {
      PackagePrivateConstructorClass() {}
    }
    TypeToken<PackagePrivateConstructorClass> packagePrivateConstructorClassToken = new TypeToken<PackagePrivateConstructorClass>() {};
    ObjectConstructor<PackagePrivateConstructorClass> packagePrivateConstructorClassConstructor = constructorConstructor.get(packagePrivateConstructorClassToken);
    PackagePrivateConstructorClass packagePrivateConstructorClassInstance = packagePrivateConstructorClassConstructor.construct();
    Assert.assertNotNull(packagePrivateConstructorClassInstance);

    // Test getting an instance of a class with a final constructor
    final class FinalConstructorClass {
      FinalConstructorClass() {}
    }
    TypeToken<FinalConstructorClass> finalConstructorClassToken = new TypeToken<FinalConstructorClass>() {};
    ObjectConstructor<FinalConstructorClass> finalConstructorClassConstructor = constructorConstructor.get(finalConstructorClassToken);
    FinalConstructorClass finalConstructorClassInstance = finalConstructorClassConstructor.construct();
    Assert.assertNotNull(finalConstructorClassInstance);

    // Test getting an instance of a class with a static constructor
    class StaticConstructorClass {
      static StaticConstructorClass() {}
    }
    TypeToken<StaticConstructorClass> staticConstructorClassToken = new TypeToken<StaticConstructorClass>() {};
    ObjectConstructor<StaticConstructorClass> staticConstructorClassConstructor = constructorConstructor.get(staticConstructorClassToken);
    StaticConstructorClass staticConstructorClassInstance = staticConstructorClassConstructor.construct();
    Assert.assertNotNull(staticConstructorClassInstance);

    // Test getting an instance of a class with a constructor that throws an exception
    class ThrowingConstructorClass {
      ThrowingConstructorClass(int i) {
        throw new RuntimeException("ConstructorException");
      }
    }
    TypeToken<ThrowingConstructorClass> throwingConstructorClassToken = new TypeToken<ThrowingConstructorClass>() {};
    ObjectConstructor<ThrowingConstructorClass> throwingConstructorClassConstructor = constructorConstructor.get(throwingConstructorClassToken);
    try {
      ThrowingConstructorClass throwingConstructorClassInstance = throwingConstructorClassConstructor.construct();
      Assert.fail();
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test(expected = RuntimeException.class)
  public void testGettingInstanceOfClassWithThrowingConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<ThrowingConstructorClass> throwingConstructorClassToken = new TypeToken<ThrowingConstructorClass>() {};
    ObjectConstructor<ThrowingConstructorClass> throwingConstructorClassConstructor = constructorConstructor.get(throwingConstructorClassToken);
    throwingConstructorClassConstructor.construct();
  }

  @Test
  public void testGetInstanceFromRawType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Type stringType = String.class;
    ObjectConstructor<String> rawStringConstructor = constructorConstructor.get(new TypeToken<String>() {}.getType());
    String rawStringInstance = ((ObjectConstructor<String>) constructorConstructor.get(stringType)).construct();
    Assert.assertEquals("", rawStringInstance);

    Type intType = int.class;
    ObjectConstructor<Integer> rawIntConstructor = constructorConstructor.get(new TypeToken<Integer>() {}.getType());
    Integer rawIntInstance = ((ObjectConstructor<Integer>) constructorConstructor.get(intType)).construct();
    Assert.assertEquals(0, rawIntInstance.intValue());

    Type arrayType = new TypeToken<String[]>() {}.getType();
    ObjectConstructor<String[]> rawArrayConstructor = constructorConstructor.get(arrayType);
    String[] rawArrayInstance = rawArrayConstructor.construct();
    Assert.assertNotNull(rawArrayInstance);
    Assert.assertEquals(0, rawArrayInstance.length);
  }

  @Test
  public void testGetInstanceFromNullInstanceCreatorsMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(stringTypeToken);
    try {
      String instance = stringConstructor.construct();
      Assert.fail();
    } catch (NullPointerException e) {
      // Expected
    }
  }
}