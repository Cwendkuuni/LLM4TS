import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum_ValidEnum_ReturnsValue() {
        String value = csvRecord.get(TestEnum.COLUMN1);
        Assert.assertEquals("value1", value);
    }

    @Test
    public void testGetByIndex_ValidIndex_ReturnsValue() {
        String value = csvRecord.get(0);
        Assert.assertEquals("value1", value);
    }

    @Test
    public void testGetByName_ValidName_ReturnsValue() {
        String value = csvRecord.get("column1");
        Assert.assertEquals("value1", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_NoMappingSpecified_ThrowsIllegalStateException() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1"}, null, null, 1);
        csvRecord.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_InvalidName_ThrowsIllegalArgumentException() {
        csvRecord.get("invalidColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_MappedButNoValueFound_ThrowsIllegalArgumentException() {
        csvRecord.get("column3");
    }

    @Test
    public void testGetComment_ValidComment_ReturnsComment() {
        String comment = csvRecord.getComment();
        Assert.assertEquals("This is a comment", comment);
    }

    @Test
    public void testGetRecordNumber_ValidRecordNumber_ReturnsRecordNumber() {
        long recordNumber = csvRecord.getRecordNumber();
        Assert.assertEquals(1, recordNumber);
    }

    @Test
    public void testIsConsistent_ValidMappingAndValues_ReturnsTrue() {
        boolean isConsistent = csvRecord.isConsistent();
        Assert.assertTrue(isConsistent);
    }

    @Test
    public void testIsConsistent_MappingNull_ReturnsTrue() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1"}, null, null, 1);
        boolean isConsistent = csvRecord.isConsistent();
        Assert.assertTrue(isConsistent);
    }

    @Test
    public void testIsConsistent_MappingSizeNotEqualValuesLength_ReturnsFalse() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        mapping.put("column4", 3);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        boolean isConsistent = csvRecord.isConsistent();
        Assert.assertFalse(isConsistent);
    }

    @Test
    public void testIsMapped_MappedColumn_ReturnsTrue() {
        boolean isMapped = csvRecord.isMapped("column1");
        Assert.assertTrue(isMapped);
    }

    @Test
    public void testIsMapped_NotMappedColumn_ReturnsFalse() {
        boolean isMapped = csvRecord.isMapped("column4");
        Assert.assertFalse(isMapped);
    }

    @Test
    public void testIsSet_MappedAndValuePresent_ReturnsTrue() {
        boolean isSet = csvRecord.isSet("column1");
        Assert.assertTrue(isSet);
    }

    @Test
    public void testIsSet_NotMappedColumn_ReturnsFalse() {
        boolean isSet = csvRecord.isSet("column4");
        Assert.assertFalse(isSet);
    }

    @Test
    public void testIsSet_MappedButNoValuePresent_ReturnsFalse() {
        boolean isSet = csvRecord.isSet("column3");
        Assert.assertFalse(isSet);
    }

    @Test
    public void testIterator_ReturnsIterator() {
        Iterator<String> iterator = csvRecord.iterator();
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testPutIn_ValidMappingAndValues_ReturnsMapWithValues() {
        Map<String, String> map = new HashMap<>();
        csvRecord.putIn(map);
        Assert.assertEquals("value1", map.get("column1"));
        Assert.assertEquals("value2", map.get("column2"));
        Assert.assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize_ValidValues_ReturnsSize() {
        int size = csvRecord.size();
        Assert.assertEquals(3, size);
    }

    @Test
    public void testToList_ReturnsListOfValues() {
        List<String> list = csvRecord.toList();
        Assert.assertEquals(3, list.size());
        Assert.assertEquals("value1", list.get(0));
        Assert.assertEquals("value2", list.get(1));
        Assert.assertEquals("value3", list.get(2));
    }

    @Test
    public void testToMap_ValidMappingAndValues_ReturnsMapWithValues() {
        Map<String, String> map = csvRecord.toMap();
        Assert.assertEquals("value1", map.get("column1"));
        Assert.assertEquals("value2", map.get("column2"));
        Assert.assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testToString_ReturnsStringRepresentationOfValues() {
        String string = csvRecord.toString();
        Assert.assertEquals("[value1, value2, value3]", string);
    }

    @Test
    public void testValues_ReturnsValuesArray() {
        String[] values = csvRecord.values();
        Assert.assertEquals(3, values.length);
        Assert.assertEquals("value1", values[0]);
        Assert.assertEquals("value2", values[1]);
        Assert.assertEquals("value3", values[2]);
    }

    private enum TestEnum {
        COLUMN1,
        COLUMN2,
        COLUMN3
    }
}