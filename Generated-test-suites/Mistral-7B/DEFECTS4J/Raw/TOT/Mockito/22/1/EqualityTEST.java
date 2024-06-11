import org.junit.Test;
import static org.mockito.internal.matchers.Equality.*;
import static org.junit.Assert.*;

public class EqualityTest {
    // Testing areEqual method with null objects
    @Test
    public void testAreEqualWithNullObjects() {
        assertTrue(areEqual(null, null));
        assertFalse(areEqual(null, new Object()));
        assertFalse(areEqual(new Object(), null));
    }

    // Testing areEqual method with simple objects
    @Test
    public void testAreEqualWithSimpleObjects() {
        String s1 = new String("Hello");
        String s2 = new String("Hello");
        assertTrue(areEqual(s1, s2));

        String s3 = new String("World");
        assertFalse(areEqual(s1, s3));
    }

    // Testing areEqual method with arrays of primitive types
    @Test
    public void testAreEqualWithArraysOfPrimitives() {
        int[] a1 = {1, 2, 3};
        int[] a2 = {1, 2, 3};
        assertTrue(areEqual(a1, a2));

        int[] a3 = {1, 2, 3, 4};
        assertFalse(areEqual(a1, a3));
    }

    // Testing areEqual method with arrays of objects
    @Test
    public void testAreEqualWithArraysOfObjects() {
        String[] a1 = {"Hello", "World"};
        String[] a2 = {"Hello", "World"};
        assertTrue(areEqual(a1, a2));

        String[] a3 = {"Hello", "World", "Mockito"};
        assertFalse(areEqual(a1, a3));
    }

    // Testing areEqual method with objects of different classes
    @Test
    public void testAreEqualWithObjectsOfDifferentClasses() {
        String s1 = new String("Hello");
        Integer i1 = new Integer(1);

        assertFalse(areEqual(s1, i1));
    }

    // Testing areArraysEqual method with arrays of different lengths
    @Test
    public void testAreArraysEqualWithArraysOfDifferentLengths() {
        int[] a1 = {1, 2, 3};
        int[] a2 = {1, 2, 3, 4};
        assertFalse(areArraysEqual(a1, a2));
    }

    // Testing areArraysEqual method with arrays containing different elements
    @Test
    public void testAreArraysEqualWithArraysContainingDifferentElements() {
        int[] a1 = {1, 2, 3};
        int[] a2 = {1, 3, 3};
        assertFalse(areArraysEqual(a1, a2));
    }
}