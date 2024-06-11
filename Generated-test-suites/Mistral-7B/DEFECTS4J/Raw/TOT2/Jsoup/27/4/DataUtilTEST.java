
package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert.*;

public class DataUtilTest {

   @Test
   public void testLoadFile() throws IOException {
      File inputFile = new File("test.html");
      Document doc = DataUtil.load(inputFile, "UTF-8", "");
      assertNotNull(doc);
   }

   @Test(expected = FileNotFoundException.class)
   public void testLoadInvalidFilePath() throws IOException {
      Document doc = DataUtil.load(new File("nonexistentfile.html"), "UTF-8", "");
      fail("Expected FileNotFoundException");
   }

   @Test(expected = IOException.class)
   public void testLoadIOException() throws IOException {
      File inputFile = new File("test.html");
      InputStream inputStream = new ByteArrayInputStream(new byte[0]);
      try (FileInputStream in = new FileInputStream(inputFile);) {
         inputStream = new FileInputStream(inputFile);
      }
      InputStream inStream = inputStream; // Replace the valid input stream with the empty one
      Document doc = DataUtil.load(inStream, "UTF-8", "");
      fail("Expected IOException");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testLoadInvalidCharsetName() throws IOException {
      File inputFile = new File("test.html");
      Document doc = assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputFile, "", ""));
      assertEquals("Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML", doc.getMessage());
   }

   @Test
   public void testLoadNullBaseUri() throws IOException {
      File inputFile = new File("test.html");
      Document doc = DataUtil.load(inputFile, "UTF-8", null);
      assertNotNull(doc);
   }

   // Test cases for other methods go here

   @Test
   public void testParseByteDataWithDefaultCharset() {
      // Test case for parseByteData method with default charset
   }

   @Test
   public void testParseByteDataWithSpecifiedCharset() {
      // Test case for parseByteData method with specified charset
   }

   @Test
   public void testReadToByteBuffer() throws IOException {
      // Test case for readToByteBuffer method
   }

   @Test
   public void testGetCharsetFromContentType() {
      // Test case for getCharsetFromContentType method
   }

}