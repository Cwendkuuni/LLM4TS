package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for CSVRecord.
 */
public class CSVRecordTest {

    @Test
    public void testCSVRecordConstructor() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }};
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals(values, csvRecord.values());
        Assert.assertEquals(mapping, csvRecord.mapping);
        Assert.assertEquals(comment, csvRecord.getComment());
        Assert.assertEquals(recordNumber, csvRecord.getRecordNumber());
    }

    @Test
    public void testGetMethod() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "City", "New York"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 2);
            put("City", 4);
        }};
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals("John Doe", csvRecord.get(Enum.valueOf("Age")));
        Assert.assertEquals("30", csvRecord.get(2));
        Assert.assertEquals("New York", csvRecord.get("City"));
        Assert.assertNull(csvRecord.get("Unknown"));

        Assert.assertNull(csvRecord.getComment());
        Assert.assertEquals(1, csvRecord.getRecordNumber());
        Assert.assertTrue(csvRecord.isConsistent());
        Assert.assertTrue(csvRecord.isMapped("Name"));
        Assert.assertTrue(csvRecord.isMapped("Age"));
        Assert.assertTrue(csvRecord.isMapped("City"));
        Assert.assertFalse(csvRecord.isMapped("Unknown"));
        Assert.assertTrue(csvRecord.isSet("Name"));
        Assert.assertTrue(csvRecord.isSet("Age"));
        Assert.assertTrue(csvRecord.isSet("City"));
        Assert.assertFalse(csvRecord.isSet("Unknown"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMethodWithNoMapping() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "City", "New York"};
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, null, comment, recordNumber);

        csvRecord.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodWithInvalidIndex() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "City", "New York"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 2);
            put("City", 4);
        }};
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        csvRecord.get(5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodWithInvalidName() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "City", "New York"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 2);
            put("City", 4);
        }};
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        csvRecord.get("Unknown");
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "City", "New York", "#This is a comment"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 2);
            put("City", 4);
        }};
        String comment = "#This is a comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals("#This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "City", "New York"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 2);
            put("City", 4);
        }};
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "City", "New York"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 2);
            put("City", 4);
        }};
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertTrue(csvRecord.isConsistent());

        String[] valuesInvalid = new String[]{"Name", "John Doe", "Age", "30", "City", "New York", "Invalid"};
        Map<String, Integer> mappingInvalid = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 2);
            put("City", 4);
            put("Invalid", 6);
        }};

        CSVRecord csvRecordInvalid = new CSVRecord(valuesInvalid, mappingInvalid, null, 1);

        Assert.assertFalse(csvRecordInvalid.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "City", "New York"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 2);
            put("City", 4);
        }};
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertTrue(csvRecord.isMapped("Name"));
        Assert.assertTrue(csvRecord.isMapped("Age"));
        Assert.assertTrue(csvRecord.isMapped("City"));
        Assert.assertFalse(csvRecord.isMapped("Unknown"));

        String[] valuesNoMapping = new String[]{"Name", "John Doe", "Age"};
        Map<String, Integer> mappingNoMapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 1);
        }};

        CSVRecord csvRecordNoMapping = new CSVRecord(valuesNoMapping, mappingNoMapping, null, 1);

        Assert.assertFalse(csvRecordNoMapping.isMapped("City"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "City", "New York"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 2);
            put("City", 4);
        }};
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertTrue(csvRecord.isSet("Name"));
        Assert.assertTrue(csvRecord.isSet("Age"));
        Assert.assertTrue(csvRecord.isSet("City"));
        Assert.assertFalse(csvRecord.isSet("Unknown"));

        String[] valuesNoSet = new String[]{"Name", "John Doe", "City", "New York"};
        Map<String, Integer> mappingNoSet = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("City", 2);
        }};

        CSVRecord csvRecordNoSet = new CSVRecord(valuesNoSet, mappingNoSet, null, 1);

        Assert.assertFalse(csvRecordNoSet.isSet("Age"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "City", "New York"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 2);
            put("City", 4);
        }};
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Iterator<String> iterator = csvRecord.iterator();

        List<String> expected = Arrays.asList(csvRecord.values());
        List<String> actual = Arrays.asList(iterator.toArray(new String[expected.size()]));

        Iterator<String> expectedIterator = expected.iterator();
        while (expectedIterator.hasNext()) {
            String expectedNext = expectedIterator.next();
            String actualNext = iterator.next();
            Assert.assertEquals(expectedNext, actualNext);
        }
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "City", "New York"};
        Map<String, String> expected = new HashMap<String, String>(){{
            put("Name", "John Doe");
            put("Age", "30");
            put("City", "New York");
        }};
        Map<String, String> map = new HashMap<String, String>();

        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        map = csvRecord.putIn(map);

        Assert.assertEquals(expected, map);
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "City", "New York"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 2);
            put("City", 4);
        }};

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals(values.length, csvRecord.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"Name", "John Doe", "Age", "30", "City", "New York"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("Name", 0);
            put("Age", 2);
            put("City", 4);
        }};
        String comment = null;
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals("[Name, John Doe, Age, 30, City, New York]", csvRecord.toString());
    }
}