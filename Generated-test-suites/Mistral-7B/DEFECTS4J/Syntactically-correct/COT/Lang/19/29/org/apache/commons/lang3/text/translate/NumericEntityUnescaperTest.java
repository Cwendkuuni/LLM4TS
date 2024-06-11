import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_Basic() {
 CharSequence input = "&amp; &lt; &gt; &apos; &quot; &#35; &#x26; &#123; &#125;";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void write(char[] c, int offset, int length) throws IOException {
 for (int i = offset; i < offset + length; i++) {
 System.out.print(c[i]);
 }
 }
 };
 int length = unescaper.translate(input, 0, outWriter);
 assertEquals("&amp; &lt; &gt; &apos; &quot; \u0026 \u0026 \u007B \u007D", new String(outWriter.getBuffer().toString().toCharArray(), 0, length));
 }

 @Test
 public void testTranslate_EmptyInput() {
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 }

 @Override
 public void write(char[] c, int offset, int length) throws IOException {
 }
 };
 int length = unescaper.translate(new StringBuilder(), 0, outWriter);
 assertEquals(0, length);
 }

 @Test(expected = IOException.class)
 public void testTranslate_NullWriter() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(new StringBuilder(), 0, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslate_IndexOutOfBounds() {
 CharSequence input = "&amp; &lt; &gt; &apos; &quot; &#35; &#x26; &#123; &#125;";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void write(char[] c, int offset, int length) throws IOException {
 for (int i = offset; i < offset + length; i++) {
 System.out.print(c[i]);
 }
 }
 };
 int length = unescaper.translate(input, input.length(), outWriter);
 }

 @Test
 public void testTranslate_InvalidEntity() {
 CharSequence input = "&invalidEntity;";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void write(char[] c, int offset, int length) throws IOException {
 for (int i = offset; i < offset + length; i++) {
 System.out.print(c[i]);
 }
 }
 };
 int length = unescaper.translate(input, 0, outWriter);
 assertEquals("&invalidEntity;", new String(outWriter.getBuffer().toString().toCharArray(), 0, length));
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidHexCode() {
 CharSequence input = "&#[xXYZ];";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void write(char[] c, int offset, int length) throws IOException {
 for (int i = offset; i < offset + length; i++) {
 System.out.print(c[i]);
 }
 }
 };
 int length = unescaper.translate(input, 0, outWriter);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_NegativeEntityValue() {
 CharSequence input = "&#[-1];";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void write(char[] c, int offset, int length) throws IOException {
 for (int i = offset; i < offset + length; i++) {
 System.out.print(c[i]);
 }
 }
 };
 int length = unescaper.translate(input, 0, outWriter);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidEntityWithSemicolon() {
 CharSequence input = "&#[32];&";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void write(char[] c, int offset, int length) throws IOException {
 for (int i = offset; i < offset + length; i++) {
 System.out.print(c[i]);
 }
 }
 };
 int length = unescaper.translate(input, 0, outWriter);
 }

 @Test
 public void testTranslate_LargeEntityValue() {
 CharSequence input = "&#[1000000];";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void write(char[] c, int offset, int length) throws IOException {
 for (int i = offset; i < offset + length; i++) {
 System.out.print(c[i]);
 }
 }
 };
 int length = unescaper.translate(input, 0, outWriter);
 assertEquals("\uD800\uDC00", new String(outWriter.getBuffer().toString().toCharArray(), 0, length));
 }
}