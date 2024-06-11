import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecord_BasicFunctionality() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,27,Chicago\n";
        Map<String, Integer> headerMapping = new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }};

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();
            CSVRecord testRecord = new CSVRecord(record.toArray(), headerMapping, null, parser.getHeaderNumber());

            Assert.assertEquals("John Doe", testRecord.get("Name"));
            Assert.assertEquals("30", testRecord.get("Age"));
            Assert.assertEquals("New York", testRecord.get("City"));
            Assert.assertEquals("New York", testRecord.getComment());
            Assert.assertEquals(1, testRecord.getRecordNumber());
            Assert.assertTrue(testRecord.isConsistent());
        }
    }

    @Test
    public void testCSVRecord_GetByEnum() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,27,Chicago\n";
        Map<String, Integer> headerMapping = new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }};

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();
            CSVRecord testRecord = new CSVRecord(record.toArray(), headerMapping, null, parser.getHeaderNumber());

            Enum<String> nameEnum = Enum.valueOf(String.class, "Name");
            Assert.assertEquals("John Doe", testRecord.get(nameEnum));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_NoHeaderMapping() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,27,Chicago\n";
        CSVRecord testRecord = new CSVRecord(new String[0], null, null, 0);
        testRecord.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_InvalidNameMapping() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,27,Chicago\n";
        Map<String, Integer> headerMapping = new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City2", 2); // Misspelled City
        }};

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();
            CSVRecord testRecord = new CSVRecord(record.toArray(), headerMapping, null, parser.getHeaderNumber());
            testRecord.get("City2");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_InvalidIndex() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,27,Chicago\n";
        Map<String, Integer> headerMapping = new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }};

        CSVRecord testRecord = new CSVRecord(new String[0], headerMapping, null, 0);
        testRecord.get(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_InvalidIndexSizeMismatch() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,27,Chicago\n";
        Map<String, Integer> headerMapping = new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }};

        CSVRecord testRecord = new CSVRecord(new String[1], headerMapping, null, 0);
        testRecord.get(0);
    }

    @Test
    public void testCSVRecord_Comments() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York#This is a comment\nJane Smith,27,Chicago";
        Map<String, Integer> headerMapping = new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }};

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();
            CSVRecord testRecord = new CSVRecord(record.toArray(), headerMapping, null, parser.getHeaderNumber());
            Assert.assertEquals("This is a comment", testRecord.getComment());
        }
    }

    @Test
    public void testCSVRecord_NullValues() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,null,Chicago";
        Map<String, Integer> headerMapping = new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }};

        try (CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader())) {
            CSVRecord record = parser.nextRecord();
            CSVRecord testRecord = new CSVRecord(record.toArray(), headerMapping, null, parser.getHeaderNumber());
            Assert.assertNull(testRecord.get("Age"));
        }
    }
}