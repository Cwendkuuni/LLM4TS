import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mock;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Mock
    private CSVRecord csvRecord;

    @Test
    public void testGetByEnum_ValidEnum_ReturnsStringValue() {
        csvRecord = new CSVRecord(null, null, null, 0);
        String expected = "value";
        Assert.assertEquals(expected, csvRecord.get(TestEnum.TEST));
    }

    @Test
    public void testGetByEnum_InvalidEnum_ReturnsNull() {
        csvRecord = new CSVRecord(null, null, null, 0);
        Assert.assertNull(csvRecord.get(TestEnum.INVALID));
    }

    @Test
    public void testGetByIndex_ValidIndex_ReturnsStringValue() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        String expected = "value1";
        Assert.assertEquals(expected, csvRecord.get(0));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByIndex_InvalidIndex_ThrowsArrayIndexOutOfBoundsException() {
        csvRecord = new CSVRecord(null, null, null, 0);
        csvRecord.get(0);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_MappingNotSpecified_ThrowsIllegalStateException() {
        csvRecord = new CSVRecord(null, null, null, 0);
        csvRecord.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_InvalidName_ThrowsIllegalArgumentException() {
        Map<String, Integer> mapping = new HashMap<>();
        csvRecord = new CSVRecord(null, mapping, null, 0);
        csvRecord.get("invalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_InvalidIndex_ThrowsIllegalArgumentException() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 1);
        csvRecord = new CSVRecord(new String[]{"value1"}, mapping, null, 0);
        csvRecord.get("name");
    }

    @Test
    public void testGetComment_CommentExists_ReturnsCommentString() {
        csvRecord = new CSVRecord(null, null, "comment", 0);
        String expected = "comment";
        Assert.assertEquals(expected, csvRecord.getComment());
    }

    @Test
    public void testGetComment_NoComment_ReturnsNull() {
        csvRecord = new CSVRecord(null, null, null, 0);
        Assert.assertNull(csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber_RecordNumberExists_ReturnsRecordNumber() {
        csvRecord = new CSVRecord(null, null, null, 5);
        long expected = 5;
        Assert.assertEquals(expected, csvRecord.getRecordNumber());
    }

    @Test
    public void testGetRecordNumber_RecordNumberDoesNotExist_ReturnsZero() {
        csvRecord = new CSVRecord(null, null, null, 0);
        long expected = 0;
        Assert.assertEquals(expected, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent_MappingNotSpecified_ReturnsTrue() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        Assert.assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistent_MappingSpecifiedAndSizesMatch_ReturnsTrue() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 1);
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        Assert.assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistent_MappingSpecifiedAndSizesDoNotMatch_ReturnsFalse() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 2);
        csvRecord = new CSVRecord(new String[]{"value1"}, mapping, null, 0);
        Assert.assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped_ColumnIsMapped_ReturnsTrue() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 1);
        csvRecord = new CSVRecord(null, mapping, null, 0);
        Assert.assertTrue(csvRecord.isMapped("name"));
    }

    @Test
    public void testIsMapped_ColumnIsNotMapped_ReturnsFalse() {
        csvRecord = new CSVRecord(null, null, null, 0);
        Assert.assertFalse(csvRecord.isMapped("name"));
    }

    @Test
    public void testIsSet_ColumnIsSet_ReturnsTrue() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        csvRecord = new CSVRecord(new String[]{"value"}, mapping, null, 0);
        Assert.assertTrue(csvRecord.isSet("name"));
    }

    @Test
    public void testIsSet_ColumnIsNotSet_ReturnsFalse() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 1);
        csvRecord = new CSVRecord(new String[]{"value"}, mapping, null, 0);
        Assert.assertFalse(csvRecord.isSet("name"));
    }

    @Test
    public void testIterator_ValidRecord_ReturnsIteratorOverValues() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        Assert.assertEquals("value1", csvRecord.iterator().next());
    }

    @Test
    public void testPutIn_ValidRecord_PutsValuesInMap() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "value");
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        csvRecord.putIn(map);
        Assert.assertEquals("value1", map.get("name"));
        Assert.assertEquals("value2", map.get("name"));
    }

    @Test
    public void testSize_ValidRecord_ReturnsNumberOfValues() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        int expected = 2;
        Assert.assertEquals(expected, csvRecord.size());
    }

    @Test
    public void testToList_ValidRecord_ReturnsListOfValues() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        int expected = 2;
        Assert.assertEquals(expected, csvRecord.toList().size());
    }

    @Test
    public void testToMap_ValidRecord_ReturnsMapWithValues() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        Map<String, String> expected = new HashMap<>();
        expected.put("index0", "value1");
        expected.put("index1", "value2");
        Assert.assertEquals(expected, csvRecord.toMap());
    }

    @Test
    public void testToString_ValidRecord_ReturnsStringRepresentationOfValues() {
        csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        String expected = "[value1, value2]";
        Assert.assertEquals(expected, csvRecord.toString());
    }

}