package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil methods.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        File inputFile = new File("test.html");
        FileUtils.writeStringToFile(inputFile, fileContent, "UTF-8");

        Document expectedDocument = new Parser().parseInput(fileContent, null);
        Document actualDocument = DataUtil.load(inputFile, "UTF-8", null);

        Assert.assertEquals(expectedDocument, actualDocument);
        FileUtils.deleteQuietly(inputFile);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document expectedDocument = new Parser().parseInput(inputStreamContent, null);
        Document actualDocument = DataUtil.load(inputStream, "UTF-8", null);

        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document expectedDocument = new Parser().xmlParser().parseInput(inputStreamContent, null);
        Document actualDocument = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());

        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        File inputFile = new File("test.html");
        FileUtils.writeStringToFile(inputFile, fileContent, "UTF-8");

        DataUtil.load(inputFile, "GBK", null);
        FileUtils.deleteQuietly(inputFile);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        DataUtil.load(inputStream, "GBK", null);
    }
}