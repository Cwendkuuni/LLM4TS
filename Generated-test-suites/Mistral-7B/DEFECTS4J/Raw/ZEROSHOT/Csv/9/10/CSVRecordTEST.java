import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

    private static CSVFormat csvFormat;
    private static CSVParser csvParser;
    private static String csvData;

    @BeforeClass
    public static void setUp() {
        csvFormat = CSVFormat.DEFAULT.withHeader();
        csvParser = new CSVParser(new StringReader(csvData), csvFormat);
    }

    @Test
    public void testNewCSVRecord() {
        List<CSVRecord> records = csvParser.getRecords();
        CSVRecord csvRecord = records.get(0);
        Map<String, Integer> mapping = csvRecord.mapping;

        // Test empty record
        CSVRecord emptyRecord = new CSVRecord(null, null, null, 0L);
        assertTrue(emptyRecord.isEmpty());
        assertFalse(emptyRecord.isConsistent());
        assertNull(emptyRecord.getComment());
        assertNull(emptyRecord.get(0));
        assertNull(emptyRecord.get(1));
        assertNull(emptyRecord.get("name"));
        assertFalse(emptyRecord.isMapped("name"));
        assertFalse(emptyRecord.isSet("name"));
        assertFalse(emptyRecord.isSet(0));
        assertSame(Collections.emptyList(), emptyRecord.toList());
        assertSame(Collections.emptyMap(), emptyRecord.toMap());

        // Test record with values
        CSVRecord recordWithValues = records.get(1);
        assertTrue(recordWithValues.isConsistent());
        assertEquals("value1", recordWithValues.get(0));
        assertEquals("value2", recordWithValues.get(1));
        assertEquals("value3", recordWithValues.get("name1"));
        assertEquals("value4", recordWithValues.get("name2"));
        assertSame(Arrays.asList("value1", "value2", "value3", "value4"), recordWithValues.toList());
        assertSame(new HashMap<String, String>() {{
            put("name1", "0");
            put("name2", "1");
        }}, recordWithValues.toMap());
        assertSame(mapping, recordWithValues.mapping);
        assertSame(records.get(1).values, recordWithValues.values());

        // Test record with comment
        CSVRecord recordWithComment = records.get(2);
        assertEquals("comment1", recordWithComment.getComment());

        // Test record with no header mapping
        CSVRecord recordWithNoMapping = new CSVRecord(
                new String[]{"value1", "value2", "value3"}, null, null, 0L);
        assertFalse(recordWithNoMapping.isConsistent());
        assertTrue(recordWithNoMapping.isEmpty());
        assertNull(recordWithNoMapping.getComment());
        assertSame("value1", recordWithNoMapping.get(0));
        assertSame("value2", recordWithNoMapping.get(1));
        assertSame("value3", recordWithNoMapping.get(2));
        assertNull(recordWithNoMapping.get("name1"));
        assertFalse(recordWithNoMapping.isMapped("name1"));
        assertFalse(recordWithNoMapping.isSet("name1"));
        assertFalse(recordWithNoMapping.isSet(3));
        assertSame(Arrays.asList("value1", "value2", "value3"), recordWithNoMapping.toList());
        assertSame(new HashMap<>(), recordWithNoMapping.toMap());
        assertSame(Collections.singletonMap("0", "value1"), recordWithNoMapping.asMap());
    }
}