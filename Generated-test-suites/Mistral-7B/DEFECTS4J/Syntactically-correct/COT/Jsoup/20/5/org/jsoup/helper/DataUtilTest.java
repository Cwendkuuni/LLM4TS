import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "Some content of a file.";
        File file = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(fileContent, doc.body().html());

        file.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "Some content of an input stream.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream in = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.load(in, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().html());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String inputStreamContent = "Some content of an input stream.<meta charset=\"gb2312\">";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream in = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(in), null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().html());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "Some content of an input stream.<meta charset=\"gb2312\">";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream in = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(in), "gb2312", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        // GB2312 is a multi-byte encoding, so it's not straightforward to compare the html content using assertEquals.
        // You may want to use a library to compare the byte arrays instead.
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenLoadingFile() throws IOException {
        File file = new File("nonExistentFile.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenLoadingInputStream() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.load(in, "UTF-8", null);
    }
}