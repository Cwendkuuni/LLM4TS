import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {
  
  @Test
  public void testContains() {
    TCharHash hash = new TCharHash();
    hash.insertKey('a');
    hash.insertKey('b');
    hash.insertKey('c');
    
    assertTrue(hash.contains('a'));
    assertTrue(hash.contains('b'));
    assertTrue(hash.contains('c'));
    assertFalse(hash.contains('d'));
  }
  
  @Test
  public void testForEach() {
    TCharHash hash = new TCharHash();
    hash.insertKey('a');
    hash.insertKey('b');
    hash.insertKey('c');
    
    final StringBuilder result = new StringBuilder();
    TCharProcedure proc = new TCharProcedure() {
        public boolean execute(char value) {
            result.append(value);
            return true;
        }
    };
    
    assertTrue(hash.forEach(proc));
    assertEquals("abc", result.toString());
  }
  
  @Test
  public void testRemoveAt() {
    TCharHash hash = new TCharHash();
    hash.insertKey('a');
    hash.insertKey('b');
    hash.insertKey('c');
    
    hash.removeAt(1);
    
    assertTrue(hash.contains('a'));
    assertFalse(hash.contains('b'));
    assertTrue(hash.contains('c'));
  }
  
  @Test
  public void testInsertKey() {
    TCharHash hash = new TCharHash();
    int index = hash.insertKey('a');
    
    assertTrue(hash.contains('a'));
    assertEquals(0, index);
    
    index = hash.insertKey('b');
    
    assertTrue(hash.contains('b'));
    assertEquals(1, index);
    
    index = hash.insertKey('c');
    
    assertTrue(hash.contains('c'));
    assertEquals(2, index);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testInsertKey_full() {
    TCharHash hash = new TCharHash(3);
    hash.insertKey('a');
    hash.insertKey('b');
    hash.insertKey('c');
    hash.insertKey('d');
  }
  
}