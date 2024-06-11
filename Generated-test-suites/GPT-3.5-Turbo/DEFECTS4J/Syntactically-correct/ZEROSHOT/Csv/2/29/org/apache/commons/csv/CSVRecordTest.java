import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
        Assert.assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        Assert.assertEquals("value1", record.get("column1"));
        Assert.assertEquals("value2", record.get("column2"));
        Assert.assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        String[] values = {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, null, "", 1);
        record.get("column1");
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsConsistentNoMapping() {
        String[] values = {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, null, "", 1);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        Assert.assertTrue(record.isMapped("column1"));
    }

    @Test
    public void testIsNotMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        Assert.assertFalse(record.isMapped("column1"));
    }

    @Test
    public void testIsMappedNoMapping() {
        String[] values = {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, null, "", 1);
        Assert.assertFalse(record.isMapped("column1"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        Assert.assertTrue(record.isSet("column1"));
    }

    @Test
    public void testIsNotSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 2);
        mapping.put("column3", 3);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        Assert.assertFalse(record.isSet("column2"));
    }

    @Test
    public void testIsSetNoMapping() {
        String[] values = {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, null, "", 1);
        Assert.assertFalse(record.isSet("column1"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, null, "", 1);
        Iterator<String> iterator = record.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("value1", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("value2", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("value3", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, null, "", 1);
        String[] result = record.values();
        Assert.assertArrayEquals(values, result);
    }

    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, null, "comment", 1);
        String comment = record.getComment();
        Assert.assertEquals("comment", comment);
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, null, "", 1);
        long recordNumber = record.getRecordNumber();
        Assert.assertEquals(1, recordNumber);
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, null, "", 1);
        int size = record.size();
        Assert.assertEquals(3, size);
    }

    @Override
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};

        CSVRecord record = new CSVRecord(values, null, "", 1);
        String result = record.toString();
        Assert.assertEquals("[value1, value2, value3]", result);
    }

}