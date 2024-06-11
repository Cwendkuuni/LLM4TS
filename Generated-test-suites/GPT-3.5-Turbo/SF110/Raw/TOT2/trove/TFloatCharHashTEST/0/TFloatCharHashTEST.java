import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

  @Test
  public void testGetNoEntryKey() {
    TFloatCharHash hash = new TFloatCharHash();
    assertEquals(0.0f, hash.getNoEntryKey(), 0.0001f);
  }

  @Test
  public void testGetNoEntryValue() {
    TFloatCharHash hash = new TFloatCharHash();
    assertEquals('\0', hash.getNoEntryValue());
  }

  @Test
  public void testContains() {
    TFloatCharHash hash = new TFloatCharHash();
    assertFalse(hash.contains(0.0f));
    
    hash.insertKey(1.0f);
    assertTrue(hash.contains(1.0f));
  }

  @Test
  public void testForEach() {
    TFloatCharHash hash = new TFloatCharHash();
    hash.insertKey(1.0f);
    
    TFloatProcedure procedure = new TFloatProcedure() {
      @Override
      public boolean execute(float value) {
        assertEquals(1.0f, value, 0.0001f);
        return true;
      }
    };
    
    assertTrue(hash.forEach(procedure));
  }

  @Test
  public void testInsertKey() {
    TFloatCharHash hash = new TFloatCharHash();
    hash.insertKey(1.0f);
    assertEquals(1.0f, hash._set[0], 0.0001f);
  }

  @Test
  public void testRemoveAt() {
    TFloatCharHash hash = new TFloatCharHash();
    hash.insertKey(1.0f);
    hash.removeAt(0);
    assertEquals(0.0f, hash._set[0], 0.0001f);
  }

  @Test(expected = IllegalStateException.class)
  public void testInsertKeyRehashFull() {
    TFloatCharHash hash = new TFloatCharHash(2, 1.0f, 0.0f, '\0');
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    hash.insertKey(3.0f); // Should throw exception
  }

}