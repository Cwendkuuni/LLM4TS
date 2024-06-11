package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(Equality.areEqual(obj1, obj2));

        obj1 = "Hello";
        obj2 = "Hello";
        assertTrue(Equality.areEqual(obj1, obj2));

        obj1 = new int[]{1, 2, 3};
        obj2 = new int[]{1, 2, 3};
        assertTrue(Equality.areEqual(obj1, obj2));
    }
}