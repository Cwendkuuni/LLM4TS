import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the CSVRecord class.
 */
public class CSVRecordTest {

    @Test
    public void testConstructor_Valid() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertArrayEquals(values, record.values());
        Assert.assertNotNull(record.mapping);
        Assert.assertEquals(3, record.size());
        Assert.assertNull(record.getComment());
        Assert.assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testConstructor_Empty() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 1);
        Assert.assertArrayEquals(new String[0], record.values());
        Assert.assertNull(record.mapping);
        Assert.assertEquals(0, record.size());
        Assert.assertNull(record.getComment());
        Assert.assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testGet_Valid() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals("Name", record.get(0));
        Assert.assertEquals("Age", record.get(1));
        Assert.assertEquals("City", record.get(2));
        Assert.assertNull(record.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_OutOfBounds() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get(3);
    }

    @Test
    public void testGet_ByName_Valid() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals("Name", record.get("Name"));
        Assert.assertEquals("Age", record.get("Age"));
        Assert.assertEquals("City", record.get("City"));
        Assert.assertNull(record.get("Unknown"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_ByName_InvalidMapping() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("Unknown");
    }

    @Test
    public void testIsConsistent_Valid() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_Invalid() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        mapping.put("Unknown", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped_Valid() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertTrue(record.isMapped("City"));
        Assert.assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testIsSet_Valid() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertTrue(record.isSet("City"));
        Assert.assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Iterator<String> iterator = record.iterator();
        Assert.assertEquals("Name", iterator.next());
        Assert.assertEquals("Age", iterator.next());
        Assert.assertEquals("City", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals("[Name, Age, City]", record.toString());
    }

    @Test
    public void testGetComment_Valid() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "Comment", 1);
        Assert.assertEquals("Comment", record.getComment());
    }

    @Test
    public void testGetComment_Null() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber_Valid() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals(1, record.getRecordNumber());
    }

}