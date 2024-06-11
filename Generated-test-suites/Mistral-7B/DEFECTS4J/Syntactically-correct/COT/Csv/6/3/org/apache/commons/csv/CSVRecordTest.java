package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test class for CSVRecord
 */
public class CSVRecordTest {

    @Test
    public void testCSVRecord_nullValues_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            CSVRecord record = new CSVRecord(null, new HashMap<>(), null, 1L);
        });
    }

    @Test
    public void testCSVRecord_emptyMapping_noError() {
        String[] values = new String[5];
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        assertEquals(5, record.size());
    }

    @Test
    public void testCSVRecord_get_byEnum_returnsCorrectValue() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\nvalue1,value2,value3");
        CSVRecord record = parser.nextRecord();

        Enum<?> enumValue1 = Enum.of("HEADER1");
        Enum<?> enumValue2 = Enum.of("HEADER2");

        String expectedValue1 = "value1";
        String expectedValue2 = "value2";

        String actualValue1 = record.get(enumValue1);
        String actualValue2 = record.get(enumValue2);

        assertEquals(expectedValue1, actualValue1);
        assertEquals(expectedValue2, actualValue2);
    }

    @Test
    public void testCSVRecord_get_byIndex_returnsCorrectValue() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\nvalue1,value2,value3");
        CSVRecord record = parser.nextRecord();

        String expectedValue = "value2";
        int index = 1;

        String actualValue = record.get(index);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testCSVRecord_get_byName_returnsCorrectValue() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\nvalue1,value2,value3");
        CSVRecord record = parser.nextRecord();

        String expectedValue = "value2";
        String name = "header2";

        String actualValue = record.get(name);

        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_get_byName_noMapping_throwsException() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\nvalue1,value2,value3");
        CSVRecord record = parser.nextRecord();

        String name = "header4";

        record.get(name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_get_byName_invalidIndex_throwsException() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\nvalue1,value2,value3");
        CSVRecord record = parser.nextRecord();

        String name = "header1";
        int index = 5;

        record.get(name, index);
    }

    @Test
    public void testCSVRecord_getComment_returnsCorrectValue() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\nvalue1,value2,value3#comment1");
        CSVRecord record = parser.nextRecord();

        String expectedComment = "comment1";
        String actualComment = record.getComment();

        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testCSVRecord_getRecordNumber_returnsCorrectValue() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\n1,2,3");
        CSVRecord record = parser.nextRecord();

        long expectedRecordNumber = 1;
        long actualRecordNumber = record.getRecordNumber();

        assertEquals(expectedRecordNumber, actualRecordNumber);
    }

    @Test
    public void testCSVRecord_isConsistent_returnsTrueIfConsistent() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\nvalue1,value2,value3");
        CSVRecord record = parser.nextRecord();

        Map<String, Integer> mapping = record.mapping;

        assertTrue(record.isConsistent());
        assertTrue(mapping != null);
        assertTrue(mapping.size() == 3);
    }

    @Test
    public void testCSVRecord_isMapped_returnsTrueIfMapped() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\nvalue1,value2,value3");
        CSVRecord record = parser.nextRecord();

        String name = "header2";

        assertTrue(record.isMapped(name));
    }

    @Test
    public void testCSVRecord_isEmpty_returnsTrueIfEmpty() {
        String[] values = new String[0];
        CSVRecord record = new CSVRecord(values, null, null, 1L);

        assertTrue(record.isEmpty());
    }

    @Test
    public void testCSVRecord_isSet_returnsTrueIfSet() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\nvalue1,value2,value3");
        CSVRecord record = parser.nextRecord();

        String name = "header2";

        assertTrue(record.isSet(name));
    }

    @Test
    public void testCSVRecord_iterator_returnsCorrectValues() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\nvalue1,value2,value3");
        CSVRecord record = parser.nextRecord();

        List<String> expectedValues = Arrays.asList("value1", "value2", "value3");

        Iterator<String> iterator = record.iterator();
        List<String> actualValues = Arrays.asList(iterator.next(), iterator.next(), iterator.next());

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testCSVRecord_putIn_mapsValuesToMap() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\nvalue1,value2,value3");
        CSVRecord record = parser.nextRecord();

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("header1", "value1");
        expectedMap.put("header2", "value2");
        expectedMap.put("header3", "value3");

        Map<String, String> actualMap = record.putIn(new HashMap<String, String>());

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testCSVRecord_toString_returnsCorrectStringRepresentation() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\nvalue1,value2,value3");
        CSVRecord record = parser.nextRecord();

        String expectedString = "[CSVRecord: values=[value1, value2, value3], mapping=null, comment=null, recordNumber=1]";

        String actualString = record.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testCSVRecord_values_returnsCorrectValuesArray() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse("header1,header2,header3\nvalue1,value2,value3");
        CSVRecord record = parser.nextRecord();

        String[] expectedValues = new String[]{"value1", "value2", "value3"};

        String[] actualValues = record.values();

        assertArrayEquals(expectedValues, actualValues);
    }
}