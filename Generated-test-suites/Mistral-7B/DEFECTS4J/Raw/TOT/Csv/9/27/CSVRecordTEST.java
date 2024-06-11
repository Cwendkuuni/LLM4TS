import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new String[]{"John", "30", "NYC"});
        assertEquals("John", record.get(Enum.NAME));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new String[]{"John", "30", "NYC"});
        assertEquals("John", record.get(0));
        assertEquals("30", record.get(1));
        assertEquals("NYC", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new String[]{"John", "30", "NYC"});
        assertEquals("John", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals("NYC", record.get("City"));
        assertNull(record.get("Unknown"));
    }

    @Test
    public void testGetComment() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new String[]{"John", "30", "NYC"}, "Comment");
        assertEquals("Comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new String[]{"John", "30", "NYC"}, 1L);
        assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new String[]{"John", "30", "NYC"});
        assertTrue(record.isConsistent());
        CSVRecord record2 = createRecord(new String[]{"Name", "Age", "City"}, new String[]{"John", "30"});
        assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new String[]{"John", "30", "NYC"});
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new String[]{"John", "30", "NYC"});
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new String[]{"John", "30", "NYC"});
        List<String> values = record.toList();
        Iterator<String> iterator = record.iterator();
        Map<String, String> map = new HashMap<>();
        map.putAll(record.toMap());
        int i = 0;
        while (iterator.hasNext()) {
            assertEquals(values.get(i), iterator.next());
            assertEquals(values.get(i), map.get(String.valueOf(i)));
            i++;
        }
    }

    @Test
    public void testToString() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new String[]{"John", "30", "NYC"});
        assertEquals("[Name=John, Age=30, City=NYC]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = createRecord(new String[]{"Name", "Age", "City"}, new String[]{"John", "30", "NYC"});
        String[] values = record.values();
        assertEquals("John", values[0]);
        assertEquals("30", values[1]);
        assertEquals("NYC", values[2]);
    }

    private CSVRecord createRecord(String[] header, String[] values, String comment, long recordNumber) {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = null;
        try {
            parser = new CSVParser(new StringReader(format.printRecord(header, values, comment, recordNumber)));
            return parser.getRecord();
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private static final Enum<String> NAME = new Enum<String>() {
        @Override
        public String toString() {
            return "NAME";
        }
    };

}