package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;

public class UncheckedIOExceptionTest {
    
    // Test constructor
    @Test
    public void testConstructor() {
        IOException ioExceptionMock = mock(IOException.class);
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioExceptionMock);
        
        assertEquals(ioExceptionMock, uncheckedIOException.ioException());
    }
    
    // Test ioException method
    @Test
    public void testIoException() {
        IOException ioExceptionMock = mock(IOException.class);
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioExceptionMock);
        
        assertEquals(ioExceptionMock, uncheckedIOException.ioException());
    }
    
    // Test that the cause of the UncheckedIOException is an IOException
    @Test
    public void testCauseIsIOException() {
        IOException ioExceptionMock = mock(IOException.class);
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioExceptionMock);
        
        assertEquals(IOException.class, uncheckedIOException.getCause().getClass());
    }
    
    // Test that the cause of the UncheckedIOException is the provided IOException
    @Test
    public void testCauseIsProvidedException() {
        IOException ioExceptionMock = mock(IOException.class);
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioExceptionMock);
        
        assertEquals(ioExceptionMock, uncheckedIOException.getCause());
    }
    
    // Test that the stack trace of the UncheckedIOException is preserved
    @Test
    public void testStackTracePreserved() {
        IOException ioExceptionMock = mock(IOException.class);
        
        try {
            throw ioExceptionMock;
        } catch (IOException e) {
            UncheckedIOException uncheckedIOException = new UncheckedIOException(e);
            
            assertEquals(e.getStackTrace()[0], uncheckedIOException.getStackTrace()[0]);
        }
    }
    
    // Test that a new UncheckedIOException is thrown when attempting to get the cause of null
    @Test
    public void testNullCause() {
        assertThrows(UncheckedIOException.class, () -> {
            new UncheckedIOException(null);
        });
    }
}