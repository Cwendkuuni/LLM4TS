import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorConstructorTest {

  @InjectMocks
  private ConstructorConstructor constructorConstructor;

  @Test
  public void testGetInstanceForStringType() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(stringTypeToken);
    assertNotNull(stringConstructor);
  }

  @Test
  public void testGetInstanceForArrayListType() {
    TypeToken<ArrayList<Integer>> listTypeToken = new TypeToken<ArrayList<Integer>>() {};
    ObjectConstructor<ArrayList<Integer>> listConstructor = constructorConstructor.get(listTypeToken);
    assertNotNull(listConstructor);
  }

  @Test
  public void testGetInstanceForLinkedListType() {
    TypeToken<LinkedList<Integer>> listTypeToken = new TypeToken<LinkedList<Integer>>() {};
    ObjectConstructor<LinkedList<Integer>> listConstructor = constructorConstructor.get(listTypeToken);
    assertNotNull(listConstructor);
  }

  @Test
  public void testGetInstanceForLinkedHashMapType() {
    TypeToken<LinkedHashMap<String, Object>> mapTypeToken = new TypeToken<LinkedHashMap<String, Object>>() {};
    ObjectConstructor<LinkedHashMap<String, Object>> mapConstructor = constructorConstructor.get(mapTypeToken);
    assertNotNull(mapConstructor);
  }

  @Test
  public void testGetInstanceForTreeMapType() {
    TypeToken<TreeMap<String, Object>> mapTypeToken = new TypeToken<TreeMap<String, Object>>() {};
    ObjectConstructor<TreeMap<String, Object>> mapConstructor = constructorConstructor.get(mapTypeToken);
    assertNotNull(mapConstructor);
  }

  @Test
  public void testGetInstanceForTreeSetType() {
    TypeToken<TreeSet<Integer>> setTypeToken = new TypeToken<TreeSet<Integer>>() {};
    ObjectConstructor<TreeSet<Integer>> setConstructor = constructorConstructor.get(setTypeToken);
    assertNotNull(setConstructor);
  }

  @Test
  public void testGetInstanceForEnumSetType() {
    TypeToken<EnumSet<Colors>> enumSetTypeToken = new TypeToken<EnumSet<Colors>>() {};
    ObjectConstructor<EnumSet<Colors>> enumSetConstructor = constructorConstructor.get(enumSetTypeToken);
    assertNotNull(enumSetConstructor);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceForInvalidType() {
    Type invalidType = new TypeToken<String>() {}.getType();
    constructorConstructor.get(new TypeToken<Object>() {}.getType());
  }

  @Test
  public void testInstanceCreatorIsUsedWhenAvailable() {
    InstanceCreator<ArrayList<String>> instanceCreator = Mockito.mock(InstanceCreator.class);
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    instanceCreators.put(new TypeToken<ArrayList<String>>() {}, instanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<ArrayList<String>> arrayListConstructor = constructorConstructor.get(
        new TypeToken<ArrayList<String>>() {});

    Mockito.verify(instanceCreator).createInstance(new TypeToken<ArrayList<String>>() {}.getType());
  }

  @Test
  public void testRawTypeIsUsedWhenInstanceCreatorIsNotFound() {
    InstanceCreator<ArrayList<String>> instanceCreator = Mockito.mock(InstanceCreator.class);
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    instanceCreators.put(String.class, instanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<ArrayList<String>> arrayListConstructor = constructorConstructor.get(
        new TypeToken<ArrayList<String>>() {});

    Mockito.verify(instanceCreator, Mockito.never()).createInstance(new TypeToken<ArrayList<String>>() {}.getType());
    Mockito.verify(instanceCreator).createInstance(String.class);
  }

  @Test
  public void testNewDefaultConstructorIsUsedWhenNoOtherConstructorIsAvailable() {
    Class<Object> objectClass = Object.class;
    ObjectConstructor<Object> objectConstructor = constructorConstructor.newDefaultConstructor(objectClass);

    assertNotNull(objectConstructor);
  }

  @Test
  public void testNewDefaultImplementationConstructorIsUsedForCommonTypes() {
    Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
    ObjectConstructor<ArrayList<Integer>> arrayListConstructor = constructorConstructor.newDefaultImplementationConstructor(
        new TypeToken<ArrayList<Integer>>() {}.getType(), arrayListClass);

    assertNotNull(arrayListConstructor);
  }

  @Test
  public void testNewUnsafeAllocatorIsUsedAsLastResort() {
    Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
    ObjectConstructor<ArrayList<Integer>> arrayListConstructor = constructorConstructor.newUnsafeAllocator(
        new TypeToken<ArrayList<Integer>>() {}.getType(), arrayListClass);

    assertNotNull(arrayListConstructor);
  }
}

enum Colors {
  RED, GREEN, BLUE
}