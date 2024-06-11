import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.*;

public class DataUtilTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";

    @Test
    public void testLoadFromFile() throws IOException {
        File file = Mockito.mock(File.class);
        FileInputStream fileInputStream = new FileInputStream(file);
        String expectedCharset = "UTF-8";
        Document expectedDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>", BASE_URI);
        
        Mockito.when(DataUtil.readToByteBuffer(fileInputStream)).thenReturn(ByteBuffer.wrap("test".getBytes()));
        Mockito.when(DataUtil.parseByteData(ByteBuffer.wrap("test".getBytes()), expectedCharset, BASE_URI, Parser.htmlParser())).thenReturn(expectedDocument);
        
        Document actualDocument = DataUtil.load(file, expectedCharset, BASE_URI);
        
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        String expectedCharset = "UTF-8";
        Document expectedDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>", BASE_URI);
        
        Mockito.when(DataUtil.readToByteBuffer(inputStream)).thenReturn(ByteBuffer.wrap("test".getBytes()));
        Mockito.when(DataUtil.parseByteData(ByteBuffer.wrap("test".getBytes()), expectedCharset, BASE_URI, Parser.htmlParser())).thenReturn(expectedDocument);
        
        Document actualDocument = DataUtil.load(inputStream, expectedCharset, BASE_URI);
        
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        String expectedCharset = "UTF-8";
        Document expectedDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>", BASE_URI);
        Parser parser = Parser.xmlParser();
        
        Mockito.when(DataUtil.readToByteBuffer(inputStream)).thenReturn(ByteBuffer.wrap("test".getBytes()));
        Mockito.when(DataUtil.parseByteData(ByteBuffer.wrap("test".getBytes()), expectedCharset, BASE_URI, parser)).thenReturn(expectedDocument);
        
        Document actualDocument = DataUtil.load(inputStream, expectedCharset, BASE_URI, parser);
        
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithCharsetFromMeta() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = null;
        String baseUri = BASE_URI;
        Parser parser = Parser.htmlParser();
        
        String docData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body><h1>Test</h1></body></html>";
        Document expectedDocument = Jsoup.parse(docData, baseUri);
        
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithCharsetFromMetaHTML5() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = null;
        String baseUri = BASE_URI;
        Parser parser = Parser.htmlParser();
        
        String docData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        Document expectedDocument = Jsoup.parse(docData, baseUri);
        
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithCharsetFromContentType() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = BASE_URI;
        Parser parser = Parser.htmlParser();
        
        String docData = "<html><body><h1>Test</h1></body></html>";
        Document expectedDocument = Jsoup.parse(docData, baseUri);
        
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        
        int maxSize = 100;
        int bufferSize = 0x20000;
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
        
        Mockito.when(inputStream.read(buffer)).thenReturn(bufferSize).thenReturn(-1);
        
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(outStream.toByteArray());
        
        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        
        int bufferSize = 0x20000;
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
        
        Mockito.when(inputStream.read(buffer)).thenReturn(bufferSize).thenReturn(-1);
        
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(outStream.toByteArray());
        
        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);
        
        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String expectedCharset1 = "UTF-8";
        
        String contentType2 = "text/html";
        String expectedCharset2 = null;
        
        String contentType3 = null;
        String expectedCharset3 = null;
        
        String contentType4 = "text/html; charset=gb2312";
        String expectedCharset4 = "gb2312";
        
        String contentType5 = "text/html;CHARSET=UTF-8";
        String expectedCharset5 = "UTF-8";
        
        String contentType6 = "text/html; charset=EUC-JP";
        String expectedCharset6 = "EUC-JP";
        
        String contentType7 = "text/html; charset=UTF-16";
        String expectedCharset7 = "UTF-16";
        
        String contentType8 = "text/html; charset=ISO-8859-1";
        String expectedCharset8 = "ISO-8859-1";
        
        String contentType9 = "text/html; charset=";
        String expectedCharset9 = null;
        
        String contentType10 = "text/html; charset=unknown";
        String expectedCharset10 = null;
        
        String actualCharset1 = DataUtil.getCharsetFromContentType(contentType1);
        String actualCharset2 = DataUtil.getCharsetFromContentType(contentType2);
        String actualCharset3 = DataUtil.getCharsetFromContentType(contentType3);
        String actualCharset4 = DataUtil.getCharsetFromContentType(contentType4);
        String actualCharset5 = DataUtil.getCharsetFromContentType(contentType5);
        String actualCharset6 = DataUtil.getCharsetFromContentType(contentType6);
        String actualCharset7 = DataUtil.getCharsetFromContentType(contentType7);
        String actualCharset8 = DataUtil.getCharsetFromContentType(contentType8);
        String actualCharset9 = DataUtil.getCharsetFromContentType(contentType9);
        String actualCharset10 = DataUtil.getCharsetFromContentType(contentType10);
        
        assertEquals(expectedCharset1, actualCharset1);
        assertEquals(expectedCharset2, actualCharset2);
        assertEquals(expectedCharset3, actualCharset3);
        assertEquals(expectedCharset4, actualCharset4);
        assertEquals(expectedCharset5, actualCharset5);
        assertEquals(expectedCharset6, actualCharset6);
        assertEquals(expectedCharset7, actualCharset7);
        assertEquals(expectedCharset8, actualCharset8);
        assertEquals(expectedCharset9, actualCharset9);
        assertEquals(expectedCharset10, actualCharset10);
    }
}