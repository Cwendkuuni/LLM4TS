import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = new String[] {"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = new String[] {"Name:John,Age:30,City:New York", "Name:Anna,Age:25,City:Chicago"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("John", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals("New York", record.get("City"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        String[] values = new String[] {"Name", "Age"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        record.get("Name");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetOutOfBoundsIndex() {
        String[] values = new String[] {"Name", "Age"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        record.get(values.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUnknownColumn() {
        String[] values = new String[] {"Name", "Age"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("UnknownColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidMapping() {
        String[] values = new String[] {"Name:John,Age:30,City:New York", "Name:Anna,Age:25,City:Chicago"};
        Map<String, Integer> mapping = Map.of("Name", 2, "Age", 1, "City", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("Name");
    }

    @Test
    public void testIsConsistentTrue() {
        String[] values = new String[] {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentFalse() {
        String[] values = new String[] {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "City", 1, "Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMappedTrue() {
        String[] values = new String[] {"Name:John,Age:30,City:New York", "Name:Anna,Age:25,City:Chicago"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
    }

    @Test
    public void testIsMappedFalse() {
        String[] values = new String[] {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertFalse(record.isMapped("City"));
    }

    @Test
    public void testIsSetTrue() {
        String[] values = new String[] {"Name:John,Age:30,City:New York", "Name:Anna,Age:25,City:Chicago"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertFalse(record.isSet("City"));
    }

    @Test
    public void testIsSetFalse() {
        String[] values = new String[] {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertFalse(record.isSet("Name"));
        assertFalse(record.isSet("Age"));
        assertFalse(record.isSet("City"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[] {"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        Iterator<String> iterator = record.iterator();
        List<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        assertEquals(Arrays.asList("Name", "Age", "City"), result);
    }

    @Test
    public void testValues() {
        String[] values = new String[] {"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertArrayEquals(values, record.values());
    }

    @Test
    public void testComment() {
        String[] values = new String[] {"Name:John,Age:30,City:New York", "Name:Anna,Age:25,City:Chicago"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        String comment = "This is a comment";
        CSVRecord record = new CSVRecord(values, mapping, comment, 0);
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testRecordNumber() {
        String[] values = new String[] {"Name", "Age", "City"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        long recordNumber = 3;
        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = new String[] {"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals(values.length, record.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[] {"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals("[Name, Age, City]", record.toString());
    }
}