package org.example.junit4;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        String[] values = new String[]{"Apple", "Red", "1.5"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Fruit", 0);
        mapping.put("Color", 1);
        mapping.put("Weight", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("Apple", record.get(Enum.valueOf(Fruit.class, "Fruit")));
    }

    @Test
    public void testGetInt() {
        String[] values = new String[]{"Apple", "Red", "1.5"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        assertEquals("Red", record.get(1));
    }

    @Test
    public void testGetString() {
        String[] values = new String[]{"Apple", "Red", "1.5"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Color", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("Red", record.get("Color"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetString_MissingColumn() {
        String[] values = new String[]{"Apple", "Red", "1.5"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Fruit", 0);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        record.get("Weight"); // Should throw exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetString_NullMapping() {
        String[] values = new String[]{"Apple", "Red", "1.5"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        record.get("Color"); // Should throw exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetString_IncorrectMapping() {
        String[] values = new String[]{"Apple", "Red", "1.5"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Fruit", 0);
        mapping.put("Color", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        record.get("Color"); // Should throw exception
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetInt_OutOfBoundsIndex() {
        String[] values = new String[]{"Apple", "Red", "1.5"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        record.get(values.length); // Should throw exception
    }

    @Test(expected = NullPointerException.class)
    public void testGetString_NullInput() {
        String[] values = new String[]{"Apple", "Red", "1.5"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Color", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        record.get(null); // Should throw exception
    }
}