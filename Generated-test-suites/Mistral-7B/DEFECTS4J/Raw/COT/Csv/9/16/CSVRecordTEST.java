import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstructor() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        String comment = "comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals(Arrays.toString(values), Arrays.toString(csvRecord.values()));
        Assert.assertEquals(mapping, csvRecord.mapping);
        Assert.assertEquals(comment, csvRecord.getComment());
        Assert.assertEquals(recordNumber, csvRecord.getRecordNumber());
    }

    @Test
    public void testGetMethod() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        String comment = "comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals("value1", csvRecord.get(0));
        Assert.assertEquals("value2", csvRecord.get(1));
        Assert.assertEquals("value3", csvRecord.get(2));

        Assert.assertEquals("value1", csvRecord.get(Enum.valueOf("Column1", 0)));
        Assert.assertEquals("value2", csvRecord.get(Enum.valueOf("Column2", 0)));
        Assert.assertEquals("value3", csvRecord.get(Enum.valueOf("Column3", 0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodWithInvalidName() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        String comment = "comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        csvRecord.get("invalidName");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMethodWithoutMapping() {
        String[] values = new String[]{"value1", "value2", "value3"};
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, null, null, recordNumber);

        csvRecord.get("column1");
    }

    @Test
    public void testIsConsistentMethod() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        String comment = "comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertTrue(csvRecord.isConsistent());

        String[] valuesInvalidSize = new String[]{"value1", "value2"};
        CSVRecord csvRecordInvalidSize = new CSVRecord(valuesInvalidSize, mapping, comment, recordNumber);

        Assert.assertFalse(csvRecordInvalidSize.isConsistent());
    }

    @Test
    public void testIsMappedMethod() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        String comment = "comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertTrue(csvRecord.isMapped("column1"));
        Assert.assertTrue(csvRecord.isMapped("column2"));
        Assert.assertTrue(csvRecord.isMapped("column3"));

        String unMapped = "unmapped";
        Assert.assertFalse(csvRecord.isMapped(unMapped));
    }

    @Test
    public void testIsSetMethod() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        String comment = "comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertTrue(csvRecord.isSet("column1"));
        Assert.assertTrue(csvRecord.isSet("column2"));
        Assert.assertTrue(csvRecord.isSet("column3"));

        String[] valuesMissingValue = new String[]{"value1", null};
        CSVRecord csvRecordMissingValue = new CSVRecord(valuesMissingValue, mapping, comment, recordNumber);

        Assert.assertFalse(csvRecordMissingValue.isSet("column3"));
    }

    @Test
    public void testIteratorMethod() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        String comment = "comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        List<String> expectedList = Arrays.asList(values);

        Iterator<String> iterator = csvRecord.iterator();

        Assert.assertEquals(expectedList, Arrays.asList(iterator.next(), iterator.next(), iterator.next()));
    }

    @Test
    public void testPutInMethod() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, String> targetMap = new HashMap<>();

        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
        Map<String, Integer> mapping = csvRecord.mapping;

        csvRecord.putIn(targetMap);

        for (Map.Entry<String, Integer> entry : mapping.entrySet()) {
            Assert.assertEquals(entry.getValue(), targetMap.get(entry.getKey()).intValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutInMethodWithInvalidIndex() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        String comment = "comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Map<String, String> targetMap = new HashMap<>();

        targetMap.put("column1", "value4");
        csvRecord.putIn(targetMap);
    }

    @Test
    public void testSizeMethod() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        String comment = "comment";
        long recordNumber = 1;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals(values.length, csvRecord.size());
    }
}