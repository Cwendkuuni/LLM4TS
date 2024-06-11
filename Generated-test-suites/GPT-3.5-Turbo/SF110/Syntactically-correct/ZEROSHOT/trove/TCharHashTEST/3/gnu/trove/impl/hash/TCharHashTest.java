import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

  private TCharHash tCharHash;

  @Before
  public void setUp() {
    tCharHash = new TCharHash();
  }

  @Test
  public void testConstructor_noParameter() {
    tCharHash = new TCharHash();
    assertNotNull(tCharHash);
    assertEquals(0, tCharHash.size());
  }

  @Test
  public void testConstructor_withInitialCapacity() {
    tCharHash = new TCharHash(10);
    assertNotNull(tCharHash);
    assertEquals(0, tCharHash.size());
  }

  @Test
  public void testConstructor_withInitialCapacityAndLoadFactor() {
    tCharHash = new TCharHash(10, 0.5f);
    assertNotNull(tCharHash);
    assertEquals(0, tCharHash.size());
  }

  @Test
  public void testConstructor_withInitialCapacityAndLoadFactorAndEntryValue() {
    tCharHash = new TCharHash(10, 0.5f, 'a');
    assertNotNull(tCharHash);
    assertEquals(0, tCharHash.size());
  }

  @Test
  public void testGetNoEntryValue() {
    assertEquals('\0', tCharHash.getNoEntryValue());
  }

  @Test
  public void testSetUp() {
    int capacity = tCharHash.setUp(10);
    assertEquals(10, capacity);
    assertNotNull(tCharHash._set);
    assertEquals(10, tCharHash._set.length);
  }

  @Test
  public void testContains() {
    tCharHash.insertKey('a');
    assertTrue(tCharHash.contains('a'));
    assertFalse(tCharHash.contains('b'));
  }

  @Test
  public void testForEach() {
    tCharHash.insertKey('a');
    tCharHash.insertKey('b');
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
    tCharHash.insertKey('a');
    tCharHash.removeAt(0);
    assertFalse(tCharHash.contains('a'));
    assertEquals(0, tCharHash.size());
  }

  @Test
  public void testIndex() {
    tCharHash.insertKey('a');
    assertEquals(0, tCharHash.index('a'));
    assertEquals(-1, tCharHash.index('b'));
  }

  @Test
  public void testIndexRehashed() {
    tCharHash.insertKey('a');
    assertEquals(-1, tCharHash.indexRehashed('b', 0, 0, (byte) 0));
  }

  @Test
  public void testInsertKey() {
    tCharHash.insertKey('a');
    assertTrue(tCharHash.contains('a'));
    assertEquals(1, tCharHash.size());
  }

  @Test
  public void testInsertKeyRehash() {
    tCharHash.insertKey('a');
    int index = tCharHash.insertKeyRehash('b', 0, 0, (byte) 0);
    assertEquals(1, index);
    assertTrue(tCharHash.contains('a'));
    assertTrue(tCharHash.contains('b'));
    assertEquals(2, tCharHash.size());
  }

  @Test
  public void testInsertKeyAt() {
    tCharHash.insertKeyAt(0, 'a');
    assertTrue(tCharHash.contains('a'));
    assertEquals(1, tCharHash.size());
  }

}