import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);

        String[] values = {"value1", "value2"};

        csvRecord = new CSVRecord(values, mapping, "Test comment", 1);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", csvRecord.get(CSVRecordTestEnum.COL1));
        assertEquals("value2", csvRecord.get(CSVRecordTestEnum.COL2));
    }

    @Test
    public void testGetIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetName() {
        assertEquals("value1", csvRecord.get("col1"));
        assertEquals("value2", csvRecord.get("col2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithoutMapping() {
        CSVRecord csvRecordWithoutMapping = new CSVRecord(new String[]{"value1", "value2"}, null, "Test comment", 1);
        csvRecordWithoutMapping.get("col1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameInvalid() {
        csvRecord.get("col3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameOutOfBounds() {
        csvRecord.get("col1");
    }

    @Test
    public void testGetComment() {
        assertEquals("Test comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());

        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("col1", 0);

        assertFalse(new CSVRecord(new String[]{"value1", "value2"}, inconsistentMapping, "Test comment", 1).isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("col1"));
        assertTrue(csvRecord.isMapped("col2"));
        assertFalse(csvRecord.isMapped("col3"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("col1"));
        assertTrue(csvRecord.isSet("col2"));
        assertTrue(csvRecord.isSet("col1"));
        assertFalse(csvRecord.isSet("col3"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        csvRecord.putIn(map);
        assertEquals(2, map.size());
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
    }

    @Test
    public void testSize() {
        assertEquals(2, csvRecord.size());
    }

    @Test
    public void testToList() {
        assertEquals(2, csvRecord.toList().size());
        assertEquals("value1", csvRecord.toList().get(0));
        assertEquals("value2", csvRecord.toList().get(1));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals(2, map.size());
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        assertEquals(2, csvRecord.values().length);
        assertEquals("value1", csvRecord.values()[0]);
        assertEquals("value2", csvRecord.values()[1]);
    }
}

enum CSVRecordTestEnum {
    COL1, COL2;
}