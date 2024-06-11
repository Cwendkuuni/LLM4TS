import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstruction() {
        // Test empty constructor
        CSVRecord record = new CSVRecord(null, null, null, -1);
        Assert.assertNull(record.values());
        Assert.assertNull(record.mapping());
        Assert.assertNull(record.getComment());
        Assert.assertEquals(-1, record.getRecordNumber());
        Assert.assertFalse(record.isConsistent());

        // Test constructor with values and mapping
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record1 = new CSVRecord(values, mapping, null, 1);
        Assert.assertArrayEquals(values, record1.values());
        Assert.assertEquals(mapping, record1.mapping());
        Assert.assertNull(record1.getComment());
        Assert.assertEquals(1, record1.getRecordNumber());
        Assert.assertTrue(record1.isConsistent());

        // Test constructor with values, mapping and comment
        String comment = "test comment";
        CSVRecord record2 = new CSVRecord(values, mapping, comment, 2);
        Assert.assertArrayEquals(values, record2.values());
        Assert.assertEquals(mapping, record2.mapping());
        Assert.assertEquals(comment, record2.getComment());
        Assert.assertEquals(2, record2.getRecordNumber());
        Assert.assertTrue(record2.isConsistent());

        // Test constructor with values, mapping, comment and record number
        CSVRecord record3 = new CSVRecord(values, mapping, comment, 3);
        Assert.assertArrayEquals(values, record3.values());
        Assert.assertEquals(mapping, record3.mapping());
        Assert.assertEquals(comment, record3.getComment());
        Assert.assertEquals(3, record3.getRecordNumber());
        Assert.assertTrue(record3.isConsistent());
    }

    @Test
    public void testGetMethod() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test get with int index
        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));

        // Test get with String name
        Assert.assertEquals("value1", record.get("name1"));
        Assert.assertEquals("value2", record.get("name2"));

        // Test get with Enum
        EnumTest enumTest = EnumTest.NAME1;
        Assert.assertEquals("value1", record.get(enumTest));

        // Test get with null input
        Assert.assertNull(record.get(null));
        Assert.assertNull(record.get("name3"));
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        String comment = "test comment";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1);
        Assert.assertEquals(comment, record.getComment());
        Assert.assertNull(record.getComment()); // Test with empty constructor
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertEquals(-1, new CSVRecord(null, null, null, -1).getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isConsistent());
        String[] wrongValues = new String[]{"name1", "value1", null};
        CSVRecord wrongRecord = new CSVRecord(wrongValues, mapping, null, 1);
        Assert.assertFalse(wrongRecord.isConsistent());
        CSVRecord emptyRecord = new CSVRecord(null, null, null, -1);
        Assert.assertTrue(emptyRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isMapped("name1"));
        Assert.assertTrue(record.isMapped("name2"));
        Assert.assertFalse(record.isMapped("name3"));
        CSVRecord recordWithoutMapping = new CSVRecord(values, null, null, 1);
        Assert.assertFalse(recordWithoutMapping.isMapped("name1"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isSet("name1"));
        Assert.assertTrue(record.isSet("name2"));
        Assert.assertFalse(record.isSet("name3"));
        String[] emptyValues = new String[]{"name1", null, "value2"};
        CSVRecord emptyRecord = new CSVRecord(emptyValues, mapping, null, 1);
        Assert.assertFalse(emptyRecord.isSet("name1"));
        Assert.assertFalse(emptyRecord.isSet("name2"));
        Assert.assertFalse(emptyRecord.isSet("name3"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Iterator<String> iterator = record.iterator();
        List<String> expectedValues = Arrays.asList(values);
        int i = 0;
        while (iterator.hasNext()) {
            Assert.assertEquals(expectedValues.get(i), iterator.next());
            i++;
        }
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 1);
        map = record.putIn(map);
        List<String> expectedValues = Arrays.asList(values);
        Assert.assertEquals(expectedValues.size(), map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Assert.assertEquals(expectedValues.get(entry.getKey().intValue()), entry.getValue());
        }
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals(values.length, record.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals("[name1, value1, value2]", record.toString());
    }

    @Test
    public void testValues() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertArrayEquals(values, record.values());
    }

    @Test
    public void testParseCSVFile() throws IOException {
        String csvData = "name1,name2,name3\n" +
                "value1_1,value2_1,value3_1\n" +
                "value1_2,value2_2,value3_2\n";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = new CSVParser(new StringReader(csvData), format);
        CSVRecord record = parser.getRecord();
        Assert.assertNotNull(record);
        Assert.assertEquals(3, record.size());
        Assert.assertEquals("name1", record.get("name1"));
        Assert.assertEquals("value1_1", record.get("name1"));
        Assert.assertEquals("value2_1", record.get("name2"));
        Assert.assertEquals("value3_1", record.get("name3"));

        record = parser.getRecord();
        Assert.assertNotNull(record);
        Assert.assertEquals(3, record.size());
        Assert.assertEquals("name1", record.get("name1"));
        Assert.assertEquals("value1_2", record.get("name1"));
        Assert.assertEquals("value2_2", record.get("name2"));
        Assert.assertEquals("value3_2", record.get("name3"));

        record = parser.getRecord();
        Assert.assertNull(record);
    }

    enum EnumTest {
        NAME1, NAME2
    }
}