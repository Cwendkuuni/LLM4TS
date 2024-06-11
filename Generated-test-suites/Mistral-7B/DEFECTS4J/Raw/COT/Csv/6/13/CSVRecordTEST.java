import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstructor() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertArrayEquals(values, record.values());
        Assert.assertEquals(mapping, record.mapping);
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertNull(record.getComment());
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testGetMethod() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("John", record.get(0));
        Assert.assertEquals("Doe", record.get(1));
        Assert.assertEquals("30", record.get(2));
        Assert.assertEquals("M", record.get(3));
        Assert.assertEquals("John", record.get("Name"));
        Assert.assertEquals("30", record.get("Age"));
        Assert.assertEquals("M", record.get("Gender"));
        Assert.assertNull(record.get("Unknown"));
        Assert.assertNull(record.get((Enum<?>) null));
        Assert.assertNull(record.get(-1));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMethodWithoutMapping() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodInvalidIndex() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodInvalidName() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("Unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodInvalidEnum() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get((Enum<?>) null);
    }

    @Test
    public void testGetCommentMethod() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, "Comment", 1);

        Assert.assertEquals("Comment", record.getComment());
    }

    @Test
    public void testGetRecordNumberMethod() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistentMethod() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isConsistent());

        String[] values2 = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping2 = new HashMap<>();
        mapping2.put("Name", 0);
        mapping2.put("Age", 1);

        CSVRecord record2 = new CSVRecord(values2, mapping2, null, 1);

        Assert.assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMappedMethod() {
        String[] values = new String[]{"Name:John,Age:30,Gender:M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertTrue(record.isMapped("Gender"));
    }

    @Test
    public void testIsSetMethod() {
        String[] values = new String[]{"Name:John,Age:30,Gender:M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertTrue(record.isSet("Gender"));
        Assert.assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testIteratorMethod() {
        String[] values = new String[]{"Name:John,Age:30,Gender:M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Iterator<String> iterator = record.iterator();

        Assert.assertEquals("Name:John", iterator.next());
        Assert.assertEquals("Age:30", iterator.next());
        Assert.assertEquals("Gender:M", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutInMethod() {
        String[] values = new String[]{"Name:John,Age:30,Gender:M"};
        Map<String, String> map = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 1);
        map = record.putIn(map);

        Assert.assertEquals("Name:John", map.get("Name"));
        Assert.assertEquals("Age:30", map.get("Age"));
        Assert.assertEquals("Gender:M", map.get("Gender"));
        Assert.assertEquals(3, map.size());
    }

    @Test
    public void testSizeMethod() {
        String[] values = new String[]{"Name:John,Age:30,Gender:M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToStringMethod() {
        String[] values = new String[]{"Name:John,Age:30,Gender:M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("[Name:John, Age:30, Gender:M]", record.toString());
    }

    @Test
    public void testValuesMethod() {
        String[] values = new String[]{"Name:John,Age:30,Gender:M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertArrayEquals(values, record.values());
    }

}