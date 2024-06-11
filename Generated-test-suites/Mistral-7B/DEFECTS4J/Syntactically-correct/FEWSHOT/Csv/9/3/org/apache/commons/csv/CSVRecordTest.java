package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValues() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, csvRecord.getRecordNumber());
        assertEquals(Arrays.asList(values), csvRecord.toList());
        assertEquals(3, csvRecord.size());
        assertTrue(csvRecord.isConsistent());

        assertEquals("Name", csvRecord.get(0));
        assertEquals("Age", csvRecord.get(1));
        assertEquals("City", csvRecord.get(2));

        assertEquals(null, csvRecord.getComment());
        assertNull(csvRecord.get("UnknownColumn"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithNullMapping() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithoutMapping() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
        csvRecord.get("Name");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCSVRecordWithInvalidIndex() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        csvRecord.get(3);
    }

    @Test
    public void testCSVRecordWithInvalidName() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        assertNull(csvRecord.get("UnknownColumn"));
    }

    @Test
    public void testCSVRecordWithComment() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        String comment = "This is a comment";

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, 1);

        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testCSVRecordIterator() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Iterator<String> iterator = csvRecord.iterator();

        List<String> valuesList = Arrays.asList(csvRecord.values());

        Iterator<String> expectedIterator = valuesList.iterator();

        while (iterator.hasNext()) {
            String next = iterator.next();
            assertEquals(expectedIterator.next(), next);
        }
    }

    @Test
    public void testCSVRecordPutInMap() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Map<String, String> map = new HashMap<>();

        map = csvRecord.putIn(map);

        List<String> valuesList = Arrays.asList(csvRecord.values());

        for (Map.Entry<String, String> entry : map.entrySet()) {
            assertEquals(valuesList.get(entry.getValue().intValue()), entry.getValue());
        }
    }
}