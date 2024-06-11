
package org.apache.commons.csv;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() {
        // Test normal read
        // Test single character read
        // Test end of stream
    }

    @Test
    public void testReadAgain() {
        // Test reading the last character after read
        // Test reading the last character after end of stream
    }

    @Test
    public void testReadCharArray() {
        // Test normal read
        // Test read line separator
        // Test read line separator with different line endings
        // Test read end of stream
    }

    @Test
    public void testReadLine() {
        // Test normal read
        // Test empty line
        // Test end of stream
    }

    @Test
    public void testLookAhead() {
        // Test normal lookAhead
        // Test end of stream
    }

    @Test
    public void testGetLineNumber() {
        // Test line number after normal read
        // Test line number after end of stream
    }
}
