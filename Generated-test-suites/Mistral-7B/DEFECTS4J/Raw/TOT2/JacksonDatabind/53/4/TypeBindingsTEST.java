import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class TypeBindingsTest {

 @Test
 public void testEmptyBindings() {
 TypeBindings empty = TypeBindings.emptyBindings();
 assertEquals("", empty.toString());
 assertEquals(0, empty.size());
 assertFalse(empty.hasUnbound("T"));
 }

 @Test
 public void testCreateWithNoTypes() {
 Class<List<Object>> clazz = new ArrayList<Object>().getClass();
 TypeBindings bindings = TypeBindings.create(clazz, null);
 assertEquals("List<T>", bindings.toString());
 assertEquals(1, bindings.size());
 assertFalse(bindings.hasUnbound("T"));
 }

 @Test
 public void testCreateWithOneType() {
 Class<List<Integer>> clazz = new ArrayList<Integer>().getClass();
 TypeBindings bindings = TypeBindings.create(clazz, TypeFactory.defaultInstance.constructType(Integer.TYPE));
 assertEquals("List<Integer>", bindings.toString());
 assertEquals(1, bindings.size());
 assertFalse(bindings.hasUnbound("T"));
 }

 @Test
 public void testCreateWithTwoTypes() {
 Class<Map<String, List<Integer>>> clazz = new HashMap<String, ArrayList<Integer>>().getClass();
 TypeBindings bindings = TypeBindings.create(clazz, TypeFactory.defaultInstance.constructType(String.class), TypeFactory.defaultInstance.constructType(List.class));
 assertEquals("Map<K, List<V>>", bindings.toString());
 assertEquals(2, bindings.size());
 assertFalse(bindings.hasUnbound("K"));
 assertFalse(bindings.hasUnbound("V"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithMismatchedTypes() {
 Class<Map<String, Integer>> clazz = new HashMap<String, Integer>().getClass();
 TypeBindings bindings = TypeBindings.create(clazz, TypeFactory.defaultInstance.constructType(List.class));
 }

 @Test
 public void testCreateWithUnboundTypes() {
 Class<Map<String, List<Integer>>> clazz = new HashMap<String, ArrayList<Integer>>().getClass();
 TypeBindings bindings = TypeBindings.create(clazz, null);
 TypeBindings bindingsWithT = bindings.withUnboundVariable("T");
 assertEquals("Map<K, List<T>>", bindingsWithT.toString());
 assertTrue(bindingsWithT.hasUnbound("T"));
 }

 @Test
 public void testFindBoundType() {
 TypeBindings bindings = TypeBindings.create(String[].class, String.TYPE, String.TYPE);
 assertEquals(String.TYPE, bindings.findBoundType("T1"));
 assertEquals(String.TYPE, bindings.findBoundType("T2"));
 assertEquals(null, bindings.findBoundType("T3"));
 }

 @Test
 public void testIsEmpty() {
 TypeBindings empty = TypeBindings.emptyBindings();
 assertTrue(empty.isEmpty());
 TypeBindings notEmpty = TypeBindings.create(List.class, Integer.TYPE);
 assertFalse(notEmpty.isEmpty());
 }

}