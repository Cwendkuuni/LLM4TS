import static org.junit.Assert.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord createRecord(String[] values, int recordNumber) {
        return new CSVRecord(values, null, null, recordNumber);
    }

    @Test
    public void get_WithEnum_ReturnsValue() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = createRecord(values, 1);

        String result = record.get(TestEnum.TEST_VALUE1);

        assertEquals("Value1", result);
    }

    @Test
    public void get_WithIndex_ReturnsValue() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = createRecord(values, 1);

        String result = record.get(1);

        assertEquals("Value2", result);
    }

    @Test(expected = IllegalStateException.class)
    public void get_WithNoMapping_ThrowsException() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_WithInvalidName_ThrowsException() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = createRecord(values, 1);

        record.get("InvalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_WithInvalidIndex_ThrowsException() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = createRecord(values, 1);

        record.get(3);
    }

    @Test
    public void getComment_WithComment_ReturnsComment() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment", 1);

        String result = record.getComment();

        assertEquals("This is a comment", result);
    }

    @Test
    public void getComment_WithoutComment_ReturnsNull() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = createRecord(values, 1);

        String result = record.getComment();

        assertNull(result);
    }

    @Test
    public void getRecordNumber_ReturnsRecordNumber() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = createRecord(values, 5);

        long result = record.getRecordNumber();

        assertEquals(5, result);
    }

    @Test
    public void isConsistent_WithMapping_ReturnsTrue() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, createMapping(), null, 1);

        boolean result = record.isConsistent();

        assertTrue(result);
    }

    @Test
    public void isConsistent_WithNoMapping_ReturnsTrue() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = createRecord(values, 1);

        boolean result = record.isConsistent();

        assertTrue(result);
    }

    @Test
    public void isConsistent_WithInvalidMapping_ReturnsFalse() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, createInvalidMapping(), null, 1);

        boolean result = record.isConsistent();

        assertFalse(result);
    }

    @Test
    public void isMapped_WithMappedName_ReturnsTrue() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, createMapping(), null, 1);

        boolean result = record.isMapped("Name");

        assertTrue(result);
    }

    @Test
    public void isMapped_WithUnmappedName_ReturnsFalse() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = createRecord(values, 1);

        boolean result = record.isMapped("Name");

        assertFalse(result);
    }

    @Test
    public void isSet_WithValue_ReturnsTrue() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, createMapping(), null, 1);

        boolean result = record.isSet("Name");

        assertTrue(result);
    }

    @Test
    public void isSet_WithEmptyValue_ReturnsFalse() {
        String[] values = {"", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, createMapping(), null, 1);

        boolean result = record.isSet("Name");

        assertFalse(result);
    }

    @Test
    public void iterator_ReturnsIterator() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = createRecord(values, 1);

        Iterable<String> iterable = record;
        Iterator<String> iterator = iterable.iterator();

        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("Value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Value2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Value3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void putIn_WithMapping_PopulatesMap() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, createMapping(), null, 1);

        Map<String, String> map = record.putIn(new HashMap<>());

        assertEquals(3, map.size());
        assertEquals("Value1", map.get("Name1"));
        assertEquals("Value2", map.get("Name2"));
        assertEquals("Value3", map.get("Name3"));
    }

    @Test
    public void putIn_WithNoMapping_PopulatesEmptyMap() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = createRecord(values, 1);

        Map<String, String> map = record.putIn(new HashMap<>());

        assertTrue(map.isEmpty());
    }

    @Test
    public void size_ReturnsSize() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = createRecord(values, 1);

        int result = record.size();

        assertEquals(3, result);
    }

    @Test
    public void toMap_PopulatesMap() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = createRecord(values, 1);

        Map<String, String> map = record.toMap();

        assertEquals(3, map.size());
        assertEquals("Value1", map.get("0"));
        assertEquals("Value2", map.get("1"));
        assertEquals("Value3", map.get("2"));
    }

    @Test
    public void values_ReturnsValuesArray() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = createRecord(values, 1);

        String[] result = record.values();

        assertArrayEquals(values, result);
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name1", 0);
        mapping.put("Name2", 1);
        mapping.put("Name3", 2);
        return mapping;
    }

    private Map<String, Integer> createInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name1", 0);
        mapping.put("Name2", 1);
        return mapping;
    }

    private enum TestEnum {
        TEST_VALUE1
    }

}