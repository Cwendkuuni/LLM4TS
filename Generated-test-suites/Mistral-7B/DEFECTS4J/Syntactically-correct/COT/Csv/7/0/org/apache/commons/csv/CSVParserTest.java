package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;

/**
 * Test for CSVParser class.
 */
public class CSVParserTest {
    // 

    @Test
    public void testEmptyCSV() throws IOException {
        CSVParser parser = createCSVParser(new ByteArrayInputStream(new byte[0]));
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertNull(records.get(0));
        parser.close();
    }

    @Test
    public void testSimpleCSV() throws IOException {
        CSVParser parser = createCSVParser(
                new ByteArrayInputStream(
                        ("a,b\nc,d").getBytes(StandardCharsets.UTF_8)));
        List<CSVRecord> records = parser.getRecords();
        assertSame(2, records.size());
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"a", "b"}, record1.values());
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"c", "d"}, record2.values());
        parser.close();
    }

    @Test
    public void testCSVWithHeader() throws IOException {
        CSVParser parser = createCSVParser(
                new ByteArrayInputStream(
                        ("Name,Age\nJohn,25\nMike,30").getBytes(StandardCharsets.UTF_8)));
        List<CSVRecord> records = parser.getRecords();
        assertSame(2, records.size());
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age"}, record1.getHeader());
        assertArrayEquals(new String[]{"John", "25"}, record1.toArray());
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age"}, record2.getHeader());
        assertArrayEquals(new String[]{"Mike", "30"}, record2.toArray());
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testInvalidCSV() throws IOException {
        CSVParser parser = createCSVParser(
                new ByteArrayInputStream(
                        ("a,b\nc,d\ne").getBytes(StandardCharsets.UTF_8)));
        parser.getRecords();
    }

    @Test
    public void testCustomCSVFormat() throws IOException {
        CSVParser parser = createCSVParser(
                new ByteArrayInputStream(
                        ("Name:First,Name:Last\nJohn:Doe,Smith:Jones").getBytes(StandardCharsets.UTF_8)),
                CSVFormat.DEFAULT.withHeader("Name:First", "Name:Last"));
        List<CSVRecord> records = parser.getRecords();
        assertSame(1, records.size());
        CSVRecord record = records.get(0);
        Map<String, Integer> headerMap = record.getHeaderMap();
        List<String> values = Arrays.asList("John:Doe", "Smith:Jones");
        assertArrayEquals(new String[]{"Name:First", "Name:Last"}, record.getHeader());
        assertArrayEquals(values.toArray(new String[values.size()]), record.values());
        assertSame(2, headerMap.size());
        assertSame(0, headerMap.get("Name:First"));
        assertSame(1, headerMap.get("Name:Last"));
        parser.close();
    }

    @Test
    public void testCSVWithEmptyValues() throws IOException {
        CSVParser parser = createCSVParser(
                new ByteArrayInputStream(
                        ("a,,c\nd,e,,").getBytes(StandardCharsets.UTF_8)));
        List<CSVRecord> records = parser.getRecords();
        assertSame(2, records.size());
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"a", "", "c"}, record1.toArray());
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"d", "e", ""}, record2.toArray());
        parser.close();
    }

    @Test
    public void testCSVWithSkippedHeader() throws IOException {
        CSVParser parser = createCSVParser(
                new ByteArrayInputStream(("Name,Age\n25,John").getBytes(StandardCharsets.UTF_8)),
                CSVFormat.DEFAULT.withHeader(false));
        List<CSVRecord> records = parser.getRecords();
        assertSame(1, records.size());
        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"25", "John"}, record.toArray());
        parser.close();
    }

    @Test
    public void testCSVWithComment() throws IOException {
        CSVParser parser = createCSVParser(
                new ByteArrayInputStream(
                        ("Name,Comment\nJohn,This is a comment\nMike").getBytes(StandardCharsets.UTF_8)));
        List<CSVRecord> records = parser.getRecords();
        assertSame(2, records.size());
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Name"}, record1.getHeader());
        assertArrayEquals(new String[]{"John"}, record1.values());
        String comment1 = record1.getComment();
        assertNull(comment1);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name"}, record2.getHeader());
        assertArrayEquals(new String[]{"Mike"}, record2.values());
        String comment2 = record2.getComment();
        assertNotNull(comment2);
        assertTrue(comment2.contains("comment"));
        parser.close();
    }

    @Test
    public void testCSVWithEmptyLine() throws IOException {
        CSVParser parser = createCSVParser(
                new ByteArrayInputStream(
                        ("Name,Age\nJohn,25\n\nMike,30").getBytes(StandardCharsets.UTF_8)));
        List<CSVRecord> records = parser.getRecords();
        assertSame(2, records.size());
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age"}, record1.getHeader());
        assertArrayEquals(new String[]{"John", "25"}, record1.toArray());
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age"}, record2.getHeader());
        assertArrayEquals(new String[]{"Mike", "30"}, record2.toArray());
        parser.close();
    }

    @Test
    public void testCSVWithEmptyFile() throws IOException {
        CSVParser parser = createCSVParser(new ByteArrayInputStream(new byte[0]));
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertNull(records.get(0));
        parser.close();
    }

    /**
     * Test for Iterator.
     *
     * @throws IOException for invalid input.
     */
    @Test(value = { "org.junit.platform.runner.JUnitPlatform", "org.junit.runner.JUnitCore" }, runWithRunner = Enclosed.class)
    public class IteratorTest {
        @Test
        public void testEmptyCSVWithIterator() throws IOException {
            CSVParser parser = createCSVParser(new ByteArrayInputStream(new byte[0]));
            List<CSVRecord> records = new ArrayList<>();
            parser.iterator().forEachRemaining(records::add);
            parser.close();
            assertTrue(records.isEmpty());
        }

        @Test
        public void testSimpleCSVWithIterator() throws IOException {
            CSVParser parser = createCSVParser(
                    new ByteArrayInputStream(
                            ("a,b\nc,d".getBytes(StandardCharsets.UTF_8))));
            List<CSVRecord> records = new ArrayList<>();
            parser.iterator().forEachRemaining(records::add);
            parser.close();
            assertArrayEquals(new String[]{"a", "b"}, records.get(0).toArray());
            assertArrayEquals(new String[]{"c", "d"}, records.get(1).toArray());
            assertSame(2, records.size());
        }

        @Test
        public void testCSVWithEmptyValuesWithIterator() throws IOException {
            CSVParser parser = createCSVParser(
                    new ByteArrayInputStream(
                            ("a,,c\nd,e,,".getBytes(StandardCharsets.UTF_8))));
            List<CSVRecord> records = new ArrayList<>();
            parser.iterator().forEachRemaining(records::add);
            parser.close();
            assertArrayEquals(new String[]{"a", "", "c"}, records.get(0).toArray());
            assertArrayEquals(new String[]{"d", "e", ""}, records.get(1).toArray());
            assertSame(2, records.size());
        }

        @Test
        public void testCSVWithCommentWithIterator() throws IOException {
            CSVParser parser = createCSVParser(
                    new ByteArrayInputStream(
                            ("Name,Comment\nJohn,This is a comment\nMike".getBytes(StandardCharsets.UTF_8))));
            List<CSVRecord> records = new ArrayList<>();
            parser.iterator().forEachRemaining(records::add);
            parser.close();
            CSVRecord record1 = records.get(0);
            assertArrayEquals(new String[]{"Name"}, record1.getHeader());
            assertArrayEquals(new String[]{"John"}, record1.values());
            assertNull(record1.getComment());
            CSVRecord record2 = records.get(1);
            assertArrayEquals(new String[]{"Name"}, record2.getHeader());
            assertArrayEquals(new String[]{"Mike"}, record2.values());
            String comment2 = record2.getComment();
            assertNotNull(comment2);
            assertTrue(comment2.contains("comment"));
            assertSame(2, records.size());
        }

        @Test(expected = NoSuchElementException.class)
        public void testEmptyIterator() throws IOException {
            CSVParser parser = createCSVParser(new ByteArrayInputStream(new byte[0]));
            parser.iterator().next();
            parser.close();
        }

        @Test(expected = NoSuchElementException.class)
        public void testEmptyIteratorAfterClose() throws IOException {
            CSVParser parser = createCSVParser(new ByteArrayInputStream(new byte[0]));
            parser.iterator().next();
            parser.close();
            parser.iterator().next();
        }

        @Test(expected = IllegalStateException.class)
        public void testIteratorAfterClose() throws IOException {
            CSVParser parser = createCSVParser(
                    new ByteArrayInputStream(
                            ("a,b\nc,d".getBytes(StandardCharsets.UTF_8))));
            parser.iterator().next();
            parser.close();
            parser.iterator().next();
        }
    }

    // 

    private CSVParser createCSVParser(InputStreamReader reader, CSVFormat format) throws IOException {
        return new CSVParser(reader, format);
    }

    private CSVParser createCSVParser(ByteArrayInputStream inputStream) throws IOException {
        return createCSVParser(inputStream, CSVFormat.DEFAULT);
    }

    private CSVParser createCSVParser(ByteArrayInputStream inputStream, CSVFormat format) throws IOException {
        return createCSVParser(new InputStreamReader(inputStream), format);
    }
}