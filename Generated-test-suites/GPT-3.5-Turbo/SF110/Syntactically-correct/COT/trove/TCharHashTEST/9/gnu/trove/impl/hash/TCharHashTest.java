import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.impl.hash.TCharHash;

public class TCharHashTest {
  
  private TCharHash charHash;
  
  @Before
  public void setUp() {
    charHash = new TCharHash();
  }
  
  @Test
  public void testContains() {
    assertFalse(charHash.contains('a'));
    charHash.insertKey('a');
    assertTrue(charHash.contains('a'));
  }
  
  @Test
  public void testForEach() {
    charHash.insertKey('a');
    charHash.insertKey('b');
    charHash.insertKey('c');
    StringBuilder result = new StringBuilder();
    charHash.forEach((val) -> {
      result.append(val);
      return true;
    });
    assertEquals("abc", result.toString());
  }
  
  @Test
  public void testRemoveAt() {
    charHash.insertKey('a');
    charHash.insertKey('b');
    charHash.insertKey('c');
    charHash.removeAt(0);
    assertFalse(charHash.contains('a'));
    assertTrue(charHash.contains('b'));
    assertTrue(charHash.contains('c'));
  }
  
  @Test
  public void testInsertKey() {
    charHash.insertKey('a');
    assertTrue(charHash.contains('a'));
    charHash.insertKey('b');
    assertTrue(charHash.contains('b'));
  }
}