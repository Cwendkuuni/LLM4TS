package org.jsoup;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class UncheckedIOExceptionTest {

    @Test
    public void ioException_ReturnsCauseIOException() {
        // Arrange
        IOException expectedCause = new IOException();
        UncheckedIOException uncheckedIOException = new UncheckedIOException(expectedCause);

        // Act
        IOException actualCause = uncheckedIOException.ioException();

        // Assert
        assertNotNull(actualCause);
        assertEquals(expectedCause, actualCause);
    }

    @Test
    public void constructor_SetsCauseAndMessage() {
        // Arrange
        IOException expectedCause = new IOException("Some IO exception message");

        // Act
        UncheckedIOException uncheckedIOException = new UncheckedIOException(expectedCause);

        // Assert
        assertNotNull(uncheckedIOException.getCause());
        assertEquals(expectedCause, uncheckedIOException.getCause());
        assertEquals(expectedCause.getMessage(), uncheckedIOException.getMessage());
    }

}