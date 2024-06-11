package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        Document doc = DataUtil.load(new File(filePath), null, "");
        assertEquals("Test file content", doc.body().text(), "Test file content");
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        Document doc = DataUtil.load(new File(filePath), "UTF-16LE", "");
        assertEquals("Test file content", doc.body().text(), "Test file content");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String content = "Test input stream content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        Document doc = DataUtil.load(inputStream, null, "");
        assertEquals("Test input stream content", doc.body().text(), content);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String content = "Test input stream content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_16LE.name()));
        Document doc = DataUtil.load(inputStream, "UTF-16LE", "");
        assertEquals("Test input stream content", doc.body().text(), content);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String content = "Test input stream content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        Document doc = DataUtil.load(inputStream, null, "", Parser.xmlParser());
        assertEquals("Test input stream content", doc.body().text(), content);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source text";
        String target = "Target text";
        String inputContent = source + "\n" + target;
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        OutputStream outputStream = new FileOutputStream("output.txt");

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(source + "\n" + target, Files.readString(new File("output.txt").toPath()));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-16";
        String fileContent = "<html><body><p>Test content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(charsetName));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());

        assertEquals("Test content", doc.body().text(), "Test content");
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        byte[] fileContent = Files.readAllBytes(new File(filePath).toPath());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(filePath), fileContent.length);

        assertEquals(fileContent, byteBuffer.array());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        byte[] fileContent = Files.readAllBytes(new File(filePath).toPath());
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File(filePath));

        assertEquals(fileContent, byteBuffer.array());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-16";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("UTF-16", validatedCharset);

        charset = "UTF-16BE";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("UTF-16BE", validatedCharset);

        charset = "invalidCharset";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBOM() throws IOException {
        String filePath = "src/test/resources/testFileWithBOM.html";
        byte[] fileContent = Files.readAllBytes(new File(filePath).toPath());
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertEquals("UTF-16BE", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBOMWithInvalidBOM() throws IOException {
        String filePath = "src/test/resources/testFileWithoutBOM.html";
        byte[] fileContent = Files.readAllBytes(new File(filePath).toPath());
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertNull(bomCharset);
    }

    private InputStreamReader getMockInputStreamReader(String charsetName) {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.markSupported()).thenReturn(true);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charsetName);
        return inputStreamReader;
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String charsetName = "UTF-16BE";
        String fileContent = "<html><body><p>Test content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        InputStreamReader inputStreamReader = getMockInputStreamReader(charsetName);
        Document doc = null;

        try {
            doc = DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
            assertEquals("Test content", doc.body().text(), "Test content");
        } finally {
            inputStream.close();
            inputStreamReader.close();
        }
    }

    @Test
    public void testParseInputStreamWithBOMAndInvalidCharset() throws IOException {
        String charsetName = "invalidCharset";
        String fileContent = "<html><body><p>Test content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        InputStreamReader inputStreamReader = getMockInputStreamReader(charsetName);
        Document doc = null;

        try {
            doc = DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        } finally {
            inputStream.close();
            inputStreamReader.close();
        }
    }
}

class ByteArrayInputStream implements InputStream {

    private final byte[] data;

    ByteArrayInputStream(byte[] data) {
        this.data = data;
    }

    @Override
    public int read() throws IOException {
        if (data.length > 0) {
            byte value = data[0];
            data = Arrays.copyOfRange(data, 1, data.length);
            return value;
        }
        return -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = 0;
        int limit = data.length;

        while (len > 0 && limit > 0) {
            int min = Math.min(len, limit);
            System.arraycopy(data, off + bytesRead, b, off + bytesRead, min);
            len -= min;
            limit -= min;
            off += min;
            bytesRead += min;
        }

        if (len > 0) {
            throw new IOException("Unexpected end of stream");
        }

        return bytesRead;
    }

    @Override
    public void close() throws IOException {
        // Empty
    }
}

class FileInputStream implements InputStream {

    private final File file;

    FileInputStream(File file) {
        this.file = file;
    }

    @Override
    public int read() throws IOException {
        int c = -1;
        FileChannel channel = new RandomAccessFile(file, "r").getChannel();
        try {
            c = (int) channel.read();
        } catch (IOException e) {
            throw new IOException("Error reading file", e);
        } finally {
            channel.close();
        }
        return c;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        FileChannel channel = new RandomAccessFile(file, "r").getChannel();
        try {
            int bytesRead = channel.read(ByteBuffer.wrap(b, off, len));
            return bytesRead;
        } catch (IOException e) {
            throw new IOException("Error reading file", e);
        } finally {
            channel.close();
        }
    }

    @Override
    public void close() throws IOException {
        new RandomAccessFile(file, "r").close();
    }
}

class RandomAccessFileWithCloseMethod implements RandomAccessFile {

    private final File file;

    RandomAccessFileWithCloseMethod(File file) {
        this.file = file;
    }

    @Override
    public long length() throws IOException {
        return new FileInputStream(file).available();
    }

    @Override
    public int read() throws IOException {
        InputStream inputStream = new FileInputStream(file);
        int result = inputStream.read();
        inputStream.close();
        return result;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        int result = inputStream.read(b, off, len);
        inputStream.close();
        return result;
    }

    @Override
    public void close() throws IOException {
        // Empty
    }

    @Override
    public long getFilePointer() throws IOException {
        return 0;
    }

    @Override
    public void seek(long pos) throws IOException {
        // Empty
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        // Empty
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void channels() {
        // Empty
    }
}