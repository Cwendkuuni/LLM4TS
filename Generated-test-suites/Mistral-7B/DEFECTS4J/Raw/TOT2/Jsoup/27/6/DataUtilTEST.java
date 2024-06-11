import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {
    // ... (previous test cases)

    @Test
    public void testLoadFromEmptyFile() throws IOException {
        File file = new File("empty.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertEquals("", doc.body().text());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadFromNonExistentFile() throws IOException {
        File file = new File("non_existent.html");
        DataUtil.load(file, "UTF-8", null);
    }

    @Test
    public void testLoadFromCorruptedFile() throws IOException {
        String corruptedHtml = "<html<body><h1>Hello World</h1></body></html>"; // missing '>' in 'html' tag
        ByteArrayInputStream in = new ByteArrayInputStream(corruptedHtml.getBytes());
        Document doc = null;
        try {
            doc = DataUtil.load(in, "UTF-8", null);
        } catch (IOException e) {
            // Expected IOException
        }
        assertNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadWithNullInputStream() {
        DataUtil.load((InputStream) null, "UTF-8", null);
    }

    @Test
    public void testLoadWithEmptyInputStream() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.load(in, "UTF-8", null);
        assertEquals("", doc.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadWithNonSupportedCharset() throws IOException {
        Document doc = null;
        try {
            doc = DataUtil.load(new File("test.html"), null, null);
        } catch (IOException e) {
            // Expected IOException
        }
        ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(new File("test.html")));
        doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(new ByteArrayInputStream(new byte[0]), "X-CHARSET", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadWithNullBaseUri() {
        DataUtil.load(new File("test.html"), "UTF-8", null);
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(new FileInputStream("test.html"), null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadWithNullParser() {
        DataUtil.load(new File("test.html"), "UTF-8", null);
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(new FileInputStream("test.html"), "UTF-8", null, null));
    }
}