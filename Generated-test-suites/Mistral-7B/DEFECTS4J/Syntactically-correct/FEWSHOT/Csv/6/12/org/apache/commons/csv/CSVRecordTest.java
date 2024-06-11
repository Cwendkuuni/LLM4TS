package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] values = {};
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 0;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(0, csvRecord.size());
        assertFalse(csvRecord.isConsistent());
        assertNull(csvRecord.getComment());
        assertNull(csvRecord.getRecordNumber());

    }

    @Test
    public void testCSVRecordWithValidValuesAndMapping() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        String comment = "Record Comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("Name,Age,City", Arrays.toString(csvRecord.values()));
        assertArrayEquals(new String[]{"Name", "Age", "City"}, csvRecord.toList().toArray());
        assertEquals(3, csvRecord.size());
        assertTrue(csvRecord.isConsistent());
        assertEquals("Record Comment", csvRecord.getComment());
        assertEquals(1, csvRecord.getRecordNumber());

        assertEquals("Name", csvRecord.get(0));
        assertEquals("Age", csvRecord.get(1));
        assertEquals("City", csvRecord.get(2));

    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithNoMapping() {
        String[] values = {"Name", "Age", "City"};
        String comment = "Record Comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, null, comment, recordNumber);

    }

    @Test
    public void testGetValueByEnum() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);

        assertEquals("Name", csvRecord.get(Enum.valueOf(CSVRecord.class, "NAME")));
    }

    @Test
    public void testIterator() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);

        List<String> list = csvRecord.toList();

        Iterator<String> iterator = csvRecord.iterator();

        assertEquals(list.get(0), iterator.next());
        assertEquals(list.get(1), iterator.next());
        assertEquals(list.get(2), iterator.next());
        assertFalse(iterator.hasNext());

    }

    @Test
    public void testPutIn() {
        String[] values = {"Name", "Age", "City"};
        Map<String, String> map = new HashMap<>();

        CSVRecord csvRecord = new CSVRecord(values, null, null, 0);

        Map<String, String> result = csvRecord.putIn(map);

        assertEquals("Name", result.get("Name"));
        assertEquals("Age", result.get("Age"));
        assertEquals("City", result.get("City"));

    }

    @Test
    public void testIsMapped() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);

        assertTrue(csvRecord.isMapped("Name"));
        assertTrue(csvRecord.isMapped("Age"));
        assertTrue(csvRecord.isMapped("City"));
        assertFalse(csvRecord.isMapped("Test"));

    }

    @Test
    public void testIsSet() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);

        assertTrue(csvRecord.isSet("Name"));
        assertTrue(csvRecord.isSet("Age"));
        assertTrue(csvRecord.isSet("City"));
        assertFalse(csvRecord.isSet("Test"));

        CSVRecord csvRecord2 = new CSVRecord(new String[0], mapping, null, 0);

        assertFalse(csvRecord2.isSet("Name"));
        assertFalse(csvRecord2.isSet("Age"));
        assertFalse(csvRecord2.isSet("City"));

    }

}