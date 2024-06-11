package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        for (CSVRecord record : records) {
            String[] values = record.toArray(String[].class);
            assertEquals(3, values.length);
            for (int i = 0; i < values.length; i++) {
                assertEquals("Column" + (i + 1), values[i]);
            }
        }
        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Column1,Column2,Column3\n" +
                "Value1_1,Value2_1,Value3_1\n" +
                "Value1_2,Value2_2,Value3_2\n";

        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        for (CSVRecord record : records) {
            String[] values = record.toArray(String[].class);
            assertEquals(3, values.length);
            for (int i = 0; i < values.length; i++) {
                assertEquals("Column" + (i + 1), values[i]);
            }
        }
        parser.close();
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/data.csv");
        CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        for (CSVRecord record : records) {
            String[] values = record.toArray(String[].class);
            assertEquals(3, values.length);
            for (int i = 0; i < values.length; i++) {
                assertEquals("Column" + (i + 1), values[i]);
            }
        }
        parser.close();
    }

    @Test
    public void testParseCustomFormat() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, new CSVFormat(';', '"', '\'\''));

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        for (CSVRecord record : records) {
            String[] values = record.toArray(String[].class);
            assertEquals(3, values.length);
            for (int i = 0; i < values.length; i++) {
                assertEquals("Column" + (i + 1), values[i]);
            }
        }
        parser.close();
    }

    @Test
    public void testParseIntoMemory() throws IOException {
        String csvData = "Column1,Column2,Column3\n" +
                "Value1_1,Value2_1,Value3_1\n" +
                "Value1_2,Value2_2,Value3_2\n";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());

        for (CSVRecord record : records) {
            String[] values = record.toArray(String[].class);
            assertEquals(3, values.length);
            for (int i = 0; i < values.length; i++) {
                assertEquals("Column" + (i + 1), values[i]);
            }
        }
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFormat() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, null);
        parser.getRecords();
    }

    @Test(expected = IOException.class)
    public void testParseNullFile() throws IOException {
        CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test(expected = IOException.class)
    public void testParseNullFormat() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, null);
        parser.getRecords();
    }

    @Test(expected = IOException.class)
    public void testParseNullReader() throws IOException {
        CSVParser parser = new CSVParser(null, CSVFormat.DEFAULT);
        parser.getRecords();
    }

}