package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

  @Test
  public void testLoadFile() throws IOException {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Document document = DataUtil.load(file, charsetName, baseUri);
    Assert.assertNotNull(document);
  }

  @Test
  public void testLoadInputStream() throws IOException {
    InputStream inputStream = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Document document = DataUtil.load(inputStream, charsetName, baseUri);
    Assert.assertNotNull(document);
  }

  @Test
  public void testLoadInputStreamWithParser() throws IOException {
    InputStream inputStream = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
    Assert.assertNotNull(document);
  }

  @Test
  public void testCrossStreams() throws IOException {
    InputStream inputStream = new FileInputStream("input.txt");
    OutputStream outputStream = new FileOutputStream("output.txt");
    DataUtil.crossStreams(inputStream, outputStream);

    // Assert output.txt has the same content as input.txt
    String inputContent = readFileContent("input.txt");
    String outputContent = readFileContent("output.txt");
    Assert.assertEquals(inputContent, outputContent);
  }

  @Test
  public void testParseInputStream() throws IOException {
    InputStream inputStream = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
    Assert.assertNotNull(document);
  }

  @Test
  public void testReadToByteBuffer() throws IOException {
    InputStream inputStream = new FileInputStream("test.html");
    int maxSize = 1024;
    ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
    Assert.assertNotNull(byteBuffer);
    Assert.assertEquals(byteBuffer.limit(), maxSize);
  }

  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    Assert.assertEquals(charset, "UTF-8");
  }
  
  @Test
  public void testMimeBoundary() {
    String mimeBoundary = DataUtil.mimeBoundary();
    Assert.assertNotNull(mimeBoundary);
    Assert.assertEquals(mimeBoundary.length(), DataUtil.boundaryLength);
  }

  @Test
  public void testDetectCharsetFromBom() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(4);
    byteBuffer.put(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
    byteBuffer.rewind();
    DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
    Assert.assertNotNull(bomCharset);
    Assert.assertEquals(bomCharset.charset, "UTF-8");
    Assert.assertEquals(bomCharset.offset, true);
  }
  
  private String readFileContent(String fileName) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(fileName));
    StringBuilder content = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      content.append(line);
    }
    reader.close();
    return content.toString();
  }
}