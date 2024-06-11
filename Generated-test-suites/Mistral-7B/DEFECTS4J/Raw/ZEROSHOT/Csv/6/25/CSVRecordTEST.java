import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithHeaderMapping() throws Exception {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = new CSVParser(new StringReader(csv), format);
        CSVRecord record = parser.nextRecord();

        Map<String, Integer> mapping = record.getMapping();
        Assert.assertNotNull(mapping);
        Assert.assertEquals(3, mapping.size());
        Assert.assertEquals("Name", mapping.get("Name").toString());
        Assert.assertEquals("Age", mapping.get("Age").toString());
        Assert.assertEquals("City", mapping.get("City").toString());

        String name = record.get("Name");
        Assert.assertEquals("John Doe", name);

        int age = Integer.parseInt(record.get("Age"));
        Assert.assertEquals(30, age);

        String city = record.get("City");
        Assert.assertEquals("New York", city);
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithoutHeaderMapping() throws Exception {
        String csv = "Age,Name\n30,John Doe\n25,Jane Doe";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = new CSVParser(new StringReader(csv), format);
        CSVRecord record = parser.nextRecord();

        record.get("Name"); //should throw an exception
    }

    @Test
    public void testCSVRecordConsistency() throws Exception {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = new CSVParser(new StringReader(csv), format);
        CSVRecord record = parser.nextRecord();

        Assert.assertTrue(record.isConsistent());

        String invalidCsv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25";
        CSVParser parserInvalid = new CSVParser(new StringReader(invalidCsv), format);
        CSVRecord recordInvalid = parserInvalid.nextRecord();

        Assert.assertFalse(recordInvalid.isConsistent());
    }

    @Test
    public void testCSVRecordGetComment() throws Exception {
        String csv = "Name,Age,City,Comment\nJohn Doe,30,New York,Hello\nJane Doe,25,Los Angeles";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = new CSVParser(new StringReader(csv), format);
        CSVRecord record = parser.nextRecord();

        String comment = record.getComment();
        Assert.assertEquals("Hello", comment);
    }

    @Test
    public void testCSVRecordGetRecordNumber() throws Exception {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = new CSVParser(new StringReader(csv), format);
        CSVRecord record1 = parser.nextRecord();
        CSVRecord record2 = parser.nextRecord();

        long recordNumber1 = record1.getRecordNumber();
        long recordNumber2 = record2.getRecordNumber();

        Assert.assertEquals(1, recordNumber1);
        Assert.assertEquals(2, recordNumber2);
    }

    @Test
    public void testCSVRecordIterable() throws Exception {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = new CSVParser(new StringReader(csv), format);
        CSVRecord record = parser.nextRecord();

        List<String> values = Arrays.asList(record.values());

        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            String expectedValue = String.format("Column %d", i + 1);
            Assert.assertEquals(expectedValue, value);
        }
    }

    @Test
    public void testCSVRecordSize() throws Exception {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = new CSVParser(new StringReader(csv), format);
        CSVRecord record = parser.nextRecord();

        int size = record.size();
        Assert.assertEquals(3, size);
    }

    @Test
    public void testCSVRecordToList() throws Exception {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = new CSVParser(new StringReader(csv), format);
        CSVRecord record = parser.nextRecord();

        List<String> values = record.toList();

        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            String expectedValue = String.format("Column %d", i + 1);
            Assert.assertEquals(expectedValue, value);
        }
    }

    @Test
    public void testCSVRecordToMap() throws Exception {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = new CSVParser(new StringReader(csv), format);
        CSVRecord record = parser.nextRecord();

        Map<String, String> map = record.toMap();

        Assert.assertEquals(3, map.size());
        Assert.assertEquals("Name", map.keySet().toArray()[0].toString());
        Assert.assertEquals("Age", map.keySet().toArray()[1].toString());
        Assert.assertEquals("City", map.keySet().toArray()[2].toString());

        Assert.assertEquals("John Doe", map.get("Name"));
        Assert.assertEquals("30", map.get("Age"));
        Assert.assertEquals("New York", map.get("City"));
    }
}