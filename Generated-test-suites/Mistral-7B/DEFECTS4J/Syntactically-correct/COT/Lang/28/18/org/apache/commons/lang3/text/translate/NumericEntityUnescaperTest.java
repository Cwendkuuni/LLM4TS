import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateSimpleNumericEntity() {
 int length = unescaper.translate("&#[0123];", 0, null);
 assertEquals(5, length);
 }

 @Test
 public void testTranslateHexNumericEntity() {
 int length = unescaper.translate("&#[xAB];", 0, null);
 assertEquals(2, length);
 assertEquals((int) 0xAB, unescaper.translate("&#[xAB];", 0, null)[0]);
 }

 @Test
 public void testTranslateInvalidNumericEntity() {
 int length = unescaper.translate("&#[AB];", 0, null);
 assertEquals(0, length);
 }

 @Test(expected = IOException.class)
 public void testTranslateWithNullWriter() {
 unescaper.translate("&#[0123];", 0, null);
 }

 @Test
 public void testTranslateWithIndexGreaterThanLength() {
 int length = unescaper.translate("&#[0123];", 5, null);
 assertEquals(0, length);
 }

 @Test
 public void testTranslateWithInvalidCharacterBeforeHash() {
 int length = unescaper.translate("&@#[0123];", 0, null);
 assertEquals(1, length);
 }

 @Test
 public void testTranslateWithInvalidCharacterAfterHash() {
 int length = unescaper.translate("&#[0123];@", 0, null);
 assertEquals(2, length);
 }

 @Test
 public void testTranslateWithInvalidCharacterAfterHashButBeforeSemicolon() {
 int length = unescaper.translate("&#[0123];a", 0, null);
 assertEquals(3, length);
 }
}