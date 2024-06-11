import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

import com.google.gson.InstanceCreator;
import com.google.gson.TypeToken;
import com.google.gson.reflect.TypeToken;

import static com.google.gson.internal.ConstructorConstructorTest.getTestClassInstance;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorConstructorTest {

 private static final ConstructorConstructor constructorConstructor = getTestClassInstance();

 @Test
 public void testGet_ValidTypeToken_ReturnsInstanceCreatorOrDefaultConstructor() {
 final Type type = new TypeToken<ArrayList<String>>() {}.getType();
 ObjectConstructor<ArrayList<String>> actual = constructorConstructor.get(new TypeToken<ArrayList<String>>() {});
 assertInstanceCreatorOrDefaultConstructor(actual, type);
 }

 @Test
 public void testGet_ValidRawType_ReturnsInstanceCreatorOrDefaultConstructor() {
 final Type type = new TypeToken<ArrayList<String>>() {}.getType();
 ObjectConstructor<ArrayList<String>> actual = constructorConstructor.get(new TypeToken<ArrayList<String>>() {}.getType());
 assertInstanceCreatorOrDefaultConstructor(actual, ArrayList.class);
 }

 @Test
 public void testGet_InvalidType_ReturnsNull() {
 final Type type = new TypeToken<ArrayList<String>>() {}.getType();
 ObjectConstructor<Boolean> actual = constructorConstructor.get(new TypeToken<Boolean>() {});
 assertNull(actual);
 }

 @Test
 public void testGet_NullTypeToken_ThrowsRuntimeException() {
 try {
 constructorConstructor.get(null);
 fail("Expected RuntimeException");
 } catch (RuntimeException e) {
 // expected
 }
 }

 @Test
 public void testGet_ParameterizedTypeWithoutRawType_ThrowsRuntimeException() {
 try {
 constructorConstructor.get(new TypeToken<Map<String, Set<String>>>() {});
 fail("Expected RuntimeException");
 } catch (RuntimeException e) {
 // expected
 }
 }

 @Test
 public void testGet_ParameterizedTypeWithRawType_ReturnsInstanceCreatorOrDefaultConstructor() {
 final Type type = new TypeToken<Map<String, Set<String>>>() {}.getType();
 ObjectConstructor<Map<String, Set<String>>> actual = constructorConstructor.get(new TypeToken<Map<String, Set<String>>>() {});
 assertInstanceCreatorOrDefaultConstructor(actual, type);
 }

 @Test
 public void testGet_CustomInstanceCreator_ReturnsCustomInstanceCreator() {
 InstanceCreator<ArrayList<String>> mockInstanceCreator = Mockito.mock(InstanceCreator.class);
 Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
 instanceCreators.put(new TypeToken<ArrayList<String>>() {}.getType(), mockInstanceCreator);
 ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
 ObjectConstructor<ArrayList<String>> actual = constructorConstructor.get(new TypeToken<ArrayList<String>>() {});
 assertEquals(mockInstanceCreator, actual);
 }

 @Test
 public void testGet_EmptyInstanceCreatorsMap_ThrowsRuntimeException() {
 try {
 new ConstructorConstructor(Collections.emptyMap());
 fail("Expected RuntimeException");
 } catch (RuntimeException e) {
 // expected
 }
 }

 @Test
 public void testToString_ReturnsInstanceCreatorsMapStringRepresentation() {
 assertEquals("{...}", constructorConstructor.toString());
 }

 private void assertInstanceCreatorOrDefaultConstructor(ObjectConstructor<?> actual, Type type) {
 if (actual == null) {
 fail("Expected non-null result");
 }
 if (actual instanceof InstanceCreatorWrapper) {
 InstanceCreator<?> instanceCreator = ((InstanceCreatorWrapper<?>) actual).instanceCreator;
 assertEquals(type, instanceCreator.getType());
 } else {
 assertNotNull(actual.construct());
 }
 }

 private static ConstructorConstructor getTestClassInstance() {
 return new ConstructorConstructor(null);
 }
}