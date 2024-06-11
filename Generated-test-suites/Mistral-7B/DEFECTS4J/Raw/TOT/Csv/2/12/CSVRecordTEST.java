package org.apache.commons.csv.test;

import static org.junit.Assert.*;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordParser;
import org.apache.commons.csv.CSVParserBuilder;
import org.junit.Test;

/**
 * Tests for {@link CSVRecord}.
 */
public class CSVRecordTest {

    /**
     * Test constructor with valid values, mapping, and comment.
     */
    @Test
    public void testValidCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(3, record.size());
        assertEquals("John Doe", record.get(0));
        assertEquals("30", record.get(1));
        assertEquals("New York", record.get(2));
        assertEquals(0, record.get("Name").intValue());
        assertEquals(1, record.get("Age").intValue());
        assertEquals(2, record.get("City").intValue());
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
        assertEquals(mapping, record.mapping);
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
    }

    /**
     * Test constructor with null values, mapping, and comment.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullValuesCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = null;

        new CSVRecord(values, mapping, comment, recordNumber);
    }

    /**
     * Test constructor with empty values array.
     */
    @Test
    public void testEmptyValuesCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with empty data";
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(new String[0], mapping, comment, recordNumber);

        assertEquals(0, record.size());
        assertNull(record.get(0));
        assertNull(record.get(1));
        assertNull(record.get(2));
        assertNull(record.get("Name"));
        assertNull(record.get("Age"));
        assertNull(record.get("City"));
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
        assertEquals(mapping, record.mapping);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("Name"));
        assertFalse(record.isMapped("Age"));
        assertFalse(record.isMapped("City"));
        assertFalse(record.isSet("Name"));
        assertFalse(record.isSet("Age"));
        assertFalse(record.isSet("City"));
    }

    /**
     * Test constructor with invalid mapping size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMappingSizeCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        new CSVRecord(values, mapping, comment, recordNumber);
    }

    /**
     * Test get method with valid index.
     */
    @Test
    public void testGetValidIndexCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("John Doe", record.get(0));
        assertEquals("30", record.get(1));
        assertEquals("New York", record.get(2));
    }

    /**
     * Test get method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndexCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        record.get(3);
    }

    /**
     * Test get method with valid name and exists.
     */
    @Test
    public void testGetValidNameCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("John Doe", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertNull(record.get("City2"));
    }

    /**
     * Test get method with valid name and not exists.
     */
    @Test
    public void testGetInvalidNameCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertNull(record.get("City2"));
    }

    /**
     * Test isConsistent method with valid values.
     */
    @Test
    public void testIsConsistentValidValuesCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(record.isConsistent());
    }

    /**
     * Test isConsistent method with invalid values.
     */
    @Test
    public void testIsConsistentInvalidValuesCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York", "Some extra data"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertFalse(record.isConsistent());
    }

    /**
     * Test isMapped method with valid name.
     */
    @Test
    public void testIsMappedValidNameCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
    }

    /**
     * Test isMapped method with invalid name.
     */
    @Test
    public void testIsMappedInvalidNameCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertFalse(record.isMapped("City2"));
    }

    /**
     * Test isSet method with valid name.
     */
    @Test
    public void testIsSetValidNameCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
    }

    /**
     * Test isSet method with invalid name.
     */
    @Test
    public void testIsSetInvalidNameCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertFalse(record.isSet("City2"));
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIteratorCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Iterator<String> it = record.iterator();

        assertEquals("John Doe", it.next());
        assertEquals("30", it.next());
        assertEquals("New York", it.next());
        assertFalse(it.hasNext());
    }

    /**
     * Test values method.
     */
    @Test
    public void testValuesCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        String[] result = record.values();

        assertEquals(3, result.length);
        assertEquals("John Doe", result[0]);
        assertEquals("30", result[1]);
        assertEquals("New York", result[2]);
    }

    /**
     * Test getComment method.
     */
    @Test
    public void testGetCommentCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(comment, record.getComment());
    }

    /**
     * Test getRecordNumber method.
     */
    @Test
    public void testGetRecordNumberCSVRecord() {
        String[] header = {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        String[] values = {"John Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(recordNumber, record.getRecordNumber());
    }
}