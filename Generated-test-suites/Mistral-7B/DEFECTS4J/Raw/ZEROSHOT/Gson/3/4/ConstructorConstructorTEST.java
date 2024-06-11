import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

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
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
public class ConstructorConstructorTest {

  @Test
  public void testGetConstructor() throws Exception {
    // Given
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<String> stringCreator = Mockito.mock(InstanceCreator.class);
    PowerMockito.whenNew(String.class).withNoArguments().thenReturn(new String());
    instanceCreators.put(String.TYPE, stringCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    // When
    TypeToken<String> stringToken = new TypeToken<String>() {};
    ObjectConstructor<String> constructor = constructorConstructor.get(stringToken);

    // Then
    assertNotNull(constructor);
    assertNotNull(constructor.construct());

    // Cleanup
    PowerMockito.verifyNew(String.class).withNoArguments();
    Mockito.verify(stringCreator).createInstance(String.TYPE);
  }

  @Test
  public void testGetConstructorForRawType() throws Exception {
    // Given
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<ArrayList<String>> arrayListCreator = Mockito.mock(InstanceCreator.class);
    PowerMockito.whenNew(ArrayList.class).withGenericType(ArrayList.class, String.class).withNoArguments()
        .thenReturn(new ArrayList<>());
    instanceCreators.put(ArrayList.class, arrayListCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    // When
    TypeToken<List<String>> listToken = new TypeToken<List<String>>() {};
    ObjectConstructor<List<String>> constructor = constructorConstructor.get(listToken);

    // Then
    assertNotNull(constructor);
    assertNotNull(constructor.construct());

    // Cleanup
    Mockito.verifyNew(ArrayList.class).withGenericType(ArrayList.class, String.class).withNoArguments();
    Mockito.verify(arrayListCreator).createInstance(ArrayList.class);
  }

  @Test(expected = RuntimeException.class)
  public void testGetConstructorForUnknownType() throws Exception {
    // Given
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    // When
    TypeToken<UnknownType> unknownTypeToken = new TypeToken<UnknownType>() {};
    ObjectConstructor<UnknownType> constructor = constructorConstructor.get(unknownTypeToken);

    // Then (should throw an exception)
  }

  @Test
  public void testGetConstructorForDefaultConstructor() throws Exception {
    // Given
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    Class<String> stringClass = String.class;
    Constructor<String> stringConstructor = stringClass.getDeclaredConstructor();
    InstanceCreator<String> stringCreator = mockInstanceCreator(stringConstructor);
    instanceCreators.put(String.TYPE, stringCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    // When
    TypeToken<String> stringToken = new TypeToken<String>() {};
    ObjectConstructor<String> constructor = constructorConstructor.get(stringToken);

    // Then
    assertNotNull(constructor);
    assertNotNull(constructor.construct());

    // Cleanup
    Mockito.verify(stringCreator).createInstance(String.TYPE);
  }

  @Test
  public void testGetConstructorForDefaultImplementation() throws Exception {
    // Given
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    Class<ArrayList<String>> arrayListClass = ArrayList.class;
    Constructor<ArrayList<String>> arrayListConstructor = arrayListClass.getDeclaredConstructor();
    InstanceCreator<ArrayList<String>> arrayListCreator = mockInstanceCreator(arrayListConstructor);
    instanceCreators.put(ArrayList.class, arrayListCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    // When
    TypeToken<List<String>> listToken = new TypeToken<List<String>>() {};
    ObjectConstructor<List<String>> constructor = constructorConstructor.get(listToken);

    // Then
    assertNotNull(constructor);
    assertNotNull(constructor.construct());

    // Cleanup
    Mockito.verify(arrayListCreator).createInstance(ArrayList.class);
  }

  @Test
  public void testGetConstructorForUnsafeAllocator() throws Exception {
    // Given
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    Class<TreeMap<String, Object>> treeMapClass = TreeMap.class;
    Constructor<TreeMap<String, Object>> treeMapConstructor = treeMapClass.getDeclaredConstructor();
    InstanceCreator<TreeMap<String, Object>> treeMapCreator = mockInstanceCreator(treeMapConstructor);
    instanceCreators.put(TreeMap.class, treeMapCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    // When
    TypeToken<SortedMap<String, Object>> sortedMapToken = new TypeToken<SortedMap<String, Object>>() {};
    ObjectConstructor<SortedMap<String, Object>> constructor = constructorConstructor.get(sortedMapToken);

    // Then
    assertNotNull(constructor);
    assertNotNull(constructor.construct());

    // Cleanup
    Mockito.verify(treeMapCreator).createInstance(TreeMap.class);
  }

  @Test(expected = RuntimeException.class)
  public void testGetConstructorForUnknownDefaultImplementation() throws Exception {
    // Given
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    // When
    TypeToken<UnknownDefaultImplementationType> unknownDefaultImplementationTypeToken =
        new TypeToken<UnknownDefaultImplementationType>() {};
    ObjectConstructor<UnknownDefaultImplementationType> constructor = constructorConstructor.get(unknownDefaultImplementationTypeToken);

    // Then (should throw an exception)
  }

  private InstanceCreator<?> mockInstanceCreator(Constructor<?> constructor) throws IllegalAccessException,
      InstantiationException, NoSuchMethodException, InvocationTargetException {
    // Given
    InstanceCreator<?> instanceCreator = Mockito.mock(InstanceCreator.class);
    PowerMockito.when(instanceCreator.createInstance(constructor.getType())).thenReturn(constructor.newInstance());
    return instanceCreator;
  }
}