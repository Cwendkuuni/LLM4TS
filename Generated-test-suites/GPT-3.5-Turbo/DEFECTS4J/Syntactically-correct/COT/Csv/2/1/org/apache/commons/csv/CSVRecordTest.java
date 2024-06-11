
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        // Arrange
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);

        // Act
        String result = csvRecord.get(1);

        // Assert
        Assert.assertEquals("value2", result);
    }

    // Rest of the test methods...


}
