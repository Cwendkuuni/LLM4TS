import org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {
    @Test
    public void testAreEqual_SameObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_NullObjects() {
        assertFalse(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equala.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_DifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    // Add tests for other methods here
}