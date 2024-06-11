import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetEnum() {
        String value = csvRecord.get(Enum.COL1);
        Assert.assertEquals("value1", value);
    }

    @Test
    public void testGetByIndex() {
        String value = csvRecord.get(0);
        Assert.assertEquals("value1", value);
    }

    @Test
    public void testGetByName() {
        String value = csvRecord.get("col2");
        Assert.assertEquals("value2", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByInvalidNameWithNoMapping() {
        csvRecord.get("col4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidNameWithMapping() {
        csvRecord.get("col4");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByIndexOutOfBounds() {
        csvRecord.get(3);
    }

    @Test
    public void testGetComment() {
        String comment = csvRecord.getComment();
        Assert.assertEquals("This is a comment", comment);
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = csvRecord.getRecordNumber();
        Assert.assertEquals(1, recordNumber);
    }

    @Test
    public void testIsConsistent() {
        boolean consistent = csvRecord.isConsistent();
        Assert.assertTrue(consistent);
    }

    @Test
    public void testIsMappedByValidName() {
        boolean mapped = csvRecord.isMapped("col2");
        Assert.assertTrue(mapped);
    }

    @Test
    public void testIsMappedByInvalidName() {
        boolean mapped = csvRecord.isMapped("col4");
        Assert.assertFalse(mapped);
    }

    @Test
    public void testIsSetByValidName() {
        boolean set = csvRecord.isSet("col2");
        Assert.assertTrue(set);
    }

    @Test
    public void testIsSetByInvalidName() {
        boolean set = csvRecord.isSet("col4");
        Assert.assertFalse(set);
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
    public void testPutIn() {
        Map<String, String> map = csvRecord.putIn(new HashMap<>());
        Assert.assertEquals(3, map.size());
        Assert.assertEquals("value1", map.get("col1"));
        Assert.assertEquals("value2", map.get("col2"));
        Assert.assertEquals("value3", map.get("col3"));
    }

    @Test
    public void testSize() {
        int size = csvRecord.size();
        Assert.assertEquals(3, size);
    }

    @Test
    public void testToList() {
        List<String> list = csvRecord.toList();
        Assert.assertEquals(3, list.size());
        Assert.assertEquals("value1", list.get(0));
        Assert.assertEquals("value2", list.get(1));
        Assert.assertEquals("value3", list.get(2));
    }

    @Test
    public void testToString() {
        String str = csvRecord.toString();
        Assert.assertEquals("[value1, value2, value3]", str);
    }

    @Test
    public void testValues() {
        String[] values = csvRecord.values();
        Assert.assertEquals(3, values.length);
        Assert.assertEquals("value1", values[0]);
        Assert.assertEquals("value2", values[1]);
        Assert.assertEquals("value3", values[2]);
    }
}