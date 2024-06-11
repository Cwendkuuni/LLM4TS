import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {
  
  @Test
  public void testContains() {
    TCharHash hash = new TCharHash();
    hash.insertKey((char)65);
    assertTrue(hash.contains((char)65));
    assertFalse(hash.contains((char)66));
  }
  
  @Test
  public void testForEach() {
    TCharHash hash = new TCharHash();
    hash.insertKey((char)65);
    hash.insertKey((char)66);
    StringBuilder result = new StringBuilder();
    hash.forEach(new TCharProcedure() {
      public boolean execute(char value) {
        result.append(value);
        return true;
      }
    });
    assertEquals("6566", result.toString());
  }
  
  @Test
  public void testRemoveAt() {
    TCharHash hash = new TCharHash();
    hash.insertKey((char)65);
    hash.removeAt(0);
    assertFalse(hash.contains((char)65));
  }
  
  @Test
  public void testIndex() {
    TCharHash hash = new TCharHash();
    hash.insertKey((char)65);
    hash.insertKey((char)66);
    assertEquals(0, hash.index((char)65));
    assertEquals(1, hash.index((char)66));
    assertEquals(-1, hash.index((char)67));
  }
  
  @Test
  public void testInsertKey() {
    TCharHash hash = new TCharHash();
    int index1 = hash.insertKey((char)65);
    assertEquals(0, index1);
    int index2 = hash.insertKey((char)66);
    assertEquals(1, index2);
    int index3 = hash.insertKey((char)65);
    assertTrue(index3 < 0);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testInsertKeyRehash() {
    TCharHash hash = new TCharHash(2);
    hash.insertKey((char)65);
    hash.insertKey((char)66);
    hash.insertKey((char)67);
  }
  
  @Test
  public void testInsertKeyAt() {
    TCharHash hash = new TCharHash();
    hash.insertKeyAt(0, (char)65);
    assertEquals((char)65, hash._set[0]);
    assertEquals((byte)1, hash._states[0]);
  }
  
  @Test
  public void testGetNoEntryValue() {
    TCharHash hash = new TCharHash();
    assertEquals('\0', hash.getNoEntryValue());
  }
  
}