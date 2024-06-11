import org.junit.Test;
import org.junit.Assert;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, null, null, 1);
        Assert.assertEquals("a", record.get(TestEnum.A));
    }

    @Test
    public void testGetInt() {
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, null, null, 1);
        Assert.assertEquals("a", record.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, null, null, 1);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("a", 0);
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, mapping, null, 1);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("a", 5);
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, mapping, null, 1);
        record.get("a");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, null, "comment", 1);
        Assert.assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, null, null, 10);
        long recordNumber = record.getRecordNumber();
        Assert.assertEquals(10, recordNumber);
    }

    @Test
    public void testIsConsistentWithNullMapping() {
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, null, null, 1);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithValidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("a", 0);
        mapping.put("b", 1);
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, mapping, null, 1);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("a", 0);
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, mapping, null, 1);
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMappedWithMappedColumn() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("a", 0);
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, mapping, null, 1);
        Assert.assertTrue(record.isMapped("a"));
    }

    @Test
    public void testIsMappedWithUnmappedColumn() {
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, null, null, 1);
        Assert.assertFalse(record.isMapped("d"));
    }

    @Test
    public void testIsSetWithUnmappedColumn() {
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, null, null, 1);
        Assert.assertFalse(record.isSet("d"));
    }

    @Test
    public void testIsSetWithMappedColumnAndValue() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("a", 0);
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, mapping, null, 1);
        Assert.assertTrue(record.isSet("a"));
    }

    @Test
    public void testIsSetWithMappedColumnAndNoValue() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("a", 5);
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, mapping, null, 1);
        Assert.assertFalse(record.isSet("a"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, null, null, 1);
        Assert.assertEquals(3, record.iterator().toList().size());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("a", 0);
        mapping.put("b", 1);
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, mapping, null, 1);
        Map<String, String> resultMap = record.putIn(map);
        Assert.assertEquals(2, resultMap.size());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, null, null, 1);
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, null, null, 1);
        Assert.assertEquals("[a, b, c]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"}, null, null, 1);
        String[] values = record.values();
        Assert.assertEquals(3, values.length);
    }

    private enum TestEnum {
        A, B, C
    }

}