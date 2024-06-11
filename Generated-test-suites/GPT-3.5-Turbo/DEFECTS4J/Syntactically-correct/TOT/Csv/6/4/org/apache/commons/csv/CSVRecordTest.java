import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {

    CSVRecord csvRecord;

    @Before
    public void setup() {
        String[] values = {"John", "Doe", "30", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);
        csvRecord = new CSVRecord(values, mapping, "This is a comment", 1);
    }

    @Test
    public void testGetEnum() {
        assertEquals("John", csvRecord.get(TestEnum.FIRST_NAME));
    }

    @Test
    public void testGetIndex() {
        assertEquals("Doe", csvRecord.get(1));
    }

    @Test
    public void testGetByName() {
        assertEquals("30", csvRecord.get("Age"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord csvRecord = new CSVRecord(null, null, null, 1);
        csvRecord.get("FirstName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        csvRecord.get("InvalidColumn");
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
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
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("FirstName"));
    }

    @Test
    public void testIsMappedInvalidColumn() {
        assertFalse(csvRecord.isMapped("InvalidColumn"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("Age"));
    }

    @Test
    public void testIsSetInvalidColumn() {
        assertFalse(csvRecord.isSet("InvalidColumn"));
    }

    @Test
    public void testIterator() {
        assertNotNull(csvRecord.iterator());
    }

    @Test
    public void testSize() {
        assertEquals(4, csvRecord.size());
    }

    @Test
    public void testToMap() {
        Map<String, String> resultMap = csvRecord.toMap();
        assertEquals("John", resultMap.get("FirstName"));
        assertEquals("Doe", resultMap.get("LastName"));
        assertEquals("30", resultMap.get("Age"));
        assertEquals("Male", resultMap.get("Gender"));
    }

    @Test
    public void testToString() {
        assertEquals("[John, Doe, 30, Male]", csvRecord.toString());
    }

    enum TestEnum {
        FIRST_NAME
    }
}