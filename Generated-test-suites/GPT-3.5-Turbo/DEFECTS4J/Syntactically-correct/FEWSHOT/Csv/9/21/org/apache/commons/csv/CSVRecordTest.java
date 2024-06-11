import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.csv.CSVFormat;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {
    
    private static final String[] VALUES = {"value1", "value2", "value3"};
    private static final Map<String, Integer> MAPPING = new HashMap<>();
    private static final String COMMENT = "This is a comment";
    private static final long RECORD_NUMBER = 1;

    private CSVRecord record;

    @Before
    public void setUp() {
        MAPPING.put("column1", 0);
        MAPPING.put("column2", 1);
        MAPPING.put("column3", 2);
        
        record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        recordWithoutMapping.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidName() {
        record.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        record.get("column1");
    }

    @Test
    public void testGetComment() {
        assertEquals(COMMENT, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(RECORD_NUMBER, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentNoMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertTrue(recordWithoutMapping.isConsistent());
    }

    @Test
    public void testIsConsistentIncorrectSize() {
        MAPPING.put("column4", 3);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
        assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
        assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        java.util.Iterator<String> iterator = record.iterator();
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);

        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
        assertNull(map.get("column4"));
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        java.util.List<String> list = record.toList();

        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));
        assertEquals("value3", list.get(2));
        
        assertEquals(3, list.size());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();

        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
        assertNull(map.get("column4"));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        String[] values = record.values();

        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
        
        assertEquals(3, values.length);
    }

}