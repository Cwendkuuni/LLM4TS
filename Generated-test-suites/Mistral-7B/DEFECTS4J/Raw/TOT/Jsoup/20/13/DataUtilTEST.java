package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

 private String htmlFileWithUtf8;
 private String htmlFileWithGb2312;
 private String htmlFileWithInvalidCharset;
 private String htmlFileWithNoCharset;

 @Before
 public void setUp() {
 String resourcePath = getClass().getResource("").getPath();
 htmlFileWithUtf8 = resourcePath + "/htmlFileWithUtf8.html";
 htmlFileWithGb2312 = resourcePath + "/htmlFileWithGb2312.html";
 htmlFileWithInvalidCharset = resourcePath + "/htmlFileWithInvalidCharset.html";
 htmlFileWithNoCharset = resourcePath + "/htmlFileWithNoCharset.html";
 }

 @Test
 public void testLoad_ValidFile_Utf8() throws IOException {
 Document doc = DataUtil.load(new File(htmlFileWithUtf8), "UTF-8", null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoad_ValidFile_Gb2312() throws IOException {
 Document doc = DataUtil.load(new File(htmlFileWithGb2312), null, null);
 Assert.assertNotNull(doc);

 Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
 Validate.notNull(meta);
 String charset = meta.hasAttr("http-equiv") ? getCharsetFromContentType(meta.attr("content")) : meta.attr("charset");
 Validate.notNull(charset);
 Validate.assertEquals("GB2312", charset);
 }

 @Test(expected = IOException.class)
 public void testLoad_InvalidFile() throws IOException {
 DataUtil.load(new File("invalidFile"), null, null);
 // This test should throw an IOException
 }

 @Test
 public void testLoad_ValidFileStream_Utf8() throws IOException {
 InputStream in = new FileInputStream(new File(htmlFileWithUtf8));
 Document doc = DataUtil.load(in, "UTF-8", null);
 Assert.assertNotNull(doc);
 in.close();
 }

 @Test
 public void testLoad_ValidFileStream_Gb2312() throws IOException {
 InputStream in = new FileInputStream(new File(htmlFileWithGb2312));
 Document doc = DataUtil.load(in, null, null);
 Assert.assertNotNull(doc);

 Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
 Validate.notNull(meta);
 String charset = meta.hasAttr("http-equiv") ? getCharsetFromContentType(meta.attr("content")) : meta.attr("charset");
 Validate.notNull(charset);
 Validate.assertEquals("GB2312", charset);

 in.close();
 }

 @Test
 public void testLoad_ValidFileStream_WithParser() throws IOException {
 InputStream in = new FileInputStream(new File(htmlFileWithUtf8));
 Document doc = DataUtil.load(in, "UTF-8", null, Parser.htmlParser());
 Assert.assertNotNull(doc);
 in.close();
 }

 @Test
 public void testParseByteData_Utf8() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(htmlFileWithUtf8)));
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteData_Gb2312() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(htmlFileWithGb2312)));
 String charset = getCharsetFromByteData(byteData);
 Validate.notNull(charset);
 Validate.assertEquals("GB2312", charset);

 Document doc = DataUtil.parseByteData(byteData, "GB2312", null, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteData_UnknownCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(htmlFileWithUtf8)));
 Document doc = DataUtil.parseByteData(byteData, " invalid_charset ", null, Parser.htmlParser());
 Assert.assertNull(doc);
 }

 @Test
 public void testParseByteData_NoCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(htmlFileWithNoCharset)));
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 Assert.assertNull(doc);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=EUC-JP";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Validate.notNull(charset);
 Validate.assertEquals("EUC-JP", charset);
 }

 @Test
 public void testGetCharsetFromContentType_NoCharset() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }

 private String getCharsetFromByteData(ByteBuffer byteData) {
 String docData = Charset.forName("UTF-8").decode(byteData).toString();
 Element meta = parseByteDataToDom(docData).select("meta[http-equiv=content-type], meta[charset]").first();
 return meta != null ? getCharsetFromContentType(meta.attr("content")) : null;
 }

 private Document parseByteDataToDom(String content) {
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
 return DataUtil.load(byteArrayInputStream, null, null);
 }
}