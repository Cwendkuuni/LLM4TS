import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_valid_entities() {
 char[] expected = new char[]{'&', '#', 'x', '3', 'B', ';'};
 int length = unescaper.translate("&xi3B;", 0, new StringWriter(null).getBuffer());
 assertEquals(expected.length, length);
 assertArrayEquals(expected, new String(new StringReader(new StringWriter(new StringReader("&xi3B;")).getBuffer().toString()).readAll().toString().toCharArray()));
 }

 @Test
 public void testTranslate_valid_entities_without_semicolon() {
 char[] expected = new char[]{'&', '#', 'x', '3', 'B'};
 int length = unescaper.translate("&xi3B", 0, new StringWriter(null).getBuffer());
 assertEquals(expected.length, length);
 assertArrayEquals(expected, new String(new StringReader(new StringWriter(new StringReader("&xi3B")).getBuffer().toString()).readAll().toString().toCharArray()));
 }

 @Test
 public void testTranslate_valid_entities_decimal() {
 char[] expected = new char[]{'&', '#', '4', '1', ';'};
 int length = unescaper.translate("&#41;", 0, new StringWriter(null).getBuffer());
 assertEquals(expected.length, length);
 assertArrayEquals(expected, new String(new StringReader(new StringWriter(new StringReader("&#41;")).getBuffer().toString()).readAll().toString().toCharArray()));
 }

 @Test
 public void testTranslate_valid_entities_hexadecimal() {
 char[] expected = new char[]{'&', '#', 'x', '3', 'A', '9', ';'};
 int length = unescaper.translate("&#x3A9;", 0, new StringWriter(null).getBuffer());
 assertEquals(expected.length, length);
 assertArrayEquals(expected, new String(new StringReader(new StringWriter(new StringReader("&#x3A9;")).getBuffer().toString()).readAll().toString().toCharArray()));
 }

 @Test
 public void testTranslate_entity_out_of_range() {
 char[] expected = new char[]{'&', '#', 'x', '1', '1', '0', '0', ';'};
 int length = unescaper.translate("&xi1100;", 0, new StringWriter(null).getBuffer());
 assertEquals(expected.length, length);
 assertArrayEquals(expected, new String(new StringReader(new StringWriter(new StringReader("&xi1100;")).getBuffer().toString()).readAll().toString().toCharArray()));
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_invalid_entity() {
 int length = unescaper.translate("& invalid;", 0, new StringWriter(null).getBuffer());
 assertNull(new String(new StringReader(new StringWriter(new StringReader("& invalid;")).getBuffer().toString()).readAll().toString()));
 }

 @Test(expected = IOException.class)
 public void testTranslate_io_exception() {
 int length = unescaper.translate("&xi3A9;", 0, null);
 }

 @Test
 public void testTranslate_empty_input() {
 int length = unescaper.translate("", 0, new StringWriter(null).getBuffer());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_input_starting_with_ampersand() {
 int length = unescaper.translate("&input", 0, new StringWriter(null).getBuffer());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_input_ending_with_ampersand() {
 int length = unescaper.translate("input&", 0, new StringWriter(null).getBuffer());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_input_containing_ampersands() {
 char[] expected = new char[]{'&', 'a', 'm', 'p', '&', 'r', '&'};
 int length = unescaper.translate("am&p&r", 0, new StringWriter(null).getBuffer());
 assertEquals(expected.length, length);
 assertArrayEquals(expected, new String(new StringReader(new StringWriter(new StringReader("am&p&r")).getBuffer().toString()).readAll().toString().toCharArray()));
 }
}