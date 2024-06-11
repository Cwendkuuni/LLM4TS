import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

   @Test
   public void testAnswer_toStringMethod() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new Object().getClass().getMethod("toString");
      Mockito.when(invocation.getMethod()).thenReturn(method);
      String result = returnsEmptyValues.answer(invocation);
      assertTrue(result.startsWith("Mock for"));
      assertTrue(result.contains(", hashCode:"));
   }

   @Test
   public void testAnswer_compareToMethod_sameReference() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new Object().getClass().getMethod("compareTo", Object.class);
      Mockito.when(invocation.getMethod()).thenReturn(method);
      Object argument = new Object();
      Mockito.when(invocation.getArguments()).thenReturn(new Object[] {argument});
      Integer result = returnsEmptyValues.answer(invocation);
      assertEquals(Integer.valueOf(0), result);
   }

   @Test
   public void testAnswer_compareToMethod_differentReference() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new Object().getClass().getMethod("compareTo", Object.class);
      Mockito.when(invocation.getMethod()).thenReturn(method);
      Object argument = new Object();
      Mockito.when(invocation.getArguments()).thenReturn(new Object[] {argument});
      Integer result = returnsEmptyValues.answer(invocation);
      assertEquals(Integer.valueOf(1), result);
   }

   @Test
   public void testAnswer_primitiveReturnMethod() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new ExampleClass().getClass().getMethod("getNumber");
      Mockito.when(invocation.getMethod()).thenReturn(method);
      int result = (int) returnsEmptyValues.answer(invocation);
      assertEquals(0, result);
   }

   @Test
   public void testAnswer_primitiveWrapperReturnMethod() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new ExampleClass().getClass().getMethod("getInteger");
      Mockito.when(invocation.getMethod()).thenReturn(method);
      Integer result = (Integer) returnsEmptyValues.answer(invocation);
      assertEquals(Integer.valueOf(0), result);
   }

   @Test
   public void testAnswer_collectionReturnMethod() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new ExampleClass().getClass().getMethod("getList");
      Mockito.when(invocation.getMethod()).thenReturn(method);
      List<Object> result = (List<Object>) returnsEmptyValues.answer(invocation);
      assertNotNull(result);
      assertTrue(result.isEmpty());
   }

   @Test
   public void testAnswer_setReturnMethod() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new ExampleClass().getClass().getMethod("getSet");
      Mockito.when(invocation.getMethod()).thenReturn(method);
      Set<Object> result = (Set<Object>) returnsEmptyValues.answer(invocation);
      assertNotNull(result);
      assertTrue(result.isEmpty());
   }

   @Test
   public void testAnswer_sortedSetReturnMethod() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new ExampleClass().getClass().getMethod("getSortedSet");
      Mockito.when(invocation.getMethod()).thenReturn(method);
      SortedSet<Object> result = (SortedSet<Object>) returnsEmptyValues.answer(invocation);
      assertNotNull(result);
      assertTrue(result.isEmpty());
   }

   @Test
   public void testAnswer_linkedHashSetReturnMethod() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new ExampleClass().getClass().getMethod("getLinkedHashSet");
      Mockito.when(invocation.getMethod()).thenReturn(method);
      LinkedHashSet<Object> result = (LinkedHashSet<Object>) returnsEmptyValues.answer(invocation);
      assertNotNull(result);
      assertTrue(result.isEmpty());
   }

   @Test
   public void testAnswer_mapReturnMethod() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new ExampleClass().getClass().getMethod("getMap");
      Mockito.when(invocation.getMethod()).thenReturn(method);
      Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.answer(invocation);
      assertNotNull(result);
      assertTrue(result.isEmpty());
   }

   @Test
   public void testAnswer_hashMapReturnMethod() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new ExampleClass().getClass().getMethod("getHashMap");
      Mockito.when(invocation.getMethod()).thenReturn(method);
      HashMap<Object, Object> result = (HashMap<Object, Object>) returnsEmptyValues.answer(invocation);
      assertNotNull(result);
      assertTrue(result.isEmpty());
   }

   @Test
   public void testAnswer_sortedMapReturnMethod() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new ExampleClass().getClass().getMethod("getSortedMap");
      Mockito.when(invocation.getMethod()).thenReturn(method);
      SortedMap<Object, Object> result = (SortedMap<Object, Object>) returnsEmptyValues.answer(invocation);
      assertNotNull(result);
      assertTrue(result.isEmpty());
   }

   @Test
   public void testAnswer_treeMapReturnMethod() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new ExampleClass().getClass().getMethod("getTreeMap");
      Mockito.when(invocation.getMethod()).thenReturn(method);
      TreeMap<Object, Object> result = (TreeMap<Object, Object>) returnsEmptyValues.answer(invocation);
      assertNotNull(result);
      assertTrue(result.isEmpty());
   }

   @Test
   public void testAnswer_linkedHashMapReturnMethod() {
      ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
      InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
      Method method = new ExampleClass().getClass().getMethod("getLinkedHashMap");
      Mockito.when(invocation.getMethod()).thenReturn(method);
      LinkedHashMap<Object, Object> result = (LinkedHashMap<Object, Object>) returnsEmptyValues.answer(invocation);
      assertNotNull(result);
      assertTrue(result.isEmpty());
   }
}