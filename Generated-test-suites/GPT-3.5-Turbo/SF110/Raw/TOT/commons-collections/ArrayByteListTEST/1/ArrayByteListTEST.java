import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {
  
  private ArrayByteList arrayByteList;

  @Before
  public void setUp() {
    arrayByteList = new ArrayByteList();
  }
  
  @Test
  public void testArrayByteList_EmptyConstructor() {
    assertNotNull("ArrayByteList object should not be null", arrayByteList);
    assertEquals("ArrayByteList size should be 0", 0, arrayByteList.size());
    assertTrue("ArrayByteList data should not be null", arrayByteList._data != null && arrayByteList._data.length == 8);
  }
  
  @Test
  public void testArrayByteList_InitialCapacityConstructor() {
    ArrayByteList arrayByteListWithCapacity = new ArrayByteList(16);
    assertNotNull("ArrayByteList object should not be null", arrayByteListWithCapacity);
    assertEquals("ArrayByteList size should be 0", 0, arrayByteListWithCapacity.size());
    assertTrue("ArrayByteList data should not be null", arrayByteListWithCapacity._data != null && arrayByteListWithCapacity._data.length == 16);
  }
  
  @Test
  public void testArrayByteList_CollectionConstructor() {
    ByteCollection byteCollection = new ArrayByteList();
    byteCollection.add((byte) 1);
    byteCollection.add((byte) 2);
    
    ArrayByteList arrayByteListWithCollection = new ArrayByteList(byteCollection);
    assertNotNull("ArrayByteList object should not be null", arrayByteListWithCollection);
    assertEquals("ArrayByteList size should be equal to ByteCollection size", byteCollection.size(), arrayByteListWithCollection.size());
    
    assertTrue("ArrayByteList data should not be null", arrayByteListWithCollection._data != null 
               && arrayByteListWithCollection._data.length == byteCollection.size());
    assertEquals("ArrayByteList should contain the same elements as the ByteCollection", 1, arrayByteListWithCollection.get(0));
    assertEquals("ArrayByteList should contain the same elements as the ByteCollection", 2, arrayByteListWithCollection.get(1));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testArrayByteList_InitialCapacityNegative() {
    new ArrayByteList(-1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGet_IndexNegative() {
    arrayByteList.get(-1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGet_IndexGreaterThanSize() {
    arrayByteList.get(0);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGet_IndexEqualToSize() {
    arrayByteList.add((byte) 1);
    arrayByteList.get(1);
  }
  
  @Test
  public void testGet_ValidIndex() {
    arrayByteList.add((byte) 1);
    arrayByteList.add((byte) 2);
    
    byte result = arrayByteList.get(1);
    assertEquals("Get method in ArrayByteList should return the correct element at the specified index", 2, result);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveElementAt_IndexNegative() {
    arrayByteList.removeElementAt(-1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveElementAt_IndexGreaterThanSize() {
    arrayByteList.removeElementAt(0);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveElementAt_IndexEqualToSize() {
    arrayByteList.add((byte) 1);
    arrayByteList.removeElementAt(1);
  }
  
  @Test
  public void testRemoveElementAt_ValidIndex() {
    arrayByteList.add((byte) 1);
    arrayByteList.add((byte) 2);
    
    byte result = arrayByteList.removeElementAt(0);
    assertEquals("RemoveElementAt method in ArrayByteList should return the correct element at the specified index", 1, result);
    assertEquals("RemoveElementAt method in ArrayByteList should update the size correctly", 1, arrayByteList.size());
    assertEquals("RemoveElementAt method in ArrayByteList should update the data array correctly", 2, arrayByteList._data[0]);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testSet_IndexNegative() {
    arrayByteList.set(-1, (byte) 1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testSet_IndexGreaterThanSize() {
    arrayByteList.set(0, (byte) 1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testSet_IndexEqualToSize() {
    arrayByteList.add((byte) 1);
    arrayByteList.set(1, (byte) 2);
  }
  
  @Test
  public void testSet_ValidIndex() {
    arrayByteList.add((byte) 1);
    arrayByteList.add((byte) 2);
    
    byte result = arrayByteList.set(0, (byte) 3);
    assertEquals("Set method in ArrayByteList should return the correct element at the specified index", 1, result);
    assertEquals("Set method in ArrayByteList should update the data array correctly", 3, arrayByteList._data[0]);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testAdd_IndexNegative() {
    arrayByteList.add(-1, (byte) 1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testAdd_IndexGreaterThanSize() {
    arrayByteList.add(1, (byte) 1);
  }
  
  @Test
  public void testAdd_IndexEqualToSize() {
    arrayByteList.add((byte) 1);
    arrayByteList.add((byte) 2);
    
    arrayByteList.add(2, (byte) 3);
    assertEquals("Add method in ArrayByteList should update the size correctly", 3, arrayByteList.size());
    assertEquals("Add method in ArrayByteList should update the data array correctly", 3, arrayByteList._data[2]);
  }
  
  @Test
  public void testAdd_ValidIndex() {
    arrayByteList.add((byte) 1);
    arrayByteList.add((byte) 2);
    
    arrayByteList.add(1, (byte) 3);
    assertEquals("Add method in ArrayByteList should update the size correctly", 3, arrayByteList.size());
    assertEquals("Add method in ArrayByteList should update the data array correctly", 3, arrayByteList._data[1]);
    assertEquals("Add method in ArrayByteList should update the data array correctly", 2, arrayByteList._data[2]);
  }
  
  @Test
  public void testEnsureCapacity_CurrentCapacityGreaterThanMinCap() {
    arrayByteList.ensureCapacity(0);
    assertEquals("EnsureCapacity method in ArrayByteList should not update data array if current capacity is already greater than mincap", 8, arrayByteList._data.length);
  }
  
  @Test
  public void testEnsureCapacity_CurrentCapacityEqualToMinCap() {
    arrayByteList.add((byte) 1);
    arrayByteList.ensureCapacity(1);
    assertEquals("EnsureCapacity method in ArrayByteList should not update data array if current capacity is equal to mincap", 8, arrayByteList._data.length);
  }
  
  @Test
  public void testEnsureCapacity_CurrentCapacityLessThanMinCap() {
    arrayByteList.add((byte) 1);
    arrayByteList.ensureCapacity(16);
    assertTrue("EnsureCapacity method in ArrayByteList should update data array if current capacity is less than mincap", arrayByteList._data.length >= 16);
  }
  
  @Test
  public void testTrimToSize_SizeEqualToDataLength() {
    arrayByteList.add((byte) 1);
    arrayByteList.add((byte) 2);
    
    arrayByteList.trimToSize();
    assertEquals("TrimToSize method in ArrayByteList should update data array length to be equal to size", 2, arrayByteList._data.length);
  }
  
  @Test
  public void testTrimToSize_SizeLessThanDataLength() {
    arrayByteList.ensureCapacity(16);
    arrayByteList.trimToSize();
    assertEquals("TrimToSize method in ArrayByteList should update data array to have size elements", arrayByteList.size(), arrayByteList._data.length);
  }
  
  @Test
  public void testWriteObject() {
    // TODO: Implement test for the writeObject method
  }
  
  @Test
  public void testReadObject() {
    // TODO: Implement test for the readObject method
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testCheckRange_IndexNegative() {
    arrayByteList.checkRange(-1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testCheckRange_IndexGreaterThanSize() {
    arrayByteList.checkRange(0);
  }
  
  @Test
  public void testCheckRange_IndexEqualToSize() {
    arrayByteList.add((byte) 1);
    arrayByteList.checkRange(1);
  }
  
  @Test
  public void testCheckRange_ValidIndex() {
    arrayByteList.add((byte) 1);
    arrayByteList.add((byte) 2);
    
    arrayByteList.checkRange(1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testCheckRangeIncludingEndpoint_IndexNegative() {
    arrayByteList.checkRangeIncludingEndpoint(-1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testCheckRangeIncludingEndpoint_IndexGreaterThanSize() {
    arrayByteList.checkRangeIncludingEndpoint(1);
  }
  
  @Test
  public void testCheckRangeIncludingEndpoint_IndexEqualToSize() {
    arrayByteList.add((byte) 1);
    arrayByteList.checkRangeIncludingEndpoint(1);
  }
  
  @Test
  public void testCheckRangeIncludingEndpoint_ValidIndex() {
    arrayByteList.add((byte) 1);
    arrayByteList.add((byte) 2);
    
    arrayByteList.checkRangeIncludingEndpoint(1);
    arrayByteList.checkRangeIncludingEndpoint(2);
  }
  
}