package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello, world!</p>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document document = DataUtil.load(testFile, "UTF-8", null);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello, world!", document.body().text());

        testFile.delete(); // clean up after test
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello, world!</p>\n</body>\n</html>";
        byte[] inputData = fileContent.getBytes(StandardCharsets.UTF_8);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        Document document = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello, world!", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello, world!</p>\n</body>\n</html>";
        byte[] inputData = fileContent.getBytes(StandardCharsets.UTF_8);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        Document document = DataUtil.load(inputStream, "ISO-8859-1", null);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello, world!", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String fileContent = "<xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello, world!</p>\n</body>\n</html>";
        byte[] inputData = fileContent.getBytes(StandardCharsets.UTF_8);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        Document document = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello, world!", document.body().text());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullFile() {
        Document document = DataUtil.load(null, null, null);
        // Test should fail with IOException
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInputStream() {
        Document document = DataUtil.load(null, null, null);
        // Test should fail with IOException
    }
}