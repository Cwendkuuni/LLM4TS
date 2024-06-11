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
    assertEquals(0, hash.size());
    assertFalse(hash.contains(0.0f));
  }

  @Test
  public void testConstructor2() {
    hash = new TFloatDoubleHash(5);
    assertEquals(0, hash.size());
    assertFalse(hash.contains(0.0f));
  }

  @Test
  public void testConstructor3() {
    hash = new TFloatDoubleHash(5, 0.5f);
    assertEquals(0, hash.size());
    assertFalse(hash.contains(0.0f));
  }

  @Test
  public void testConstructor4() {
    hash = new TFloatDoubleHash(5, 0.5f, 1.0f, 2.0);
    assertEquals(0, hash.size());
    assertFalse(hash.contains(1.0f));
  }

  @Test
  public void testGetNoEntryKey() {
    assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
  }

  @Test
  public void testGetNoEntryValue() {
    assertEquals(0.0, hash.getNoEntryValue(), 0.0);
  }

  @Test
  public void testSetUp() {
    int capacity = hash.setUp(10);
    assertEquals(10, capacity);
    assertEquals(10, hash._set.length);
  }

  @Test
  public void testContains() {
    assertFalse(hash.contains(1.0f));
    hash.insertKey(1.0f);
    assertTrue(hash.contains(1.0f));
  }

  @Test
  public void testForEach() {
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    hash.insertKey(3.0f);

    TFloatProcedure procedure = new TFloatProcedure() {
      int count = 0;

      public boolean execute(float value) {
        count++;
        return true;
      }
    };

    assertTrue(hash.forEach(procedure));
    assertEquals(3, procedure.count);
  }

  @Test
  public void testRemoveAt() {
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    hash.removeAt(1);

    assertFalse(hash.contains(2.0f));
    assertEquals(1, hash.size());
  }

  @Test
  public void testIndex() {
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);

    assertEquals(0, hash.index(1.0f));
    assertEquals(1, hash.index(2.0f));
    assertEquals(-1, hash.index(3.0f));
  }

  @Test
  public void testInsertKey() {
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);

    assertEquals(0, hash.index(1.0f));
    assertEquals(1, hash.index(2.0f));
  }

  @Test
  public void testInsertKeyRehash() {
    for (int i = 0; i < 20; i++) {
      hash.insertKey(i);
    }

    assertEquals(20, hash.size());
    assertTrue(hash.contains(19.0f));
  }

  @Test
  public void testXInsertKey() {
    hash.XinsertKey(1.0f);
    hash.XinsertKey(2.0f);

    assertEquals(0, hash.index(1.0f));
    assertEquals(1, hash.index(2.0f));
  }

  @Test
  public void testWriteExternal() {
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);

    try {
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);

      hash.writeExternal(objectOutStream);
      objectOutStream.flush();
      objectOutStream.close();

      ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
      ObjectInputStream objectInStream = new ObjectInputStream(inStream);

      TFloatDoubleHash newHash = new TFloatDoubleHash();
      newHash.readExternal(objectInStream);

      objectInStream.close();
      inStream.close();

      assertEquals(2, newHash.size());
      assertTrue(newHash.contains(1.0f));
      assertTrue(newHash.contains(2.0f));
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

}