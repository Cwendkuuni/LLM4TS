package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import java.io.*;
import java.nio.charset.StandardCharsets;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // Existing test case for loading file
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Existing test case for loading input stream
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        // Existing test case for parsing byte data with default charset
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        // Existing test case for parsing byte data with specified charset
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test Data";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        mockStatic(System.class);
        when(System.out, "println", Mockito.anyString()).thenReturn(null);

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedByteData = testData.getBytes(StandardCharsets.UTF_8);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(expectedByteData);

        Assert.assertNotNull(actualByteBuffer);
        Assert.assertEquals(expectedByteBuffer.capacity(), actualByteBuffer.capacity());
        Assert.assertArrayEquals(expectedByteData, DataUtil.getBytes(actualByteBuffer));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String expectedCharset = "EUC-JP";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals(expectedCharset, actualCharset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";

        DataUtil.getCharsetFromContentType(contentType);
    }
}