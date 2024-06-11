import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.CharSequenceUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

 private LookupTranslator lookupTranslator;

 @Test
 public void testLookupTranslatorConstructor() {
 lookupTranslator = new LookupTranslator(new CharSequence[][]{{"A", "Z"}, {"B", "Y"}, {"C", "X"}});
 assertEquals(3, lookupTranslator.shortest);
 assertEquals(3, lookupTranslator.longest);
 }

 @Test
 public void testTranslate() {
 lookupTranslator = new LookupTranslator(new CharSequence[][]{{"A", "Z"}, {"B", "Y"}, {"C", "X"}});
 assertEquals("Z", lookupTranslator.translate(CharSequenceUtils.quotedPrintableEncode("AAA"), 0, null));
 assertEquals("Y", lookupTranslator.translate(CharSequenceUtils.quotedPrintableEncode("BBB"), 0, null));
 assertEquals("X", lookupTranslator.translate(CharSequenceUtils.quotedPrintableEncode("CCC"), 0, null));
 assertEquals("", lookupTranslator.translate(CharSequenceUtils.quotedPrintableEncode(""), 0, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNegativeIndex() {
 lookupTranslator = new LookupTranslator(new CharSequence[][]{{"A", "Z"}, {"B", "Y"}, {"C", "X"}});
 lookupTranslator.translate(CharSequenceUtils.quotedPrintableEncode("AAA"), -1, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateWithIndexOutOfBounds() {
 lookupTranslator = new LookupTranslator(new CharSequence[][]{{"A", "Z"}, {"B", "Y"}, {"C", "X"}});
 lookupTranslator.translate(CharSequenceUtils.quotedPrintableEncode("AAA"), 3, null);
 }

 @Test
 public void testTranslateWithPartialMatch() {
 lookupTranslator = new LookupTranslator(new CharSequence[][]{{"AB", "CD"}, {"EF", "GH"}, {"IJ", "KL"}});
 assertEquals("CD", lookupTranslator.translate(CharSequenceUtils.quotedPrintableEncode("ABC"), 0, null));
 assertEquals("GH", lookupTranslator.translate(CharSequenceUtils.quotedPrintableEncode("EFG"), 0, null));
 assertEquals("", lookupTranslator.translate(CharSequenceUtils.quotedPrintableEncode("IJK"), 0, null));
 }

 @Test
 public void testTranslateWithEmptyLookupMap() {
 lookupTranslator = new LookupTranslator(new CharSequence[][]{{}, {}});
 assertEquals("", lookupTranslator.translate(CharSequenceUtils.quotedPrintableEncode(""), 0, null));
 }
}