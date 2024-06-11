import org.junit.Test;
import org.junit.Assert;
import org.jsoup.UncheckedIOException;
import java.io.IOException;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionConstructor() {
        IOException cause = new IOException();
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        Assert.assertEquals(cause, uncheckedException.ioException());
    }

    @Test
    public void testIoExceptionGetMethod() {
        IOException cause = new IOException();
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedException.ioException());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionFromUncheckedException() {
        IOException cause = new IOException();
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        uncheckedException.ioException();
    }

    @Test
    public void testEmptyConstructor() {
        UncheckedIOException uncheckedException = new UncheckedIOException();
        Assert.assertNull(uncheckedException.ioException());
    }

    @Test
    public void testNullConstructor() {
        try {
            UncheckedIOException uncheckedException = new UncheckedIOException(null);
            Assert.fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testToStringMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        String expected = "Unexpected IO Error: Test IOException";
        String actual = uncheckedException.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalizedMessageMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        String expected = "Unexpected IO Error: Test IOException";
        String actual = uncheckedException.getLocalizedMessage();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        String expected = "Unexpected IO Error: Test IOException";
        String actual = uncheckedException.getMessage();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasDetailsMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        Assert.assertTrue(uncheckedException.hasDetails());
    }

    @Test
    public void testHasRootCauseMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        Assert.assertFalse(uncheckedException.hasRootCause());
    }

    @Test
    public void testGetCauseMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        Assert.assertSame(cause, uncheckedException.getCause());
    }

    @Test
    public void testGetSuppressedMethods() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        Assert.assertArrayEquals(new Exception[0], uncheckedException.getSuppressed());
    }

    @Test
    public void testEqualsMethod() {
        IOException cause1 = new IOException("Test IOException 1");
        IOException cause2 = new IOException("Test IOException 2");
        UncheckedIOException uncheckedException1 = new UncheckedIOException(cause1);
        UncheckedIOException uncheckedException2 = new UncheckedIOException(cause2);
        Assert.assertNotEquals(uncheckedException1, uncheckedException2);

        UncheckedIOException uncheckedException3 = new UncheckedIOException(cause1);
        Assert.assertEquals(uncheckedException1, uncheckedException3);
    }

    @Test
    public void testHashCodeMethod() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedException1 = new UncheckedIOException(cause);
        UncheckedIOException uncheckedException2 = new UncheckedIOException(cause);
        Assert.assertEquals(uncheckedException1.hashCode(), uncheckedException2.hashCode());
    }

    @Test
    public void testNullCauseMethod() {
        UncheckedIOException uncheckedException = new UncheckedIOException();
        Assert.assertNull(uncheckedException.getCause());
    }

    @Test
    public void testEmptyCauseMessageMethod() {
        IOException cause = new IOException();
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        String expected = "Unexpected IO Error";
        String actual = uncheckedException.getMessage();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEmptyCauseDetailMessageMethod() {
        IOException cause = new IOException();
        UncheckedIOException uncheckedException = new UncheckedIOException(cause);
        String expected = "Unexpected IO Error";
        String actual = uncheckedException.getLocalizedMessage();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNullCauseDetailMessageMethod() {
        UncheckedIOException uncheckedException = new UncheckedIOException();
        String expected = "Unexpected IO Error";
        String actual = uncheckedException.getLocalizedMessage();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWrongCauseTypeMethod() {
        RuntimeException cause = new RuntimeException("Test RuntimeException");
        try {
            new UncheckedIOException(cause);
            Assert.fail("Should have thrown a ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testIOExceptionWithDetailMessageMethod() {
        IOException cause = new IOException("Test IOException Cause");
        String detailMessage = "Test Detail Message";
        UncheckedIOException uncheckedException = new UncheckedIOException(cause, detailMessage);
        String expected = "Unexpected IO Error: Test IOException Cause";
        String actual = uncheckedException.getMessage();
        Assert.assertEquals(expected, actual);

        expected = "Unexpected IO Error: Test IOException Cause\nDetail Message";
        actual = uncheckedException.getLocalizedMessage();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIOExceptionWithDetailMessageAndSuppressedExceptionMethod() {
        IOException cause = new IOException("Test IOException Cause");
        IOException suppressedException = new IOException("Test Suppressed Exception");
        UncheckedIOException uncheckedException = new UncheckedIOException(cause, "Test Detail Message", suppressedException);
        String expected = "Unexpected IO Error: Test IOException Cause";
        String actual = uncheckedException.getMessage();
        Assert.assertEquals(expected, actual);

        expected = "Unexpected IO Error: Test IOException Cause\nDetail Message";
        actual = uncheckedException.getLocalizedMessage();
        Assert.assertEquals(expected, actual);

        Assert.assertSame(suppressedException, uncheckedException.getSuppressed()[0]);
    }
}