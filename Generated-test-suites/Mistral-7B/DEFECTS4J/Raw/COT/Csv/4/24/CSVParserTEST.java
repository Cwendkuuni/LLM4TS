import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Test class for CSVParser.
 */
public class CSVParserTest {

    /**
     * Test method for {@link CSVParser#parse(File, CSVFormat)}.
     */
    @Test
    public void testParseFile() throws IOException {
        File inputFile = new File("testCSVFile.csv");
        CSVParser parser = CSVParser.parse(inputFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Verify the number of records
        Assert.assertEquals(3, records.size());

        // Verify record values
        for (CSVRecord record : records) {
            Assert.assertNotNull(record.getValues());
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Header1", record.get(0));
            Assert.assertEquals("Header2", record.get(1));
            Assert.assertEquals("Header3", record.get(2));
        }

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(String, CSVFormat)}.
     */
    @Test
    public void testParseString() throws IOException {
        String csvData = "Header1,Header2,Header3\n" +
                "Value1_1,Value1_2,Value1_3\n" +
                "Value2_1,Value2_2,Value2_3\n";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Verify the number of records
        Assert.assertEquals(2, records.size());

        // Verify record values
        for (CSVRecord record : records) {
            Assert.assertNotNull(record.getValues());
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Value1_1", record.get(0));
            Assert.assertEquals("Value1_2", record.get(1));
            Assert.assertEquals("Value1_3", record.get(2));
        }

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(URL, Charset, CSVFormat)}.
     */
    @Test
    public void testParseURL() throws IOException {
        String csvData = "Header1,Header2,Header3\n" +
                "Value1_1,Value1_2,Value1_3\n" +
                "Value2_1,Value2_2,Value2_3\n";
        URL url = new URL("test:/csvparser/test.csv");

        CSVParser parser = new CSVParser(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Verify the number of records
        Assert.assertEquals(2, records.size());

        // Verify record values
        for (CSVRecord record : records) {
            Assert.assertNotNull(record.getValues());
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Value1_1", record.get(0));
            Assert.assertEquals("Value1_2", record.get(1));
            Assert.assertEquals("Value1_3", record.get(2));
        }

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)}.
     */
    @Test
    public void testParseReader() throws IOException {
        String csvData = "Header1,Header2,Header3\n" +
                "Value1_1,Value1_2,Value1_3\n" +
                "Value2_1,Value2_2,Value2_3\n";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Verify the number of records
        Assert.assertEquals(2, records.size());

        // Verify record values
        for (CSVRecord record : records) {
            Assert.assertNotNull(record.getValues());
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Value1_1", record.get(0));
            Assert.assertEquals("Value1_2", record.get(1));
            Assert.assertEquals("Value1_3", record.get(2));
        }

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#getRecords()}.
     */
    @Test
    public void testGetRecords() throws IOException {
        String csvData = "Header1,Header2,Header3\n" +
                "Value1_1,Value1_2,Value1_3\n" +
                "Value2_1,Value2_2,Value2_3\n";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Verify the number of records
        Assert.assertEquals(2, records.size());

        // Verify record values
        for (CSVRecord record : records) {
            Assert.assertNotNull(record.getValues());
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Value1_1", record.get(0));
            Assert.assertEquals("Value1_2", record.get(1));
            Assert.assertEquals("Value1_3", record.get(2));
        }
    }

    /**
     * Test method for {@link CSVParser#getHeaderMap()}.
     */
    @Test
    public void testGetHeaderMap() throws IOException {
        String csvData = "Header1,Header2,Header3\n" +
                "Value1_1,Value1_2,Value1_3\n" +
                "Value2_1,Value2_2,Value2_3\n";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        parser.close();

        Assert.assertEquals(3, headerMap.size());
        Assert.assertEquals("Header1", headerMap.get("0"));
        Assert.assertEquals("Header2", headerMap.get("1"));
        Assert.assertEquals("Header3", headerMap.get("2"));
    }

    /**
     * Test method for {@link CSVParser#getRecordNumber()}.
     */
    @Test
    public void testGetRecordNumber() throws IOException {
        String csvData = "Header1,Header2,Header3\n" +
                "Value1_1,Value1_2,Value1_3\n" +
                "Value2_1,Value2_2,Value2_3\n";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        Assert.assertEquals(2, parser.getRecordNumber());
    }

    /**
     * Test method for {@link CSVParser#isClosed()}.
     */
    @Test
    public void testIsClosed() throws IOException {
        String csvData = "Header1,Header2,Header3\n" +
                "Value1_1,Value1_2,Value1_3\n" +
                "Value2_1,Value2_2,Value2_3\n";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        Assert.assertFalse(parser.isClosed());

        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    /**
     * Test method for {@link CSVParser#iterator()}.
     */
    @Test
    public void testIterator() throws IOException {
        String csvData = "Header1,Header2,Header3\n" +
                "Value1_1,Value1_2,Value1_3\n" +
                "Value2_1,Value2_2,Value2_3\n";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();

        Assert.assertTrue(iterator.hasNext());
        CSVRecord record1 = iterator.next();

        Assert.assertEquals("Value1_1", record1.get(0));
        Assert.assertEquals("Value1_2", record1.get(1));
        Assert.assertEquals("Value1_3", record1.get(2));

        Assert.assertTrue(iterator.hasNext());
        CSVRecord record2 = iterator.next();

        Assert.assertEquals("Value2_1", record2.get(0));
        Assert.assertEquals("Value2_2", record2.get(1));
        Assert.assertEquals("Value2_3", record2.get(2));

        Assert.assertFalse(iterator.hasNext());

        parser.close();
    }
}