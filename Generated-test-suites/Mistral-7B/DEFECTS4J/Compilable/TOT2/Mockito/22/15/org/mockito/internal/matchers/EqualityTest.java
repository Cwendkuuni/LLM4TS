package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        Object obj1 = new Object();
        Object obj2 = obj1;

        assertTrue(Equality.areEqual(obj1, obj2));

        Object str1 = "test";
        Object str2 = "test";

        assertTrue(Equality.areEqual(str1, str2));

        Object int1 = new Integer(10);
        Object int2 = new Integer(10);

        assertTrue(Equality.areEqual(int1, int2));
    }
}