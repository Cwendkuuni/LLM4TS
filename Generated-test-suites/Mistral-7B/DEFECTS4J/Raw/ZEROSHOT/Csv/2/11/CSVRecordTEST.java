import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertArrayEquals(values, record.values());
        Assert.assertEquals(mapping, record.mapping);
        Assert.assertNull(record.comment);
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testGetValueByIndex() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Assert.assertEquals("John", record.get(0));
        Assert.assertEquals("Doe", record.get(1));
        Assert.assertEquals("30", record.get(2));
        Assert.assertEquals("M", record.get(3));
        Assert.assertNull(record.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueByIndexOutOfBounds() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get(values.length);
    }

    @Test
    public void testGetValueByName() {
        String[] values = new String[]{"Name:John,Age:30,Gender:M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("John", record.get("Name"));
        Assert.assertEquals("30", record.get("Age"));
        Assert.assertEquals("M", record.get("Gender"));
        Assert.assertNull(record.get("Unknown"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueByNameInvalidMapping() {
        String[] values = new String[]{"Name:John,Age:30,Gender:M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("Gender");
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"John,Doe,30,M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsInconsistent() {
        String[] values = new String[]{"John,Doe,30,M,Unknown"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Name:John,Age:30,Gender:M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertTrue(record.isMapped("Gender"));
        Assert.assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"Name:John,Age:30,Gender:M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertTrue(record.isSet("Gender"));
        Assert.assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testGetIterator() {
        String[] values = new String[]{"John,Doe,30,M"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        List<String> expectedList = Arrays.asList(values);
        Iterator<String> iterator = record.iterator();

        Assert.assertEquals(expectedList, Arrays.asList(iterator.toArray()));
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"John,Doe,30,M"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        String expectedString = "[John, Doe, 30, M]";
        String actualString = record.toString();

        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Name:John,Age:30,Gender:M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"Name:John,Age:30,Gender:M", "Comment:Test Record"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, "Comment:Test Record", 1);

        Assert.assertEquals("Comment:Test Record", record.getComment());
    }
}