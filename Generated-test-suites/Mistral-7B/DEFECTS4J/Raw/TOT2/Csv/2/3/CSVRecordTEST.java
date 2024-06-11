import static org.junit.Assert.*;
import org.apache.commons.csv.CSVRecord;

public class CSVRecordTest {
    @Test
    public void testGetMethodByIndex() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        String expected = "Name";
        String actual = record.get(0);
        assertEquals(expected, actual);
    }
}