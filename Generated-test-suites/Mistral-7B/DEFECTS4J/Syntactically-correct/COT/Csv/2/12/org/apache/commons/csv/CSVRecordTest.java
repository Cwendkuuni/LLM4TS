import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetMethodWithValidIndex() {
        String[] mockValues = new String[]{"John", "Doe", "30"};
        CSVRecord csvRecord = new CSVRecord(mockValues, null, null, 1L);

        String expectedValue = "Doe";
        String actualValue = csvRecord.get(1);

        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetMethodWithInvalidIndex() {
        String[] mockValues = new String[]{"John", "Doe", "30"};
        CSVRecord csvRecord = new CSVRecord(mockValues, null, null, 1L);

        Assert.assertNull(csvRecord.get(3));
    }

    @Test
    public void testGetMethodWithValidName() {
        String[] mockValues = new String[]{"Name:John,Age:30,City:New York"};
        CSVRecord csvRecord = new CSVRecord(mockValues, createMapFromStringArray(mockValues), null, 1L);

        String expectedValue = "John";
        String actualValue = csvRecord.get("Name");

        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMethodWithInvalidNameWhenNoMapping() {
        String[] mockValues = new String[]{"Name:John,Age:30,City:New York"};
        CSVRecord csvRecord = new CSVRecord(mockValues, null, null, 1L);

        String invalidName = "InvalidName";
        csvRecord.get(invalidName);
    }

    @Test
    public void testGetMethodWithValidNameWhenMappingExists() {
        String[] mockValues = new String[]{"Name:John,Age:30,City:New York"};
        CSVRecord csvRecord = new CSVRecord(mockValues, createMapFromStringArray(mockValues), null, 1L);

        String expectedValue = "John";
        String actualValue = csvRecord.get("Name");

        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsConsistentMethodWhenConsistent() {
        String[] mockValues = new String[]{"Name:John,Age:30,City:New York"};
        CSVRecord csvRecord = new CSVRecord(mockValues, createMapFromStringArray(mockValues), null, 1L);

        boolean expectedResult = true;
        boolean actualResult = csvRecord.isConsistent();

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsConsistentMethodWhenInconsistent() {
        String[] mockValues = new String[]{"Name:John,Age:30,City:New York", "Name:John,City:New York"};
        CSVRecord csvRecord = new CSVRecord(mockValues, null, null, 1L);

        boolean expectedResult = false;
        boolean actualResult = csvRecord.isConsistent();

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsMappedMethodWhenMapped() {
        String[] mockValues = new String[]{"Name:John,Age:30,City:New York"};
        CSVRecord csvRecord = new CSVRecord(mockValues, createMapFromStringArray(mockValues), null, 1L);

        boolean expectedResult = true;
        boolean actualResult = csvRecord.isMapped("Name");

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsMappedMethodWhenNotMapped() {
        String[] mockValues = new String[]{"Name:John,Age:30,City:New York"};
        CSVRecord csvRecord = new CSVRecord(mockValues, null, null, 1L);

        boolean expectedResult = false;
        boolean actualResult = csvRecord.isMapped("InvalidName");

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsSetMethodWhenSet() {
        String[] mockValues = new String[]{"Name:John,Age:30,City:New York"};
        CSVRecord csvRecord = new CSVRecord(mockValues, createMapFromStringArray(mockValues), null, 1L);

        boolean expectedResult = true;
        boolean actualResult = csvRecord.isSet("Age");

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsSetMethodWhenNotSet() {
        String[] mockValues = new String[]{"Name:John,Age:30,City:New York"};
        CSVRecord csvRecord = new CSVRecord(mockValues, createMapFromStringArray(mockValues), null, 1L);

        boolean expectedResult = false;
        boolean actualResult = csvRecord.isSet("InvalidName");

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToStringMethod() {
        String[] mockValues = new String[]{"Name:John,Age:30,City:New York"};
        CSVRecord csvRecord = new CSVRecord(mockValues, null, null, 1L);

        String expectedValue = "[Name:John, Age:30, City:New York]";
        String actualValue = csvRecord.toString();

        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidMethod() {
        String[] mockValues = new String[]{"Name:John,Age:30,City:New York"};
        CSVRecord csvRecord = new CSVRecord(mockValues, null, null, 1L);

        csvRecord.get(3.0);
    }

    @Test
    public void testSizeMethod() {
        String[] mockValues = new String[]{"Name:John,Age:30,City:New York"};
        CSVRecord csvRecord = new CSVRecord(mockValues, null, null, 1L);

        int expectedValue = 3;
        int actualValue = csvRecord.size();

        Assert.assertEquals(expectedValue, actualValue);
    }

    private Map<String, Integer> createMapFromStringArray(String[] array) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            String[] elements = array[i].split(":");
            map.put(elements[0], i);
        }
        return map;
    }
}