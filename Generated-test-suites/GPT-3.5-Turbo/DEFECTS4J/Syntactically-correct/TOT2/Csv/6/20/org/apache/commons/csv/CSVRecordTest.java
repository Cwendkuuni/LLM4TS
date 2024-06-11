import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.COLUMN1));
        assertEquals("value2", csvRecord.get(TestEnum.COLUMN2));
        assertEquals("value3", csvRecord.get(TestEnum.COLUMN3));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNoMapping() {
        CSVRecord csvRecordNoMapping = new CSVRecord(new String[]{"value1"}, null, "No mapping", 2);
        csvRecordNoMapping.get("column1");
    }

    // Additional test cases for edge cases and error scenarios can be added here

}