package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord_nullValuesAndMapping_createRecordWithEmptyStringArrayAndNullMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertEquals(CSVRecord.EMPTY_STRING_ARRAY, record.values());
        assertNull(record.mapping);
    }

    @Test
    public void testCSVRecord_emptyValuesAndMapping_createRecordWithEmptyStringArrayAndEmptyMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(CSVRecord.EMPTY_STRING_ARRAY, mapping, null, 0);
        assertEquals(CSVRecord.EMPTY_STRING_ARRAY, record.values());
        assertEquals(mapping.size(), record.mapping.size());
        assertTrue(record.mapping.isEmpty());
    }

    @Test
    public void testCSVRecord_valuesAndMapping_createRecordWithGivenValuesAndMapping() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertArrayEquals(values, record.values());
        assertEquals(mapping.size(), record.mapping.size());
        assertArrayEquals(mapping.toArray(), record.mapping.toArray());
    }

    @Test
    public void testCSVRecord_get_returnValueByIndex() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("john.doe@example.com", record.get(3));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_get_noHeaderMapping_throwException() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_get_recordIsInconsistent_throwException() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com", "Foo"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("Foo");
    }

    @Test
    public void testCSVRecord_isConsistent_returnTrueIfValuesAndMappingSizeEquals() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecord_isMapped_returnTrueIfMappingContainsKey() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Email"));
        assertFalse(record.isMapped("Foo"));
    }

    @Test
    public void testCSVRecord_isSet_returnTrueIfValueIsSetForGivenColumnName() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertFalse(record.isSet("Foo"));
    }

    @Test
    public void testCSVRecord_iterator_returnIteratorOverValues() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        List<String> valuesList = Arrays.asList(record.values());
        Iterator<String> recordIterator = record.iterator();
        Iterator<String> valuesListIterator = valuesList.iterator();
        assertTrue(recordIterator.hasNext());
        assertTrue(recordIterator.next().equals(valuesListIterator.next()));
        assertEquals(valuesList.size(), record.size());
    }

    @Test
    public void testCSVRecord_toString_returnStringRepresentation() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("[John, Doe, 30, john.doe@example.com]", record.toString());
    }

    @Test
    public void testCSVRecord_getComment_returnComment() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        String comment = "This is a comment for record";
        CSVRecord record = new CSVRecord(values, mapping, comment, 0);
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testCSVRecord_getRecordNumber_returnRecordNumber() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        long recordNumber = 12345;
        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }
}