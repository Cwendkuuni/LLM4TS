package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        // typical use case
        Assert.assertNotNull(doc);

        // edge case 1: file does not exist
        file = new File("nonexistentfile.html");
        doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNull(doc);

        // edge case 2: incorrect charset name
        charsetName = "invalidcharset";
        doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNull(doc);

        // error case: IO error
        file = new File("test.html");
        InputStream in = new ByteArrayInputStream(new byte[0]);
        doc = DataUtil.load(in, charsetName, baseUri);
        Assert.assertNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String docData = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        Parser parser = Parser.htmlParser();

        // typical use case
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);

        // edge case 1: empty byte buffer
        byteData = DataUtil.emptyByteBuffer();
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNull(doc);

        // error case 1: invalid charset
        charsetName = "invalidcharset";
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNull(doc);
    }

    @Test
    public void testCharsetDetection() throws IOException {
        String docData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        String baseUri = "http://localhost";
        Parser parser = Parser.htmlParser();

        // typical use case
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        Assert.assertNotNull(doc);

        // edge case 1: no meta tag
        docData = "<html><head></head><body></body></html>";
        byteData = ByteBuffer.wrap(docData.getBytes());
        doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        Assert.assertNotNull(doc);

        // error case: invalid charset
        docData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=invalidcharset\"></head><body></body></html>";
        byteData = ByteBuffer.wrap(docData.getBytes());
        doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        Assert.assertNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(bufferSize);
        InputStream in = new ByteArrayInputStream("Test data".getBytes());
        OutputStream out2 = new ByteArrayOutputStream(bufferSize);

        DataUtil.crossStreams(in, out);
        byte[] outData = out.toByteArray();
        byte[] inData = in.readAllBytes();
        Assert.assertArrayEquals(inData, outData);

        // edge case: different sizes
        out = new ByteArrayOutputStream();
        in = new ByteArrayInputStream("Test data".getBytes());
        out2 = new ByteArrayOutputStream(bufferSize - 1);
        DataUtil.crossStreams(in, out);
        outData = out.toByteArray();
        inData = in.readAllBytes();
        Assert.assertArrayNotEquals(inData, outData);

        // error case: IO error
        in = new ByteArrayInputStream(new byte[0]);
        outData = new byte[0];
        DataUtil.crossStreams(in, new ByteArrayOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outData[0] = (byte) b;
            }
        });
        Assert.assertArrayEquals(new byte[0], outData);
    }
}