import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 2);
        mapping.put("col3", 1);
        String[] values = {"value1", "value2", "value3"};
        csvRecord = new CSVRecord(values, mapping, "This is a comment", 1);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", csvRecord.get(Enum1.ENUM1));
        assertEquals("value2", csvRecord.get(Enum2.ENUM2));
        assertEquals("value3", csvRecord.get(Enum3.ENUM3));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("col1"));
        assertEquals("value2", csvRecord.get("col3"));
        assertEquals("value3", csvRecord.get("col2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutHeaderMapping() {
        csvRecord = new CSVRecord(new String[3], null, "This is a comment", 1);
        csvRecord.get("col1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidName() {
        csvRecord.get("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidIndex() {
        csvRecord.get(3);
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());

        csvRecord = new CSVRecord(new String[2], null, "This is a comment", 1);
        assertFalse(csvRecord.isConsistent());

        csvRecord = new CSVRecord(new String[4], null, "This is a comment", 1);
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("col1"));
        assertFalse(csvRecord.isMapped("col4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("col1"));
        assertTrue(csvRecord.isSet("col3"));
        assertFalse(csvRecord.isSet("col2"));
        assertFalse(csvRecord.isSet("col4"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            assertEquals("value" + (count + 1), value);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = csvRecord.putIn(new HashMap<String, String>());
        assertTrue(map.containsKey("col1"));
        assertTrue(map.containsKey("col2"));
        assertTrue(map.containsKey("col3"));
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col3"));
        assertEquals("value3", map.get("col2"));
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList() {
        List<String> values = csvRecord.toList();
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();

        assertTrue(map.containsKey("col1"));
        assertTrue(map.containsKey("col2"));
        assertTrue(map.containsKey("col3"));
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col3"));
        assertEquals("value3", map.get("col2"));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        String[] values = csvRecord.values();
        assertEquals(3, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }

    enum Enum1 {
        ENUM1
    }

    enum Enum2 {
        ENUM2
    }

    enum Enum3 {
        ENUM3
    }

}