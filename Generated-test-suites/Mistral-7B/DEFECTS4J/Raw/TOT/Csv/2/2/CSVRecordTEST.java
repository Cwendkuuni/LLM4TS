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
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("Name", record.get(0));
        Assert.assertEquals("Age", record.get(1));
        Assert.assertEquals("City", record.get(2));
        Assert.assertEquals(1, record.size());
        Assert.assertTrue(record.isConsistent());
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("Name", record.get(0));
        Assert.assertEquals("Age", record.get(1));
        Assert.assertEquals("City", record.get(2));
        Assert.assertNull(record.get("InvalidColumnName"));
        Assert.assertNull(record.get(-1));

        Assert.assertTrue(record.isConsistent());
    }

    /**
     * Test isConsistent method.
     */
    @Test
    public void testIsConsistent() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record1 = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record1.isConsistent());

        CSVRecord record2 = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record2.isConsistent());

        CSVRecord record3 = new CSVRecord(new String[]{"Name", "Age"}, mapping, null, 1);
        Assert.assertTrue(record3.isConsistent());

        CSVRecord record4 = new CSVRecord(values, null, null, 1);
        Assert.assertTrue(record4.isConsistent());

        CSVRecord record5 = new CSVRecord(values, mapping, "Comment", 1);
        Assert.assertTrue(record5.isConsistent());

        CSVRecord record6 = new CSVRecord(values, mapping, null, 0);
        Assert.assertTrue(record6.isConsistent());

        CSVRecord record7 = new CSVRecord(values, mapping, null, 2);
        Assert.assertFalse(record7.isConsistent());

        CSVRecord record8 = new CSVRecord(new String[]{"Name", "Age"}, mapping, null, 1);
        Assert.assertFalse(record8.isConsistent());

        CSVRecord record9 = new CSVRecord(values, null, null, 1);
        Assert.assertFalse(record9.isConsistent());
    }

    /**
     * Test isMapped method.
     */
    @Test
    public void testIsMapped() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertTrue(record.isMapped("City"));
        Assert.assertFalse(record.isMapped("InvalidColumnName"));
    }

    /**
     * Test isSet method.
     */
    @Test
    public void testIsSet() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record1 = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record1.isSet("Name"));
        Assert.assertTrue(record1.isSet("Age"));
        Assert.assertTrue(record1.isSet("City"));

        String[] values2 = {"Name", null, "City"};
        CSVRecord record2 = new CSVRecord(values2, mapping, null, 1);
        Assert.assertTrue(record2.isSet("Name"));
        Assert.assertFalse(record2.isSet("Age"));
        Assert.assertTrue(record2.isSet("City"));

        String[] values3 = {"Name", "Age", null};
        CSVRecord record3 = new CSVRecord(values3, mapping, null, 1);
        Assert.assertTrue(record3.isSet("Name"));
        Assert.assertTrue(record3.isSet("Age"));
        Assert.assertFalse(record3.isSet("City"));
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

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
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.values());
    }

    /**
     * Test getComment method.
     */
    @Test
    public void testGetComment() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, "Comment", 1);

        Assert.assertEquals("Comment", record.getComment());
        Assert.assertNull(new CSVRecord(values, mapping, null, 1).getComment());
    }

    /**
     * Test getRecordNumber method.
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals(1, record.getRecordNumber());
    }
}