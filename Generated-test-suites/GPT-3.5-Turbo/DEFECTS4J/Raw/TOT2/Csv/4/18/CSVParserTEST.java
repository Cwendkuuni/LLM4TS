import org.junit.Test;
import org.junit.Assert;

public class CSVParserTest {

    @Test
    public void testParseFile() {
        // Test parsing a CSV file with valid CSVFormat
        CSVParser parser = null;
        try {
            parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT);
            Assert.assertNotNull(parser);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown here");
        } finally {
            if (parser != null) {
                try {
                    parser.close();
                } catch (IOException e) {
                    Assert.fail("IOException should not be thrown here");
                }
            }
        }
    }

    @Test
    public void testParseString() {
        // Test parsing a CSV string with valid CSVFormat
        CSVParser parser = null;
        try {
            parser = CSVParser.parse("name,age\nJohn,30", CSVFormat.DEFAULT);
            Assert.assertNotNull(parser);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown here");
        } finally {
            if (parser != null) {
                try {
                    parser.close();
                } catch (IOException e) {
                    Assert.fail("IOException should not be thrown here");
                }
            }
        }
    }

    // Add more test cases for other methods

}