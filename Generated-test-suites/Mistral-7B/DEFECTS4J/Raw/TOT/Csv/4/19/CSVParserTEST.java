package org.apache.commons.csv;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link CSVParser}.
 */
public class CSVParserTest {

    private static final String CSV_FILE_PATH = "src/test/resources/CSVParserTestData.csv";
    private static final String CSV_STRING = "a,b\nc,d;e\n1,2,3,4";

    private CSVParser parser;

    @Before
    public void setUp() throws Exception {
        parser = CSVParser.parse(CSV_FILE_PATH, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    @Test
    public void testParseFile() throws Exception {
        CSVRecord record;
        List<CSVRecord> records = new ArrayList<>();

        while ((record = parser.nextRecord()) != null) {
            records.add(record);
        }

        assertEquals(4, records.size());
        assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
        assertArrayEquals(new String[]{"c", "d;e"}, records.get(1).values());
        assertArrayEquals(new String[]{"1", "2", "3", "4"}, records.get(2).values());
        assertNull(records.get(3).getComment());
    }

    @Test
    public void testParseString() throws Exception {
        parser = new CSVParser(new StringReader(CSV_STRING), CSVFormat.DEFAULT);
        CSVRecord record;
        List<CSVRecord> records = new ArrayList<>();

        while ((record = parser.nextRecord()) != null) {
            records.add(record);
        }

        assertEquals(3, records.size());
        assertArrayEquals(new String[]{"a"}, records.get(0).values());
        assertArrayEquals(new String[]{"c", "d;e"}, records.get(1).values());
        assertArrayEquals(new String[]{"1", "2", "3", "4"}, records.get(2).values());
        assertNull(records.get(2).getComment());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileWithNullFormat() throws Exception {
        parser = CSVParser.parse(CSV_FILE_PATH, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileWithInvalidFormat() throws Exception {
        parser = CSVParser.parse(CSV_FILE_PATH, new CSVFormat());
    }

    @Test(expected = IOException.class)
    public void testParseFileWithIOException() throws Exception {
        parser = CSVParser.parse(new File("non_existing_file.csv"), CSVFormat.DEFAULT);
        parser.nextRecord();
        assertThrows(IOException.class, parser::nextRecord);
    }

    @Test(expected = IllegalStateException.class)
    public void testParseFileAfterClosed() throws Exception {
        parser.close();
        parser.nextRecord();
        assertThrows(IllegalStateException.class, parser::nextRecord);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithIOException() throws Exception {
        URL url = getClass().getResource("/CSVParserTestData.csv");
        parser = new CSVParser(new InputStreamReader(url.openStream()), CSVFormat.DEFAULT);
        parser.nextRecord();
        assertThrows(IOException.class, parser::nextRecord);
    }
}