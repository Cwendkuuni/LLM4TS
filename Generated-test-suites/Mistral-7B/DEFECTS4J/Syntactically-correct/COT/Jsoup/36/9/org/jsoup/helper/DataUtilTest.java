import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        File file = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.body().text());

        // clean up
        file.delete();
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        byte[] inputStreamContentBytes = inputStreamContent.getBytes();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContentBytes);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        byte[] inputStreamContentBytes = inputStreamContent.getBytes();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContentBytes);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"gb2312\"></head><body>Test</body></html>";
        byte[] inputStreamContentBytes = inputStreamContent.getBytes("gb2312");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContentBytes);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.body().text());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithUnsupportedCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"XYZ\"></head><body>Test</body></html>";
        byte[] inputStreamContentBytes = inputStreamContent.getBytes();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContentBytes);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        DataUtil.load(inputStream, "XYZ", null);
    }

    @Test
    public void testParseByteData() throws IOException {
        String docData = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(docData.getBytes());
        Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.body().text());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String docData = "<html><head><meta charset=\"gb2312\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(docData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        String charsetName = "gb2312";
        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.body().text());
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String docData = "<html><head><meta charset=\"XYZ\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(docData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        String charsetName = "XYZ";
        DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        byte[] inputStreamContentBytes = inputStreamContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputStreamContentBytes);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedByteData = new byte[inputStreamContentBytes.length + 4]; // +4 for byte order mark
        System.arraycopy(inputStreamContentBytes, 0, expectedByteData, 0, inputStreamContentBytes.length);
        byteData.mark();
        byteData.order(ByteOrder.LITTLE_ENDIAN);
        byteData.put(expectedByteData, 0, expectedByteData.length);
        byteData.position(0);

        byte[] actualByteData = new byte[byteData.limit()];
        byteData.get(actualByteData, 0, actualByteData.length);

        byteData.rewind();
        byte[] byteOrderMark = new byte[1];
        byteData.get(byteOrderMark, 0, 1);
        assertEquals(65279, byteOrderMark[0]); // UTF-8 byte order mark
        assertArrayEquals(expectedByteData, actualByteData);
    }
}