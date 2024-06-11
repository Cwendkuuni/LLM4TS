
import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

  ReturnsEmptyValues rev = new ReturnsEmptyValues();

  @Test
  public void testAnswer_toString() {
    String actualResult = (String) rev.answer(null);
    assertNotNull(actualResult);
    String expectedPrefix = "Mock for ";
    assertTrue(actualResult.startsWith(expectedPrefix));
  }

  @Test
  public void testAnswer_compareTo() {
    int actualResult = (int) rev.answer(null);
    assertEquals(1, actualResult);
  }

  @Test
  public void testReturnValueFor_primitiveOrWrapper() {
    Object actualResult = rev.returnValueFor(Integer.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof Integer);
  }

  @Test
  public void testReturnValueFor_Collection() {
    Object actualResult = rev.returnValueFor(Collection.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof LinkedList);
  }

  @Test
  public void testReturnValueFor_Set() {
    Object actualResult = rev.returnValueFor(Set.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof HashSet);
  }

  @Test
  public void testReturnValueFor_HashSet() {
    Object actualResult = rev.returnValueFor(HashSet.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof HashSet);
  }

  @Test
  public void testReturnValueFor_SortedSet() {
    Object actualResult = rev.returnValueFor(SortedSet.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof TreeSet);
  }

  @Test
  public void testReturnValueFor_TreeSet() {
    Object actualResult = rev.returnValueFor(TreeSet.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof TreeSet);
  }

  @Test
  public void testReturnValueFor_LinkedHashSet() {
    Object actualResult = rev.returnValueFor(LinkedHashSet.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof LinkedHashSet);
  }

  @Test
  public void testReturnValueFor_List() {
    Object actualResult = rev.returnValueFor(List.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof LinkedList);
  }

  @Test
  public void testReturnValueFor_LinkedList() {
    Object actualResult = rev.returnValueFor(LinkedList.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof LinkedList);
  }

  @Test
  public void testReturnValueFor_ArrayList() {
    Object actualResult = rev.returnValueFor(ArrayList.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof ArrayList);
  }

  @Test
  public void testReturnValueFor_Map() {
    Object actualResult = rev.returnValueFor(Map.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof HashMap);
  }

  @Test
  public void testReturnValueFor_HashMap() {
    Object actualResult = rev.returnValueFor(HashMap.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof HashMap);
  }

  @Test
  public void testReturnValueFor_SortedMap() {
    Object actualResult = rev.returnValueFor(SortedMap.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof TreeMap);
  }

  @Test
  public void testReturnValueFor_TreeMap() {
    Object actualResult = rev.returnValueFor(TreeMap.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof TreeMap);
  }

  @Test
  public void testReturnValueFor_LinkedHashMap() {
    Object actualResult = rev.returnValueFor(LinkedHashMap.class);
    assertNotNull(actualResult);
    assertTrue(actualResult instanceof LinkedHashMap);
  }

}
