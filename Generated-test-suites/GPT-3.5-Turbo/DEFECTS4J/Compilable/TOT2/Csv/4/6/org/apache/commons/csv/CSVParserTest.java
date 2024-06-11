package org.apache.commons.csv;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "Name, Age, Gender\nJohn, 30, Male\nJane, 25, Female";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFile() throws IOException {
        File file = new File("invalid.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
    }

    @Test
    public void testGetRecords() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        assertEquals(2, parser.getRecords().size());
    }

    @Test
    public void testIterator() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        int recordCount = 0;
        for (CSVRecord record : parser) {
            recordCount++;
        }
        assertEquals(2, recordCount);
    }

    @Test
    public void testClose() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        parser.close();
        assertTrue(parser.isClosed());
    }

}