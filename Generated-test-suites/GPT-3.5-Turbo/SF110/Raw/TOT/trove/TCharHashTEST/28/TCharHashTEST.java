import gnu.trove.impl.hash.TCharHash;
import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {
  
  private TCharHash emptyHash;
  private TCharHash hashWithValues;
  
  public TCharHashTest() {
    emptyHash = new TCharHash();
    
    hashWithValues = new TCharHash(5);
    hashWithValues.insertKey('a');
    hashWithValues.insertKey('b');
    hashWithValues.insertKey('c');
  }
  
  @Test
  public void testConstructor1() {
    assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, emptyHash.getNoEntryValue());
    assertEquals(10, emptyHash.capacity());
    assertFalse(emptyHash.contains('a'));
  }
  
  @Test
  public void testConstructor2() {
    TCharHash hash = new TCharHash(20);
    assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    assertEquals(20, hash.capacity());
    assertFalse(hash.contains('a'));
  }
  
  @Test
  public void testConstructor3() {
    TCharHash hash = new TCharHash(20, 0.5f);
    assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    assertEquals(20, hash.capacity());
    assertFalse(hash.contains('a'));
  }
  
  @Test
  public void testConstructor4() {
    TCharHash hash = new TCharHash(20, 0.5f, 'x');
    assertEquals('x', hash.getNoEntryValue());
    assertEquals(20, hash.capacity());
    assertFalse(hash.contains('a'));
  }
  
  @Test
  public void testSetUp() {
    assertEquals(10, emptyHash.capacity());
    assertEquals(5, emptyHash.setUp(5));
    assertEquals(5, emptyHash.capacity());
  }
  
  @Test
  public void testContains() {
    assertFalse(emptyHash.contains('a'));
    assertTrue(hashWithValues.contains('a'));
  }
  
  @Test
  public void testForEach() {
    TCharProcedure procedure = new TCharProcedure() {
      public boolean execute(char value) {
        return value != 'b';
      }
    };
    
    assertTrue(emptyHash.forEach(procedure));
    assertFalse(hashWithValues.forEach(procedure));
  }
  
  @Test
  public void testRemoveAt() {
    hashWithValues.removeAt(1);
    assertFalse(hashWithValues.contains('b'));
  }
  
  @Test
  public void testIndex() {
    assertEquals(-1, emptyHash.index('a'));
    assertEquals(0, hashWithValues.index('a'));
  }
  
  @Test
  public void testIndexRehashed() {
    assertEquals(-1, emptyHash.indexRehashed('a', 0, 1, (byte)0));
    assertEquals(0, hashWithValues.indexRehashed('a', 0, 1, (byte)0));
  }
  
  @Test
  public void testInsertKey() {
    assertEquals(0, emptyHash.insertKey('a'));
    assertTrue(emptyHash.contains('a'));
  }
  
  @Test
  public void testInsertKeyRehash() {
    assertEquals(0, emptyHash.insertKeyRehash('a', 0, 1, (byte)0));
    assertTrue(emptyHash.contains('a'));
  }
  
  @Test
  public void testInsertKeyAt() {
    emptyHash.insertKeyAt(0, 'a');
    assertTrue(emptyHash.contains('a'));
  }
  
}