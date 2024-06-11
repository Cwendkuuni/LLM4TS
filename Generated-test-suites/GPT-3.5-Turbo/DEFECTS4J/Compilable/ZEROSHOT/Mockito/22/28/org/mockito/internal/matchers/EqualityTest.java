package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs_ReturnsTrue() {
        Object o1 = null;
        Object o2 = null;

        boolean result = Equality.areEqual(o1, o2);

        assertEquals(true, result);
    }

    @Test
    public void testAreEqual_OneNullInput_ReturnsFalse() {
        Object o1 = new Object();
        Object o2 = null;

        boolean result = Equality.areEqual(o1, o2);

        assertEquals(false, result);
    }

    @Test
    public void testAreEqual_BothArrays_ReturnsTrue() {
        Object[] o1 = {1, 2, 3};
        Object[] o2 = {1, 2, 3};

        boolean result = Equality.areEqual(o1, o2);

        assertEquals(true, result);
    }

    @Test
    public void testAreEqual_OneArrayOneObject_ReturnsFalse() {
        Object[] o1 = {1, 2, 3};
        Object o2 = new Object();

        boolean result = Equality.areEqual(o1, o2);

        assertEquals(false, result);
    }

    @Test
    public void testAreEqual_BothObjects_ReturnsTrue() {
        Object o1 = "Hello";
        Object o2 = "Hello";

        boolean result = Equality.areEqual(o1, o2);

        assertEquals(true, result);
    }

    @Test
    public void testAreEqual_DifferentObjects_ReturnsFalse() {
        Object o1 = "Hello";
        Object o2 = "World";

        boolean result = Equality.areEqual(o1, o2);

        assertEquals(false, result);
    }

}