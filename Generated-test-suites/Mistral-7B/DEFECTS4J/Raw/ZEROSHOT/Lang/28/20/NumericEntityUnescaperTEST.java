import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslationOfDecimalEntities() {
 CharSequence input = "This is a string with &amp;#10; &#32; &#13; &#34; &#60; &#62;";
 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
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
 unescaper.translate(input, 0, out);
 }

 assertEquals("This is a string with &amp; &#x26; & #32; & #13; & #34; & & #60; & & #62; ", input.toString());
 }

 @Test
 public void testTranslationOfHexEntities() {
 CharSequence input = "This is a string with &amp;#x41; &#x26; #x23; &#x27; &#x3C; &#x3E;";
 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
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
 unescaper.translate(input, 0, out);
 }

 assertEquals("This is a string with &amp; A & #&x26; & #&x23; & #&x27; & & & #&x3C; & & #&x3E; ", input.toString());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidDecimalEntity() {
 CharSequence input = "This is a string with &amp;#xAb;";
 unescaper.translate(input, 0, new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 // empty
 }

 @Override
 public void flush() throws IOException {
 // empty
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 });
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testInvalidEntityStart() {
 CharSequence input = "&amp;#10;This is a string";
 unescaper.translate(input, 1, new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 // empty
 }

 @Override
 public void flush() throws IOException {
 // empty
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 });
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testInvalidEntityEnd() {
 CharSequence input = "&amp;#10;This is a string&";
 unescaper.translate(input, 11, new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 // empty
 }

 @Override
 public void flush() throws IOException {
 // empty
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 });
 }
}