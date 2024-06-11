import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
  
  private ArrayByteList arrayByteList;
  
  @Before
  public void setUp() {
    arrayByteList = new ArrayByteList();
  }
  
  @Test
  public void testConstructor() {
    ArrayByteList list = new ArrayByteList();
    assertEquals(0, list.size());
  }
  
  @Test
  public void testConstructorWithInitialCapacity() {
    ArrayByteList list = new ArrayByteList(10);
    assertEquals(0, list.size());
  }
  
  @Test
  public void testConstructorWithByteCollection() {
    ByteCollection collection = new ArrayByteList();
    collection.add((byte) 1);
    ArrayByteList list = new ArrayByteList(collection);
    assertEquals(1, list.size());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNegativeCapacity() {
    new ArrayByteList(-1);
  }
  
  @Test
  public void testGet() {
    arrayByteList.add((byte) 1);
    assertEquals((byte) 1, arrayByteList.get(0));
  }
  
  @Test
  public void testSize() {
    assertEquals(0, arrayByteList.size());
    arrayByteList.add((byte) 1);
    assertEquals(1, arrayByteList.size());
  }
  
  @Test
  public void testRemoveElementAt() {
    arrayByteList.add((byte) 1);
    arrayByteList.add((byte) 2);
    arrayByteList.add((byte) 3);
    byte removedElement = arrayByteList.removeElementAt(1);
    assertEquals(2, removedElement);
    assertEquals(2, arrayByteList.size());
    assertEquals((byte) 1, arrayByteList.get(0));
    assertEquals((byte) 3, arrayByteList.get(1));
  }
  
  @Test
  public void testSet() {
    arrayByteList.add((byte) 1);
    byte oldval = arrayByteList.set(0, (byte) 2);
    assertEquals((byte) 1, oldval);
    assertEquals((byte) 2, arrayByteList.get(0));
  }
  
  @Test
  public void testAdd() {
    arrayByteList.add(0, (byte) 1);
    assertEquals(1, arrayByteList.size());
    assertEquals((byte) 1, arrayByteList.get(0));
    arrayByteList.add(1, (byte) 2);
    assertEquals(2, arrayByteList.size());
    assertEquals((byte) 1, arrayByteList.get(0));
    assertEquals((byte) 2, arrayByteList.get(1));
  }
  
  @Test
  public void testEnsureCapacity() {
    arrayByteList.ensureCapacity(10);
    assertEquals(0, arrayByteList.size());
  }
  
  @Test
  public void testTrimToSize() {
    arrayByteList.trimToSize();
    assertEquals(0, arrayByteList.size());
  }
  
  @Test
  public void testSerialization() {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bos);
      out.writeObject(arrayByteList);
      out.close();
      
      ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
      ObjectInputStream in = new ObjectInputStream(bis);
      ArrayByteList serializedList = (ArrayByteList) in.readObject();
      in.close();
      
      assertEquals(arrayByteList.size(), serializedList.size());
      
      for (int i = 0; i < arrayByteList.size(); ++i) {
        assertEquals(arrayByteList.get(i), serializedList.get(i));
      }
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testCheckRangeWithNegativeIndex() {
    arrayByteList.checkRange(-1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testCheckRangeWithOutOfRangeIndex() {
    arrayByteList.add((byte) 1);
    arrayByteList.checkRange(1);
  }
  
  @Test
  public void testCheckRangeWithValidIndex() {
    arrayByteList.add((byte) 1);
    arrayByteList.checkRange(0);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testCheckRangeIncludingEndpointWithNegativeIndex() {
    arrayByteList.checkRangeIncludingEndpoint(-1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testCheckRangeIncludingEndpointWithOutOfRangeIndex() {
    arrayByteList.add((byte) 1);
    arrayByteList.checkRangeIncludingEndpoint(2);
  }
  
  @Test
  public void testCheckRangeIncludingEndpointWithValidIndex() {
    arrayByteList.add((byte) 1);
    arrayByteList.checkRangeIncludingEndpoint(1);
  }
}