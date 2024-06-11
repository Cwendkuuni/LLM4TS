import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        // Create a CSVRecord object with values and mapping
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "Comment", 1);

        // Test getting a value by enum
        Assert.assertEquals("Value1", record.get(TestEnum.TEST1));
    }

    @Test
    public void testGetIndex() {
        // Create a CSVRecord object with values
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test getting a value by index
        Assert.assertEquals("Value2", record.get(1));
    }

    @Test
    public void testGetName() {
        // Create a CSVRecord object with values and mapping
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test getting a value by name
        Assert.assertEquals("Value3", record.get("Column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithoutMapping() {
        // Create a CSVRecord object without mapping
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test getting a value by name without mapping
        record.get("Column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameInvalidMapping() {
        // Create a CSVRecord object with values and invalid mapping
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 3); // Invalid index
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test getting a value by name with invalid mapping
        record.get("Column3");
    }

    @Test
    public void testGetComment() {
        // Create a CSVRecord object with comment
        CSVRecord record = new CSVRecord(null, null, "This is a comment", 1);

        // Test getting the comment
        Assert.assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        // Create a CSVRecord object with record number
        CSVRecord record = new CSVRecord(null, null, null, 10);

        // Test getting the record number
        Assert.assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        // Create a CSVRecord object with values and mapping
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test isConsistent() method
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentInvalidMapping() {
        // Create a CSVRecord object with values and invalid mapping
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 3); // Invalid index
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test isConsistent() method with invalid mapping
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        // Create a CSVRecord object with mapping
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test isMapped() method
        Assert.assertTrue(record.isMapped("Column2"));
    }

    @Test
    public void testIsMappedNotMapped() {
        // Create a CSVRecord object without mapping
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test isMapped() method without mapping
        Assert.assertFalse(record.isMapped("Column1"));
    }

    @Test
    public void testIsSet() {
        // Create a CSVRecord object with values and mapping
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test isSet() method
        Assert.assertTrue(record.isSet("Column3"));
    }

    @Test
    public void testIsSetNotMapped() {
        // Create a CSVRecord object without mapping
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test isSet() method without mapping
        Assert.assertFalse(record.isSet("Column1"));
    }

    @Test
    public void testIterator() {
        // Create a CSVRecord object with values
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test iterator() method
        Iterator<String> iterator = record.iterator();
        Assert.assertEquals("Value1", iterator.next());
        Assert.assertEquals("Value2", iterator.next());
        Assert.assertEquals("Value3", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        // Create a CSVRecord object with values and mapping
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Create a new map and put values from CSVRecord
        Map<String, String> map = new HashMap<>();
        record.putIn(map);

        // Test putIn() method
        Assert.assertEquals("Value1", map.get("Column1"));
        Assert.assertEquals("Value2", map.get("Column2"));
        Assert.assertEquals("Value3", map.get("Column3"));
    }

    @Test
    public void testSize() {
        // Create a CSVRecord object with values
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test size() method
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToMap() {
        // Create a CSVRecord object with values and mapping
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test toMap() method
        Map<String, String> map = record.toMap();
        Assert.assertEquals("Value1", map.get("Column1"));
        Assert.assertEquals("Value2", map.get("Column2"));
        Assert.assertEquals("Value3", map.get("Column3"));
    }

    @Test
    public void testValues() {
        // Create a CSVRecord object with values
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test values() method
        Assert.assertArrayEquals(new String[]{"Value1", "Value2", "Value3"}, record.values());
    }

}