package org.jsoup.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class DataUtilTest {

    private String baseUri = "https://www.example.com";

    @Test
    void load_File_ValidCharset_ValidBaseUri_ReturnsDocument() throws IOException {
        // Arrange
        File file = createTemporaryFile("test.html");
        String content = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        Files.write(file.toPath(), content.getBytes(Charset.forName("UTF-8")));
        
        // Act
        Document document = DataUtil.load(file, "UTF-8", baseUri);
        
        // Assert
        Assertions.assertEquals(content, document.html());
    }

    @Test
    void load_File_InvalidCharset_ValidBaseUri_ThrowsIOException() {
        // Arrange
        File file = createTemporaryFile("test.html");
        String content = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        try {
            Files.write(file.toPath(), content.getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Act and Assert
        Assertions.assertThrows(IOException.class, () -> {
            DataUtil.load(file, "INVALID-CHARSET", baseUri);
        });
    }

    @Test
    void load_File_ValidCharset_InvalidBaseUri_ReturnsDocumentWithBaseUriSetToNull() throws IOException {
        // Arrange
        File file = createTemporaryFile("test.html");
        String content = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        Files.write(file.toPath(), content.getBytes(Charset.forName("UTF-8")));

        // Act
        Document document = DataUtil.load(file, "UTF-8", null);

        // Assert
        Assertions.assertNull(document.baseUri());
    }

    @Test
    void load_File_NullCharset_ValidBaseUri_ThrowsNullPointerException() {
        // Arrange
        File file = createTemporaryFile("test.html");
        String content = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        try {
            Files.write(file.toPath(), content.getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Act and Assert
        Assertions.assertThrows(NullPointerException.class, () -> {
            DataUtil.load(file, null, baseUri);
        });
    }

    @Test
    void load_File_ValidCharset_NullBaseUri_ReturnsDocumentWithBaseUriSetToNull() throws IOException {
        // Arrange
        File file = createTemporaryFile("test.html");
        String content = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        Files.write(file.toPath(), content.getBytes(Charset.forName("UTF-8")));

        // Act
        Document document = DataUtil.load(file, "UTF-8", null);

        // Assert
        Assertions.assertNull(document.baseUri());
    }

    @Test
    void load_InputStream_ValidCharset_ValidBaseUri_ReturnsDocument() throws IOException {
        // Arrange
        String content = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));

        // Act
        Document document = DataUtil.load(inputStream, "UTF-8", baseUri);

        // Assert
        Assertions.assertEquals(content, document.html());
    }

    @Test
    void load_InputStream_InvalidCharset_ValidBaseUri_ThrowsIOException() {
        // Arrange
        String content = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));

        // Act and Assert
        Assertions.assertThrows(IOException.class, () -> {
            DataUtil.load(inputStream, "INVALID-CHARSET", baseUri);
        });
    }

    @Test
    void load_InputStream_ValidCharset_InvalidBaseUri_ReturnsDocumentWithBaseUriSetToNull() throws IOException {
        // Arrange
        String content = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));

        // Act
        Document document = DataUtil.load(inputStream, "UTF-8", null);

        // Assert
        Assertions.assertNull(document.baseUri());
    }

    @Test
    void load_InputStream_NullCharset_ValidBaseUri_ThrowsNullPointerException() {
        // Arrange
        String content = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));

        // Act and Assert
        Assertions.assertThrows(NullPointerException.class, () -> {
            DataUtil.load(inputStream, null, baseUri);
        });
    }

    @Test
    void load_InputStream_ValidCharset_NullBaseUri_ReturnsDocumentWithBaseUriSetToNull() throws IOException {
        // Arrange
        String content = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));

        // Act
        Document document = DataUtil.load(inputStream, "UTF-8", null);

        // Assert
        Assertions.assertNull(document.baseUri());
    }

    @Test
    void load_InputStream_ValidCharset_ValidBaseUri_ValidParser_ReturnsDocument() throws IOException {
        // Arrange
        String content = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));

        // Act
        Document document = DataUtil.load(inputStream, "UTF-8", baseUri, Parser.htmlParser());

        // Assert
        Assertions.assertEquals(content, document.html());
    }

    @Test
    void parseByteData_NullCharset_ReturnsDocumentWithBaseUriSetToNull() {
        // Arrange
        String data = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(data);

        // Act
        Document document = DataUtil.parseByteData(byteBuffer, null, baseUri, Parser.htmlParser());

        // Assert
        Assertions.assertNull(document.baseUri());
    }

    @Test
    void parseByteData_ValidCharset_ReturnsDocument() {
        // Arrange
        String data = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(data);

        // Act
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", baseUri, Parser.htmlParser());

        // Assert
        Assertions.assertEquals(data, document.html());
    }

    @Test
    void parseByteData_ValidCharsetWithContentMetaCharset_ReturnsDocument() {
        // Arrange
        String data = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=gb2312\"></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(data);

        // Act
        Document document = DataUtil.parseByteData(byteBuffer, null, baseUri, Parser.htmlParser());

        // Assert
        Assertions.assertEquals(data, document.html());
    }

    @Test
    void parseByteData_InvalidCharsetWithContentMetaCharset_ThrowsUnsupportedCharsetException() {
        // Arrange
        String data = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=INVALID-CHARSET\"></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(data);

        // Act and Assert
        Assertions.assertThrows(UnsupportedCharsetException.class, () -> {
            DataUtil.parseByteData(byteBuffer, null, baseUri, Parser.htmlParser());
        });
    }

    @Test
    void readToByteBuffer_ValidInputStream_ReturnsByteBuffer() {
        // Arrange
        String content = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));

        // Act
        ByteBuffer byteBuffer = null;
        try {
            byteBuffer = DataUtil.readToByteBuffer(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        Assertions.assertNotNull(byteBuffer);
    }

    @Test
    void getCharsetFromContentType_ValidContentType_ReturnsCharset() {
        // Arrange
        String contentType = "text/html;charset=gb2312";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assertions.assertEquals("GB2312", charset);
    }

    @Test
    void getCharsetFromContentType_InvalidContentType_ReturnsNull() {
        // Arrange
        String contentType = "text/html";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assertions.assertNull(charset);
    }

    private File createTemporaryFile(String fileName) throws IOException {
        Path tempDirectory = Paths.get("target");
        Path tempFile = tempDirectory.resolve(fileName);
        Files.createDirectories(tempDirectory);
        return Files.createFile(tempFile).toFile();
    }
}