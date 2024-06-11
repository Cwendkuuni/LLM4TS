import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {

  private TFloatCharHash hash;

  @Before
  public void setUp() {
    hash = new TFloatCharHash();
  }

  @Test
  public void testGetNoEntryKey() {
    float expected = 0.0f;
    float result = hash.getNoEntryKey();
    assertEquals(expected, result, 0.0001);
  }

  @Test
  public void testGetNoEntryValue() {
    char expected = '\0';
    char result = hash.getNoEntryValue();
    assertEquals(expected, result);
  }

  @Test
  public void testSetUp() {
    int expected = 10;
    int result = hash.setUp(10);
    assertEquals(expected, result);
    assertEquals(expected, hash._set.length);
  }

  @Test
  public void testContains() {
    float val = 5.5f;
    assertFalse(hash.contains(val));
    hash.insertKey(val);
    assertTrue(hash.contains(val));
  }

  @Test
  public void testForEach() {
    final StringBuilder sb = new StringBuilder();
    TFloatProcedure procedure = new TFloatProcedure() {
      public boolean execute(float val) {
        sb.append(val);
        return true;
      }
    };
    hash.insertKey(1.1f);
    hash.insertKey(2.2f);
    hash.forEach(procedure);
    assertEquals("1.12.2", sb.toString());
  }

  @Test
  public void testRemoveAt() {
    hash.insertKey(1.1f);
    hash.removeAt(0);
    assertFalse(hash.contains(1.1f));
  }

  @Test
  public void testIndex() {
    float key = 1.1f;
    hash.insertKey(key);
    int expected = 0;
    int result = hash.index(key);
    assertEquals(expected, result);
    expected = -1;
    result = hash.index(2.2f);
    assertEquals(expected, result);
  }

  @Test
  public void testInsertKey() {
    float key = 1.1f;
    int expected = 0;
    int result = hash.insertKey(key);
    assertEquals(expected, result);
    key = 2.2f;
    expected = 1;
    result = hash.insertKey(key);
    assertEquals(expected, result);
    assertFalse(hash.consumeFreeSlot);
    key = 1.1f;
    expected = -1;
    result = hash.insertKey(key);
    assertEquals(expected, result);
  }

  @Test
  public void testXinsertKey() {
    float key = 1.1f;
    int expected = 0;
    int result = hash.XinsertKey(key);
    assertEquals(expected, result);
    key = 2.2f;
    expected = 1;
    result = hash.XinsertKey(key);
    assertEquals(expected, result);
    assertFalse(hash.consumeFreeSlot);
    key = 1.1f;
    expected = -1;
    result = hash.XinsertKey(key);
    assertEquals(expected, result);
  }

  @Test
  public void testWriteExternal() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    hash.writeExternal(dos);

    byte[] byteArray = baos.toByteArray();

    ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
    DataInputStream dis = new DataInputStream(bais);
    hash.readExternal(dis);

    float expectedKey = 0.0f;
    char expectedValue = '\0';
    float resultKey = hash.getNoEntryKey();
    char resultValue = hash.getNoEntryValue();
    assertEquals(expectedKey, resultKey, 0.0001);
    assertEquals(expectedValue, resultValue);
  }

}