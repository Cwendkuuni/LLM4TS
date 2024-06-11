package org.apache.commons.csv;

import static org.junit.Assert.*;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordTest;
import org.junit.Test;

/**
 * Test class for CSVRecord.
 *
 * @author johan.lstefan <johan.lstefan@gmail.com>
 */
public class CSVRecordTest {

    // Basic tests

    @Test
    public void testGetByEnum() {
        CSVRecord record = createCSVRecord("column1,column2,column3", null, null, 1L);
        assertEquals("column1", record.get(CSVRecordTest.Column1.COLUMN1));
        assertEquals("column2", record.get(CSVRecordTest.Column1.COLUMN2));
        assertEquals("column3", record.get(CSVRecordTest.Column1.COLUMN3));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = createCSVRecord(null, null, null, 1L);
        record.setValue(0, "value1");
        record.setValue(1, "value2");
        record.setValue(2, "value3");

        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = createCSVRecord("column1:column1,column2:column2,column3:column3", null, null, 1L);
        assertEquals("column1", record.mapping.get("column1"));
        assertEquals(0, record.mapping.get("column1").intValue());
        assertEquals("column2", record.mapping.get("column2"));
        assertEquals(1, record.mapping.get("column2").intValue());
        assertEquals("column3", record.mapping.get("column3"));
        assertEquals(2, record.mapping.get("column3").intValue());

        assertEquals("column1Value", record.get("column1"));
        assertEquals("column2Value", record.get("column2"));
        assertEquals("column3Value", record.get("column3"));
    }

    @Test
    public void testGetComment() {
        CSVRecord record = createCSVRecord(null, null, "comment", 1L);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = createCSVRecord(null, null, null, 1L);
        assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = createCSVRecord("column1:column1,column2:column2,column3:column3", null, null, 1L);
        assertTrue(record.isConsistent());

        record = createCSVRecord("column1:column1,column2:column2,column3:column3,column4", null, null, 1L);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = createCSVRecord("column1:column1,column2:column2,column3:column3", null, null, 1L);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
        assertFalse(record.isMapped("nonExistentColumn"));

        record = createCSVRecord(null, null, null, 1L);
        assertFalse(record.isMapped("column1"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = createCSVRecord("column1:column1,column2:column2,column3:column3", null, null, 1L);
        record.setValue(0, "column1Value");
        record.setValue(1, "column2Value");
        record.setValue(2, "column3Value");

        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
        assertFalse(record.isSet("nonExistentColumn"));

        record = createCSVRecord(null, null, null, 1L);
        assertFalse(record.isSet("column1"));
    }

    @Test
    public void testSize() {
        CSVRecord record = createCSVRecord(null, null, null, 1L);
        assertEquals(0, record.size());

        record = createCSVRecord("column1:column1,column2:column2,column3:column3", null, null, 1L);
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = createCSVRecord(null, null, null, 1L);
        assertEquals("[], []", record.toString());

        record = createCSVRecord("column1:column1,column2:column2,column3:column3", null, null, 1L);
        assertEquals("[column1, column2, column3]", record.toString());
    }

    // Edge cases and exception handling tests

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWhenNoMapping() {
        CSVRecord record = createCSVRecord(null, null, null, 1L);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWhenIndexOutOfBounds() {
        CSVRecord record = createCSVRecord("column1:column1,column2:column2,column3:column3", null, null, 1L);
        record.mapping.put("column4", 3);
        record.values = new String[3];
        record.values[0] = "column1Value";
        record.values[1] = "column2Value";
        record.values[2] = "column3Value";

        record.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWhenRecordIsInconsistent() {
        CSVRecord record = createCSVRecord("column1:column1,column2:column2,column3:column3,column4", null, null, 1L);
        record.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetByNameWhenIndexOutOfBounds() {
        CSVRecord record = createCSVRecord("column1:column1,column2:column2,column3:column3", null, null, 1L);
        record.mapping.put("column4", 3);

        record.setValue("column4", "newValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetByNameWhenRecordIsInconsistent() {
        CSVRecord record = createCSVRecord("column1:column1,column2:column2,column3:column3,column4", null, null, 1L);
        record.setValue("column4", "newValue");
    }

    // Helper method

    private CSVRecord createCSVRecord(String header, Map<String, Integer> mapping, String comment, long recordNumber) {
        String[] values = new String[3];
        if (header != null) {
            mapping = new HashMap<>();
            for (String part : header.split(",")) {
                String[] nameValue = part.split(":");
                mapping.put(nameValue[0], Integer.valueOf(Integer.parseInt(nameValue[1].replaceAll("\\D+", ""))));
            }
        }
        return new CSVRecord(values, mapping, comment, recordNumber);
    }

}

enum Column1 {
    COLUMN1, COLUMN2, COLUMN3
}