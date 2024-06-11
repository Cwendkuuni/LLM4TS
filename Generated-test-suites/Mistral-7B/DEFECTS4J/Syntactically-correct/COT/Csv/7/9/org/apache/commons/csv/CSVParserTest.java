package org.apache.commons.csv; // import necessary packages here

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

public class CSVParserTest {

    // Reusable CSVFormat and InputStream for testing
    private static final CSVFormat csvFormat = CSVFormat.DEFAULT;
    private static final byte[] csvData =
            "name,age,height\n"
                    + "Alice,25,165.5\n"
                    + "Bob,30,178.0\n"
                    + "Charlie,35,172.5\n"
                    + "Dave,40,180.5";
    private static final InputStream inputStream = new ByteArrayInputStream(csvData);

    // Initialize parser and reader for testing
    private CSVParser parser;
    private Reader reader;

    @Before
    public void setUp() throws IOException {
        reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        parser = CSVParser.parse(reader, csvFormat);
    }

    @Test
    public void testParseCSVFile() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertArrayEquals(new String[]{"name", "age", "height"}, records.get(0).getHeaderMap().keySet().toArray(new String[0]));
        assertArrayEquals(new Object[]{"Alice", "25", "165.5"}, records.get(0).toArray());
        assertArrayEquals(new Object[]{"Bob", "30", "178.0"}, records.get(1).toArray());
        assertArrayEquals(new Object[]{"Charlie", "35", "172.5"}, records.get(2).toArray());
        assertArrayEquals(new Object[]{"Dave", "40", "180.5"}, records.get(3).toArray());
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCSVFormat() throws IOException {
        CSVParser parser = CSVParser.parse(reader, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFile() throws IOException {
        CSVParser.parse(null, csvFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() throws IOException {
        CSVParser.parse(new File("test.csv"), null);
    }

    @Test
    public void testParseEmptyCSVFile() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader(""), csvFormat);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertNull(records.get(0));
    }

    @Test
    public void testParseEmptyRecord() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("\n"), csvFormat);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertNull(records.get(0));
    }

    @Test
    public void testParseCSVFileWithMissingColumn() throws IOException {
        CSVFormat csvFormat = new CSVFormat(Arrays.asList("name", "age"), '\t');
        CSVParser parser = CSVParser.parse(new StringReader(new String(csvData) + "\nname,age,height\nAlice,25"), csvFormat);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        assertArrayEquals(new Object[]{"Alice", "25"}, record.toArray());
        assertNull(record.get(2));
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEndOfStream() throws IOException {
        CSVParser parser = new CSVParser(new StringReader(""), csvFormat);
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord(); // Throws NoSuchElementException
    }
}