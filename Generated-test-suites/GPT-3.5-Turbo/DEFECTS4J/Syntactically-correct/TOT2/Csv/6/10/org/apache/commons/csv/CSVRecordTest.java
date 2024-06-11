import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        // Test basic functionality
        CSVRecord record = createSampleCSVRecord();
        assertEquals("Value1", record.get(TestEnum.TEST1));
    }

    @Test
    public void testGetIndex() {
        // Test basic functionality
        CSVRecord record = createSampleCSVRecord();
        assertEquals("Value1", record.get(0));

        // Test edge case: Index out of bounds
        try {
            record.get(-1);
            fail("Index out of bounds exception not thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testGetName() {
        // Test basic functionality
        CSVRecord record = createSampleCSVRecord();
        assertEquals("Value1", record.get("Column1"));

        // Test edge case: Mapping for name not found
        try {
            record.get("NotExistingColumn");
            fail("IllegalArgumentException not thrown for non-existing name");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test edge case: Index for header out of bounds
        try {
            record.get("Column3");
            fail("IllegalArgumentException not thrown for out of bounds index");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test error scenario: No header mapping specified
        CSVRecord recordWithoutMapping = new CSVRecord(new String[]{"Value1", "Value2"}, null, "Sample Comment", 1);
        try {
            recordWithoutMapping.get("Column1");
            fail("IllegalStateException not thrown for missing mapping");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testIsConsistent() {
        // Test consistent record
        CSVRecord record = createSampleCSVRecord();
        assertTrue(record.isConsistent());

        // Test inconsistent record
        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"Value1", "Value2"}, new HashMap<>(), null, 1);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        // Test basic functionality
        CSVRecord record = createSampleCSVRecord();
        assertTrue(record.isMapped("Column1"));
        assertFalse(record.isMapped("NotExistingColumn"));
    }

    @Test
    public void testIsSet() {
        // Test basic functionality
        CSVRecord record = createSampleCSVRecord();
        assertTrue(record.isSet("Column1"));
        assertFalse(record.isSet("Column3"));
    }

    @Test
    public void testIterator() {
        // Test basic functionality
        CSVRecord record = createSampleCSVRecord();
        Iterator<String> iterator = record.iterator();
        assertEquals("Value1", iterator.next());
        assertEquals("Value2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        // Test basic functionality
        CSVRecord record = createSampleCSVRecord();
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("Value1", map.get("Column1"));
        assertEquals("Value2", map.get("Column2"));
    }

    @Test
    public void testSize() {
        // Test basic functionality
        CSVRecord record = createSampleCSVRecord();
        assertEquals(2, record.size());
    }

    @Test
    public void testToMap() {
        // Test basic functionality
        CSVRecord record = createSampleCSVRecord();
        Map<String, String> map = record.toMap();
        assertEquals("Value1", map.get("Column1"));
        assertEquals("Value2", map.get("Column2"));
    }

    private CSVRecord createSampleCSVRecord() {
        String[] values = {"Value1", "Value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        return new CSVRecord(values, mapping, "Sample Comment", 1);
    }
}