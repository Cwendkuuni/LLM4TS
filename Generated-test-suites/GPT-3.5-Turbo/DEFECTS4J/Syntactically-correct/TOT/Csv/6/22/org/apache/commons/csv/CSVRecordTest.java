import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setup() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        String comment = "This is a comment";
        long recordNumber = 1;
        
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum_ValidEnum_Success() {
        String expected = "value1";
        String actual = csvRecord.get(ColumnEnum.column1);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByEnum_InvalidEnum_ExceptionThrown() {
        csvRecord.get(ColumnEnum.column3);
    }

    @Test
    public void testGetByIndex_ValidIndex_Success() {
        String expected = "value2";
        String actual = csvRecord.get(1);
        assertEquals(expected, actual);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByIndex_InvalidIndex_ExceptionThrown() {
        csvRecord.get(2);
    }

    @Test
    public void testGetByName_ValidName_Success() {
        String expected = "value1";
        String actual = csvRecord.get("column1");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_InvalidName_ExceptionThrown() {
        csvRecord.get("column3");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_NoMapping_ExceptionThrown() {
        csvRecord = new CSVRecord(null, null, null, 1);
        csvRecord.get("column1");
    }

    @Test
    public void testGetComment_ValidComment_Success() {
        String expected = "This is a comment";
        String actual = csvRecord.getComment();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetComment_NoComment_ReturnNull() {
        csvRecord = new CSVRecord(null, null, null, 1);
        assertNull(csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber_ValidRecordNumber_Success() {
        long expected = 1;
        long actual = csvRecord.getRecordNumber();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsConsistent_ConsistentRecord_ReturnTrue() {
        boolean actual = csvRecord.isConsistent();
        assertTrue(actual);
    }

    @Test
    public void testIsConsistent_InconsistentRecord_ReturnFalse() {
        csvRecord = new CSVRecord(new String[] {"value1"}, null, null, 1);
        boolean actual = csvRecord.isConsistent();
        assertFalse(actual);
    }

    @Test
    public void testIsMapped_ValidName_ReturnTrue() {
        boolean actual = csvRecord.isMapped("column1");
        assertTrue(actual);
    }

    @Test
    public void testIsMapped_InvalidName_ReturnFalse() {
        boolean actual = csvRecord.isMapped("column3");
        assertFalse(actual);
    }

    @Test
    public void testIsMapped_NoMapping_ReturnFalse() {
        csvRecord = new CSVRecord(null, null, null, 1);
        boolean actual = csvRecord.isMapped("column1");
        assertFalse(actual);
    }

    @Test
    public void testIsSet_MappedAndHasValue_ReturnTrue() {
        boolean actual = csvRecord.isSet("column1");
        assertTrue(actual);
    }

    @Test
    public void testIsSet_MappedButNoValue_ReturnFalse() {
        csvRecord = new CSVRecord(new String[] {"value1", ""}, null, null, 1);
        boolean actual = csvRecord.isSet("column2");
        assertFalse(actual);
    }

    @Test
    public void testIsSet_Unmapped_ReturnFalse() {
        boolean actual = csvRecord.isSet("column3");
        assertFalse(actual);
    }

    @Test
    public void testIterator_ValidRecord_IterateSuccessfully() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testPutIn_ValidMap_PutSuccessfully() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> expected = new HashMap<>();
        expected.put("column1", "value1");
        expected.put("column2", "value2");

        csvRecord.putIn(map);

        assertEquals(expected, map);
    }

    @Test
    public void testSize_ValidRecord_ReturnSize() {
        int expected = 2;
        int actual = csvRecord.size();
        assertEquals(expected, actual);
    }

    @Test
    public void testToList_ValidRecord_ReturnList() {
        int expected = 2;
        int actual = csvRecord.toList().size();
        assertEquals(expected, actual);
    }

    @Test
    public void testToMap_ValidRecord_ReturnMap() {
        Map<String, String> expected = new HashMap<>();
        expected.put("column1", "value1");
        expected.put("column2", "value2");

        Map<String, String> actual = csvRecord.toMap();

        assertEquals(expected, actual);
    }

    @Test
    public void testToString_ValidRecord_ReturnString() {
        String expected = "[value1, value2]";
        String actual = csvRecord.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testValues_ValidRecord_ReturnValues() {
        String[] expected = {"value1", "value2"};
        String[] actual = csvRecord.values();
        assertArrayEquals(expected, actual);
    }
    
    private enum ColumnEnum {
        column1,
        column2,
        column3
    }

}