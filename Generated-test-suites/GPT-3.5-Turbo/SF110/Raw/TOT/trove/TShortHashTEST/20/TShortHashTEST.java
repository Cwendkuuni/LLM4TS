import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TShortHashTest {
  
  private TShortHash tShortHash;
  
  @Before
  public void setUp() {
    tShortHash = new TShortHash();
  }
  
  @After
  public void tearDown() {
    tShortHash = null;
  }
  
  @Test
  public void testConstructor1() {
    assertEquals(0, tShortHash.size());
    assertEquals(0, tShortHash.capacity());
  }
  
  @Test
  public void testConstructor2() {
    tShortHash = new TShortHash(10);
    assertEquals(0, tShortHash.size());
    assertEquals(10, tShortHash.capacity());
  }
  
  @Test
  public void testConstructor3() {
    tShortHash = new TShortHash(10, 0.5f);
    assertEquals(0, tShortHash.size());
    assertEquals(10, tShortHash.capacity());
  }
  
  @Test
  public void testConstructor4() {
    tShortHash = new TShortHash(10, 0.5f, (short) -1);
    assertEquals(0, tShortHash.size());
    assertEquals(10, tShortHash.capacity());
    assertEquals(-1, tShortHash.getNoEntryValue());
  }
  
  @Test
  public void testSetUp() {
    assertEquals(10, tShortHash.setUp(10));
    assertEquals(10, tShortHash.size());
    assertEquals(10, tShortHash.capacity());
  }
  
  @Test
  public void testContains() {
    tShortHash.insertKey((short) 1);
    assertTrue(tShortHash.contains((short) 1));
    assertFalse(tShortHash.contains((short) 2));
  }
  
  @Test
  public void testForEach() {
    tShortHash.insertKey((short) 1);
    tShortHash.insertKey((short) 2);
    tShortHash.insertKey((short) 3);
    
    final int[] sum = {0};
    TShortProcedure procedure = new TShortProcedure() {
      @Override
      public boolean execute(short value) {
        sum[0] += value;
        return true;
      }
    };
    
    assertTrue(tShortHash.forEach(procedure));
    assertEquals(6, sum[0]);
  }
  
  @Test
  public void testRemoveAt() {
    tShortHash.insertKey((short) 1);
    tShortHash.removeAt(0);
    assertEquals(0, tShortHash.size());
    assertEquals(0, tShortHash.capacity());
  }
  
  @Test
  public void testIndex() {
    tShortHash.insertKey((short) 1);
    assertEquals(0, tShortHash.index((short) 1));
    assertEquals(-1, tShortHash.index((short) 2));
  }
  
  @Test
  public void testIndexRehashed() {
    tShortHash.insertKey((short) 1);
    assertEquals(0, tShortHash.indexRehashed((short) 1, 0, 0, (byte) 1));
    assertEquals(-1, tShortHash.indexRehashed((short) 2, 0, 0, (byte) 1));
  }
  
  @Test
  public void testInsertKey() {
    assertEquals(0, tShortHash.insertKey((short) 1));
    assertEquals(1, tShortHash.size());
    assertEquals(10, tShortHash.capacity());
  }
  
  @Test
  public void testInsertKeyRehash() {
    tShortHash.insertKey((short) 1);
    assertEquals(0, tShortHash.insertKeyRehash((short) 1, 0, 0, (byte) 1));
    assertEquals(-1, tShortHash.insertKeyRehash((short) 2, 0, 0, (byte) 1));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testInsertKeyThrowException() {
    tShortHash.insertKey((short) 1);
    tShortHash.insertKey((short) 2);
    tShortHash.insertKey((short) 3);
    tShortHash.insertKey((short) 4);
    tShortHash.insertKey((short) 5);
    tShortHash.insertKey((short) 6);
  }
}