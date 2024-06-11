import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;

    @Before
    public void setup() {
        String[] values = new String[]{"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        csvRecord = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    public void testGetByEnum_ValidColumn_ReturnsValue() {
        Enum<?> enumValue = SomeEnum.COLUMN1;
        String expectedValue = "value1";

        String actualValue = csvRecord.get(enumValue);

        assertEquals(expectedValue, actualValue);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetByEnum_InvalidColumn_ThrowsIllegalArgumentException() {
        Enum<?> enumValue = SomeEnum.COLUMN4;

        csvRecord.get(enumValue);
    }

    @Test
    public void testGetByIndex_ValidIndex_ReturnsValue() {
        int index = 1;
        String expectedValue = "value2";

        String actualValue = csvRecord.get(index);

        assertEquals(expectedValue, actualValue);
    }

    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testGetByIndex_InvalidIndex_ThrowsArrayIndexOutOfBoundsException() {
        int index = 5;

        csvRecord.get(index);
    }

    @Test
    public void testGetByName_ValidName_ReturnsValue() {
        String name = "column3";
        String expectedValue = "value3";

        String actualValue = csvRecord.get(name);

        assertEquals(expectedValue, actualValue);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetByName_InvalidName_ThrowsIllegalArgumentException() {
        String name = "column4";

        csvRecord.get(name);
    }

    @Test(expected=IllegalStateException.class)
    public void testGetByName_NoMappingSpecified_ThrowsIllegalStateException() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 1L);
        String name = "column1";

        record.get(name);
    }

    @Test
    public void testGetComment_NullComment_ReturnsNull() {
        assertNull(csvRecord.getComment());
    }

    @Test
    public void testGetComment_ValidComment_ReturnsComment() {
        String comment = "This is a comment";
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, comment, 1L);

        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber_ValidNumber_ReturnsNumber() {
        long expectedNumber = 1L;

        long actualNumber = csvRecord.getRecordNumber();

        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    public void testIsConsistent_ValidMappingAndValues_ReturnsTrue() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistent_NullMapping_ReturnsTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 1L);

        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_InconsistentMappingAndValues_ReturnsFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1L);

        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped_ValidMappedColumn_ReturnsTrue() {
        String columnName = "column2";

        assertTrue(csvRecord.isMapped(columnName));
    }

    @Test
    public void testIsMapped_InvalidMappedColumn_ReturnsFalse() {
        String columnName = "column4";

        assertFalse(csvRecord.isMapped(columnName));
    }

    @Test
    public void testIsMapped_NullMapping_ReturnsFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 1L);
        String columnName = "column1";

        assertFalse(record.isMapped(columnName));
    }

    @Test
    public void testIsSet_ValidMappedAndExistingColumn_ReturnsTrue() {
        String columnName = "column3";

        assertTrue(csvRecord.isSet(columnName));
    }

    @Test
    public void testIsSet_ValidMappedAndNonExistingColumn_ReturnsFalse() {
        String columnName = "column4";

        assertFalse(csvRecord.isSet(columnName));
    }

    @Test
    public void testIsSet_NullMapping_ReturnsFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 1L);
        String columnName = "column1";

        assertFalse(record.isSet(columnName));
    }

    @Test
    public void testIterator_ValuesExist_ReturnsIterator() {
        int expectedSize = 3;

        int actualSize = 0;
        for (String value : csvRecord) {
            actualSize++;
        }

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testPutIn_ValidRecord_ReturnsMap() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("column1", "value1");
        expectedMap.put("column2", "value2");
        expectedMap.put("column3", "value3");

        Map<String, String> actualMap = csvRecord.putIn(new HashMap<String, String>());

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testSize_ValidRecord_ReturnsSize() {
        int expectedSize = 3;

        int actualSize = csvRecord.size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testToList_ValidRecord_ReturnsList() {
        int expectedSize = 3;

        int actualSize = csvRecord.toList().size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testToMap_ValidRecord_ReturnsMap() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("column1", "value1");
        expectedMap.put("column2", "value2");
        expectedMap.put("column3", "value3");

        Map<String, String> actualMap = csvRecord.toMap();

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testToString_ValidRecord_ReturnsString() {
        String expectedString = "[value1, value2, value3]";

        String actualString = csvRecord.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testValues_ValidRecord_ReturnsValuesArray() {
        String[] expectedValues = new String[]{"value1", "value2", "value3"};

        String[] actualValues = csvRecord.values();

        assertArrayEquals(expectedValues, actualValues);
    }
}

enum SomeEnum {
    COLUMN1, COLUMN2, COLUMN3, COLUMN4;
}