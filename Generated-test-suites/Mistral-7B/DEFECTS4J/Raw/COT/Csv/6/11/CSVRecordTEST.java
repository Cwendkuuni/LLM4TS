package org.apache.commons.csv.test;

import static org.junit.Assert.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for {@link CSVRecord}
 */
public class CSVRecordTest {

    @Test
    public void testCSVRecord_ValidValues() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
        }}, null, 1);

        assertEquals("John", record.get("Name"));
        assertEquals("Doe", record.get("Name")); // case insensitive
        assertEquals("30", record.get("Age"));
        assertEquals("30", record.get(0));
        assertEquals("30", record.get(1));
        assertEquals("John Doe", record.toString());
    }

    @Test
    public void testCSVRecord_EmptyValues() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 1);
        assertTrue(record.isEmpty());
        assertEquals(0, record.size());
        assertEquals("", record.get(0));
        assertNull(record.get("Name"));
        assertFalse(record.isConsistent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_InvalidMapping() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
        record.get("Unknown");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCSVRecord_InvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>() {{
            put("Name", 0);
        }}, null, 1);
        record.get(2);
    }

    @Test
    public void testCSVRecord_NullComment() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
        assertNull(record.getComment());
    }

    @Test
    public void testCSVRecord_ValidComment() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, "Comment", 1);
        assertEquals("Comment", record.getComment());
    }

    @Test
    public void testCSVRecord_EmptyComment() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, "", 1);
        assertNull(record.getComment());
    }

    @Test
    public void testCSVRecord_Consistent() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
        }}, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecord_Inconsistent() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe"}, null, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecord_ValidGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
        assertEquals("John", record.get(CSVRecordTest.Name.NAME));
        assertEquals("Doe", record.get(CSVRecordTest.Age.AGE));
        assertEquals("30", record.get(CSVRecordTest.Age.AGE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_InvalidGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
        record.get(CSVRecordTest.Unknown.UNKNOWN);
    }

    @Test
    public void testCSVRecord_ValidGetByMap() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:30", "Comment:Test"},
                new HashMap<String, Integer>() {{
                    put("Name", 0);
                    put("Age", 1);
                    put("Comment", 2);
                }}, null, 1);
        assertEquals("John", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals("Test", record.get("Comment"));
        assertEquals("Test", record.get(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_NoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:30", "Comment:Test"}, null, null, 1);
        record.get("Unknown");
    }

    @Test
    public void testCSVRecord_ValidPutInMap() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:30", "Comment:Test"}, null, null, 1);
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("John", map.get("Name"));
        assertEquals("30", map.get("Age"));
        assertEquals("Test", map.get("Comment"));
    }

    @Test
    public void testCSVRecord_ValidSize() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:30", "Comment:Test"}, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testCSVRecord_ValidToString() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:30", "Comment:Test"}, null, null, 1);
        assertEquals("[Name:John, Age:30, Comment:Test]", record.toString());
    }

    @Test
    public void testCSVRecord_ValidValuesList() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:30", "Comment:Test"}, null, null, 1);
        List<String> valuesList = record.toList();
        assertEquals("Name:John", valuesList.get(0));
        assertEquals("Age:30", valuesList.get(1));
        assertEquals("Comment:Test", valuesList.get(2));
    }

    @Test
    public void testCSVRecord_ValidMap() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:30", "Comment:Test"}, null, null, 1);
        Map<String, String> map = record.toMap();
        assertEquals("John", map.get("Name"));
        assertEquals("30", map.get("Age"));
        assertEquals("Test", map.get("Comment"));
    }

    @Test
    public void testCSVRecord_ValidGetByEnumFromCSVParser() throws Exception {
        String csvText = "Name,Age,Comment\nName:John,Age:30,Comment:Test";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csvText));
        CSVRecord record = parser.nextRecord();
        assertEquals("John", record.get(CSVRecordTest.Name.NAME));
        assertEquals("30", record.get(CSVRecordTest.Age.AGE));
        assertEquals("Test", record.get(CSVRecordTest.Age.AGE));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_InvalidGetByEnumFromCSVParser() throws Exception {
        String csvText = "Name,Age\nName:John,Age:30";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csvText));
        CSVRecord record = parser.nextRecord();
        record.get(CSVRecordTest.Unknown.UNKNOWN);
    }

    enum Name {
        NAME
    }

    enum Age {
        AGE
    }

    enum Unknown {
        UNKNOWN
    }
}