package org.jsoup.test;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

import org.jsoup.UncheckedIOException;
import org.junit.Test;

public class UncheckedIOExceptionTest {

    @Test
    public void testUncheckedIOExceptionCreation() {
        IOException cause = new IOException("Test IOException");
        UncheckedIOException uncheckedIOException = new UncheckedIOException(cause);

        assertInstanceOf(IOException.class, uncheckedIOException.ioException());
        assertInstanceOf(UncheckedIOException.class, uncheckedIOException);
        assertInstanceOf(RuntimeException.class, uncheckedIOException);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionThrownWhenGettingCause() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(new IOException("Test IOException"));
        assertThrows(IOException.class, () -> uncheckedIOException.getCause());
    }

    @Test
    public void testToThrowUncheckedIOExceptionWhenMethodThrowsCheckedIOException() {
        IOException ioException = new IOException("Test IOException");
        AssertionError assertionError = assertThrows(AssertionError.class, () -> {
            throw new UncheckedIOException(ioException);
        });

        assertInstanceOf(UncheckedIOException.class, assertionError.getCause());
    }

    @Test
    public void testToThrowUncheckedIOExceptionWhenMethodThrowsOtherCheckedException() {
        Class<RuntimeException> checkedExceptionType = RuntimeException.class;
        IOException ioException = new IOException("Test IOException");

        assertThrows(checkedExceptionType, () -> {
            throw new UncheckedIOException(ioException);
        });

        assertThrows(checkedExceptionType, () -> {
            throw new UncheckedIOException(new RuntimeException("Test RuntimeException"));
        });
    }
}