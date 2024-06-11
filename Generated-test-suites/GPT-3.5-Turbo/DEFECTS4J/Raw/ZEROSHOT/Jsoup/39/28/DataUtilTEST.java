package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
	
	private static final String CHARSET_NAME = "UTF-8";
	
	@Test
	public void testLoadFromFile() throws IOException {
		File file = new File("test.html");
		Document document = DataUtil.load(file, CHARSET_NAME, "");
		Assert.assertNotNull(document);
	}
	
	@Test
	public void testLoadFromInputStream() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("/test.html");
		Document document = DataUtil.load(inputStream, CHARSET_NAME, "");
		Assert.assertNotNull(document);
	}
	
	@Test
	public void testLoadFromInputStreamWithParser() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("/test.html");
		Document document = DataUtil.load(inputStream, CHARSET_NAME, "", Parser.htmlParser());
		Assert.assertNotNull(document);
	}
	
	@Test
	public void testParseByteData() {
		ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes());
		Document document = DataUtil.parseByteData(byteBuffer, CHARSET_NAME, "", Parser.htmlParser());
		Assert.assertNotNull(document);
	}
	
	@Test
	public void testReadToByteBufferWithMaxSize() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("/test.html");
		ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
		Assert.assertNotNull(byteBuffer);
	}
	
	@Test
	public void testReadToByteBufferWithoutMaxSize() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("/test.html");
		ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
		Assert.assertNotNull(byteBuffer);
	}
	
	@Test
	public void testGetCharsetFromContentType() {
		String contentType = "text/html; charset=UTF-8";
		String charset = DataUtil.getCharsetFromContentType(contentType);
		Assert.assertEquals("UTF-8", charset);
	}
	
	@Test
	public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
		String contentType = "text/html; charset=unsupportedCharset";
		String charset = DataUtil.getCharsetFromContentType(contentType);
		Assert.assertNull(charset);
	}
	
	@Test
	public void testGetCharsetFromContentTypeWithEmptyCharset() {
		String contentType = "text/html; charset=";
		String charset = DataUtil.getCharsetFromContentType(contentType);
		Assert.assertNull(charset);
	}
	
	@Test
	public void testGetCharsetFromContentTypeWithNullContentType() {
		String charset = DataUtil.getCharsetFromContentType(null);
		Assert.assertNull(charset);
	}

}