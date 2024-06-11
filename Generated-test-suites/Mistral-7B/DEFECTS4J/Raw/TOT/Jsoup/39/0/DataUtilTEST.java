import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        String fileContent = "Hello World!";
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        // Given
        File testFile = new File("test.html");
        Files.write(testFile.toPath(), fileContent.getBytes(Charset.forName(charset)));

        // When
        Document doc = DataUtil.load(testFile, charset, baseUri);

        // Then
        Assertions.assertEquals("Hello World!", doc.body().text());

        // Cleanup
        testFile.delete();
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "Hello World!";

        // Given
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        // When
        Document doc = DataUtil.load(byteArrayInputStream, null);

        // Then
        Assertions.assertEquals("Hello World!", doc.body().text());

        // Cleanup
        byteArrayInputStream.close();
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "Hello World!";
        String charset = "UTF-8";

        // Given
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName(charset)));

        // When
        Document doc = DataUtil.load(byteArrayInputStream, charset);

        // Then
        Assertions.assertEquals("Hello World!", doc.body().text());

        // Cleanup
        byteArrayInputStream.close();
    }

    @Test
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "Hello World!";
        String charset = "invalidCharset";

        // When
        try {
            Document doc = DataUtil.load(new ByteArrayInputStream(inputStreamContent.getBytes()), charset);
        } catch (IOException e) {
            // Expected
            Assertions.assertEquals("Invalid charset 'invalidCharset'", e.getMessage());
        }
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String inputStreamContent = "Hello World!";

        // Given
        byte[] byteData = inputStreamContent.getBytes(Charset.forName("UTF-8"));

        // When
        String docData = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, null, Parser.htmlParser()).body().text();

        // Then
        Assertions.assertEquals("Hello World!", docData);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String inputStreamContent = "Hello World!";
        String charset = "invalidCharset";

        // Given
        byte[] byteData = inputStreamContent.getBytes(Charset.forName("UTF-8"));

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.parseByteData(ByteBuffer.wrap(byteData), charset, null, Parser.htmlParser());
        });
    }
}