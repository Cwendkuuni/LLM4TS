import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setup() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;

        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }
    
    // Testing get(Enum<?> e) method
    @Test
    public void testGetEnum() {
        Assert.assertEquals("value1", csvRecord.get(TestEnum.ENUM1));
        Assert.assertEquals("value2", csvRecord.get(TestEnum.ENUM2));
        Assert.assertEquals("value3", csvRecord.get(TestEnum.ENUM3));
    }

    // Testing get(int i) method
    @Test
    public void testGetByIndex() {
        Assert.assertEquals("value1", csvRecord.get(0));
        Assert.assertEquals("value2", csvRecord.get(1));
        Assert.assertEquals("value3", csvRecord.get(2));
    }

    // Testing get(String name) method
    @Test
    public void testGetByName() {
        Assert.assertEquals("value1", csvRecord.get("column1"));
        Assert.assertEquals("value2", csvRecord.get("column2"));
        Assert.assertEquals("value3", csvRecord.get("column3"));
    }

    // Testing getComment() method
    @Test
    public void testGetComment() {
        Assert.assertEquals("This is a comment", csvRecord.getComment());
    }

    // Testing getRecordNumber() method
    @Test
    public void testGetRecordNumber() {
        Assert.assertEquals(1, csvRecord.getRecordNumber());
    }

    // Testing isConsistent() method
    @Test
    public void testIsConsistent() {
        Assert.assertTrue(csvRecord.isConsistent());
    }

    // Testing isMapped(String name) method
    @Test
    public void testIsMapped() {
        Assert.assertTrue(csvRecord.isMapped("column1"));
        Assert.assertTrue(csvRecord.isMapped("column2"));
        Assert.assertTrue(csvRecord.isMapped("column3"));
    }

    // Testing isSet(String name) method
    @Test
    public void testIsSet() {
        Assert.assertTrue(csvRecord.isSet("column1"));
        Assert.assertTrue(csvRecord.isSet("column2"));
        Assert.assertTrue(csvRecord.isSet("column3"));
    }

    // Helper enum for testing get(Enum<?> e) method
    private enum TestEnum {
        ENUM1,
        ENUM2,
        ENUM3
    }
    
    // Edge case testing when values array is null
     @Test
    public void testValuesArrayNull() {
        String[] values = null;
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;

        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals(null, csvRecord.get(0));
        Assert.assertEquals(null, csvRecord.get("column1"));
        Assert.assertFalse(csvRecord.isSet("column1"));
        Assert.assertFalse(csvRecord.isMapped("column1"));
    }
    
    // Error scenario testing when header mapping was not specified
     @Test(expected = IllegalStateException.class)
    public void testNoHeaderMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = "This is a comment";
        long recordNumber = 1;

        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        csvRecord.get("column1");
    }

}