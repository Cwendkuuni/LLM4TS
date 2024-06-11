import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.unbounded.UnsafeAllocator;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorConstructorTest {

  @InjectMocks private ConstructorConstructor constructorConstructor;
  @SuppressWarnings("unchecked")
  private final Map<Type, InstanceCreator<?>> instanceCreators = Mockito.mock(Map.class);
  private static final UnsafeAllocator UNSAFE_ALLOCATOR = mock(UnsafeAllocator.class);

  @Test
  public void testGetInstanceCreatorForType() {
    when(instanceCreators.get(any(Type.class))).thenReturn(mock(InstanceCreator.class));

    TypeType typeToken = new TypeToken<Map<String, Integer>>(){};
    ObjectConstructor<Map<String, Integer>> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    verify(instanceCreators).get(eq(typeToken.getType()));
  }

  @Test
  public void testGetInstanceCreatorForRawType() {
    when(instanceCreators.get(any(Class.class))).thenReturn(mock(InstanceCreator.class));

    TypeType typeToken = new TypeToken<Map<String, Integer>>(){};
    ObjectConstructor<Map<String, Integer>> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    verify(instanceCreators).get(eq(Map.class));
  }

  @Test
  public void testGetDefaultConstructor() {
    when(instanceCreators.isEmpty()).thenReturn(true);

    ObjectConstructor<String> stringConstructor = constructorConstructor.get(TypeToken.STRING_TYPE);

    assertNotNull(stringConstructor);
  }

  @Test
  public void testGetDefaultImplementationConstructorForMap() {
    when(instanceCreators.isEmpty()).thenReturn(true);

    ObjectConstructor<Map<String, Integer>> mapConstructor = constructorConstructor.get( new TypeToken<Map<String, Integer>>(){});

    assertNotNull(mapConstructor);
    verify(instanceCreators).get(eq(Map.class));
  }

  @Test
  public void testGetDefaultImplementationConstructorForCollection() {
    when(instanceCreators.isEmpty()).thenReturn(true);

    ObjectConstructor<ArrayList<Integer>> listConstructor = constructorConstructor.get( new TypeToken<ArrayList<Integer>>(){});

    assertNotNull(listConstructor);
    verify(instanceCreators).get(eq(ArrayList.class));
  }

  @Test(expected = JsonIOException.class)
  public void testGetDefaultImplementationConstructorForInvalidEnumSet() {
    when(instanceCreators.isEmpty()).thenReturn(true);

    ObjectConstructor<EnumSet<Integer>> enumSetConstructor = constructorConstructor.get( new TypeToken<EnumSet<Integer>>(){});

    assertNull(enumSetConstructor);
  }

  @Test
  public void testGetDefaultImplementationConstructorForValidEnumSet() {
    when(instanceCreators.isEmpty()).thenReturn(true);

    ObjectConstructor<EnumSet<MyEnum>> enumSetConstructor = constructorConstructor.get( new TypeToken<EnumSet<MyEnum>>(){});

    assertNotNull(enumSetConstructor);
    verify(instanceCreators).get(eq(EnumSet.class));
  }

  @Test
  public void testGetUnsafeConstructor() {
    when(instanceCreators.isEmpty()).thenReturn(true);

    ObjectConstructor<Map<String, Integer>> mapConstructor = constructorConstructor.get( new TypeToken<Map<String, Integer>>(){});

    assertNotNull(mapConstructor);
    verify(instanceCreators).isEmpty();
    verify(UNSAFE_ALLOCATOR).newInstance(eq(Map.class));
  }

  @Test(expected = RuntimeException.class)
  public void testGetUnsafeConstructorForUnsupportedClass() {
    when(instanceCreators.isEmpty()).thenReturn(true);

    ObjectConstructor<String> stringConstructor = constructorConstructor.get(TypeToken.STRING_TYPE);

    assertNull(stringConstructor);
    verify(UNSAFE_ALLOCATOR).newInstance(eq(String.class));
  }
}