import static org.junit.Assert.assertEquals;
import org.apache.commons.csv.CSVRecord;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = new String[]{"Alice", "25", "alice@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        String expected = "Alice";
        String actual = record.get(0);
        assertEquals(expected, actual);
    }
}