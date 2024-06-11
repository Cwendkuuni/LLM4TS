import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnumValue() {
        CSVRecord record = new CSVRecord(new String[]{"Hello", "World"}, null, null, 1);
        assertEquals("Hello", record.get(Enum.valueOf(CSVRecordTest.class, "HELLO")));
    }
}