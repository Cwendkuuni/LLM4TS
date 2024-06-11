import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CSVRecord.
 */
public class CSVRecordTest {

    private CSVFormat format;
    private CSVParser parser;

    @Before
    public void setUp() {
        format = new CSVFormat();
        try {
            parser = new CSVParser(new StringReader("name,age,comment\nAlice,30,foo\nBob,25,bar\n"), format);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetByEnum() {
        final CSVRecord record = parser.nextRecord();
        assertEquals("Alice", record.get(CSVRecordTest.Name.NAME));
        assertEquals("30", record.get(CSVRecordTest.Name.AGE));
        assertNull(record.get(CSVRecordTest.Name.COMMENT));
        final CSVRecord record2 = parser.nextRecord();
        assertEquals("Bob", record2.get(CSVRecordTest.Name.NAME));
        assertEquals("25", record2.get(CSVRecordTest.Name.AGE));
        assertEquals("bar", record2.get(CSVRecordTest.Name.COMMENT));
    }

    @Test
    public void testGetByIndex() {
        final CSVRecord record = parser.nextRecord();
        assertEquals("Alice", record.get(0));
        assertEquals("30", record.get(1));
        assertNull(record.get(2));
        final CSVRecord record2 = parser.nextRecord();
        assertEquals("Bob", record2.get(0));
        assertEquals("25", record2.get(1));
        assertEquals("bar", record2.get(2));
    }

    @Test
    public void testGetByString() {
        final CSVRecord record = parser.nextRecord();
        assertEquals("Alice", record.get("name"));
        assertEquals("30", record.get("age"));
        assertNull(record.get("comment"));
        final CSVRecord record2 = parser.nextRecord();
        assertEquals("Bob", record2.get("name"));
        assertEquals("25", record2.get("age"));
        assertEquals("bar", record2.get("comment"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        final CSVRecord record = parser.nextRecord();
        record.get("nonExistentName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        final CSVRecord record = parser.nextRecord();
        record.get("name");
        record.get(values().length);
    }

    @Test
    public void testGetComment() {
        final CSVRecord record = parser.nextRecord();
        assertNull(record.getComment());
        final CSVRecord recordWithComment = parser.nextRecord();
        assertEquals("foo", recordWithComment.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        final CSVRecord record = parser.nextRecord();
        assertEquals(0, record.getRecordNumber());
        final CSVRecord record2 = parser.nextRecord();
        assertEquals(1, record2.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        final CSVRecord record = parser.nextRecord();
        assertTrue(record.isConsistent());
        final CSVRecord recordWithMismatchingSize = new CSVRecord(new String[]{"Alice", "30"}, null, null, 0);
        assertFalse(recordWithMismatchingSize.isConsistent());
    }

    @Test
    public void testIsMapped() {
        final CSVRecord record = parser.nextRecord();
        assertTrue(record.isMapped("name"));
        assertTrue(record.isMapped("age"));
        assertFalse(record.isMapped("nonExistent"));

        final CSVRecord recordWithNoMapping = new CSVRecord(new String[]{"Alice", "30"}, null, null, 0);
        assertFalse(recordWithNoMapping.isMapped("name"));
    }

    @Test
    public void testIsSet() {
        final CSVRecord record = parser.nextRecord();
        assertTrue(record.isSet("name"));
        assertTrue(record.isSet("age"));
        assertFalse(record.isSet("comment"));

        final CSVRecord recordWithNoComment = new CSVRecord(new String[]{"Alice", "30"}, null, null, 0);
        assertFalse(recordWithNoComment.isSet("comment"));
    }

    @Test
    public void testIterator() {
        final CSVRecord record = parser.nextRecord();
        final List<String> expectedValues = Arrays.asList("Alice", "30", null);
        final List<String> actualValues = Arrays.asList(record.iterator().toArray(new String[0]));
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testPutIn() {
        final Map<String, String> expectedMap = new HashMap<String, String>() {{
            put("name", "Alice");
            put("age", "30");
            put("comment", null);
        }};
        final Map<String, String> actualMap = new CSVRecord(new String[]{"Alice", "30", null}, null, null, 0).putIn(new HashMap<String, String>());
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testSize() {
        final CSVRecord record = parser.nextRecord();
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        final CSVRecord record = parser.nextRecord();
        assertEquals("[Alice, 30, null]", record.toString());
    }

    @Test
    public void testValues() {
        final CSVRecord record = parser.nextRecord();
        final String[] expectedValues = new String[]{"Alice", "30", null};
        assertArrayEquals(expectedValues, record.values());
    }

    private enum Name {
        NAME,
        AGE,
        COMMENT
    }

    private void values() {
        return new String[]{"name", "age", "comment"};
    }
}