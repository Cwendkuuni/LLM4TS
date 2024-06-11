
package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;
import java.io.IOException;

import org.junit.Test;

public class ExtendedBufferedReaderTest {
    
    @Test
    public void testRead() throws IOException {
        Reader reader = mock(Reader.class);
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        when(reader.read()).thenReturn(97);
        assertEquals(97, br.read());
        assertEquals(1, br.getLineNumber());
        
        when(reader.read()).thenReturn(98);
        assertEquals(98, br.read());
        assertEquals(1, br.getLineNumber());
        
        when(reader.read()).thenReturn(13);
        assertEquals(13, br.read());
        assertEquals(2, br.getLineNumber());
        
        when(reader.read()).thenReturn(10);
        assertEquals(10, br.read());
        assertEquals(2, br.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = mock(Reader.class);
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        when(reader.read()).thenReturn(97);
        assertEquals(97, br.read());
        assertEquals(1, br.getLineNumber());
        assertEquals(97, br.readAgain());
        
        when(reader.read()).thenReturn(98);
        assertEquals(98, br.read());
        assertEquals(1, br.getLineNumber());
        assertEquals(98, br.readAgain());
        
        when(reader.read()).thenReturn(13);
        assertEquals(13, br.read());
        assertEquals(2, br.getLineNumber());
        assertEquals(13, br.readAgain());
        
        when(reader.read()).thenReturn(10);
        assertEquals(10, br.read());
        assertEquals(2, br.getLineNumber());
        assertEquals(10, br.readAgain());
    }
    
    @Test
    public void testReadArray() throws IOException {
        Reader reader = mock(Reader.class);
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        char[] buf = {'a', 'b', 'c'};
        
        when(reader.read(buf, 0, 3)).thenReturn(3);
        assertEquals(3, br.read(buf, 0, 3));
        assertEquals(1, br.getLineNumber());
        
        when(reader.read(buf, 0, 3)).thenReturn(2);
        assertEquals(2, br.read(buf, 0, 3));
        assertEquals(1, br.getLineNumber());
        
        when(reader.read(buf, 0, 3)).thenReturn(1);
        assertEquals(1, br.read(buf, 0, 3));
        assertEquals(1, br.getLineNumber());
        
        when(reader.read(buf, 0, 3)).thenReturn(-1);
        assertEquals(-1, br.read(buf, 0, 3));
        assertEquals(-1, br.getLineNumber());
    }
    
    @Test
    public void testReadLine() throws IOException {
        Reader reader = mock(Reader.class);
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        when(reader.readLine()).thenReturn("line1");
        assertEquals("line1", br.readLine());
        assertEquals(1, br.getLineNumber());
        assertEquals('\n', br.readAgain());
        
        when(reader.readLine()).thenReturn("line2\r");
        assertEquals("line2\r", br.readLine());
        assertEquals(2, br.getLineNumber());
        assertEquals('\r', br.readAgain());
        
        when(reader.readLine()).thenReturn("line3\n");
        assertEquals("line3\n", br.readLine());
        assertEquals(3, br.getLineNumber());
        assertEquals('\n', br.readAgain());
        
        when(reader.readLine()).thenReturn(null);
        assertNull(br.readLine());
        assertEquals(4, br.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.readAgain());
    }
    
    @Test
    public void testLookAhead() throws IOException {
        Reader reader = mock(Reader.class);
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        when(reader.read()).thenReturn(97);
        when(reader.markSupported()).thenReturn(true);
        
        assertEquals(97, br.lookAhead());
        assertEquals('\0', br.readAgain());
    }
}
