
package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {
    
    private ArArchiveInputStream arStream;

    @Before
    public void setUp() {
        InputStream mockInputStream = mock(InputStream.class);
        arStream = new ArArchiveInputStream(mockInputStream);
    }
    
    @Test
    public void testGetNextArEntry() throws IOException {
        assertEquals(ArArchiveEntry.class, arStream.getNextArEntry().getClass());
        
        // Test header read check
        InputStream inputStream = new ByteArrayInputStream(ArArchiveEntry.HEADER.getBytes());
        arStream = new ArArchiveInputStream(inputStream);
        assertNull(arStream.getNextArEntry()); // Header already read
        
        // Test available() method check
        InputStream inputStreamAvailable = mock(InputStream.class);
        when(inputStreamAvailable.available()).thenReturn(0);
        arStream = new ArArchiveInputStream(inputStreamAvailable);
        assertNull(arStream.getNextArEntry());
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(ArArchiveEntry.HEADER.getBytes());
        arStream = new ArArchiveInputStream(inputStream);
        arStream.getNextArEntry();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        assertEquals(ArchiveEntry.class, arStream.getNextArEntry().getClass());
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStreamClose = mock(InputStream.class);
        arStream = new ArArchiveInputStream(inputStreamClose);
        
        arStream.close();
        
        assertFalse(arStream.closed);
    }

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0, 1});
        arStream = new ArArchiveInputStream(inputStream);
        
        assertEquals(0, arStream.read());
        assertEquals(1, arStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0, 1});
        arStream = new ArArchiveInputStream(inputStream);
        byte[] buffer = new byte[2];
        
        arStream.read(buffer);
        
        assertEquals(Arrays.toString(new byte[]{0, 1}), Arrays.toString(buffer));
    }

    @Test
    public void testReadByteArrayIntInt() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0, 1});
        arStream = new ArArchiveInputStream(inputStream);
        byte[] buffer = new byte[2];
        
        arStream.read(buffer, 0, 2);
        
        assertEquals(Arrays.toString(new byte[]{0, 1}), Arrays.toString(buffer));
    }

    @Test
    public void testMatches() {
        assertFalse(ArArchiveInputStream.matches(new byte[]{(byte)0x00, (byte)0x01}, 1));   
    }
    
}
