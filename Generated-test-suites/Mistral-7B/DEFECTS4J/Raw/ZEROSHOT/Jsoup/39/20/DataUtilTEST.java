package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Mock
    private Parser parser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World!</h1>\n" +
                "</body>\n" +
                "</html>";

        File inputFile = new File("test.html");
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent.getBytes(Charset.forName("UTF-8")));

        when(parser.parseInput(anyString(), anyString())).thenReturn(new Document());

        Document document = DataUtil.load(inputFile, "UTF-8", null);

        assertEquals(document, DataUtil.load(inputStream, "UTF-8", null));
        assertEquals(document, DataUtil.load(byteBuffer, "UTF-8", null, parser));

        verify(parser).parseInput(anyString(), anyString());
    }

    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World!</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.load(inputStream, null, null);

        assertEquals(document, DataUtil.parseByteData(byteBuffer, null, null, parser));

        verify(parser).parseInput(eq("<?xml version=\"1.0\" encoding=\"UTF-8\"?"), anyString());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World!</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.load(inputStream, "GBK", null);

        assertEquals(document, DataUtil.parseByteData(byteBuffer, "UTF-8", null, parser));

        verify(parser, never()).parseInput(anyString(), anyString());
    }

    @Test
    public void testLoadInputStreamWithDetectedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=GBK\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World!</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.load(inputStream, null, null);

        assertEquals(document, DataUtil.parseByteData(byteBuffer, null, null, parser));

        verify(parser).parseInput(eq("<?xml version=\"1.0\" encoding=\"GBK\"?"), anyString());
    }

    @Test
    public void testLoadFileWithDetectedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=GBK\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World!</h1>\n" +
                "</body>\n" +
                "</html>";

        File inputFile = new File("test.html");
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.load(inputFile, null, null);

        assertEquals(document, DataUtil.parseByteData(byteBuffer, null, null, parser));

        verify(parser).parseInput(eq("<?xml version=\"1.0\" encoding=\"GBK\"?"), anyString());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=GBK\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World!</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.parseByteData(byteBuffer, null, null, parser);

        assertEquals(document, DataUtil.parseByteData(byteBuffer, null, null, parser));

        verify(parser).parseInput(eq("<?xml version=\"1.0\" encoding=\"GBK\"?"), anyString());
    }
}