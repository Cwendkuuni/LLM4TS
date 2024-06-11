import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstructor() {
        String[] values = new String[]{"John", "Doe", "age:25", "city:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);
        Assert.assertEquals(1L, csvRecord.getRecordNumber());
        Assert.assertEquals(Arrays.asList("John", "Doe", "age:25", "city:New York"), csvRecord.toList());
        Assert.assertEquals(mapping, csvRecord.mapping);
        Assert.assertNull(csvRecord.getComment());
    }

    @Test
    public void testGetString() {
        String[] values = new String[]{"John", "Doe", "age:25", "city:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);

        Assert.assertEquals("John", csvRecord.get(0));
        Assert.assertEquals("Doe", csvRecord.get(1));
        Assert.assertEquals("25", csvRecord.get("Age"));
        Assert.assertEquals("New York", csvRecord.get("City"));
        Assert.assertEquals("age:25", csvRecord.get(2));

        Assert.assertNull(csvRecord.get("InvalidColumnName"));
        Assert.assertThrows(IllegalArgumentException.class, () -> csvRecord.get(-1));
        Assert.assertThrows(IllegalArgumentException.class, () -> csvRecord.get(values.length));
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"John", "Doe", "age:25", "city:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, "Comment", 1L);
        Assert.assertEquals("Comment", csvRecord.getComment());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"John", "Doe", "age:25", "city:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);
        Assert.assertTrue(csvRecord.isConsistent());

        String[] invalidValues = new String[]{"John", "Doe", "age:25", null};
        Map<String, Integer> invalidMapping = new HashMap<>();
        invalidMapping.put("Name", 0);
        invalidMapping.put("Age", 1);
        invalidMapping.put("City", 3);
        CSVRecord invalidCSVRecord = new CSVRecord(invalidValues, invalidMapping, null, 1L);
        Assert.assertFalse(invalidCSVRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"John", "Doe", "age:25", "city:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);
        Assert.assertTrue(csvRecord.isMapped("Name"));
        Assert.assertTrue(csvRecord.isMapped("Age"));
        Assert.assertTrue(csvRecord.isMapped("City"));
        Assert.assertFalse(csvRecord.isMapped("InvalidColumnName"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"John", "Doe", "age:25", "city:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);
        Assert.assertTrue(csvRecord.isSet("Name"));
        Assert.assertTrue(csvRecord.isSet("Age"));
        Assert.assertTrue(csvRecord.isSet("City"));
        Assert.assertFalse(csvRecord.isSet("InvalidColumnName"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"John", "Doe", "age:25", "city:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);

        Iterator<String> iterator = csvRecord.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.startsWith("age:")) {
                count++;
            }
        }
        Assert.assertEquals(1, count);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWhenNoHeaderMapping() {
        String[] values = new String[]{"John", "Doe", "age:25", "city:New York"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1L);
        csvRecord.get("Name");
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"John", "Doe", "age:25", "city:New York"};
        Map<String, String> map = new HashMap<>();
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1L);
        map.putAll(csvRecord.putIn(map));
        Assert.assertEquals("John", map.get("Name"));
        Assert.assertEquals("Doe", map.get("Name2")); // should not be present
        Assert.assertEquals("age:25", map.get("Age"));
        Assert.assertEquals("city:New York", map.get("City"));
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"John", "Doe", "age:25", "city:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);
        Assert.assertEquals(values.length, csvRecord.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"John", "Doe", "age:25", "city:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);
        Assert.assertEquals("[John, Doe, age:25, city:New York]", csvRecord.toString());
    }
}