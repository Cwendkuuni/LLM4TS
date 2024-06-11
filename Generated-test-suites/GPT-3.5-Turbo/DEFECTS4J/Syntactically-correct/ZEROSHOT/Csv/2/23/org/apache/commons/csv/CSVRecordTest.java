import org.junit.Test;
import org.junit.Assert;

public class CSVRecordTest {

    @Test
    public void testGet() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        Assert.assertEquals("Value1", record.get(0));
        Assert.assertEquals("Value2", record.get(1));
        Assert.assertEquals("Value3", record.get(2));
    }

    @Test
    public void testGetWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, Map.of("Column1", 0, "Column2", 1, "Column3", 2), null, 1);
        Assert.assertEquals("Value1", record.get("Column1"));
        Assert.assertEquals("Value2", record.get("Column2"));
        Assert.assertEquals("Value3", record.get("Column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        record.get("Column1");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        Assert.assertTrue(record1.isConsistent());

        CSVRecord record2 = new CSVRecord(new String[]{"Value1", "Value2"}, Map.of("Column1", 0, "Column2", 1), null, 1);
        Assert.assertTrue(record2.isConsistent());

        CSVRecord record3 = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, Map.of("Column1", 0, "Column2", 1, "Column3", 2), null, 1);
        Assert.assertTrue(record3.isConsistent());

        CSVRecord record4 = new CSVRecord(new String[]{"Value1", "Value2"}, Map.of("Column1", 0, "Column2", 1, "Column3", 2), null, 1);
        Assert.assertFalse(record4.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record1 = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        Assert.assertFalse(record1.isMapped("Column1"));
        Assert.assertFalse(record1.isMapped("Column2"));
        Assert.assertFalse(record1.isMapped("Column3"));

        CSVRecord record2 = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, Map.of("Column1", 0, "Column2", 1, "Column3", 2), null, 1);
        Assert.assertTrue(record2.isMapped("Column1"));
        Assert.assertTrue(record2.isMapped("Column2"));
        Assert.assertTrue(record2.isMapped("Column3"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record1 = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        Assert.assertFalse(record1.isSet("Column1"));
        Assert.assertFalse(record1.isSet("Column2"));
        Assert.assertFalse(record1.isSet("Column3"));

        CSVRecord record2 = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, Map.of("Column1", 0, "Column2", 1, "Column3", 2), null, 1);
        Assert.assertTrue(record2.isSet("Column1"));
        Assert.assertTrue(record2.isSet("Column2"));
        Assert.assertTrue(record2.isSet("Column3"));
        Assert.assertFalse(record2.isSet("Column4"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        StringBuilder sb = new StringBuilder();
        for (String value : record) {
            sb.append(value);
        }
        Assert.assertEquals("Value1Value2Value3", sb.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        Assert.assertArrayEquals(new String[]{"Value1", "Value2", "Value3"}, record.values());
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, "This is a comment", 1);
        Assert.assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        Assert.assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, null, null, 1);
        Assert.assertEquals("[Value1, Value2, Value3]", record.toString());
    }
}