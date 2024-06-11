import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGet_Enum_String() {
        CSVRecord record = createCSVRecord();
        String value = record.get(TestEnum.TEST);
        Assert.assertEquals("Value", value);
    }

    @Test
    public void testGet_int_String() {
        CSVRecord record = createCSVRecord();
        String value = record.get(0);
        Assert.assertEquals("Value", value);
    }

    @Test
    public void testGet_String_String() {
        CSVRecord record = createCSVRecord();
        String value = record.get("Column1");
        Assert.assertEquals("Value", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_String_String_NoHeaderMapping() {
        CSVRecord record = createCSVRecordNoHeaderMapping();
        String value = record.get("Column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_String_String_MissingMapping() {
        CSVRecord record = createCSVRecord();
        String value = record.get("Column3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_String_String_IndexOutOfBounds() {
        CSVRecord record = createCSVRecord();
        String value = record.get("Column2");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = createCSVRecord();
        String comment = record.getComment();
        Assert.assertEquals("Line comment", comment);
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = createCSVRecord();
        long recordNumber = record.getRecordNumber();
        Assert.assertEquals(1L, recordNumber);
    }

    @Test
    public void testIsConsistent_True() {
        CSVRecord record = createCSVRecord();
        boolean consistent = record.isConsistent();
        Assert.assertTrue(consistent);
    }

    @Test
    public void testIsConsistent_False() {
        CSVRecord record = createIncompleteRecord();
        boolean consistent = record.isConsistent();
        Assert.assertFalse(consistent);
    }

    @Test
    public void testIsMapped_True() {
        CSVRecord record = createCSVRecord();
        boolean mapped = record.isMapped("Column1");
        Assert.assertTrue(mapped);
    }

    @Test
    public void testIsMapped_False() {
        CSVRecord record = createCSVRecord();
        boolean mapped = record.isMapped("Column3");
        Assert.assertFalse(mapped);
    }

    @Test
    public void testIsSet_True() {
        CSVRecord record = createCSVRecord();
        boolean set = record.isSet("Column1");
        Assert.assertTrue(set);
    }

    @Test
    public void testIsSet_False() {
        CSVRecord record = createCSVRecord();
        boolean set = record.isSet("Column3");
        Assert.assertFalse(set);
    }

    @Test
    public void testIterator() {
        CSVRecord record = createCSVRecord();
        Iterator<String> iterator = record.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("Value", iterator.next());
    }

    @Test
    public void testPutIn() {
        CSVRecord record = createCSVRecord();
        Map<String, String> map = record.putIn(new HashMap<String, String>());
        Assert.assertEquals("Value", map.get("Column1"));
    }

    @Test
    public void testSize() {
        CSVRecord record = createCSVRecord();
        int size = record.size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void testToList() {
        CSVRecord record = createCSVRecord();
        List<String> list = record.toList();
        Assert.assertEquals("Value", list.get(0));
    }

    @Test
    public void testToMap() {
        CSVRecord record = createCSVRecord();
        Map<String, String> map = record.toMap();
        Assert.assertEquals("Value", map.get("Column1"));
    }

    @Test
    public void testToString() {
        CSVRecord record = createCSVRecord();
        String string = record.toString();
        Assert.assertEquals("[Value]", string);
    }

    @Test
    public void testValues() {
        CSVRecord record = createCSVRecord();
        String[] values = record.values();
        Assert.assertEquals("Value", values[0]);
    }

    private CSVRecord createCSVRecord() {
        String[] values = new String[]{"Value"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        String comment = "Line comment";
        long recordNumber = 1L;
        return new CSVRecord(values, mapping, comment, recordNumber);
    }

    private CSVRecord createCSVRecordNoHeaderMapping() {
        String[] values = new String[]{"Value"};
        String comment = "Line comment";
        long recordNumber = 1L;
        return new CSVRecord(values, null, comment, recordNumber);
    }

    private CSVRecord createIncompleteRecord() {
        String[] values = new String[]{"Value"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        String comment = "Line comment";
        long recordNumber = 1L;
        return new CSVRecord(values, mapping, comment, recordNumber);
    }
}