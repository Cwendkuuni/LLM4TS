package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String baseUri = "http://example.com/";
        File file = new File("path/to/valid/file.html");
        String charsetName = "UTF-8";

        Document document = DataUtil.load(file, charsetName, baseUri);
        // assertions on document
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String baseUri = "http://example.com/";
        File file = new File("path/to/invalid/file.html");
        String charsetName = "GBK"; // invalid charset

        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String baseUri = "http://example.com/";
        String inputData = "<html><head><meta charset=\"UTF-8\"></meta></head><body>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        // assertions on document
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String baseUri = "http://example.com/";
        String inputData = "<html><head><meta charset=\"GBK\"></meta></head><body>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        Document document = DataUtil.load(inputStream, "GBK", baseUri);
        // assertions on document
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        String baseUri = "http://example.com/";
        String inputData = "<html><head></head><body>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());

        Document document = DataUtil.load(inputStream, null, baseUri);
        // assertions on document

        Document documentWithoutBaseUri = DataUtil.load(inputStream);
        // assertions on document
    }

    @Test
    public void testLoadInputStreamWithBOM() throws IOException {
        String baseUri = "http://example.com/";
        String inputData = "\uFEFF<html><head><meta charset=\"UTF-8\"></meta></head><body>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.load(inputStream, null, baseUri);
        // assertions on document

        Document documentWithoutBaseUri = DataUtil.load(inputStream);
        // assertions on document

        Document documentWithDetectedCharset = DataUtil.load(inputStream);
        // assertions on document
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String baseUri = "http://example.com/";
        String inputData = "<html><head><meta charset=\"UTF-8\"></meta></head><body>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        // assertions on document
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String baseUri = "http://example.com/";
        String inputData = "<html><head><meta charset=\"GBK\"></meta></head><body>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";

        try {
            Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        Document documentWithDetectedCharset = DataUtil.parseInputStream(inputStream, null, baseUri, DataUtil.Parser.htmlParser());
        // assertions on document
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String baseUri = "http://example.com/";
        String inputData = "<html><head></head><body>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());

        Document document = DataUtil.parseInputStream(inputStream, null, baseUri, DataUtil.Parser.htmlParser());
        // assertions on document

        Document documentWithoutBaseUri = DataUtil.parseInputStream(inputStream, DataUtil.Parser.htmlParser());
        // assertions on document
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String baseUri = "http://example.com/";
        String inputData = "\uFEFF<html><head><meta charset=\"UTF-8\"></meta></head><body>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.parseInputStream(inputStream, null, baseUri, DataUtil.Parser.htmlParser());
        // assertions on document

        Document documentWithoutBaseUri = DataUtil.parseInputStream(inputStream, DataUtil.Parser.htmlParser());
        // assertions on document

        Document documentWithDetectedCharset = DataUtil.parseInputStream(inputStream, null, baseUri, DataUtil.Parser.htmlParser());
        // assertions on document
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "input data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputData = outputStream.toByteArray();
        String outputString = new String(outputData, Charset.forName("UTF-8"));

        // assertions on outputString
    }
}