import org.junit.Test;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class CSVParserTest {

    @Test
    public void testParseFile() {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.EXCEL;

        try {
            CSVParser parser = CSVParser.parse(file, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }

    @Test
    public void testParseString() {
        String data = "name,age\nJohn,25";
        CSVFormat format = CSVFormat.EXCEL;

        try {
            CSVParser parser = CSVParser.parse(data, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }

    @Test
    public void testParseURL() {
        URL url = new URL("https://example.com/test.csv");
        Charset charset = Charset.defaultCharset();
        CSVFormat format = CSVFormat.EXCEL;

        try {
            CSVParser parser = CSVParser.parse(url, charset, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }

    @Test
    public void testInvalidFileInput() {
        File file = null;
        CSVFormat format = CSVFormat.EXCEL;

        try {
            CSVParser parser = CSVParser.parse(file, format);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }

    @Test
    public void testInvalidStringInput() {
        String data = null;
        CSVFormat format = CSVFormat.EXCEL;

        try {
            CSVParser parser = CSVParser.parse(data, format);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }

    @Test
    public void testInvalidURLInput() {
        URL url = null;
        Charset charset = Charset.defaultCharset();
        CSVFormat format = CSVFormat.EXCEL;

        try {
            CSVParser parser = CSVParser.parse(url, charset, format);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }
}