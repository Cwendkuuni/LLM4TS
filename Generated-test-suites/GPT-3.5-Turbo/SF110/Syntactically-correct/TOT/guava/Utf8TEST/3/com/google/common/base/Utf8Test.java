import org.junit.Assert;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Typical use case
        Assert.assertEquals(6, Utf8.encodedLength("Hello!"));

        // Edge case
        Assert.assertEquals(0, Utf8.encodedLength(""));

        // Error scenario
        try {
            Utf8.encodedLength(null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testIsWellFormed() {
        // Typical use case
        Assert.assertTrue(Utf8.isWellFormed(new byte[]{72, 101, 108, 108, 111}));

        // Edge case
        Assert.assertTrue(Utf8.isWellFormed(new byte[]{}));

        // Error scenario
        Assert.assertFalse(Utf8.isWellFormed(new byte[]{72, -115, 108, 108, 111}));

    }
}