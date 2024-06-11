import org.junit.*;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

  @Test
  public void testContains() {
    TFloatCharHash hash = new TFloatCharHash();
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    
    assertTrue(hash.contains(1.0f));
    assertTrue(hash.contains(2.0f));
    assertFalse(hash.contains(3.0f));
  }
  
  @Test
  public void testForEach() {
    TFloatCharHash hash = new TFloatCharHash();
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    
    TFloatProcedure procedure = new TFloatProcedure() {
      public boolean execute(float value) {
        return value > 1.0f;
      }
    };
    
    assertFalse(hash.forEach(procedure));
  }
  
  @Test
  public void testRemoveAt() {
    TFloatCharHash hash = new TFloatCharHash();
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    
    hash.removeAt(0);
    
    assertFalse(hash.contains(1.0f));
    assertTrue(hash.contains(2.0f));
  }
  
  @Test
  public void testIndex() {
    TFloatCharHash hash = new TFloatCharHash();
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    
    assertEquals(0, hash.index(1.0f));
    assertEquals(1, hash.index(2.0f));
    assertEquals(-1, hash.index(3.0f));
  }
  
  @Test
  public void testInsertKey() {
    TFloatCharHash hash = new TFloatCharHash();
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    
    assertTrue(hash.contains(1.0f));
    assertTrue(hash.contains(2.0f));
    assertFalse(hash.contains(3.0f));
  }
  
  @Test
  public void testXinsertKey() {
    TFloatCharHash hash = new TFloatCharHash();
    hash.XinsertKey(1.0f);
    hash.XinsertKey(2.0f);
    
    assertTrue(hash.contains(1.0f));
    assertTrue(hash.contains(2.0f));
    assertFalse(hash.contains(3.0f));
  }

  @Test(expected = IllegalStateException.class)
  public void testInsertKeyRehash() {
    TFloatCharHash hash = new TFloatCharHash();
    for(float i = 0.0f; i < 100000.0f; i++) {
      hash.insertKey(i);
    }
    
    assertTrue(hash.contains(99999.0f));
    assertFalse(hash.contains(100000.0f));
  }

  @Test
  public void testWriteExternal() throws IOException {
    TFloatCharHash hash = new TFloatCharHash();
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
    
    objectOutputStream.writeObject(hash);
    objectOutputStream.close();
    
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
    
    TFloatCharHash newHash = (TFloatCharHash) objectInputStream.readObject();
    
    assertTrue(newHash.contains(1.0f));
    assertTrue(newHash.contains(2.0f));
    assertFalse(newHash.contains(3.0f));
  }

  @Test
  public void testReadExternal() throws IOException, ClassNotFoundException {
    TFloatCharHash hash = new TFloatCharHash();
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
    
    objectOutputStream.writeObject(hash);
    objectOutputStream.close();
    
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
    
    TFloatCharHash newHash = new TFloatCharHash();
    newHash.readExternal(objectInputStream);
    
    assertTrue(newHash.contains(1.0f));
    assertTrue(newHash.contains(2.0f));
    assertFalse(newHash.contains(3.0f));
  }

  @Test
  public void testGetNoEntryKey() {
    TFloatCharHash hash = new TFloatCharHash();
    
    assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
  }
  
  @Test
  public void testGetNoEntryValue() {
    TFloatCharHash hash = new TFloatCharHash();
    
    assertEquals('\0', hash.getNoEntryValue());
  }
  
  @Test
  public void testSetUp() {
    TFloatCharHash hash = new TFloatCharHash();
    hash.setUp(100);
    
    assertEquals(100, hash.size());
  }

  // TODO: Add tests for other methods in TFloatCharHash
  
}