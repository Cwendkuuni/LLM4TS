import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord record;

    private static final String[] VALUES = {"value1", "value2", "value3"};
    private static final long RECORD_NUMBER = 1;

    @Before
    public void setUp() {
        record = new CSVRecord(VALUES, null, null, RECORD_NUMBER);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", record.get(EnumTest.FIRST));
        assertEquals("value2", record.get(EnumTest.SECOND));
        assertEquals("value3", record.get(EnumTest.THIRD));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        assertNull(record.get("nonexistent"));
        assertNull(record.get(null));
        assertNull(record.get(""));
        assertNull(record.get(" "));

        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(VALUES, null, null, RECORD_NUMBER);
        recordWithoutMapping.get("name1"); // Expecting IllegalStateException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidMapping() {
        CSVRecord recordWithInvalidMapping = new CSVRecord(VALUES, createInvalidMapping(), null, RECORD_NUMBER);
        recordWithInvalidMapping.get("name1"); // Expecting IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        CSVRecord recordWithInvalidIndex = new CSVRecord(VALUES, createMapping(), null, RECORD_NUMBER);
        recordWithInvalidIndex.get("name1"); // Expecting IllegalArgumentException
    }

    @Test
    public void testGetComment() {
        assertNull(record.getComment());

        CSVRecord recordWithComment = new CSVRecord(VALUES, null, "This is a comment", RECORD_NUMBER);
        assertEquals("This is a comment", recordWithComment.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(RECORD_NUMBER, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());

        CSVRecord inconsistentRecord = new CSVRecord(null, createMapping(), null, RECORD_NUMBER);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertFalse(record.isMapped("nonexistent"));
        assertFalse(record.isMapped(null));
        assertFalse(record.isMapped(""));
        assertFalse(record.isMapped(" "));

        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        assertFalse(record.isSet("nonexistent"));
        assertFalse(record.isSet(null));
        assertFalse(record.isSet(""));
        assertFalse(record.isSet(" "));

        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : record) {
            assertEquals(VALUES[count], value);
            count++;
        }
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToMap() {
        assertTrue(record.toMap().isEmpty());

        assertEquals("value1", recordWithMapping.toMap().get("name1"));
        assertEquals("value2", recordWithMapping.toMap().get("name2"));
        assertEquals("value3", recordWithMapping.toMap().get("name3"));
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        return mapping;
    }

    private Map<String, Integer> createInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        return mapping;
    }
}

enum EnumTest {
    FIRST,
    SECOND,
    THIRD
}