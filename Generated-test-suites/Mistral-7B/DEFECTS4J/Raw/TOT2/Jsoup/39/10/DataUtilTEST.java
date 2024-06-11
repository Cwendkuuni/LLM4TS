import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class DataUtilTest {
    // ...

    @Test
    public void testLoadFile() throws IOException {
        // ...
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() {
        // ...
    }

    @Test
    public void testLoadFileWithNullBaseUri() {
        // ...
    }

    @Test
    public void testLoadFileWithEmptyCharsetName() {
        // ...
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // ...
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharsetName() {
        // ...
    }

    @Test
    public void testLoadInputStreamWithNullBaseUri() {
        // ...
    }

    // ...

    @Test
    public void testParseByteDataWithValidCharset() {
        // Test Case for parseByteData with a valid charset
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        // Test Case for parseByteData with an invalid charset
    }

    @Test
    public void testParseByteDataWithEmptyCharsetName() {
        // Test Case for parseByteData with an empty charset name
    }

    @Test
    public void testParseByteDataWithNullByteData() {
        // Test Case for parseByteData with null byteData
    }

    @Test
    public void testParseByteDataWithUtf8Bom() {
        // Test Case for parseByteData with UTF-8 BOM
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithIOException() {
        // Test Case for parseByteData with IOException
    }

    @Test
    public void testReadToByteBufferWithValidInputStream() {
        // Test Case for readToByteBuffer with a valid input stream
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInvalidInputStream() {
        // Test Case for readToByteBuffer with an invalid input stream
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        // Test Case for readToByteBuffer with a maximum size
    }

    @Test
    public void testReadToByteBufferWithUnlimitedSize() {
        // Test Case for readToByteBuffer with an unlimited size
    }

    @Test
    public void testGetCharsetFromContentTypeWithValidContentType() {
        // Test Case for getCharsetFromContentType with a valid content type
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        // Test Case for getCharsetFromContentType with an invalid content type
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() {
        // Test Case for getCharsetFromContentType with an empty content type
    }
}