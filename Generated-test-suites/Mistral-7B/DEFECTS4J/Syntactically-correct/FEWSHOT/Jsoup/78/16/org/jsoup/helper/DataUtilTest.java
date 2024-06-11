package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expected = "Hello World";
        String charset = "UTF-8";
        File file = new File("test.html");
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(file);
        writer.write(expected);
        writer.close();

        Document doc = DataUtil.load(file, charset, null);
        assertEquals(expected, doc.body().text());
        doc.outputSettings().charset(charset);

        DataUtil.load(file, null, null); // null charset
        DataUtil.load(file, "invalid", null); // invalid charset
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String expected = "Hello World";
        String charset = "UTF-8";
        InputStream input = new ByteArrayInputStream(expected.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(input, charset, null);
        assertEquals(expected, doc.body().text());
        doc.outputSettings().charset(charset);

        DataUtil.load(input, null, null); // null charset
        DataUtil.load(input, "invalid", null); // invalid charset
    }

    @Test
    public void testParseInputStream() throws IOException {
        String expected = "Hello World";
        String charset = "UTF-8";
        String inputData = expected;

        ByteArrayInputStream input = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, charset, null, Parser.htmlParser());
        assertEquals(expected, doc.body().text());
        doc.outputSettings().charset(charset);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        DataUtil.crossStreams(input, output);
        input.close();
        String outputText = output.toString(StandardCharsets.UTF_8.name());
        Document docFromOutput = DataUtil.parseInputStream(new InputStreamReader(new ByteArrayInputStream(output.toByteArray())), null, null, Parser.htmlParser());
        assertEquals(expected, docFromOutput.body().text());

        DataUtil.parseInputStream(null, charset, null, Parser.htmlParser()); // null input
        DataUtil.parseInputStream(input, null, null, Parser.htmlParser()); // null charset
        DataUtil.parseInputStream(input, "invalid", null, Parser.htmlParser()); // invalid charset
        DataUtil.parseInputStream(input, charset, null, null); // no parser

        InputStream invalidInput = new ByteArrayInputStream(new byte[0]);
        assertThrows(IOException.class, () -> DataUtil.parseInputStream(invalidInput, charset, null, Parser.htmlParser()));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String expected = "Hello World";
        String charset = "UTF-8";
        String inputData = expected;

        ByteArrayInputStream input = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input);
        byte[] expectedBytes = inputData.getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expectedBytes, byteBuffer.array());
        input.close();

        byteBuffer = DataUtil.readToByteBuffer(input, 0);
        assertArrayEquals(expectedBytes, byteBuffer.array());

        byteBuffer = DataUtil.readToByteBuffer(input, 100);
        assertArrayEquals(expectedBytes, byteBuffer.array());

        byteBuffer = DataUtil.readToByteBuffer(null, 0);
        assertNull(byteBuffer);

        byteBuffer = DataUtil.readToByteBuffer(input, 1000000);
        assertArrayEquals(expectedBytes, byteBuffer.array());
    }

    @Test
    public void testDetectCharsetFromBom() {
        String utf8Bom = "\uFEFFHello World";
        String utf16Lebom = "\uFFFE\u0001Hello World";
        String utf32Lebom = "\u0000\u0000\u0000\u0000\u0000\u0000Hello World";
        String noBom = "Hello World";

        ByteBuffer utf8BomBuffer = ByteBuffer.wrap(utf8Bom.getBytes(StandardCharsets.UTF_8));
        ByteBuffer utf16LebomBuffer = ByteBuffer.wrap(utf16Lebom.getBytes(StandardCharsets.UTF_16LE));
        ByteBuffer utf32LebomBuffer = ByteBuffer.wrap(utf32Lebom.getBytes(StandardCharsets.UTF_32LE));
        ByteBuffer noBomBuffer = ByteBuffer.wrap(noBom.getBytes(StandardCharsets.UTF_8));

        BomCharset utf8BomResult = DataUtil.detectCharsetFromBom(utf8BomBuffer);
        assertEquals("UTF-8", utf8BomResult.charset);
        assertEquals(0, utf8BomResult.offset);

        BomCharset utf16LebomResult = DataUtil.detectCharsetFromBom(utf16LebomBuffer);
        assertEquals("UTF-16LE", utf16LebomResult.charset);
        assertEquals(0, utf16LebomResult.offset);

        BomCharset utf32LebomResult = DataUtil.detectCharsetFromBom(utf32LebomBuffer);
        assertEquals("UTF-32LE", utf32LebomResult.charset);
        assertEquals(0, utf32LebomResult.offset);

        BomCharset noBomResult = DataUtil.detectCharsetFromBom(noBomBuffer);
        assertNull(noBomResult);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String csInvalid = "UTF-8\u0001"; // invalid char in charset name

        String csUpper = "UTF-8".toUpperCase();
        String csUpperInvalid = "UTF-8\u0001".toUpperCase();

        assertEquals(cs, DataUtil.validateCharset(cs));
        assertEquals(cs, DataUtil.validateCharset(csUpper));
        assertNull(DataUtil.validateCharset(csInvalid));
        assertNull(DataUtil.validateCharset(csUpperInvalid));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String contentTypeNoCharset = "text/html";

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
        assertNull(DataUtil.getCharsetFromContentType(contentTypeNoCharset));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertTrue(mimeBoundary.length() == 32);
        for (int i = 0; i < mimeBoundary.length(); i++) {
            assertTrue(Character.isAlphabetic(mimeBoundary.charAt(i)) || mimeBoundary.charAt(i) == '-');
        }
    }
}