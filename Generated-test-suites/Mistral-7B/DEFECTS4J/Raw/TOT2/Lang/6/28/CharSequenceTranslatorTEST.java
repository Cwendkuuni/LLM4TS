package org.apache.commons.lang3.text.translate;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslatorTest;
import org.junit.Test;
import static org.junit.Assert.*;
public class CharSequenceTranslatorTest {
// Test case for basic usage
@Test
public void testTranslate() {
CharSequenceTranslator translator = new SimpleCharSequenceTranslator();
String input = "Hello, World!";
String expectedOutput = "48656c6c6f2c20576f726c6421";

String output = translator.translate(input);
assertEquals(expectedOutput, output);
}

// Test case for empty input
@Test
public void testTranslateEmptyInput() {
CharSequenceTranslator translator = new SimpleCharSequenceTranslator();
String input = "";
String expectedOutput = "";

String output = translator.translate(input);
assertEquals(expectedOutput, output);
}

// Test case for null input
@Test(expected = IllegalArgumentException.class)
public void testTranslateNullInput() {
CharSequenceTranslator translator = new SimpleCharSequenceTranslator();
String input = null;

translator.translate(input);
}

// Test case for Writer is null
@Test(expected = IllegalArgumentException.class)
public void testTranslateNullWriter() {
CharSequenceTranslator translator = new SimpleCharSequenceTranslator();
String input = "Hello, World!";
Writer out = null;

translator.translate(input, out);
}

// Test case for Writer has an IOException
@Test(expected = IOException.class)
public void testTranslateIOException() throws IOException {
CharSequenceTranslator translator = new SimpleCharSequenceTranslator();
String input = "Hello, World!";
Writer out = new StringWriter() {
@Override
public void write(int c, IOException e) throws IOException {
throw new IOException("Writer exception");
}
};

translator.translate(input, out);
}

// Test case for input contains invalid Unicode characters
@Test
public void testTranslateInvalidUnicode() {
CharSequenceTranslator translator = new SimpleCharSequenceTranslator();
String input = "\uD800\uDC00"; // Supplementary character, not a valid Unicode character
String expectedOutput = "\\uD800\\uDC00";

String output = translator.translate(input);
assertEquals(expectedOutput, output);
}
}