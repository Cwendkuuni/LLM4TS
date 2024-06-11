import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() {
        // Test empty CSVRecord
        CSVRecord emptyRecord = new CSVRecord(null, null, null, 0L);
        Assert.assertArrayEquals(new String[0], emptyRecord.values());
        Assert.assertNull(emptyRecord.getComment());
        Assert.assertFalse(emptyRecord.isConsistent());
        Assert.assertFalse(emptyRecord.isMapped("test"));
        Assert.assertFalse(emptyRecord.isSet("test"));
        Assert.assertFalse(emptyRecord.iterator().hasNext());
        Assert.assertEquals(0, emptyRecord.size());
        Assert.assertEquals("[]", emptyRecord.toString());

        // Test CSVRecord with values
        CSVFormat format = new CSVFormat();
        String[] values = new String[]{"John", "Doe", "30", "male"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        Assert.assertArrayEquals(values, record.values());
        Assert.assertNull(record.getComment());
        Assert.assertTrue(record.isConsistent());
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isSet("Name"));
        Assert.assertEquals("John", record.get("Name"));
        Assert.assertEquals("Doe", record.get("Age"));
        Assert.assertNull(record.get("Sex"));
        Assert.assertEquals(3, record.size());
        Assert.assertEquals("[John, Doe, 30]", record.toString());
        Assert.assertFalse(record.iterator().hasNext());

        // Test CSVRecord with comment
        String comment = "Test comment";
        CSVRecord recordWithComment = new CSVRecord(values, null, comment, 1L);
        Assert.assertEquals(comment, recordWithComment.getComment());

        // Test CSVRecord with mapping
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Sex", 2);
        CSVRecord recordWithMapping = new CSVRecord(values, mapping, comment, 1L);
        Assert.assertArrayEquals(values, recordWithMapping.values());
        Assert.assertEquals(comment, recordWithMapping.getComment());
        Assert.assertTrue(recordWithMapping.isConsistent());
        Assert.assertTrue(recordWithMapping.isMapped("Name"));
        Assert.assertTrue(recordWithMapping.isSet("Name"));
        Assert.assertEquals("John", recordWithMapping.get("Name"));
        Assert.assertEquals("30", recordWithMapping.get("Age"));
        Assert.assertNull(recordWithMapping.get("Name2"));
        Assert.assertEquals(3, recordWithMapping.size());
        Assert.assertFalse(recordWithMapping.iterator().hasNext());

        // Test get with null values
        CSVFormat formatWithNullValues = new CSVFormat(CSVFormat.DEFAULT.withNullString(""));
        String[] valuesWithNull = new String[]{"John", "Doe", "", "male"};
        CSVRecord recordWithNull = new CSVRecord(valuesWithNull, null, null, 1L);
        Assert.assertEquals("", recordWithNull.get(""));

        // Test get with null mapping
        CSVRecord recordWithNullMapping = new CSVRecord(values, null, null, 1L);
        Assert.assertNull(recordWithNullMapping.get("Name"));

        // Test get with inconsistent record
        String[] valuesInconsistent = new String[]{"John", "Doe", "30", "male", "test"};
        CSVRecord recordInconsistent = new CSVRecord(valuesInconsistent, null, null, 1L);
        Assert.assertFalse(recordInconsistent.isConsistent());
        Assert.assertEquals(5, recordInconsistent.size());
        Assert.assertTrue(recordInconsistent.iterator().hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithUnmappedColumn() {
        String[] values = new String[]{"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        record.get("Sex");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithOutOfBoundsIndex() {
        String[] values = new String[]{"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        record.get(2);
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithNullValues() {
        String[] values = null;
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        record.get("Name");
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"John", "Doe"};
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "John");
        expectedMap.put("Age", "Doe");
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        Map<String, String> map = record.putIn(new HashMap<>());
        Assert.assertEquals(expectedMap, map);
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"John", "Doe"};
        String expectedString = "[John, Doe]";
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        Assert.assertEquals(expectedString, record.toString());
    }
}