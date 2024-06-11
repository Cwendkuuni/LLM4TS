package org.apache.commons.csv;

import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for CSVRecord.
 */
public class CSVRecordTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Assert.assertArrayEquals(values, record.values());
        Assert.assertEquals(mapping, record.mapping);
        Assert.assertNull(record.comment);
        Assert.assertEquals(0, record.recordNumber);
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Assert.assertEquals("Name", record.get(0));
        Assert.assertEquals("Age", record.get(1));
        Assert.assertEquals("City", record.get(2));
        Assert.assertNull(record.get(3));
    }

    /**
     * Test get method with null values.
     */
    @Test
    public void testGetWithNullValues() {
        String[] values = null;
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Assert.assertNull(record.get(0));
        Assert.assertNull(record.get(1));
        Assert.assertNull(record.get(2));
    }

    /**
     * Test get method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        record.get(values.length);
    }

    /**
     * Test get method with invalid name.
     */
    @Test
    public void testGetWithInvalidName() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Assert.assertNull(record.get("InvalidName"));
    }

    /**
     * Test isConsistent method.
     */
    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Assert.assertTrue(record.isConsistent());

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 0);
        Assert.assertTrue(emptyRecord.isConsistent());

        String[] valuesWithMismatchedLength = new String[]{"Name", "Age", "City", "Invalid"};
        Map<String, Integer> mappingWithMismatchedLength = new HashMap<>();
        mappingWithMismatchedLength.put("Name", 0);
        mappingWithMismatchedLength.put("Age", 1);
        mappingWithMismatchedLength.put("City", 2);
        CSVRecord recordWithMismatchedLength = new CSVRecord(valuesWithMismatchedLength, mappingWithMismatchedLength, null, 0);
        Assert.assertFalse(recordWithMismatchedLength.isConsistent());
    }

    /**
     * Test isMapped method.
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertTrue(record.isMapped("City"));
        Assert.assertFalse(record.isMapped("InvalidName"));
    }

    /**
     * Test isSet method.
     */
    @Test
    public void testIsSet() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertTrue(record.isSet("City"));
        Assert.assertFalse(record.isSet("InvalidName"));
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Iterator<String> iterator = record.iterator();

        Assert.assertEquals("Name", iterator.next());
        Assert.assertEquals("Age", iterator.next());
        Assert.assertEquals("City", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * Test values method.
     */
    @Test
    public void testValues() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        Assert.assertArrayEquals(values, record.values());
    }

    /**
     * Test getComment method.
     */
    @Test
    public void testGetComment() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, "Comment", 0);

        Assert.assertEquals("Comment", record.getComment());
    }

    /**
     * Test getRecordNumber method.
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 123);

        Assert.assertEquals(123, record.getRecordNumber());
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        Assert.assertEquals(3, record.size());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Assert.assertEquals("[Name, Age, City]", record.toString());
    }
}