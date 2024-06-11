import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        String expected = "value";
        record.values = new String[]{"value"};

        String actual = record.get(0);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        String expected = "value";
        record.values = new String[]{"value"};

        String actual = record.get(0);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetByNameWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, new HashMap<>(), null, 0);
        String name = "column";
        String mappingKey = "column";
        record.mapping.put(mappingKey, 0);
        String expected = "value";

        String actual = record.get(name);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, null, null, 0);

        record.get("column");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, new HashMap<>(), null, 0);

        record.get("column");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, new HashMap<>(), null, 0);
        String name = "column";
        String mappingKey = "column";
        record.mapping.put(mappingKey, 1);

        record.get(name);
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "comment", 0);
        String expected = "comment";

        String actual = record.getComment();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 5);
        long expected = 5;

        long actual = record.getRecordNumber();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsConsistentWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, new HashMap<>(), null, 0);
        boolean expected = true;

        boolean actual = record.isConsistent();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, null, null, 0);
        boolean expected = true;

        boolean actual = record.isConsistent();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsMappedWithMapping() {
        CSVRecord record = new CSVRecord(null, new HashMap<>(), null, 0);
        String name = "column";
        record.mapping.put(name, 0);
        boolean expected = true;

        boolean actual = record.isMapped(name);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsMappedWithoutMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        String name = "column";
        boolean expected = false;

        boolean actual = record.isMapped(name);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsSetWithMappingAndValidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, new HashMap<>(), null, 0);
        String name = "column";
        String mappingKey = "column";
        record.mapping.put(mappingKey, 0);
        boolean expected = true;

        boolean actual = record.isSet(name);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsSetWithMappingAndInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, new HashMap<>(), null, 0);
        String name = "column";
        String mappingKey = "column";
        record.mapping.put(mappingKey, 1);
        boolean expected = false;

        boolean actual = record.isSet(name);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsSetWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, null, null, 0);
        String name = "column";
        boolean expected = false;

        boolean actual = record.isSet(name);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        String[] expected = new String[]{"value1", "value2"};

        int i = 0;
        for (String value : record) {
            Assert.assertEquals(expected[i], value);
            i++;
        }
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, new HashMap<>(), null, 0);
        Map<String, String> map = new HashMap<>();
        String mappingKey = "column";
        record.mapping.put(mappingKey, 0);
        String expected = "value";

        map = record.putIn(map);
        String actual = map.get(mappingKey);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        int expected = 2;

        int actual = record.size();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        List<String> expected = Arrays.asList("value1", "value2");

        List<String> actual = record.toList();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, new HashMap<>(), null, 0);
        Map<String, String> expected = new HashMap<>();
        String mappingKey = "column";
        record.mapping.put(mappingKey, 0);
        expected.put(mappingKey, "value");

        Map<String, String> actual = record.toMap();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        String expected = "[value1, value2]";

        String actual = record.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        String[] expected = new String[]{"value1", "value2"};

        String[] actual = record.values();

        Assert.assertEquals(expected, actual);
    }
}