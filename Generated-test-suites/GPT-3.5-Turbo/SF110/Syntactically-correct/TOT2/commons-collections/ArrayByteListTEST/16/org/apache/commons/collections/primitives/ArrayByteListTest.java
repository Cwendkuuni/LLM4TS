package org.apache.commons.collections.primitives;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testArrayByteListConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteListWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    // Add more test cases for other public methods

    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListNegativeInitialCapacity() {
        ArrayByteList list = new ArrayByteList(-10);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals(10, list.get(0));
    }

    // Additional test cases for edge cases and exceptions

}