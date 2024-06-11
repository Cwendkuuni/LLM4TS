import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_ValidNumericEntity() {
 char[] expected = {'&', '#', 'x', '3', 'B', ';', ' '};
 CharSequence input = "&#x3B; ";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testTranslate_ValidNumericEntity_Decimal() {
 char[] expected = {'&', '#', '4', '8', '3', ';', ' '};
 CharSequence input = "&#483; ";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testTranslate_ValidNumericEntity_HexWithSemicolon() {
 char[] expected = {'&', '#', 'X', '3', 'D', 'A', ';', ' '};
 CharSequence input = "&#x3DA; ";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testTranslate_ValidNumericEntity_HexWithoutSemicolon() {
 char[] expected = {'&', '#', 'X', '3', 'D', 'A'};
 CharSequence input = "&#x3DA";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_InvalidInput_CharacterBeforeAmpersand() {
 char[] input = {'&', 'P', '#', 'x', '3', 'B', ';', ' '};
 CharSequence cs = new String(new String(new String(new byte[1][0], 0, 1), "UTF-8")); + new String(input, 1);
 int length = unescaper.translate(cs, 0, new StringWriter(null).getBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_InvalidInput_EntityStartsInMiddleOfSequence() {
 char[] input = {'&', '#', 'x', '3', 'B', '&', ';', ' '};
 CharSequence cs = new String(new String(input, 0, input.length - 2) + new String(new byte[2][0], 0, 2));
 int length = unescaper.translate(cs, 0, new StringWriter(null).getBuffer());
 }

 @Test
 public void testTranslate_InvalidInput_InvalidCharacterAfterHash() {
 char[] input = {'&', '#', 'x', '3', 'B', 'C', ';', ' '};
 CharSequence cs = new String(new String(input, 0, input.length - 1) + new String(new byte[1][0], 0, 1));
 int length = unescaper.translate(cs, 0, new StringWriter(null).getBuffer());
 assertEquals(0, length);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidInput_InvalidNumericEntity() {
 char[] input = {'&', '#', 'X', 'M', 'Y', ';', ' '};
 CharSequence cs = new String(input);
 int length = unescaper.translate(cs, 0, new StringWriter(null).getBuffer());
 }

 @Test
 public void testTranslate_InvalidInput_EntityValueOutOfUCS2Range() {
 char[] expected = {'&', '#', 'x', '1', '0', 'F', 'F', 'F', 'F', 'F', 'F', 'F', ';', ' '};
 CharSequence input = "&#x10FFFF; ";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testTranslate_ValidInput_Empty() {
 char[] result = new char[0];
 int length = unescaper.translate(new String(), 0, new StringWriter(null).getBuffer());
 assertEquals(0, length);
 assertArrayEquals(result, new StringWriter(null).getBuffer().toString().toCharArray());
 }

 @Test
 public void testTranslate_ValidInput_EntityIsAtEnd() {
 char[] input = {"&#x3B;", " "};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(new char[]{' '}, result);
 }

 @Test
 public void testTranslate_ValidInput_EntityIsAtBeginning() {
 char[] input = {"&#x3B;", ""};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(new char[]{'&', '#', 'x', '3', 'B', ';'}, result);
 }

 @Test
 public void testTranslate_ValidInput_EntityInMiddle() {
 char[] input = {"foo&#x3B;bar"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(new char[]{'f', 'o', 'o', '&', '#', 'x', '3', 'B', ';', 'b', 'a', 'r'}, result);
 }

 @Test
 public void testTranslate_ValidInput_MultipleEntities() {
 char[] input = {"foo&amp;#xAB;bar&amp;#xCD;baz"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(new char[]{'f', 'o', 'o', '&', '&', '#', 'A', 'B', ';', 'b', 'a', 'r', '&', '&', '#', 'C', 'D', ';', 'b', 'a', 'z'}, result);
 }

 @Test
 public void testTranslate_ValidInput_MultipleEntitiesWithSemicolons() {
 char[] input = {"foo&amp;#xAB;&#xCD;bar"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(new char[]{'f', 'o', 'o', '&', '&', '#', 'A', 'B', ';', '#', 'C', 'D', ';', 'b', 'a', 'r'}, result);
 }

 @Test
 public void testTranslate_ValidInput_MixedEntitiesAndText() {
 char[] input = {"foo&amp;#xAB;bar&amp;#xCD;baz&amp;hello"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(new char[]{'f', 'o', 'o', '&', '&', '#', 'A', 'B', ';', '#', 'C', 'D', ';', 'b', 'a', 'z', '&', 'h', 'e', 'l', 'l', 'o'}, result);
 }

 @Test
 public void testTranslate_EmptyInput() {
 int length = unescaper.translate(new String(), 0, new StringWriter(null).getBuffer());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_NullInput() {
 assertThrows(NullPointerException.class, () -> unescaper.translate(null, 0, new StringWriter(null).getBuffer()));
 }

 @Test
 public void testTranslate_NullOutput() {
 assertThrows(NullPointerException.class, () -> unescaper.translate("foo".toCharArray(), 0, null));
 }

 @Test
 public void testTranslate_InvalidInput_NullInput() {
 assertThrows(NullPointerException.class, () -> unescaper.translate(null, 0, new StringWriter(null).getBuffer()));
 }

 @Test
 public void testTranslate_InvalidInput_NullOutput() {
 assertThrows(NullPointerException.class, () -> unescaper.translate("foo".toCharArray(), 0, null));
 }

 @Test
 public void testTranslate_EmptyOutput() {
 StringWriter writer = new StringWriter(null);
 int length = unescaper.translate(new String(), 0, writer);
 assertEquals(0, length);
 assertEquals("", writer.toString());
 }

 @Test
 public void testTranslate_InputWithNonPrintableCharacters() {
 char[] input = {"foo&#x0002;bar"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(new char[]{'f', 'o', 'o', '&#', '0', '0', '2', ';', 'b', 'a', 'r'}, result);
 }

 @Test
 public void testTranslate_InputWithMultipleEntities() {
 char[] input = {"foo&#x3B;bar&#x26;foo&#x3C;bar"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(new char[]{'f', 'o', 'o', '&#', 'x', '3', 'B', ';', 'b', 'a', 'r', '&', '&#', 'x', '3', 'C', ';', 'f', 'o', 'o'}, result);
 }

 @Test
 public void testTranslate_InputWithEntitiesOutsideUCS2Range() {
 char[] input = {"foo&#x110000;bar"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(new char[]{'f', 'o', 'o', '&#', 'x', '1', '1', '0', '0', '0', ';', 'b', 'a', 'r'}, result);
 }

 @Test
 public void testTranslate_InputWithEntitiesInMiddleOfSequence() {
 char[] input = {"foo&#x3B;bar&#32;baz"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(new char[]{'f', 'o', 'o', '&#', 'x', '3', 'B', ';', 'b', 'a', 'r', '\u0020', 'b', 'a', 'z'}, result);
 }

 @Test
 public void testTranslate_InputWithEntitiesAfterSequence() {
 char[] input = {"foo&#x3B;bar&amp;"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(new char[]{'f', 'o', 'o', '&#', 'x', '3', 'B', ';', 'b', 'a', 'r', '&', '&', 'a', 'm', 'p'}, result);
 }

 @Test
 public void testTranslate_InputWithEntitiesBeforeSequence() {
 char[] input = {"&amp;#x3B;foo"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 char[] result = new String(new StringWriter(null).getBuffer().toString().toCharArray(), 0, length);
 assertArrayEquals(new char[]{'&', '&', 'a', 'm', 'p', ';', 'f', 'o', 'o'}, result);
 }

 @Test
 public void testTranslate_InputWithInvalidEntity() {
 char[] input = {"&amp;#INVALID;foo"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_InputWithEntityWithInvalidDigits() {
 char[] input = {"&amp;#xABC;foo"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_InputWithEntityValueOutOfRange() {
 char[] input = {"&amp;#123456789011;foo"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_InputWithEntityValueOutOfRange_Negative() {
 char[] input = {"&amp;#-123456;foo"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_InputWithEntityValueOutOfRange_Hex() {
 char[] input = {"&amp;#x-123456;foo"};
 int length = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 assertEquals(0, length);
 }

}