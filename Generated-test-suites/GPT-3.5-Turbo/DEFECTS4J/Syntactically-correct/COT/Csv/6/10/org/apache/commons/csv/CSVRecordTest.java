import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setup() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        csvRecord = new CSVRecord(values, mapping, "comment", 1);
    }

    @Test
    public void testGetByEnum() {
        String value = csvRecord.get(TestEnum.TEST);
        assertEquals("value1", value);
    }

    @Test
    public void testGetByIndex() {
        String value = csvRecord.get(0);
        assertEquals("value1", value);
    }

    @Test
    public void testGetByName() {
        String value = csvRecord.get("column1");
        assertEquals("value1", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, null, null, 1);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        csvRecord.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidIndex() {
        csvRecord.get(3);
    }

    @Test
    public void testGetComment() {
        String comment = csvRecord.getComment();
        assertEquals("comment", comment);
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = csvRecord.getRecordNumber();
        assertEquals(1, recordNumber);
    }

    @Test
    public void testIsConsistentWithMapping() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
    }

    @Test
    public void testIsNotMapped() {
        assertFalse(csvRecord.isMapped("column4"));
    }

    @Test
    public void testIsSetWithValidColumn() {
        assertTrue(csvRecord.isSet("column1"));
    }

    @Test
    public void testIsSetWithInvalidColumn() {
        assertFalse(csvRecord.isSet("column4"));
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
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        csvRecord.putIn(map);
        assertEquals("value1", map.get("column1"));
    }

    @Test
    public void testSize() {
        int size = csvRecord.size();
        assertEquals(3, size);
    }

    @Test
    public void testToList() {
        List<String> list = csvRecord.toList();
        assertEquals("value1", list.get(0));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals("value1", map.get("column1"));
    }

    @Test
    public void testToString() {
        String str = csvRecord.toString();
        assertEquals("[value1, value2, value3]", str);
    }

    private enum TestEnum {
        TEST
    }
}