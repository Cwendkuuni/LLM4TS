package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-16LE", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-16LE"));
        Document doc = DataUtil.load(inputStream, "UTF-16LE", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("<?xml version='1.0' encoding='UTF-8'?>\n<html><head></head><body></body></html>".getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("<?xml version='1.0' encoding='UTF-16LE'?>\n<html><head></head><body></body></html>".getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, "UTF-16LE", null, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullBaseUri() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, null);
        Assert.assertNotNull(doc); // This test case should fail with an IOException, as null baseUri is not allowed
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullBaseUri() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc); // This test case should fail with an IOException, as null baseUri is not allowed
    }
}