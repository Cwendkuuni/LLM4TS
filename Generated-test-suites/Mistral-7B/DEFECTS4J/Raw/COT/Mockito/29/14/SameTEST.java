import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class SameTest {

    @Test
    public void testMatchesSameObjects() {
        Object wantedObject = new Object();
        Same sameMatcher = new Same(wantedObject);
        Object actualObject = wantedObject;
        assertTrue("should match same object", sameMatcher.matches(actualObject));
    }

    @Test
    public void testMatchesNullWantedObject() {
        Same sameMatcher = new Same(null);
        Object actualObject = new Object();
        assertFalse("should not match null wanted object", sameMatcher.matches(actualObject));
    }

    @Test
    public void testMatchesNullActualObject() {
        Object wantedObject = new Object();
        Same sameMatcher = new Same(wantedObject);
        assertFalse("should not match null actual object", sameMatcher.matches(null));
    }

    @Test
    public void testMatchesDifferentClasses() {
        Object wantedObject = new Object();
        Same sameMatcher = new Same(wantedObject);
        String actualObject = "different class";
        assertFalse("should not match different classes", sameMatcher.matches(actualObject));
    }

    @Test
    public void testMatchesDifferentObjectsWithSameValue() {
        Object wantedObject1 = new Object();
        Object wantedObject2 = new Object();
        Same sameMatcher = new Same(wantedObject1);
        Object actualObject = wantedObject2;
        assertFalse("should not match different objects with different references", sameMatcher.matches(actualObject));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWantedObjectThrowsIllegalArgumentException() {
        Same sameMatcher = new Same(null);
    }

    @Test
    public void testDescribeTo() {
        Object wantedObject = new Object();
        Same sameMatcher = new Same(wantedObject);
        Description description = new Description();
        sameMatcher.describeTo(description);
        String expectedDescription = "same(" + wantedObject.toString() + ")";
        String actualDescription = description.toString();
        assertEquals("description should match", expectedDescription, actualDescription);
    }
}