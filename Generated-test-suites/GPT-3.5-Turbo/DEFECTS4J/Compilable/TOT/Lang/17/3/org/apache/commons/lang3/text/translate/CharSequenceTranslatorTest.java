package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CharSequenceTranslatorTest {

	@Test
	public void testTranslate_Index_Writer() throws IOException {
		CharSequenceTranslator translator = new CharSequenceTranslator() {
			@Override
			public int translate(CharSequence input, int index, Writer out) throws IOException {
				// Custom translation implementation
				return 0;
			}
		};

		String input = "test";
		String expectedOutput = "";

		StringWriter writer = new StringWriter();
		int consumed = translator.translate(input, 0, writer);
		String actualOutput = writer.toString();

		Assert.assertEquals(expectedOutput, actualOutput);
		Assert.assertEquals(input.length(), consumed);
	}

	@Test
	public void testTranslate_Input_Null() {
		CharSequenceTranslator translator = new CharSequenceTranslator() {
			@Override
			public int translate(CharSequence input, int index, Writer out) throws IOException {
				// Custom translation implementation
				return 0;
			}
		};

		String input = null;

		String actualOutput = translator.translate(input);

		Assert.assertNull(actualOutput);
	}

	@Test
	public void testTranslate_Input_NonNull() {
		CharSequenceTranslator translator = new CharSequenceTranslator() {
			@Override
			public int translate(CharSequence input, int index, Writer out) throws IOException {
				// Custom translation implementation
				return 0;
			}
		};

		String input = "test";
		String expectedOutput = "";

		String actualOutput = translator.translate(input);

		Assert.assertEquals(expectedOutput, actualOutput);
	}

	@Test
	public void testTranslate_Input_Empty() {
		CharSequenceTranslator translator = new CharSequenceTranslator() {
			@Override
			public int translate(CharSequence input, int index, Writer out) throws IOException {
				// Custom translation implementation
				return 0;
			}
		};

		String input = "";
		String expectedOutput = "";

		String actualOutput = translator.translate(input);

		Assert.assertEquals(expectedOutput, actualOutput);
	}

	@Test
	public void testTranslate_NullWriter() {
		CharSequenceTranslator translator = new CharSequenceTranslator() {
			@Override
			public int translate(CharSequence input, int index, Writer out) throws IOException {
				// Custom translation implementation
				return 0;
			}
		};

		String input = "test";
		Writer writer = null;

		try {
			translator.translate(input, writer);
			Assert.fail("Expected IllegalArgumentException was not thrown.");
		} catch (IllegalArgumentException e) {
			// Expected exception was thrown
		} catch (Exception e) {
			Assert.fail("Unexpected exception: " + e.getMessage());
		}
	}

	@Test
	public void testTranslate_Input_NullWriter() {
		CharSequenceTranslator translator = new CharSequenceTranslator() {
			@Override
			public int translate(CharSequence input, int index, Writer out) throws IOException {
				// Custom translation implementation
				return 0;
			}
		};

		String input = null;
		Writer writer = new StringWriter();

		try {
			translator.translate(input, writer);
			Assert.fail("Expected IllegalArgumentException was not thrown.");
		} catch (IllegalArgumentException e) {
			// Expected exception was thrown
		} catch (Exception e) {
			Assert.fail("Unexpected exception: " + e.getMessage());
		}
	}

	@Test
	public void testTranslate_Input_ValidWriter_IOException() {
		CharSequenceTranslator translator = new CharSequenceTranslator() {
			@Override
			public int translate(CharSequence input, int index, Writer out) throws IOException {
				throw new IOException("Test IOException");
			}
		};

		String input = "test";
		Writer writer = new StringWriter();

		try {
			translator.translate(input, writer);
			Assert.fail("Expected IOException was not thrown.");
		} catch (IOException e) {
			// Expected exception was thrown
		} catch (Exception e) {
			Assert.fail("Unexpected exception: " + e.getMessage());
		}
	}

	@Test
	public void testWith() {
		CharSequenceTranslator translator1 = new CharSequenceTranslator() {
			@Override
			public int translate(CharSequence input, int index, Writer out) throws IOException {
				// Custom translation implementation
				return 0;
			}
		};

		CharSequenceTranslator translator2 = new CharSequenceTranslator() {
			@Override
			public int translate(CharSequence input, int index, Writer out) throws IOException {
				// Custom translation implementation
				return 0;
			}
		};

		CharSequenceTranslator mergedTranslator = translator1.with(translator2);

		Assert.assertNotNull(mergedTranslator);
		Assert.assertTrue(mergedTranslator instanceof AggregateTranslator);
	}

	@Test
	public void testHex() {
		int codepoint = 10;
		String expectedHex = "A";

		String actualHex = CharSequenceTranslator.hex(codepoint);

		Assert.assertEquals(expectedHex, actualHex);
	}

}