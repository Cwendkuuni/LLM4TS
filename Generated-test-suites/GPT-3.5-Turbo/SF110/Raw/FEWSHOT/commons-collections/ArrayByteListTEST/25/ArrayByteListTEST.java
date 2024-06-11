package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayByteListTest {

  @Test
  public void testConstructor() {
    ArrayByteList list = new ArrayByteList();
    assertNotNull(list);
  }

  @Test
  public void testSize() {
    ArrayByteList list = new ArrayByteList();
    assertEquals(0, list.size());
    list.add(1);
    assertEquals(1, list.size());
  }

  @Test
  public void testGet() {
    ArrayByteList list = new ArrayByteList();
    list.add((byte) 1);
    byte result = list.get(0);
    assertEquals(1, result);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetOutOfBounds() {
    ArrayByteList list = new ArrayByteList();
    list.get(0);
  }

  @Test
  public void testRemoveElementAt() {
    ArrayByteList list = new ArrayByteList();
    list.add((byte) 1);
    byte result = list.removeElementAt(0);
    assertEquals(1, result);
    assertEquals(0, list.size());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveElementAtOutOfBounds() {
    ArrayByteList list = new ArrayByteList();
    list.removeElementAt(0);
  }

  @Test
  public void testSet() {
    ArrayByteList list = new ArrayByteList();
    list.add((byte) 1);
    byte result = list.set(0, (byte) 2);
    assertEquals(1, result);
    assertEquals(2, list.get(0));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSetOutOfBounds() {
    ArrayByteList list = new ArrayByteList();
    list.set(0, (byte) 2);
  }

  @Test
  public void testAdd() {
    ArrayByteList list = new ArrayByteList();
    list.add((byte) 1);
    assertEquals(1, list.size());
    assertEquals(1, list.get(0));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testAddOutOfBounds() {
    ArrayByteList list = new ArrayByteList();
    list.add(1, (byte) 2);
  }

  @Test
  public void testEnsureCapacity() {
    ArrayByteList list = new ArrayByteList();
    list.ensureCapacity(10);
    assertEquals(10, list._data.length);
  }

  @Test
  public void testTrimToSize() {
    ArrayByteList list = new ArrayByteList();
    list.ensureCapacity(10);
    list.trimToSize();
    assertEquals(0, list._data.length);
  }

  @Test
  public void testWriteObject() {
    // Not tested
  }

  @Test
  public void testReadObject() {
    // Not tested
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testCheckRange() {
    ArrayByteList list = new ArrayByteList();
    list.checkRange(0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testCheckRangeIncludingEndpoint() {
    ArrayByteList list = new ArrayByteList();
    list.checkRangeIncludingEndpoint(1);
  }
  
}