import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {
    @Test
    public void testGetInt() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        String expectedValue = "30";

        String actualValue = record.get(2);
        Assert.assertEquals(expectedValue, actualValue);
    }
}