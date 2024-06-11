package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
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
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n<title>Test File</title>\n</head>\n<body>\n<p>Content of the file.</p>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document document = DataUtil.load(testFile, "ISO-8859-1", null);
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n<title>Test File</title>\n</head>\n<body>\n<p>Content of the file.</p>\n</body>\n</html>", document.html());

        DataUtil.load(testFile, null, null); // It should use the default charset "UTF-8" when the charsetName is null.
        document = DataUtil.load(testFile, null, null);
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n<title>Test File</title>\n</head>\n<body>\n<p>Content of the file.</p>\n</body>\n</html>", document.html());

        DataUtil.load(testFile, "UTF-8", null); // It should use the specified charset when charsetName is provided.
        document = DataUtil.load(testFile, "UTF-8", null);
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n<title>Test File</title>\n</head>\n<body>\n<p>Content of the file.</p>\n</body>\n</html>", document.html());

        testFile.delete(); // Delete the test file.
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testInputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n<title>Test InputStream</title>\n</head>\n<body>\n<p>Content of the InputStream.</p>\n</body>\n</html>";

        InputStream inputStream = new ByteArrayInputStream(testInputStreamContent.getBytes());

        Document document = DataUtil.load(null, null, null, inputStream);
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n<title>Test InputStream</title>\n</head>\n<body>\n<p>Content of the InputStream.</p>\n</body>\n</html>", document.html());

        Document documentWithDefaultCharset = DataUtil.load(null, null, null, inputStream);
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n<title>Test InputStream</title>\n</head>\n<body>\n<p>Content of the InputStream.</p>\n</body>\n</html>", documentWithDefaultCharset.html());

        Document documentWithSpecifiedCharset = DataUtil.load(null, "UTF-8", null, inputStream);
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n<title>Test InputStream</title>\n</head>\n<body>\n<p>Content of the InputStream.</p>\n</body>\n</html>", documentWithSpecifiedCharset.html());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=ISO-8859-1";
        String contentType3 = "text/html";

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType1));
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType2));
        assertNull(DataUtil.getCharsetFromContentType(contentType3));
    }

    @Test
    public void testParseByteData() throws IOException {
        String testByteDataContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n<title>Test ByteData</title>\n</head>\n<body>\n<p>Content of the ByteData.</p>\n</body>\n</html>";

        ByteBuffer byteData = ByteBuffer.wrap(testByteDataContent.getBytes());

        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n<title>Test ByteData</title>\n</head>\n<body>\n<p>Content of the ByteData.</p>\n</body>\n</html>", document.html());

        Document documentWithDefaultCharset = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n<title>Test ByteData</title>\n</head>\n<body>\n<p>Content of the ByteData.</p>\n</body>\n</html>", documentWithDefaultCharset.html());

        Document documentWithSpecifiedCharset = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n<title>Test ByteData</title>\n</head>\n<body>\n<p>Content of the ByteData.</p>\n</body>\n</html>", documentWithSpecifiedCharset.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testInputStreamContent = "Content of the InputStream.";
        InputStream inputStream = new ByteArrayInputStream(testInputStreamContent.getBytes());
        byte[] buffer = new byte[1024];

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertArrayEquals(testInputStreamContent.getBytes(), byteData.array());

        inputStream.close();
    }

    @Test
    public void testLoadWithInvalidContentTypeHeader() throws IOException {
        String contentType = "application/xml; foo=bar";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"" + contentType + "\">\n<title>Test File</title>\n</head>\n<body>\n<p>Content of the file.</p>\n</body>\n</html>".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(null, contentType, null, inputStream));

        inputStream.close();
    }
}