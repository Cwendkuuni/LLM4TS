import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVRecord;

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
        String comment = "# Comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetValueByIndex() {
        Assert.assertEquals("value1", csvRecord.get(0));
        Assert.assertEquals("value2", csvRecord.get(1));
        Assert.assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetValueByName() {
        Assert.assertEquals("value1", csvRecord.get("column1"));
        Assert.assertEquals("value2", csvRecord.get("column2"));
        Assert.assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueByNameNoMapping() {
        csvRecord = new CSVRecord(new String[]{}, null, "# Comment", 1);
        csvRecord.get("column1");
    }

    @Test
    public void testIsConsistent() {
        Assert.assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithNoMapping() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "# Comment", 1);
        Assert.assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithInconsistentRecord() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "# Comment", 1);
        Assert.assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Assert.assertTrue(csvRecord.isMapped("column1"));
        Assert.assertTrue(csvRecord.isMapped("column2"));
        Assert.assertTrue(csvRecord.isMapped("column3"));
        Assert.assertFalse(csvRecord.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        Assert.assertTrue(csvRecord.isSet("column1"));
        Assert.assertTrue(csvRecord.isSet("column2"));
        Assert.assertTrue(csvRecord.isSet("column3"));
        Assert.assertFalse(csvRecord.isSet("column4"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        Assert.assertEquals(3, count);
    }

    @Test
    public void testValues() {
        String[] expectedValues = {"value1", "value2", "value3"};
        Assert.assertArrayEquals(expectedValues, csvRecord.values());
    }

    @Test
    public void testGetComment() {
        Assert.assertEquals("# Comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        Assert.assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testSize() {
        Assert.assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("[value1, value2, value3]", csvRecord.toString());
    }
}