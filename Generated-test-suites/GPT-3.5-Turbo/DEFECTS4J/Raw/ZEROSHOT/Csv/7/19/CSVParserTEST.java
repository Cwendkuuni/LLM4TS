import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Arrays;

public class CSVParserTest {
    private static CSVParser parser;
    
    @BeforeClass
    public static void setUp() throws IOException {
        String csvData = "name,age\nJohn,25\nAlice,30";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        parser = CSVParser.parse(csvData, format);
    }
    
    @Test
    public void testGetHeaderMap() {
        Map<String, Integer> expectedHeaderMap = new LinkedHashMap<>();
        expectedHeaderMap.put("name", 0);
        expectedHeaderMap.put("age", 1);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        Assert.assertEquals(expectedHeaderMap, headerMap);
    }
    
    @Test
    public void testGetRecordNumber() {
        long expectedRecordNumber = 0;
        long recordNumber = parser.getRecordNumber();
        Assert.assertEquals(expectedRecordNumber, recordNumber);
    }
    
    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> expectedRecords = new ArrayList<>();
        String[] record1 = {"John", "25"};
        String[] record2 = {"Alice", "30"};
        List<String[]> records = Arrays.asList(record1, record2);
        for (String[] values : records) {
            CSVRecord record = new CSVRecord(values, parser.getHeaderMap(), null, parser.getRecordNumber());
            expectedRecords.add(record);
        }
        List<CSVRecord> actualRecords = parser.getRecords();
        Assert.assertEquals(expectedRecords, actualRecords);
    }
}