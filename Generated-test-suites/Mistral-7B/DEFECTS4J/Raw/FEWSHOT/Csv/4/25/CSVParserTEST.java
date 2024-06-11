package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File csvFile = new File("src/test/resources/csv/sample.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(5, records.size());

        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertArrayEquals(new String[]{"Name", "Age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            assertArrayEquals(new Object[]{"Alice", 25}, record.toArray());
        }

        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "Name,Age\nAlice,25\nBob,30\n";
        CSVParser parser = new CSVParser(new StringReader(csvString), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        for (CSVRecord record : records) {
            assertArrayEquals(new String[]{"Name", "Age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            assertArrayEquals(new Object[]{"Alice", 25}, record.toArray());
        }

        parser.close();
    }

    @Test
    public void testParseUrl() throws IOException {
        URL url = new URL("https://example.com/data.csv");
        CSVParser parser = CSVParser.parse(url, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        for (CSVRecord record : records) {
            assertArrayEquals(new String[]{"Name", "Age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            assertArrayEquals(new Object[]{"Alice", 25}, record.toArray());
        }

        parser.close();
    }

    @Test
    public void testParseCustomFormat() throws IOException {
        File csvFile = new File("src/test/resources/csv/sample_custom.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.EXCEL);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(5, records.size());

        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertArrayEquals(new String[]{"Name", "Age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            assertArrayEquals(new Object[]{"Alice", 25.0}, record.toArray());
        }

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFile() throws IOException {
        File csvFile = new File("src/test/resources/csv/invalid.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        fail("IOException expected");
    }

    @Test
    public void testIterator() throws IOException {
        File csvFile = new File("src/test/resources/csv/sample.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);

        int index = 0;
        for (CSVRecord record : parser) {
            assertArrayEquals(new String[]{"Name", "Age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            assertArrayEquals(new Object[]{"Alice", 25}, record.toArray());
            index++;
        }

        assertEquals(5, index);

        parser.close();
    }

    @Test
    public void testClose() throws IOException {
        File csvFile = new File("src/test/resources/csv/sample.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);

        parser.close();

        try {
            List<CSVRecord> records = parser.getRecords();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }

        try {
            for (CSVRecord record : parser) {
                fail("NoSuchElementException expected");
            }
        } catch (NoSuchElementException e) {
            // expected
        }
    }
}