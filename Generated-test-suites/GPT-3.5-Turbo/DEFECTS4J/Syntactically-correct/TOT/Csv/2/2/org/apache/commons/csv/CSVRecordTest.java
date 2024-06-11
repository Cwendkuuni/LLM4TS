import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private static final String[] VALUES = {"value1", "value2", "value3"};
    private static final String COMMENT = "This is a comment";
    private static final long RECORD_NUMBER = 1L;

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;

    @Before
    public void setUp() {
        mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        csvRecord = new CSVRecord(VALUES, mapping, COMMENT, RECORD_NUMBER);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName_ExistingColumn() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_NoMappingProvided() {
        csvRecord = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        csvRecord.get("column1");
    }

    @Test
    public void testGetByName_ColumnNotFound() {
        assertNull(csvRecord.get("column4"));
    }

    @Test
    public void testIsConsistent_MatchingSize() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistent_MismatchedSize() {
        mapping.put("column4", 3);
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped_ExistingColumn() {
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertTrue(csvRecord.isMapped("column3"));
    }

    @Test
    public void testIsMapped_NonExistingColumn() {
        assertFalse(csvRecord.isMapped("column4"));
    }

    @Test
    public void testIsSet_MappedAndValueExists() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertTrue(csvRecord.isSet("column3"));
    }

    @Test
    public void testIsSet_MappedAndValueDoesNotExist() {
        mapping.put("column4", 3);
        assertFalse(csvRecord.isSet("column4"));
    }

    @Test
    public void testIsSet_NonExistingColumn() {
        assertFalse(csvRecord.isSet("column5"));
    }

    @Test
    public void testGetComment_CommentExists() {
        assertEquals(COMMENT, csvRecord.getComment());
    }

    @Test
    public void testGetComment_NoComment() {
        csvRecord = new CSVRecord(VALUES, mapping, null, RECORD_NUMBER);
        assertNull(csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(RECORD_NUMBER, csvRecord.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            assertEquals(VALUES[count++], value);
        }
    }

    @Test
    public void testValues() {
        assertArrayEquals(VALUES, csvRecord.values());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }
}