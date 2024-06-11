import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {
	@Test
	public void testLoadFile() throws IOException {
		File file = new File("test.html");
		Document doc = DataUtil.load(file, "UTF-8", "");
		assertNotNull(doc);
	}

	@Test
	public void testLoadFile_IOException() throws IOException {
		File file = new File("nonexistentfile.html");
		try {
			Document doc = DataUtil.load(file, "UTF-8", "");
			assertNull(doc); // This test will fail, but that's expected since we're testing for an IOException
		} catch (IOException e) {
			// Expected
		}
	}

	@Test
	public void testLoadInputStream() throws IOException {
		InputStream in = new ByteArrayInputStream("<html><body><h1>Hello World</h1></body></html>".getBytes());
		Document doc = DataUtil.load(in, "UTF-8", "");
		assertNotNull(doc);
	}

	// Add test cases for other methods here

	@Test
	public void testInvalidCharset() {
		InputStream in = new ByteArrayInputStream("<html><body><h1>Hello World</h1></body></html>".getBytes());
		try {
			DataUtil.load(in, "", "");
			assertFalse("An exception should have been thrown for empty charset name.", true);
		} catch (IllegalArgumentException e) {
			// Expected
		}

		try {
			DataUtil.load(in, "abc123", "");
			assertFalse("An exception should have been thrown for invalid charset name.", true);
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	// Add test cases for edge cases and exceptions here
}

//