package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        String filePath = "src/test/resources/csv/test.csv";
        CSVParser parser = CSVParser.parse(new File(filePath), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Verify record count
        assertEquals(4, records.size());

        // Verify record values
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Col1", "Col2", "Col3"}, record1.toArray());
        assertEquals("Col1", record1.get(0));
        assertEquals("Col2", record1.get(1));
        assertEquals("Col3", record1.get(2));

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"", "Value2", ""}, record2.toArray());
        assertEquals(null, record2.get(0));
        assertEquals("Value2", record2.get(1));
        assertNull(record2.get(2));

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"Col4", null}, record3.toArray());
        assertEquals("Col4", record3.get(0));
        assertNull(record3.get(1));

        CSVRecord record4 = records.get(3);
        assertArrayEquals(new String[]{"", "", ""}, record4.toArray());
        assertEquals(null, record4.get(0));
        assertEquals(null, record4.get(1));
        assertEquals(null, record4.get(2));
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Col1,Col2,Col3\n" +
                "\"Col1\",Col2,\"Col3\"\n" +
                "\n" +
                "Col4, null\n" +
                "";

        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Verify record count
        assertEquals(4, records.size());

        // Verify record values
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Col1", "Col2", "Col3"}, record1.toArray());
        assertEquals("Col1", record1.get(0));
        assertEquals("Col2", record1.get(1));
        assertEquals("Col3", record1.get(2));

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"", "Col2", ""}, record2.toArray());
        assertEquals(null, record2.get(0));
        assertEquals("Col2", record2.get(1));
        assertNull(record2.get(2));

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"Col4", null}, record3.toArray());
        assertEquals("Col4", record3.get(0));
        assertNull(record3.get(1));

        CSVRecord record4 = records.get(3);
        assertArrayEquals(new String[]{"", "", ""}, record4.toArray());
        assertEquals(null, record4.get(0));
        assertEquals(null, record4.get(1));
        assertEquals(null, record4.get(2));
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "http://example.com/data.csv";
        Charset charset = Charset.forName("UTF-8");
        CSVParser parser = CSVParser.parse(new URL(urlString), charset, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Verify record count
        assertEquals(4, records.size());

        // Verify record values
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Col1", "Col2", "Col3"}, record1.toArray());
        assertEquals("Col1", record1.get(0));
        assertEquals("Col2", record1.get(1));
        assertEquals("Col3", record1.get(2));

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"", "Col2", ""}, record2.toArray());
        assertEquals(null, record2.get(0));
        assertEquals("Col2", record2.get(1));
        assertNull(record2.get(2));

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"Col4", null}, record3.toArray());
        assertEquals("Col4", record3.get(0));
        assertNull(record3.get(1));

        CSVRecord record4 = records.get(3);
        assertArrayEquals(new String[]{"", "", ""}, record4.toArray());
        assertEquals(null, record4.get(0));
        assertEquals(null, record4.get(1));
        assertEquals(null, record4.get(2));
    }

    @Test
    public void testParseWithCustomFormat() throws IOException {
        String csvData = "Col1,Col2,Col3\n" +
                "Col1,Col2,\"Col3 with quotes\"\n" +
                "\"Col1 with quotes\",Col2,Col3\n" +
                "Col4, ,Col5\n" +
                "Col6,";

        CSVFormat format = new CSVFormatBuilder()
                .setHeaderNames(Arrays.asList("Col1", "Col2", "Col3 with quotes", "Col4", "Col5", "Col6"))
                .setDelimiter(',')
                .setHeaderLineNumber(1)
                .setIgnoreLeadingWhiteSpace(true)
                .setTrimLeadingWhiteSpace(true)
                .setIgnoreTrailingWhiteSpace(true)
                .setTrimTrailingWhiteSpace(true)
                .build();

        CSVParser parser = CSVParser.parse(new StringReader(csvData), format);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Verify record count
        assertEquals(5, records.size());

        // Verify record values
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Col1", "Col2", "Col3 with quotes"}, record1.toArray());
        assertEquals("Col1", record1.get(0));
        assertEquals("Col2", record1.get(1));
        assertEquals("Col3 with quotes", record1.get(2));

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Col1 with quotes", "Col2", "Col3"}, record2.toArray());
        assertEquals("Col1 with quotes", record2.get(0));
        assertEquals("Col2", record2.get(1));
        assertEquals("Col3", record2.get(2));

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"Col4", null, "Col5"}, record3.toArray());
        assertEquals("Col4", record3.get(0));
        assertNull(record3.get(1));
        assertEquals("Col5", record3.get(2));

        CSVRecord record4 = records.get(3);
        assertArrayEquals(new String[]{"", "", ""}, record4.toArray());
        assertEquals(null, record4.get(0));
        assertEquals(null, record4.get(1));
        assertEquals(null, record4.get(2));

        CSVRecord record5 = records.get(4);
        assertArrayEquals(new String[]{"Col6"}, record5.toArray());
        assertEquals("Col6", record5.get(0));
        assertNull(record5.get(1));
        assertNull(record5.get(2));
    }
}