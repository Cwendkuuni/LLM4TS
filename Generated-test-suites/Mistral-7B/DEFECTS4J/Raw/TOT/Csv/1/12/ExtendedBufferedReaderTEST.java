package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.apache.commons.csv.ExtendedBufferedReader.UNDEFINED;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @BeforeEach
    public void setUp() {
        String testData = "Line1\nLine2\nLine3\nLine4\n";
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));
    }

    @Test
    public void testRead() throws IOException {
        // Normal read
        Assertions.assertEquals('L', reader.read());
        Assertions.assertEquals('i', reader.read());
        Assertions.assertEquals('n', reader.read());
        Assertions.assertEquals('e', reader.read());
        Assertions.assertEquals('1', reader.read());
        Assertions.assertEquals('\n', reader.read());

        Assertions.assertEquals( reader.getLineNumber(), 1);
        Assertions.assertEquals( reader.readAgain(), '\n');

        // End of stream
        Assertions.assertEquals(UNDEFINED, reader.read());

        // Negative case
        Assertions.assertThrows(IOException.class, () -> reader.read(-1));
    }

    @Test
    public void testReadLine() throws IOException {
        String line = reader.readLine();
        Assertions.assertEquals("Line1", line);
        Assertions.assertEquals( reader.getLineNumber(), 1);

        line = reader.readLine();
        Assertions.assertEquals("Line2", line);
        Assertions.assertEquals( reader.getLineNumber(), 2);

        line = reader.readLine();
        Assertions.assertEquals("Line3", line);
        Assertions.assertEquals( reader.getLineNumber(), 3);

        line = reader.readLine();
        Assertions.assertEquals("Line4", line);
        Assertions.assertEquals( reader.getLineNumber(), 4);

        // End of stream
        Assertions.assertEquals(null, reader.readLine());
        Assertions.assertEquals( reader.getLineNumber(), 4);

        Assertions.assertEquals(UNDEFINED, reader.read());
    }

    @Test
    public void testLookAhead() throws IOException {
        int ch = reader.lookAhead();
        Assertions.assertEquals('L', ch);

        ch = reader.lookAhead();
        Assertions.assertEquals('i', ch);

        ch = reader.lookAhead();
        Assertions.assertEquals('n', ch);

        ch = reader.lookAhead();
        Assertions.assertEquals('e', ch);

        // End of stream
        Assertions.assertEquals(UNDEFINED, reader.lookAhead());

        // Negative case
        Assertions.assertThrows(IOException.class, () -> reader.lookAhead(1));
    }
}