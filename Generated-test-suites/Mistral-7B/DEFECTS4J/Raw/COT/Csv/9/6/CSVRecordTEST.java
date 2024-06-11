import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstructor() {
        String[] values = new String[]{"name1", "value1", "comment"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("value1", 1);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);

        Assert.assertEquals("name1", record.get("name1"));
        Assert.assertEquals("value1", record.get("value1"));
        Assert.assertEquals("comment", record.getComment());
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertTrue(record.isConsistent());
        Assert.assertTrue(record.isMapped("name1"));
        Assert.assertTrue(record.isSet("name1"));
        Assert.assertEquals(2, record.size());
        Assert.assertFalse(record.isMapped("nonExistent"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordNoHeaderMapping() {
        String[] values = new String[]{"name1", "value1"};
        CSVRecord record = new CSVRecord(values, null, "", 1);
        record.get("nonExistent");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordInvalidColumnName() {
        String[] values = new String[]{"name1", "value1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("value1", 1);
        mapping.put("nonExistent", 2);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        record.get("nonExistent");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordInvalidColumnIndex() {
        String[] values = new String[]{"name1", "value1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("value1", 1);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        record.get(2);
    }

    @Test
    public void testCSVRecordGetByEnum() {
        String[] values = new String[]{"name1", "value1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("value1", 1);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        Enum<String> enumName = new Enum<String>("NAME1") {
            @Override
            public String toString() {
                return "NAME1";
            }
        };

        Assert.assertEquals("name1", record.get(enumName));
    }

    @Test
    public void testCSVRecordGetByNullName() {
        String[] values = new String[]{"name1", "value1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("value1", 1);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        Assert.assertNull(record.get(null));
    }

    @Test
    public void testCSVRecordGetByEmptyName() {
        String[] values = new String[]{"name1", "value1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("value1", 1);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        Assert.assertNull(record.get(""));
    }

    @Test
    public void testCSVRecordGetByEmptyArray() {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        Assert.assertNull(record.get(new String[0]));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCSVRecordInvalidGetByIndex() {
        String[] values = new String[]{"name1", "value1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("value1", 1);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        record.get(2);
    }

    @Test
    public void testCSVRecordIterable() {
        String[] values = new String[]{"name1", "value1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("value1", 1);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);

        Iterator<String> iterator = record.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("name1", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("value1", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testCSVRecordToString() {
        String[] values = new String[]{"name1", "value1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("value1", 1);

        CSVRecord record = new CSVRecord(values, mapping, "", 1);
        Assert.assertEquals("[name1, value1]", record.toString());
    }

    @Test
    public void testCSVRecordValues() {
        String[] values = new String[]{"name1", "value1"};

        CSVRecord record = new CSVRecord(values, null, "", 1);
        Assert.assertArrayEquals(values, record.values());
    }

    @Test
    public void testCSVRecordPutIn() {
        String[] values = new String[]{"name1", "value1"};
        Map<String, String> map = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, "", 1);
        map.putAll(record.putIn(map));

        Assert.assertEquals("name1", map.get("name1"));
        Assert.assertEquals("value1", map.get("value1"));
        Assert.assertNull(map.get("nonExistent"));
    }

}