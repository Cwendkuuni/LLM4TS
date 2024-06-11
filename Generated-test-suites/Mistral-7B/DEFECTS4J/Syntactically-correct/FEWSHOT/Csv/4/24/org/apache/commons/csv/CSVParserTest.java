package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("testCSVFile.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        assertEquals(3, records.size());
        assertEquals("Col1,Col2,Col3", firstRecord.getHeaderMap().get("Col1").toString());
        assertEquals("Value1,Value2,Value3", Arrays.toString(firstRecord.toArray()));
        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Col1,Col2,Col3\nValue1,Value2,Value3";
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        assertEquals(3, records.size());
        assertEquals("Col1,Col2,Col3", firstRecord.getHeaderMap().get("Col1").toString());
        assertEquals("Value1,Value2,Value3", Arrays.toString(firstRecord.toArray()));
        parser.close();
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("https://example.com/testCSVFile.csv");
        CSVParser parser = CSVParser.parse(url, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        assertEquals(3, records.size());
        assertEquals("Col1,Col2,Col3", firstRecord.getHeaderMap().get("Col1").toString());
        assertEquals("Value1,Value2,Value3", Arrays.toString(firstRecord.toArray()));
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFile() {
        CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() {
        File file = new File("testCSVFile.csv");
        CSVParser parser = CSVParser.parse(file, null);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IOException.class)
    public void testInvalidFile() throws IOException {
        File file = new File("testCSVFileDoesNotExist.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        parser.getRecords();
        parser.close();
    }

    @Test
    public void testGetRecordsEmpty() throws IOException {
        String csvData = "";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());
        parser.close();
    }

    @Test
    public void testGetRecordsNull() throws IOException {
        String csvData = null;
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());
        parser.close();
    }

    @Test
    public void testClose() throws IOException {
        File file = new File("testCSVFile.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        parser.close();
        parser.getRecords(); // should throw IOException
    }

    @Test
    public void testIterator() throws IOException {
        File file = new File("testCSVFile.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Iterator<CSVRecord> iterator = parser.iterator();
        assertEquals(records.get(0), iterator.next());
        assertEquals(records.get(1), iterator.next());
        assertFalse(iterator.hasNext());
        parser.close();
    }

}