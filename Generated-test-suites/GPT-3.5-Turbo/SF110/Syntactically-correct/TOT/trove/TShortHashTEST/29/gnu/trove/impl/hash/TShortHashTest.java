import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TShortHashTest {
  
  private TShortHash tShortHash;
  
  @Before
  public void setUp() {
    tShortHash = new TShortHash(10, 0.8f, (short) -1);
  }
  
  @Test
  public void testConstructor() {
    assertEquals(10, tShortHash.size());
    assertEquals(-1, tShortHash.getNoEntryValue());
  }
  
  @Test
  public void testInsertKey() {
    tShortHash.insertKey((short) 1);
    tShortHash.insertKey((short) 2);
    assertTrue(tShortHash.contains((short) 1));
    assertTrue(tShortHash.contains((short) 2));
  }
  
  @Test
  public void testInsertKey_duplicate() {
    tShortHash.insertKey((short) 1);
    assertEquals(-1, tShortHash.insertKey((short) 1));
  }
  
  @Test
  public void testInsertKey_rehash() {
    for (int i = 1; i <= 20; i++) {
      tShortHash.insertKey((short) i);
    }
    assertEquals(-10, tShortHash.insertKey((short) 1));
  }
  
  @Test
  public void testRemoveAt() {
    tShortHash.insertKey((short) 1);
    tShortHash.insertKey((short) 2);
    tShortHash.removeAt(0);
    assertFalse(tShortHash.contains((short) 1));
    assertTrue(tShortHash.contains((short) 2));
  }
  
  @Test
  public void testIndex() {
    tShortHash.insertKey((short) 1);
    tShortHash.insertKey((short) 2);
    assertEquals(0, tShortHash.index((short) 1));
    assertEquals(1, tShortHash.index((short) 2));
    assertEquals(-1, tShortHash.index((short) 3));
  }
  
  @Test
  public void testIndexRehashed() {
    tShortHash.insertKey((short) 1);
    tShortHash.insertKey((short) 2);
    tShortHash.insertKey((short) 3);
    tShortHash.removeAt(1);
    assertEquals(0, tShortHash.indexRehashed((short) 1, 0, 1, (byte) 1));
    assertEquals(-1, tShortHash.indexRehashed((short) 2, 0, 1, (byte) 2));
    assertEquals(1, tShortHash.indexRehashed((short) 3, 0, 1, (byte) 1));
  }
  
  @Test
  public void testInsertKeyRehash() {
    for (int i = 0; i < 9; i++) {
      tShortHash.insertKey((short) i);
    }
    assertEquals(9, tShortHash.insertKeyRehash((short) 10, 0, 1, (byte) 1));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testInsertKeyRehash_fullSet() {
    for (int i = 1; i <= 10; i++) {
      tShortHash.insertKey((short) i);
    }
    tShortHash.insertKeyRehash((short) 11, 0, 1, (byte) 1);
  }
  
  @Test
  public void testInsertKeyAt() {
    tShortHash.insertKeyAt(0, (short) 1);
    tShortHash.insertKeyAt(1, (short) 2);
    assertTrue(tShortHash.contains((short) 1));
    assertTrue(tShortHash.contains((short) 2));
  }
  
  @Test
  public void testContains() {
    tShortHash.insertKey((short) 1);
    tShortHash.insertKey((short) 2);
    assertTrue(tShortHash.contains((short) 1));
    assertTrue(tShortHash.contains((short) 2));
    assertFalse(tShortHash.contains((short) 3));
  }
  
  @Test
  public void testForEach() {
    tShortHash.insertKey((short) 1);
    tShortHash.insertKey((short) 2);
    
    final short[] sum = {0};
    tShortHash.forEach(value -> {
      sum[0] += value;
      return true;
    });
    
    assertEquals(3, sum[0]);
  }
}