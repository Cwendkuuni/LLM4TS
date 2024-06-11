import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {
    @Test
    public void testGetEnum() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals("value1", csvRecord.get(SomeEnum.VALUE1));
        assertEquals("value2", csvRecord.get(SomeEnum.VALUE2));
    }

    @Test
    public void testGetInt() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetString() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 0);
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetStringNoMapping() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        csvRecord.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringMappingNotFound() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 0);
        csvRecord.get("column3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1"}, mapping, null, 0);
        csvRecord.get("column1");
    }

    @Test
    public void testGetComment() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, null, "comment", 0);
        assertEquals("comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 5);
        assertEquals(5, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals(true, csvRecord.isConsistent());

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord inconsistentRecord = new CSVRecord(new String[] {"value1"}, mapping, null, 0);
        assertEquals(false, inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals(false, csvRecord.isMapped("column1"));

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord mappedRecord = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 0);
        assertEquals(true, mappedRecord.isMapped("column1"));
        assertEquals(false, mappedRecord.isMapped("column2"));
    }

    @Test
    public void testIsSet() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals(false, csvRecord.isSet("column1"));

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord setRecord = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 0);
        assertEquals(false, setRecord.isSet("column2"));
        assertEquals(true, setRecord.isSet("column1"));
    }

    @Test
    public void testIterator() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals("value1", csvRecord.iterator().next());
    }

    @Test
    public void testSize() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals(2, csvRecord.size());
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 0);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("column1", "value1");
        expectedMap.put("column2", "value2");
        assertEquals(expectedMap, csvRecord.toMap());
    }

    @Test
    public void testToString() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals("[value1, value2]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals(2, csvRecord.values().length);
        assertEquals("value1", csvRecord.values()[0]);
        assertEquals("value2", csvRecord.values()[1]);
    }
}

enum SomeEnum {
    VALUE1, VALUE2
}