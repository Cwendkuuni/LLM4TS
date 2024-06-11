
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.cli.Util;
import org.junit.Test;

public class CharSequenceTranslatorTest {
	
	@Test
	public void testTranslate() {
		CharSequenceTranslator translator = new MockTranslator();
		assertEquals("abc", translator.translate("abc"));
	}

	@Test
	public void testTranslateWithNullInput() {
		CharSequenceTranslator translator = new MockTranslator();
		assertNull(translator.translate(null));
	}

	@Test
 	public void testTranslateWithWriter() throws IOException {
		CharSequenceTranslator translator = new MockTranslator();
		StringWriter writer = new StringWriter();
		translator.translate("abc", writer);
		assertEquals("abc", writer.toString());
 	}

 	@Test(expected = IllegalArgumentException.class)
 	public void testTranslateWithNullWriter() throws IOException {
 		CharSequenceTranslator translator = new MockTranslator();
 		translator.translate("abc", null);
 	}

 	@Test
 	public void testTranslateWithNullInputAndWriter() throws IOException {
 		CharSequenceTranslator translator = new MockTranslator();
 		StringWriter writer = new StringWriter();
 		translator.translate(null, writer);
 		assertEquals("", writer.toString());
 	}

 	private static class MockTranslator extends CharSequenceTranslator {
 		@Override
 		public int translate(CharSequence input, int index, Writer out) throws IOException {
 			out.write(input.charAt(index));
 			return 1;
 		}
 	}
}
