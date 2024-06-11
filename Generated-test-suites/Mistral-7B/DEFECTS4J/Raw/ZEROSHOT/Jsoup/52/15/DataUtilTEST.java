package org.jsoup.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testLoadFile() throws Exception {
        // Given
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        File file = new File("test.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes());
        outputStream.close();
        File expectedFile = new File(file.getAbsolutePath());
        expectedFile.deleteOnExit();
        File actualFile = new File(file.getAbsolutePath());
        actualFile.createNewFile();
        actualFile.writeBytes(fileContent.getBytes());

        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.readAllBytes()).thenReturn(fileContent.getBytes());

        ByteBuffer byteData = Mockito.mock(ByteBuffer.class);
        Mockito.when(byteData.toString()).thenReturn(fileContent);

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, "http://example.com");
        Document actualDocument = null;

        // When
        actualDocument = DataUtil.load(actualFile, "UTF-8", "http://example.com");

        // Then
        assertTrue(actualDocument != null);
        assertTrue(actualDocument.body().text().equals(expectedDocument.body().text()));
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testLoadInputStream() throws Exception {
        // Given
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document expectedDocument = Parser.htmlParser().parseInput(inputStreamContent, "http://example.com");
        Document actualDocument = null;

        // When
        actualDocument = DataUtil.load(inputStream, "UTF-8", "http://example.com");

        // Then
        assertTrue(actualDocument != null);
        assertTrue(actualDocument.body().text().equals(expectedDocument.body().text()));
        assertTrue(inputStream.close());
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testParseByteData() throws Exception {
        // Given
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes());

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, "http://example.com");
        Document actualDocument = null;

        // When
        actualDocument = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());

        // Then
        assertTrue(actualDocument != null);
        assertTrue(actualDocument.body().text().equals(expectedDocument.body().text()));
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testParseByteDataWithCharsetDetectedFromBom() throws Exception {
        // Given
        String fileContent = "\uFEFF<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes());

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, "http://example.com");
        Document actualDocument = null;

        // When
        actualDocument = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());

        // Then
        assertTrue(actualDocument != null);
        assertTrue(actualDocument.body().text().equals(expectedDocument.body().text()));
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testParseByteDataWithInvalidCharset() throws Exception {
        // Given
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes());

        // When
        String invalidCharset = "asdf";
        Document actualDocument = null;

        // Then
        assertNull(DataUtil.parseByteData(byteData, invalidCharset, "http://example.com", Parser.htmlParser()));
    }
}