package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for CSVRecord
 */
public class CSVRecordTest {

    /**
     * Test method for constructor CSVRecord(String[], Map<String, Integer>, String, long)
     */
    @Test
    public void testCSVRecord() {
        // Test case 1: Construct a CSVRecord with valid values, mapping, comment and recordNumber
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        String comment = "Test comment";
        long recordNumber = 1;
        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        // Test the getter methods for values, mapping, comment and recordNumber
        assertArrayEquals(values, csvRecord.values());
        assertEquals(mapping, csvRecord.mapping);
        assertEquals(comment, csvRecord.getComment());
        assertEquals(recordNumber, csvRecord.getRecordNumber());

        // Test case 2: Construct a CSVRecord with empty values, mapping, comment and recordNumber
        String[] emptyValues = new String[0];
        CSVRecord emptyCSVRecord = new CSVRecord(emptyValues, null, null, 0);

        // Test the getter methods for values, mapping, comment and recordNumber in case of empty CSVRecord
        assertNotNull(emptyCSVRecord.mapping);
        assertArrayEquals(emptyValues, emptyCSVRecord.values());
        assertNull(emptyCSVRecord.getComment());
        assertEquals(0, emptyCSVRecord.getRecordNumber());

        // Test case 3: Test get method for value by index
        assertEquals("Name", csvRecord.get(0));
        assertEquals("Age", csvRecord.get(1));
        assertEquals("City", csvRecord.get(2));

        // Test case 4: Test get method for value by name
        assertEquals("Name", csvRecord.get("Name"));
        assertEquals("Age", csvRecord.get("Age"));
        assertEquals("City", csvRecord.get("City"));
        assertNull(csvRecord.get("InvalidName"));

        // Test case 5: Test isConsistent method
        assertTrue(csvRecord.isConsistent());

        // Test case 6: Test isMapped method
        assertTrue(csvRecord.isMapped("Name"));
        assertTrue(csvRecord.isMapped("Age"));
        assertTrue(csvRecord.isMapped("City"));
        assertFalse(csvRecord.isMapped("InvalidName"));

        // Test case 7: Test isSet method
        assertTrue(csvRecord.isSet("Name"));
        assertTrue(csvRecord.isSet("Age"));
        assertTrue(csvRecord.isSet("City"));
        assertFalse(csvRecord.isSet("InvalidName"));

        // Test case 8: Test iterator method
        List<String> valuesList = Arrays.asList(csvRecord.values());
        Iterator<String> iterator = csvRecord.iterator();

        for (int i = 0; i < csvRecord.size(); i++) {
            assertEquals(valuesList.get(i), iterator.next());
        }

        // Test case 9: Test toString method
        assertEquals("[Name, Age, City]", csvRecord.toString());

        // Test case 10: Test get method for size
        assertEquals(3, csvRecord.size());
    }
}