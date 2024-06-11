import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

    private File file;
    private FileInputStream fileInputStream;
    private String charsetName;
    private String baseUri;
    private Parser parser;

    @Before
    public void setup() {
        file = mock(File.class);
        fileInputStream = mock(FileInputStream.class);
        charsetName = "UTF-8";
        baseUri = "http://www.example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadWithFile_shouldReturnDocument() {
        try {
            when(fileInputStream.read()).thenReturn(1); // Mocking read method
            when(file.length()).thenReturn(10L); // Mocking file length
            when(fileInputStream.available()).thenReturn(10); // Mocking available method
            Document expectedDocument = mock(Document.class);
            when(DataUtil.parseByteData(fileInputStream, charsetName, baseUri, parser)).thenReturn(expectedDocument);

            Document actualDocument = DataUtil.load(file, charsetName, baseUri);

            assertEquals(expectedDocument, actualDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadWithFile_shouldThrowExceptionWhenIOExceptionOccur() {
        try {
            when(fileInputStream.read()).thenThrow(new IOException("IO Exception occurred"));
            when(file.length()).thenReturn(10L);
            when(fileInputStream.available()).thenReturn(10);

            assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, baseUri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadWithInputStream_shouldReturnDocument() {
        try {
            when(fileInputStream.read()).thenReturn(1);
            when(fileInputStream.available()).thenReturn(10);
            Document expectedDocument = mock(Document.class);
            when(DataUtil.parseByteData(fileInputStream, charsetName, baseUri, parser)).thenReturn(expectedDocument);

            Document actualDocument = DataUtil.load(fileInputStream, charsetName, baseUri);

            assertEquals(expectedDocument, actualDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadWithInputStream_shouldThrowExceptionWhenIOExceptionOccur() {
        try {
            when(fileInputStream.read()).thenThrow(new IOException("IO Exception occurred"));
            when(fileInputStream.available()).thenReturn(10);

            assertThrows(IOException.class, () -> DataUtil.load(fileInputStream, charsetName, baseUri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadWithInputStreamAndParser_shouldReturnDocument() {
        try {
            when(fileInputStream.read()).thenReturn(1);
            when(fileInputStream.available()).thenReturn(10);
            Document expectedDocument = mock(Document.class);
            when(DataUtil.parseByteData(fileInputStream, charsetName, baseUri, parser)).thenReturn(expectedDocument);

            Document actualDocument = DataUtil.load(fileInputStream, charsetName, baseUri, parser);

            assertEquals(expectedDocument, actualDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadWithInputStreamAndParser_shouldThrowExceptionWhenIOExceptionOccur() {
        try {
            when(fileInputStream.read()).thenThrow(new IOException("IO Exception occurred"));
            when(fileInputStream.available()).thenReturn(10);

            assertThrows(IOException.class, () -> DataUtil.load(fileInputStream, charsetName, baseUri, parser));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteData_shouldReturnDocument() {
        try {
            ByteBuffer byteData = ByteBuffer.allocate(10);
            String docData = "<html><body><h1>Hello World!</h1></body></html>";
            byteData.put(docData.getBytes());
            byteData.flip();
            String expectedCharsetName = "UTF-8";
            Document expectedDocument = mock(Document.class);
            when(DataUtil.parseInput(docData, baseUri)).thenReturn(expectedDocument);

            Document actualDocument = DataUtil.parseByteData(byteData, null, baseUri, parser);

            assertEquals(expectedDocument, actualDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBuffer_shouldReturnByteBuffer() {
        try {
            FileInputStream inStream = mock(FileInputStream.class);
            byte[] buffer = new byte[DataUtil.bufferSize];
            when(inStream.read(buffer)).thenReturn(10);
            when(DataUtil.readToByteBuffer(inStream)).thenCallRealMethod();

            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);

            assertEquals(ByteBuffer.wrap(buffer, 0, 10), byteData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentType_shouldReturnCharset() {
        String contentType = "text/html; charset=EUC-JP";
        String expectedCharset = "EUC-JP";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentType_shouldReturnNullWhenContentTypeIsNull() {
        String contentType = null;

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(null, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentType_shouldReturnNullWhenContentTypeDoesNotContainCharset() {
        String contentType = "text/html";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(null, actualCharset);
    }
}