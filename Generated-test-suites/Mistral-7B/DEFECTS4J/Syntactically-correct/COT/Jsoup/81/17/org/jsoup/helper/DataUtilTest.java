package org.jsoup.helper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class DataUtilTest {

    private static final String FILENAME = "testFile.html";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String BASE_URI = "http://example.com";
    private static Document testDocument;

    @BeforeClass
    public static void setup() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"><title>Test Page</title></head><body>Test Content</body></html>";
        File testFile = new File(FILENAME);
        FileOutputStream fos = new FileOutputStream(testFile);
        OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        writer.write(fileContent);
        writer.close();
        fos.close();
        testDocument = new Document(fileContent, BASE_URI);
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new File(FILENAME), DEFAULT_CHARSET, BASE_URI},
                {new FileInputStream(new File(FILENAME)), DEFAULT_CHARSET, BASE_URI},
                {new ByteArrayInputStream(testDocument.html().getBytes(StandardCharsets.UTF_8)), DEFAULT_CHARSET, BASE_URI},
                {new FileInputStream(new File(FILENAME)), null, BASE_URI},
                {new ByteArrayInputStream(new byte[0]), null, BASE_URI},
                {null, DEFAULT_CHARSET, BASE_URI},
                {null, null, BASE_URI},
                {null, DEFAULT_CHARSET, null},
                {null, null, null}
        });
    }

    private File file;
    private InputStream inputStream;
    private String charsetName;
    private String baseUri;

    public DataUtilTest(File file, String charsetName, String baseUri) {
        this.file = file;
        this.charsetName = charsetName;
        this.baseUri = baseUri;
    }

    @Test
    public void testLoadDocument() throws IOException {
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertEquals(testDocument, document);
    }

    @Test(expected = IOException.class)
    public void testLoadDocumentWithNullFile() throws IOException {
        DataUtil.load(null, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(testDocument.html().getBytes(StandardCharsets.UTF_8));
        byteStream.close();
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(testDocument, document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInputStream() throws IOException {
        DataUtil.load(null, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        Document document = DataUtil.load(inputStream, null, baseUri);
        assertTrue(document != null);
    }

    @Test
    public void testLoadInputStreamWithNullBaseUri() throws IOException {
        Document document = DataUtil.load(inputStream, charsetName, null);
        assertTrue(document != null);
    }

    @Test
    public void testParseInputStream() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(testDocument.html().getBytes(StandardCharsets.UTF_8));
        byteStream.close();
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());

        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, mock(Parser.class));
        assertEquals(testDocument, document);

        verify(mock(Parser.class)).parseInput(eq(testDocument.html()), eq(baseUri));
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithNullInputStream() throws IOException {
        Document document = DataUtil.parseInputStream(null, charsetName, baseUri, mock(Parser.class));
    }

    @Test
    public void testParseInputStreamWithNullCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(testDocument.html().getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, baseUri, mock(Parser.class));
        assertTrue(document != null);
    }

    @Test
    public void testParseInputStreamWithNullBaseUri() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(testDocument.html().getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, charsetName, null, mock(Parser.class));
        assertTrue(document != null);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithNullParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(testDocument.html().getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, null);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testDocument.html().getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Test with valid data
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] expectedOutput = testDocument.html().getBytes(StandardCharsets.UTF_8);
        assertEquals(expectedOutput.length, outputStream.size());
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);

        // Test with empty input
        inputStream = new ByteArrayInputStream(new byte[0]);
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(0, outputStream.size());

        // Test with empty output
        outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(0, inputStream.available());
    }

    @AfterClass
    public static void teardown() throws IOException {
        File testFile = new File(FILENAME);
        testFile.delete();
    }
}