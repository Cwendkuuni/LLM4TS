import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstruction() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertEquals(cause, uncheckedIOException.ioException());
    }

    @Test
    public void testGetCauseMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedIOException.getCause());
    }

    // Edge cases and exception handling scenarios:
    // 1. Test null cause
    @Test(expected = NullPointerException.class)
    public void testNullCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
    }

    // 2. Test exception type other than IOException
    @Test(expected = ClassCastException.class)
    public void testExceptionTypeOtherThanIOException() {
        Exception cause = new Exception("Test Exception");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
    }

    // 3. Test multiple exceptions in the constructor
    @Test(expected = IllegalArgumentException.class)
    public void testMultipleExceptionsInConstructor() {
        IOException ioException1 = new IOException("Test IOException 1");
        IOException ioException2 = new IOException("Test IOException 2");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException1, ioException2);
    }

    // 4. Test toString() method with different message and cause
    @Test
    public void testToStringMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        String expected = "org.jsoup.UncheckedIOException: Test IOException";
        String actual = uncheckedIOException.toString();
        Assert.assertEquals(expected, actual);
    }

    // 5. Test equals() method with different instances
    @Test
    public void testEqualsMethod() {
        IOException cause1 = new IOException("Test IOException 1");
        IOException cause2 = new IOException("Test IOException 2");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(cause1);
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(cause2);
        Assert.assertFalse(uncheckedIOException1.equals(uncheckedIOException2));

        UncheckedIOException uncheckedIOException3 = new UncheckedIOException(cause1);
        Assert.assertTrue(uncheckedIOException1.equals(uncheckedIOException3));
    }

    // 6. Test equals() method with same instance
    @Test
    public void testEqualsMethodSameInstance() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Assert.assertTrue(uncheckedIOException.equals(uncheckedIOException));
    }

    // 7. Test equals() method with other objects
    @Test
    public void testEqualsMethodOtherObjects() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);
        Object otherObject = new Object();
        Assert.assertFalse(uncheckedIOException.equals(otherObject));
    }

    // 8. Test hashCode() method
    @Test
    public void testHashCodeMethod() {
        IOException cause1 = new IOException("Test IOException 1");
        IOException cause2 = new IOException("Test IOException 2");
        UncheckedIOException uncheckedIOException1 = new UncheckedIOException(cause1);
        UncheckedIOException uncheckedIOException2 = new UncheckedIOException(cause2);
        Assert.assertNotSame(uncheckedIOException1.hashCode(), uncheckedIOException2.hashCode());

        UncheckedIOException uncheckedIOException3 = new UncheckedIOException(cause1);
        Assert.assertEquals(uncheckedIOException1.hashCode(), uncheckedIOException3.hashCode());
    }
}