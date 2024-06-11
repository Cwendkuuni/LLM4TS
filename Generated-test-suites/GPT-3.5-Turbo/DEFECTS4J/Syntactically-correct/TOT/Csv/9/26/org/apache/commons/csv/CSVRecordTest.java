import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.csv.CSVRecord;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"John", "Doe", "25"};
        csvRecord = new CSVRecord(values, null, "", 1);
    }

    @Test
    public void testGetEnum() {
        assertEquals("John", csvRecord.get(TestEnum.NAME));
    }

    @Test
    public void testGetIndex() {
        assertEquals("Doe", csvRecord.get(1));
    }

    @Test
    public void testGetName() {
        assertNull(csvRecord.get("Age"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithoutMapping() {
        CSVRecord csvRecordWithoutMapping = new CSVRecord(null, null, "", 1);
        csvRecordWithoutMapping.get("Age");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidMapping() {
        CSVRecord csvRecordWithoutMapping = new CSVRecord(new String[]{"John", "Doe"}, null, "", 1);
        csvRecordWithoutMapping.get("Age");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidIndex() {
        csvRecord.get("Age");
    }

    @Test
    public void testGetComment() {
        assertEquals("", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithHeaders() {
        csvRecord = new CSVRecord(new String[]{"Name", "Age"}, null, "", 1);
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertFalse(csvRecord.isMapped("Name"));
    }

    @Test
    public void testIsSet() {
        assertFalse(csvRecord.isSet("Name"));
    }

    @Test
    public void testIterator() {
        assertNotNull(csvRecord.iterator());
    }

    @Test
    public void testPutIn() {
        assertEquals(0, csvRecord.putIn(new HashMap<String, String>()).size());
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList() {
        assertEquals(3, csvRecord.toList().size());
    }

    @Test
    public void testToMap() {
        assertEquals(0, csvRecord.toMap().size());
    }

    @Test
    public void testToString() {
        assertEquals("[John, Doe, 25]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        assertArrayEquals(new String[]{"John", "Doe", "25"}, csvRecord.values());
    }

    private enum TestEnum {
        NAME
    }
}