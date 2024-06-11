package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_SameObject() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }
    
    @Test
    public void testAreEqual_NullObject() {
        Object obj = null;
        assertTrue(Equality.areEqual(obj, obj));
        assertTrue(Equality.areEqual(obj, null));
        assertTrue(Equality.areEqual(null, obj));
    }
    
    @Test
    public void testAreEqual_EqualObjects() {
        String str1 = "test";
        String str2 = new String("test");
        assertTrue(Equality.areEqual(str1, str2));
    }
    
    @Test
    public void testAreEqual_DifferentObjects() {
        String str1 = "test";
        String str2 = "Test";
        assertFalse(Equality.areEqual(str1, str2));
    }
    
    @Test
    public void testAreEqual_Arrays_SameLength() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }
    
    @Test
    public void testAreEqual_Arrays_DifferentLength() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areEqual(arr1, arr2));
    }
    
    @Test
    public void testAreEqual_Arrays_DifferentElements() {
        int[] arr1 = {1, 2, 3, 4};
        int[] arr2 = {1, 2, 5, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

}