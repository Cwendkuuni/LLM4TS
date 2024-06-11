import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CSVRecord}.
 */
public class CSVRecordTest {

    /**
     * Test record with valid values.
     */
    @Test
    public void testValidRecord() {
        final String csv = "Name,Age,City\nJohn Doe,30,New York\n";
        final CSVFormat csvFormat = new CSVFormatBuilder().setHeaderNames(new String[]{"Name", "Age", "City"}).build();
        final CSVParser parser = new CSVParser(new StringReader(csv), csvFormat);
        final CSVRecord record = parser.nextRecord();
        assertSame(CSVRecord.class, record.getClass());
        assertArrayEquals(new String[]{"John Doe", "30", "New York"}, record.values());
        assertEquals("John Doe", record.get("Name"));
        assertEquals("New York", record.get("City"));
        assertFalse(record.isConsistent()); // CSVFormat does not support size validation
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    /**
     * Test record with empty values.
     */
    @Test
    public void testEmptyRecord() {
        final String csv = "\n";
        final CSVFormat csvFormat = new CSVFormatBuilder().build();
        final CSVParser parser = new CSVParser(new StringReader(csv), csvFormat);
        final CSVRecord record = parser.nextRecord();
        assertSame(CSVRecord.class, record.getClass());
        assertArrayEquals(CSVRecord.EMPTY_STRING_ARRAY, record.values());
        assertFalse(record.isConsistent()); // CSVFormat does not support size validation
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isSet("Name"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    /**
     * Test record with missing values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMissingValue() {
        final String csv = "Name,Age\nJohn Doe,30\n";
        final CSVFormat csvFormat = new CSVFormatBuilder().setHeaderNames(new String[]{"Name", "Age", "City"}).build();
        final CSVParser parser = new CSVParser(new StringReader(csv), csvFormat);
        parser.nextRecord(); // consume the first record
        parser.nextRecord(); // consume the record with valid values
        final CSVRecord record = parser.nextRecord();
        assertSame(CSVRecord.class, record.getClass());
        assertArrayEquals(new String[2], record.values());
        assertFalse(record.isConsistent()); // CSVFormat does not support size validation
        assertFalse(record.isMapped("City"));
        assertFalse(record.isSet("City"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    /**
     * Test record with null values.
     */
    @Test
    public void testNullValues() {
        final String csv = "Name,Age,City\nJohn Doe,null,New York\n";
        final CSVFormat csvFormat = new CSVFormatBuilder().setNullString("null").setHeaderNames(new String[]{"Name", "Age", "City"}).build();
        final CSVParser parser = new CSVParser(new StringReader(csv), csvFormat);
        final CSVRecord record = parser.nextRecord();
        assertSame(CSVRecord.class, record.getClass());
        assertArrayEquals(new String[]{"John Doe", null, "New York"}, record.values());
        assertEquals(null, record.get("Age"));
        assertEquals("New York", record.get("City"));
        assertFalse(record.isConsistent()); // CSVFormat does not support size validation
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    /**
     * Test record with comments.
     */
    @Test
    public void testRecordWithComments() {
        final String csv = "Name,Age,City\nJohn Doe,30,New York# This is a comment\n";
        final CSVFormat csvFormat = new CSVFormatBuilder().setHeaderNames(new String[]{"Name", "Age", "City"}).build();
        final CSVParser parser = new CSVParser(new StringReader(csv), csvFormat);
        final CSVRecord record = parser.nextRecord();
        assertSame(CSVRecord.class, record.getClass());
        assertArrayEquals(new String[]{"John Doe", "30", "New York"}, record.values());
        assertEquals("This is a comment", record.getComment());
        assertFalse(record.isConsistent()); // CSVFormat does not support size validation
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertNull(record.getRecordNumber());
    }

    /**
     * Test record with number values.
     */
    @Test
    public void testRecordWithNumberValues() {
        final String csv = "Name,Age,City\nJohn Doe,30,New York\n";
        final CSVFormat csvFormat = new CSVFormatBuilder().setHeaderNames(new String[]{"Name", "Age", "City"}).build();
        final CSVParser parser = new CSVParser(new StringReader(csv), csvFormat);
        final CSVRecord record = parser.nextRecord();
        assertSame(CSVRecord.class, record.getClass());
        assertArrayEquals(new String[]{"John Doe", "30", "New York"}, record.values());
        assertEquals("30", record.get("Age"));
        assertEquals("New York", record.get("City"));
        assertFalse(record.isConsistent()); // CSVFormat does not support size validation
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    /**
     * Test record with empty header names.
     */
    @Test
    public void testEmptyHeaderNames() {
        final String csv = "Name,Age,City\nJohn Doe,30,New York\n";
        final CSVFormat csvFormat = new CSVFormatBuilder().build();
        final CSVParser parser = new CSVParser(new StringReader(csv), csvFormat);
        final CSVRecord record = parser.nextRecord();
        assertSame(CSVRecord.class, record.getClass());
        assertArrayEquals(new String[]{"John Doe", "30", "New York"}, record.values());
        assertFalse(record.isConsistent()); // CSVFormat does not support size validation
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isSet("Name"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    /**
     * Test record with custom null String.
     */
    @Test
    public void testRecordWithCustomNullString() throws IOException {
        final String csv = "Name,Age,City\nJohn Doe,null,New York\n";
        final String nullString = "NA";
        final CSVFormat csvFormat = new CSVFormatBuilder().setNullString(nullString).setHeaderNames(new String[]{"Name", "Age", "City"}).build();
        final StringReader reader = new StringReader(csv);
        final CSVParser parser = new CSVParser(reader, csvFormat);
        final CSVRecord record = parser.nextRecord();
        assertSame(CSVRecord.class, record.getClass());
        assertArrayEquals(new String[]{"John Doe", null, "New York"}, record.values());
        assertEquals(nullString, record.get("Age"));
        assertEquals("New York", record.get("City"));
        assertFalse(record.isConsistent()); // CSVFormat does not support size validation
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    /**
     * Test record with custom null String and comment.
     */
    @Test
    public void testRecordWithCustomNullStringAndComment() throws IOException {
        final String csv = "Name,Age,City# This is a comment\nJohn Doe,null,New York\n";
        final String nullString = "NA";
        final CSVFormat csvFormat = new CSVFormatBuilder().setNullString(nullString).setHeaderNames(new String[]{"Name", "Age", "City"}).build();
        final StringReader reader = new StringReader(csv);
        final CSVParser parser = new CSVParser(reader, csvFormat);
        final CSVRecord record = parser.nextRecord();
        assertSame(CSVRecord.class, record.getClass());
        assertArrayEquals(new String[]{"John Doe", null, "New York"}, record.values());
        assertEquals("This is a comment", record.getComment());
        assertEquals(nullString, record.get("Age"));
        assertEquals("New York", record.get("City"));
        assertFalse(record.isConsistent()); // CSVFormat does not support size validation
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertNull(record.getRecordNumber());
    }
}