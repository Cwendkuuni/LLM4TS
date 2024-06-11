package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        Object o1 = new Object();
        Object o2 = new Object();
        assertEquals(true, Equality.areEqual(o1, o2)); // same object

        Object o3 = new Object();
        Object o4 = new Object();
        assertEquals(false, Equality.areEqual(o3, o4)); // different objects

        Object obj1 = "HelloWorld";
        Object obj2 = "HelloWorld";
        assertEquals(true, Equality.areEqual(obj1, obj2)); // same string

        Object arr1[] = {1, 2, 3};
        Object arr2[] = {1, 2, 3};
        assertEquals(true, Equality.areEqual(arr1, arr2)); // same array
    }
}