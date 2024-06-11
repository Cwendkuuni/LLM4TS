
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setup() {
        String[] values = new String[] { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        record = new CSVRecord(values, mapping, "comment", 1);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByInvalidIndex() {
        record.get(3);
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }

    @Test
    public void testGetByNonExistentName() {
        assertNull(record.get("nonExistentColumn"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(record.values(), null, record.getComment(), record.getRecordNumber());
        recordWithoutMapping.get("column1");
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        String[] invalidValues = new String[] { "value1" };
        CSVRecord invalidRecord = new CSVRecord(invalidValues, record.mapping, record.getComment(), record.getRecordNumber());
        assertTrue(!invalidRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("column1"));
    }

    @Test
    public void testIsNotMapped() {
        assertTrue(!record.isMapped("nonExistentColumn"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("column1"));
    }

    @Test
    public void testIsNotSet() {
        assertTrue(!record.isSet("nonExistentColumn"));
        assertTrue(!record.isSet("column3"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : record) {
            assertEquals(record.get(count), value);
            count++;
        }
    }

    @Test
    public void testValues() {
        String[] values = record.values();
        for (int i = 0; i < values.length; i++) {
            assertEquals(record.get(i), values[i]);
        }
    }

    @Test
    public void testGetComment() {
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }

}
