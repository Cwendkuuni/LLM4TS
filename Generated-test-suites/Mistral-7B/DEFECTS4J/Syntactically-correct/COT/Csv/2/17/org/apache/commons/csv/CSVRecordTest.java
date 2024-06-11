import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetMethodWithValidIndex() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 0L);
        String expectedValue = "Name";
        String actualValue = record.get(0);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetMethodWithInvalidIndex() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 0L);
        try {
            record.get(-1);
            Assert.fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected exception
        }
    }

    @Test
    public void testGetMethodWithValidName() {
        String[] values = new String[]{"Name:John,Age:25,City:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        String expectedValue = "John";
        String actualValue = record.get("Name");
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMethodWithNoHeaderMapping() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 0L);
        record.get("Name");
    }

    @Test
    public void testIsConsistentMethodWithValidValues() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentMethodWithInvalidValues() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new ArrayList<>() {{
            add(new Integer(2), "Name");
            add(new Integer(0), "City");
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMappedMethodWithValidName() {
        String[] values = new String[]{"Name:John,Age:25,City:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        Assert.assertTrue(record.isMapped("Name"));
    }

    @Test
    public void testIsMappedMethodWithInvalidName() {
        String[] values = new String[]{"Name:John,Age:25,City:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        Assert.assertFalse(record.isMapped("CityName"));
    }

    @Test
    public void testIsSetMethodWithValidName() {
        String[] values = new String[]{"Name:John,Age:25,City:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        Assert.assertTrue(record.isSet("Name"));
    }

    @Test
    public void testIsSetMethodWithInvalidName() {
        String[] values = new String[]{"Name:John,Age:25,City:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        Assert.assertFalse(record.isSet("CityName"));
    }

    @Test
    public void testIteratorMethod() {
        String[] values = new String[]{"Name:John,Age:25,City:New York"};
        CSVRecord record = new CSVRecord(values, null, null, 0L);
        List<String> expectedValues = new ArrayList<>(Arrays.asList(values));
        Iterator<String> actualIterator = record.iterator();
        List<String> actualValues = new ArrayList<>();
        while (actualIterator.hasNext()) {
            actualValues.add(actualIterator.next());
        }
        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testSizeMethod() {
        String[] values = new String[]{"Name:John,Age:25,City:New York"};
        CSVRecord record = new CSVRecord(values, null, null, 0L);
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToStringMethod() {
        String[] values = new String[]{"Name:John,Age:25,City:New York"};
        CSVRecord record = new CSVRecord(values, null, null, 0L);
        String expectedValue = "[Name:John, Age:25, City:New York]";
        String actualValue = record.toString();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodWithInvalidName() {
        String[] values = new String[]{"Name:John,Age:25,City:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        record.get("CityName");
    }
}