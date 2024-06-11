import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_ValidNumericEntity_ReturnsCorrectCodePoint() {
 CharSequence input = "&amp; &lt; &gt; &apos; &quot; &#128; &#x1F600;";
 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 for (int i = 0; i < length; i++) {
 System.out.print((char) c);
 }
 }

 @Override
 public void flush() throws IOException {
 // empty
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 }) {
 int index = 0;
 while (index < input.length()) {
 unescaper.translate(input, index, out);
 index += unescaper.getLength();
 }

 // Verify the output
 assertEquals("&amp; &lt; &gt; &apos; &quot; &#128; &#x1F600;", "&amp; &lt; &gt; &apos; &quot; ✋ 🥵", out.toString());
 }
}

 @Test(expected = IOException.class)
 public void testTranslate_NullWriter_ThrowsIOException() {
 unescaper.translate(null, 0, null);
 }

 @Test(expected = NullPointerException.class)
 public void testTranslate_NullInput_ThrowsNullPointerException() {
 unescaper.translate(null, 0, new Writer() {
 // empty writer implementation
 });
 }

 @Test
 public void testTranslate_InvalidNumericEntity_ReturnsZero() {
 CharSequence input = "&foo; &bar;";
 int index = 0;
 int length = 0;
 while (index < input.length()) {
 length += unescaper.translate(input, index, new Writer() {
 // empty writer implementation
 });
 index += length;
 }
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_NumericEntityWithOutSemicolon_Works() {
 CharSequence input = "&amp; #123;";
 try (Writer out = new Writer() {
 // empty writer implementation
 }) {
 int index = 0;
 while (index < input.length()) {
 unescaper.translate(input, index, out);
 index += unescaper.getLength();
 }

 // Verify the output
 assertEquals("&amp;", "&amp;", out.toString());
 }
 }

 @Test
 public void testTranslate_NumericEntityWithTwoDigits_Works() {
 CharSequence input = "&#[12];";
 try (Writer out = new Writer() {
 // empty writer implementation
 }) {
 int index = 0;
 while (index < input.length()) {
 unescaper.translate(input, index, out);
 index += unescaper.getLength();
 }

 // Verify the output
 assertEquals("&#12;", "\u000b", out.toString());
 }
 }

 @Test
 public void testTranslate_NumericEntityWithLeadingZero_Works() {
 CharSequence input = "&#[012];";
 try (Writer out = new Writer() {
 // empty writer implementation
 }) {
 int index = 0;
 while (index < input.length()) {
 unescaper.translate(input, index, out);
 index += unescaper.getLength();
 }

 // Verify the output
 assertEquals("&#12;", "\u000b", out.toString());
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidHexNumericEntity_ThrowsNumberFormatException() {
 CharSequence input = "&#[xz];";
 try (Writer out = new Writer() {
 // empty writer implementation
 }) {
 int index = 0;
 while (index < input.length()) {
 unescaper.translate(input, index, out);
 index += unescaper.getLength();
 }
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidDecimalNumericEntity_ThrowsNumberFormatException() {
 CharSequence input = "&#[12x];";
 try (Writer out = new Writer() {
 // empty writer implementation
 }) {
 int index = 0;
 while (index < input.length()) {
 unescaper.translate(input, index, out);
 index += unescaper.getLength();
 }
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_NumericEntityOutOfRange_ThrowsNumberFormatException() {
 CharSequence input = "&#[256000];";
 try (Writer out = new Writer() {
 // empty writer implementation
 }) {
 int index = 0;
 while (index < input.length()) {
 unescaper.translate(input, index, out);
 index += unescaper.getLength();
 }
 }
 }
}