
import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class TShortHashTest {

  @Test
  public void testConstructor1() {
    TShortHash hash = new TShortHash();
    assertNotNull(hash);
  }
  
  @Test
  public void testConstructor2() {
    TShortHash hash = new TShortHash(10);
    assertNotNull(hash);
  }
  
  @Test
  public void testConstructor3() {
    TShortHash hash = new TShortHash(10, 0.5f);
    assertNotNull(hash);
  }
  
  @Test
  public void testConstructor4() {
    TShortHash hash = new TShortHash(10, 0.5f, (short) 0);
    assertNotNull(hash);
  }
  
  @Test
  public void testGetNoEntryValue() {
    TShortHash hash = new TShortHash();
    short value = hash.getNoEntryValue();
    assertEquals(0, value);
  }
  
  @Test
  public void testContains() {
    TShortHash hash = new TShortHash();
    hash.insertKey((short) 5);
    boolean contains = hash.contains((short) 5);
    assertTrue(contains);
  }
  
  @Test
  public void testForEach() {
    TShortHash hash = new TShortHash();
    hash.insertKey((short) 5);
    hash.insertKey((short) 10);
    TShortProcedure procedure = new TShortProcedure() {
      int sum = 0;
      
      public boolean execute(short value) {
        sum += value;
        return true;
      }
    };
    boolean result = hash.forEach(procedure);
    assertEquals(15, procedure.sum);
    assertTrue(result);
  }
  
  @Test
  public void testRemoveAt() {
    TShortHash hash = new TShortHash();
    hash.insertKey((short) 5);
    hash.insertKey((short) 10);
    hash.removeAt(0);
    boolean contains = hash.contains((short) 5);
    assertFalse(contains);
  }
  
  @Test
  public void testIndex() {
    TShortHash hash = new TShortHash();
    hash.insertKey((short) 5);
    int index = hash.index((short) 5);
    assertEquals(0, index);
  }
  
  @Test
  public void testIndexRehashed() {
    TShortHash hash = new TShortHash();
    hash.insertKey((short) 5);
    int index = hash.indexRehashed((short) 5, 0, 0, (byte) 1);
    assertEquals(0, index);
  }
  
  @Test
  public void testInsertKey() {
    TShortHash hash = new TShortHash();
    int index = hash.insertKey((short) 5);
    assertEquals(0, index);
  }
  
  @Test
  public void testInsertKeyRehash() {
    TShortHash hash = new TShortHash();
    hash.insertKey((short) 5);
    int index = hash.insertKeyRehash((short) 5, 0, 0, (byte) 1);
    assertEquals(0, index);
  }
  
  @Test
  public void testInsertKeyAt() {
    TShortHash hash = new TShortHash();
    hash.insertKeyAt(0, (short) 5);
    assertEquals(5, hash._set[0]);
    assertEquals(1, hash._states[0]);
  }
}
