import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testConstructors() {
        // Test constructor with valid arguments
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Surname", 1, "Email", 2);
        String comment = "Record with valid data";
        long recordNumber = 1;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Assert.assertEquals("Alice", record.get(0));
        Assert.assertEquals("Smith", record.get(1));
        Assert.assertEquals("alice.smith@example.com", record.get(2));
        Assert.assertEquals(mapping, record.mapping);
        Assert.assertEquals(comment, record.comment);
        Assert.assertEquals(recordNumber, record.recordNumber);
        Assert.assertArrayEquals(values, record.values());

        // Test constructor with null mapping
        values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        CSVRecord nullMappingRecord = new CSVRecord(values, null, comment, recordNumber);
        Assert.assertNull(nullMappingRecord.mapping);
        Assert.assertEquals(comment, nullMappingRecord.comment);
        Assert.assertEquals(recordNumber, nullMappingRecord.recordNumber);
        Assert.assertArrayEquals(values, nullMappingRecord.values());
    }

    @Test
    public void testGet() {
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Surname", 1, "Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("Alice", record.get(0));
        Assert.assertEquals("Smith", record.get(1));
        Assert.assertEquals("alice.smith@example.com", record.get(2));
        Assert.assertEquals(null, record.get(3));
    }

    @Test
    public void testGetInvalidName() {
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Surname", 1, "Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertNull(record.get("InvalidName"));
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Surname", 1, "Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isConsistent());

        values = new String[]{"Alice", "Smith", "alice.smith@example.com", "InvalidValue"};
        record = new CSVRecord(values, mapping, null, 1);
        Assert.assertFalse(record.isConsistent());

        mapping = Map.of("Name", 0, "Surname", 1);
        values = new String[]{"Alice", "Smith"};
        record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isConsistent());

        mapping = Map.of("Name", 0, "Surname", 1, "InvalidColumn", 2);
        values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        record = new CSVRecord(values, mapping, null, 1);
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Surname", 1, "Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Surname"));
        Assert.assertTrue(record.isMapped("Email"));
        Assert.assertFalse(record.isMapped("InvalidName"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Surname", 1, "Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Surname"));
        Assert.assertTrue(record.isSet("Email"));
        Assert.assertFalse(record.isSet("InvalidName"));

        values = new String[]{"Alice", "Smith", null};
        record = new CSVRecord(values, mapping, null, 1);
        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Surname"));
        Assert.assertFalse(record.isSet("Email"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Iterator<String> iterator = record.iterator();
        Assert.assertEquals("Alice", iterator.next());
        Assert.assertEquals("Smith", iterator.next());
        Assert.assertEquals("alice.smith@example.com", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Surname", 1, "Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertEquals("[Alice, Smith, alice.smith@example.com]", record.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidIndex() {
        String[] values = new String[]{"Alice", "Smith", "alice.smith@example.com"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Surname", 1, "Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get(-1);
    }
}