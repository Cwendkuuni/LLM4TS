package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {
	
	// Helper class to verify the behavior of the translate() method
	private static class TestTranslator extends CharSequenceTranslator {
		@Override
		public int translate(CharSequence input, int index, Writer out) throws IOException {
			out.write(input.charAt(index));
			return 1;
		}
	}
	
	@Test
	public void testTranslate_inputIsNull() {
		TestTranslator translator = new TestTranslator();
		String result = translator.translate(null);
		assertNull(result);
	}
	
	@Test
	public void testTranslate_noTranslators() throws IOException {
		String input = "abcdef";
		
		TestTranslator translator = new TestTranslator();
		
		StringWriter writer = new StringWriter();
		translator.translate(input, writer);
		assertEquals(input, writer.toString());
	}
	
	@Test
	public void testTranslate_withTranslators() throws IOException {
		String input = "abcdef";
		String expectedOutput = "aceg";
		
		CharSequenceTranslator translator1 = mock(CharSequenceTranslator.class);
		CharSequenceTranslator translator2 = mock(CharSequenceTranslator.class);
		
		when(translator1.translate(any(CharSequence.class), anyInt(), any(Writer.class))).thenReturn(1);
		when(translator2.translate(any(CharSequence.class), anyInt(), any(Writer.class))).thenReturn(2);
		
		CharSequenceTranslator translator = new AggregateTranslator(translator1, translator2);
		
		StringWriter writer = new StringWriter();
		translator.translate(input, writer);
		assertEquals(expectedOutput, writer.toString());
		
		verify(translator1).translate(input, 0, writer);
		verify(translator1).translate(input, 1, writer);
		verify(translator1).translate(input, 2, writer);
		
		verify(translator2).translate(input, 3, writer);
		verify(translator2).translate(input, 5, writer);
	}
	
	@Test
	public void testWith() {
		CharSequenceTranslator translator1 = mock(CharSequenceTranslator.class);
		CharSequenceTranslator translator2 = mock(CharSequenceTranslator.class);
		CharSequenceTranslator translator3 = mock(CharSequenceTranslator.class);
		
		CharSequenceTranslator translator = new TestTranslator().with(translator1, translator2, translator3);
		assertTranslatorArray(translator, TestTranslator.class, translator1.getClass(), translator2.getClass(), translator3.getClass());
	}
	
	@Test
	public void testHex() {
		assertEquals("41", CharSequenceTranslator.hex(65));
		assertEquals("61", CharSequenceTranslator.hex(97));
		assertEquals("7A", CharSequenceTranslator.hex(122));
	}
	
	private void assertTranslatorArray(CharSequenceTranslator translator, Class<?>... classes) {
		CharSequenceTranslator[] translators = ((AggregateTranslator) translator).getTranslators();
		assertEquals(classes.length, translators.length);
		
		for (int i = 0; i < classes.length; i++) {
			assertEquals(classes[i], translators[i].getClass());
		}
	}
}