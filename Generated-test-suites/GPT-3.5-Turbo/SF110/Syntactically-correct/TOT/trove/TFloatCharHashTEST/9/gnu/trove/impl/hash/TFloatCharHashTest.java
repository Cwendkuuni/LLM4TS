import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

  @Test
  public void testGetNoEntryKey() {
    TFloatCharHash hash = new TFloatCharHash(10, 0.5f, 0.0f, '\0');
    assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
  }

  @Test
  public void testGetNoEntryValue() {
    TFloatCharHash hash = new TFloatCharHash(10, 0.5f, 0.0f, '\0');
    assertEquals('\0', hash.getNoEntryValue());
  }

  @Test
  public void testSetUp() {
    TFloatCharHash hash = new TFloatCharHash(10, 0.5f, 0.0f, '\0');
    int capacity = hash.setUp(5);
    assertEquals(5, capacity);
  }

  @Test
  public void testContains() {
    TFloatCharHash hash = new TFloatCharHash(2, 0.5f, 0.0f, '\0');
    hash.insertKey(1.1f);
    assertTrue(hash.contains(1.1f));
  }

  @Test
  public void testForEach() {
    TFloatCharHash hash = new TFloatCharHash(2, 0.5f, 0.0f, '\0');
    hash.insertKey(1.1f);
    hash.insertKey(2.2f);
    TFloatProcedure procedure = new TFloatProcedure() {
      float sum = 0.0f;
      public boolean execute(float value) {
        sum += value;
        return true;
      }
    };
    assertTrue(hash.forEach(procedure));
    assertEquals(3.3f, procedure.sum, 0.0);
  }

  @Test
  public void testRemoveAt() {
    TFloatCharHash hash = new TFloatCharHash(2, 0.5f, 0.0f, '\0');
    hash.insertKey(1.1f);
    hash.insertKey(2.2f);
    hash.removeAt(0);
    assertFalse(hash.contains(1.1f));
  }

  @Test
  public void testIndex() {
    TFloatCharHash hash = new TFloatCharHash(2, 0.5f, 0.0f, '\0');
    hash.insertKey(1.1f);
    hash.insertKey(2.2f);
    int index1 = hash.index(1.1f);
    int index2 = hash.index(2.2f);
    assertEquals(0, index1);
    assertEquals(1, index2);
  }

  @Test
  public void testInsertKey() {
    TFloatCharHash hash = new TFloatCharHash(2, 0.5f, 0.0f, '\0');
    int index1 = hash.insertKey(1.1f);
    int index2 = hash.insertKey(2.2f);
    assertEquals(0, index1);
    assertEquals(0, index2);
  }

  @Test
  public void testInsertKeyRehash() {
    TFloatCharHash hash = new TFloatCharHash(5, 0.5f, 0.0f, '\0');
    hash.insertKey(1.1f); // index 0
    hash.insertKey(2.2f); // index 1
    hash.insertKey(3.3f); // index 2
    int index = hash.insertKeyRehash(4.4f, 0, 0, (byte)0);
    assertEquals(3, index); // should insert at index 3
  }

  @Test
  public void testInsertKeyAt() {
    TFloatCharHash hash = new TFloatCharHash(2, 0.5f, 0.0f, '\0');
    hash.insertKeyAt(1, 1.1f);
    assertEquals(1.1f, hash._set[1], 0.0);
    assertEquals((byte)1, hash._states[1]);
  }

  @Test
  public void testXinsertKey() {
    TFloatCharHash hash = new TFloatCharHash(2, 0.5f, 0.0f, '\0');
    int index1 = hash.XinsertKey(1.1f);
    int index2 = hash.XinsertKey(2.2f);
    assertEquals(0, index1);
    assertEquals(0, index2);
  }
  
  @Test
  public void testWriteExternal() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutput out = new ObjectOutputStream(baos);
    
    TFloatCharHash hash = new TFloatCharHash(2, 0.5f, 0.0f, '\0');
    hash.writeExternal(out);
    
    out.flush();
    byte[] byteArray = baos.toByteArray();
    
    assertTrue(byteArray.length > 0);
  }
  
  @Test
  public void testReadExternal() throws IOException, ClassNotFoundException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutput out = new ObjectOutputStream(baos);
    
    TFloatCharHash hash = new TFloatCharHash(2, 0.5f, 0.0f, '\0');
    hash.writeExternal(out);
    
    out.flush();
    byte[] byteArray = baos.toByteArray();
    
    ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
    ObjectInput in = new ObjectInputStream(bais);
    
    hash.readExternal(in);
    
    assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
    assertEquals('\0', hash.getNoEntryValue());
  }
}