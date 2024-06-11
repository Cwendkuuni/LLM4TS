
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {
    private static final String[] VALUES = {"value1", "value2", "value3"};
    private static final Map<String, Integer> MAPPING = new HashMap<>();
    private static final String COMMENT = "comment";
    private static final long RECORD_NUMBER = 1L;

    private CSVRecord csvRecord;

    @Before
    public void setup() {
        MAPPING.put("column1", 0);
        MAPPING.put("column2", 1);
        MAPPING.put("column3", 2);
        csvRecord = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.VALUE1));
        assertEquals("value2", csvRecord.get(TestEnum.VALUE2));
        assertEquals("value3", csvRecord.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        csvRecord = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        csvRecord.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameNotFound() {
        csvRecord.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameIndexOutOfBounds() {
        csvRecord.get("column1");
    }

    @Test
    public void testGetComment() {
        assertEquals(COMMENT, csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(RECORD_NUMBER, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentNoMapping() {
        csvRecord = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertTrue(csvRecord.isMapped("column3"));
        assertFalse(csvRecord.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertTrue(csvRecord.isSet("column3"));
        assertFalse(csvRecord.isSet("column4"));
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList() {
        List<String> expectedList = Arrays.asList(VALUES);
        assertEquals(expectedList, csvRecord.toList());
    }

    @Test
    public void testToMap() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("column1", "value1");
        expectedMap.put("column2", "value2");
        expectedMap.put("column3", "value3");
        assertEquals(expectedMap, csvRecord.toMap());
    }
    
    enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }
}