import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {
  
  @Test
  public void testContains() {
    TCharHash tCharHash = new TCharHash();
    tCharHash.insertKey('a');
    assertTrue(tCharHash.contains('a'));
    assertFalse(tCharHash.contains('b'));
  }
  
  @Test
  public void testForEach() {
    TCharHash tCharHash = new TCharHash();
    tCharHash.insertKey('a');
    tCharHash.insertKey('b');
    tCharHash.insertKey('c');
    TCharProcedure procedure = new TCharProcedure() {
      @Override
      public boolean execute(char value) {
        System.out.println(value);
        return true;
      }
    };
    assertTrue(tCharHash.forEach(procedure));
  }
  
  @Test
  public void testRemoveAt() {
    TCharHash tCharHash = new TCharHash();
    tCharHash.insertKey('a');
    tCharHash.insertKey('b');
    tCharHash.insertKey('c');
    tCharHash.removeAt(1);
    assertFalse(tCharHash.contains('b'));
  }
  
  @Test
  public void testInsertKey() {
    TCharHash tCharHash = new TCharHash();
    assertEquals(0, tCharHash.insertKey('a'));
    assertEquals(1, tCharHash.insertKey('b'));
    assertEquals(2, tCharHash.insertKey('c'));
    assertEquals(-2, tCharHash.insertKey('b'));
  }
}