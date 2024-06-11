package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVParserBuilder;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.apache.commons.csv.DefaultCSVParser;
import org.apache.commons.csv.field.CSVName;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    private CSVParser parser;

    @Before
    public void setup() {
        this.parser = new CSVParserBuilder().build();
    }

    @Test
    public void testGet_WhenIndexValid_ReturnsValue() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"});
        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));
    }

    @Test
    public void testGet_WhenIndexInvalid_ReturnsNull() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"});
        assertNull(record.get(3));
    }

    @Test
    public void testGet_WhenNameValid_ReturnsValue() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }});
        assertEquals("Name", record.get("Name"));
        assertEquals("Age", record.get("Age"));
        assertEquals("City", record.get("City"));
        assertNull(record.get("Country"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_WhenNameInvalidAndNoMapping_ThrowsException() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"});
        record.get("Country");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"});
        assertTrue(record.isConsistent());

        CSVRecord recordWithExtraValue = createRecord(new String[]{"Name", "Age", "City", "Extra"});
        assertTrue(recordWithExtraValue.isConsistent());

        CSVRecord recordWithMissingValue = createRecord(new String[]{"Name", "Age"});
        assertFalse(recordWithMissingValue.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }});
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("Country"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }});
        assertFalse(record.isSet("Name2"));
        assertFalse(record.isSet("Name")); // by default, all values are set

        CSVRecord recordWithNullValue = createRecord(new String[]{"Name", "Age", "City"}, new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }}, null);
        assertFalse(recordWithNullValue.isSet("Name"));
        assertTrue(recordWithNullValue.isSet("Age"));
        assertTrue(recordWithNullValue.isSet("City"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"});
        List<String> values = Arrays.asList(record.values());
        Iterator<String> iterator = record.iterator();

        assertEquals(values.get(0), iterator.next());
        assertEquals(values.get(1), iterator.next());
        assertEquals(values.get(2), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValues() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"});
        assertArrayEquals(new String[]{"Name", "Age", "City"}, record.values());
    }

    @Test
    public void testComment() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, null, "Record comment");
        assertEquals("Record comment", record.getComment());
    }

    @Test
    public void testRecordNumber() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, null, null, 123L);
        assertEquals(123L, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"});
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"});
        assertEquals("[Name, Age, City]", record.toString());
    }

    @Test
    public void testGetRecordNumberFromCSVParser() throws IOException {
        String csvText = "Name,Age,City\nJohn Doe,34,New York\nJane Doe,29,Paris";
        InputStream inputStream = new ByteArrayInputStream(csvText.getBytes());
        CSVParser parser = new DefaultCSVParser(new InputStreamReader(inputStream));
        CSVRecordIterator iterator = parser.iterator();
        CSVRecord record = iterator.next();
        assertEquals(0, record.getRecordNumber());

        CSVRecord nextRecord = iterator.next();
        assertEquals(1, nextRecord.getRecordNumber());
    }

    private CSVRecord createRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) {
        return new CSVRecord(values, mapping, comment, recordNumber);
    }

    private CSVRecord createRecord(String[] values, Map<String, Integer> mapping, String comment) {
        return createRecord(values, mapping, comment, null);
    }

    private CSVRecord createRecord(String[] values, Map<String, Integer> mapping) {
        return createRecord(values, mapping, null, null);
    }

    private CSVRecord createRecord(String[] values) {
        CSVName header = new CSVName("header");
        InputStream inputStream = new ByteArrayInputStream(values[0].getBytes());
        CSVParser parser = new CSVParserBuilder().build();
        parser.parseHeader(new InputStreamReader(inputStream));
        return parser.parseRecord();
    }

}