import org.junit.*;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

  @Test
  public void testGetNoEntryKey() {
    TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.5f, 0.0f, 0.0);
    assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
  }

  @Test
  public void testGetNoEntryValue() {
    TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.5f, 0.0f, 0.0);
    assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
  }

  @Test
  public void testContains() {
    TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.5f, 0.0f, 0.0);
    hash.insertKey(1.0f);
    assertTrue(hash.contains(1.0f));
    assertFalse(hash.contains(2.0f));
  }

  @Test
  public void testForEach() {
    TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.5f, 0.0f, 0.0);
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    hash.insertKey(3.0f);
    hash.forEach(new TFloatProcedure() {
      public boolean execute(float value) {
        System.out.println(value);
        return true;
      }
    });
  }

  @Test
  public void testRemoveAt() {
    TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.5f, 0.0f, 0.0);
    hash.insertKey(1.0f);
    assertEquals(1.0f, hash._set[0], 0.0001);
    assertEquals(1, hash._states[0]);
    hash.removeAt(0);
    assertEquals(0.0f, hash._set[0], 0.0001);
    assertEquals(0, hash._states[0]);
  }

  // Add more test cases for the remaining methods

}