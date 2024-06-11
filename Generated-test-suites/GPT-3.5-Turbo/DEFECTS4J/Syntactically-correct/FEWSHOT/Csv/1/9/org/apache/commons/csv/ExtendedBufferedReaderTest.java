
package org.apache.commons.csv;

import java.io.IOException;
import java.io.Reader;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ExtendedBufferedReaderTest {

    @Mock
    private Reader mockReader;

    private ExtendedBufferedReader extendedBufferedReader;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(97, extendedBufferedReader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        extendedBufferedReader.read();
        assertEquals(97, extendedBufferedReader.readAgain());
    }

    @Test
    public void testRead_withCharArray() throws IOException {
        char[] buf = new char[]{'a', 'b', 'c'};
        assertEquals(3, extendedBufferedReader.read(buf, 0, 3));
    }

    @Test
    public void testReadAgain_withCharArray() throws IOException {
        char[] buf = new char[]{'a', 'b', 'c'};
        extendedBufferedReader.read(buf, 0, 3);
        assertEquals(99, extendedBufferedReader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        String line = extendedBufferedReader.readLine();
        assertEquals("test line", line);
    }

    @Test
    public void testReadLine_withEmptyLine() throws IOException {
        String line = extendedBufferedReader.readLine();
        assertEquals("", line);
    }

    @Test
    public void testLookAhead() throws IOException {
        extendedBufferedReader.read();
        int c = extendedBufferedReader.lookAhead();
        assertEquals(98, c);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        extendedBufferedReader.readLine();
        int lineNumber = extendedBufferedReader.getLineNumber();
        assertEquals(1, lineNumber);
    }
}
