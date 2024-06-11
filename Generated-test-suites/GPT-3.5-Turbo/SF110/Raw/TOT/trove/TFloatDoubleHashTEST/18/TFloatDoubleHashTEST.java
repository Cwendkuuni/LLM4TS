import org.junit.Assert;
import org.junit.Test;

public class TFloatDoubleHashTest {

  @Test
  public void testGetNoEntryKey() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    Assert.assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
  }

  @Test
  public void testGetNoEntryValue() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    Assert.assertEquals(0.0, hash.getNoEntryValue(), 0.0);
  }

  @Test
  public void testSetUp() {
    TFloatDoubleHash hash = new TFloatDoubleHash(10);
    Assert.assertEquals(10, hash._set.length);
  }

  @Test
  public void testContains() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(2.3f);
    Assert.assertTrue(hash.contains(2.3f));
    Assert.assertFalse(hash.contains(1.5f));
  }

  @Test
  public void testForEach() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(1.2f);
    hash.insertKey(3.4f);
    hash.insertKey(5.6f);
    TFloatProcedure procedure = new TFloatProcedure() {
      public boolean execute(float value) {
        return value < 10;
      }
    };
    Assert.assertTrue(hash.forEach(procedure));
    TFloatProcedure falseProcedure = new TFloatProcedure() {
      public boolean execute(float value) {
        return value > 10;
      }
    };
    Assert.assertFalse(hash.forEach(falseProcedure));
  }

  @Test
  public void testRemoveAt() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(1.2f);
    hash.insertKey(3.4f);
    hash.insertKey(5.6f);
    hash.removeAt(1);
    Assert.assertFalse(hash.contains(3.4f));
    Assert.assertEquals(0.0f, hash._set[1], 0.0f);
  }

  @Test
  public void testIndex() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(1.2f);
    Assert.assertEquals(0, hash.index(1.2f));
    Assert.assertEquals(-1, hash.index(3.4f));
  }

  @Test
  public void testInsertKey() {
    TFloatDoubleHash hash = new TFloatDoubleHash(3);
    Assert.assertEquals(0, hash.insertKey(1.2f));
    Assert.assertEquals(1, hash.insertKey(3.4f));
    Assert.assertEquals(2, hash.insertKey(5.6f));
    Assert.assertEquals(0, hash.insertKey(7.8f));
    Assert.assertEquals(3, hash.insertKey(9.0f));
  }

  @Test
  public void testXinsertKey() {
    TFloatDoubleHash hash = new TFloatDoubleHash(3);
    Assert.assertEquals(0, hash.XinsertKey(1.2f));
    Assert.assertEquals(-1, hash.XinsertKey(1.2f));
  }
  
  @Test
  public void testWriteExternal() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(1.2f);
    hash.insertKey(3.4f);
    try {
      FileOutputStream fileOut = new FileOutputStream("hash.ser");
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      hash.writeExternal(out);
      out.close();
      fileOut.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    // Test if the file exists
    File file = new File("hash.ser");
    Assert.assertTrue(file.exists());
  }
  
  @Test
  public void testReadExternal() {
    TFloatDoubleHash hash = new TFloatDoubleHash();
    hash.insertKey(1.2f);
    hash.insertKey(3.4f);
    try {
      FileOutputStream fileOut = new FileOutputStream("hash.ser");
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      hash.writeExternal(out);
      out.close();
      fileOut.close();

      FileInputStream fileIn = new FileInputStream("hash.ser");
      ObjectInputStream in = new ObjectInputStream(fileIn);
      TFloatDoubleHash hash2 = (TFloatDoubleHash) in.readObject();
      in.close();
      fileIn.close();

      Assert.assertEquals(hash.getNoEntryKey(), hash2.getNoEntryKey(), 0.0);
      Assert.assertEquals(hash.getNoEntryValue(), hash2.getNoEntryValue(), 0.0);
      Assert.assertTrue(hash.contains(1.2f));
      Assert.assertTrue(hash.contains(3.4f));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

}