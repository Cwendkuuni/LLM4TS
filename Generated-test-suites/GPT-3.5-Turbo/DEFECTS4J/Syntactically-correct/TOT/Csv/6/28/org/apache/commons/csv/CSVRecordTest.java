import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setup() {
        String[] values = { "John", "Doe", "30" };
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        mapping.put("age", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetEnum() {
        String expected = "John";
        String actual = record.get(0);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIndex() {
        String expected = "Doe";
        String actual = record.get(1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {
        String expected = "30";
        String actual = record.get(2);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNoMapping() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 1);
        record.get("firstName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidMapping() {
        record.get("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[2], null, null, 1);
        record.get(2);
    }

    @Test
    public void testGetComment() {
        String expected = "This is a comment";
        String actual = record.getComment();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetRecordNumber() {
        long expected = 1;
        long actual = record.getRecordNumber();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsConsistent() {
        boolean expected = true;
        boolean actual = record.isConsistent();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsMapped() {
        boolean expected = true;
        boolean actual = record.isMapped("firstName");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNotMapped() {
        boolean expected = false;
        boolean actual = record.isMapped("invalid");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsSet() {
        boolean expected = true;
        boolean actual = record.isSet("firstName");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNotSet() {
        boolean expected = false;
        boolean actual = record.isSet("lastName");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIterator() {
        String[] expected = { "John", "Doe", "30" };
        int i = 0;
        for (String value : record) {
            Assert.assertEquals(expected[i++], value);
        }
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<String, String>();
        map = record.putIn(map);
        Assert.assertEquals(record.get("firstName"), map.get("firstName"));
        Assert.assertEquals(record.get("lastName"), map.get("lastName"));
        Assert.assertEquals(record.get("age"), map.get("age"));
    }

    @Test
    public void testSize() {
        int expected = 3;
        int actual = record.size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToList() {
        List<String> expected = Arrays.asList("John", "Doe", "30");
        List<String> actual = record.toList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToMap() {
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("firstName", "John");
        expected.put("lastName", "Doe");
        expected.put("age", "30");
        Map<String, String> actual = record.toMap();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "[John, Doe, 30]";
        String actual = record.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testValues() {
        String[] expected = { "John", "Doe", "30" };
        String[] actual = record.values();
        Assert.assertArrayEquals(expected, actual);
    }
}