import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstructor() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertArrayEquals(values, csvRecord.values());
        Assert.assertEquals(mapping, csvRecord.mapping);
        Assert.assertEquals(1, csvRecord.getRecordNumber());
        Assert.assertNull(csvRecord.getComment());
        Assert.assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testGetMethod() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("John", csvRecord.get(0));
        Assert.assertEquals("Doe", csvRecord.get(1));
        Assert.assertEquals("30", csvRecord.get(2));
        Assert.assertEquals("Doe", csvRecord.get(Enum.valueOf(CSVRecord.class, "Name")));
        Assert.assertEquals("30", csvRecord.get(Enum.valueOf(CSVRecord.class, "Age")));

        Assert.assertNull(csvRecord.get(3));
        Assert.assertNull(csvRecord.get(Enum.valueOf(CSVRecord.class, "Unknown")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNullMapping() {
        String[] values = new String[]{"John", "Doe", "30"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
        csvRecord.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidName() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        csvRecord.get("Unknown");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        csvRecord.get(3);
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, "Comment", 1);

        Assert.assertEquals("Comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(csvRecord.isConsistent());

        String[] valuesWithMissedColumn = new String[]{"John", "Doe"};
        Map<String, Integer> mappingWithMissedColumn = new HashMap<>();
        mappingWithMissedColumn.put("Name", 0);

        CSVRecord csvRecordWithMissedColumn = new CSVRecord(valuesWithMissedColumn, mappingWithMissedColumn, null, 1);

        Assert.assertFalse(csvRecordWithMissedColumn.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(csvRecord.isMapped("Name"));
        Assert.assertTrue(csvRecord.isMapped("Age"));
        Assert.assertFalse(csvRecord.isMapped("Unknown"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(csvRecord.isSet("Name"));
        Assert.assertTrue(csvRecord.isSet("Age"));
        Assert.assertFalse(csvRecord.isSet("Unknown"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        List<String> list = new ArrayList<>();

        for (String value : csvRecord) {
            list.add(value);
        }

        Assert.assertEquals(Arrays.asList("John", "Doe", "30"), list);
    }

    @Test
    public void testPutInMethod() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, String> map = new HashMap<>();

        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        Map<String, String> resultMap = csvRecord.putIn(map);

        Assert.assertEquals(3, resultMap.size());
        Assert.assertEquals("John", resultMap.get("Name"));
        Assert.assertEquals("Doe", resultMap.get("Doe"));
        Assert.assertEquals("30", resultMap.get("Age"));
    }

    @Test
    public void testSizeMethod() {
        String[] values = new String[]{"John", "Doe", "30"};

        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        Assert.assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToListMethod() {
        String[] values = new String[]{"John", "Doe", "30"};

        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        List<String> list = csvRecord.toList();

        Assert.assertSame(Arrays.asList(values), list);
    }

    @Test
    public void testToMapMethod() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Map<String, String> map = csvRecord.toMap();

        Assert.assertEquals(mapping.size(), map.size());
        Assert.assertEquals("John", map.get("Name"));
        Assert.assertEquals("30", map.get("Age"));
        Assert.assertNull(map.get("Unknown"));
    }
}