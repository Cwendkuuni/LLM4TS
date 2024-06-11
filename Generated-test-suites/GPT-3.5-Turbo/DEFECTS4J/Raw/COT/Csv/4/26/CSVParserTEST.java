import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @Test
    public void testParseFile() {
        try {
            CSVParser parser = CSVParser.parse(new File("example.csv"), CSVFormat.DEFAULT);
            List<CSVRecord> records = parser.getRecords();
            Assert.assertEquals(3, records.size());
            
            // Assert values for each record if needed
            
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testParseString() {
        try {
            String csvString = "Header1,Header2\nValue1,Value2\nValue3,Value4";
            CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
            List<CSVRecord> records = parser.getRecords();
            Assert.assertEquals(2, records.size());
            
            // Assert values for each record if needed
            
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testParseURL() {
        try {
            URL url = new URL("http://www.example.com/example.csv");
            CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
            List<CSVRecord> records = parser.getRecords();
            Assert.assertEquals(3, records.size());
            
            // Assert values for each record if needed
            
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}