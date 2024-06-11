import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertEquals("value1", record.get(TestEnum.ONE));
    }

    @Test
    public void testGetValidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            record.get(3);
        });
    }

    @Test
    public void testGetValidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertEquals("value3", record.get("name3"));
    }

    @Test
    public void testGetInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            record.get("name4");
        });
    }

    @Test
    public void testIsConsistentWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsInconsistentWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1);
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsInconsistentWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Assert.assertFalse(record.isConsistent());
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        return mapping;
    }

    @Test
    public void testIsMappedWithValidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        Assert.assertTrue(record.isMapped("name2"));
    }

    @Test
    public void testIsMappedWithInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        Assert.assertFalse(record.isMapped("name4"));
    }

    @Test
    public void testIsSetWithMappedAndValidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        Assert.assertTrue(record.isSet("name1"));
    }

    @Test
    public void testIsSetWithMappedAndInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        Assert.assertFalse(record.isSet("name4"));
    }

    @Test
    public void testIsSetWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertFalse(record.isSet("name1"));
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[0], null, "This is a comment", 1);
        Assert.assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 5);
        Assert.assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Iterator<String> iterator = record.iterator();
        Assert.assertEquals("value1", iterator.next());
        Assert.assertEquals("value2", iterator.next());
        Assert.assertEquals("value3", iterator.next());
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        Assert.assertEquals("value1", map.get("name1"));
        Assert.assertEquals("value2", map.get("name2"));
        Assert.assertEquals("value3", map.get("name3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        List<String> list = record.toList();
        Assert.assertEquals("value1", list.get(0));
        Assert.assertEquals("value2", list.get(1));
        Assert.assertEquals("value3", list.get(2));
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), null, 1);
        Map<String, String> map = record.toMap();
        Assert.assertEquals("value1", map.get("name1"));
        Assert.assertEquals("value2", map.get("name2"));
        Assert.assertEquals("value3", map.get("name3"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertEquals("[value1, value2, value3]", record.toString());
    }
}