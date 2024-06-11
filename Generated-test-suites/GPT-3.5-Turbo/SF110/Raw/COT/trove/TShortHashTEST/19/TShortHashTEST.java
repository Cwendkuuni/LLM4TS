import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

  @Test
  public void testContains() {
    TShortHash t = new TShortHash();
    
    // Test contains when value is present
    t.insertKey((short) 10);
    assertTrue(t.contains((short) 10));
    
    // Test contains when value is not present
    assertFalse(t.contains((short) 20));
    
    // Test contains when value is no_entry_value
    t.insertKey(t.getNoEntryValue());
    assertTrue(t.contains(t.getNoEntryValue()));
  }

  @Test
  public void testForEach() {
    TShortHash t = new TShortHash();
    
    // Test forEach with empty set
    assertFalse(t.forEach(new TShortProcedure() {
      public boolean execute(short value) {
        return false;
      }
    }));
    
    // Test forEach with non-empty set
    t.insertKey((short) 10);
    t.insertKey((short) 20);
    assertTrue(t.forEach(new TShortProcedure() {
      public boolean execute(short value) {
        return value % 10 == 0;
      }
    }));
  }

  @Test
  public void testRemoveAt() {
    TShortHash t = new TShortHash();
    
    // Test removeAt with non-empty set
    t.insertKey((short) 10);
    assertEquals((short) 10, t._set[0]);
    t.removeAt(0);
    assertEquals(t.getNoEntryValue(), t._set[0]);
    
    // Test removeAt with empty set
    t.removeAt(0);
    assertEquals(t.getNoEntryValue(), t._set[0]);
  }

  @Test
  public void testIndex() {
    TShortHash t = new TShortHash();
    
    // Test index when value is present
    t.insertKey((short) 10);
    assertEquals(0, t.index((short) 10));
    
    // Test index when value is not present
    assertEquals(-1, t.index((short) 20));
    
    // Test index when value is no_entry_value
    t.insertKey(t.getNoEntryValue());
    assertEquals(1, t.index(t.getNoEntryValue()));
  }

  @Test
  public void testIndexRehashed() {
    TShortHash t = new TShortHash();
    
    // TODO: Test indexRehashed method
  }

  @Test
  public void testInsertKey() {
    TShortHash t = new TShortHash();
    
    // Test insertKey when value is not present
    assertEquals(0, t.insertKey((short) 10));
    
    // Test insertKey when value is already present
    assertEquals(-1, t.insertKey((short) 10));
    
    // Test insertKey when value is no_entry_value
    assertEquals(1, t.insertKey(t.getNoEntryValue()));
  }

  @Test
  public void testInsertKeyRehash() {
    TShortHash t = new TShortHash();
    
    // TODO: Test insertKeyRehash method
  }

  @Test
  public void testInsertKeyAt() {
    TShortHash t = new TShortHash();
    
    // TODO: Test insertKeyAt method
  }
}