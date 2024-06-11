import static org.junit.Assert.*;
import org.junit.Test;

public class FileTextTest {

	@Test
	public void testConstructorWithFileAndCharsetName() {
		// Test case 1: Valid file and charset name
		File file = new File("test.txt");
		String charsetName = "UTF-8";
		try {
			FileText fileText = new FileText(file, charsetName);
			assertNotNull(fileText);
			assertEquals(file, fileText.getFile());
			assertEquals(Charset.forName(charsetName), fileText.getCharset());
		} catch (IOException e) {
			fail("IOException thrown");
		}

		// Test case 2: Unsupported charset
		file = new File("test.txt");
		charsetName = "UTF-16";
		try {
			FileText fileText = new FileText(file, charsetName);
			fail("UnsupportedCharsetException not thrown");
		} catch (UnsupportedCharsetException e) {
			assertTrue(e.getMessage().contains("Unsupported charset"));
		} catch (IOException e) {
			fail("IOException thrown");
		}

		// Test case 3: Nonexistent file
		file = new File("nonexistent.txt");
		charsetName = "UTF-8";
		try {
			FileText fileText = new FileText(file, charsetName);
			fail("FileNotFoundException not thrown");
		} catch (FileNotFoundException e) {
			assertTrue(e.getMessage().contains("No such file or directory"));
		} catch (IOException e) {
			fail("IOException thrown");
		}
	}

	@Test
	public void testConstructorWithFileText() {
		File file = new File("test.txt");
		String charsetName = "UTF-8";
		FileText fileText1;
		try {
			fileText1 = new FileText(file, charsetName);
			FileText fileText2 = new FileText(fileText1);
			assertNotNull(fileText2);
			assertEquals(file, fileText2.getFile());
			assertEquals(fileText1.getCharset(), fileText2.getCharset());
			assertEquals(fileText1.getFullText(), fileText2.getFullText());
			assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
		} catch (IOException e) {
			fail("IOException thrown");
		}
	}

	@Test
	public void testFromFileText() {
		File file = new File("test.txt");
		String charsetName = "UTF-8";
		FileText fileText1;
		try {
			fileText1 = new FileText(file, charsetName);
			FileText fileText2 = FileText.fromLines(file, fileText1);
			assertNotNull(fileText2);
			assertEquals(file, fileText2.getFile());
			assertEquals(fileText1.getCharset(), fileText2.getCharset());
			assertEquals(fileText1.getFullText(), fileText2.getFullText());
			assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
		} catch (IOException e) {
			fail("IOException thrown");
		}
	}

	@Test
	public void testReadFile() {
		// Test case 1: Valid file and charset decoder
		File inputFile = new File("test.txt");
		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
		try {
			String text = FileText.readFile(inputFile, decoder);
			assertNotNull(text);
			assertEquals("This is a test file.", text);
		} catch (IOException e) {
			fail("IOException thrown");
		}

		// Test case 2: Nonexistent file
		inputFile = new File("nonexistent.txt");
		decoder = Charset.forName("UTF-8").newDecoder();
		try {
			String text = FileText.readFile(inputFile, decoder);
			fail("FileNotFoundException not thrown");
		} catch (FileNotFoundException e) {
			assertTrue(e.getMessage().contains("No such file or directory"));
		} catch (IOException e) {
			fail("IOException thrown");
		}
	}

	@Test
	public void testGetFile() {
		File file = new File("test.txt");
		String charsetName = "UTF-8";
		try {
			FileText fileText = new FileText(file, charsetName);
			assertEquals(file, fileText.getFile());
		} catch (IOException e) {
			fail("IOException thrown");
		}
	}

	@Test
	public void testGetCharset() {
		File file = new File("test.txt");
		String charsetName = "UTF-8";
		try {
			FileText fileText = new FileText(file, charsetName);
			assertEquals(Charset.forName(charsetName), fileText.getCharset());
		} catch (IOException e) {
			fail("IOException thrown");
		}
	}

	@Test
	public void testGetFullText() {
		File file = new File("test.txt");
		String charsetName = "UTF-8";
		try {
			FileText fileText = new FileText(file, charsetName);
			assertEquals("This is a test file.", fileText.getFullText());
		} catch (IOException e) {
			fail("IOException thrown");
		}
	}

	@Test
	public void testToLinesArray() {
		File file = new File("test.txt");
		String charsetName = "UTF-8";
		try {
			FileText fileText = new FileText(file, charsetName);
			String[] linesArray = {"This is a test file."};
			assertArrayEquals(linesArray, fileText.toLinesArray());
		} catch (IOException e) {
			fail("IOException thrown");
		}
	}

	@Test
	public void testFindLineBreaks() {
		File file = new File("test.txt");
		String charsetName = "UTF-8";
		try {
			FileText fileText = new FileText(file, charsetName);
			int[] lineBreakPositions = {0, 22};
			assertArrayEquals(lineBreakPositions, fileText.findLineBreaks());
		} catch (IOException e) {
			fail("IOException thrown");
		}
	}

	@Test
	public void testLineColumn() {
		File file = new File("test.txt");
		String charsetName = "UTF-8";
		try {
			FileText fileText = new FileText(file, charsetName);
			int pos = 5;
			LineColumn lineColumn = fileText.lineColumn(pos);
			assertEquals(1, lineColumn.getLineNo());
			assertEquals(pos, lineColumn.getColumnNo());
		} catch (IOException e) {
			fail("IOException thrown");
		}
	}

	@Test
	public void testGet() {
		File file = new File("test.txt");
		String charsetName = "UTF-8";
		try {
			FileText fileText = new FileText(file, charsetName);
			assertEquals("This is a test file.", fileText.get(0));
		} catch (IOException e) {
			fail("IOException thrown");
		}
	}

	@Test
	public void testSize() {
		File file = new File("test.txt");
		String charsetName = "UTF-8";
		try {
			FileText fileText = new FileText(file, charsetName);
			assertEquals(1, fileText.size());
		} catch (IOException e) {
			fail("IOException thrown");
		}
	}

}