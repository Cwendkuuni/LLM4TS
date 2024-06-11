import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Assert.assertEquals("value1", record.get(TestEnum.TEST1));
        Assert.assertEquals("value2", record.get(TestEnum.TEST2));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Assert.assertEquals("value1", record.get("name1"));
        Assert.assertEquals("value2", record.get("name2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        record.get("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        record.get("name3");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        Assert.assertEquals("Comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 10);
        Assert.assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Assert.assertTrue(record.isConsistent());

        record = new CSVRecord(new String[]{"value1"}, null, null, 1);
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Assert.assertFalse(record.isMapped("name1"));
        Assert.assertFalse(record.isMapped("name2"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Assert.assertFalse(record.isSet("name1"));
        Assert.assertFalse(record.isSet("name2"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Assert.assertEquals(2, record.size());
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Map<String, String> map = record.toMap();
        Assert.assertEquals("value1", map.get("name1"));
        Assert.assertEquals("value2", map.get("name2"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Assert.assertEquals("[value1, value2]", record.toString());
    }
    
    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        List<String> list = record.toList();
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("value1", list.get(0));
        Assert.assertEquals("value2", list.get(1));
    }

    private enum TestEnum {
        TEST1, TEST2
    }
}