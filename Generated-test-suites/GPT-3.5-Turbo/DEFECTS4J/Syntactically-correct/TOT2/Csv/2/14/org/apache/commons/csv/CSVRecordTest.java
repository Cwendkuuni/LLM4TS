import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertNull(record.get("Name"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        record.get("Name");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertTrue(record.isConsistent());

        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"A", "B"}, null, "", 1);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertFalse(record.isMapped("Name"));
        assertTrue(record.isMapped("A"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "", 1);
        assertFalse(record.isSet("Name"));
        assertTrue(record.isSet("A"));
    }
}