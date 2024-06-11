import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    // Test for get(Enum<?> e) method

    @Test
    public void testGetEnum_typical() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.TEST1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnum_invalidEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.INVALID));
    }

    // Test for get(int i) method

    @Test
    public void testGetIndex_typical() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(0));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetIndex_invalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(5));
    }

    // Test for get(String name) method

    @Test
    public void testGetName_typical() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get("name1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetName_noHeaderMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get("name1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetName_invalidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get("invalidName"));
    }

    // Test for isConsistent() method

    @Test
    public void testIsConsistent_true() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_false() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertFalse(record.isConsistent());
    }

    // Test for isMapped(String name) method

    @Test
    public void testIsMapped_true() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(record.isMapped("name1"));
    }

    @Test
    public void testIsMapped_false() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isMapped("invalidName"));
    }

    // Test for isSet(String name) method

    @Test
    public void testIsSet_true() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(record.isSet("name1"));
    }

    @Test
    public void testIsSet_false() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isSet("name3"));
    }

    // Test for iterator() method

    @Test
    public void testIterator_typical() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        String[] expectedValues = {"value1", "value2", "value3"};
        int i = 0;
        for (String value : record) {
            assertEquals(expectedValues[i++], value);
        }
    }

    // Test for putIn(M map) method

    @Test
    public void testPutIn_typical() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        HashMap<String, String> map = new HashMap<>();
        Map<String, String> resultMap = record.putIn(map);
        assertEquals("value1", resultMap.get("name1"));
        assertEquals("value2", resultMap.get("name2"));
        assertEquals("value3", resultMap.get("name3"));
    }

    // Test for size() method

    @Test
    public void testSize_typical() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.size());
    }

    // Test for toList() method

    @Test
    public void testToList_typical() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        List<String> expectedList = Arrays.asList("value1", "value2", "value3");
        assertEquals(expectedList, record.toList());
    }

    // Test for toMap() method

    @Test
    public void testToMap_typical() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.toMap().get("name1"));
        assertEquals("value2", record.toMap().get("name2"));
        assertEquals("value3", record.toMap().get("name3"));
    }

    // Test for toString() method

    @Test
    public void testToString_typical() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }

    enum TestEnum {
        TEST1, TEST2, TEST3, INVALID
    }

}