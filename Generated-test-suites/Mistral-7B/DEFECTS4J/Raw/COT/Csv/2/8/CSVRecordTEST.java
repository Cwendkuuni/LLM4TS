import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testConstructor_ValidArguments() {
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertArrayEquals(values, csvRecord.values());
        Assert.assertEquals(mapping, csvRecord.mapping);
        Assert.assertNull(csvRecord.comment);
        Assert.assertEquals(1, csvRecord.recordNumber);
    }

    @Test
    public void testGet_ValidIndex() {
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        String name = csvRecord.get(0);
        Assert.assertEquals("Alice", name);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_InvalidIndex() {
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        csvRecord.get(values.length);
    }

    @Test
    public void testGet_ValidName() {
        String[] values = new String[]{"Name:Alice,LastName:Smith,Email:alice.smith@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        String email = csvRecord.get("Email");
        Assert.assertEquals("alice.smith@example.com", email);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_InvalidName() {
        String[] values = new String[]{"Name:Alice,LastName:Smith,Email:alice.smith@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        csvRecord.get("InvalidName");
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"Name:Alice,LastName:Smith,Email:alice.smith@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Name:Alice,LastName:Smith,Email:alice.smith@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(csvRecord.isMapped("Name"));
        Assert.assertTrue(csvRecord.isMapped("LastName"));
        Assert.assertTrue(csvRecord.isMapped("Email"));
        Assert.assertFalse(csvRecord.isMapped("InvalidName"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"Name:Alice,LastName:Smith,Email:alice.smith@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(csvRecord.isSet("Name"));
        Assert.assertTrue(csvRecord.isSet("LastName"));
        Assert.assertTrue(csvRecord.isSet("Email"));
        Assert.assertFalse(csvRecord.isSet("InvalidName"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"Name:Alice,LastName:Smith,Email:alice.smith@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Iterator<String> iterator = csvRecord.iterator();

        Assert.assertEquals("Alice", iterator.next());
        Assert.assertEquals("Smith", iterator.next());
        Assert.assertEquals("alice.smith@example.com", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"Name:Alice,LastName:Smith,Email:alice.smith@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"Name:Alice,LastName:Smith,Email:alice.smith@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        String expected = "[Name:Alice, LastName:Smith, Email:alice.smith@example.com]";

        Assert.assertEquals(expected, csvRecord.toString());
    }
}