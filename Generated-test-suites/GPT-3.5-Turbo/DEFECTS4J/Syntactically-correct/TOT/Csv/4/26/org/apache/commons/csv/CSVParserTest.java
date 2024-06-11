
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(file, format);

        // Test number of records
        assertEquals(3, parser.getRecords().size());

        // Test values of first record
        CSVRecord record1 = parser.getRecords().get(0);
        assertEquals("John", record1.get(0));
        assertEquals("Doe", record1.get(1));

        // Test values of second record
        CSVRecord record2 = parser.getRecords().get(1);
        assertEquals("Jane", record2.get(0));
        assertEquals("Smith", record2.get(1));

        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String string = "John,Doe\nJane,Smith\n";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(string, format);

        // Test number of records
        assertEquals(2, parser.getRecords().size());

        // Test values of first record
        CSVRecord record1 = parser.getRecords().get(0);
        assertEquals("John", record1.get(0));
        assertEquals("Doe", record1.get(1));

        // Test values of second record
        CSVRecord record2 = parser.getRecords().get(1);
        assertEquals("Jane", record2.get(0));
        assertEquals("Smith", record2.get(1));

        parser.close();
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(url, charset, format);

        // Test number of records
        assertEquals(3, parser.getRecords().size());

        // Test values of first record
        CSVRecord record1 = parser.getRecords().get(0);
        assertEquals("John", record1.get(0));
        assertEquals("Doe", record1.get(1));

        // Test values of second record
        CSVRecord record2 = parser.getRecords().get(1);
        assertEquals("Jane", record2.get(0));
        assertEquals("Smith", record2.get(1));

        parser.close();
    }
}

