import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File inputFile = new File("src/test/resources/test.csv");
        CSVParser parser = CSVParser.parse(inputFile, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(4, records.size());

        for (CSVRecord record : records) {
            String[] values = record.toArray(String[].class);
            for (int i = 0; i < values.length; i++) {
                System.out.print(values[i] + "\t");
            }
            System.out.println();
        }

        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "A,B\nC,D";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        String[] values = record.toArray(String[].class);
        Assert.assertArrayEquals(new String[]{"A", "B"}, values);

        parser.close();
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "http://example.com/test.csv";
        CSVParser parser = CSVParser.parse(new URL(urlString), Charset.forName("UTF-8"), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size()); // Replace this with an assertion based on the expected CSV content

        parser.close();
    }

    @Test
    public void testGetRecordsCollection() throws IOException {
        String csvData = "A,B\nC,D";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<>();
        parser.getRecords(records);

        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        String[] values = record.toArray(String[].class);
        Assert.assertArrayEquals(new String[]{"A", "B"}, values);

        parser.close();
    }

    @Test
    public void testGetHeaderMap() {
        String csvData = "Name,Age\nJohn,30\nMary,25";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);

        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(2, headerMap.size());
        Assert.assertEquals("Name", headerMap.get("Name"));
        Assert.assertEquals("Age", headerMap.get("Age"));

        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoMoreRecords() throws IOException {
        String csvData = "A,B\nC,D";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.getRecords(); // Consume the records

        parser.close();

        parser.getRecords(); // Should throw NoSuchElementException
    }

    @Test(expected = IllegalStateException.class)
    public void testClosedParser() throws IOException {
        String csvData = "A,B\nC,D";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.getRecords();
        parser.close();

        parser.getRecords(); // Should throw IllegalStateException
    }
}