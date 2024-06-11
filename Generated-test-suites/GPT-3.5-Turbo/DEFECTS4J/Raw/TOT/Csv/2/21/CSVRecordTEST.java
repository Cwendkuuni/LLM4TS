import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    void testGetMethodByIndex() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        String result = csvRecord.get(1);

        Assertions.assertEquals("value2", result);
    }

    @Test
    void testGetMethodByName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        String result = csvRecord.get("name2");

        Assertions.assertEquals("value2", result);
    }

    @Test
    void testGetMethodByNameWhenMappingIsNull() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            csvRecord.get("name2");
        });
    }

    @Test
    void testGetMethodByNameWhenColumnNotFound() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        String result = csvRecord.get("name3");

        Assertions.assertNull(result);
    }

    @Test
    void testIsConsistentWhenMappingIsNull() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        boolean result = csvRecord.isConsistent();

        Assertions.assertTrue(result);
    }

    @Test
    void testIsConsistentWhenMappingSizeDiffersFromValuesLength() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        boolean result = csvRecord.isConsistent();

        Assertions.assertFalse(result);
    }

    @Test
    void testIsMappedWhenMappingIsNotNullAndContainsColumn() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        boolean result = csvRecord.isMapped("name1");

        Assertions.assertTrue(result);
    }

    @Test
    void testIsMappedWhenMappingIsNull() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        boolean result = csvRecord.isMapped("name1");

        Assertions.assertFalse(result);
    }

    @Test
    void testIsSetWhenColumnMappedAndHasValue() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        boolean result = csvRecord.isSet("name2");

        Assertions.assertTrue(result);
    }

    @Test
    void testIsSetWhenColumnMappedAndHasNoValue() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 3);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        boolean result = csvRecord.isSet("name2");

        Assertions.assertFalse(result);
    }

    @Test
    void testIsSetWhenColumnNotMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        boolean result = csvRecord.isSet("name3");

        Assertions.assertFalse(result);
    }

    @Test
    void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        int counter = 0;
        for (String value : csvRecord) {
            Assertions.assertEquals(values[counter], value);
            counter++;
        }
    }

    @Test
    void testValues() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        String[] result = csvRecord.values();

        Assertions.assertArrayEquals(values, result);
    }

    @Test
    void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, "This is a comment", 1);

        String result = csvRecord.getComment();

        Assertions.assertEquals("This is a comment", result);
    }

    @Test
    void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 5);

        long result = csvRecord.getRecordNumber();

        Assertions.assertEquals(5, result);
    }

    @Test
    void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        int result = csvRecord.size();

        Assertions.assertEquals(3, result);
    }

    @Test
    void testToString() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);

        String result = csvRecord.toString();

        Assertions.assertEquals("[value1, value2, value3]", result);
    }

}