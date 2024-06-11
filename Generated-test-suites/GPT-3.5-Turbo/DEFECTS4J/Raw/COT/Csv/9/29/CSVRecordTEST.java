import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "", 1);
        Assert.assertEquals("Value1", record.get(TestEnum.TEST));
    }

    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "", 1);
        Assert.assertEquals("Value2", record.get(1));
    }

    @Test
    public void testGetName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, mapping, "", 1);
        Assert.assertEquals("Value3", record.get("Column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "", 1);
        record.get("Column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, mapping, "", 1);
        record.get("Column4");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "This is a comment", 1);
        Assert.assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "", 5);
        Assert.assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistentWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, mapping, "", 1);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "", 1);
        Assert.assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, mapping, "", 1);
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, mapping, "", 1);
        Assert.assertTrue(record.isMapped("Column1"));
    }

    @Test
    public void testIsNotMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, mapping, "", 1);
        Assert.assertFalse(record.isMapped("Column4"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, mapping, "", 1);
        Assert.assertTrue(record.isSet("Column1"));
    }

    @Test
    public void testIsNotSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, mapping, "", 1);
        Assert.assertFalse(record.isSet("Column4"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "", 1);
        Assert.assertTrue(record.iterator().hasNext());
        Assert.assertEquals("Value1", record.iterator().next());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "", 1);
        record.putIn(map);
        Assert.assertEquals("Value1", map.get("Column1"));
        Assert.assertEquals("Value2", map.get("Column2"));
        Assert.assertEquals("Value3", map.get("Column3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "", 1);
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "", 1);
        Assert.assertEquals("Value1", record.toList().get(0));
        Assert.assertEquals("Value2", record.toList().get(1));
        Assert.assertEquals("Value3", record.toList().get(2));
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, mapping, "", 1);
        Map<String, String> result = record.toMap();
        Assert.assertEquals("Value1", result.get("Column1"));
        Assert.assertEquals("Value2", result.get("Column2"));
        Assert.assertEquals("Value3", result.get("Column3"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "", 1);
        Assert.assertEquals("[Value1, Value2, Value3]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "", 1);
        Assert.assertArrayEquals(new String[]{"Value1", "Value2", "Value3"}, record.values());
    }

    private enum TestEnum {
        TEST
    }
}