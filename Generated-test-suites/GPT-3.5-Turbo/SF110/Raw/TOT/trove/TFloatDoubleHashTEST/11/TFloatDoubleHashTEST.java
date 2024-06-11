import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {

  private TFloatDoubleHash hash;

  @Before
  public void setUp() {
    hash = new TFloatDoubleHash();
  }

  @Test
  public void testConstructor1() {
    assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
    assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
  }

  @Test
  public void testConstructor2() {
    hash = new TFloatDoubleHash(100);
    assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
    assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
    // Test capacity
    assertTrue(hash._states.length >= 100);
    assertTrue(hash._set.length >= 100);
  }

  @Test
  public void testConstructor3() {
    hash = new TFloatDoubleHash(100, 0.5f);
    assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
    assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
    // Test capacity
    assertTrue(hash._states.length >= 100);
    assertTrue(hash._set.length >= 100);
  }

  @Test
  public void testConstructor4() {
    hash = new TFloatDoubleHash(100, 0.5f, 10.0f, 20.0);
    assertEquals(10.0f, hash.getNoEntryKey(), 0.0001);
    assertEquals(20.0, hash.getNoEntryValue(), 0.0001);
    // Test capacity
    assertTrue(hash._states.length >= 100);
    assertTrue(hash._set.length >= 100);
  }

  @Test
  public void testInsertKey() {
    assertTrue(hash.isEmpty());
    hash.insertKey(1.0f);
    assertFalse(hash.isEmpty());
    // Check if key exists
    assertTrue(hash.contains(1.0f));
  }

  @Test
  public void testRemoveAt() {
    hash.insertKey(1.0f);
    assertFalse(hash.isEmpty());
    hash.removeAt(0);
    assertTrue(hash.isEmpty());
    // Check if key is removed
    assertFalse(hash.contains(1.0f));
  }

  @Test
  public void testIndex() {
    hash.insertKey(1.0f);
    // Test existing key
    assertEquals(0, hash.index(1.0f));
    // Test non-existing key
    assertEquals(-1, hash.index(2.0f));
  }

  @Test
  public void testIndexRehashed() {
    hash.insertKey(1.0f);
    // Test existing key
    assertEquals(0, hash.indexRehashed(1.0f, 0, 0, (byte)1));
    // Test non-existing key
    assertEquals(-1, hash.indexRehashed(2.0f, 0, 0, (byte)1));
  }

  @Test
  public void testForEach() {
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    hash.insertKey(3.0f);
    
    TFloatProcedure procedure = new TFloatProcedure() {
      int counter = 0;

      public boolean execute(float value) {
        counter++;
        return counter < 2; // Stops after the second execution
      }
    };

    assertFalse(hash.forEach(procedure)); // Should return false due to early return

    // Should have executed 2 times
    assertEquals(2, procedure.counter);
  }

  @Test
  public void testWriteExternal() {
    // Not implemented in the test class
    // Just to ensure no exceptions are thrown
    try {
      hash.writeExternal(null);
    } catch (Exception e) {
      fail("Unexpected exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testReadExternal() {
    // Not implemented in the test class
    // Just to ensure no exceptions are thrown
    try {
      hash.readExternal(null);
    } catch (Exception e) {
      fail("Unexpected exception occurred: " + e.getMessage());
    }
  }

}