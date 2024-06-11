import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private static CSVRecord record;

    @BeforeClass
    public static void setUp() {
        String[] values = {"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        // Setting up the header mapping
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("Email", 2);
        record = new CSVRecord(values, mapping, null, 1);        
    }

    @Test
    public void get_ValidEnum_ReturnsValue() {
        String value = record.get(TestEnum.FIRST_NAME);
        assertEquals("John", value);
    }

    @Test
    public void get_InvalidEnum_ThrowsException() {
        try {
            record.get(TestEnum.INVALID);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for INVALID not found, expected one of [First Name, Last Name, Email]", e.getMessage());
        }
    }

    @Test
    public void get_ValidIndex_ReturnsValue() {
        String value = record.get(0);
        assertEquals("John", value);
    }

    @Test
    public void get_InvalidIndex_ThrowsException() {
        try {
            record.get(3);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("Index for header '3' is 3 but CSVRecord only has 3 values!", e.getMessage());
        }
    }

    @Test
    public void get_ValidName_ReturnsValue() {
        String value = record.get("First Name");
        assertEquals("John", value);
    }

    @Test
    public void get_InvalidNameNoMapping_ThrowsException() {
        try {
            record.get("Age");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("No header mapping was specified, the record values can't be accessed by name", e.getMessage());
        }
    }

    @Test
    public void get_InvalidNameNoKeyMapping_ThrowsException() {
        try {
            record.get("Location");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for Location not found, expected one of [First Name, Last Name, Email]", e.getMessage());
        }
    }

    @Test
    public void getComment_CommentAvailable_ReturnsComment() {
        CSVRecord commentedRecord = new CSVRecord(record.values(), record.mapping(), "This is a comment", record.getRecordNumber());
        String comment = commentedRecord.getComment();
        assertEquals("This is a comment", comment);
    }

    @Test
    public void getComment_CommentNotAvailable_ReturnsNull() {
        String comment = record.getComment();
        assertNull(comment);
    }

    @Test
    public void getRecordNumber_ValidRecord_ReturnsRecordNumber() {
        long recordNumber = record.getRecordNumber();
        assertEquals(1, recordNumber);
    }

    @Test
    public void isConsistent_NoMapping_ReturnsTrue() {
        CSVRecord noMappingRecord = new CSVRecord(record.values(), null, null, record.getRecordNumber());
        boolean isConsistent = noMappingRecord.isConsistent();
        assertTrue(isConsistent);
    }

    @Test
    public void isConsistent_MappingSizeNotEqualToValueSize_ReturnsFalse() {
        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("First Name", 0);
        inconsistentMapping.put("Last Name", 1);
        CSVRecord inconsistentRecord = new CSVRecord(record.values(), inconsistentMapping, null, record.getRecordNumber());
        boolean isConsistent = inconsistentRecord.isConsistent();
        assertFalse(isConsistent);
    }

    @Test
    public void isConsistent_MappingSizeEqualToValueSize_ReturnsTrue() {
        boolean isConsistent = record.isConsistent();
        assertTrue(isConsistent);
    }

    @Test
    public void isMapped_MappedName_ReturnsTrue() {
        boolean isMapped = record.isMapped("First Name");
        assertTrue(isMapped);
    }

    @Test
    public void isMapped_UnmappedName_ReturnsFalse() {
        boolean isMapped = record.isMapped("Location");
        assertFalse(isMapped);
    }

    @Test
    public void isSet_MappedNameWithValue_ReturnsTrue() {
        boolean isSet = record.isSet("First Name");
        assertTrue(isSet);
    }

    @Test
    public void isSet_MappedNameWithNoValue_ReturnsFalse() {
        Map<String, Integer> incompleteMapping = new HashMap<>();
        incompleteMapping.put("First Name", 0);
        CSVRecord incompleteRecord = new CSVRecord(record.values(), incompleteMapping, null, record.getRecordNumber());
        boolean isSet = incompleteRecord.isSet("First Name");
        assertFalse(isSet);
    }

    @Test
    public void isSet_UnmappedName_ReturnsFalse() {
        boolean isSet = record.isSet("Location");
        assertFalse(isSet);
    }

    @Test
    public void iterator_ReturnsIteratorOverValues() {
        String[] expectedValues = {"John", "Doe", "john.doe@example.com"};
        int index = 0;
        for (String value : record) {
            assertEquals(expectedValues[index++], value);
        }
    }

    @Test
    public void putIn_PopulatesMapWithMappedValues() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals(3, map.size());
        assertEquals("John", map.get("First Name"));
        assertEquals("Doe", map.get("Last Name"));
        assertEquals("john.doe@example.com", map.get("Email"));
    }

    @Test
    public void size_ReturnsNumberOfValues() {
        int size = record.size();
        assertEquals(3, size);
    }

    @Test
    public void toMap_ReturnsMapWithMappedValues() {
        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertEquals("John", map.get("First Name"));
        assertEquals("Doe", map.get("Last Name"));
        assertEquals("john.doe@example.com", map.get("Email"));
    }

    @Test
    public void values_ReturnsArrayWithValues() {
        String[] expectedValues = {"John", "Doe", "john.doe@example.com"};
        String[] values = record.values();
        assertArrayEquals(expectedValues, values);
    }
}

enum TestEnum {
    FIRST_NAME,
    LAST_NAME,
    EMAIL,
    INVALID
}