import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        // Create a sample CSVRecord object for testing
        String[] values = {"John", "Doe", "30"};
        record = new CSVRecord(values, null, null, 1);
    }

    @Test
    public void testGetEnum() {
        // Test getting a value by Enum
        assertEquals("John", record.get(MyEnum.NAME));
    }

    @Test
    public void testGetIndex() {
        // Test getting a value by index
        assertEquals("Doe", record.get(1));
    }

    @Test
    public void testGetName() {
        // Test getting a value by name
        assertEquals("30", record.get("Age"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithoutMapping() {
        // Test getting a value by name without mapping
        CSVRecord csvRecord = new CSVRecord(new String[0], null, null, 1);
        csvRecord.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        // Test getting a value by invalid name
        record.get("Invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidIndex() {
        // Test getting a value by invalid index
        record.get(10);
    }

    @Test
    public void testGetComment() {
        // Test getting comment
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        // Test getting record number
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        // Test checking if record is consistent
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        // Test checking if column is mapped
        assertFalse(record.isMapped("InvalidColumn"));
    }

    @Test
    public void testIsSet() {
        // Test checking if column is mapped and has a value
        assertTrue(record.isSet("Name"));
    }

    @Test
    public void testIterator() {
        // Test iterating over the values of the record
        String[] expectedValues = {"John", "Doe", "30"};
        int i = 0;
        for (String value : record) {
            assertEquals(expectedValues[i], value);
            i++;
        }
    }

    @Test
    public void testPutIn() {
        // Test putting all values of the record into a Map
        MyMap map = new MyMap();
        record.putIn(map);
        assertEquals("John", map.get("Name"));
        assertEquals("Doe", map.get("LastName"));
        assertEquals("30", map.get("Age"));
    }

    @Test
    public void testSize() {
        // Test getting the number of values in the record
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        // Test converting the values to a List
        List<String> expectedList = Arrays.asList("John", "Doe", "30");
        assertEquals(expectedList, record.toList());
    }

    @Test
    public void testToMap() {
        // Test converting the values to a Map
        Map<String, String> expectedMap = new MyMap();
        expectedMap.put("Name", "John");
        expectedMap.put("LastName", "Doe");
        expectedMap.put("Age", "30");
        assertEquals(expectedMap, record.toMap());
    }

    private enum MyEnum {
        NAME,
        LAST_NAME,
        AGE
    }

    private class MyMap extends HashMap<String, String> {
    }
}