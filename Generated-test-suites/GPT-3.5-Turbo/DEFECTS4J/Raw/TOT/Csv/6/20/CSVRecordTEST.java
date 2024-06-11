import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
    
    private static CSVRecord record;
    private static final String COMMENT = "This is a comment";
    private static final long RECORD_NUMBER = 1;
    private static final String[] VALUES = {"value1", "value2", "value3"};
    private static final String[] EMPTY_VALUES = {};
    private static final String MAPPED_COLUMN = "column1";
    private static final String UNMAPPED_COLUMN = "column4";
    
    @Before
    public void setUp() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put(MAPPED_COLUMN, 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        record = new CSVRecord(VALUES, mapping, COMMENT, RECORD_NUMBER);
    }
    
    @Test
    public void testConstructor_ValidValues() {
        assertEquals(VALUES.length, record.size());
        assertEquals(COMMENT, record.getComment());
        assertEquals(RECORD_NUMBER, record.getRecordNumber());
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testConstructor_NullValues() {
        record = new CSVRecord(null, null, null, 0);
        assertEquals(0, record.size());
    }
    
    @Test
    public void testConstructor_EmptyValues() {
        record = new CSVRecord(EMPTY_VALUES, null, null, 0);
        assertEquals(0, record.size());
    }
    
    @Test
    public void testGet_ByEnum_ValidEnum() {
        assertEquals(VALUES[0], record.get(TestEnum.TEST1));
    }
    
    @Test
    public void testGet_ByEnum_InvalidEnum() {
        try {
            record.get(TestEnum.INVALID);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for INVALID not found, expected one of [TEST1, column2, column3]", e.getMessage());
        }
    }
    
    @Test
    public void testGet_ByIndex_ValidIndex() {
        assertEquals(VALUES[0], record.get(0));
    }
    
    @Test
    public void testGet_ByIndex_InvalidIndex() {
        try {
            record.get(3);
            fail("ArrayIndexOutOfBoundsException should be thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("Index for header '3' is 3 but CSVRecord only has 3 values!", e.getMessage());
        }
    }
    
    @Test
    public void testGet_ByIndex_NegativeIndex() {
        try {
            record.get(-1);
            fail("ArrayIndexOutOfBoundsException should be thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("Index for header '-1' is -1 but CSVRecord only has 3 values!", e.getMessage());
        }
    }
    
    @Test
    public void testGet_ByName_ValidName() {
        assertEquals(VALUES[0], record.get(MAPPED_COLUMN));
    }
    
    @Test
    public void testGet_ByName_InvalidName() {
        try {
            record.get(UNMAPPED_COLUMN);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for column4 not found, expected one of [column1, column2, column3]", e.getMessage());
        }
    }
    
    @Test
    public void testGet_ByName_NullMapping() {
        record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        try {
            record.get(MAPPED_COLUMN);
            fail("IllegalStateException should be thrown");
        } catch (IllegalStateException e) {
            assertEquals("No header mapping was specified, the record values can't be accessed by name", e.getMessage());
        }
    }
    
    @Test
    public void testGet_ByName_ColumnIndexOutOfBounds() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 3);
        record = new CSVRecord(VALUES, mapping, COMMENT, RECORD_NUMBER);
        try {
            record.get(MAPPED_COLUMN);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Index for header 'column1' is 3 but CSVRecord only has 3 values!", e.getMessage());
        }
    }
    
    @Test
    public void testGetComment_ValidComment() {
        assertEquals(COMMENT, record.getComment());
    }
    
    @Test
    public void testGetComment_NullComment() {
        record = new CSVRecord(VALUES, null, null, RECORD_NUMBER);
        assertNull(record.getComment());
    }
    
    @Test
    public void testGetRecordNumber_ValidRecordNumber() {
        assertEquals(RECORD_NUMBER, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent_MappingIsNull() {
        record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistent_MappingSizeMismatch() {
        Map<String, Integer> invalidMapping = new HashMap<>();
        invalidMapping.put(MAPPED_COLUMN, 0);
        invalidMapping.put("column2", 1);
        record = new CSVRecord(VALUES, invalidMapping, COMMENT, RECORD_NUMBER);
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsConsistent_ValuesSizeMismatch() {
        record = new CSVRecord(EMPTY_VALUES, null, COMMENT, RECORD_NUMBER);
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsMapped_IsMapped() {
        assertTrue(record.isMapped(MAPPED_COLUMN));
    }
    
    @Test
    public void testIsMapped_IsNotMapped() {
        assertFalse(record.isMapped(UNMAPPED_COLUMN));
    }
    
    @Test
    public void testIsSet_IsSet() {
        assertTrue(record.isSet(MAPPED_COLUMN));
    }
    
    @Test
    public void testIsSet_IsNotSet() {
        assertFalse(record.isSet(UNMAPPED_COLUMN));
    }
    
    @Test
    public void testIterator_ToList_ValidList() {
        List<String> expectedList = Arrays.asList(VALUES);
        assertTrue(expectedList.equals(record.toList()));
    }
    
    @Test
    public void testPutIn_ValidMap() {
        Map<String, String> map = new HashMap<>();
        map = record.putIn(map);
        
        for (int i = 0; i < VALUES.length; i++) {
            String expectedValue = VALUES[i];
            String actualValue = map.get(Integer.toString(i));
            assertEquals(expectedValue, actualValue);
        }
    }
    
    @Test
    public void testPutIn_EmptyMap() {
        Map<String, String> emptyMap = new HashMap<>();
        emptyMap = record.putIn(emptyMap);
        assertTrue(emptyMap.isEmpty());
    }
    
    @Test
    public void testSize_ValidSize() {
        assertEquals(VALUES.length, record.size());
    }
    
    @Test
    public void testToMap_ValidMap() {
        Map<String, String> expectedMap = new HashMap<>();
        for (int i = 0; i < VALUES.length; i++) {
            expectedMap.put(Integer.toString(i), VALUES[i]);
        }
        assertEquals(expectedMap, record.toMap());
    }
    
    @Test
    public void testToString_ValidString() {
        String expectedString = Arrays.toString(VALUES);
        assertEquals(expectedString, record.toString());
    }
    
    enum TestEnum {
        TEST1,
        INVALID;
    }

}