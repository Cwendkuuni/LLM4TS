import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataUtilTest {
  
  private File mockFile;
  private InputStream mockInputStream;
  private Document mockDocument;
  private String charsetName;
  private String baseUri;
  private Parser parser;
  
  @Before
  public void setup() {
    mockFile = mock(File.class);
    mockInputStream = mock(InputStream.class);
    mockDocument = mock(Document.class);
    charsetName = Charset.defaultCharset().name();
    baseUri = "http://www.example.com";
    parser = Parser.htmlParser();
  }

  @Test
  public void testLoadWithFile() throws IOException {
    // Typical use case
    when(mockFile.exists()).thenReturn(true);
    when(mockFile.isFile()).thenReturn(true);

    Document result = DataUtil.load(mockFile, charsetName, baseUri);

    assertNotNull(result);
  }

  @Test
  public void testLoadWithFile_NotFile() throws IOException {
    // Error scenario - given file is not actually a file
    when(mockFile.exists()).thenReturn(true);
    when(mockFile.isFile()).thenReturn(false);

    Document result = DataUtil.load(mockFile, charsetName, baseUri);

    assertNull(result);
  }

  @Test(expected = IOException.class)
  public void testLoadWithFile_IOError() throws IOException {
    // Error scenario - IOException when trying to load file
    when(mockFile.exists()).thenReturn(true);
    when(mockFile.isFile()).thenReturn(true);
    when(mockFile.canRead()).thenReturn(true);
    when(mockFile.getPath()).thenReturn("some/file/path");

    whenNew(FileInputStream.class).withArguments(mockFile).thenThrow(new IOException());

    DataUtil.load(mockFile, charsetName, baseUri);
  }

  @Test
  public void testLoadWithInputStream() throws IOException {
    // Typical use case
    when(mockInputStream.available()).thenReturn(10);

    Document result = DataUtil.load(mockInputStream, charsetName, baseUri);

    assertNotNull(result);
  }

  @Test(expected = IOException.class)
  public void testLoadWithInputStream_IOError() throws IOException {
    // Error scenario - IOException when trying to load InputStream
    when(mockInputStream.available()).thenReturn(10);
    doThrow(new IOException()).when(mockInputStream).close();

    DataUtil.load(mockInputStream, charsetName, baseUri);
  }

  @Test
  public void testLoadWithInputStream_CustomParser() throws IOException {
    // Typical use case - load with a custom Parser
    Parser customParser = mock(Parser.class);
    when(mockInputStream.available()).thenReturn(10);

    Document result = DataUtil.load(mockInputStream, charsetName, baseUri, customParser);

    assertNotNull(result);
  }

  @Test
  public void testCrossStreams() throws IOException {
    // Typical use case
    InputStream mockIn = mock(InputStream.class);
    OutputStream mockOut = mock(OutputStream.class);

    DataUtil.crossStreams(mockIn, mockOut);

    verify(mockIn, atLeastOnce()).read(any(byte[].class));
    verify(mockOut, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
  }

  @Test
  public void testParseInputStream() throws IOException {
    // Typical use cases
    InputStream inputStream1 = getClass().getResourceAsStream("test.html");
    Document result1 = DataUtil.parseInputStream(inputStream1, charsetName, baseUri, parser);
    assertNotNull(result1);

    InputStream inputStream2 = getClass().getResourceAsStream("test.xml");
    Document result2 = DataUtil.parseInputStream(inputStream2, charsetName, baseUri, Parser.xmlParser());
    assertNotNull(result2);
  }

  @Test
  public void testReadToByteBuffer() throws IOException {
    // Typical use case
    InputStream mockInStream = mock(InputStream.class);
    int maxSize = 1024;

    assertNotNull(DataUtil.readToByteBuffer(mockInStream, maxSize));
  }

  @Test(expected = IOException.class)
  public void testReadToByteBuffer_IOError() throws IOException {
    // Error scenario - IOException when reading from input stream
    InputStream mockInStream = mock(InputStream.class);
    int maxSize = 1024;

    when(mockInStream.read(any(byte[].class))).thenThrow(new IOException());

    DataUtil.readToByteBuffer(mockInStream, maxSize);
  }

  @Test
  public void testReadFileToByteBuffer() throws IOException {
    // Typical use case
    File file = new File("test.txt");

    assertNotNull(DataUtil.readFileToByteBuffer(file));
  }

  @Test(expected = IOException.class)
  public void testReadFileToByteBuffer_IOError() throws IOException {
    // Error scenario - IOException when reading file
    File file = new File("test.txt");

    RandomAccessFile mockRandomAccessFile = mock(RandomAccessFile.class);
    when(mockRandomAccessFile.readFully(any(byte[].class))).thenThrow(new IOException());
    whenNew(RandomAccessFile.class).withArguments(file, "r").thenReturn(mockRandomAccessFile);

    DataUtil.readFileToByteBuffer(file);
  }

  @Test
  public void testEmptyByteBuffer() {
    ByteBuffer result = DataUtil.emptyByteBuffer();

    assertNotNull(result);
    assertEquals(0, result.capacity());
  }

  @Test
  public void testGetCharsetFromContentType() {
    // Typical use cases
    String contentType1 = "text/html; charset=UTF-8";
    assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType1));

    String contentType2 = "text/html;charset=ISO-8859-1";
    assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType2));

    // Edge case - no charset specified
    String contentType3 = "text/html";
    assertNull(DataUtil.getCharsetFromContentType(contentType3));

    // Error scenario - invalid charset name
    String contentType4 = "text/html;charset=InvalidCharset";
    assertNull(DataUtil.getCharsetFromContentType(contentType4));
  }

  @Test
  public void testValidateCharset() {
    // Typical use case
    String charset = "UTF-8";
    assertEquals("UTF-8", DataUtil.validateCharset(charset));

    // Error scenario - invalid charset name
    String invalidCharset = "InvalidCharset";
    assertNull(DataUtil.validateCharset(invalidCharset));
  }

  @Test
  public void testMimeBoundary() {
    String result = DataUtil.mimeBoundary();

    assertNotNull(result);
    assertEquals(DataUtil.boundaryLength, result.length());
  }
}