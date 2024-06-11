import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    // Test for get(Enum<?> e)
    @Test
    public void testGetByEnum() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("Value1", record.get(Enum.valueOf("Header1", "Header1")));
    }

    // ... Other basic test cases for other methods

}