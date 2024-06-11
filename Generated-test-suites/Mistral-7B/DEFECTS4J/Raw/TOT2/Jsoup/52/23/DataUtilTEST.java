import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("input.html");
        Document document = DataUtil.load(inputFile, null, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("input.html");
        Document document = DataUtil.load(inputFile, "UTF-16", null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputData = "input stream data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        Document document = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputData = "input stream data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-16", null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputData = "input stream data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        Document document = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.html");
        File outputFile = new File("output.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        File outputDocFile = new File("output.html");
        Document outputDocument = DataUtil.load(outputDocFile, null, null);
        Assert.assertNotNull(outputDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadInvalidFile() throws IOException {
        File inputFile = new File("invalid_file.txt");
        Document document = DataUtil.load(inputFile, null, null);
        Assert.fail(); // Test should throw an IOException
    }

    @Test(expected = IOException.class)
    public void testLoadNullInputStream() throws IOException {
        InputStream inputStream = null;
        Document document = DataUtil.load(inputStream, null, null);
        Assert.fail(); // Test should throw an IOException
    }
}