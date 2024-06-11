import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum_ValidEnum_ReturnsValue() {
        String value = csvRecord.get(Enum1.ENUM_1);
        assertEquals("value1", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByEnum_InvalidEnum_ThrowsException() {
        csvRecord.get(Enum2.ENUM_2);
    }

    @Test
    public void testGetByIndex_ValidIndex_ReturnsValue() {
        String value = csvRecord.get(0);
        assertEquals("value1", value);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByIndex_InvalidIndex_ThrowsException() {
        csvRecord.get(3);
    }

    @Test
    public void testGetByName_ValidName_ReturnsValue() {
        String value = csvRecord.get("column1");
        assertEquals("value1", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_NoMapping_ThrowsException() {
        csvRecord.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_InvalidName_ThrowsException() {
        csvRecord.get("column4");
    }

    @Test
    public void testGetComment_ReturnsComment() {
        String comment = csvRecord.getComment();
        assertEquals("This is a comment", comment);
    }

    @Test
    public void testGetRecordNumber_ReturnsRecordNumber() {
        long recordNumber = csvRecord.getRecordNumber();
        assertEquals(1, recordNumber);
    }

    @Test
    public void testIsConsistent_ValidMapping_ReturnsTrue() {
        boolean isConsistent = csvRecord.isConsistent();
        assertTrue(isConsistent);
    }

    @Test
    public void testIsConsistent_InvalidMapping_ReturnsFalse() {
        csvRecord = new CSVRecord(new String[] { "value1" }, csvRecord.mapping, csvRecord.comment, csvRecord.recordNumber);
        boolean isConsistent = csvRecord.isConsistent();
        assertFalse(isConsistent);
    }

    @Test
    public void testIsMapped_ValidName_ReturnsTrue() {
        boolean isMapped = csvRecord.isMapped("column1");
        assertTrue(isMapped);
    }

    @Test
    public void testIsMapped_InvalidName_ReturnsFalse() {
        boolean isMapped = csvRecord.isMapped("column4");
        assertFalse(isMapped);
    }

    @Test
    public void testIsSet_ValidName_ReturnsTrue() {
        boolean isSet = csvRecord.isSet("column1");
        assertTrue(isSet);
    }

    @Test
    public void testIsSet_InvalidName_ReturnsFalse() {
        boolean isSet = csvRecord.isSet("column4");
        assertFalse(isSet);
    }

    @Test
    public void testIterator_ReturnsIterator() {
        assertNotNull(csvRecord.iterator());
    }

    @Test
    public void testPutIn_ValidMapping_PutsAllValuesInMap() {
        Map<String, String> map = new HashMap<>();
        csvRecord.putIn(map);
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize_ReturnsSize() {
        int size = csvRecord.size();
        assertEquals(3, size);
    }

    @Test
    public void testToList_ReturnsList() {
        assertNotNull(csvRecord.toList());
        assertEquals(3, csvRecord.toList().size());
    }

    @Test
    public void testToMap_ReturnsMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testToString_ReturnsString() {
        String string = csvRecord.toString();
        assertEquals("[value1, value2, value3]", string);
    }

    @Test
    public void testValues_ReturnsValuesArray() {
        String[] values = csvRecord.values();
        assertNotNull(values);
        assertEquals(3, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }

    enum Enum1 {
        ENUM_1
    }

    enum Enum2 {
        ENUM_2
    }
}