import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, null, 1);

        String result = record.get(TestEnum.TEST1);

        assertEquals("value1", result);
    }

    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, null, 1);

        String result = record.get(1);

        assertEquals("value2", result);
    }

    @Test
    public void testGetName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                createMapping(), null, 1);

        String result = record.get("field2");

        assertEquals("value2", result);
    }

    @Test
    public void testGetNameWithNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, null, 1);

        try {
            record.get("field2");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetNameWithInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                createMapping(), null, 1);

        try {
            record.get("field4");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, "This is a comment", 1);

        String result = record.getComment();

        assertEquals("This is a comment", result);
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, null, 10);

        long result = record.getRecordNumber();

        assertEquals(10, result);
    }

    @Test
    public void testIsConsistentWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                createMapping(), null, 1);

        boolean result = record.isConsistent();

        assertTrue(result);
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, null, 1);

        boolean result = record.isConsistent();

        assertTrue(result);
    }

    @Test
    public void testIsConsistentWithInvalidLength() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"},
                createMapping(), null, 1);

        boolean result = record.isConsistent();

        assertFalse(result);
    }

    @Test
    public void testIsMappedWithValidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                createMapping(), null, 1);

        boolean result = record.isMapped("field1");

        assertTrue(result);
    }

    @Test
    public void testIsMappedWithInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                createMapping(), null, 1);

        boolean result = record.isMapped("field4");

        assertFalse(result);
    }

    @Test
    public void testIsSetWithValidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                createMapping(), null, 1);

        boolean result = record.isSet("field1");

        assertTrue(result);
    }

    @Test
    public void testIsSetWithInValidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                createMapping(), null, 1);

        boolean result = record.isSet("field4");

        assertFalse(result);
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, null, 1);

        String[] expectedValues = {"value1", "value2", "value3"};
        int index = 0;

        for (String value : record) {
            assertEquals(expectedValues[index++], value);
        }
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                createMapping(), null, 1);

        Map<String, String> map = record.putIn(new HashMap<>());

        assertEquals(3, map.size());
        assertEquals("value1", map.get("field1"));
        assertEquals("value2", map.get("field2"));
        assertEquals("value3", map.get("field3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, null, 1);

        int result = record.size();

        assertEquals(3, result);
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                createMapping(), null, 1);

        Map<String, String> map = record.toMap();

        assertEquals(3, map.size());
        assertEquals("value1", map.get("field1"));
        assertEquals("value2", map.get("field2"));
        assertEquals("value3", map.get("field3"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, null, 1);

        String result = record.toString();

        assertEquals("[value1, value2, value3]", result);
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("field1", 0);
        mapping.put("field2", 1);
        mapping.put("field3", 2);
        return mapping;
    }

    private enum TestEnum {
        TEST1,
        TEST2,
        TEST3
    }
}