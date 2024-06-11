import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        String[] values = new String[]{"Alice", "25", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertNotNull(record);
        Assert.assertEquals(values, record.values());
        Assert.assertEquals(mapping, record.mapping);
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertNull(record.getComment());
        Assert.assertTrue(record.isConsistent());

        String name = record.get("name");
        Assert.assertEquals("Alice", name);

        int age = Integer.parseInt(record.get("age"));
        Assert.assertEquals(25, age);

        String email = record.get("email");
        Assert.assertEquals("alice@example.com", email);

        Assert.assertEquals("Alice", record.get(0));
        Assert.assertEquals("25", record.get(1));
        Assert.assertEquals("alice@example.com", record.get(2));

        String comment = record.getComment();
        Assert.assertNull(comment);
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordNoMapping() {
        String[] values = new String[]{"Alice", "25", "alice@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Assert.assertNull(record.get("name"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordInvalidMapping() {
        String[] values = new String[]{"Alice", "25", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 2); //Invalid index

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String name = record.get("name");
        Assert.assertEquals("Alice", name);

        Assert.assertThrows(IllegalArgumentException.class, () -> record.get("age"));
    }

    @Test
    public void testCSVRecordGetNullValue() {
        String[] values = new String[]{"Alice", null, "alice@example.com"};
        CSVFormat format = new CSVFormat();
        format.setNullString("NA");

        CSVRecord record = new CSVRecord(values, null, null, 1, format);

        String name = record.get("name");
        Assert.assertEquals("Alice", name);

        String age = record.get("age");
        Assert.assertEquals("NA", age);
    }

    @Test
    public void testCSVRecordPutInMap() {
        String[] values = new String[]{"Alice", "25", "alice@example.com"};
        Map<String, String> map = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 1);

        map = record.putIn(map);

        Assert.assertEquals("Alice", map.get("name"));
        Assert.assertEquals("25", map.get("age"));
        Assert.assertEquals("alice@example.com", map.get("email"));
    }

    @Test
    public void testCSVRecordIsMapped() {
        String[] values = new String[]{"Alice", "25", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isMapped("name"));
        Assert.assertTrue(record.isMapped("age"));
        Assert.assertTrue(record.isMapped("email"));

        Assert.assertFalse(record.isMapped("unknown"));
    }

    @Test
    public void testCSVRecordIsSet() {
        String[] values = new String[]{"Alice", "25", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isSet("name"));
        Assert.assertTrue(record.isSet("age"));
        Assert.assertTrue(record.isSet("email"));

        Assert.assertFalse(record.isSet("unknown"));
    }

    @Test
    public void testCSVRecordSize() {
        String[] values = new String[]{"Alice", "25", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testCSVRecordToMap() {
        String[] values = new String[]{"Alice", "25", "alice@example.com"};
        Map<String, String> map = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 1);

        map = record.toMap();

        Assert.assertEquals("Alice", map.get("name"));
        Assert.assertEquals("25", map.get("age"));
        Assert.assertEquals("alice@example.com", map.get("email"));
    }
}