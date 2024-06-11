import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {
  
  @Test
  public void testToString() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    String expected = "Mock for Object, hashCode: " + rev.hashCode();
    assertEquals(expected, rev.answer(null));
  }
  
  @Test
  public void testCompareToSameReference() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertEquals(0, rev.answer(generateInvocationWithArguments(rev)));
  }
  
  @Test
  public void testCompareToDifferentReference() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertEquals(1, rev.answer(generateInvocationWithArguments(new Object())));
  }
  
  @Test
  public void testReturnValueForPrimitives() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertEquals(0, rev.answer(generateInvocationWithReturnType(int.class)));
    assertEquals(0L, rev.answer(generateInvocationWithReturnType(long.class)));
    assertEquals(0.0f, rev.answer(generateInvocationWithReturnType(float.class)));
    assertEquals(0.0, rev.answer(generateInvocationWithReturnType(double.class)));
    assertEquals(false, rev.answer(generateInvocationWithReturnType(boolean.class)));
    assertEquals('\0', rev.answer(generateInvocationWithReturnType(char.class)));
    assertNull(rev.answer(generateInvocationWithReturnType(void.class)));
  }
  
  @Test
  public void testReturnValueForPrimitiveWrappers() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertEquals(Integer.valueOf(0), rev.answer(generateInvocationWithReturnType(Integer.class)));
    assertEquals(Long.valueOf(0), rev.answer(generateInvocationWithReturnType(Long.class)));
    assertEquals(Float.valueOf(0), rev.answer(generateInvocationWithReturnType(Float.class)));
    assertEquals(Double.valueOf(0), rev.answer(generateInvocationWithReturnType(Double.class)));
    assertEquals(Boolean.FALSE, rev.answer(generateInvocationWithReturnType(Boolean.class)));
    assertEquals(Character.valueOf('\0'), rev.answer(generateInvocationWithReturnType(Character.class)));
  }
  
  @Test
  public void testReturnValueForCollection() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(Collection.class)) instanceof LinkedList);
  }
  
  @Test
  public void testReturnValueForSet() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(Set.class)) instanceof HashSet);
  }
  
  @Test
  public void testReturnValueForHashSet() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(HashSet.class)) instanceof HashSet);
  }
  
  @Test
  public void testReturnValueForSortedSet() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(SortedSet.class)) instanceof TreeSet);
  }
  
  @Test
  public void testReturnValueForTreeSet() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(TreeSet.class)) instanceof TreeSet);
  }
  
  @Test
  public void testReturnValueForLinkedHashSet() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(LinkedHashSet.class)) instanceof LinkedHashSet);
  }
  
  @Test
  public void testReturnValueForList() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(List.class)) instanceof LinkedList);
  }
  
  @Test
  public void testReturnValueForLinkedList() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(LinkedList.class)) instanceof LinkedList);
  }
  
  @Test
  public void testReturnValueForArrayList() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(ArrayList.class)) instanceof ArrayList);
  }
  
  @Test
  public void testReturnValueForMap() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(Map.class)) instanceof HashMap);
  }
  
  @Test
  public void testReturnValueForHashMap() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(HashMap.class)) instanceof HashMap);
  }
  
  @Test
  public void testReturnValueForSortedMap() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(SortedMap.class)) instanceof TreeMap);
  }
  
  @Test
  public void testReturnValueForTreeMap() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(TreeMap.class)) instanceof TreeMap);
  }
  
  @Test
  public void testReturnValueForLinkedHashMap() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertTrue(rev.answer(generateInvocationWithReturnType(LinkedHashMap.class)) instanceof LinkedHashMap);
  }
  
  // Helper methods for generating InvocationOnMock objects
  
  private InvocationOnMock generateInvocationWithArguments(Object... arguments) {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getArguments()).thenReturn(arguments);
    return invocation;
  }
  
  private InvocationOnMock generateInvocationWithReturnType(Class<?> returnType) {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Method method = mock(Method.class);
    when(invocation.getMethod()).thenReturn(method);
    when(method.getReturnType()).thenReturn(returnType);
    return invocation;
  }
}