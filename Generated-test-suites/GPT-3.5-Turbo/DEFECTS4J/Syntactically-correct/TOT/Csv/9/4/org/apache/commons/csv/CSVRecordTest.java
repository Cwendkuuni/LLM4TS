import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByEnumTypicalUseCase() {
        CSVRecord csvRecord = createCSVRecord();
        String value = csvRecord.get(MyEnum.VALUE_1);
        Assert.assertEquals("expected value", value);
    }

    @Test
    public void testGetByEnumEdgeCase() {
        CSVRecord csvRecord = createCSVRecord();
        String value = csvRecord.get(MyEnum.VALUE_3);
        Assert.assertEquals(null, value);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByIndexErrorScenario() {
        CSVRecord csvRecord = createCSVRecord();
        String value = csvRecord.get(3);
    }

    @Test
    public void testGetByNameTypicalUseCase() {
        CSVRecord csvRecord = createCSVRecord();
        String value = csvRecord.get("name_1");
        Assert.assertEquals("expected value", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameErrorScenario() {
        CSVRecord csvRecord = new CSVRecord(null, null, null, 0);
        csvRecord.get("name_1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidErrorScenario() {
        CSVRecord csvRecord = createCSVRecord();
        csvRecord.get("invalid_name");
    }

    @Test
    public void testGetCommentTypicalUseCase() {
        CSVRecord csvRecord = createCSVRecord();
        String comment = csvRecord.getComment();
        Assert.assertEquals("expected comment", comment);
    }

    @Test
    public void testGetCommentEdgeCase() {
        CSVRecord csvRecord = createCSVRecordWithoutComment();
        String comment = csvRecord.getComment();
        Assert.assertEquals(null, comment);
    }

    @Test
    public void testGetRecordNumberTypicalUseCase() {
        CSVRecord csvRecord = createCSVRecord();
        long recordNumber = csvRecord.getRecordNumber();
        Assert.assertEquals(1, recordNumber);
    }

    @Test
    public void testIsConsistentTypicalUseCase() {
        CSVRecord csvRecord = createCSVRecord();
        boolean isConsistent = csvRecord.isConsistent();
        Assert.assertTrue(isConsistent);
    }

    @Test
    public void testIsConsistentEdgeCase() {
        // Test record with inconsistent mapping
        CSVRecord csvRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        boolean isConsistent = csvRecord.isConsistent();
        Assert.assertFalse(isConsistent);
    }

    @Test
    public void testIsMappedTypicalUseCase() {
        CSVRecord csvRecord = createCSVRecord();
        boolean isMapped = csvRecord.isMapped("name_1");
        Assert.assertTrue(isMapped);
    }

    @Test
    public void testIsMappedEdgeCase() {
        CSVRecord csvRecord = createCSVRecord();
        boolean isMapped = csvRecord.isMapped("invalid_name");
        Assert.assertFalse(isMapped);
    }

    @Test
    public void testIsSetTypicalUseCase() {
        CSVRecord csvRecord = createCSVRecord();
        boolean isSet = csvRecord.isSet("name_1");
        Assert.assertTrue(isSet);
    }

    @Test
    public void testIsSetEdgeCase() {
        CSVRecord csvRecord = createCSVRecord();
        boolean isSet = csvRecord.isSet("name_3");
        Assert.assertFalse(isSet);
    }

    @Test
    public void testIteratorTypicalUseCase() {
        CSVRecord csvRecord = createCSVRecord();
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        Assert.assertEquals(3, count);
    }

    @Test
    public void testPutInTypicalUseCase() {
        CSVRecord csvRecord = createCSVRecord();
        Map<String, String> map = new HashMap<>();
        csvRecord.putIn(map);
        Assert.assertEquals("expected value", map.get("name_1"));
    }

    @Test
    public void testSizeTypicalUseCase() {
        CSVRecord csvRecord = createCSVRecord();
        int size = csvRecord.size();
        Assert.assertEquals(3, size);
    }

    @Test
    public void testToListTypicalUseCase() {
        CSVRecord csvRecord = createCSVRecord();
        List<String> list = csvRecord.toList();
        Assert.assertEquals(3, list.size());
        Assert.assertEquals("expected value", list.get(0));
    }

    @Test
    public void testToMapTypicalUseCase() {
        CSVRecord csvRecord = createCSVRecord();
        Map<String, String> map = csvRecord.toMap();
        Assert.assertEquals("expected value", map.get("name_1"));
    }

    @Test
    public void testValuesTypicalUseCase() {
        CSVRecord csvRecord = createCSVRecord();
        String[] values = csvRecord.values();
        Assert.assertEquals(3, values.length);
        Assert.assertEquals("expected value", values[0]);
    }

    private CSVRecord createCSVRecord() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name_1", 0);
        mapping.put("name_2", 1);

        return new CSVRecord(new String[] {"expected value", "value2", "value3"}, mapping, "expected comment", 1);
    }

    private CSVRecord createCSVRecordWithoutComment() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name_1", 0);
        mapping.put("name_2", 1);

        return new CSVRecord(new String[] {"expected value", "value2", "value3"}, mapping, null, 1);
    }

    private enum MyEnum {
        VALUE_1, VALUE_2, VALUE_3
    }
}