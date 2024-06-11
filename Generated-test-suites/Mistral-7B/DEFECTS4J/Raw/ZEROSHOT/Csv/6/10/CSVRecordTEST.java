import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordEmpty() {
        CSVRecord record = new CSVRecord(null, null, null, 0L);
        Assert.assertNull(record.getComment());
        Assert.assertNull(record.getMapping());
        Assert.assertArrayEquals(new String[0], record.values());
        Assert.assertEquals(0, record.getRecordNumber());
        Assert.assertTrue(record.isConsistent());
        Assert.assertFalse(record.isMapped("Name"));
    }

    @Test
    public void testCSVRecordSimple() {
        String[] values = new String[]{"Alice", "Smith", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        Assert.assertEquals("Alice", record.get("Name"));
        Assert.assertEquals("Smith", record.get("Surname"));
        Assert.assertEquals("30", record.get("Age"));
        Assert.assertEquals("Alice,Smith,30", record.toString());
        Assert.assertEquals("30", record.get(2));
        Assert.assertNull(record.getComment());
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertTrue(record.isConsistent());
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Surname"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertFalse(record.isSet("Gender"));

        Map<String, String> map = record.toMap();
        Assert.assertEquals("Alice", map.get("Name"));
        Assert.assertEquals("Smith", map.get("Surname"));
        Assert.assertEquals("30", map.get("Age"));
    }

    @Test
    public void testCSVRecordWithComment() {
        String[] values = new String[]{"Alice", "Smith", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);

        CSVRecord record = new CSVRecord(values, mapping, "Comment", 1L);

        Assert.assertEquals("Comment", record.getComment());
        Assert.assertEquals("Alice", record.get("Name"));
        Assert.assertEquals("Smith", record.get("Surname"));
        Assert.assertEquals("30", record.get("Age"));
        Assert.assertEquals("Alice,Smith,30", record.toString());
        Assert.assertEquals("30", record.get(2));
        Assert.assertNull(record.getComment());
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertTrue(record.isConsistent());
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Surname"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertFalse(record.isSet("Gender"));

        Map<String, String> map = record.toMap();
        Assert.assertEquals("Alice", map.get("Name"));
        Assert.assertEquals("Smith", map.get("Surname"));
        Assert.assertEquals("30", map.get("Age"));
    }

    @Test
    public void testCSVRecordWithNullValues() {
        String[] values = new String[]{"Alice", null, "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        Assert.assertEquals("Alice", record.get("Name"));
        Assert.assertNull(record.get("Surname"));
        Assert.assertEquals("30", record.get("Age"));
        Assert.assertEquals("Alice,null,30", record.toString());
        Assert.assertEquals("30", record.get(2));
        Assert.assertNull(record.getComment());
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertTrue(record.isConsistent());
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Surname"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertFalse(record.isSet("Gender"));

        Map<String, String> map = record.toMap();
        Assert.assertEquals("Alice", map.get("Name"));
        Assert.assertNull(map.get("Surname"));
        Assert.assertEquals("30", map.get("Age"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInvalidName() {
        String[] values = new String[]{"Alice", "Smith", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);
        mapping.put("InvalidName", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        record.get("InvalidName");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCSVRecordWithInvalidIndex() {
        String[] values = new String[]{"Alice", "Smith", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        record.get(3);
    }

    @Test
    public void testCSVRecordWithComplexValues() {
        String[] values = new String[]{"Alice,Smith,30", "Bob,Doe,40"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Surname", 1);
        mapping.put("Age", 2);

        CSVFormat format = new CSVFormat();
        CSVParser parser = new CSVParser(new StringReader(format.formatRecord(values)), format);

        CSVRecord record = parser.nextRecord();

        Assert.assertEquals("Alice", record.get("Name"));
        Assert.assertEquals("Smith", record.get("Surname"));
        Assert.assertEquals("30", record.get("Age"));
        Assert.assertNull(record.getComment());
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertTrue(record.isConsistent());
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Surname"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertFalse(record.isSet("Gender"));

        Map<String, String> map = record.toMap();
        Assert.assertEquals("Alice", map.get("Name"));
        Assert.assertEquals("Smith", map.get("Surname"));
        Assert.assertEquals("30", map.get("Age"));

        CSVRecord nextRecord = parser.nextRecord();

        Assert.assertEquals("Bob", nextRecord.get("Name"));
        Assert.assertEquals("Doe", nextRecord.get("Surname"));
        Assert.assertEquals("40", nextRecord.get("Age"));
        Assert.assertNull(nextRecord.getComment());
        Assert.assertEquals(2, nextRecord.getRecordNumber());
        Assert.assertTrue(nextRecord.isConsistent());
        Assert.assertTrue(nextRecord.isMapped("Name"));
        Assert.assertTrue(nextRecord.isMapped("Surname"));
        Assert.assertTrue(nextRecord.isMapped("Age"));
        Assert.assertFalse(nextRecord.isSet("Gender"));

        map = nextRecord.toMap();
        Assert.assertEquals("Bob", map.get("Name"));
        Assert.assertEquals("Doe", map.get("Surname"));
        Assert.assertEquals("40", map.get("Age"));
    }
}