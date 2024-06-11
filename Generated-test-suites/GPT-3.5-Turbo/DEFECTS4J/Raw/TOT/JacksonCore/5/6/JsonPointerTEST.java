
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;

        // Test matches method
        Assert.assertTrue(pointer.matches());

        // Test getMatchingProperty method
        Assert.assertEquals("", pointer.getMatchingProperty());

        // Test getMatchingIndex method
        Assert.assertEquals(-1, pointer.getMatchingIndex());

        // Test mayMatchProperty method
        Assert.assertFalse(pointer.mayMatchProperty());

        // Test mayMatchElement method
        Assert.assertFalse(pointer.mayMatchElement());

        // Test matchProperty method
        Assert.assertNull(pointer.matchProperty("name"));

        // Test matchElement method
        Assert.assertNull(pointer.matchElement(0));

        // Test tail method
        Assert.assertNull(pointer.tail());
    }

    @Test
    public void testNonEmptyJsonPointer() {
        JsonPointer pointer = JsonPointer.compile("/address/street");

        // Test matches method
        Assert.assertFalse(pointer.matches());

        // Test getMatchingProperty method
        Assert.assertEquals("", pointer.getMatchingProperty());

        // Test getMatchingIndex method
        Assert.assertEquals(-1, pointer.getMatchingIndex());

        // Test mayMatchProperty method
        Assert.assertTrue(pointer.mayMatchProperty());

        // Test mayMatchElement method
        Assert.assertFalse(pointer.mayMatchElement());

        // Test matchProperty method
        Assert.assertEquals(pointer, pointer.matchProperty("address"));

        // Test matchElement method
        Assert.assertNull(pointer.matchElement(0));

        // Test tail method
        Assert.assertNotNull(pointer.tail());
    }

    @Test
    public void testInvalidJsonPointer() {
        try {
            JsonPointer pointer = JsonPointer.compile("address/street");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test success
        } catch (Exception e) {
            Assert.fail("Expected IllegalArgumentException, but got " + e.getClass().getName());
        }
    }
}

