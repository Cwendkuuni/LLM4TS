import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

  @Test
  public void testAnswer_forToString() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(String.class.getMethod("toString"));
    when(invocation.getMock()).thenReturn(mock(Object.class));
    
    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof String);
    assertFalse(result.toString().isEmpty());
  }

  @Test
  public void testAnswer_forCompareTo_whenReferencesAreSame() {
    // Setup
    String obj = "test";
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
    when(invocation.getMock()).thenReturn(obj);
    when(invocation.getArguments()).thenReturn(new Object[]{obj});

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof Integer);
    assertEquals(0, (int) result);
  }

  @Test
  public void testAnswer_forCompareTo_whenReferencesAreDifferent() {
    // Setup
    String obj1 = "test1";
    String obj2 = "test2";
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
    when(invocation.getMock()).thenReturn(obj1);
    when(invocation.getArguments()).thenReturn(new Object[]{obj2});

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof Integer);
    assertEquals(1, (int) result);
  }

  @Test
  public void testAnswer_forPrimitiveWrapperType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(Integer.class.getMethod("intValue"));
    when(invocation.getMock()).thenReturn(mock(Integer.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof Integer);
    assertEquals(0, (int) result);
  }

  @Test
  public void testAnswer_forPrimitiveType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(int.class.getMethod("intValue"));
    when(invocation.getMock()).thenReturn(mock(int.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof Integer);
    assertEquals(0, (int) result);
  }

  @Test
  public void testAnswer_forCollectionType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(Collection.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(Collection.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof LinkedList);
  }

  @Test
  public void testAnswer_forSetType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(Set.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(Set.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof HashSet);
  }

  @Test
  public void testAnswer_forHashSetType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(HashSet.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(HashSet.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof HashSet);
  }

  @Test
  public void testAnswer_forSortedSetType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(SortedSet.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(SortedSet.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof TreeSet);
  }

  @Test
  public void testAnswer_forTreeSetType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(TreeSet.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(TreeSet.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof TreeSet);
  }

  @Test
  public void testAnswer_forLinkedHashSetType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(LinkedHashSet.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(LinkedHashSet.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof LinkedHashSet);
  }

  @Test
  public void testAnswer_forListType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(List.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(List.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof LinkedList);
  }

  @Test
  public void testAnswer_forLinkedListType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(LinkedList.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(LinkedList.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof LinkedList);
  }

  @Test
  public void testAnswer_forArrayListType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(ArrayList.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(ArrayList.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof ArrayList);
  }

  @Test
  public void testAnswer_forMapType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(Map.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(Map.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof HashMap);
  }

  @Test
  public void testAnswer_forHashMapType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(HashMap.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(HashMap.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof HashMap);
  }

  @Test
  public void testAnswer_forSortedMapType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(SortedMap.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(SortedMap.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof TreeMap);
  }

  @Test
  public void testAnswer_forTreeMapType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(TreeMap.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(TreeMap.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof TreeMap);
  }

  @Test
  public void testAnswer_forLinkedHashMapType() {
    // Setup
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(LinkedHashMap.class.getMethod("isEmpty"));
    when(invocation.getMock()).thenReturn(mock(LinkedHashMap.class));

    // Execute
    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    Object result = returnsEmptyValues.answer(invocation);

    // Verify
    assertNotNull(result);
    assertTrue(result instanceof LinkedHashMap);
  }
}