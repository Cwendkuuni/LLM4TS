import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class CSVRecordTest {

    // Test data
    private final String[] values = {"value1", "value2", "value3"};
    private final Map<String, Integer> mapping = new HashMap<>();
    private final String comment = "This is a comment";
    private final long recordNumber = 1;

    // Test cases
    @Test
    public void testGetByEnum() {
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        Enum<?> testEnum = new Enum<>() {
            @Override
            public String toString() {
                return "testEnum";
            }
        };

        assertEquals("value1", csvRecord.get(testEnum));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("value1", csvRecord.get(0));
    }

    @Test
    public void testGetByNameValid() {
        mapping.put("name1", 0);
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("value1", csvRecord.get("name1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord csvRecord = new CSVRecord(values, null, comment, recordNumber);

        csvRecord.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        mapping.put("name1", 0);
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        csvRecord.get("name2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameIndexOutOfBounds() {
        mapping.put("name1", 5);
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        csvRecord.get("name1");
    }

    @Test
    public void testGetComment() {
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistentTrue() {
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentFalse() {
        mapping.put("name1", 0);
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMappedTrue() {
        mapping.put("name1", 0);
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(csvRecord.isMapped("name1"));
    }

    @Test
    public void testIsMappedFalse() {
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertFalse(csvRecord.isMapped("name1"));
    }

    @Test
    public void testIsSetTrue() {
        mapping.put("name1", 0);
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(csvRecord.isSet("name1"));
    }

    @Test
    public void testIsSetFalse() {
        mapping.put("name1", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertFalse(csvRecord.isSet("name1"));
    }

    @Test
    public void testIterator() {
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        Iterator<String> iterator = csvRecord.iterator();

        while (iterator.hasNext()) {
            assertEquals(values[(int) csvRecord.getRecordNumber() - 1], iterator.next());
        }
    }

    @Test
    public void testPutIn() {
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> map = new HashMap<>();
        map.put("name1", "value1");

        assertEquals(map, csvRecord.putIn(new HashMap<String, String>()));
    }

    @Test
    public void testSize() {
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToMap() {
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Map<String, String> map = new HashMap<>();
        map.put("name1", "value1");
        map.put("name2", "value2");
        map.put("name3", "value3");

        assertEquals(map, csvRecord.toMap());
    }

    @Test
    public void testToString() {
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertArrayEquals(values, csvRecord.values());
    }

}