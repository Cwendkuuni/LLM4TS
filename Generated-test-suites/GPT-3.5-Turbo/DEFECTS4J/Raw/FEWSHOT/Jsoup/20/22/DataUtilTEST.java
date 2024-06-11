import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {
    private File file;
    private InputStream inputStream;
    private String charsetName;
    private String baseUri;
    private Parser parser;

    @Before
    public void setup() {
        file = mock(File.class);
        inputStream = mock(InputStream.class);
        charsetName = "UTF-8";
        baseUri = "http://example.com";
        parser = mock(Parser.class);
    }

    @Test
    public void testLoad_File() throws IOException {
        FileInputStream inStream = mock(FileInputStream.class);

        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        whenNew(FileInputStream.class).withArguments(file).thenReturn(inStream);
        when(DataUtil.parseByteData(any(), eq(charsetName), eq(baseUri), eq(parser))).thenReturn(mock(Document.class));

        Document result = DataUtil.load(file, charsetName, baseUri);

        verify(inStream).close();
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testLoad_File_IOException() throws IOException {
        FileInputStream inStream = mock(FileInputStream.class);

        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        whenNew(FileInputStream.class).withArguments(file).thenReturn(inStream);
        doThrow(new IOException()).when(inStream).close();

        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoad_File_NonexistentFile() throws IOException {
        when(file.exists()).thenReturn(false);

        try {
            DataUtil.load(file, charsetName, baseUri);
            fail("Exception not thrown");
        } catch (IOException e) {
            assertEquals("File does not exist", e.getMessage());
        }
    }

    @Test
    public void testLoad_File_Directory() throws IOException {
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(false);

        try {
            DataUtil.load(file, charsetName, baseUri);
            fail("Exception not thrown");
        } catch (IOException e) {
            assertEquals("File is a directory", e.getMessage());
        }
    }

    @Test
    public void testLoad_File_UnreadableFile() throws IOException {
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(false);

        try {
            DataUtil.load(file, charsetName, baseUri);
            fail("Exception not thrown");
        } catch (IOException e) {
            assertEquals("Cannot read file", e.getMessage());
        }
    }

    @Test
    public void testLoad_InputStream() throws IOException {
        when(DataUtil.parseByteData(any(), eq(charsetName), eq(baseUri), eq(parser))).thenReturn(mock(Document.class));

        Document result = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testLoad_InputStream_IOException() throws IOException {
        when(DataUtil.parseByteData(any(), eq(charsetName), eq(baseUri), eq(parser))).thenThrow(new IOException());

        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testLoad_InputStream_NullInputStream() throws IOException {
        try {
            DataUtil.load((InputStream) null, charsetName, baseUri);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Input stream cannot be null", e.getMessage());
        }
    }

    @Test
    public void testLoad_InputStream_NullCharsetName() throws IOException {
        when(DataUtil.parseByteData(any(), eq(charsetName), eq(baseUri), eq(parser))).thenReturn(mock(Document.class));

        Document result = DataUtil.load(inputStream, null, baseUri);

        assertNotNull(result);
    }

    @Test
    public void testLoad_InputStream_NullBaseUri() throws IOException {
        when(DataUtil.parseByteData(any(), eq(charsetName), eq(baseUri), eq(parser))).thenReturn(mock(Document.class));

        Document result = DataUtil.load(inputStream, charsetName, null);

        assertNotNull(result);
    }

    @Test
    public void testLoad_InputStream_NullParser() throws IOException {
        Parser htmlParser = Parser.htmlParser();
        when(DataUtil.parseByteData(any(), eq(charsetName), eq(baseUri), eq(htmlParser))).thenReturn(mock(Document.class));

        Document result = DataUtil.load(inputStream, charsetName, baseUri, null);

        assertNotNull(result);
    }
}