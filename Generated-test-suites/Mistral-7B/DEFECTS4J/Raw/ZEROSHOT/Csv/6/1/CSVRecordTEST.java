import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Assert.assertArrayEquals(values, record.values());
        Assert.assertEquals(mapping, record.mapping);
        Assert.assertNull(record.getComment());
        Assert.assertEquals(1L, record.getRecordNumber());
        Assert.assertTrue(record.isConsistent());
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isSet("Name"));
        Assert.assertEquals("Alice", record.get("Name"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordNoMapping() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        record.get("Name");
    }

    @Test
    public void testCSVRecordGetValueByEnum() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Assert.assertEquals("Alice", record.get(Enum.valueOf(String.class, "Name")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordGetValueByNameNull() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        record.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordGetValueByNameNotFound() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        record.get("Unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordGetValueByIndexOutOfBounds() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        record.get(values.length + 1);
    }

    @Test
    public void testCSVRecordGetComment() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, "Comment", 1L);
        Assert.assertEquals("Comment", record.getComment());
    }

    @Test
    public void testCSVRecordGetRecordNumber() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Assert.assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testCSVRecordIsConsistent() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Assert.assertTrue(record.isConsistent());

        String[] wrongValues = new String[]{"Alice", "25"};
        record = new CSVRecord(wrongValues, mapping, null, 1L);
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordIsMapped() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertTrue(record.isMapped("Lastname"));
        Assert.assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testCSVRecordIsSet() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertTrue(record.isSet("Lastname"));
        Assert.assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testCSVRecordToString() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Assert.assertEquals("[Alice, 25, Smith]", record.toString());
    }

    @Test
    public void testCSVRecordSize() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testCSVRecordIterator() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        List<String> expectedList = Arrays.asList(new String[]{"Alice", "25", "Smith"});

        Iterator<String> iterator = record.iterator();
        for (String expectedValue : expectedList) {
            Assert.assertEquals(expectedValue, iterator.next());
        }
    }

    @Test
    public void testCSVRecordPutIn() {
        String[] values = new String[]{"Alice", "25", "Smith"};
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "Alice");
        expectedMap.put("Age", "25");
        expectedMap.put("Lastname", "Smith");

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Lastname", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> map = record.putIn(new HashMap<String, String>(values.length));
        Assert.assertEquals(expectedMap, map);
    }
}