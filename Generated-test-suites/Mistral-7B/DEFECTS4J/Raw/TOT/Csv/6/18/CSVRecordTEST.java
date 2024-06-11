package org.apache.commons.csv;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CSVRecord class.
 */
public class CSVRecordTest {

    /**
     * Test for get method with index.
     */
    @Test
    public void testGetByIndex() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], record.get(i));
        }
    }

    /**
     * Test for get method with name.
     */
    @Test
    public void testGetByName() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("John", record.get("Name"));
        assertEquals("Doe", record.get("Name")); // case insensitive
        assertEquals("30", record.get("Age"));
        assertNull(record.get("Unknown"));
        assertNull(record.get(null));
    }

    /**
     * Test for get method with enum.
     */
    @Test
    public void testGetByEnum() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("John", record.get(CSVRecordTest.Name.NAME));
        assertEquals("Doe", record.get(CSVRecordTest.Name.NAME)); // case insensitive
        assertEquals("30", record.get(CSVRecordTest.Age.AGE));

        assertNull(record.get(CSVRecordTest.Unknown.UNEXPECTED));
        assertNull(record.get(null));
    }

    /**
     * Test for isConsistent method.
     */
    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        String[] inconsistentValues = new String[]{"John", "Doe"};
        CSVRecord inconsistentRecord = new CSVRecord(inconsistentValues, mapping, null, 1);
        assertFalse(inconsistentRecord.isConsistent());
    }

    /**
     * Test for isMapped method.
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Name")); // case insensitive
        assertTrue(record.isMapped("Age"));
        assertFalse(record.isMapped("Unknown"));
        assertFalse(record.isMapped(null));

        Map<String, Integer> emptyMapping = new HashMap<>();
        CSVRecord emptyRecord = new CSVRecord(values, emptyMapping, null, 1);
        assertFalse(emptyRecord.isMapped("Name"));
    }

    /**
     * Test for isSet method.
     */
    @Test
    public void testIsSet() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Name")); // case insensitive
        assertTrue(record.isSet("Age"));
        assertFalse(record.isSet("Unknown"));
        assertFalse(record.isSet(null));
        assertFalse(record.isSet(""));

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, mapping, null, 1);
        assertFalse(emptyRecord.isSet("Name"));
    }

    /**
     * Test for getRecordNumber method.
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    /**
     * Test for getComment method.
     */
    @Test
    public void testGetComment() {
        String[] values = new String[]{"John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Comment", 1);
        String comment = "This is a comment";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1);
        assertEquals(comment, record.getComment());
    }

    /**
     * Test for iterator method.
     */
    @Test
    public void testIterator() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> expectedList = Arrays.asList("John", "Doe", "30", "M");
        Iterator<String> iterator = record.iterator();
        assertSame(expectedList.iterator(), iterator);
    }

    /**
     * Test for putIn method.
     */
    @Test
    public void testPutIn() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, String> resultMap = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "John");
        expectedMap.put("Age", "30");
        expectedMap.put("Gender", "M");

        record.putIn(expectedMap);

        assertArrayEquals(values, expectedMap.values().toArray());
    }

    /**
     * Test for size method.
     */
    @Test
    public void testSize() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(values.length, record.size());
    }

    /**
     * Test for toList method.
     */
    @Test
    public void testToList() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> expectedList = Arrays.asList("John", "Doe", "30", "M");
        List<String> resultList = record.toList();

        assertSame(expectedList, resultList);
    }

    /**
     * Test for toMap method.
     */
    @Test
    public void testToMap() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "John");
        expectedMap.put("Age", "30");
        expectedMap.put("Gender", "M");

        Map<String, String> resultMap = record.toMap();

        assertSame(expectedMap, resultMap);
    }

    /**
     * Enum used for testing get method with enum.
     */
    enum Name {
        NAME, UNKNOWN
    }

    enum Age {
        AGE
    }

    enum Unknown {
        UNEXPECTED
    }
}