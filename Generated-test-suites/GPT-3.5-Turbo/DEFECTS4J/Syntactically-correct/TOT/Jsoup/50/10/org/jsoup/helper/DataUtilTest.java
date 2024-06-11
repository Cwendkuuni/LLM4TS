
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

public class DataUtilTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "https://example.com";
    private static final String FILE_NAME = "data.html";
    private static final String TEST_FILE_CONTENTS = "<html><body><h1>Hello, World!</h1></body></html>";

    private static File createTestFile() throws IOException {
        File file = new File(FILE_NAME);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(TEST_FILE_CONTENTS);
        fileWriter.close();
        return file;
    }
    
    private static void deleteTestFile() {
        File file = new File(FILE_NAME);
        file.delete();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File testFile = createTestFile();

        try {
            Document doc = DataUtil.load(testFile, CHARSET_NAME, BASE_URI);
            assertNotNull(doc);
            assertEquals(TEST_FILE_CONTENTS, doc.html());

        } finally {
            deleteTestFile();
        }
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));

        try {
            Document doc = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
            assertNotNull(doc);
            assertEquals(TEST_FILE_CONTENTS, doc.html());

        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));

        try {
            Document doc = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
            assertNotNull(doc);
            assertEquals(TEST_FILE_CONTENTS, doc.html());

        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        byte[] inputBytes = TEST_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            DataUtil.crossStreams(inputStream, outputStream);
            byte[] outputBytes = outputStream.toByteArray();
            assertArrayEquals(inputBytes, outputBytes);

        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(TEST_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteBuffer, CHARSET_NAME, BASE_URI, Parser.htmlParser());

        assertNotNull(doc);
        assertEquals(TEST_FILE_CONTENTS, doc.html());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        byte[] inputBytes = TEST_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 20);
            byte[] outputBytes = byteBuffer.array();

            assertArrayEquals(inputBytes, outputBytes);
            assertEquals(20, outputBytes.length);

        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        byte[] inputBytes = TEST_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            byte[] outputBytes = byteBuffer.array();

            assertArrayEquals(inputBytes, outputBytes);
            assertEquals(inputBytes.length, outputBytes.length);

        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File testFile = createTestFile();

        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
            byte[] outputBytes = byteBuffer.array();

            byte[] expectedBytes = TEST_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8);
            assertArrayEquals(expectedBytes, outputBytes);

        } finally {
            deleteTestFile();
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNotSupported() {
        String contentType = "text/html; charset=not_supported";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
        assertTrue(isValidMimeBoundary(mimeBoundary));
    }

    private static boolean isValidMimeBoundary(String boundary) {
        for (char c : boundary.toCharArray()) {
            if (new String(DataUtil.mimeBoundaryChars).indexOf(c) == -1)
                return false;
        }
        return true;
    }
}
