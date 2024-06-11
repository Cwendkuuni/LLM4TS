import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class TCharHashTest {
  private TCharHash tCharHash;

  @Before
  public void setUp() {
    tCharHash = new TCharHash();
  }

  @After
  public void tearDown() {
    tCharHash = null;
  }

  @Test
  public void testContains() {
    // Test when the value is in the hash
    tCharHash.insertKey('a');
    boolean result = tCharHash.contains('a');
    assertTrue(result);

    // Test when the value is not in the hash
    result = tCharHash.contains('b');
    assertFalse(result);
  }

  @Test
  public void testForEach() {
    // Test when the hash is empty
    boolean result = tCharHash.forEach(new TCharProcedure() {
      public boolean execute(char value) {
        return true;
      }
    });
    assertTrue(result);

    // Test when the hash contains values
    tCharHash.insertKey('a');
    tCharHash.insertKey('b');
    tCharHash.insertKey('c');
    final List<Character> list = new ArrayList<>();
    result = tCharHash.forEach(new TCharProcedure() {
      public boolean execute(char value) {
        list.add(value);
        return true;
      }
    });
    assertTrue(result);
    assertEquals(Arrays.asList('a', 'b', 'c'), list);
  }

  @Test
  public void testRemoveAt() {
    // Test when removing from an empty hash
    tCharHash.removeAt(0);

    // Test when removing from non-empty hash
    tCharHash.insertKey('a');
    tCharHash.insertKey('b');
    tCharHash.insertKey('c');
    tCharHash.removeAt(1);
    assertFalse(tCharHash.contains('b'));
  }

  @Test
  public void testIndex() {
    // Test when the value is not in the hash
    int result = tCharHash.index('a');
    assertEquals(-1, result);

    // Test when the value is in the hash
    tCharHash.insertKey('a');
    result = tCharHash.index('a');
    assertEquals(0, result);
  }

  @Test
  public void testIndexRehashed() {
    // Test when the value is not in the hash
    int result = tCharHash.indexRehashed('a', 0, 0, (byte)0);
    assertEquals(-1, result);

    // Test when the value is in the hash
    tCharHash.insertKey('a');
    result = tCharHash.indexRehashed('a', 0, 0, (byte)1);
    assertEquals(0, result);
  }

  @Test
  public void testInsertKey() {
    // Test when inserting a new value
    int result = tCharHash.insertKey('a');
    assertEquals(0, result);
    assertTrue(tCharHash.contains('a'));

    // Test when inserting an existing value
    result = tCharHash.insertKey('a');
    assertEquals(-1, result);
  }

  @Test
  public void testInsertKeyRehash() {
    // Test when inserting a new value without collisions
    int result = tCharHash.insertKeyRehash('a', 0, 0, (byte)0);
    assertEquals(0, result);
    assertTrue(tCharHash.contains('a'));

    // Test when inserting a new value with collisions
    result = tCharHash.insertKeyRehash('a', 0, 0, (byte)1);
    assertEquals(1, result);
    assertTrue(tCharHash.contains('a'));

    // Test when hash is full
    tCharHash = new TCharHash(2, 1.0f, ' ');
    tCharHash.insertKey('a');
    tCharHash.insertKey('b');
    result = tCharHash.insertKeyRehash('c', 0, 0, (byte)2);
    assertEquals(0, result);
    assertTrue(tCharHash.contains('c'));
  }

  @Test(expected = IllegalStateException.class)
  public void testInsertKeyRehashFull() {
    tCharHash = new TCharHash(1, 1.0f);
    tCharHash.insertKey('a');
    tCharHash.insertKey('b');
  }

  @Test
  public void testInsertKeyAt() {
    // Test when inserting a new value
    tCharHash.insertKeyAt(0, 'a');
    assertTrue(tCharHash.contains('a'));
    assertEquals('a', tCharHash.get(0));

    // Test when inserting an existing value
    tCharHash.insertKeyAt(0, 'b');
    assertTrue(tCharHash.contains('b'));
    assertEquals('b', tCharHash.get(0));
  }
}