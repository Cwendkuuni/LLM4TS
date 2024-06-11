package org.apache.commons.csv;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setup() {
        try {
            parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParse() throws IOException {
        assertEquals(0, parser.getRecordNumber());

        assertTrue(parser.iterator().hasNext());
    }

    @Test
    public void testGetRecords() throws IOException {
        assertNotNull(parser.getRecords());
        assertFalse(parser.getRecords().isEmpty());
    }

    @Test
    public void testGetHeaderMap() {
        assertNotNull(parser.getHeaderMap());
        assertTrue(parser.getHeaderMap().containsKey("header1"));
        assertTrue(parser.getHeaderMap().containsKey("header2"));
    }

    @Test
    public void testGetCurrentLineNumber() {
        assertEquals(1, parser.getCurrentLineNumber());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(0, parser.getRecordNumber());
    }

    @Test
    public void testIsClosed() {
        assertFalse(parser.isClosed());
    }

    @Test
    public void testIterator() {
        assertNotNull(parser.iterator());
        assertTrue(parser.iterator().hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextRecordThrowsException() throws IOException {
        CSVParser closedParser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT);
        closedParser.close();
        closedParser.nextRecord();
    }

    @Test
    public void testClose() throws IOException {
        parser.close();
        assertTrue(parser.isClosed());
    }

}