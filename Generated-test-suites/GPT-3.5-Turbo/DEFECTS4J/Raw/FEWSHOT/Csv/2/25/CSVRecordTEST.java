package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGet() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                        null, null, 1);

        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetWithName() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                        null, null, 1);

        assertNull(record.get("name1"));
        assertNull(record.get("name2"));
        assertNull(record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                        null, null, 1);

        record.get("name");
    }

    @Test
    public void testGetWithNameMapping() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                        null, null, 1);

        assertNull(record.get("name"));

        record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                Map.of("name1", 0, "name2", 1, "name3", 2),
                                null, 1);

        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNameMappingInconsistent() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"},
                                        Map.of("name1", 0),
                                        null, 1);

        record.get("name");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                        null, null, 1);

        assertEquals(true, record.isConsistent());

        record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                Map.of("name1", 0, "name2", 1, "name3", 2),
                                null, 1);
        
        assertEquals(true, record.isConsistent());

        record = new CSVRecord(new String[] {"value1", "value2"},
                                Map.of("name1", 0),
                                null, 1);
        
        assertEquals(false, record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                        null, null, 1);

        assertEquals(false, record.isMapped("name1"));
        assertEquals(false, record.isMapped("name2"));
        assertEquals(false, record.isMapped("name3"));

        record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                Map.of("name1", 0, "name2", 1, "name3", 2),
                                null, 1);

        assertEquals(true, record.isMapped("name1"));
        assertEquals(true, record.isMapped("name2"));
        assertEquals(true, record.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                        null, null, 1);

        assertEquals(false, record.isSet("name1"));
        assertEquals(false, record.isSet("name2"));
        assertEquals(false, record.isSet("name3"));

        record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                Map.of("name1", 0, "name2", 1, "name3", 2),
                                null, 1);

        assertEquals(true, record.isSet("name1"));
        assertEquals(true, record.isSet("name2"));
        assertEquals(true, record.isSet("name3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                        null, null, 1);

        String[] expectedValues = {"value1", "value2", "value3"};
        int i = 0;
        for (String value : record) {
            assertEquals(expectedValues[i], value);
            i++;
        }
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                        null, null, 1);

        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                        null, null, 1);

        assertNull(record.getComment());

        record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                null, "This is a comment", 1);

        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                        null, null, 1);

        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"},
                                        null, null, 1);

        assertEquals(3, record.size());
    }
}