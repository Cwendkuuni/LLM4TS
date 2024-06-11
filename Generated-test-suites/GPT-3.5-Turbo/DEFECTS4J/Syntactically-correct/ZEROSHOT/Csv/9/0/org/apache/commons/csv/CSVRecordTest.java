import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private static CSVRecord csvRecord;
    private static String[] values;
    private static Map<String, Integer> mapping;
    private static String comment;
    private static long recordNumber;

    @BeforeClass
    public static void setup() {
        values = new String[]{"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        comment = "This is a comment";
        recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        String expected = "value1";
        String actual = csvRecord.get(TestEnum.COLUMN1);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetByIndex() {
        String expected = "value2";
        String actual = csvRecord.get(1);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetByName() {
        String expected = "value3";
        String actual = csvRecord.get("column3");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithNoMapping() {
        csvRecord = new CSVRecord(values, null, comment, recordNumber);
        csvRecord.get("column3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        csvRecord.get("column4");
    }

    @Test
    public void testGetComment() {
        String expected = "This is a comment";
        String actual = csvRecord.getComment();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRecordNumber() {
        long expected = 1;
        long actual = csvRecord.getRecordNumber();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsConsistent() {
        boolean expected = true;
        boolean actual = csvRecord.isConsistent();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsMapped() {
        boolean expected = true;
        boolean actual = csvRecord.isMapped("column1");
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSet() {
        boolean expected = true;
        boolean actual = csvRecord.isSet("column1");
        assertEquals(expected, actual);
    }

    @Test
    public void testIterator() {
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
        Map<String, String> map = new HashMap<>();
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("column1", "value1");
        expectedMap.put("column2", "value2");
        expectedMap.put("column3", "value3");
        csvRecord.putIn(map);
        assertEquals(expectedMap, map);
    }

    @Test
    public void testSize() {
        int expected = 3;
        int actual = csvRecord.size();
        assertEquals(expected, actual);
    }

    @Test
    public void testToMap() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("column1", "value1");
        expectedMap.put("column2", "value2");
        expectedMap.put("column3", "value3");
        Map<String, String> actualMap = csvRecord.toMap();
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testToString() {
        String expected = "[value1, value2, value3]";
        String actual = csvRecord.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testValues() {
        String[] expected = {"value1", "value2", "value3"};
        String[] actual = csvRecord.values();
        assertArrayEquals(expected, actual);
    }
}

public enum TestEnum {
    COLUMN1,
    COLUMN2,
    COLUMN3
}