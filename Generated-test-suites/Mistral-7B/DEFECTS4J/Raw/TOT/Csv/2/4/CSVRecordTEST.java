package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test class for {@link CSVRecord}.
 */
public class CSVRecordTest {

    @Test
    public void testBasicFunctionality() {
        String[] values = {"Alice", "Smith", "alice.smith@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("Alice", record.get(0));
        assertEquals("Smith", record.get(1));
        assertEquals("alice.smith@example.com", record.get(2));
        assertEquals("Alice", record.get("Name"));
        assertEquals("Smith", record.get("Surname"));
        assertEquals("alice.smith@example.com", record.get("Email"));
        assertFalse(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Surname"));
        assertTrue(record.isMapped("Email"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Surname"));
        assertTrue(record.isSet("Email"));
        assertFalse(record.isSet("NonExistentColumn"));

        Iterator<String> iterator = record.iterator();
        assertEquals("Alice", iterator.next());
        assertEquals("Smith", iterator.next());
        assertEquals("alice.smith@example.com", iterator.next());
    }

    @Test(expected = IllegalStateException.class)
    public void testNoHeaderMapping() {
        String[] values = {"Alice", "Smith", "alice.smith@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("NonExistentColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInconsistentValues() {
        String[] values = {"Alice", "Smith", "alice.smith@example.com", "test"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get(3);
    }
}