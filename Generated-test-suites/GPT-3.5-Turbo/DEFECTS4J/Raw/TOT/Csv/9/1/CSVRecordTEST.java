import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.csv.CSVFormat;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @Before
    public void setUp() {
        values = new String[]{"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        comment = "This is a comment";
        recordNumber = 1;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", record.get(Enum.VAL1));
        assertEquals("value2", record.get(Enum.VAL2));
        assertEquals("value3", record.get(Enum.VAL3));
    }

    @Test
    public void testGetIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetName() {
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }

    @Test
    public void testGetInvalidName() {
        try {
            record.get("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for invalid not found, expected one of [column1, column2, column3]", e.getMessage());
        }
    }

    @Test
    public void testGetNoMapping() {
        CSVRecord recordNoMapping = new CSVRecord(values, null, comment, recordNumber);
        
        try {
            recordNoMapping.get("column1");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("No header mapping was specified, the record values can't be accessed by name", e.getMessage());
        }
    }

    @Test
    public void testGetInvalidIndex() {
        try {
            record.get(3);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("Index for header '3' is 3 but CSVRecord only has 3 values!", e.getMessage());
        }
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithNoMapping() {
        CSVRecord recordNoMapping = new CSVRecord(values, null, comment, recordNumber);
        assertTrue(recordNoMapping.isConsistent());
    }

    @Test
    public void testIsConsistentWithInvalidMapping() {
        Map<String, Integer> invalidMapping = new HashMap<>();
        invalidMapping.put("column1", 0);
        invalidMapping.put("column2", 1);
        
        CSVRecord recordInvalidMapping = new CSVRecord(values, invalidMapping, comment, recordNumber);
        assertFalse(recordInvalidMapping.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
    }

    @Test
    public void testIsNotMapped() {
        assertFalse(record.isMapped("invalid"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
    }

    @Test
    public void testIsNotSet() {
        assertFalse(record.isSet("invalid"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertTrue(iterator.hasNext());
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
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        assertArrayEquals(values, record.values());
    }

    enum Enum {
        VAL1, VAL2, VAL3
    }
}