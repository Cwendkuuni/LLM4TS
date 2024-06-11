import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"Value1"}, null, null, 1);
        assertEquals("Value1", record.get(TestEnum.TEST1));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"Value1"}, null, null, 1);
        assertEquals("Value1", record.get(0));
    }

    @Test
    public void testGetByNameWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, createMapping(), null, 1);
        assertEquals("Value1", record.get("Column1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Value1"}, null, null, 1);
        record.get("Column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Value1"}, createMapping(), null, 1);
        record.get("Column3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"Value1"}, createMapping(), null, 1);
        record.get("Column2");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"Value1"}, null, "Comment", 1);
        assertEquals("Comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"Value1"}, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistentWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, createMapping(), null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithInvalidMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Value1"}, createMapping(), null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMappedWithValidName() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, createMapping(), null, 1);
        assertTrue(record.isMapped("Column1"));
    }

    @Test
    public void testIsMappedWithInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, createMapping(), null, 1);
        assertFalse(record.isMapped("Column3"));
    }

    @Test
    public void testIsSetWithValidName() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, createMapping(), null, 1);
        assertTrue(record.isSet("Column1"));
    }

    @Test
    public void testIsSetWithInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, createMapping(), null, 1);
        assertFalse(record.isSet("Column3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, createMapping(), null, 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, createMapping(), null, 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, createMapping(), null, 1);
        assertEquals(Arrays.asList("Value1", "Value2"), record.toList());
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, createMapping(), null, 1);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Column1", "Value1");
        expectedMap.put("Column2", "Value2");
        assertEquals(expectedMap, record.toMap());
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        return mapping;
    }

    // Additional test cases for edge cases and error scenarios can be added here

}