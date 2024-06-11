import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {
  
  @Test
  public void testDefaultConstructor() {
    ArrayByteList list = new ArrayByteList();
    assertEquals(0, list.size());
  }
  
  @Test
  public void testInitialCapacityConstructor() {
    ArrayByteList list = new ArrayByteList(10);
    assertEquals(0, list.size());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInitialCapacityConstructorWithNegativeValue() {
    new ArrayByteList(-10);
  }
  
  @Test
  public void testByteCollectionConstructor() {
    ByteCollection collection = new ArrayByteCollection(new byte[] {1, 2, 3});
    ArrayByteList list = new ArrayByteList(collection);
    assertEquals(3, list.size());
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetWithNegativeIndex() {
    ArrayByteList list = new ArrayByteList();
    list.get(-1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetWithIndexOutOfRange() {
    ArrayByteList list = new ArrayByteList();
    list.get(10);
  }
  
  @Test
  public void testGet() {
    ArrayByteList list = new ArrayByteList(new byte[] {1, 2, 3});
    assertEquals(2, list.get(1));
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveElementAtWithNegativeIndex() {
    ArrayByteList list = new ArrayByteList();
    list.removeElementAt(-1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveElementAtWithIndexOutOfRange() {
    ArrayByteList list = new ArrayByteList();
    list.removeElementAt(10);
  }
  
  @Test
  public void testRemoveElementAt() {
    ArrayByteList list = new ArrayByteList(new byte[] {1, 2, 3});
    byte removed = list.removeElementAt(1);
    assertEquals(2, removed);
    assertEquals(2, list.size());
    assertEquals(3, list.get(1));
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testSetWithNegativeIndex() {
    ArrayByteList list = new ArrayByteList();
    list.set(-1, (byte)1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testSetWithIndexOutOfRange() {
    ArrayByteList list = new ArrayByteList();
    list.set(10, (byte)1);
  }
  
  @Test
  public void testSet() {
    ArrayByteList list = new ArrayByteList(new byte[] {1, 2, 3});
    byte old = list.set(1, (byte)4);
    assertEquals(2, old);
    assertEquals(3, list.size());
    assertEquals(4, list.get(1));
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testAddWithNegativeIndex() {
    ArrayByteList list = new ArrayByteList();
    list.add(-1, (byte)1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testAddWithIndexOutOfRange() {
    ArrayByteList list = new ArrayByteList();
    list.add(10, (byte)1);
  }
  
  @Test
  public void testAdd() {
    ArrayByteList list = new ArrayByteList(new byte[] {1, 2, 3});
    list.add(1, (byte)4);
    assertEquals(4, list.size());
    assertEquals(1, list.get(0));
    assertEquals(4, list.get(1));
    assertEquals(2, list.get(2));
    assertEquals(3, list.get(3));
  }
  
  @Test
  public void testEnsureCapacity() {
    ArrayByteList list = new ArrayByteList();
    list.ensureCapacity(10);
    assertEquals(0, list.size());
  }
  
  @Test
  public void testTrimToSize() {
    ArrayByteList list = new ArrayByteList(new byte[] {1, 2, 3});
    list.trimToSize();
    assertEquals(3, list.size());
  }
  
  @Test
  public void testSerialization() throws IOException, ClassNotFoundException {
    ArrayByteList list = new ArrayByteList(new byte[] {1, 2, 3});
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(baos);
    out.writeObject(list);
    out.close();
    
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream in = new ObjectInputStream(bais);
    ArrayByteList deserializedList = (ArrayByteList) in.readObject();
    in.close();
    
    assertEquals(3, deserializedList.size());
    assertEquals(2, deserializedList.get(1));
  }
  
}