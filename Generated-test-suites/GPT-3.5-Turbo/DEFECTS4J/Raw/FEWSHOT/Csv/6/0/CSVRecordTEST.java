import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = createRecord("", null);
        Enum<?> e = null;
        assertNull(record.get(e));

        e = MyEnum.VALUE1;
        assertNull(record.get(e));

        e = MyEnum.VALUE2;
        assertNull(record.get(e));

        record = createRecord("value1,value2", null);
        e = MyEnum.VALUE1;
        assertEquals("value1", record.get(e));

        e = MyEnum.VALUE2;
        assertEquals("value2", record.get(e));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = createEmptyRecord();
        int index = 0;
        assertNull(record.get(index));

        record = createRecord("value1,value2", null);
        index = 0;
        assertEquals("value1", record.get(index));

        index = 1;
        assertEquals("value2", record.get(index));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = createEmptyRecord();
        String name = null;
        try {
            record.get(name);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        record = createRecord("value1,value2", null);
        name = null;
        try {
            record.get(name);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        name = "invalidName";
        try {
            record.get(name);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        name = "value1";
        assertEquals("value1", record.get(name));

        name = "value2";
        assertEquals("value2", record.get(name));
    }

    @Test
    public void testGetComment() {
        CSVRecord record = createRecord("", null);
        assertNull(record.getComment());

        record = createRecord("", "comment");
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = createRecord("", null);
        assertEquals(0, record.getRecordNumber());

        record = createRecord("", null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = createEmptyRecord();
        assertTrue(record.isConsistent());

        record = createRecord("value1,value2", null);
        assertTrue(record.isConsistent());

        record = createRecord("value1,value2,value3", null);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = createEmptyRecord();
        String name = null;
        assertFalse(record.isMapped(name));

        name = "invalidName";
        assertFalse(record.isMapped(name));

        record = createRecord("value1,value2", null);
        name = null;
        assertFalse(record.isMapped(name));

        name = "invalidName";
        assertFalse(record.isMapped(name));

        name = "value1";
        assertTrue(record.isMapped(name));

        name = "value2";
        assertTrue(record.isMapped(name));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = createEmptyRecord();
        String name = null;
        assertFalse(record.isSet(name));

        name = "invalidName";
        assertFalse(record.isSet(name));

        record = createRecord("value1,value2", null);
        name = null;
        assertFalse(record.isSet(name));

        name = "invalidName";
        assertFalse(record.isSet(name));

        name = "value1";
        assertTrue(record.isSet(name));

        name = "value2";
        assertTrue(record.isSet(name));

        record = createRecord("value1", null);
        name = "value1";
        assertTrue(record.isSet(name));

        name = "value2";
        assertFalse(record.isSet(name));
    }

    @Test
    public void testPutIn() {
        CSVRecord record = createEmptyRecord();
        Map<String, String> map = new HashMap<>();
        map = record.putIn(map);
        assertTrue(map.isEmpty());

        record = createRecord("value1,value2", null);
        map = record.putIn(map);
        assertTrue(map.containsKey("value1"));
        assertEquals("value1", map.get("value1"));
        assertTrue(map.containsKey("value2"));
        assertEquals("value2", map.get("value2"));
    }

    @Test
    public void testSize() {
        CSVRecord record = createEmptyRecord();
        assertEquals(0, record.size());

        record = createRecord("value1,value2", null);
        assertEquals(2, record.size());

        record = createRecord("value1,value2,value3", null);
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = createEmptyRecord();
        List<String> list = record.toList();
        assertTrue(list.isEmpty());

        record = createRecord("value1,value2", null);
        list = record.toList();
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));

        record = createRecord("value1,value2,value3", null);
        list = record.toList();
        assertEquals(3, list.size());
        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));
        assertEquals("value3", list.get(2));
    }

    @Test
    public void testToMap() {
        CSVRecord record = createEmptyRecord();
        Map<String, String> map = record.toMap();
        assertTrue(map.isEmpty());

        record = createRecord("value1,value2", null);
        map = record.toMap();
        assertTrue(map.containsKey("value1"));
        assertEquals("value1", map.get("value1"));
        assertTrue(map.containsKey("value2"));
        assertEquals("value2", map.get("value2"));
    }

    private CSVRecord createEmptyRecord() {
        return new CSVRecord(null, null, null, 0);
    }

    private CSVRecord createRecord(String values, String comment) {
        String[] valuesArray = values.split(",");
        Map<String, Integer> mapping = new HashMap<>();
        for (int i = 0; i < valuesArray.length; i++) {
            mapping.put("value" + (i + 1), i);
        }
        return new CSVRecord(valuesArray, mapping, comment, 0);
    }

    private CSVRecord createRecord(String values, String comment, long recordNumber) {
        String[] valuesArray = values.split(",");
        Map<String, Integer> mapping = new HashMap<>();
        for (int i = 0; i < valuesArray.length; i++) {
            mapping.put("value" + (i + 1), i);
        }
        return new CSVRecord(valuesArray, mapping, comment, recordNumber);
    }

    private enum MyEnum {
        VALUE1, VALUE2
    }
}