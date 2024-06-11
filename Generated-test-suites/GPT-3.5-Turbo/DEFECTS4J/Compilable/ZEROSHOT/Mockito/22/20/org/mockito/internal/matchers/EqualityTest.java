package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {
    
    @Test
    public void testAreEqual_NullObjects_ReturnsTrue() {
        Object o1 = null;
        Object o2 = null;
        
        assertTrue(Equality.areEqual(o1, o2));
    }
    
    @Test
    public void testAreEqual_OneObjectNull_ReturnsFalse() {
        Object o1 = new Object();
        Object o2 = null;
        
        assertFalse(Equality.areEqual(o1, o2));
    }
    
    @Test
    public void testAreEqual_TwoDifferentObjects_ReturnsFalse() {
        Object o1 = new Object();
        Object o2 = new Object();
        
        assertFalse(Equality.areEqual(o1, o2));
    }
    
    @Test
    public void testAreEqual_TwoEqualObjects_ReturnsTrue() {
        Object o1 = new Object();
        Object o2 = o1;
        
        assertTrue(Equality.areEqual(o1, o2));
    }
    
    @Test
    public void testAreEqual_TwoEqualArrays_ReturnsTrue() {
        int[] o1 = {1, 2, 3};
        int[] o2 = {1, 2, 3};
        
        assertTrue(Equality.areEqual(o1, o2));
    }
    
    @Test
    public void testAreEqual_TwoDifferentArrays_ReturnsFalse() {
        int[] o1 = {1, 2, 3};
        int[] o2 = {4, 5, 6};
        
        assertFalse(Equality.areEqual(o1, o2));
    }
    
    @Test
    public void testAreEqual_TwoArraysDifferentLength_ReturnsFalse() {
        int[] o1 = {1, 2, 3};
        int[] o2 = {1, 2, 3, 4};
        
        assertFalse(Equality.areEqual(o1, o2));
    }
    
    @Test
    public void testAreEqual_TwoEqualNestedArrays_ReturnsTrue() {
        int[][] o1 = {{1, 2}, {3, 4}};
        int[][] o2 = {{1, 2}, {3, 4}};
        
        assertTrue(Equality.areEqual(o1, o2));
    }
    
    @Test
    public void testAreEqual_TwoDifferentNestedArrays_ReturnsFalse() {
        int[][] o1 = {{1, 2}, {3, 4}};
        int[][] o2 = {{5, 6}, {7, 8}};
        
        assertFalse(Equality.areEqual(o1, o2));
    }
    
    @Test
    public void testAreEqual_TwoNestedArraysDifferentLength_ReturnsFalse() {
        int[][] o1 = {{1, 2}, {3, 4}};
        int[][] o2 = {{1, 2, 3}, {4, 5, 6}};
        
        assertFalse(Equality.areEqual(o1, o2));
    }
}