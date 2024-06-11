package org.apache.commons.csv;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordInstance() {
        String[] values = new String[]{"John", "Doe", "30", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        CSVRecord expectedCSVRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals(expectedCSVRecord, csvRecord);
    }

    @Test
    public void testGetValueByIndex() {
        String[] values = new String[]{"John", "Doe", "30", "Male"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        String value = csvRecord.get(0);
        String expectedValue = "John";

        Assert.assertEquals(expectedValue, value);
    }

    @Test
    public void testGetValueByName() {
        String[] values = new String[]{"Name: John, Age: 30, Gender: Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        String name = csvRecord.get("Name");
        String expectedName = "John";

        Assert.assertEquals(expectedName, name);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueByNameWithNoMapping() {
        String[] values = new String[]{"Name: John, Age: 30, Gender: Male"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        String name = csvRecord.get("Name");
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"John", "Doe", "30", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        boolean isConsistent = csvRecord.isConsistent();
        boolean expectedIsConsistent = true;

        Assert.assertEquals(expectedIsConsistent, isConsistent);
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Name: John, Age: 30, Gender: Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        boolean isMapped = csvRecord.isMapped("Name");
        boolean expectedIsMapped = true;

        Assert.assertEquals(expectedIsMapped, isMapped);
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"Name: John, Age: 30, Gender: Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        boolean isSet = csvRecord.isSet("Name");
        boolean expectedIsSet = true;

        Assert.assertEquals(expectedIsSet, isSet);
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"Name: John, Age: 30, Gender: Male"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        int size = csvRecord.size();
        int expectedSize = 3;

        Assert.assertEquals(expectedSize, size);
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"Name: John, Age: 30, Gender: Male"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        String toString = csvRecord.toString();
        String expectedToString = "[Name: John, Age: 30, Gender: Male]";

        Assert.assertEquals(expectedToString, toString);
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"Name: John, Age: 30, Gender: Male, Comment: This is a comment"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, "This is a comment", 1);

        String comment = csvRecord.getComment();
        String expectedComment = "This is a comment";

        Assert.assertEquals(expectedComment, comment);
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Name: John, Age: 30, Gender: Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        long recordNumber = csvRecord.getRecordNumber();
        long expectedRecordNumber = 1;

        Assert.assertEquals(expectedRecordNumber, recordNumber);
    }
}