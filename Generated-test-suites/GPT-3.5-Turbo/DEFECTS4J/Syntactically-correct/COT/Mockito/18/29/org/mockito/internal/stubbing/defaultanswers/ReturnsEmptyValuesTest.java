
import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

  @Test
  public void testToString() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertEquals("Mock for Object, hashCode: " + rev.hashCode(), rev.answer(null));
  }

  @Test
  public void testCompareTo() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    assertEquals(0, rev.answer(createInvocation(rev, rev)));
    assertNotEquals(0, rev.answer(createInvocation(rev, new ReturnsEmptyValues())));
  }

  @Test
  public void testReturnValueFor_String() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(String.class);
    assertNull(actual);
  }

  @Test
  public void testReturnValueFor_Int() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(int.class);
    assertEquals(0, actual);
  }

  @Test
  public void testReturnValueFor_Integer() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(Integer.class);
    assertEquals(0, actual);
  }

  @Test
  public void testReturnValueFor_Collection() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(Collection.class);
    assertNotNull(actual);
    assertTrue(actual instanceof LinkedList);
  }

  @Test
  public void testReturnValueFor_Set() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(Set.class);
    assertNotNull(actual);
    assertTrue(actual instanceof HashSet);
  }

  @Test
  public void testReturnValueFor_HashSet() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(HashSet.class);
    assertNotNull(actual);
    assertTrue(actual instanceof HashSet);
  }

  @Test
  public void testReturnValueFor_SortedSet() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(SortedSet.class);
    assertNotNull(actual);
    assertTrue(actual instanceof TreeSet);
  }

  @Test
  public void testReturnValueFor_TreeSet() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(TreeSet.class);
    assertNotNull(actual);
    assertTrue(actual instanceof TreeSet);
  }

  @Test
  public void testReturnValueFor_LinkedHashSet() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(LinkedHashSet.class);
    assertNotNull(actual);
    assertTrue(actual instanceof LinkedHashSet);
  }

  @Test
  public void testReturnValueFor_List() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(List.class);
    assertNotNull(actual);
    assertTrue(actual instanceof LinkedList);
  }

  @Test
  public void testReturnValueFor_LinkedList() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(LinkedList.class);
    assertNotNull(actual);
    assertTrue(actual instanceof LinkedList);
  }

  @Test
  public void testReturnValueFor_ArrayList() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(ArrayList.class);
    assertNotNull(actual);
    assertTrue(actual instanceof ArrayList);
  }

  @Test
  public void testReturnValueFor_Map() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(Map.class);
    assertNotNull(actual);
    assertTrue(actual instanceof HashMap);
  }

  @Test
  public void testReturnValueFor_HashMap() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(HashMap.class);
    assertNotNull(actual);
    assertTrue(actual instanceof HashMap);
  }

  @Test
  public void testReturnValueFor_SortedMap() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(SortedMap.class);
    assertNotNull(actual);
    assertTrue(actual instanceof TreeMap);
  }

  @Test
  public void testReturnValueFor_TreeMap() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(TreeMap.class);
    assertNotNull(actual);
    assertTrue(actual instanceof TreeMap);
  }

  @Test
  public void testReturnValueFor_LinkedHashMap() {
    ReturnsEmptyValues rev = new ReturnsEmptyValues();
    Object actual = rev.returnValueFor(LinkedHashMap.class);
    assertNotNull(actual);
    assertTrue(actual instanceof LinkedHashMap);
  }
  
  private InvocationOnMock createInvocation(Object mock, Object argument) {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMock()).thenReturn(mock);
    when(invocation.getArguments()).thenReturn(new Object[]{argument});
    return invocation;
  }
}

