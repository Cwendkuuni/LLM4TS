import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_DecimalEntity() throws Exception {
 CharSequence input = "&amp;#123;";
 Writer output = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected 3 characters to be written: 123\n", "123", output.toString());
 assertEquals("Expected length of 3\n", 3, length);
 }

 @Test
 public void testTranslate_HexEntity() throws Exception {
 CharSequence input = "&amp;#x7B;";
 Writer output = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected 1 character to be written: { \n", "{", output.toString());
 assertEquals("Expected length of 3\n", 3, length);
 }

 @Test
 public void testTranslate_InvalidDecimalEntity() throws Exception {
 CharSequence input = "&amp;#a;";
 Writer output = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected 0 characters to be written and length of 1\n", "", output.toString());
 assertEquals("Expected length of 1\n", 1, length);
 }

 @Test
 public void testTranslate_InvalidHexEntity() throws Exception {
 CharSequence input = "&amp;#xz;";
 Writer output = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected 0 characters to be written and length of 2\n", "", output.toString());
 assertEquals("Expected length of 2\n", 2, length);
 }

 @Test
 public void testTranslate_EmptyEntity() throws Exception {
 CharSequence input = "&amp;#;";
 Writer output = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected 0 characters to be written and length of 1\n", "", output.toString());
 assertEquals("Expected length of 1\n", 1, length);
 }

 @Test
 public void testTranslate_NonEntityCharacter() throws Exception {
 CharSequence input = "abc&def";
 Writer output = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected 5 characters to be written: abcdef\n", "abcdef", output.toString());
 assertEquals("Expected length of 6\n", 6, length);
 }

 @Test
 public void testTranslate_MultipleEntities() throws Exception {
 CharSequence input = "&amp;#123;&amp;#x7B;&amp;#123;&amp;#77;";
 Writer output = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected 5 characters to be written: 123{123}W\n", "123{123}W", output.toString());
 assertEquals("Expected length of 11\n", 11, length);
 }

 @Test
 public void testTranslate_InputWithLeadingAmpersand() throws Exception {
 CharSequence input = "&amp;#123;abcd";
 Writer output = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected 5 characters to be written: 123abcd\n", "123abcd", output.toString());
 assertEquals("Expected length of 6\n", 6, length);
 }

 @Test
 public void testTranslate_InputWithTrailingAmpersand() throws Exception {
 CharSequence input = "abcd&amp;#123;";
 Writer output = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected 5 characters to be written: abcd123\n", "abcd123", output.toString());
 assertEquals("Expected length of 6\n", 6, length);
 }

 @Test
 public void testTranslate_InputWithMultipleEntitiesAndTrailingAmpersand() throws Exception {
 CharSequence input = "abcd&amp;#123;&amp;#x7B;&amp;#123;&amp;#77;&amp;#;";
 Writer output = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected 6 characters to be written: abcd123{123}W\n", "abcd123{123}W", output.toString());
 assertEquals("Expected length of 13\n", 13, length);
 }

 @Test
 public void testTranslate_InputWithMultipleEntitiesAndLeadingAmpersand() throws Exception {
 CharSequence input = "&amp;abcd&amp;#123;&amp;#x7B;&amp;#123;&amp;#77;&amp;#;";
 Writer output = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected 6 characters to be written: &abcd123{123}W\n", "&abcd123{123}W", output.toString());
 assertEquals("Expected length of 14\n", 14, length);
 }

 @Test
 public void testTranslate_InputWithInvalidEntities() throws Exception {
 CharSequence input = "abcd&amp;#a;&amp;#xz;&amp;#;";
 Writer output = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }
 };
 int length = unescaper.translate(input, 0, output);
 assertEquals("Expected 5 characters to be written: abcd\n", "abcd", output.toString());
 assertEquals("Expected length of 5\n", 5, length);
 }
}