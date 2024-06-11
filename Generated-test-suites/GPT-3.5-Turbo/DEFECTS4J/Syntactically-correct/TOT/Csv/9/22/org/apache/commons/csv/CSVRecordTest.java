import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = createSampleRecord();
        assertEquals("Value1", record.get(HeaderEnum.HEADER_1));
        assertEquals("Value2", record.get(HeaderEnum.HEADER_2));
        assertNull(record.get(HeaderEnum.HEADER_3));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = createSampleRecord();
        assertEquals("Value1", record.get(0));
        assertEquals("Value2", record.get(1));
        try {
            record.get(2);
            fail("Expected exception not thrown.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetByName() {
        CSVRecord record = createSampleRecord();
        assertEquals("Value1", record.get("Header1"));
        assertEquals("Value2", record.get("Header2"));
        try {
            record.get("Header3");
            fail("Expected exception not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "This is a comment.", 1L);
        assertEquals("This is a comment.", record.getComment());

        record = new CSVRecord(null, null, null, 1L);
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 1L);
        assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord consistentRecord = createSampleRecord();
        assertTrue(consistentRecord.isConsistent());

        CSVRecord inconsistentRecord = new CSVRecord(null, null, null, 1L);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = createSampleRecord();
        assertTrue(record.isMapped("Header1"));
        assertTrue(record.isMapped("Header2"));
        assertFalse(record.isMapped("Header3"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = createSampleRecord();
        assertTrue(record.isSet("Header1"));
        assertTrue(record.isSet("Header2"));
        assertFalse(record.isSet("Header3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = createSampleRecord();
        String[] expectedValues = new String[] { "Value1", "Value2" };
        int i = 0;
        for (String value : record) {
            assertEquals(expectedValues[i], value);
            i++;
        }
    }

    @Test
    public void testPutIn() {
        CSVRecord record = createSampleRecord();
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Header1", "Value1");
        expectedMap.put("Header2", "Value2");

        Map<String, String> resultMap = record.putIn(new HashMap<String, String>());
        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testSize() {
        CSVRecord record = createSampleRecord();
        assertEquals(2, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = createSampleRecord();
        assertEquals(Arrays.asList("Value1", "Value2"), record.toList());
    }

    @Test
    public void testToMap() {
        CSVRecord record = createSampleRecord();
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Header1", "Value1");
        expectedMap.put("Header2", "Value2");

        Map<String, String> resultMap = record.toMap();
        assertEquals(expectedMap, resultMap);
    }

    private CSVRecord createSampleRecord() {
        String[] values = new String[] { "Value1", "Value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        String comment = null;
        long recordNumber = 1L;
        return new CSVRecord(values, mapping, comment, recordNumber);
    }

    private enum HeaderEnum {
        HEADER_1,
        HEADER_2,
        HEADER_3;
    }
}