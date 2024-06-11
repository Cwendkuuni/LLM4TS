import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test file for CSVRecord class.
 */
public class CSVRecordTest {

    @Test
    public void testCSVRecordConstruction() {
        String[] values = new String[]{"Alice", "Smith", "age:30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First name", 0);
        mapping.put("Last name", 1);
        mapping.put("Age", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertArrayEquals(values, record.values());
        Assert.assertEquals(mapping, record.mapping);
        Assert.assertNull(record.getComment());
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecordGetValueByIndex() {
        String[] values = new String[]{"Alice", "Smith", "age:30"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Assert.assertEquals("Alice", record.get(0));
        Assert.assertEquals("Smith", record.get(1));
        Assert.assertEquals("age:30", record.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCSVRecordGetValueIndexOutOfBounds() {
        String[] values = new String[]{"Alice", "Smith", "age:30"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get(values.length);
    }

    @Test
    public void testCSVRecordGetValueByName() {
        String[] values = new String[]{"First name,Last name,Age", "Alice,Smith,age:30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First name", 0);
        mapping.put("Last name", 1);
        mapping.put("Age", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("Alice", record.get("First name"));
        Assert.assertEquals("Smith", record.get("Last name"));
        Assert.assertEquals("age:30", record.get("Age"));
        Assert.assertNull(record.get("Unknown"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordGetValueByNameInvalidMapping() {
        String[] values = new String[]{"First name,Last name,Age", "Alice,Smith,age:30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First name", 0);
        mapping.put("Last name", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("Age"); // valid call, but following should fail

        record.get("Unknown");
    }

    @Test
    public void testCSVRecordIsConsistent() {
        String[] values = new String[]{"Alice,Smith,age:30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First name", 0);
        mapping.put("Last name", 1);
        mapping.put("Age", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isConsistent());

        String[] invalidValues = new String[]{"Alice,Smith"};
        CSVRecord invalidRecord = new CSVRecord(invalidValues, mapping, null, 1);
        Assert.assertFalse(invalidRecord.isConsistent());
    }

    @Test
    public void testCSVRecordIsMapped() {
        String[] values = new String[]{"First name,Last name,Age", "Alice,Smith,age:30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First name", 0);
        mapping.put("Last name", 1);
        mapping.put("Age", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isMapped("First name"));
        Assert.assertTrue(record.isMapped("Last name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertFalse(record.isMapped("Unknown"));
    }

    @Test   
    public void testCSVRecordIsSet() {
        String[] values = new String[]{"First name,Last name,Age", "Alice,Smith,age:30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First name", 0);
        mapping.put("Last name", 1);
        mapping.put("Age", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isSet("First name"));
        Assert.assertTrue(record.isSet("Last name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testCSVRecordIterator() {
        String[] values = new String[]{"Alice,Smith,age:30"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        List<String> recordedValues = Arrays.asList(values);
        Iterator<String> recordIterator = record.iterator();

        for (String value : recordedValues) {
            Assert.assertEquals(value, recordIterator.next());
        }
        Assert.assertFalse(recordIterator.hasNext());
    }

    @Test
    public void testCSVRecordComment() {
        String[] values = new String[]{"Alice,Smith,age:30", "#Comment for this record"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First name", 0);
        mapping.put("Last name", 1);
        mapping.put("Age", 2);

        CSVRecord record = new CSVRecord(values, mapping, "#Comment for this record", 1);

        Assert.assertEquals("#Comment for this record", record.getComment());
    }

    @Test
    public void testCSVRecordRecordNumber() {
        String[] values = new String[]{"Alice,Smith,age:30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First name", 0);
        mapping.put("Last name", 1);
        mapping.put("Age", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testCSVRecordSize() {
        String[] values = new String[]{"Alice,Smith,age:30"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testCSVRecordToString() {
        String[] values = new String[]{"Alice,Smith,age:30"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Assert.assertEquals("[Alice, Smith, age:30]", record.toString());
    }

}