package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></head><body>Hello World</body></html>";
        File testFile = new File("testFile.html");
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataUtil.writeStringToFile(outputStream, expectedHtml, "UTF-8");
            File inputFile = new File(testFile.getAbsolutePath());
            Document doc = DataUtil.load(inputFile, "UTF-8", "");
            Assert.assertEquals(expectedHtml, doc.html());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException should not have been thrown");
        }
    }

    @Test
    public void testLoadInvalidCharset() {
        File testFile = new File("testFile.html");
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write("InvalidCharsetHeader: text/html".getBytes());
            File invalidCharsetFile = new File(testFile.getAbsolutePath());
            Assert.assertThrows(IllegalArgumentException.class, () -> DataUtil.load(invalidCharsetFile, "InvalidCharsetHeader", ""));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException should not have been thrown");
        }
    }

    @Test
    public void testLoadNullBaseUri() {
        File testFile = new File("testFile.html");
        Document doc = null;
        try {
            doc = DataUtil.load(testFile, "UTF-8", null);
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException should not have been thrown");
        }
    }

    @Test
    public void testLoadInvalidInputStream() {
        InputStream invalidInput = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Invalid Input Stream");
            }
        };
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></head><body>Hello World</body></html>";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataUtil.writeStringToFile(outputStream, expectedHtml, "UTF-8");
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            Document doc = DataUtil.load(inputStream, "UTF-8", "");
            Assert.assertEquals(expectedHtml, doc.html());
            DataUtil.load(invalidInput, "UTF-8", "");
            Assert.fail("IOException should have been thrown");
        } catch (IOException e1) {
            // expected
        }
    }

    @Test
    public void testParseByteDataDefaultCharset() {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></head><body>Hello World</body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.writeStringToFile(outputStream, expectedHtml, "UTF-8");
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        Assert.assertEquals(expectedHtml, doc.html());
    }

    @Test
    public void testParseByteDataSpecifiedCharset() {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></head><body>Hello World</body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.writeStringToFile(outputStream, expectedHtml, "UTF-8");
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        Assert.assertEquals(expectedHtml, doc.html());
    }

    @Test
    public void testParseByteDataNoCharset() {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></head><body>Hello World</body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.writeStringToFile(outputStream, expectedHtml, "UTF-8");
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        Assert.assertEquals(expectedHtml, doc.html());

        byteData.rewind();
        byteData.position(0);
        byteData.put(0, (byte) 0xFF);
        byteData.put(1, (byte) 0xFE);
        byteData.flip();

        doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        Assert.assertEquals(expectedHtml, doc.html());
    }

    @Test
    public void testParseByteDataInvalidCharset() {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></head><body>Hello World</body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.writeStringToFile(outputStream, expectedHtml, "UTF-8");
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, "invalidCharset", "", Parser.htmlParser());
        Assert.assertEquals(expectedHtml, doc.html());
    }

    @Test
    public void testParseByteDataInvalidCharset2() {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></head><body>Hello World</body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.writeStringToFile(outputStream, expectedHtml, "UTF-8");
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        Assert.assertEquals(expectedHtml, doc.html());

        byteData.rewind();
        byteData.position(0);
        byteData.put(0, (byte) 0xFF);
        byteData.put(1, (byte) 0xFE);
        byteData.flip();

        doc = DataUtil.parseByteData(byteData, "invalidCharset", "", Parser.htmlParser());
        Assert.assertEquals(expectedHtml, doc.html());
    }

    @Test
    public void testParseByteDataInvalidCharset3() {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></head><body>Hello World</body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.writeStringToFile(outputStream, expectedHtml, "UTF-8");
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, "invalidCharset", "", Parser.xmlParser());
        Assert.assertEquals(expectedHtml, doc.html());
    }

    @Test
    public void testWriteStringToFile() {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></head><body>Hello World</body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.writeStringToFile(outputStream, expectedHtml, "UTF-8");
        String actualHtml = new String(outputStream.toByteArray(), "UTF-8");
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testWriteStringToFileInvalidCharset() {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></head><body>Hello World</body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Assert.assertThrows(IllegalArgumentException.class, () -> DataUtil.writeStringToFile(outputStream, expectedHtml, "invalidCharset"));
    }

    @Test
    public void testWriteStringToFileNullInput() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Assert.assertThrows(NullPointerException.class, () -> DataUtil.writeStringToFile(outputStream, null, "UTF-8"));
    }
}