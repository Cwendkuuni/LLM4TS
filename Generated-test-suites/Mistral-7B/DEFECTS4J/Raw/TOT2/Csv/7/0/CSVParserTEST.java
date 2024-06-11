import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * CSVParserTest.java
 */
public class CSVParserTest {

    @Test
    public void testParseSimpleCSVFile() throws IOException {
        String csvFile = "name,age\nJohn,30\nMike,25\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvFile), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Check the number of records returned
        assertEquals(2, records.size());

        // Check the values of each record
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            String name = (String) map.get("name");
            int age = (int) map.get("age");

            if (name.equals("John")) {
                assertEquals(30, age);
            } else if (name.equals("Mike")) {
                assertEquals(25, age);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        String csvFile = "name,age\nJohn,30\nMike,25\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvFile), new CSVFormat("", true, '\n'));
    }

    @Test
    public void testParseCSVFileWithHeader() throws IOException {
        String csvFile = "name,age\nJohn,30\nMike,25\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvFile), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();

        // Check the number of records and header returned
        assertEquals(2, records.size());
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(2, headerMap.size());

        // Check the values of each record
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            String name = (String) map.get("name");
            int age = (int) map.get("age");

            if (name.equals("John")) {
                assertEquals(30, age);
            } else if (name.equals("Mike")) {
                assertEquals(25, age);
            }
        }

        // Check the header names
        List<String> headerNames = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
            headerNames.add(entry.getKey());
        }
        List<String> expectedHeaderNames = Arrays.asList("name", "age");
        assertEquals(expectedHeaderNames, headerNames);
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmptyCSVFile() throws IOException {
        String csvFile = "";
        CSVParser parser = CSVParser.parse(new StringReader(csvFile), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test(expected = IOException.class)
    public void testParseMalformedCSVFile() throws IOException {
        String csvFile = "name,age\nJohn,30\nMike,25\nname,age";
        CSVParser parser = CSVParser.parse(new StringReader(csvFile), CSVFormat.DEFAULT);
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseCSVFileWithNoRecords() throws IOException {
        String csvFile = "name,age";
        CSVParser parser = CSVParser.parse(new StringReader(csvFile), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test
    public void testParseCSVFileWithQuotedValues() throws IOException {
        String csvFile = "name,age\nJohn,\"O'Neill\",30\nMike,\"O'Reilly\",25\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvFile), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Check the number of records returned
        assertEquals(2, records.size());

        // Check the values of each record
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            String name = (String) map.get("name");
            String lastName = (String) map.get("age");

            if (name.equals("John")) {
                assertEquals("O'Neill", lastName);
            } else if (name.equals("Mike")) {
                assertEquals("O'Reilly", lastName);
            }
        }
    }

    @Test
    public void testParseCSVFileWithEmptyValues() throws IOException {
        String csvFile = "name,age\nJohn,\n30\nMike,25\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvFile), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Check the number of records returned
        assertEquals(2, records.size());

        // Check the values of each record
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            String name = (String) map.get("name");
            Object age = map.get("age");

            if (name.equals("John")) {
                assertNull(age);
            } else if (name.equals("Mike")) {
                assertEquals(25, age);
            }
        }
    }

    @Test
    public void testParseCSVFileWithNullValues() throws IOException {
        String csvFile = "name,age\nJohn,null\nMike,25\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvFile), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Check the number of records returned
        assertEquals(2, records.size());

        // Check the values of each record
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            String name = (String) map.get("name");
            Object age = map.get("age");

            if (name.equals("John")) {
                assertNull(age);
            } else if (name.equals("Mike")) {
                assertEquals(25, age);
            }
        }
    }

    @Test
    public void testParseCSVFileWithTrailingComma() throws IOException {
        String csvFile = "name,age\nJohn,30,\nMike,25,\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvFile), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Check the number of records returned
        assertEquals(2, records.size());

        // Check the values of each record
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            String name = (String) map.get("name");
            Object age = map.get("age");

            if (name.equals("John")) {
                assertNull(age);
            } else if (name.equals("Mike")) {
                assertEquals(25, age);
            }
        }
    }

    @Test
    public void testParseCSVFileWithEmptyLine() throws IOException {
        String csvFile = "name,age\nJohn,30\n\nMike,25\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvFile), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Check the number of records returned
        assertEquals(2, records.size());

        // Check the values of each record
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            String name = (String) map.get("name");
            Object age = map.get("age");

            if (name.equals("John")) {
                assertEquals(30, age);
            } else if (name.equals("Mike")) {
                assertEquals(25, age);
            }
        }
    }
}