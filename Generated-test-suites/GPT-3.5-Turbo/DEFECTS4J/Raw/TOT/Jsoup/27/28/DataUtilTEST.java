import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class DataUtilTest {
    private File testFile;
    private String baseUri;
    private String charsetName;
    private String defaultCharset;

    @Before
    public void setUp() {
        testFile = new File("test.html");
        baseUri = "http://www.example.com";
        charsetName = "UTF-8";
        defaultCharset = "UTF-8";
    }

    @Test
    public void testLoad_file_input_valid() {
        try {
            Document doc = DataUtil.load(testFile, charsetName, baseUri);
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoad_file_input_invalid_file_not_found() {
        File nonExistingFile = new File("non_existing.html");
        try {
            DataUtil.load(nonExistingFile, charsetName, baseUri);
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertEquals("File not found: non_existing.html", e.getMessage());
        }
    }

    @Test
    public void testLoad_file_input_invalid_charset_name_empty() {
        charsetName = "";
        try {
            DataUtil.load(testFile, charsetName, baseUri);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML", e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoad_file_input_invalid_charset_name_not_supported() {
        charsetName = "InvalidCharset";
        try {
            DataUtil.load(testFile, charsetName, baseUri);
            fail("UnsupportedCharsetException should have been thrown");
        } catch (UnsupportedCharsetException e) {
            assertEquals("Charset 'InvalidCharset' is not supported", e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoad_inputStream_input_valid() {
        try {
            FileInputStream fileInputStream = new FileInputStream(testFile);
            Document doc = DataUtil.load(fileInputStream, charsetName, baseUri);
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
            fileInputStream.close();
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoad_inputStream_input_invalid_charset_name_empty() {
        charsetName = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(testFile);
            DataUtil.load(fileInputStream, charsetName, baseUri);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML", e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoad_inputStream_input_invalid_charset_name_not_supported() {
        charsetName = "InvalidCharset";
        try {
            FileInputStream fileInputStream = new FileInputStream(testFile);
            DataUtil.load(fileInputStream, charsetName, baseUri);
            fail("UnsupportedCharsetException should have been thrown");
        } catch (UnsupportedCharsetException e) {
            assertEquals("Charset 'InvalidCharset' is not supported", e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoad_inputStream_input_invalid_file_not_found() {
        InputStream nonExistingInputStream = null;
        try {
            nonExistingInputStream = new FileInputStream("non_existing.html");
            DataUtil.load(nonExistingInputStream, charsetName, baseUri);
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertEquals("File not found: non_existing.html", e.getMessage());
        } finally {
            try {
                if (nonExistingInputStream != null)
                    nonExistingInputStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    @Test
    public void testLoad_inputStream_input_invalid_null_input_stream() {
        InputStream nullInputStream = null;
        try {
            DataUtil.load(nullInputStream, charsetName, baseUri);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Input stream must not be null", e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoad_parser_input_valid() {
        try {
            FileInputStream fileInputStream = new FileInputStream(testFile);
            Parser parser = Parser.htmlParser();
            Document doc = DataUtil.load(fileInputStream, charsetName, baseUri, parser);
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
            fileInputStream.close();
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoad_parser_input_invalid_charset_name_empty() {
        charsetName = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(testFile);
            Parser parser = Parser.htmlParser();
            DataUtil.load(fileInputStream, charsetName, baseUri, parser);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML", e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoad_parser_input_invalid_charset_name_not_supported() {
        charsetName = "InvalidCharset";
        try {
            FileInputStream fileInputStream = new FileInputStream(testFile);
            Parser parser = Parser.htmlParser();
            DataUtil.load(fileInputStream, charsetName, baseUri, parser);
            fail("UnsupportedCharsetException should have been thrown");
        } catch (UnsupportedCharsetException e) {
            assertEquals("Charset 'InvalidCharset' is not supported", e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoad_parser_input_invalid_file_not_found() {
        InputStream nonExistingInputStream = null;
        try {
            nonExistingInputStream = new FileInputStream("non_existing.html");
            Parser parser = Parser.htmlParser();
            DataUtil.load(nonExistingInputStream, charsetName, baseUri, parser);
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertEquals("File not found: non_existing.html", e.getMessage());
        } finally {
            try {
                if (nonExistingInputStream != null)
                    nonExistingInputStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    private String getTestData() {
        return "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello, World!</h1>\n" +
                "<p>This is a test page.</p>\n" +
                "</body>\n" +
                "</html>";
    }

    @Test
    public void testParseByteData_charsetName_null() {
        ByteBuffer byteData = ByteBuffer.wrap(getTestData().getBytes());
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseByteData_charsetName_empty() {
        ByteBuffer byteData = ByteBuffer.wrap(getTestData().getBytes());
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, "", baseUri, parser);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseByteData_charsetName_not_found_in_meta() {
        ByteBuffer byteData = ByteBuffer.wrap(getTestData().getBytes());
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", baseUri, parser);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseByteData_charsetName_found_in_meta() {
        ByteBuffer byteData = ByteBuffer.wrap(getTestData().getBytes());
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", baseUri, parser);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseByteData_charsetName_found_in_http_equiv() {
        String testData = "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello, World!</h1>\n" +
                "<p>This is a test page.</p>\n" +
                "</body>\n" +
                "</html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseByteData_charsetName_not_supported() {
        ByteBuffer byteData = ByteBuffer.wrap(getTestData().getBytes());
        Parser parser = Parser.htmlParser();
        try {
            DataUtil.parseByteData(byteData, "InvalidCharset", baseUri, parser);
            fail("UnsupportedCharsetException should have been thrown");
        } catch (UnsupportedCharsetException e) {
            assertEquals("Charset 'InvalidCharset' is not supported", e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType_charset_found() {
        String contentType = "text/html; charset=utf-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_charset_not_found() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}