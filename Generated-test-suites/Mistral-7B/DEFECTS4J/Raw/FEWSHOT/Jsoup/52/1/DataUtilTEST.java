package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<meta http-equiv='Content-Type' content='text/html; charset=UTF-32'>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        Files.write(testFile.getAbsoluteFile().toPath(), fileContent.getBytes());

        Document doc = DataUtil.load(testFile, null, null);
        assertEquals("UTF-16", doc.outputSettings().charset());

        Files.delete(testFile.getAbsoluteFile().toPath());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<meta http-equiv='Content-Type' content='text/html; charset=UTF-32'>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";

        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(testInputStream, null, null);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithCustomCharset() throws IOException {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<meta http-equiv='Content-Type' content='text/html; charset=UTF-32'>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";

        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(testInputStream, "UTF-16", null);
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithCustomCharsetAndParser() throws IOException {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<meta http-equiv='Content-Type' content='text/html; charset=UTF-32'>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";

        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(testInputStream, "UTF-16", null, Parser.xmlParser());
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws IOException {
        String docData = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<meta http-equiv='Content-Type' content='text/html; charset=UTF-32'>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputStreamContent = "Hello World";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(testInputStream, testOutputStream);
        assertEquals(inputStreamContent, testOutputStream.toString("UTF-8"));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-16";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-16", charset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-16";
        String validCharset = DataUtil.validateCharset(cs);
        assertEquals(cs, validCharset);

        cs = "";
        validCharset = DataUtil.validateCharset(cs);
        assertNull(validCharset);

        cs = "UTF-16  ";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16\n";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16\"";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16 '";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16'";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16\"";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16\";";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16;charset=UTF-16";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16; charset=UTF-16";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16; Charset=UTF-16";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16; CHARSET=UTF-16";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16; charset=UTF-16\n";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16; charset=UTF-16 \"";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16; charset=UTF-16 '";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16; charset=UTF-16'";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16; charset=UTF-16\"";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);

        cs = "UTF-16; charset=UTF-16;";
        validCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-16", validCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertTrue(mimeBoundary.length() >= 32);
        for (int i = 0; i < mimeBoundary.length(); i++) {
            assertTrue(mimeBoundary.indexOf(mimeBoundaryChars[i]) != -1);
        }
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00});
        String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16LE", charsetName);

        byteData.rewind();
        byteData.put(new byte[]{(byte) 0xFE, (byte) 0xFF, (byte) 0x00, (byte) 0x00});
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16BE", charsetName);

        byteData.rewind();
        byteData.put(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, (byte) 0x00});
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-8", charsetName);

        byteData.rewind();
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertNull(charsetName);
    }
}