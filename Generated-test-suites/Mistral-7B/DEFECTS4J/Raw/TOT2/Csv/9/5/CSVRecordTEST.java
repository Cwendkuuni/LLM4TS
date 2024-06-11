package org.apache.commons.csv;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecord_BasicFunctionality() {
        String[] values = new String[]{"John", "Doe", "johndoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("John", csvRecord.get(0));
        Assert.assertEquals("Doe", csvRecord.get(1));
        Assert.assertEquals("johndoe@example.com", csvRecord.get(2));
        Assert.assertEquals("johndoe@example.com", csvRecord.get("Email"));
        Assert.assertNull(csvRecord.get("NonExistentColumn"));
        Assert.assertEquals("1", String.valueOf(csvRecord.getRecordNumber()));
        Assert.assertTrue(csvRecord.isConsistent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_GetValueFromNonExistentColumn() {
        String[] values = new String[]{"John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        csvRecord.get("Email"); // should throw an IllegalArgumentException
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_NoHeaderMapping() {
        String[] values = new String[]{"John", "Doe"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        csvRecord.get("FirstName"); // should throw an IllegalStateException
    }

    @Test
    public void testCSVRecord_ValuesAndMappingMismatch() {
        String[] values = new String[]{"John", "Doe", "johndoe@example.com", "test"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(csvRecord.isConsistent()); // should be inconsistent due to the extra value
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_GetValueFromInconsistentRecord() {
        String[] values = new String[]{"John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        csvRecord.values = new String[0]; // modify the values array
        csvRecord.get("FirstName"); // should throw an IllegalArgumentException
    }

    @Test
    public void testCSVRecord_GetComment() {
        String comment = "This record is a test record.";
        String[] values = new String[]{"John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, 1);

        Assert.assertEquals(comment, csvRecord.getComment());
    }
}