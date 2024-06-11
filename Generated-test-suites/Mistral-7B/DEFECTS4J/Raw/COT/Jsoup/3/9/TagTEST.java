import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 private Tag pTag;
 private Tag divTag;
 private Tag h1Tag;
 private Tag unknownTag;
 private Tag formTag;

 @Test
 public void testValueOf() {
 pTag = Tag.valueOf("p");
 divTag = Tag.valueOf("div");
 h1Tag = Tag.valueOf("H1");

 Assert.assertEquals("p", pTag.getName());
 Assert.assertEquals("div", divTag.getName());
 Assert.assertEquals("H1", h1Tag.getName());

 // test with empty and null input
 Assert.assertNull(Tag.valueOf(""));
 Assert.assertNull(Tag.valueOf(null));
 }

 @Test
 public void testCanContain() {
 pTag = Tag.valueOf("p");
 divTag = Tag.valueOf("div");

 Tag childP = Tag.valueOf("p");
 Tag childDiv = Tag.valueOf("div");
 Tag childSpan = Tag.valueOf("span");

 Assert.assertTrue(pTag.canContain(childP));
 Assert.assertTrue(pTag.canContain(childDiv));
 Assert.assertTrue(pTag.canContain(childSpan));

 // test with same tag
 Assert.assertFalse(pTag.canContain(pTag));

 // test with different tags
 Assert.assertTrue(divTag.canContain(childP));
 Assert.assertTrue(divTag.canContain(childDiv));
 Assert.assertTrue(divTag.canContain(childSpan));

 // test with self-closing tag
 Assert.assertFalse(pTag.canContain(pTag));

 // test with empty tag
 Tag emptyTag = Tag.valueOf("img");
 Assert.assertFalse(pTag.canContain(emptyTag));

 // test with data only tag
 Tag scriptTag = Tag.valueOf("script");
 Assert.assertFalse(pTag.canContain(scriptTag));
 }

 @Test
 public void testIsBlockAndIsInline() {
 pTag = Tag.valueOf("p");
 divTag = Tag.valueOf("div");

 Assert.assertTrue(pTag.isBlock());
 Assert.assertFalse(pTag.isInline());

 Assert.assertTrue(divTag.isBlock());
 Assert.assertFalse(divTag.isInline());
 }

 @Test
 public void testIsData() {
 scriptTag = Tag.valueOf("script");

 Assert.assertTrue(scriptTag.isData());
 }

 @Test
 public void testIsEmpty() {
 emptyTag = Tag.valueOf("img");

 Assert.assertTrue(emptyTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 preTag = Tag.valueOf("pre");

 Assert.assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 pTag = Tag.valueOf("p");
 divTag = Tag.valueOf("div");
 bodyTag = Tag.valueOf("body");

 pTag.setAncestor(bodyTag.getName());
 divTag.setAncestor(bodyTag.getName());

 Assert.assertEquals("body", pTag.getImplicitParent().getName());
 Assert.assertEquals("body", divTag.getImplicitParent().getName());
 }

 @Test
 public void testValidateParent() {
 pTag = Tag.valueOf("p");
 divTag = Tag.valueOf("div");
 headTag = Tag.valueOf("head");
 scriptTag = Tag.valueOf("script");

 Assert.assertTrue(pTag.isValidParent(scriptTag));
 Assert.assertTrue(headTag.isValidParent(scriptTag));
 Assert.assertFalse(pTag.isValidParent(pTag));
 Assert.assertFalse(pTag.isValidParent(divTag));
 }

 @Test
 public void testToString() {
 pTag = Tag.valueOf("p");
 divTag = Tag.valueOf("div");

 Assert.assertEquals("p", pTag.toString());
 Assert.assertEquals("div", divTag.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainNullTag() {
 pTag = Tag.valueOf("p");

 pTag.canContain(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAncestorNull() {
 pTag = Tag.valueOf("p");

 pTag.setAncestor(null);
 }

 @Test
 public void testRegisterTags() {
 // Test that all tags are registered
 List<String> allTags = new ArrayList<>();

 synchronized (Tag.tags) {
 for (Tag tag : Tag.tags.values()) {
 allTags.add(tag.tagName);
 }
 }

 List<String> expectedTags = Arrays.asList("HTML", "HEAD", "BODY", "FRAMESET", "P", "H1", "H2", "H3", "H4", "H5", "H6", "UL", "OL", "PRE", "DIV", "BLOCKQUOTE", "HR", "ADDRESS", "FRAME", "NOFRAMES", "FONT", "TT", "I", "B", "BIG", "SMALL", "EM", "STRONG", "DFN", "CODE", "SAMP", "KBD", "VAR", "CITE", "ABBR", "ACRONYM", "A", "IMG", "BR", "MAP", "Q", "SUB", "SUP", "SPAN", "BDO", "UL", "LI", "DL", "DT", "DD", "TR", "TH", "TD", "TABLE", "CAPTION", "THEAD", "TFOOT", "TBODY", "COLGROUP", "COL", "INS", "DEL");

 Assert.assertEquals(expectedTags, allTags);
 }

 @Test
 public void testUnknownTag() {
 unknownTag = new Tag("UNKNOWN");

 Assert.assertFalse(unknownTag.isBlock());
 Assert.assertFalse(unknownTag.isInline());
 Assert.assertFalse(unknownTag.isEmpty());
 Assert.assertFalse(unknownTag.preserveWhitespace());
 Assert.assertFalse(unknownTag.canContainBlock());
 Assert.assertFalse(unknownTag.canContainInline());
 Assert.assertEquals("UNKNOWN", unknownTag.getName());
 }

 @Test
 public void testFormTag() {
 formTag = Tag.valueOf("form");

 Assert.assertTrue(formTag.isBlock());
 Assert.assertTrue(formTag.canContainInline());
 Assert.assertTrue(formTag.canContainBlock());
 Assert.assertFalse(formTag.isEmpty());
 Assert.assertFalse(formTag.preserveWhitespace());
 Assert.assertTrue(formTag.optionalClosing());
 }
}