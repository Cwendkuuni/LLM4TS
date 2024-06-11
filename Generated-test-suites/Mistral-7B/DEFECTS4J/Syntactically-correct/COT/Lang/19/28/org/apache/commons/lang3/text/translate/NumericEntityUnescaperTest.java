import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_SimpleNumericEntity() {
 CharSequence input = "&amp;";
 int expectedLength = 1;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_NullInput() {
 CharSequence input = null;
 int expectedLength = 0;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslate_InvalidEntityStart() {
 CharSequence input = "&asdf#";
 int expectedLength = 0;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_InvalidEntityEnd() {
 CharSequence input = "&#";
 int expectedLength = 0;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testTranslate_SimpleNumericEntityHex() {
 CharSequence input = "&#x41;";
 int expectedLength = 1;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidHexValue() {
 CharSequence input = "&#xFXXX;";
 int expectedLength = 0;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidDecimalValue() {
 CharSequence input = "&#123456789012345;";
 int expectedLength = 0;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testTranslate_MultiCharacterEntity() {
 CharSequence input = "&#128515;&#x1F600;";
 int expectedLength = 2;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testTranslate_InputWithMultipleEntities() {
 CharSequence input = "&amp;&#x41;";
 int expectedLength = 2;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

}