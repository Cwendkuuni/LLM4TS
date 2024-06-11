import static org.junit.Assert.*;

import org.apache.commons.csv.CSVFormat;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        record = new CSVRecord(values, null, null, 1);
    }

    @Test
    public void testGetByEnum() {
        Enum<EnumTest> enumValue = EnumTest.TEST1;
        String expected = "value1";
        String actual = record.get(enumValue);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetByIndex() {
        int index = 0;
        String expected = "value1";
        String actual = record.get(index);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetByName() {
        String name = "name1";
        String expected = "value1";
        record.mapping = new HashMap<String, Integer>();
        record.mapping.put(name, 0);
        String actual = record.get(name);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWhenNoMapping() {
        String name = "name1";
        record.get(name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWhenNameNotFound() {
        String name = "name1";
        record.mapping = new HashMap<String, Integer>();
        record.get(name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWhenIndexOutOfBounds() {
        String name = "name1";
        record.mapping = new HashMap<String, Integer>();
        record.mapping.put(name, 1);
        record.get(name);
    }

    @Test
    public void testGetComment() {
        String expected = null;
        String actual = record.getComment();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRecordNumber() {
        long expected = 1;
        long actual = record.getRecordNumber();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsConsistentWhenMappingNull() {
        boolean expected = true;
        boolean actual = record.isConsistent();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsConsistentWhenMappingSizeEqualsValuesLength() {
        boolean expected = true;
        record.mapping = new HashMap<String, Integer>();
        record.mapping.put("name1", 0);
        record.mapping.put("name2", 1);
        record.mapping.put("name3", 2);
        boolean actual = record.isConsistent();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsConsistentWhenMappingSizeNotEqualsValuesLength() {
        boolean expected = false;
        record.mapping = new HashMap<String, Integer>();
        record.mapping.put("name1", 0);
        boolean actual = record.isConsistent();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsMappedWhenMappingNull() {
        boolean expected = false;
        boolean actual = record.isMapped("name1");
        assertEquals(expected, actual);
    }

    @Test
    public void testIsMappedWhenNameMapped() {
        boolean expected = true;
        record.mapping = new HashMap<String, Integer>();
        record.mapping.put("name1", 0);
        boolean actual = record.isMapped("name1");
        assertEquals(expected, actual);
    }

    @Test
    public void testIsMappedWhenNameNotMapped() {
        boolean expected = false;
        record.mapping = new HashMap<String, Integer>();
        record.mapping.put("name1", 0);
        boolean actual = record.isMapped("name2");
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSetWhenNameMappedAndIndexNotOutOfBounds() {
        boolean expected = true;
        record.mapping = new HashMap<String, Integer>();
        record.mapping.put("name1", 0);
        boolean actual = record.isSet("name1");
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSetWhenNameNotMapped() {
        boolean expected = false;
        boolean actual = record.isSet("name1");
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSetWhenNameMappedAndIndexOutOfBounds() {
        boolean expected = false;
        record.mapping = new HashMap<String, Integer>();
        record.mapping.put("name1", 1);
        boolean actual = record.isSet("name1");
        assertEquals(expected, actual);
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(record.size(), count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name1", "value1");
        map.put("name2", "value2");
        record.mapping = new HashMap<String, Integer>();
        record.mapping.put("name1", 0);
        record.mapping.put("name2", 1);
        record.mapping.put("name3", 2);
        record.putIn(map);
        assertEquals(record.get("name1"), map.get("name1"));
        assertEquals(record.get("name2"), map.get("name2"));
        assertNull(map.get("name3"));
    }

    @Test
    public void testSize() {
        int expected = 3;
        int actual = record.size();
        assertEquals(expected, actual);
    }

    @Test
    public void testToList() {
        List<String> expected = Arrays.asList("value1", "value2", "value3");
        List<String> actual = record.getToList();
        assertEquals(expected, actual);
    }

    @Test
    public void testToMap() {
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("name1", "value1");
        expected.put("name2", "value2");
        expected.put("name3", "value3");
        record.mapping = new HashMap<String, Integer>();
        record.mapping.put("name1", 0);
        record.mapping.put("name2", 1);
        record.mapping.put("name3", 2);
        Map<String, String> actual = record.toMap();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "[value1, value2, value3]";
        String actual = record.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testValues() {
        String[] expected = {"value1", "value2", "value3"};
        String[] actual = record.values();
        assertArrayEquals(expected, actual);
    }

    private enum EnumTest {
        TEST1, TEST2, TEST3
    }

}