import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;

    @Before
    public void setUp() {
        String[] values = { "value1", "value2", "value3" };
        mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        csvRecord = new CSVRecord(values, mapping, "Test comment", 1L);
    }

    @Test
    public void testGetValueByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetValueByName() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[] { "value" }, null, null, 1L);
        record.get("column");
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertFalse(csvRecord.isMapped("unknownColumn"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertFalse(csvRecord.isSet("column3"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testGetComment() {
        assertEquals("Test comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1L, csvRecord.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }
}