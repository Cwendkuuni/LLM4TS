package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

    private static final String CSV_LINE_1 = "Name,Age,City";
    private static final String CSV_LINE_2 = "John,25,New York";
    private static final String CSV_LINE_3 = "Mike,30,Chicago";

    private BufferedReader originalReader;
    private ExtendedBufferedReader extendedReader;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() throws IOException {
        String csvContent = CSV_LINE_1 + System.lineSeparator() + CSV_LINE_2 + System.lineSeparator() + CSV_LINE_3;
        inputStream = new ByteArrayInputStream(csvContent.getBytes());
        originalReader = new BufferedReader(new InputStreamReader(inputStream));
        extendedReader = new ExtendedBufferedReader(originalReader);
    }

    @After
    public void tearDown() throws IOException {
        inputStream.close();
        extendedReader.close();
        originalReader.close();
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('N', extendedReader.read());
        assertEquals('a', extendedReader.read());
        // ... test all characters from the first line
        assertTrue(extendedReader.read() == '\n');

        assertEquals('J', extendedReader.read());
        // ... test all characters from the second line
        assertTrue(extendedReader.read() == '\n');

        assertEquals('M', extendedReader.read());
        // ... test all characters from the third line
        assertTrue(extendedReader.read() == '\n');
        assertTrue(extendedReader.read() == -1);
    }

    @Test
    public void testReadAgain() throws IOException {
        assertEquals('N', extendedReader.read());
        assertEquals('a', extendedReader.read());
        assertEquals('N', extendedReader.readAgain());
        assertEquals('a', extendedReader.readAgain());
        // ... test all characters from the first line
        assertTrue(extendedReader.read() == '\n');

        assertEquals('J', extendedReader.read());
        assertEquals('o', extendedReader.read());
        assertEquals('h', extendedReader.readAgain());
        assertEquals('n', extendedReader.readAgain());
        // ... test all characters from the second line
        assertTrue(extendedReader.read() == '\n');

        assertEquals('M', extendedReader.read());
        assertEquals('i', extendedReader.read());
        assertEquals('k', extendedReader.readAgain());
        assertEquals('e', extendedReader.readAgain());
        // ... test all characters from the third line
        assertTrue(extendedReader.read() == '\n');
        assertTrue(extendedReader.read() == -1);
    }

    @Test
    public void testReadLine() throws IOException {
        String line1 = extendedReader.readLine();
        assertEquals(CSV_LINE_1, line1);

        String line2 = extendedReader.readLine();
        assertEquals(CSV_LINE_2, line2);

        String line3 = extendedReader.readLine();
        assertEquals(CSV_LINE_3, line3);

        assertTrue(extendedReader.readLine() == null);
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals('N', extendedReader.lookAhead());
        assertEquals('a', extendedReader.lookAhead());

        extendedReader.read(); // read 'N'
        assertEquals('a', extendedReader.lookAhead());

        extendedReader.read(); // read 'a'
        extendedReader.read(); // read 'N'

        assertEquals('J', extendedReader.lookAhead());

        extendedReader.readLine(); // read the first line
        assertEquals('J', extendedReader.lookAhead());

        extendedReader.readLine(); // read the second line
        assertEquals('M', extendedReader.lookAhead());

        assertTrue(extendedReader.lookAhead() == -1); // EOF
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(0, extendedReader.getLineNumber());
        assertEquals(1, extendedReader.readLine() == null ? 0 : extendedReader.getLineNumber());
        assertEquals(2, extendedReader.readLine() == null ? 0 : extendedReader.getLineNumber());
        assertEquals(3, extendedReader.readLine() == null ? 0 : extendedReader.getLineNumber());
        assertEquals(-1, extendedReader.getLineNumber());
    }
}