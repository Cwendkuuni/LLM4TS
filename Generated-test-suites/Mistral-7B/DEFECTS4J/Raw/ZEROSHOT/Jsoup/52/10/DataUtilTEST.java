package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {
    private Parser parser;
    private Document document;

    @Before
    public void setup() {
        parser = Mockito.mock(Parser.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "Hello World!";
        File file = new File("test.html");
        ByteBuffer expectedByteData = ByteBuffer.wrap(fileContent.getBytes(Charset.forName("UTF-8")));

        Document actualDocument = DataUtil.load(file, "UTF-8", "");
        assertEquals(expectedByteData, actualDocument.inputEncoding().asReadOnlyBuffer());
        assertEquals(expectedByteData.limit(), actualDocument.body().text().getBytes(Charset.forName("UTF-8")).length);
        assertNotNull(actualDocument);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "Hello World!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName("UTF-8")));
        ByteBuffer expectedByteData = ByteBuffer.wrap(inputStreamContent.getBytes(Charset.forName("UTF-8")));

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "");
        assertEquals(expectedByteData, actualDocument.inputEncoding().asReadOnlyBuffer());
        assertEquals(expectedByteData.limit(), actualDocument.body().text().getBytes(Charset.forName("UTF-8")).length);
        assertNotNull(actualDocument);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "Hello World!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName("UTF-8")));
        ByteBuffer expectedByteData = ByteBuffer.wrap(inputStreamContent.getBytes(Charset.forName("UTF-8")));

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "", parser);
        assertEquals(expectedByteData, actualDocument.inputEncoding().asReadOnlyBuffer());
        assertEquals(expectedByteData.limit(), actualDocument.body().text().getBytes(Charset.forName("UTF-8")).length);
        assertNotNull(actualDocument);
        Mockito.verify(parser).parseInput(expectedByteData.asReadOnlyBuffer().toString(), "");
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String docData = "Hello World!";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, "", parser);
        assertEquals(expectedByteData, actualDocument.inputEncoding().asReadOnlyBuffer());
        assertEquals(expectedByteData.limit(), actualDocument.body().text().getBytes(Charset.forName(charsetName)).length);
        assertNotNull(actualDocument);
        Mockito.verify(parser).parseInput(docData, "");
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Input Stream".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        // Verify the content of the output stream matches the input stream
        byte[] outputStreamContent = outputStream.toByteArray();
        assertEquals(inputStream.available(), outputStreamContent.length);
        assertEquals(new String(inputStream.readAllBytes(), Charset.forName("UTF-8")), new String(outputStreamContent, Charset.forName("UTF-8")));
    }
}