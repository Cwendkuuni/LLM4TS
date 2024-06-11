package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Map;

public class CSVParserTest {

    private static CSVParser parser;
    private static CSVFormat format;

    @BeforeClass
    public static void setUp() throws IOException {
        format = new CSVFormat(new CSVParserDefaultRecordParser(), ',', '\u0001');
        String csvData = "name,age\nAlice,30\nBob,25\n";
        parser = CSVParser.parse(new StringReader(csvData), format);
    }

    @Test
    public void testParseBasic() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            String[] values = record.toArray(String[].class);
            if (values.length == 2) {
                Assert.assertEquals("Alice", values[0]);
                Assert.assertEquals("30", values[1]);
            } else if (values.length == 1) {
                Assert.assertEquals(null, values[0]);
            }
        }
    }

    @Test
    public void testParseHeader() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        Map<String, Integer> headerMap = parser.getHeaderMap();

        String[] header = firstRecord.getHeader();
        Assert.assertArrayEquals(new String[]{"name", "age"}, header);

        Assert.assertEquals(0, headerMap.get("name").intValue());
        Assert.assertEquals(1, headerMap.get("age").intValue());

        for (CSVRecord record : records) {
            String[] values = record.toArray(String[].class);
            Assert.assertEquals("Alice", values[headerMap.get("name")]);
            Assert.assertEquals("30", values[headerMap.get("age")]);
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseNoRecords() throws IOException {
        parser = new CSVParser(new StringReader(""), format);
        parser.getRecords();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFormat() throws IOException {
        String csvData = "name,age\nAlice,30\nBob,25\n--";
        parser = new CSVParser(new StringReader(csvData), format);
        parser.nextRecord();
    }
}