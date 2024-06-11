package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {
  
  @Test
  public void load_FileExists_ShouldReturnDocument() throws IOException {
    // Arrange
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://www.example.com";
    
    // Act
    Document document = DataUtil.load(file, charsetName, baseUri);
    
    // Assert
    assertNotNull("Document should not be null", document);
    assertThat(document.location(), is(baseUri));
  }
  
  @Test
  public void load_FileDoesNotExist_ShouldThrowIOException() {
    // Arrange
    File file = new File("nonexistent.html");
    String charsetName = "UTF-8";
    String baseUri = "http://www.example.com";
    
    // Act & Assert
    try {
      Document document = DataUtil.load(file, charsetName, baseUri);
    } catch (IOException e) {
      assertThat(e.getMessage(), containsString("No such file or directory"));
    }
  }
  
  @Test
  public void load_InputStreamExists_ShouldReturnDocument() throws IOException {
    // Arrange
    InputStream inputStream = mock(InputStream.class);
    when(inputStream.read()).thenReturn('t', 'e', 's', 't');
    String charsetName = "UTF-8";
    String baseUri = "http://www.example.com";
    
    // Act
    Document document = DataUtil.load(inputStream, charsetName, baseUri);
    
    // Assert
    assertNotNull("Document should not be null", document);
    assertThat(document.location(), is(baseUri));
  }
  
  @Test
  public void load_InputStreamDoesNotExist_ShouldThrowIOException() {
    // Arrange
    InputStream inputStream = null;
    String charsetName = "UTF-8";
    String baseUri = "http://www.example.com";
    
    // Act & Assert
    try {
      Document document = DataUtil.load(inputStream, charsetName, baseUri);
    } catch (IOException e) {
      assertThat(e.getMessage(), containsString("Stream closed"));
    }
  }
  
  @Test
  public void load_WithParser_ShouldReturnDocument() throws IOException {
    // Arrange
    InputStream inputStream = mock(InputStream.class);
    when(inputStream.read()).thenReturn('t', 'e', 's', 't');
    String charsetName = "UTF-8";
    String baseUri = "http://www.example.com";
    Parser parser = Parser.xmlParser();
    
    // Act
    Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
    
    // Assert
    assertNotNull("Document should not be null", document);
    assertThat(document.location(), is(baseUri));
  }
  
  @Test
  public void parseByteData_WithCharset_ShouldReturnDocument() {
    // Arrange
    ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://www.example.com";
    Parser parser = Parser.htmlParser();
    
    // Act
    Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    
    // Assert
    assertNotNull("Document should not be null", document);
    assertThat(document.location(), is(baseUri));
  }
  
  @Test
  public void parseByteData_WithoutCharset_ShouldReturnDocumentWithDefaultCharset() {
    // Arrange
    ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
    String charsetName = null;
    String baseUri = "http://www.example.com";
    Parser parser = Parser.htmlParser();
    
    // Act
    Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    
    // Assert
    assertNotNull("Document should not be null", document);
    assertThat(document.location(), is(baseUri));
    assertThat(document.outputSettings().charset(), is(DataUtil.defaultCharset));
  }
  
  @Test
  public void readToByteBuffer_StreamExists_ShouldReturnByteBuffer() throws IOException {
    // Arrange
    InputStream inputStream = mock(InputStream.class);
    when(inputStream.read()).thenReturn('t', 'e', 's', 't');
    
    // Act
    ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
    
    // Assert
    assertNotNull("ByteBuffer should not be null", byteBuffer);
    assertThat(byteBuffer.capacity(), is(4));
  }
  
  @Test
  public void readToByteBuffer_StreamDoesNotExist_ShouldReturnNullByteBuffer() throws IOException {
    // Arrange
    InputStream inputStream = null;
    
    // Act
    ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
    
    // Assert
    assertNull("ByteBuffer should be null", byteBuffer);
  }
  
  @Test
  public void getCharsetFromContentType_WithCharset_ShouldReturnCharset() {
    // Arrange
    String contentType = "text/html; charset=UTF-8";
    
    // Act
    String charset = DataUtil.getCharsetFromContentType(contentType);
    
    // Assert
    assertThat(charset, is("UTF-8"));
  }
  
  @Test
  public void getCharsetFromContentType_WithoutCharset_ShouldReturnNull() {
    // Arrange
    String contentType = "text/html";
    
    // Act
    String charset = DataUtil.getCharsetFromContentType(contentType);
    
    // Assert
    assertNull("Charset should be null", charset);
  }
}