import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSVFile() throws IOException {
        CSVFormat format = new CSVFormat(';', '\'\''); // Use ';' as delimiter and double quotes as escape character
        CSVParser parser = CSVParser.parse(new StringReader("A;B;C\n1,2,\"3,4\"\n5;6;7"), format);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        CSVRecord secondRecord = records.get(1);

        // Check values in the first record
        assertEquals("1", firstRecord.get(0).toString());
        assertEquals("2", firstRecord.get(1).toString());
        assertEquals("3,4", firstRecord.get(2).toString());

        // Check values in the second record
        assertEquals("5", secondRecord.get(0).toString());
        assertEquals("6", secondRecord.get(1).toString());
        assertEquals("7", secondRecord.get(2).toString());
    }

    @Test
    public void testParseEmptyCSVFile() throws IOException {
        CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new StringReader(""), format);
        List<CSVRecord> records = parser.getRecords();

        assertTrue(records.isEmpty());
    }

    @Test
    public void testParseCSVFileWithInvalidFormat() throws IOException {
        CSVFormat format = new CSVFormat();
        CSVParser parser = null;

        try {
            parser = CSVParser.parse(new StringReader("A,B,C\n1,2,3"), format);
            parser.getRecords();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    @Test
    public void testParseCSVFileWithOnlyHeader() throws IOException {
        CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new StringReader("A,B,C"), format);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(0, records.size());
        Assert.assertNotNull(parser.getHeaderMap());
        assertEquals(3, parser.getHeaderMap().size());
        assertEquals("A", parser.getHeaderMap().get("A"));
        assertEquals("B", parser.getHeaderMap().get("B"));
        assertEquals("C", parser.getHeaderMap().get("C"));

        parser.close();
    }

    @Test
    public void testParseCSVFileWithMissingValue() throws IOException {
        CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new StringReader("A,B\n1,2"), format);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals(2, record.size());

        try {
            record.get(2);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        parser.close();
    }

    @Test
    public void testParseCSVFileWithEmptyValue() throws IOException {
        CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new StringReader("A,B\n1,\"\",3"), format);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals(3, record.size());

        // Empty values are represented as null
        assertEquals(null, record.get(1));

        parser.close();
    }

    @Test
    public void testParseCSVFileWithQuotationMarks() throws IOException {
        CSVFormat format = new CSVFormat(';', '\'\'');
        CSVParser parser = CSVParser.parse(new StringReader("A;\"B,C\"\n1;\"1,2,3\"\n5;7;\"8,9,10\"\n"), format);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());

        CSVRecord firstRecord = records.get(0);
        assertEquals(3, firstRecord.size());
        assertEquals("B,C", firstRecord.get(0).toString());

        CSVRecord secondRecord = records.get(1);
        assertEquals(3, secondRecord.size());
        assertEquals("1,2,3", secondRecord.get(0).toString());

        CSVRecord thirdRecord = records.get(2);
        assertEquals(3, thirdRecord.size());
        assertEquals("8,9,10", thirdRecord.get(0).toString());

        parser.close();
    }
}