package net.sf.javaml.core;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        int noAttributes = 5;
        instance.setNoAttributes(noAttributes);
        assertEquals(noAttributes, instance.noAttributes());
    }

    // Add more test cases for other public methods here
    
    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }

    @Test
    public void testValueException() {
        SparseInstance instance = new SparseInstance();
        assertThrows(IllegalArgumentException.class, () -> {
            instance.value(-1);
        });
    }

    // Add more test cases for edge cases and exceptions handling here

}