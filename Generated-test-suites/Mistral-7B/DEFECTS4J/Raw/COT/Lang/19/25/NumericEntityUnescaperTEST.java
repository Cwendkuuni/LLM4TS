import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_valid_numeric_entity() {
 CharSequence input = "&amp; #65; &#66; &#x41; &#xFFFC;";
 try (Writer out = new Writer() {
 @Override public void write(int c, int len) throws IOException {
 char[] chars = new char[len];
 Arrays.fill(chars, (char) c);
 super.write(chars, 0, len);
 }

 @Override public void flush() throws IOException {}

 @Override public void close() throws IOException {}
 }) {
 int actual = unescaper.translate(input, 0, out);
 String expected = "ABø";
 assertEquals(expected.length(), actual);
 String result = out.toString();
 assertEquals(expected, result);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown");
 }
 }

 @Test
 public void testTranslate_valid_numeric_entity_hex() {
 CharSequence input = "&amp; #x41; &#x42;";
 try (Writer out = new Writer() {
 //...
 }) {
 int actual = unescaper.translate(input, 0, out);
 String expected = "AB";
 assertEquals(expected.length(), actual);
 String result = out.toString();
 assertEquals(expected, result);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown");
 }
 }

 @Test
 public void testTranslate_empty_input() {
 CharSequence input = "";
 int actual = unescaper.translate(input, 0, new Writer() {
 //...
 });
 assertEquals(0, actual);
 }

 @Test
 public void testTranslate_input_without_entity() {
 CharSequence input = "abc";
 int actual = unescaper.translate(input, 0, new Writer() {
 //...
 });
 assertEquals(0, actual);
 }

 @Test(expected = IOException.class)
 public void testTranslate_input_with_non_numeric_entity() {
 CharSequence input = "&apple;";
 int actual = unescaper.translate(input, 0, new Writer() {
 //...
 });
 fail("IOException should be thrown for non-numeric entities");
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_invalid_numeric_entity() {
 CharSequence input = "&amp; #xabcd;";
 try (Writer out = new Writer() {
 //...
 }) {
 int actual = unescaper.translate(input, 0, out);
 fail("NumberFormatException should be thrown for invalid numeric entities");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslate_entity_starts_before_the_beginning() {
 CharSequence input = "ab&cd";
 try (Writer out = new Writer() {
 //...
 }) {
 int actual = unescaper.translate(input, -1, out);
 fail("IndexOutOfBoundsException should be thrown for entities starting before the beginning");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslate_entity_ends_before_the_end() {
 CharSequence input = "ab&cd";
 try (Writer out = new Writer() {
 //...
 }) {
 int actual = unescaper.translate(input, 2, out);
 fail("IndexOutOfBoundsException should be thrown for entities ending before the end");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslate_entity_contains_non_numeric_characters() {
 CharSequence input = "ab&cd%";
 try (Writer out = new Writer() {
 //...
 }) {
 int actual = unescaper.translate(input, 1, out);
 fail("IndexOutOfBoundsException should be thrown for entities containing non-numeric characters");
 } catch (IOException e) {
 // expected
 }
 }
}