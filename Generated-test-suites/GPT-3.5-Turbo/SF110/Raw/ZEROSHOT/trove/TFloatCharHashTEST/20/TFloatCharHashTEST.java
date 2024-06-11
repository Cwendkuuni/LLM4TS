import junit.framework.TestCase;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class TFloatCharHashTest extends TestCase {
  
  @Test
  public void testDefaultConstructor() {
    TFloatCharHash hash = new TFloatCharHash();
    assertEquals(0.0f, hash.getNoEntryKey());
    assertEquals('\0', hash.getNoEntryValue());
    assertNotNull(hash._set);
    assertFalse(hash.consumeFreeSlot);
  }
  
  @Test
  public void testInitialCapacityConstructor() {
    int initialCapacity = 10;
    TFloatCharHash hash = new TFloatCharHash(initialCapacity);
    assertEquals(0.0f, hash.getNoEntryKey());
    assertEquals('\0', hash.getNoEntryValue());
    assertNotNull(hash._set);
    assertEquals(initialCapacity, hash._set.length);
    assertFalse(hash.consumeFreeSlot);
  }
  
  @Test
  public void testInitialCapacityLoadFactorConstructor() {
    int initialCapacity = 10;
    float loadFactor = 0.75f;
    TFloatCharHash hash = new TFloatCharHash(initialCapacity, loadFactor);
    assertEquals(0.0f, hash.getNoEntryKey());
    assertEquals('\0', hash.getNoEntryValue());
    assertNotNull(hash._set);
    assertEquals(initialCapacity, hash._set.length);
    assertEquals(loadFactor, hash._loadFactor, 0.00001);
    assertFalse(hash.consumeFreeSlot);
  }
  
  @Test
  public void testFullConstructor() {
    int initialCapacity = 10;
    float loadFactor = 0.75f;
    float noEntryKey = 1.0f;
    char noEntryValue = 'a';
    TFloatCharHash hash = new TFloatCharHash(initialCapacity, loadFactor, noEntryKey, noEntryValue);
    assertEquals(noEntryKey, hash.getNoEntryKey(), 0.00001);
    assertEquals(noEntryValue, hash.getNoEntryValue());
    assertNotNull(hash._set);
    assertEquals(initialCapacity, hash._set.length);
    assertEquals(loadFactor, hash._loadFactor, 0.00001);
    assertFalse(hash.consumeFreeSlot);
  }
  
  @Test
  public void testGetNoEntryKey() {
    float noEntryKey = 1.0f;
    TFloatCharHash hash = new TFloatCharHash();
    hash.no_entry_key = noEntryKey;
    assertEquals(noEntryKey, hash.getNoEntryKey(), 0.00001);
  }
  
  @Test
  public void testGetNoEntryValue() {
    char noEntryValue = 'a';
    TFloatCharHash hash = new TFloatCharHash();
    hash.no_entry_value = noEntryValue;
    assertEquals(noEntryValue, hash.getNoEntryValue());
  }
  
  @Test
  public void testSetUp() {
    int initialCapacity = 10;
    TFloatCharHash hash = new TFloatCharHash();
    int capacity = hash.setUp(initialCapacity);
    assertNotNull(hash._set);
    assertEquals(initialCapacity, capacity);
    assertEquals(initialCapacity, hash._set.length);
  }
  
  @Test
  public void testContains() {
    TFloatCharHash hash = new TFloatCharHash();
    hash._set = new float[]{1.0f, 2.0f, 3.0f};
    assertTrue(hash.contains(2.0f));
    assertFalse(hash.contains(4.0f));
  }
  
  @Test
  public void testForEach() {
    TFloatCharHash hash = new TFloatCharHash();
    hash._states = new byte[]{1, 1, 0, 1};
    hash._set = new float[]{1.0f, 2.0f, 3.0f, 4.0f};
    TFloatProcedure procedure = new TFloatProcedure() {
      public boolean execute(float val) {
        System.out.println(val);
        return val != 3.0f;
      }
    };
    assertFalse(hash.forEach(procedure));
  }
  
  @Test
  public void testRemoveAt() {
    TFloatCharHash hash = new TFloatCharHash();
    hash._set = new float[]{1.0f, 2.0f, 3.0f};
    hash._states = new byte[]{1, 1, 1};
    int index = 1;
    hash.removeAt(index);
    assertEquals(0.0f, hash._set[index]);
    assertEquals(0, hash._states[index]);
  }
  
  @Test
  public void testIndex() {
    TFloatCharHash hash = new TFloatCharHash();
    hash._set = new float[]{1.0f, 2.0f, 3.0f};
    hash._states = new byte[]{1, 1, 1};
    int expectedIndex = 1;
    float key = 2.0f;
    assertEquals(expectedIndex, hash.index(key));
  }
  
  @Test
  public void testIndexRehashed() {
    TFloatCharHash hash = new TFloatCharHash();
    hash._set = new float[]{1.0f, 2.0f, 3.0f};
    hash._states = new byte[]{1, 1, 1};
    int expectedIndex = 2;
    float key = 3.0f;
    int index = 1;
    int hashValue = 4;
    byte state = 1;
    assertEquals(expectedIndex, hash.indexRehashed(key, index, hashValue, state));
  }
  
  @Test
  public void testInsertKey() {
    TFloatCharHash hash = new TFloatCharHash();
    hash._states = new byte[]{2, 0};
    int expectedIndex = 1;
    float val = 2.0f;
    assertEquals(expectedIndex, hash.insertKey(val));
  }
  
  @Test
  public void testInsertKeyRehash() {
    TFloatCharHash hash = new TFloatCharHash();
    hash._states = new byte[]{2, 0};
    int expectedIndex = 0;
    float val = 2.0f;
    int index = 1;
    int hashValue = 4;
    byte state = 1;
    assertEquals(expectedIndex, hash.insertKeyRehash(val, index, hashValue, state));
  }
  
  @Test
  public void testInsertKeyAt() {
    TFloatCharHash hash = new TFloatCharHash();
    hash._set = new float[2];
    hash._states = new byte[2];
    float val = 2.0f;
    int index = 1;
    hash.insertKeyAt(index, val);
    assertEquals(val, hash._set[index], 0.00001);
    assertEquals(1, hash._states[index]);
  }
  
  @Test
  public void testXInsertKey() {
    TFloatCharHash hash = new TFloatCharHash();
    hash._states = new byte[2];
    hash._set = new float[2];
    float key = 2.0f;
    int index = 1;
    hash.xinsertKey(key);
    assertEquals(key, hash._set[index], 0.00001);
    assertEquals(1, hash._states[index]);
  }
  
  @Test
  public void testWriteExternal() throws Exception {
    TFloatCharHash hash = new TFloatCharHash();
    hash.no_entry_key = 1.0f;
    hash.no_entry_value = 'a';
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    hash.writeExternal(oos);
    oos.flush();
    
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bis);
    TFloatCharHash deserializedHash = new TFloatCharHash();
    deserializedHash.readExternal(ois);
    
    assertEquals(hash.no_entry_key, deserializedHash.no_entry_key, 0.00001);
    assertEquals(hash.no_entry_value, deserializedHash.no_entry_value);
  }
  
  @Test
  public void testReadExternal() throws Exception {
    TFloatCharHash hash = new TFloatCharHash();
    float noEntryKey = 1.0f;
    char noEntryValue = 'a';
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeByte(0);
    oos.flush();
    
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bis);
    hash.readExternal(ois);
    
    assertEquals(noEntryKey, hash.no_entry_key, 0.00001);
    assertEquals(noEntryValue, hash.no_entry_value);
  }
}