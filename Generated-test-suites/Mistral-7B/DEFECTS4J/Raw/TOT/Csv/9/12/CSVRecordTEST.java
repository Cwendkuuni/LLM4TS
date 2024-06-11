import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setup() {
        String[] values = new String[]{"John Doe", "35", "jdoe@example.com", "New York", "Sales"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        mapping.put("City", 3);
        mapping.put("Department", 4);

        record = new CSVRecord(values, mapping, null, 1);
    }

    @Test
    public void testGetEnum() {
        Enum<String> departmentEnum = Enum.valueOf(String.class, "Department");
        String expectedValue = "Sales";
        String actualValue = record.get(departmentEnum);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetByIndex() {
        String expectedValue = "John Doe";
        String actualValue = record.get(0);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetByName() {
        String expectedValue = "New York";
        String actualValue = record.get("City");
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithNoMapping() {
        String unexpectedValue = "UnmappedColumn";
        String actualValue = record.get(unexpectedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidIndex() {
        int unexpectedIndex = 5;
        String actualValue = record.get(unexpectedIndex);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidName() {
        String unexpectedName = "UnmappedColumn";
        String actualValue = record.get(unexpectedName);
    }

    @Test
    public void testGetComment() {
        String expectedValue = null;
        String actualValue = record.getComment();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetRecordNumber() {
        long expectedValue = 1;
        long actualValue = record.getRecordNumber();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsConsistent() {
        boolean expectedValue = true;
        boolean actualValue = record.isConsistent();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsMapped() {
        boolean expectedValue = true;
        String name = "City";
        boolean actualValue = record.isMapped(name);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsSet() {
        boolean expectedValue = true;
        String name = "City";
        boolean actualValue = record.isSet(name);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();
        String[] expectedValues = {"John Doe", "35", "jdoe@example.com", "New York", "Sales"};
        int i = 0;
        while (iterator.hasNext()) {
            String actualValue = iterator.next();
            Assert.assertEquals(expectedValues[i], actualValue);
            i++;
        }
    }

    @Test
    public void testPutIn() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "John Doe");
        expectedMap.put("Age", "35");
        expectedMap.put("Email", "jdoe@example.com");
        expectedMap.put("City", "New York");
        expectedMap.put("Department", "Sales");

        Map<String, String> actualMap = record.putIn(new HashMap<String, String>(5));
        Assert.assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testSize() {
        int expectedValue = 5;
        int actualValue = record.size();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testToString() {
        String expectedValue = "[John Doe, 35, jdoe@example.com, New York, Sales]";
        String actualValue = record.toString();
        Assert.assertEquals(expectedValue, actualValue);
    }
}