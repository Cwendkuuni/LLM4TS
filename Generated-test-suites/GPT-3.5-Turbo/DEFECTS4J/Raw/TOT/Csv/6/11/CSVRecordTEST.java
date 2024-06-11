import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetByIndex_WhenValidIndex_ThenReturnCorrectValue() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        Assert.assertEquals("A", record.get(0));
        Assert.assertEquals("B", record.get(1));
        Assert.assertEquals("C", record.get(2));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByIndex_WhenInvalidIndex_ThenThrowArrayIndexOutOfBoundsException() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        record.get(3);
    }

    @Test
    public void testGetByName_WhenValidName_ThenReturnCorrectValue() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        Assert.assertEquals("A", record.get("col1"));
        Assert.assertEquals("B", record.get("col2"));
        Assert.assertEquals("C", record.get("col3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_WhenInvalidName_ThenThrowIllegalArgumentException() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        record.get("col4");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_WhenMappingIsNull_ThenThrowIllegalStateException() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        record.get("col1");
    }

    @Test
    public void testGetByEnum_WhenValidEnum_ThenReturnCorrectValue() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("COL1", 0);
        mapping.put("COL2", 1);
        mapping.put("COL3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        Assert.assertEquals("A", record.get(TestEnum.COL1));
        Assert.assertEquals("B", record.get(TestEnum.COL2));
        Assert.assertEquals("C", record.get(TestEnum.COL3));
    }

    @Test
    public void testIsConsistent_WhenMappingIsNull_ThenReturnTrue() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_WhenMappingSizeEqualsValuesLength_ThenReturnTrue() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_WhenMappingSizeNotEqualsValuesLength_ThenReturnFalse() {
        String[] values = {"A", "B"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped_WhenMapped_ThenReturnTrue() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        Assert.assertTrue(record.isMapped("col1"));
        Assert.assertTrue(record.isMapped("col2"));
        Assert.assertTrue(record.isMapped("col3"));
    }

    @Test
    public void testIsMapped_WhenNotMapped_ThenReturnFalse() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        Assert.assertFalse(record.isMapped("col3"));
        Assert.assertFalse(record.isMapped("col4"));
    }

    @Test
    public void testIsSet_WhenMappedAndHasValue_ThenReturnTrue() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        Assert.assertTrue(record.isSet("col1"));
        Assert.assertTrue(record.isSet("col2"));
        Assert.assertTrue(record.isSet("col3"));
    }

    @Test
    public void testIsSet_WhenMappedAndHasNoValue_ThenReturnFalse() {
        String[] values = {"A", "B"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        Assert.assertFalse(record.isSet("col3"));
    }

    @Test
    public void testGetComment_WhenCommentExists_ThenReturnComment() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment", 0);
        
        Assert.assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetComment_WhenCommentDoesNotExist_ThenReturnNull() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        Assert.assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber_WhenRecordNumber_ThenReturnRecordNumber() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 10);
        
        Assert.assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testSize_WhenValuesNotEmpty_ThenReturnValuesLength() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testSize_WhenValuesEmpty_ThenReturnZero() {
        String[] values = {};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        Assert.assertEquals(0, record.size());
    }

    @Test
    public void testToList_WhenValuesNotEmpty_ThenReturnValuesAsList() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        Assert.assertEquals(Arrays.asList("A", "B", "C"), record.toList());
    }

    @Test
    public void testToList_WhenValuesEmpty_ThenReturnEmptyList() {
        String[] values = {};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        Assert.assertEquals(Arrays.asList(), record.toList());
    }

    @Test
    public void testPutIn_WhenMappingNotNull_ThenReturnMapWithValues() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("col1", "A");
        expectedMap.put("col2", "B");
        expectedMap.put("col3", "C");
        
        Assert.assertEquals(expectedMap, record.putIn(new HashMap<>()));
    }

    @Test
    public void testPutIn_WhenMappingIsNull_ThenReturnEmptyMap() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        Assert.assertEquals(new HashMap<>(), record.putIn(new HashMap<>()));
    }

    @Test
    public void testToString_WhenValuesNotEmpty_ThenReturnValuesAsString() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        Assert.assertEquals("[A, B, C]", record.toString());
    }

    @Test
    public void testToString_WhenValuesEmpty_ThenReturnEmptyBrackets() {
        String[] values = {};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        Assert.assertEquals("[]", record.toString());
    }

    enum TestEnum {
        COL1,
        COL2,
        COL3
    }
}