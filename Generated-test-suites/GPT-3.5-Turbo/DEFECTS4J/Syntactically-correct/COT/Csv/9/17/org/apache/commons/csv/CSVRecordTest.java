
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {
    @Test
    public void testGetEnum() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            null, null, 1);
        Assert.assertEquals("value1", csvRecord.get(TestEnum.VAL1));
        Assert.assertEquals("value2", csvRecord.get(TestEnum.VAL2));
        Assert.assertEquals("value3", csvRecord.get(TestEnum.VAL3));
    }

    @Test
    public void testGetIndex() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            null, null, 1);
        Assert.assertEquals("value1", csvRecord.get(0));
        Assert.assertEquals("value2", csvRecord.get(1));
        Assert.assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            mapping, null, 1);
        Assert.assertEquals("value1", csvRecord.get("col1"));
        Assert.assertEquals("value2", csvRecord.get("col2"));
        Assert.assertEquals("value3", csvRecord.get("col3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithoutHeaderMapping() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            null, null, 1);
        csvRecord.get("col1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameNotFound() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            mapping, null, 1);
        csvRecord.get("col4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameOutOfIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 3);

        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            mapping, null, 1);
        csvRecord.get("col3");

    }

    @Test
    public void testIsConsistent() {
        CSVRecord csvRecord1 = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            null, null, 1);
        Assert.assertTrue(csvRecord1.isConsistent());

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord csvRecord2 = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            mapping, null, 1);
        Assert.assertTrue(csvRecord2.isConsistent());

        CSVRecord csvRecord3 = new CSVRecord(new String[]{"value1", "value2"},
                                            mapping, null, 1);
        Assert.assertFalse(csvRecord3.isConsistent());

    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            mapping, null, 1);
        Assert.assertTrue(csvRecord.isMapped("col1"));
        Assert.assertTrue(csvRecord.isMapped("col2"));
        Assert.assertTrue(csvRecord.isMapped("col3"));
        Assert.assertFalse(csvRecord.isMapped("col4"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            mapping, null, 1);
        Assert.assertTrue(csvRecord.isSet("col1"));
        Assert.assertTrue(csvRecord.isSet("col2"));
        Assert.assertTrue(csvRecord.isSet("col3"));
        Assert.assertFalse(csvRecord.isSet("col4"));
    }

    @Test
    public void testGetComment() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            null, "This is a comment", 1);
        Assert.assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            null, null, 10);
        Assert.assertEquals(10, csvRecord.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            null, null, 1);
        Assert.assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            null, null, 1);
        Assert.assertEquals(Arrays.asList("value1", "value2", "value3"), csvRecord.toList());
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            mapping, null, 1);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("col1", "value1");
        expectedMap.put("col2", "value2");
        expectedMap.put("col3", "value3");
        Assert.assertEquals(expectedMap, csvRecord.toMap());
    }

    @Test
    public void testValues() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                            null, null, 1);
        Assert.assertArrayEquals(new String[]{"value1", "value2", "value3"}, csvRecord.values());
    }
}

enum TestEnum {
    VAL1, VAL2, VAL3;
}
