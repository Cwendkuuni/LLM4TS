import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Test
    public void testGetEnum() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", csvRecord.get(SomeEnum.VALUE_1));
    }

    @Test
    public void testGetInt() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", csvRecord.get(0));
    }

    @Test
    public void testGetString() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", csvRecord.get("column1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetString_noMapping() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        csvRecord.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetString_mappingNotFound() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        csvRecord.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetString_indexOutOfBounds() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        csvRecord.get("column3");
    }

    @Test
    public void testGetComment() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "This is a comment", 1);
        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 10);
        assertEquals(10, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(csvRecord.isConsistent());

        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(csvRecord.isMapped("column1"));

        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<String, Integer>() {{
            put("column1", 0);
        }}, null, 1);
        assertTrue(csvRecord.isMapped("column1"));
        assertFalse(csvRecord.isMapped("column2"));
    }

    @Test
    public void testIsSet() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(csvRecord.isSet("column1"));

        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<String, Integer>() {{
            put("column1", 0);
        }}, null, 1);
        assertTrue(csvRecord.isSet("column1"));
        assertFalse(csvRecord.isSet("column2"));
    }

    @Test
    public void testIterator() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Iterator<String> iterator = csvRecord.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<String, Integer>() {{
            put("column1", 0);
            put("column2", 1);
            put("column3", 2);
        }}, null, 1);
        Map<String, String> map = new HashMap<>();
        csvRecord.putIn(map);
        assertEquals(3, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToMap() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<String, Integer>() {{
            put("column1", 0);
            put("column2", 1);
            put("column3", 2);
        }}, null, 1);
        Map<String, String> map = csvRecord.toMap();
        assertEquals(3, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testToString() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, csvRecord.values());
    }
}