package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"></head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document expectedDocument = DataUtil.parseByteData(ByteBuffer.wrap(fileContent.getBytes()), "UTF-8", null, Parser.htmlParser());
        Document actualDocument = DataUtil.load(testFile, "UTF-8", null);

        assertNotNull(actualDocument);
        assertNotNull(actualDocument.title());
        assertNotNull(actualDocument.body());
        assertNotNull(actualDocument.select("p"));
        assertNotNull(actualDocument.select("p").text());
        assertNotNull(actualDocument.outputSettings().charset());
        assertNotNull(actualDocument.outputSettings().charset().name());
        assertNotNull(actualDocument.outputSettings().charset().name().toLowerCase());
        assertNotNull(actualDocument.outputSettings().charset().name().equals("utf-8"));

        DataUtil.load(testFile, null, null); // Test case for detecting charset from file.

        assertNull(actualDocument.outputSettings().charset());

        actualDocument = DataUtil.load(testFile, null, null);
        assertNotNull(actualDocument.outputSettings().charset());
        assertNotNull(actualDocument.outputSettings().charset().name());
        assertNotNull(actualDocument.outputSettings().charset().name().equals("utf-8"));

        DataUtil.load(testFile, "", null); // Test case for empty charset.

        assertNull(actualDocument.outputSettings().charset());

        FileInputStream inputStream = new FileInputStream(testFile);
        InputStream mockedInputStream = mock(InputStream.class);
        when(mockedInputStream.read()).thenReturn('A');

        Document documentFromInputStream = DataUtil.load(mockedInputStream, "UTF-8", null);
        assertNull(documentFromInputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStreamToByteArrayOutputStream = new ByteArrayInputStream(fileContent.getBytes());
        Document documentFromByteArrayOutputStream = DataUtil.load(inputStreamToByteArrayOutputStream, "UTF-8", null);
        assertNotNull(documentFromByteArrayOutputStream);

        testFile.deleteOnExit();
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"></head>\n<body>\n<p>Test</p>\n</body>\n</html>".getBytes()).clone());

        Document expectedDocument = DataUtil.parseByteData(ByteBuffer.wrap("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"></head>\n<body>\n<p>Test</p>\n</body>\n</html>".getBytes()), "UTF-8", null, Parser.htmlParser());
        Document actualDocument = DataUtil.load(testInputStream, "UTF-8", null);

        assertNotNull(actualDocument);
        assertNotNull(actualDocument.title());
        assertNotNull(actualDocument.body());
        assertNotNull(actualDocument.select("p"));
        assertNotNull(actualDocument.select("p").text());
        assertNotNull(actualDocument.outputSettings().charset());
        assertNotNull(actualDocument.outputSettings().charset().name());
        assertNotNull(actualDocument.outputSettings().charset().name().toLowerCase());
        assertNotNull(actualDocument.outputSettings().charset().name().equals("utf-8"));
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"></head>\n<body>\n<p>Test</p>\n</body>\n</html>".getBytes()).clone());

        Document expectedDocument = DataUtil.parseByteData(ByteBuffer.wrap("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"></head>\n<body>\n<p>Test</p>\n</body>\n</html>".getBytes()), "UTF-8", null, Parser.xmlParser());
        Document actualDocument = DataUtil.load(testInputStream, "UTF-8", null, Parser.xmlParser());

        assertNotNull(actualDocument);
        assertNotNull(actualDocument.title());
        assertNotNull(actualDocument.body());
        assertNotNull(actualDocument.select("p"));
        assertNotNull(actualDocument.select("p").text());
        assertNotNull(actualDocument.outputSettings().charset());
        assertNotNull(actualDocument.outputSettings().charset().name());
        assertNotNull(actualDocument.outputSettings().charset().name().toLowerCase());
        assertNotNull(actualDocument.outputSettings().charset().name().equals("utf-8"));
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer testByteBuffer = ByteBuffer.wrap("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"></head>\n<body>\n<p>Test</p>\n</body>\n</html>".getBytes());

        Document expectedDocument = DataUtil.parseByteData(testByteBuffer, "UTF-8", null, Parser.htmlParser());
        Document actualDocument = DataUtil.parseByteData(testByteBuffer, "UTF-8", null, Parser.htmlParser());

        assertNotNull(actualDocument);
        assertNotNull(actualDocument.title());
        assertNotNull(actualDocument.body());
        assertNotNull(actualDocument.select("p"));
        assertNotNull(actualDocument.select("p").text());
        assertNotNull(actualDocument.outputSettings().charset());
        assertNotNull(actualDocument.outputSettings().charset().name());
        assertNotNull(actualDocument.outputSettings().charset().name().toLowerCase());
        assertNotNull(actualDocument.outputSettings().charset().name().equals("utf-8"));

        Document documentFromNullCharset = DataUtil.parseByteData(testByteBuffer, null, null, Parser.htmlParser());
        assertNotNull(documentFromNullCharset);
        assertNull(documentFromNullCharset.outputSettings().charset());
    }
}