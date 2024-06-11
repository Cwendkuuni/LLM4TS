package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CSVRecordTest {

    @org.junit.Test
    public void testCSVRecord_basic() {
        String[] values = new String[]{"John", "Doe", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Email", 2);
         CSVRecord record = new CSVRecord(values, mapping, null, 1);
         CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, ',');
         assertEquals(record.size(), format.getHeaderCount());
         assertEquals(record.get(0), "John");
         assertEquals(record.get(1), "Doe");
         assertEquals(record.get(2), "jdoe@example.com");
         assertNull(record.getComment());
         assertFalse(record.isConsistent());
         assertFalse(record.isMapped("NonExistentColumnName"));
         assertTrue(record.isSet("Name"));
         assertTrue(record.isSet("Surname"));
         assertTrue(record.isSet("Email"));
         Map<String, String> map = record.toMap();
         assertEquals(map.get("Name"), "John");
         assertEquals(map.get("Surname"), "Doe");
         assertEquals(map.get("Email"), "jdoe@example.com");
    }

    @org.junit.Test
    public void testGet_Enum() {
        String[] values = new String[]{"John", "Doe", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Email", 2);
         CSVRecord record = new CSVRecord(values, mapping, null, 1);
         assertEquals(record.get(Enum.NAME), "John");
         assertEquals(record.get(Enum.SURNAME), "Doe");
         assertEquals(record.get(Enum.EMAIL), "jdoe@example.com");
         enum Enum {
            NAME, SURNAME, EMAIL
         }
    }

    @org.junit.Test
    public void testGet_IndexOutOfBoundsException() {
        String[] values = new String[]{"John", "Doe", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(-1));
    }

    @org.junit.Test
    public void testGet_IllegalArgumentException() {
        String[] values = new String[]{"John", "Doe", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        String nonExistentColumnName = "NonExistentColumnName";
        assertThrows(IllegalArgumentException.class, () -> record.get(nonExistentColumnName));
    }

    @org.junit.Test
    public void testGet_IndexMismatchException() {
        String[] values = new String[]{"John", "Doe", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        mapping.put("NonExistentColumnName", 100);
        assertThrows(IllegalArgumentException.class, () -> record.get("NonExistentColumnName"));
    }

    @org.junit.Test
    public void testGetComment() {
        String[] values = new String[]{"John", "Doe", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "Comment for this record";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1);
        assertEquals(record.getComment(), comment);
    }

    @org.junit.Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"John", "Doe", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(record.getRecordNumber(), 1);
    }

    @org.junit.Test
    public void testIsConsistent() {
        String[] values = new String[]{"John", "Doe", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());
        mapping.put("NonExistentColumnName", 100);
        assertFalse(record.isConsistent());
    }

    @org.junit.Test
    public void testIsMapped() {
        String[] values = new String[]{"John", "Doe", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("Name"));
        assertFalse(record.isMapped("NonExistentColumnName"));
    }

    @org.junit.Test
    public void testIsSet() {
        String[] values = new String[]{"John", "Doe", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Surname"));
        assertTrue(record.isSet("Email"));
        mapping.put("NonExistentColumnName", 100);
        assertFalse(record.isSet("NonExistentColumnName"));
    }

    @org.junit.Test
    public void testIterator() {
        String[] values = new String[]{"John", "Doe", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Email", 2);
         CSVRecord record = new CSVRecord(values, mapping, null, 1);
         Iterator<String> iterator = record.iterator();
         List<String> expectedList = new ArrayList<>();
         expectedList.add("John");
         expectedList.add("Doe");
         expectedList.add("jdoe@example.com");
         List<String> actualList = new ArrayList<>();
         while (iterator.hasNext()) {
             actualList.add(iterator.next());
         }
         assertEquals(expectedList, actualList);
    }

    @org.junit.Test
    public void testPutIn() {
        String[] values = new String[]{"John", "Doe", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Email", 2);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "John");
        expectedMap.put("Surname", "Doe");
        expectedMap.put("Email", "jdoe@example.com");
        Map<String, String> actualMap = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        actualMap = record.putIn(actualMap);
        assertEquals(expectedMap, actualMap);
    }

    @org.junit.Test
    public void testToString() {
        String[] values = new String[]{"John", "Doe", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("[John, Doe, jdoe@example.com]", record.toString());
    }

    enum Enum {
        NAME, SURNAME, EMAIL
    }

}