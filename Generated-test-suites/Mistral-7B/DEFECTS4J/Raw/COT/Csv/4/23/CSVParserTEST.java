import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File csvFile = new File("testData/test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertNotNull(records);
        Assert.assertEquals(4, records.size());

        for (CSVRecord record : records) {
            for (int i = 0; i < record.size(); i++) {
                Assert.assertNotNull(record.get(i));
            }
        }

        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "A,B\nC,D";
        CSVParser parser = new CSVParser(new StringReader(csvString), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertNotNull(records);
        Assert.assertEquals(1, records.size());
        Assert.assertEquals(2, records.get(0).size());

        for (int i = 0; i < records.get(0).size(); i++) {
            Assert.assertNotNull(records.get(0).get(i));
        }

        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFile() {
        File csvFile = null;
        CSVFormat format = CSVFormat.DEFAULT;

        try {
            CSVParser.parse(csvFile, format);
        } catch (IOException e) {
            // expected IOException, check for IllegalArgumentException
            Assert.assertNull(e.getCause());
            throw e;
        }

        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() {
        File csvFile = new File("testData/test.csv");
        CSVFormat format = null;

        try {
            CSVParser.parse(csvFile, format);
        } catch (IOException e) {
            // expected IOException, check for IllegalArgumentException
            Assert.assertNull(e.getCause());
            throw e;
        }

        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        File csvFile = new File("testData/invalidFormat.csv");
        CSVFormat format = new CSVFormat();

        try {
            CSVParser.parse(csvFile, format);
        } catch (IOException e) {
            Assert.assertEquals("Failed to parse header", e.getMessage());
            throw e;
        }

        Assert.fail();
    }

    @Test
    public void testEmptyFile() throws IOException {
        File csvFile = new File("testData/empty.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertNotNull(records);
        Assert.assertEquals(0, records.size());

        parser.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        URL url = getClass().getResource("/testData/test.csv");
        CSVParser parser = new CSVParser(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertNotNull(records);
        Assert.assertEquals(4, records.size());

        for (CSVRecord record : records) {
            for (int i = 0; i < record.size(); i++) {
                Assert.assertNotNull(record.get(i));
            }
        }

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testNullReader() {
        Reader reader = null;
        CSVFormat format = CSVFormat.DEFAULT;

        try {
            new CSVParser(reader, format);
        } catch (IOException e) {
            Assert.assertEquals("Reader is marked closed", e.getMessage());
            throw e;
        }

        Assert.fail();
    }

    @Test(expected = IOException.class)
    public void testClosedReader() throws IOException {
        Reader reader = new StringReader("A,B");
        reader.close();
        CSVFormat format = CSVFormat.DEFAULT;

        try {
            new CSVParser(reader, format);
        } catch (IOException e) {
            Assert.assertEquals("Reader is marked closed", e.getMessage());
            throw e;
        }

        Assert.fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyIterator() {
        File csvFile = new File("testData/empty.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();

        iterator.next();

        try {
            iterator.next();
        } catch (Exception e) {
            Assert.assertEquals(NoSuchElementException.class, e.getClass());
        }

        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyRecord() throws IOException {
        String csvString = "A\n";
        CSVParser parser = new CSVParser(new StringReader(csvString), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(1, records.size());
        Assert.assertEquals(0, records.get(0).size());

        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNullRecord() throws IOException {
        File csvFile = new File("testData/null.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Assert.assertNotNull(record);
        Assert.assertEquals(0, record.size());

        parser.close();

        try {
            record.get(0);
        } catch (Exception e) {
            Assert.assertEquals(NoSuchElementException.class, e.getClass());
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetValueOutOfBounds() throws IOException {
        File csvFile = new File("testData/test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord record = records.get(0);
        Assert.assertEquals(2, record.size());

        parser.close();

        try {
            record.get(3);
        } catch (Exception e) {
            Assert.assertEquals(ArrayIndexOutOfBoundsException.class, e.getClass());
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetValueOutOfBoundsNegative() throws IOException {
        File csvFile = new File("testData/test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord record = records.get(0);
        Assert.assertEquals(2, record.size());

        parser.close();

        try {
            record.get(-1);
        } catch (Exception e) {
            Assert.assertEquals(ArrayIndexOutOfBoundsException.class, e.getClass());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullHeaderMap() throws IOException {
        File csvFile = new File("testData/test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        parser.close();

        CSVRecord record = records.get(0);
        Map<String, Integer> headerMap = null;

        try {
            record.getHeaderMap(headerMap);
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRecordNumber() throws IOException {
        File csvFile = new File("testData/test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        parser.close();

        CSVRecord record = records.get(0);

        try {
            record.getRecordNumber(-1);
        } catch (Exception e) {
            Assert.assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        File csvFile = new File("testData/test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        parser.close();

        CSVRecord record = records.get(0);
        long recordNumber = record.getRecordNumber();

        Assert.assertEquals(1, recordNumber);
    }
}