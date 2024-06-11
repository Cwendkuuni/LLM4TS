import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_Simple() {
 CharSequence sequence = "&amp;";
 int length = unescaper.translate(sequence, 0, null);
 assertEquals("&amp;", sequence.subSequence(0, length));
 }

 @Test
 public void testTranslate_SimpleHex() {
 CharSequence sequence = "&#xff;";
 int length = unescaper.translate(sequence, 0, null);
 assertEquals("\u00ff", sequence.subSequence(0, length).toString());
 }

 @Test
 public void testTranslate_SimpleDec() {
 CharSequence sequence = "&#123;";
 int length = unescaper.translate(sequence, 0, null);
 assertEquals("\u007b", sequence.subSequence(0, length).toString());
 }

 @Test
 public void testTranslate_Complex() {
 CharSequence sequence = "&#x6162;abc&amp;def&#13;";
 int length = unescaper.translate(sequence, 0, null);
 assertEquals("\u0061\u0062abc&amp;def\r", sequence.subSequence(0, length).toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_InvalidEntity() {
 CharSequence sequence = "&%";
 int length = unescaper.translate(sequence, 0, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslate_InvalidIndex() {
 CharSequence sequence = "&amp;";
 int length = unescaper.translate(sequence, 3, null);
 }

 @Test(expected = NullPointerException.class)
 public void testTranslate_NullWriter() {
 CharSequence sequence = "&amp;";
 int length = unescaper.translate(sequence, 0, null);
 }

 @Test
 public void testTranslate_EmptyInput() {
 CharSequence sequence = "";
 int length = unescaper.translate(sequence, 0, null);
 assertEquals("", sequence.subSequence(0, length));
 }

 @Test
 public void testTranslate_NoEntity() {
 CharSequence sequence = "abcdef";
 int length = unescaper.translate(sequence, 0, null);
 assertEquals("abcdef", sequence.subSequence(0, length));
 }
}