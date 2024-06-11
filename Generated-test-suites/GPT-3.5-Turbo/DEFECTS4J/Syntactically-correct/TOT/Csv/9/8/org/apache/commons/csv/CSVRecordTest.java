import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setup() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        Enum<?> enumValue = TestEnum.COLUMN1;
        
        String result = csvRecord.get(enumValue);
        
        Assert.assertEquals("Value1", result);
    }

    @Test
    public void testGetByIndex() {
        int index = 1;
        
        String result = csvRecord.get(index);
        
        Assert.assertEquals("Value2", result);
    }

    @Test
    public void testGetByName() {
        String name = "Column3";
        
        String result = csvRecord.get(name);
        
        Assert.assertEquals("Value3", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithNoMapping() {
        csvRecord = new CSVRecord(null, null, null, 1);
        String name = "Column3";
        
        csvRecord.get(name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        String name = "InvalidColumn";
        
        csvRecord.get(name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        String name = "Column3";
        CSVRecord csvRecord = new CSVRecord(new String[]{"Value1"}, null, null, 1);
        
        csvRecord.get(name);
    }

    @Test
    public void testGetComment() {
        String result = csvRecord.getComment();
        
        Assert.assertEquals("This is a comment", result);
    }

    @Test
    public void testGetRecordNumber() {
        long result = csvRecord.getRecordNumber();
        
        Assert.assertEquals(1, result);
    }

    @Test
    public void testIsConsistent() {
        boolean result = csvRecord.isConsistent();
        
        Assert.assertTrue(result);
    }

    @Test
    public void testIsConsistentWithDifferentMapping() {
        csvRecord = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, new HashMap<>(), null, 1);
        
        boolean result = csvRecord.isConsistent();
        
        Assert.assertFalse(result);
    }

    @Test
    public void testIsMapped() {
        String name = "Column2";
        
        boolean result = csvRecord.isMapped(name);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testIsMappedWithInvalidName() {
        String name = "InvalidColumn";
        
        boolean result = csvRecord.isMapped(name);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testIsSet() {
        String name = "Column1";
        
        boolean result = csvRecord.isSet(name);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testIsSetWithInvalidName() {
        String name = "InvalidColumn";
        
        boolean result = csvRecord.isSet(name);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        
        Assert.assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        
        Map<String, String> result = csvRecord.putIn(map);
        
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("Value1", result.get("Column1"));
        Assert.assertEquals("Value2", result.get("Column2"));
        Assert.assertEquals("Value3", result.get("Column3"));
    }

    @Test
    public void testSize() {
        int result = csvRecord.size();
        
        Assert.assertEquals(3, result);
    }

    @Test
    public void testToList() {
        List<String> result = csvRecord.toList();
        
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("Value1", result.get(0));
        Assert.assertEquals("Value2", result.get(1));
        Assert.assertEquals("Value3", result.get(2));
    }

    @Test
    public void testToMap() {
        Map<String, String> result = csvRecord.toMap();
        
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("Value1", result.get("Column1"));
        Assert.assertEquals("Value2", result.get("Column2"));
        Assert.assertEquals("Value3", result.get("Column3"));
    }

    @Test
    public void testToString() {
        String result = csvRecord.toString();
        
        Assert.assertEquals("[Value1, Value2, Value3]", result);
    }

    @Test
    public void testValues() {
        String[] result = csvRecord.values();
        
        Assert.assertArrayEquals(new String[]{"Value1", "Value2", "Value3"}, result);
    }

    private enum TestEnum {
        COLUMN1
    }
}