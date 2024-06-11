import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
public class ConstructorConstructorTest {

 private Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
 private ConstructorConstructor constructorConstructor;

 @Test
 public void testGetInstanceCreatorForPrimitiveType() {
 final Type intType = TypeToken.get(int.class).getType();
 InstanceCreator<Integer> intCreator = Mockito.mock(InstanceCreator.class);
 PowerMockito.whenNew(InstanceCreator.class).withArguments(intType).thenReturn(intCreator);
 PowerMockito.when(intCreator.createInstance(intType)).thenReturn(1);

 constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<Integer> intConstructor = constructorConstructor.get(TypeToken.get(int.class));
 assertEquals(1, intConstructor.construct());
 }

 @Test
 public void testGetInstanceCreatorForClassType() {
 final Class<String> stringClass = String.class;
 InstanceCreator<String> stringCreator = Mockito.mock(InstanceCreator.class);
 PowerMockito.whenNew(InstanceCreator.class).withArguments(stringClass).thenReturn(stringCreator);
 PowerMockito.when(stringCreator.createInstance(stringClass)).thenReturn("test");

 constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<String> stringConstructor = constructorConstructor.get(TypeToken.get(String.class));
 assertEquals("test", stringConstructor.construct());
 }

 @Test
 public void testGetInstanceCreatorForRawType() {
 final Class<String> stringClass = String.class;
 InstanceCreator<String> stringCreator = Mockito.mock(InstanceCreator.class);
 PowerMockito.whenNew(InstanceCreator.class).withArguments(stringClass).thenReturn(stringCreator);
 PowerMockito.when(stringCreator.createInstance(String.class)).thenReturn("test");

 constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<Object> stringConstructor = constructorConstructor.get(TypeToken.get(Object.class));
 assertEquals("test", stringConstructor.construct());
 }

 @Test
 public void testGetInstanceCreatorForCollectionTypes() {
 final Type setOfStringType = TypeToken.getParameterized(Set.class, String.class).getType();
 InstanceCreator<Set<String>> setCreator = Mockito.mock(InstanceCreator.class);
 PowerMockito.whenNew(InstanceCreator.class).withArguments(setOfStringType).thenReturn(setCreator);
 PowerMockito.when(setCreator.createInstance(setOfStringType)).thenReturn(new TreeSet<String>());

 constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<Set<String>> setConstructor = constructorConstructor.get(TypeToken.getParameterized(Set.class, String.class));
 assertNotNull(setConstructor);
 }

 @Test
 public void testGetInstanceCreatorForMapTypes() {
 final Type mapOfStringToStringType = TypeToken.getParameterized(Map.class, String.class, String.class).getType();
 InstanceCreator<Map<String, String>> mapCreator = Mockito.mock(InstanceCreator.class);
 PowerMockito.whenNew(InstanceCreator.class).withArguments(mapOfStringToStringType).thenReturn(mapCreator);
 PowerMockito.when(mapCreator.createInstance(mapOfStringToStringType)).thenReturn(new LinkedTreeMap<String, String>());

 constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<Map<String, String>> mapConstructor = constructorConstructor.get(TypeToken.getParameterized(Map.class, String.class, String.class));
 assertNotNull(mapConstructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGetInstanceCreatorForUnknownTypes() {
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<Object> unknownTypeConstructor = constructorConstructor.get(TypeToken.get(Object.class));
 assertNull(unknownTypeConstructor);
 }
}