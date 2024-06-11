import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVParserBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConsistency() {
        String csvData = "Name,Age,City\nJohn Doe,35,New York\nJane Doe,28,Chicago\n";

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',').withMultiLineQuotes('"').build();

        List<CSVRecord> records = parser.parseAll(new StringReader(csvData));

        CSVRecord record = records.get(0);
        Assert.assertTrue(record.isConsistent());

        String[] values = record.values();
        Assert.assertEquals(3, values.length);
        Assert.assertEquals("Name", record.get(0));
        Assert.assertEquals("John Doe", record.get(0));
        Assert.assertEquals("Age", record.get(1));
        Assert.assertEquals("35", record.get(1));
        Assert.assertEquals("City", record.get(2));
        Assert.assertEquals("New York", record.get(2));
    }

    @Test
    public void testCSVRecordGetValueByName() {
        String csvData = "Name,Age,City\nJohn Doe,35,New York\nJane Doe,28,Chicago\n";

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',').withMultiLineQuotes('"').build();

        List<CSVRecord> records = parser.parseAll(new StringReader(csvData));

        CSVRecord record = records.get(0);

        Assert.assertEquals("John Doe", record.get("Name"));
        Assert.assertEquals("35", record.get("Age"));
        Assert.assertEquals("New York", record.get("City"));
        Assert.assertNull(record.get("UnknownColumn"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordNoMapping() {
        String csvData = "Name,Age,City\nJohn Doe,35,New York\nJane Doe,28,Chicago\n";

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',').withMultiLineQuotes('"').build();

        List<CSVRecord> records = parser.parseAll(new StringReader(csvData));

        CSVRecord record = records.get(0);
        record.get(5);
    }

    @Test
    public void testCSVRecordIterable() {
        String csvData = "Name,Age,City\nJohn Doe,35,New York\nJane Doe,28,Chicago\n";

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',').withMultiLineQuotes('"').build();

        List<CSVRecord> records = parser.parseAll(new StringReader(csvData));

        CSVRecord record = records.get(0);

        int i = 0;
        for (String value : record) {
            Assert.assertEquals(String.valueOf(record.values()[i]), value);
            i++;
        }
    }

    @Test
    public void testCSVRecordSize() {
        String csvData = "Name,Age,City\nJohn Doe,35,New York\nJane Doe,28,Chicago\n";

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',').withMultiLineQuotes('"').build();

        List<CSVRecord> records = parser.parseAll(new StringReader(csvData));

        CSVRecord record = records.get(0);

        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testCSVRecordComment() {
        String csvData = "#This is a comment\nName,Age,City\nJohn Doe,35,New York\nJane Doe,28,Chicago\n";

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',').withMultiLineQuotes('"').build();

        List<CSVRecord> records = parser.parseAll(new StringReader(csvData));

        CSVRecord record = records.get(0);

        Assert.assertEquals("#This is a comment", record.getComment());
    }

    @Test
    public void testCSVRecordMapping() {
        String csvData = "Name,Age,City\nName1,Age1,City1\nName2,Age2,City2\n";

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',').withMultiLineQuotes('"')
                .build();

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        List<CSVRecord> records = parser.parseAll(new StringReader(csvData));

        CSVRecord record = records.get(0);

        Assert.assertEquals(mapping, record.mapping);
    }
}