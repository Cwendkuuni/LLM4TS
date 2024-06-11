package org.jsoup;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class UncheckedIOExceptionTest {

    private IOException testCause;
    private UncheckedIOException uncheckedIOException;

    @Before
    public void setUp() throws IOException {
        testCause = new IOException("Test IOException");
        uncheckedIOException = new UncheckedIOException(testCause);
    }

    @Test
    public void ioException_Returns_CorrectCause() {
        assertEquals(testCause, uncheckedIOException.ioException());
    }

    @Test
    public void constructor_Sets_CorrectCause() {
        assertEquals(testCause, uncheckedIOException.getCause());
    }

    @Test
    public void constructor_Sets_ThrowableMessage() {
        assertEquals(testCause.getMessage(), uncheckedIOException.getMessage());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_Throws_NullPointerException_WhenCauseIsNull() {
        new UncheckedIOException(null);
    }

    @Test
    public void constructor_Throws_UncheckedIOException_WhenCauseIsSet() {
        try {
            throw uncheckedIOException;
        } catch (UncheckedIOException e) {
            assertNotNull(e);
            assertEquals(testCause, e.getCause());
        }
    }

}