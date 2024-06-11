
import org.apache.commons.collections.primitives.*;
import org.junit.*;

public class ArrayByteListTest {

  // Test the default constructor
  @Test
  public void testDefaultConstructor() {
    ArrayByteList list = new ArrayByteList();
    Assert.assertEquals(0, list.size());
  }

  // Test the constructor with initial capacity
  @Test
  public void testConstructorWithInitialCapacity() {
    ArrayByteList list = new ArrayByteList(10);
    Assert.assertEquals(0, list.size());
  }

  // Test the constructor with ByteCollection parameter
  @Test
  public void testConstructorWithByteCollection() {
    ByteCollection collection = new ArrayByteList();
    collection.add((byte) 1);
    collection.add((byte) 2);
    ArrayByteList list = new ArrayByteList(collection);
    Assert.assertEquals(2, list.size());
    Assert.assertEquals(1, list.get(0));
    Assert.assertEquals(2, list.get(1));
  }

  // Test the get method
  @Test
  public void testGet() {
    ArrayByteList list = new ArrayByteList();
    list.add((byte) 1);
    list.add((byte) 2);
    Assert.assertEquals(1, list.get(0));
    Assert.assertEquals(2, list.get(1));
  }

  // Test the size method
  @Test
  public void testSize() {
    ArrayByteList list = new ArrayByteList();
    list.add((byte) 1);
    list.add((byte) 2);
    Assert.assertEquals(2, list.size());
  }

  // Test the removeElementAt method
  @Test
  public void testRemoveElementAt() {
    ArrayByteList list = new ArrayByteList();
    list.add((byte) 1);
    list.add((byte) 2);
    byte removed = list.removeElementAt(0);
    Assert.assertEquals(1, removed);
    Assert.assertEquals(1, list.size());
    Assert.assertEquals(2, list.get(0));
  }

  // Test the set method
  @Test
  public void testSet() {
    ArrayByteList list = new ArrayByteList();
    list.add((byte) 1);
    list.add((byte) 2);
    byte oldValue = list.set(0, (byte) 3);
    Assert.assertEquals(1, oldValue);
    Assert.assertEquals(2, list.size());
    Assert.assertEquals(3, list.get(0));
  }

  // Test the add method
  @Test
  public void testAdd() {
    ArrayByteList list = new ArrayByteList();
    list.add((byte) 1);
    list.add((byte) 2);
    list.add(0, (byte) 3);
    Assert.assertEquals(3, list.size());
    Assert.assertEquals(3, list.get(0));
    Assert.assertEquals(1, list.get(1));
    Assert.assertEquals(2, list.get(2));
  }

  // Test the ensureCapacity method
  @Test
  public void testEnsureCapacity() {
    ArrayByteList list = new ArrayByteList();
    list.ensureCapacity(10);
    Assert.assertTrue(list instanceof ArrayByteList);
  }

  // Test the trimToSize method
  @Test
  public void testTrimToSize() {
    ArrayByteList list = new ArrayByteList();
    for (int i = 0; i < 10; i++) {
      list.add((byte) i);
    }
    list.trimToSize();
    Assert.assertEquals(10, list.size());
    for (int i = 0; i < 10; i++) {
      Assert.assertEquals(i, list.get(i));
    }
  }

  // Test the serialization and deserialization
  @Test
  public void testSerialization() throws Exception {
    ArrayByteList list = new ArrayByteList();
    list.add((byte) 1);
    list.add((byte) 2);
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(list);
    oos.close();
    
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);
    ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
    ois.close();
    
    Assert.assertEquals(list.size(), deserializedList.size());
    Assert.assertEquals(list.get(0), deserializedList.get(0));
    Assert.assertEquals(list.get(1), deserializedList.get(1));
  }
  
  // Test the checkRange method with invalid index
  @Test(expected = IndexOutOfBoundsException.class)
  public void testCheckRangeInvalidIndex() {
    ArrayByteList list = new ArrayByteList();
    list.checkRange(-1);
  }
  
  // Test the checkRangeIncludingEndpoint method with invalid index
  @Test(expected = IndexOutOfBoundsException.class)
  public void testCheckRangeIncludingEndpointInvalidIndex() {
    ArrayByteList list = new ArrayByteList();
    list.checkRangeIncludingEndpoint(10);
  }
}

