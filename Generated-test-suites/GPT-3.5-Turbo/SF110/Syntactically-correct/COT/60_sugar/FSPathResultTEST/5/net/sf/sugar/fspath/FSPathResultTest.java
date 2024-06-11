import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FSPathResultTest {

	@Test
	public void testGetFile() {
		File file = new File("testFile.txt");
		FSPathResult result = new FSPathResult(file);
		assertEquals(file, result.getFile());
	}

	@Test
	public void testGetString() {
		String string = "testString";
		FSPathResult result = new FSPathResult(string);
		assertEquals(string, result.getString());
	}

	@Test
	public void testGetDouble() {
		Double number = 3.14;
		FSPathResult result = new FSPathResult(number);
		assertEquals(number, result.getDouble(), 0.01);
	}

	@Test
	public void testGetDate() {
		Date date = new Date();
		FSPathResult result = new FSPathResult(date);
		assertEquals(date, result.getDate());
	}

	@Test
	public void testGetURI() {
		URI uri = URI.create("https://www.example.com");
		FSPathResult result = new FSPathResult(uri);
		assertEquals(uri, result.getURI());
	}

	@Test
	public void testGetBoolean() {
		Boolean bool = true;
		FSPathResult result = new FSPathResult(bool);
		assertEquals(bool, result.getBoolean());
	}

	@Test
	public void testToString_File() {
		File file = new File("testFile.txt");
		FSPathResult result = new FSPathResult(file);
		assertEquals(file.getAbsolutePath(), result.toString());
	}

	@Test
	public void testToString_String() {
		String string = "testString";
		FSPathResult result = new FSPathResult(string);
		assertEquals(string, result.toString());
	}

	@Test
	public void testToString_Double() {
		Double number = 3.14;
		FSPathResult result = new FSPathResult(number);
		assertEquals(number.toString(), result.toString());
	}

	@Test
	public void testToString_Boolean() {
		Boolean bool = true;
		FSPathResult result = new FSPathResult(bool);
		assertEquals(bool.toString(), result.toString());
	}

	@Test
	public void testToString_Date() {
		Date date = new Date();
		FSPathResult result = new FSPathResult(date);
		assertEquals(date.toString(), result.toString());
	}

	@Test
	public void testToString_URI() {
		URI uri = URI.create("https://www.example.com");
		FSPathResult result = new FSPathResult(uri);
		assertEquals(uri.toString(), result.toString());
	}
}