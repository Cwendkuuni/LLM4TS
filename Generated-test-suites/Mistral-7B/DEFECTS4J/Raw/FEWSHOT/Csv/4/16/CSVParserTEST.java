package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.TOKEN;
import static org.apache.commons.csv.CSVRecord.Type.COMMENT;
import static org.apache.commons.csv.CSVRecord.Type.RECORD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecord.Type;
import org.apache.commons.csv.Token;

public class CSVParserTest {

    private static final String TEST_CSV = "a;b\nc;d\ne--foo\n--bar--\n--\n";

    @Test
    public void testParse() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_CSV.getBytes());
        ByteArrayReader reader = new ByteArrayReader(TEST_CSV.getBytes());

        // Using CSVFormat.DEFAULT
        CSVParser parser1 = CSVParser.parse(inputStream);
        List<CSVRecord> records1 = parser1.getRecords();
        parser1.close();

        // Using CSVFormat.EXPLICIT
        CSVFormat format = CSVFormat.EXPLICIT.withHeader("a", "b", "c", "d");
        CSVParser parser2 = CSVParser.parse(inputStream, format);
        List<CSVRecord> records2 = parser2.getRecords();
        parser2.close();

        // Using CSVFormat.RFC4180
        CSVFormat formatRFC4180 = CSVFormat.RFC4180;
        CSVParser parser3 = CSVParser.parse(inputStream, formatRFC4180);
        List<CSVRecord> records3 = parser3.getRecords();
        parser3.close();

        // Using CSVFormat.EXCEL
        CSVFormat formatExcel = CSVFormat.EXCEL;
        CSVParser parser4 = CSVParser.parse(inputStream, formatExcel);
        List<CSVRecord> records4 = parser4.getRecords();
        parser4.close();

        // Using CSVFormat.custom
        CSVFormat formatCustom = new CSVFormatBuilder().setHeader("a", "b").setDelimiter('|').build();
        CSVParser parser5 = CSVParser.parse(reader, formatCustom);
        List<CSVRecord> records5 = parser5.getRecords();
        parser5.close();

        // Using CSVParser constructor
        CSVParser parser6 = new CSVParser(reader, formatRFC4180);
        List<CSVRecord> records6 = parser6.getRecords();
        parser6.close();

        // Test record size
        assertEquals(4, records1.size());
        assertEquals(4, records2.size());
        assertEquals(4, records3.size());
        assertEquals(4, records4.size());
        assertEquals(4, records5.size());
        assertEquals(4, records6.size());

        // Test record values
        CSVRecord record1 = records1.get(0);
        assertEquals(4, record1.size());
        assertEquals("a", record1.get(0));
        assertEquals("b", record1.get(1));
        assertEquals("c", record1.get(2));
        assertEquals("d", record1.get(3));

        CSVRecord record2 = records2.get(0);
        assertEquals(4, record2.size());
        assertEquals("a", record2.get(0));
        assertEquals("b", record2.get(1));
        assertEquals("c", record2.get(2));
        assertEquals("d", record2.get(3));

        CSVRecord record3 = records3.get(0);
        assertEquals(4, record3.size());
        assertEquals("a", record3.get(0));
        assertEquals("b", record3.get(1));
        assertEquals("c", record3.get(2));
        assertEquals("d", record3.get(3));

        CSVRecord record4 = records4.get(0);
        assertEquals(4, record4.size());
        assertEquals("a", record4.get(0));
        assertEquals("b", record4.get(1));
        assertEquals("c", record4.get(2));
        assertEquals("d", record4.get(3));

        CSVRecord record5 = records5.get(0);
        assertEquals(4, record5.size());
        assertEquals("a", record5.get(0));
        assertEquals("b", record5.get(1));
        assertEquals("c", record5.get(2));
        assertEquals("d", record5.get(3));

        CSVRecord record6 = records6.get(0);
        assertEquals(4, record6.size());
        assertEquals("a", record6.get(0));
        assertEquals("b", record6.get(1));
        assertEquals("c", record6.get(2));
        assertEquals("d", record6.get(3));

        // Test comment
        CSVRecord record7 = new CSVRecord(Collections.emptyList().toArray(new Object[0]), Collections.<String, Integer> emptyMap(),
                "# This is a comment\n", 0);
        CSVParser parser7 = new CSVParser(new StringReader("# This is a comment\n"), CSVFormat.DEFAULT);
        CSVRecord record8 = parser7.nextRecord();
        assertEquals(COMMENT, record8.getType());
        assertEquals("# This is a comment\n", record8.getComment());
        parser7.close();

        // Test empty record
        CSVRecord record9 = new CSVRecord(Collections.emptyList().toArray(new Object[0]), Collections.<String, Integer> emptyMap(),
                null, 0);
        CSVParser parser8 = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
        CSVRecord record10 = parser8.nextRecord();
        assertEquals(RECORD, record10.getType());
        assertEquals(0, record10.size());
        assertEquals(record9, record10);
        parser8.close();

        // Test no record
        CSVParser parser9 = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
        CSVRecord record11 = parser9.nextRecord();
        assertNull(record11);
        parser9.close();

        // Test invalid CSV
        CSVParser parser10 = new CSVParser(new StringReader("a;b\nc:d"), CSVFormat.DEFAULT);
        CSVRecord record12 = parser10.nextRecord();
        assertNotNull(record12);
        assertEquals(2, record12.size());
        assertEquals("a", record12.get(0));
        assertEquals("c:d", record12.get(1));
        parser10.close();

        // Test multiple comments
        CSVParser parser11 = new CSVParser(new StringReader("# This is a comment 1\n# This is a comment 2\n--foo--\n"),
                CSVFormat.DEFAULT);
        CSVRecord record13 = parser11.nextRecord();
        assertEquals(COMMENT, record13.getType());
        assertEquals("# This is a comment 1\n# This is a comment 2\n", record13.getComment());
        parser11.nextRecord();
        CSVRecord record14 = parser11.nextRecord();
        assertNull(record14.getComment());
        parser11.close();

        // Test multiple records with empty record
        CSVParser parser12 = new CSVParser(new StringReader("--foo--\n--\n--bar--\n--\n"), CSVFormat.DEFAULT);
        List<CSVRecord> records7 = parser12.getRecords();
        parser12.close();
        assertEquals(3, records7.size());
        assertFalse(records7.get(0).getComment() != null);
        assertFalse(records7.get(1).getComment() != null);
        assertFalse(records7.get(2).getComment() != null);

        // Test multiple records with comment
        CSVParser parser13 = new CSVParser(new StringReader("--foo--\n# This is a comment\n--bar--"), CSVFormat.DEFAULT);
        List<CSVRecord> records8 = parser13.getRecords();
        parser13.close();
        assertEquals(2, records8.size());
        assertEquals("# This is a comment", records8.get(1).getComment());

        // Test record with null value
        CSVParser parser14 = new CSVParser(new StringReader("a;b\nc;d;"), CSVFormat.DEFAULT);
        CSVRecord record15 = parser14.nextRecord();
        assertEquals("c", record15.get(2));
        assertNull(record15.get(3));
        parser14.nextRecord();
        parser14.close();

        // Test record with empty string value
        CSVParser parser15 = new CSVParser(new StringReader("a;b\nc;\"\";d;"), CSVFormat.DEFAULT);
        CSVRecord record16 = parser15.nextRecord();
        assertEquals("", record16.get(2));
        parser15.nextRecord();
        parser15.close();

        // Test record with multiple empty values
        CSVParser parser16 = new CSVParser(new StringReader("a;b\nc;\"\";d;e;"), CSVFormat.DEFAULT);
        CSVRecord record17 = parser16.nextRecord();
        List<Object> values17 = record17.values();
        assertEquals(5, values17.size());
        assertEquals("", values17.get(2));
        assertEquals("", values17.get(4));
        parser16.nextRecord();
        parser16.close();

        // Test record with null header
        CSVParser parser17 = new CSVParser(new StringReader("a;b\nc;d;e;"), null);
        CSVRecord record18 = parser17.nextRecord();
        List<Object> values18 = record18.values();
        assertEquals(5, values18.size());
        assertNull(record18.getHeaderNames());
        parser17.nextRecord();
        parser17.close();

        // Test record with empty header
        CSVParser parser18 = new CSVParser(new StringReader("a;b\n"), null);
        CSVRecord record19 = parser18.nextRecord();
        List<Object> values19 = record19.values();
        assertEquals(2, values19.size());
        assertEquals(Collections.singletonList("a"), record19.getHeaderNames());
        parser18.nextRecord();
        parser18.close();

        // Test record with custom header
        CSVParser parser19 = new CSVParser(new StringReader("name;age;sex\nJohn;35;M\nMary;27;F\n"), null);
        CSVRecord record20 = parser19.nextRecord();
        List<Object> values20 = record20.values();
        assertEquals(3, values20.size());
        assertEquals(Arrays.asList("name", "age", "sex"), record20.getHeaderNames());
        assertEquals("John", record20.get("name"));
        assertEquals("35", record20.get("age"));
        assertEquals("M", record20.get("sex"));
        parser19.nextRecord();
        parser19.close();

        // Test record with quoted values
        CSVParser parser20 = new CSVParser(new StringReader("a;\"b, c\"\";d;e"), null);
        CSVRecord record21 = parser20.nextRecord();
        List<Object> values21 = record21.values();
        assertEquals(5, values21.size());
        assertEquals("b, c", record21.get(1));
        parser20.nextRecord();
        parser20.close();

        // Test record with quoted and empty values
        CSVParser parser21 = new CSVParser(new StringReader("a;\"\"\";b;\"hello, world\"\";"), null);
        CSVRecord record22 = parser21.nextRecord();
        List<Object> values22 = record22.values();
        assertEquals(3, values22.size());
        assertEquals("", values22.get(1));
        assertEquals("hello, world", values22.get(2));
        parser21.nextRecord();
        parser21.close();

        // Test record with quoted and null values
        CSVParser parser22 = new CSVParser(new StringReader("a;\"null\"\";b;null;"), null);
        CSVRecord record23 = parser22.nextRecord();
        List<Object> values23 = record23.values();
        assertEquals(3, values23.size());
        assertEquals("null", values23.get(1));
        assertNull(values23.get(2));
        parser22.nextRecord();
        parser22.close();

        // Test record with escaped quotes
        CSVParser parser23 = new CSVParser(new StringReader("a;\"a\"\"b\"\";d;e"), null);
        CSVRecord record24 = parser23.nextRecord();
        List<Object> values24 = record24.values();
        assertEquals(3, values24.size());
        assertEquals("a\"b", values24.get(1));
        parser23.nextRecord();
        parser23.close();

        // Test record with empty line
        CSVParser parser24 = new CSVParser(new StringReader("\n"), null);
        CSVRecord record25 = parser24.nextRecord();
        assertNull(record25);
        parser24.close();

        // Test record with inconsistent header
        CSVParser parser25 = new CSVParser(new StringReader("a;b;c\n1;2;3\na;x;c"), null);
        CSVRecord record26 = parser25.nextRecord();
        List<Object> values26 = record26.values();
        assertEquals(3, values26.size());
        assertEquals("1", values26.get(0));
        assertEquals("x", values26.get(1));
        assertEquals("3", values26.get(2));
        parser25.nextRecord();
        parser25.close();

        // Test record with inconsistent values
        CSVParser parser26 = new CSVParser(new StringReader("a;b\nc;1;d"), null);
        CSVRecord record27 = parser26.nextRecord();
        List<Object> values27 = record27.values();
        assertEquals(3, values27.size());
        assertEquals("c", values27.get(0));
        assertEquals("1", values27.get(1));
        assertEquals("d", values27.get(2));
        parser26.nextRecord();
        parser26.close();

        // Test record with inconsistent values and missing values
        CSVParser parser27 = new CSVParser(new StringReader("a;b\nc;1;d;e"), null);
        CSVRecord record28 = parser27.nextRecord();
        List<Object> values28 = record28.values();
        assertEquals(4, values28.size());
        assertEquals("c", values28.get(0));
        assertEquals("1", values28.get(1));
        assertNull(values28.get(2));
        assertEquals("d", values28.get(3));
        parser27.nextRecord();
        parser27.close();
    }
}