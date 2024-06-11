import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetMethodByIndex() {
        String[] values = new String[]{"John", "Doe", "35", "NY"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("John", record.get(0));

        // Test with an empty array
        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, mapping, null, 1);
        assertEquals(null, emptyRecord.get(0));

        // Test with invalid index
        try {
            record.get(3);
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetMethodByName() {
        String[] values = new String[]{"John", "Doe", "35", "NY"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("Doe", record.get("Age"));

        // Test with a null mapping
        Map<String, Integer> nullMapping = null;
        CSVRecord nullMappingRecord = new CSVRecord(values, nullMapping, null, 1);
        assertNull(nullMappingRecord.get("Name"));

        // Test with a missing mapping
        Map<String, Integer> missingMapping = Map.of("Name", 0);
        CSVRecord missingMappingRecord = new CSVRecord(values, missingMapping, null, 1);
        assertNull(missingMappingRecord.get("Age"));

        // Test with an empty mapping
        Map<String, Integer> emptyMapping = Map.of();
        CSVRecord emptyMappingRecord = new CSVRecord(values, emptyMapping, null, 1);
        assertNull(emptyMappingRecord.get("Name"));
    }

    // ... Other test cases for other methods here
}