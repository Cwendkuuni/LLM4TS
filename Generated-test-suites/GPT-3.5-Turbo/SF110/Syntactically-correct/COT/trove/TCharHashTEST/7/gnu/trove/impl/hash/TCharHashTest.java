import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

  private TCharHash tCharHash;

  @Before
  public void setUp() {
    tCharHash = new TCharHash(10);
  }

  @Test
  public void testContains() {
    char val = 'a';
    assertFalse(tCharHash.contains(val));
    tCharHash.insertKey(val);
    assertTrue(tCharHash.contains(val));
  }

  @Test
  public void testForEach() {
    char val = 'a';
    tCharHash.insertKey(val);
    tCharHash.forEach((i) -> {
      assertEquals(val, i);
      return true;
    });
  }

  @Test
  public void testRemoveAt() {
    char val = 'a';
    tCharHash.insertKey(val);
    assertTrue(tCharHash.contains(val));
    tCharHash.removeAt(0);
    assertFalse(tCharHash.contains(val));
  }

  @Test
  public void testIndex() {
    char val = 'a';
    tCharHash.insertKey(val);
    assertEquals(0, tCharHash.index(val));
    assertEquals(-1, tCharHash.index('b'));
  }

  @Test
  public void testInsertKey() {
    char val = 'a';
    assertEquals(0, tCharHash.insertKey(val));
    assertEquals(-1, tCharHash.insertKey(val));
  }

  @Test(expected = IllegalStateException.class)
  public void testInsertKeyRehash() {
    char val = 'a';
    for (int i = 0; i < 10; i++) {
      tCharHash.insertKey((char) ('a' + i));
    }
    tCharHash.insertKeyRehash(val, 0, 1, (byte) 1);
  }

  @Test
  public void testInsertKeyAt() {
    char val = 'a';
    tCharHash.insertKeyAt(0, val);
    assertEquals(val, tCharHash._set[0]);
    assertEquals(1, tCharHash._states[0]);
  }

}