import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head>\n<body><h1>Hello World</h1></body></html>";
        File file = new File("testFile.html");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(fileContent.getBytes());
        fos.close();

        Document doc = DataUtil.load(file, "ISO-8859-1", "");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset='ISO-8859-1'></head><body><h1>Hello World</h1></body></html>", doc.html());

        file.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFromFileWithNullCharset() throws IOException {
        File file = new File("testFile.html");
        DataUtil.load(file, null, "");
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head>\n<body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document doc = DataUtil.load(inputStream, "ISO-8859-1", "");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset='ISO-8859-1'></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamWithNullCharset() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head>\n<body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        DataUtil.load(inputStream, null);
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head>\n<body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document doc = DataUtil.load(inputStream, "", "");
        assertNotNull(doc);
        assertEquals("<html><head><meta charset='ISO-8859-1'></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithNullByteData() {
        ByteBuffer byteData = null;
        try {
            DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        } catch (IOException e) {
            Assert.assertEquals("Input stream must not be null", e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head>\n<body><h1>Hello World</h1></body></html>";
        File file = new File("testFile.html");
        FileInputStream inStream = new FileInputStream(file);

        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buf = new byte[byteData.limit()];
        byteData.get(buf);
        outStream.write(buf);
        String readData = new String(outStream.toByteArray(), Charset.forName("UTF-8"));
        assertEquals(fileContent, readData);

        file.delete();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=windows-1252";
        String expectedCharset = "WINDOWS-1252";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }
}