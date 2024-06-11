import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testCSVParserParseFile() {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.RFC4180;
        
        try {
            CSVParser parser = CSVParser.parse(file, format);
            assertNotNull(parser);
            assertFalse(parser.isClosed());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCSVParserParseString() {
        String csvString = "name,age\nJohn,30";
        CSVFormat format = CSVFormat.EXCEL;
        
        try {
            CSVParser parser = CSVParser.parse(csvString, format);
            assertNotNull(parser);
            assertFalse(parser.isClosed());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCSVParserParseURL() {
        URL url = CSVParserTest.class.getResource("test.csv");
        CSVFormat format = CSVFormat.EXCEL;
        
        try {
            CSVParser parser = CSVParser.parse(url, StandardCharsets.UTF_8, format);
            assertNotNull(parser);
            assertFalse(parser.isClosed());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCSVParserClose() {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.RFC4180;

        try {
            CSVParser parser = CSVParser.parse(file, format);
            parser.close();
            assertTrue(parser.isClosed());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCSVParserGetHeaderMap() {
        String[] headers = { "name", "age" };
        CSVFormat format = CSVFormat.RFC4180.withHeader(headers);
        File file = new File("test.csv");

        try {
            CSVParser parser = CSVParser.parse(file, format);
            assertNotNull(parser.getHeaderMap());
            assertEquals(headers.length, parser.getHeaderMap().size());

            for (String header : headers) {
                assertTrue(parser.getHeaderMap().containsKey(header));
            }
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCSVParserIterator() {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.RFC4180;

        try {
            CSVParser parser = CSVParser.parse(file, format);
            assertNotNull(parser.iterator());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}