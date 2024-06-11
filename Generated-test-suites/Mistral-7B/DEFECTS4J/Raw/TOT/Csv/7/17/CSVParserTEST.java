import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSV() throws IOException {
        String csvData = "Name,Age\nJohn,30\nMary,25\n";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = CSVParser.parse(new StringReader(csvData), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(30, record.get(1).intValue());
            Assert.assertNull(record.getComment());
        }

        parser.close();
    }

    @Test
    public void testParseCSVWithEmptyLine() throws IOException {
        String csvData = "Name,Age\nJohn,30\n\nMary,25\n";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = CSVParser.parse(new StringReader(csvData), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(30, record.get(1).intValue());
            Assert.assertNull(record.getComment());
        }

        parser.close();
    }

    @Test
    public void testParseCSVWithInvalidCSV() throws IOException {
        String csvData = "Name,Age\nJohn,30\nMary,25\nName,25";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        try {
            CSVParser parser = CSVParser.parse(new StringReader(csvData), format);
            parser.getRecords();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        parser.close();
    }

    @Test
    public void testParseCSVWithCustomFormat() throws IOException {
        String csvData = "Name,Age\nJohn,30\nMary,25\n";
        CSVFormat format = new CSVFormatBuilder()
                .setHeaderNames(new String[]{"name", "age"}).setDelimiter(';').build();
        CSVParser parser = CSVParser.parse(new StringReader(csvData), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(30, record.get(1).intValue());
            Assert.assertNull(record.getComment());
        }

        parser.close();
    }

    @Test
    public void testParseCSVWithCustomComment() throws IOException {
        String csvData = "Name,Age\nJohn,30#Comment1\nMary,25#Comment2";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = CSVParser.parse(new StringReader(csvData), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(30, record.get(1).intValue());
            Assert.assertEquals("Comment1", record.getComment());
        }

        parser.close();
    }
}