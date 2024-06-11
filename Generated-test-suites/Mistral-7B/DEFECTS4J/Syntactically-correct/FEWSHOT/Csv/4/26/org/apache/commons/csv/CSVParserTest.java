package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.ext.ExtendedBufferedReader;
import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("src/test/resources/csv/test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(5, records.size());
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Col1", "Col2", "Col3"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"", "", "3", null, "5"}, record2.getValues());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"A", "B", null}, record3.getValues());

        CSVRecord record4 = records.get(3);
        assertArrayEquals(new String[]{"", null, "C"}, record4.getValues());

        CSVRecord record5 = records.get(4);
        assertArrayEquals(new String[]{"D", "E"}, record5.getValues());
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Col1,Col2,Col3\n,Col2,Col3,3,Col3\nA,B,,C\n,,,C\nD,E";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(5, records.size());
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Col1", "Col2", "Col3"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"", "", "3", null, "Col3"}, record2.getValues());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"A", "B", null}, record3.getValues());

        CSVRecord record4 = records.get(3);
        assertArrayEquals(new String[]{"", null, "C"}, record4.getValues());

        CSVRecord record5 = records.get(4);
        assertArrayEquals(new String[]{"D", "E"}, record5.getValues());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // This test assumes that the test.csv file in the test resources folder is identical to the one in the URL.
        assertEquals(5, records.size());
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Col1", "Col2", "Col3"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"", "", "3", null, "Col3"}, record2.getValues());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"A", "B", null}, record3.getValues());

        CSVRecord record4 = records.get(3);
        assertArrayEquals(new String[]{"", null, "C"}, record4.getValues());

        CSVRecord record5 = records.get(4);
        assertArrayEquals(new String[]{"D", "E"}, record5.getValues());
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFile() throws IOException {
        File file = new File("src/test/resources/csv/invalid.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        parser.getRecords();
        parser.close();
    }

    @Test
    public void testParseEmptyFile() throws IOException {
        File file = new File("src/test/resources/csv/empty.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(0, records.size());
    }

    @Test
    public void testParseRecordWise() throws IOException {
        File file = new File("src/test/resources/csv/test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);

        int recordNumber = 0;
        for (CSVRecord record : parser) {
            recordNumber++;
            assertEquals(recordNumber, record.getRecordNumber());
            // Perform additional checks here if needed
        }

        parser.close();
    }

    @Test
    public void testParseIntoMemory() throws IOException {
        String csvData = "a;b\nc;d";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"a", "b"}, record.getValues());
        assertArrayEquals(new String[]{"c", "d"}, record.getRawValues());
    }

    @Test
    public void testParseWithCustomFormat() throws IOException {
        String csvData = "Col1,Col2,Col3\n,Col2,Col3,3,Col3\nA,B,,C\n,,,C\nD,E";
        CSVParser parser = new CSVParser(new StringReader(csvData), new CSVFormatBuilder()
                .setHeaderNames(new String[]{"Col11", "Col22", "Col33"})
                .build());
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(5, records.size());
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Col11", "Col22", "Col33"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"", "", "3", null, "Col33"}, record2.getValues());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"A", "B", null}, record3.getValues());

        CSVRecord record4 = records.get(3);
        assertArrayEquals(new String[]{"", null, "C"}, record4.getValues());

        CSVRecord record5 = records.get(4);
        assertArrayEquals(new String[]{"D", "E"}, record5.getValues());
    }

    @Test
    public void testSkipHeaderRecord() throws IOException {
        String csvData = "Col1,Col2,Col3\nCol1,Col2,Col3\n,Col2,Col3,3,Col3\nA,B,,C\n,,,C\nD,E";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.setSkipHeaderRecord(true);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(4, records.size());
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"", "", "3", null, "Col3"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"A", "B", null}, record2.getValues());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"", null, "C"}, record3.getValues());

        CSVRecord record4 = records.get(3);
        assertArrayEquals(new String[]{"D", "E"}, record4.getValues());
    }
}