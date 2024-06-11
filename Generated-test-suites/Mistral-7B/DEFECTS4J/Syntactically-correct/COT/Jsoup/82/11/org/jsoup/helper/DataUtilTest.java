package org.jsoup.helper;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.InputStreamReader;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("sample.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamNull() throws IOException {
        InputStream in = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            DataUtil.load(in, charsetName, baseUri);
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testLoadInputStreamEmpty() throws IOException {
        InputStream in = new FileInputStream(new File(""));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
        in.close();
    }

    @Test
    public void testLoadInputStreamInvalidCharset() throws IOException {
        InputStream in = new FileInputStream(new File("sample.html"));
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        try {
            DataUtil.load(in, charsetName, baseUri);
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        } finally {
            in.close();
        }
    }

    @Test
    public void testLoadInputStreamCorrectCharset() throws IOException {
        InputStream in = new FileInputStream(new File("sample.html"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
        in.close();
    }

    @Test
    public void testLoadInputStreamWithMetaCharset() throws IOException {
        InputStream in = new FileInputStream(new File("sample_meta_charset.html"));
        String charsetName = "UTF-8"; // default charset
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        String expectedCharset = "ISO-8859-1";
        assertEquals(expectedCharset, doc.charset());
        in.close();
    }

    @Test
    public void testLoadInputStreamWithXmlDeclarationCharset() throws IOException {
        InputStream in = new FileInputStream(new File("sample_xml_charset.xml"));
        String charsetName = "UTF-8"; // default charset
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        String expectedCharset = "ISO-8859-1";
        assertEquals(expectedCharset, doc.charset());
        in.close();
    }

    @Test
    public void testParseInputStreamNull() throws IOException {
        InputStream in = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            DataUtil.parseInputStream(in, charsetName, baseUri, parser);
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testParseInputStreamEmpty() throws IOException {
        InputStream in = new FileInputStream(new File(""));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInputStream(in, charsetName, baseUri, parser);
        assertNotNull(doc);
        in.close();
    }

    @Test
    public void testParseInputStreamInvalidCharset() throws IOException {
        InputStream in = new FileInputStream(new File("sample.html"));
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            DataUtil.parseInputStream(in, charsetName, baseUri, parser);
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        } finally {
            in.close();
        }
    }

    @Test
    public void testParseInputStreamCorrectCharset() throws IOException {
        InputStream in = new FileInputStream(new File("sample.html"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInputStream(in, charsetName, baseUri, parser);
        assertNotNull(doc);
        in.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        InputStream input = new FileInputStream(inputFile);
        OutputStream output = new FileOutputStream(outputFile);
        try {
            DataUtil.crossStreams(input, output);
            // other checks, e.g. verify output file contains expected data
        } finally {
            input.close();
            output.close();
        }
    }
}