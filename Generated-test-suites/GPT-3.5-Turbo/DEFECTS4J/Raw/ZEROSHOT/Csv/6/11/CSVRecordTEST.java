import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @Before
    public void setUp() {
        values = new String[]{"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        comment = "This is a comment";
        recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.ENUM1));
        assertEquals("value2", csvRecord.get(TestEnum.ENUM2));
        assertEquals("value3", csvRecord.get(TestEnum.ENUM3));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("col1"));
        assertEquals("value2", csvRecord.get("col2"));
        assertEquals("value3", csvRecord.get("col3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_NoMapping() {
        CSVRecord csvRecordNoMapping = new CSVRecord(values, null, comment, recordNumber);
        csvRecordNoMapping.get("col1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_MappingNotFound() {
        csvRecord.get("col4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_IndexOutOfBounds() {
        csvRecord.get("col1");
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("col1"));
        assertTrue(csvRecord.isSet("col2"));
        assertTrue(csvRecord.isSet("col3"));
        assertFalse(csvRecord.isSet("col4"));
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("col1"));
        assertTrue(csvRecord.isMapped("col2"));
        assertTrue(csvRecord.isMapped("col3"));
        assertFalse(csvRecord.isMapped("col4"));
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList() {
        List<String> expectedList = Arrays.asList("value1", "value2", "value3");
        assertEquals(expectedList, csvRecord.toList());
    }

    @Test
    public void testPutIn() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("col1", "value1");
        expectedMap.put("col2", "value2");
        expectedMap.put("col3", "value3");
        assertEquals(expectedMap, csvRecord.putIn(new HashMap<>()));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, csvRecord.values());
    }
}

enum TestEnum {
    ENUM1, ENUM2, ENUM3;
}