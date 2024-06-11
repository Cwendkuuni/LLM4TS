import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstructor() {
        String[] values = new String[]{"John", "Doe", "30", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertArrayEquals(values, record.values());
        Assert.assertEquals(mapping, record.mapping);
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertNull(record.getComment());
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testGetMethod() {
        String[] values = new String[]{"John", "Doe", "30", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Basic get tests
        Assert.assertEquals("John", record.get(0));
        Assert.assertEquals("Doe", record.get(1));
        Assert.assertEquals("30", record.get(2));
        Assert.assertEquals("New York", record.get(3));
        Assert.assertEquals(30, Integer.parseInt(record.get(2))); // Integer test

        // Get by enum test
        Assert.assertEquals("John", record.get(Enum.NAME)); // Enum test

        // Get by name test
        Assert.assertEquals("Doe", record.get("Age")); // Mapped name test
        Assert.assertEquals("New York", record.get("City")); // Mapped name test
        Assert.assertNull(record.get("Unknown")); // Unknown name test

        // Get comment test
        Assert.assertNull(record.getComment());

        // Get record number test
        Assert.assertEquals(1, record.getRecordNumber());

        // Get inconsistent record test
        CSVRecord inconsistentRecord = new CSVRecord(new String[0], mapping, null, 1);
        Assert.assertFalse(inconsistentRecord.isConsistent());
        try {
            inconsistentRecord.get("Name");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsMappedAndIsSet() {
        String[] values = new String[]{"John", "Doe", "30", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Is mapped test
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertTrue(record.isMapped("City"));
        Assert.assertFalse(record.isMapped("Unknown"));

        // Is set test
        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertTrue(record.isSet("City"));
        Assert.assertFalse(record.isSet("Unknown"));

        // Empty record test
        CSVRecord emptyRecord = new CSVRecord(new String[0], null, null, 1);
        Assert.assertFalse(emptyRecord.isMapped("Name"));
        Assert.assertFalse(emptyRecord.isSet("Name"));
    }

    @Test
    public void testSizeAndIterator() {
        String[] values = new String[]{"John", "Doe", "30", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Size test
        Assert.assertEquals(4, record.size());

        // Iterator test
        Iterator<String> iterator = record.iterator();
        List<String> expectedValues = Arrays.asList(record.values());
        for (int i = 0; i < record.size(); i++) {
            Assert.assertEquals(expectedValues.get(i), iterator.next());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutInAndToMap() {
        String[] values = new String[]{"John", "Doe", "30", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // PutIn test
        Map<String, String> result = record.putIn(new HashMap<String, String>(values.length));
        Map<String, String> expected = new HashMap<>();
        expected.put("Name", "John");
        expected.put("Age", "30");
        expected.put("City", "New York");
        Assert.assertEquals(expected, result);

        // ToMap test
        Map<String, String> map = record.toMap();
        Assert.assertEquals(expected, map);
    }
}