package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws Exception {
        String csvFilePath = "src/test/resources/sampleCSV.csv";
        List<CSVRecord> records = CSVParser.parse(new File(csvFilePath), CSVFormat.RFC4180).getRecords();
        assertArrayEquals(new String[]{"Name", "Age", "City"}, records.get(0).toArray());
        assertEquals("John Doe", records.get(0).getRecordNumber(), 1);
        assertEquals("25", records.get(0).get(0));
        assertEquals("New York", records.get(0).get(1));
        assertNull(records.get(0).get(2));

        assertEquals("Sarah Smith", records.get(1).getRecordNumber(), 2);
        assertEquals("30", records.get(1).get(0));
        assertEquals("Chicago", records.get(1).get(1));
        assertEquals("IL", records.get(1).get(2));
    }

    @Test
    public void testParseString() throws Exception {
        String csvString = "Name,Age,City\nJohn Doe,25,New York\nSarah Smith,30,Chicago,IL";
        List<CSVRecord> records = new CSVParser(new StringReader(csvString), CSVFormat.RFC4180).getRecords();
        assertArrayEquals(new String[]{"Name", "Age", "City"}, records.get(0).toArray());
        assertEquals("John Doe", records.get(0).getRecordNumber(), 1);
        assertEquals("25", records.get(0).get(0));
        assertEquals("New York", records.get(0).get(1));
        assertNull(records.get(0).get(2));

        assertEquals("Sarah Smith", records.get(1).getRecordNumber(), 2);
        assertEquals("30", records.get(1).get(0));
        assertEquals("Chicago", records.get(1).get(1));
        assertEquals("IL", records.get(1).get(2));
    }

    @Test
    public void testParseURL() throws Exception {
        String csvURL = "https://example.com/sampleCSV.csv";
        List<CSVRecord> records = CSVParser.parse(new URL(csvURL), CSVFormat.RFC4180).getRecords();
        // Replace the following lines with your assertions

        // ...
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFormat() throws Exception {
        String csvString = "Name,Age,City\nJohn Doe,25,New York\nSarah Smith,30,Chicago,IL\ninvalidFormat";
        new CSVParser(new StringReader(csvString), CSVFormat.RFC4180);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() throws Exception {
        new CSVParser(new StringReader("Name,Age,City\nJohn Doe,25,New York\nSarah Smith,30,Chicago,IL"), null);
    }
}