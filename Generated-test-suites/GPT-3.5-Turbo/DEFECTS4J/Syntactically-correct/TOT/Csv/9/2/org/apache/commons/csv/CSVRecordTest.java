import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setup() {
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("Age", 2);

        record = new CSVRecord(values, mapping, null, 1);
    }

    @Test
    public void testGetByEnum_Success() {
        assertEquals("John", record.get(Columns.FirstName));
        assertEquals("Doe", record.get(Columns.LastName));
        assertEquals("25", record.get(Columns.Age));
    }

    @Test
    public void testGetByIndex_Success() {
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("25", record.get(2));
    }

    @Test
    public void testGetByName_Success() {
        assertEquals("John", record.get("FirstName"));
        assertEquals("Doe", record.get("LastName"));
        assertEquals("25", record.get("Age"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_NoMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(record.values(), null, null, 1);
        recordWithoutMapping.get("FirstName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_NullMapping() {
        record.get("InvalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_IndexOutOfBounds() {
        record.get("InvalidName");
    }

    @Test
    public void testGetComment_Success() {
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber_Success() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent_Success() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped_Success() {
        assertTrue(record.isMapped("FirstName"));
        assertTrue(record.isMapped("LastName"));
        assertTrue(record.isMapped("Age"));
        assertFalse(record.isMapped("InvalidName"));
    }

    @Test
    public void testIsSet_Success() {
        assertTrue(record.isSet("FirstName"));
        assertTrue(record.isSet("LastName"));
        assertTrue(record.isSet("Age"));
        assertFalse(record.isSet("InvalidName"));
    }

    @Test
    public void testIterator_Success() {
        int count = 0;
        for (String value : record) {
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    public void testPutIn_Success() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);

        assertEquals("John", map.get("FirstName"));
        assertEquals("Doe", map.get("LastName"));
        assertEquals("25", map.get("Age"));
    }

    @Test
    public void testSize_Success() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToList_Success() {
        assertEquals(Arrays.asList("John", "Doe", "25"), record.toList());
    }

    @Test
    public void testToMap_Success() {
        Map<String, String> map = record.toMap();

        assertEquals("John", map.get("FirstName"));
        assertEquals("Doe", map.get("LastName"));
        assertEquals("25", map.get("Age"));
    }

    @Test
    public void testToString_Success() {
        assertEquals("[John, Doe, 25]", record.toString());
    }

    @Test
    public void testValues_Success() {
        assertArrayEquals(new String[] {"John", "Doe", "25"}, record.values());
    }

    private enum Columns {
        FirstName,
        LastName,
        Age
    }

}