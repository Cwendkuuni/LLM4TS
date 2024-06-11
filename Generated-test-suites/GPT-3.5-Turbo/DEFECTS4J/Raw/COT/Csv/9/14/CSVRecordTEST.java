import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);

        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);

        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"header1", "header2", "header3"}, createMapping(), null, 1);

        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
        assertEquals("value3", record.get("header3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"header1", "header2", "header3"}, null, null, 1);

        record.get("header1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"header1", "header2", "header3"}, createMapping(), null, 1);

        record.get("header4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"header1", "header2", "header3"}, createMapping(), null, 1);

        record.get("header1");
        record.get("header2");
        record.get("header3");
        record.get("header4");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "This is a comment", 1);

        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 5);

        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 2);

        assertTrue(record1.isConsistent());
        assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"header1", "header2", "header3"}, createMapping(), null, 1);

        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));
        assertFalse(record.isMapped("header4"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"header1", "header2", "header3"}, createMapping(), null, 1);

        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));
        assertFalse(record.isSet("header4"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);

        int i = 0;
        for (String value : record) {
            assertEquals(record.get(i), value);
            i++;
        }
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"header1", "header2", "header3"}, createMapping(), null, 1);
        Map<String, String> map = new HashMap<>();

        map = record.putIn(map);

        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
        assertEquals("value3", map.get("header3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);

        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);

        assertEquals(Arrays.asList("value1", "value2", "value3"), record.toList());
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"header1", "header2", "header3"}, createMapping(), null, 1);

        Map<String, String> map = record.toMap();

        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
        assertEquals("value3", map.get("header3"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);

        assertEquals("[value1, value2, value3]", record.toString());
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        return mapping;
    }

    private enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }
}