package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil.
 *
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws Exception
     */
    @Test
    public void testLoadFile() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        FileOutputStream testFileOutputStream = new FileOutputStream(testFile);
        testFileOutputStream.write(fileContent.getBytes());
        testFileOutputStream.close();

        Document expectedDocument = DataUtil.parseByteData(ByteBuffer.wrap(fileContent.getBytes("UTF-8")), "UTF-8", null, Parser.htmlParser());
        Document actualDocument = DataUtil.load(testFile, "UTF-8", null);

        assertNotNull(actualDocument);
        assertNotNull(actualDocument.title());
        assertNotNull(actualDocument.body());
        assertNotNull(actualDocument.head());
        assertNotNull(actualDocument.select("h1").first());
        assertNotNull(actualDocument.select("meta[charset]").first());
        assertNotNull(actualDocument.outputSettings().charset());
        assertNotNull(actualDocument.outputSettings().contentType());
        assertNotNull(actualDocument.outputSettings().encoding());
        assertEquals(expectedDocument, actualDocument);

        testFile.delete();
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws Exception
     */
    @Test
    public void testLoadFileWithInvalidCharset() throws Exception {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        FileOutputStream testFileOutputStream = new FileOutputStream(testFile);
        testFileOutputStream.write(fileContent.getBytes());
        testFileOutputStream.close();

        Document expectedDocument = DataUtil.parseByteData(ByteBuffer.wrap(fileContent.getBytes("UTF-8")), null, null, Parser.htmlParser());
        Document actualDocument = null;

        try {
            actualDocument = DataUtil.load(testFile, null, null);
            fail("Expected an IOException to be thrown.");
        } catch (IOException e) {
            // expected
        }

        assertNull(actualDocument);
        testFile.delete();
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws Exception
     */
    @Test
    public void testLoadInputStream() throws Exception {
        String testInputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream testInputStream = new ByteArrayInputStream(testInputStreamContent.getBytes("UTF-8"));

        Document expectedDocument = DataUtil.parseByteData(ByteBuffer.wrap(testInputStreamContent.getBytes("UTF-8")), "UTF-8", null, Parser.htmlParser());
        Document actualDocument = DataUtil.load(testInputStream, "UTF-8", null);

        assertNotNull(actualDocument);
        assertNotNull(actualDocument.title());
        assertNotNull(actualDocument.body());
        assertNotNull(actualDocument.head());
        assertNotNull(actualDocument.select("h1").first());
        assertNotNull(actualDocument.select("meta[charset]").first());
        assertNotNull(actualDocument.outputSettings().charset());
        assertNotNull(actualDocument.outputSettings().contentType());
        assertNotNull(actualDocument.outputSettings().encoding());
        assertEquals(expectedDocument, actualDocument);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     * @throws Exception
     */
    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        String testInputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream testInputStream = new ByteArrayInputStream(testInputStreamContent.getBytes("UTF-8"));
        Parser parser = mock(Parser.class);

        String charset = "UTF-8";
        String baseUri = null;

        Document expectedDocument = DataUtil.parseByteData(ByteBuffer.wrap(testInputStreamContent.getBytes("UTF-8")), "UTF-8", null, Parser.htmlParser());
        when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(testInputStream, charset, baseUri, parser);

        assertNotNull(actualDocument);
        assertEquals(expectedDocument, actualDocument);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)} with null charset argument.
     * @throws Exception
     */
    @Test
    public void testLoadInputStreamWithNullCharset() throws Exception {
        String testInputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream testInputStream = new ByteArrayInputStream(testInputStreamContent.getBytes("UTF-8"));
        Parser parser = mock(Parser.class);

        String charset = null;
        String baseUri = null;

        Document expectedDocument = DataUtil.parseByteData(ByteBuffer.wrap(testInputStreamContent.getBytes("UTF-8")), null, null, Parser.htmlParser());
        when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(testInputStream, charset, baseUri, parser);

        assertEquals(expectedDocument, actualDocument);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)} with null baseUri argument.
     * @throws Exception
     */
    @Test
    public void testLoadInputStreamWithNullBaseUri() throws Exception {
        String testInputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Hello World</h1>\n<a href=\"about.html\">About</a>\n</body>\n</html>";
        InputStream testInputStream = new ByteArrayInputStream(testInputStreamContent.getBytes("UTF-8"));
        Parser parser = mock(Parser.class);

        String charset = "UTF-8";
        String baseUri = null;

        Document expectedDocument = DataUtil.parseByteData(ByteBuffer.wrap(testInputStreamContent.getBytes("UTF-8")), charset, null, Parser.htmlParser());
        when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(testInputStream, charset, null, parser);

        assertNotNull(actualDocument);
        assertEquals(expectedDocument.select("a[href]"), actualDocument.select("a[href]"));
        assertEquals(expectedDocument.outputSettings().charset(), actualDocument.outputSettings().charset());
        assertEquals(expectedDocument.outputSettings().contentType(), actualDocument.outputSettings().contentType());
        assertEquals(expectedDocument.outputSettings().encoding(), actualDocument.outputSettings().encoding());
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with null charset argument.
     * @throws Exception
     */
    @Test
    public void testParseByteDataWithNullCharset() throws Exception {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'T', 'h', 'i', 's', ' ', 'I', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', 'F', 'i', 'l', 'e'});
        Parser parser = mock(Parser.class);

        String charset = null;
        String baseUri = null;

        Document expectedDocument = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.parseByteData(byteData, charset, baseUri, parser);

        assertEquals(expectedDocument, actualDocument);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with null baseUri argument.
     * @throws Exception
     */
    @Test
    public void testParseByteDataWithNullBaseUri() throws Exception {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'T', 'h', 'i', 's', ' ', 'I', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', 'F', 'i', 'l', 'e'});
        Parser parser = mock(Parser.class);

        String charset = "UTF-8";
        String baseUri = null;

        Document expectedDocument = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
        when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.parseByteData(byteData, charset, baseUri, parser);

        assertEquals(expectedDocument, actualDocument);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with null parser argument.
     * @throws Exception
     */
    @Test
    public void testParseByteDataWithNullParser() throws Exception {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'T', 'h', 'i', 's', ' ', 'I', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', 'F', 'i', 'l', 'e'});
        String charset = "UTF-8";
        String baseUri = null;

        Document expectedDocument = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());

        assertThrows(NullPointerException.class, () -> DataUtil.parseByteData(byteData, charset, baseUri, null));
        assertEquals(expectedDocument, DataUtil.parseByteData(byteData, charset, baseUri, Parser.xmlParser()));
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)} with null input stream argument.
     * @throws Exception
     */
    @Test
    public void testReadToByteBufferWithNullInputStream() throws Exception {
        assertThrows(NullPointerException.class, () -> DataUtil.readToByteBuffer(null));
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)} with null contentType argument.
     * @throws Exception
     */
    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() throws Exception {
        assertNull(DataUtil.getCharsetFromContentType(null));
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)} with empty contentType argument.
     * @throws Exception
     */
    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() throws Exception {
        assertNull(DataUtil.getCharsetFromContentType(""));
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)} with valid contentType argument.
     * @throws Exception
     */
    @Test
    public void testGetCharsetFromContentTypeWithValidContentType() throws Exception {
        String contentType = "text/html; charset=GB2312";
        assertEquals("GB2312", DataUtil.getCharsetFromContentType(contentType));
    }
}