package org.apache.commons.csv;

import static org.junit.Assert.*;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test class for CSVRecord class
 */
public class CSVRecordTest {

    @Test
    public void testCSVRecordConstructor() {
        String[] values = new String[]{"John", "Doe", "30", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 1);
            put("Email", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertEquals("Name=John, Age=30, Email=jdoe@example.com,", record.toString());
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("jdoe@example.com", record.get(3));
        assertNull(record.getComment());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Email"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("Email"));

        Iterator<String> iterator = record.iterator();
        List<String> valuesList = record.toList();
        Map<String, String> map = record.toMap();

        assertEquals("John", iterator.next());
        assertEquals("Doe", iterator.next());
        assertEquals("30", iterator.next());
        assertNull(iterator.next());
        assertEquals(Arrays.asList("John", "Doe", "30", "jdoe@example.com"), valuesList);
        assertEquals(Arrays.asList("Name=John, Age=30, Email=jdoe@example.com", "Name=John", "Age=30", "Email=jdoe@example.com"), map.values());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWhenNoMapping() {
        String[] values = new String[]{"John", "Doe", "30", "jdoe@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWhenNotMapped() {
        String[] values = new String[]{"John", "Doe", "30", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 1);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("Email");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetWhenInvalidIndex() {
        String[] values = new String[]{"John", "Doe", "30", "jdoe@example.com"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 1);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get(3);
    }

    @Test
    public void testGetNullString() {
        String[] values = new String[]{"John", "Doe", null, "jdoe@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(null, record.get(""));
        assertEquals(null, record.get(""));
        assertEquals(null, record.get(3));
    }

    @Test
    public void testGetEnum() {
        String[] values = new String[]{"1", "John", "Doe", "30", "male"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("ID", 0);
            put("Name", 1);
            put("Gender", 3);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("male", record.get(Gender.GENDER));

        enum Gender {GENDER}
    }

    @Test
    public void testIsConsistentWhenMissingColumnMapping() {
        String[] values = new String[]{"John", "Doe", "30"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsConsistentWhenExtraColumn() {
        String[] values = new String[]{"John", "Doe", "30", "jdoe@example.com", "extra"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 1);
            put("Email", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsConsistentWhenMissingColumn() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 1);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

}