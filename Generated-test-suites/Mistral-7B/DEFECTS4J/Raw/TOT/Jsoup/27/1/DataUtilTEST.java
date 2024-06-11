package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class DataUtilTest {

 @Test
 public void testLoadFile() throws IOException {
 String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 " <html>\n" +
 "<head>\n" +
 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
 "</head>\n" +
 "<body>\n" +
 "<p>This is a test document.</p>\n" +
 "</body>\n" +
 "</html>";
 File testFile = new File("testFile.html");
 FileWriter fileWriter = new FileWriter(testFile);
 fileWriter.write(fileContent);
 fileWriter.close();

 Document doc = DataUtil.load(testFile, "ISO-8859-1", "/");
 Assert.assertNotNull(doc);

 // Verify document content
 Element body = doc.body();
 Assert.assertEquals("This is a test document.", body.text());

 // Verify metadata
 Attributes meta = doc.head().selectFirst("meta[http-equiv=content-type]");
 Assert.assertNotNull(meta);
 String metaCharset = meta.attr("content").split(";")[0].trim();
 Assert.assertEquals("ISO-8859-1", metaCharset);

 // Clean up
 testFile.delete();
 }

 @Test
 public void testLoadInvalidFile() throws IOException {
 File testFile = new File("testFile.html");
 Assert.assertFalse(testFile.exists());

 try {
 Document doc = DataUtil.load(testFile, null, "/");
 fail("Expected IOException");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testLoadInputStream() throws IOException {
 String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 " <html>\n" +
 "<head>\n" +
 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
 "</head>\n" +
 "<body>\n" +
 "<p>This is a test document.</p>\n" +
 "</body>\n" +
 "</html>";

 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write(testData.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

 Document doc = DataUtil.load(byteArrayInputStream, "UTF-8", "/");
 Assert.assertNotNull(doc);

 // Verify document content
 Element body = doc.body();
 Assert.assertEquals("This is a test document.", body.text());

 // Verify metadata
 Assert.assertNull(doc.head().selectFirst("meta[http-equiv=content-type]"));
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 " <html>\n" +
 "<head>\n" +
 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
 "</head>\n" +
 "<body>\n" +
 "<p>This is a test document.</p>\n" +
 "</body>\n" +
 "</html>";

 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write(testData.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteArrayInputStream.readAllBytes()), null, "/", Parser.htmlParser());
 Assert.assertNotNull(doc);

 // Verify document content
 Element body = doc.body();
 Assert.assertEquals("This is a test document.", body.text());

 // Verify metadata
 Assert.assertNull(doc.head().selectFirst("meta[http-equiv=content-type]"));
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 " <html>\n" +
 "<head>\n" +
 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
 "</head>\n" +
 "<body>\n" +
 "<p>This is a test document.</p>\n" +
 "</body>\n" +
 "</html>";

 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write(testData.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteArrayInputStream.readAllBytes()), "ISO-8859-1", "/", Parser.htmlParser());
 Assert.assertNotNull(doc);

 // Verify document content
 Element body = doc.body();
 Assert.assertEquals("This is a test document.", body.text());

 // Verify metadata
 Assert.assertNotNull(doc.head().selectFirst("meta[http-equiv=content-type]"));
 Attributes meta = doc.head().selectFirst("meta[http-equiv=content-type]").attributes();
 String metaCharset = meta.get("content").split(";")[0].trim();
 Assert.assertEquals("ISO-8859-1", metaCharset);
 }

 @Test
 public void testParseByteDataWithDefaultCharsetDetected() throws IOException {
 String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 " <html>\n" +
 "<head>\n" +
 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
 "</head>\n" +
 "<body>\n" +
 "<p>This is a test document.</p>\n" +
 "</body>\n" +
 "</html>";

 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write(testData.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteArrayInputStream.readAllBytes()), null, "/", Parser.htmlParser());
 Assert.assertNotNull(doc);

 // Verify document content
 Element body = doc.body();
 Assert.assertEquals("This is a test document.", body.text());

 // Verify metadata
 Assert.assertNull(doc.head().selectFirst("meta[http-equiv=content-type]"));
 }

 @Test
 public void testParseByteDataWithSpecifiedCharsetDetected() throws IOException {
 String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 " <html>\n" +
 "<head>\n" +
 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
 "</head>\n" +
 "<body>\n" +
 "<p>This is a test document.</p>\n" +
 "</body>\n" +
 "</html>";

 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write(testData.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteArrayInputStream.readAllBytes()), "UTF-8", "/", Parser.htmlParser());
 Assert.assertNotNull(doc);

 // Verify document content
 Element body = doc.body();
 Assert.assertEquals("This is a test document.", body.text());

 // Verify metadata
 Assert.assertNotNull(doc.head().selectFirst("meta[http-equiv=content-type]"));
 Attributes meta = doc.head().selectFirst("meta[http-equiv=content-type]").attributes();
 String metaCharset = meta.get("content").split(";")[0].trim();
 Assert.assertEquals("UTF-8", metaCharset);
 }
}