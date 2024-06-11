package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Mock
    private Parser parser;

    @Before
    public void setup() {
        DataUtil.defaultCharset = "UTF-8";
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        File file = new File("test.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        FileInputStream inStream = new FileInputStream(file);

        Document expectedDoc = parser.parseInput(fileContent, "");

        Document actualDoc = DataUtil.load(file, "UTF-8", "");

        assertEquals(expectedDoc, actualDoc);

        inStream.close();
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        File file = new File("test.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        FileInputStream inStream = new FileInputStream(file);

        Document expectedDoc = parser.parseInput(fileContent, "");

        Document actualDoc = DataUtil.load(file, null, "");

        assertEquals(expectedDoc, actualDoc);

        inStream.close();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream in = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDoc = parser.parseInput(inputStreamContent, "");

        Document actualDoc = DataUtil.load(in, "UTF-8", "");

        assertEquals(expectedDoc, actualDoc);

        in.close();
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream in = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDoc = parser.parseInput(inputStreamContent, "");

        Document actualDoc = DataUtil.load(in, null, "");

        assertEquals(expectedDoc, actualDoc);

        in.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream in = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDoc = parser.parseInput(inputStreamContent, "");

        Document actualDoc = DataUtil.load(in, "UTF-8", "", parser);

        assertEquals(expectedDoc, actualDoc);

        in.close();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String byteDataContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDoc = parser.parseInput(byteDataContent, "");

        Document actualDoc = DataUtil.parseByteData(byteData, null, "", parser);

        assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String byteDataContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDoc = parser.parseInput(byteDataContent, "");

        Mockito.when(parser.parseInput(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(expectedDoc);

        Document actualDoc = DataUtil.parseByteData(byteData, "UTF-8", "", parser);

        assertSame(expectedDoc, actualDoc);

        verify(parser).parseInput(byteDataContent, "");
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "Hello World";
        InputStream in = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer expectedByteData = ByteBuffer.wrap(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer actualByteData = DataUtil.readToByteBuffer(in);

        assertEquals(expectedByteData, actualByteData);

        in.close();
    }
}