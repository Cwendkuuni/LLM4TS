import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
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

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("value1", record.get("column1"));
        Assert.assertEquals("value2", record.get("column2"));
        Assert.assertEquals("value3", record.get("column3"));
        Assert.assertNull(record.get("column4"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithNoMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("column1");
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isConsistent());

        mapping.put("column3", 2);
        Assert.assertTrue(record.isConsistent());

        mapping.put("column4", 3);
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isMapped("column1"));
        Assert.assertTrue(record.isMapped("column2"));
        Assert.assertFalse(record.isMapped("column3"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isSet("column1"));
        Assert.assertTrue(record.isSet("column2"));
        Assert.assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
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
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Assert.assertArrayEquals(values, record.values());
    }

    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, "comment", 1);
        Assert.assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Assert.assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Assert.assertEquals("[value1, value2, value3]", record.toString());
    }

}