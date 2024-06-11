import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("enum1", 0);
        mapping.put("enum2", 1);
        mapping.put("enum3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertEquals("value1", record.get(MockEnum.enum1));
        Assert.assertEquals("value2", record.get(MockEnum.enum2));
        Assert.assertEquals("value3", record.get(MockEnum.enum3));
    }

    @Test
    public void testGetIndex() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
        Assert.assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertEquals("value1", record.get("name1"));
        Assert.assertEquals("value2", record.get("name2"));
        Assert.assertEquals("value3", record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, "comment", 1);

        record.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameNotMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        record.get("name3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameIndexOutOfBounds() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        record.get("name2");
    }

    @Test
    public void testGetComment() {
        String[] values = {};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertFalse(record.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsNotConsistentNoMapping() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, "comment", 1);

        record.isConsistent();
    }

    @Test
    public void testIsMapped() {
        String[] values = {};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertTrue(record.isMapped("name1"));
        Assert.assertTrue(record.isMapped("name2"));
    }

    @Test
    public void testIsNotMapped() {
        String[] values = {};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertFalse(record.isMapped("name1"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertTrue(record.isSet("name1"));
        Assert.assertTrue(record.isSet("name2"));
    }

    @Test
    public void testIsNotSet() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertFalse(record.isSet("name3"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Iterator<String> iterator = record.iterator();
        Assert.assertEquals("value1", iterator.next());
        Assert.assertEquals("value2", iterator.next());
        Assert.assertEquals("value3", iterator.next());
    }

    @Test
    public void testPutIn() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Map<String, String> map = record.putIn(new HashMap<>());
        Assert.assertEquals("value1", map.get("name1"));
        Assert.assertEquals("value2", map.get("name2"));
        Assert.assertEquals("value3", map.get("name3"));
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        List<String> list = record.toList();
        Assert.assertEquals("value1", list.get(0));
        Assert.assertEquals("value2", list.get(1));
        Assert.assertEquals("value3", list.get(2));
    }

    @Test
    public void testToMap() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Map<String, String> map = record.toMap();
        Assert.assertEquals("value1", map.get("name1"));
        Assert.assertEquals("value2", map.get("name2"));
        Assert.assertEquals("value3", map.get("name3"));
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        Assert.assertArrayEquals(values, record.values());
    }

    private enum MockEnum {
        enum1,
        enum2,
        enum3
    }
}