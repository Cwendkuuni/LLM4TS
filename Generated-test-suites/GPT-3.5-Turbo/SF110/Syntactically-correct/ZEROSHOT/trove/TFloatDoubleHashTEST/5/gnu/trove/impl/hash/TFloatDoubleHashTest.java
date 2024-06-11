package gnu.trove.impl.hash;

import junit.framework.TestCase;

public class TFloatDoubleHashTest extends TestCase {

  public void testContains() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    hash.insertKey(3.0f);
    
    assertTrue(hash.contains(1.0f));
    assertTrue(hash.contains(2.0f));
    assertTrue(hash.contains(3.0f));
    assertFalse(hash.contains(4.0f));
  }

  public void testForEach() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    hash.insertKey(3.0f);
    
    TFloatProcedure procedure = new TFloatProcedure() {
      public boolean execute(float value) {
        System.out.println(value);
        return true;
      }
    };
    
    assertTrue(hash.forEach(procedure));
  }

  public void testRemoveAt() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    hash.insertKey(3.0f);
    
    hash.removeAt(1);
    assertFalse(hash.contains(2.0f));
  }

  public void testIndex() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    hash.insertKey(3.0f);
    
    assertEquals(1, hash.index(2.0f));
    assertEquals(-1, hash.index(4.0f));
  }

  public void testInsertKey() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    hash.insertKey(3.0f);
    
    assertTrue(hash.contains(1.0f));
    assertTrue(hash.contains(2.0f));
    assertTrue(hash.contains(3.0f));
  }

  public void testXinsertKey() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(1.0f);
    hash.insertKey(2.0f);
    hash.insertKey(3.0f);
    
    assertTrue(hash.contains(1.0f));
    assertTrue(hash.contains(2.0f));
    assertTrue(hash.contains(3.0f));
  }

  public void testWriteAndReadExternal() {
    try {
      TFloatDoubleHash hash = new TFloatDoubleHash();
      hash.insertKey(1.0f);
      hash.insertKey(2.0f);
      hash.insertKey(3.0f);
      
      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("TFloatDoubleHash.ser"));
      oos.writeObject(hash);
      oos.close();
      
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream("TFloatDoubleHash.ser"));
      TFloatDoubleHash deserializedHash = (TFloatDoubleHash) ois.readObject();
      ois.close();
      
      assertTrue(deserializedHash.contains(1.0f));
      assertTrue(deserializedHash.contains(2.0f));
      assertTrue(deserializedHash.contains(3.0f));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}