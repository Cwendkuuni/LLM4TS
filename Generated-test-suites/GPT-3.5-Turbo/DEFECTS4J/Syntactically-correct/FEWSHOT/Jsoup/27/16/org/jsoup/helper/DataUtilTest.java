package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

	@Test
	public void testLoad() {
		// Test case 1
		try {
			Document document = DataUtil.load(new TestInputStream(), "UTF-8", "");
			Assert.assertNotNull(document);
		} catch (IOException e) {
			Assert.fail("Exception thrown");
		}

		// Test case 2
		try {
			Document document = DataUtil.load(new TestInputStream(), "ISO-8859-1", "");
			Assert.assertNotNull(document);
		} catch (IOException e) {
			Assert.fail("Exception thrown");
		}

		// Test case 3
		try {
			Document document = DataUtil.load(new TestInputStream(), null, "");
			Assert.assertNotNull(document);
		} catch (IOException e) {
			Assert.fail("Exception thrown");
		}

		// Test case 4
		try {
			Document document = DataUtil.load(null, "UTF-8", "");
			Assert.assertNotNull(document);
		} catch (IOException e) {
			Assert.fail("Exception thrown");
		}

		// Test case 5
		try {
			Document document = DataUtil.load(null, null, "");
			Assert.assertNotNull(document);
		} catch (IOException e) {
			Assert.fail("Exception thrown");
		}
	}

	@Test
	public void testParseByteData() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(0);
		Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
		Assert.assertNotNull(document);
	}

	@Test
	public void testReadToByteBuffer() {
		// Test case 1
		try {
			InputStream inputStream = new TestInputStream();
			ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
			Assert.assertNotNull(byteBuffer);
		} catch (IOException e) {
			Assert.fail("Exception thrown");
		}

		// Test case 2
		try {
			InputStream inputStream = new TestInputStream();
			ByteBuffer byteBuffer = DataUtil.readToByteBuffer(null);
			Assert.assertNull(byteBuffer);
		} catch (IOException e) {
			Assert.fail("Exception thrown");
		}
	}

	@Test
	public void testGetCharsetFromContentType() {
		// Test case 1
		String charsetFromContentType = DataUtil.getCharsetFromContentType("text/html; charset=EUC-JP");
		Assert.assertEquals("EUC-JP", charsetFromContentType);

		// Test case 2
		String charsetFromContentType2 = DataUtil.getCharsetFromContentType(null);
		Assert.assertNull(charsetFromContentType2);
	}

	private class TestInputStream extends InputStream {
		@Override
		public int read() throws IOException {
			return 0;
		}
	}
}