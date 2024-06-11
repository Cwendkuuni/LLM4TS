import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {
  
  private TCharHash tCharHash;
  
  @Before
  public void setUp() {
    tCharHash = new TCharHashStub();
  }
  
  @Test
  public void testConstructor() {
    // Test default constructor
    assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    
    // Test constructor with initial capacity
    tCharHash = new TCharHashStub(10);
    assertEquals(10, tCharHash.capacity());
    assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    
    // Test constructor with initial capacity and load factor
    tCharHash = new TCharHashStub(10, 0.5f);
    assertEquals(10, tCharHash.capacity());
    assertEquals(0.5f, tCharHash.getLoadFactor(), 0.0);
    assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    
    // Test constructor with initial capacity, load factor and no entry value
    tCharHash = new TCharHashStub(10, 0.5f, 'X');
    assertEquals(10, tCharHash.capacity());
    assertEquals(0.5f, tCharHash.getLoadFactor(), 0.0);
    assertEquals('X', tCharHash.getNoEntryValue());
  }
  
  @Test
  public void testSetUp() {
    assertEquals(16, tCharHash.setUp(16));
  }
  
  @Test
  public void testContains() {
    assertFalse(tCharHash.contains('A'));
    assertFalse(tCharHash.contains('B'));
    
    // Test after adding a value
    tCharHash.insertKey('A');
    assertTrue(tCharHash.contains('A'));
    assertFalse(tCharHash.contains('B'));
  }
  
  @Test
  public void testForEach() {
    // Test forEach with empty hash
    assertTrue(tCharHash.forEach(new TCharProcedureStub()));
    
    // Test forEach with values
    tCharHash.insertKey('A');
    tCharHash.insertKey('B');
    assertFalse(tCharHash.forEach(new TCharProcedureStub()));
  }
  
  @Test
  public void testRemoveAt() {
    // Test removeAt with empty hash
    tCharHash.removeAt(0);
    assertEquals(0, tCharHash.capacity());
    
    // Test removeAt with values
    tCharHash.insertKey('A');
    tCharHash.removeAt(0);
    assertEquals(0, tCharHash.capacity());
  }
  
  @Test
  public void testIndex() {
    // Test index with empty hash
    assertEquals(-1, tCharHash.index('A'));
    
    // Test index with values
    tCharHash.insertKey('A');
    tCharHash.insertKey('B');
    assertEquals(0, tCharHash.index('A'));
    assertEquals(1, tCharHash.index('B'));
  }
  
  @Test
  public void testIndexRehashed() {
    // Test indexRehashed with empty hash
    assertEquals(-1, tCharHash.indexRehashed('A', 0, 0, (byte)0));
    
    // Test indexRehashed with values
    tCharHash.insertKey('A');
    tCharHash.insertKey('B');
    assertEquals(0, tCharHash.indexRehashed('A', 0, 0, (byte)1));
    assertEquals(1, tCharHash.indexRehashed('B', 1, 0, (byte)1));
  }
  
  @Test
  public void testInsertKey() {
    // Test insertKey with empty hash
    assertEquals(0, tCharHash.insertKey('A'));
    
    // Test insertKey with existing value
    assertEquals(-1, tCharHash.insertKey('A'));
    
    // Test insertKey with rehashing
    tCharHash.insertKey('B');
    tCharHash.insertKey('C');
    assertEquals(2, tCharHash.insertKey('A'));
  }
  
  @Test
  public void testInsertKeyRehash() {
    // Test insertKeyRehash with empty hash
    assertEquals(0, tCharHash.insertKeyRehash('A', 0, 0, (byte)0));
    
    // Test insertKeyRehash with existing value
    assertEquals(-1, tCharHash.insertKeyRehash('A', 0, 0, (byte)1));
    
    // Test insertKeyRehash with rehashing
    tCharHash.insertKeyRehash('B', 1, 0, (byte)2);
    tCharHash.insertKeyRehash('C', 2, 0, (byte)2);
    assertEquals(3, tCharHash.insertKeyRehash('A', 0, 0, (byte)1));
    
    // Test insertKeyRehash with full hash
    tCharHash = new TCharHashStub(2, 0.5f);
    tCharHash.insertKey('A');
    tCharHash.insertKey('B');
    tCharHash.insertKey('C');
    try {
      tCharHash.insertKeyRehash('D', 0, 0, (byte)1);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testInsertKeyAt() {
    // Test insertKeyAt
    tCharHash.insertKeyAt(0, 'A');
    assertEquals('A', tCharHash._set[0]);
    assertEquals((byte)1, tCharHash._states[0]);
  }
  
  class TCharHashStub extends TCharHash {
    public TCharHashStub() {
      super();
    }
    
    public TCharHashStub(int initialCapacity) {
      super(initialCapacity);
    }
    
    public TCharHashStub(int initialCapacity, float loadFactor) {
      super(initialCapacity, loadFactor);
    }
    
    public TCharHashStub(int initialCapacity, float loadFactor, char no_entry_value) {
      super(initialCapacity, loadFactor, no_entry_value);
    }
  }
  
  class TCharProcedureStub implements TCharProcedure {
    public boolean execute(char value) {
      return false;
    }
  }
}