package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

    private Document htmlDocument;
    private Document xmlDocument;
    private InputStream inputStream;
    private OutputStream outputStream;
    private File file;

    @Before
    public void setUp() {
        htmlDocument = Mockito.mock(Document.class);
        xmlDocument = Mockito.mock(Document.class);
        inputStream = mock(InputStream.class);
        outputStream = mock(OutputStream.class);
        file = Mockito.mock(File.class);
    }

    @After
    public void tearDown() {
        Mockito.reset(htmlDocument, xmlDocument, inputStream, outputStream, file);
    }

    @Test
    public void testLoadHtmlFromFile() throws IOException {
        when(file.exists()).thenReturn(true);
        when(file.length()).thenReturn(1024);

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        ByteArrayInputStream inputStreamSpy = new ByteArrayInputStream(new byte[1024]);
        when(inputStream.read()).thenAnswer(i -> inputStreamSpy.read());
        when(inputStream.available()).thenReturn(1024);

        InputStream expectedInput = new ByteArrayInputStream(new byte[1024]);

        when(file.getAbsoluteFile()).thenReturn(new File("path/to/file.html"));
        Document actualDocument = DataUtil.load(file, charsetName, baseUri);

        assertEquals(expectedInput, actualDocument.inputStream());
        assertEquals(charsetName, actualDocument.outputSettings().charset());
        assertEquals(baseUri, actualDocument.baseUri());
    }

    @Test(expected = IOException.class)
    public void testLoadHtmlFromFileIOException() throws IOException {
        when(file.exists()).thenReturn(false);

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadXmlFromFile() throws IOException {
        when(file.exists()).thenReturn(true);
        when(file.length()).thenReturn(1024);

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        ByteArrayInputStream inputStreamSpy = new ByteArrayInputStream(new byte[1024]);
        when(inputStream.read()).thenAnswer(i -> inputStreamSpy.read());
        when(inputStream.available()).thenReturn(1024);

        InputStream expectedInput = new ByteArrayInputStream(new byte[1024]);

        when(file.getAbsoluteFile()).thenReturn(new File("path/to/file.xml"));
        Document actualDocument = DataUtil.load(file, charsetName, baseUri, DataUtil.Parser.xmlParser());

        assertEquals(expectedInput, actualDocument.inputStream());
        assertEquals(charsetName, actualDocument.outputSettings().charset());
        assertEquals(baseUri, actualDocument.baseUri());
    }

    @Test(expected = IOException.class)
    public void testLoadXmlFromFileIOException() throws IOException {
        when(file.exists()).thenReturn(false);

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        DataUtil.load(file, charsetName, baseUri, DataUtil.Parser.xmlParser());
    }

    @Test
    public void testLoadHtmlFromInputStream() throws IOException {
        ByteArrayOutputStream outputStreamSpy = new ByteArrayOutputStream();
        when(outputStream.write(Mockito.anyByte())).thenAnswer(a -> outputStreamSpy.write(a.getArgument(0)));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[1024]);

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        DataUtil.crossStreams(inputStream, outputStream);

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals(inputStream, actualDocument.inputStream());
        assertEquals(charsetName, actualDocument.outputSettings().charset());
        assertEquals(baseUri, actualDocument.baseUri());
    }

    @Test
    public void testParseInputStream() throws IOException {
        ByteArrayOutputStream outputStreamSpy = new ByteArrayOutputStream();
        when(outputStream.write(Mockito.anyByte())).thenAnswer(a -> outputStreamSpy.write(a.getArgument(0)));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        when(inputStream.read()).thenAnswer(i -> inputStream.read());
        when(inputStream.available()).thenReturn(1024);

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser();

        InputStream expectedInput = new ByteArrayInputStream(new byte[1024]);

        Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        assertEquals(expectedInput, actualDocument.inputStream());
        assertEquals(charsetName, actualDocument.outputSettings().charset());
        assertEquals(baseUri, actualDocument.baseUri());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[1024]);

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser();

        DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
    }

    @Test
    public void testParseInputStreamWithReader() throws IOException {
        ByteArrayOutputStream outputStreamSpy = new ByteArrayOutputStream();
        when(outputStream.write(Mockito.anyByte())).thenAnswer(a -> outputStreamSpy.write(a.getArgument(0)));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser();

        InputStream expectedInput = new ByteArrayInputStream(new byte[1024]);

        Document actualDocument = DataUtil.parseInputStream(inputStreamReader, charsetName, baseUri, parser);

        assertEquals(expectedInput, actualDocument.inputStream());
        assertEquals(charsetName, actualDocument.outputSettings().charset());
        assertEquals(baseUri, actualDocument.baseUri());
    }

    @Test
    public void testWriteToOutputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expectedOutput = outputStream.toByteArray();
        byte[] actualOutput = inputStream.readAllBytes();

        assertNotNull(expectedOutput);
        assertNotNull(actualOutput);
        assertEquals(expectedOutput.length, actualOutput.length);
    }
}