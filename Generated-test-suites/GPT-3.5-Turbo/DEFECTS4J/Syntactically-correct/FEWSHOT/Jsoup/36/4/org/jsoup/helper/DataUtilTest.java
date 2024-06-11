package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // create test file
        File testFile = new File("testFile.txt");
        try {
            testFile.createNewFile();
            FileWriter fileWriter = new FileWriter(testFile);
            fileWriter.write("Test data");
            fileWriter.close();

            Document doc = DataUtil.load(testFile, null, null);
            assertEquals("Test data", doc.text());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // delete test file
            testFile.delete();
        }
    }

    @Test
    public void testLoadInputStream() {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        try {
            Document doc = DataUtil.load(inputStream, null, null);
            assertEquals(testData, doc.text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithCharset() {
        String testData = "Test data";
        String charset = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        try {
            Document doc = DataUtil.load(inputStream, charset, null);
            assertEquals(testData, doc.text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithCharsetAndBaseUri() {
        String testData = "Test data";
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        try {
            Document doc = DataUtil.load(inputStream, charset, baseUri);
            assertEquals(testData, doc.text());
            assertEquals(baseUri, doc.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteDataWithCharset() {
        String testData = "Test data";
        String charset = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        Document doc = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
        assertEquals(testData, doc.text());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        String testData = "Test data";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(testData, doc.text());
    }

    @Test
    public void testReadToByteBuffer() {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
            String result = new String(byteData.array(), Charset.forName("UTF-8"));
            assertEquals(testData, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String result = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", result);
    }

    @Test
    public void testGetCharsetFromContentTypeUnsupportedCharset() {
        String contentType = "text/html; charset=INVALID-CHARSET";
        String result = DataUtil.getCharsetFromContentType(contentType);
        assertNull(result);
    }

}