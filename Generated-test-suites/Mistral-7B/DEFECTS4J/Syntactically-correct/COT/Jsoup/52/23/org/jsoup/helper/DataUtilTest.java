package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class DataUtilTest {

    

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with a valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertThat(document, is(notNullValue()));
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFilePath() throws IOException {
        File file = new File("non-existent-file.html"); // Replace with a non-existent file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(file, charsetName, baseUri); // Exception should be thrown here
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertThat(document, is(notNullValue()));
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInputStream() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load((InputStream) null, charsetName, baseUri); // Exception should be thrown here
    }

    @Test
    public void testParseByteData() {
        String testData = "<html><body></body></html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(testData.getBytes()));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertThat(document, is(notNullValue()));
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithNullByteData() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData((ByteBuffer) null, charsetName, baseUri, Parser.htmlParser()); // Exception should be thrown here
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] expectedOutput = "Test data".getBytes();
        byte[] actualOutput = outputStream.toByteArray();
        assertThat(actualOutput, is(equalTo(expectedOutput)));
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullInputOrOutput() throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream); // Exception should be thrown here
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF}); // UTF-32 BOM
        byteData.flip();
        String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertThat(charsetName, is("UTF-32"));

        byteData.rewind();
        byteData.put(new byte[]{(byte) 0xFE, (byte) 0xFF, (byte) 0x00, (byte) 0x00}); // UTF-16 LE BOM
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertThat(charsetName, is("UTF-16"));

        byteData.rewind();
        byteData.put(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}); // UTF-8 BOM
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertThat(charsetName, is("UTF-8"));

        byteData.rewind();
        byteData.put(new byte[]{(byte) 0xFE, (byte) 0xFF}); // UTF-16 BE BOM
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertThat(charsetName, is("UTF-16"));

        byteData.rewind();
        byteData.put(new byte[]{(byte) 0xFF, (byte) 0xFE}); // UTF-16 LE BOM
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertThat(charsetName, is("UTF-16"));

        byteData.rewind();
        byteData.put(new byte[]{(byte) 0xFF, (byte) 0xEF, (byte) 0xBB, (byte) 0xBF}); // UTF-8 BOM in wrong order
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertThat(charsetName, is(nullValue()));

        byteData.rewind();
        charsetName = DataUtil.detectCharsetFromBom(byteData, null); // No BOM present
        assertThat(charsetName, is(nullValue()));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charsetName = DataUtil.getCharsetFromContentType(contentType);
        assertThat(charsetName, is("UTF-8"));

        contentType = "text/html; charset=ISO-8859-1";
        charsetName = DataUtil.getCharsetFromContentType(contentType);
        assertThat(charsetName, is("ISO-8859-1"));

        contentType = "text/html; charset=GB2312";
        charsetName = DataUtil.getCharsetFromContentType(contentType);
        assertThat(charsetName, is("GB2312"));

        contentType = "text/html"; // No charset specified
        charsetName = DataUtil.getCharsetFromContentType(contentType);
        assertThat(charsetName, is(nullValue()));
    }

    @Test
    public void testValidateCharset() {
        String charsetName = "UTF-8";
        assertThat(DataUtil.validateCharset(charsetName), is(charsetName));

        charsetName = "UTF-8";
        charsetName = charsetName.toUpperCase();
        assertThat(DataUtil.validateCharset(charsetName), is(charsetName));

        charsetName = "InvalidCharset";
        assertThat(DataUtil.validateCharset(charsetName), is(nullValue()));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertThat(boundary.length(), is(32));
        for (int i = 0; i < boundary.length(); i++) {
            assertThat(boundary.charAt(i), is(oneOf(mimeBoundaryChars)));
        }
    }

    
}