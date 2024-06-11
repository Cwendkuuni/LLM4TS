import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 private Tag pTag;
 private Tag imgTag;
 private Tag scriptTag;

 @Before
 public void setup() {
 pTag = Tag.valueOf("p");
 imgTag = Tag.valueOf("img");
 scriptTag = Tag.valueOf("script");
 }

 @Test
 public void testValidatorNameNotNull() {
 Tag tag = null;
 Assert.assertFalse("Tag cannot be null", tag instanceof Tag);
 tag = new Tag("tagName");
 Assert.assertNotNull("Tag name should not be null", tag.getName());
 }

 @Test
 public void testValidatorNameEmpty() {
 Tag tag = new Tag("");
 Assert.assertFalse("Tag name should not be empty", tag instanceof Tag);
 }

 @Test
 public void testGetName() {
 assertEquals("p", pTag.getName());
 }

 @Test
 public void testGetNameNormalized() {
 Tag pTagLower = Tag.valueOf("p");
 assertEquals("p", pTagLower.getName());
 }

 @Test
 public void testValueOfUnknownTag() {
 Tag unknownTag = Tag.valueOf("unknownTag");
 assertFalse("Unknown tag should not be known", Tag.isKnownTag("unknownTag"));
 assertEquals("unknownTag", unknownTag.getName());
 }

 @Test
 public void testValueOfKnownTag() {
 assertEquals(pTag, Tag.valueOf("p"));
 }

 @Test
 public void testIsBlock() {
 assertTrue("p tag is a block tag", pTag.isBlock());
 }

 @Test
 public void testIsFormatAsBlock() {
 assertTrue("p tag should be formatted as a block", pTag.formatAsBlock());
 }

 @Test
 public void testIsData() {
 assertFalse("p tag is not a data only tag", pTag.isData());
 }

 @Test
 public void testIsEmpty() {
 assertFalse("p tag is not an empty tag", pTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 assertFalse("p tag is not a self closing tag", pTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue("p is a known tag", Tag.isKnownTag("p"));
 }

 @Test
 public void testPreserveWhitespace() {
 assertFalse("p tag does not preserve whitespace", pTag.preserveWhitespace());
 }

 @Test
 public void testIsFormListed() {
 assertFalse("p tag is not a form listed tag", pTag.isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 assertFalse("p tag is not a form submittable tag", pTag.isFormSubmittable());
 }

 @Test
 public void testSetSelfClosing() {
 Tag pTagSelfClosing = pTag.setSelfClosing();
 assertTrue("p tag should be self closing after calling setSelfClosing", pTagSelfClosing.isSelfClosing());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelfClosingInvalidTag() {
 Tag invalidTag = new Tag("invalidTag");
 IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> invalidTag.setSelfClosing());
 String expectedMessage = "Cannot call setSelfClosing on an unknown tag";
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testEqualsSameObject() {
 Tag pTagEqual = pTag;
 assertTrue("Two identical tags should be equal", pTag.equals(pTagEqual));
 }

 @Test
 public void testEqualsSameClass() {
 Tag pTagEqual2 = new Tag("p");
 assertTrue("Two identical tags should be equal", pTag.equals(pTagEqual2));
 }

 @Test
 public void testEqualsDifferentClass() {
 Tag stringTag = new String("p");
 assertFalse("Two different classes should not be equal", pTag.equals(stringTag));
 }

 @Test
 public void testEqualsDifferentTagName() {
 Tag h1Tag = Tag.valueOf("h1");
 assertFalse("Two different tag names should not be equal", pTag.equals(h1Tag));
 }

 @Test
 public void testEqualsDifferentProperties() {
 Tag pTagDifferent = new Tag("p");
 pTagDifferent.setSelfClosing(true);
 assertFalse("Two identical tags with different properties should not be equal", pTag.equals(pTagDifferent));
 }

 @Test
 public void testCanContainBlock() {
 assertTrue("p tag can contain block tags", pTag.canContainBlock());
 }

 @Test
 public void testIsInline() {
 assertFalse("p tag is not an inline tag", pTag.isInline());
 }

 @Test
 public void testIsDataOnly() {
 assertFalse("p tag is not a data only tag", pTag.isData());
 }

 @Test
 public void testIsEmptyTag() {
 assertFalse("p tag is not an empty tag", pTag.isEmpty());
 }

 @Test
 public void testIsSelfClosingTag() {
 assertFalse("p tag is not a self closing tag", pTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTagImg() {
 assertTrue("img is a known tag", Tag.isKnownTag("img"));
 }

 @Test
 public void testIsKnownTagScript() {
 assertTrue("script is a known tag", Tag.isKnownTag("script"));
 }

 @Test
 public void testIsBlockImg() {
 assertFalse("img tag is not a block tag", imgTag.isBlock());
 }

 @Test
 public void testIsFormatAsBlockImg() {
 assertFalse("img tag should not be formatted as a block", imgTag.formatAsBlock());
 }

 @Test
 public void testIsDataImg() {
 assertFalse("img tag is not a data only tag", imgTag.isData());
 }

 @Test
 public void testIsEmptyImg() {
 assertTrue("img tag is an empty tag", imgTag.isEmpty());
 }

 @Test
 public void testIsSelfClosingImg() {
 assertTrue("img tag is a self closing tag", imgTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTagScript() {
 assertTrue("script is a known tag", Tag.isKnownTag("script"));
 }

 @Test
 public void testIsBlockScript() {
 assertFalse("script tag is not a block tag", scriptTag.isBlock());
 }

 @Test
 public void testIsFormatAsBlockScript() {
 assertFalse("script tag should not be formatted as a block", scriptTag.formatAsBlock());
 }

 @Test
 public void testIsDataScript() {
 assertFalse("script tag is not a data only tag", scriptTag.isData());
 }

 @Test
 public void testIsEmptyScript() {
 assertFalse("script tag is not an empty tag", scriptTag.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagNoCanContainInline() {
 Tag emptyTag = new Tag("");
 IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> emptyTag.canContainInline);
 String expectedMessage = "Empty tag cannot have canContainInline set to true";
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testCanContainBlockScript() {
 //script tag can contain block tags, but it's not a block tag itself
 assertFalse(scriptTag.canContainBlock());
 assertTrue(scriptTag.canContainInline());
 }

 @Test
 public void testIsFormListedImg() {
 assertFalse("img tag is not a form listed tag", imgTag.isFormListed());
 }

 @Test
 public void testIsFormSubmittableImg() {
 assertFalse("img tag is not a form submittable tag", imgTag.isFormSubmittable());
 }

 @Test
 public void testIsFormListedScript() {
 assertFalse("script tag is not a form listed tag", scriptTag.isFormListed());
 }

 @Test
 public void testIsFormSubmittableScript() {
 assertFalse("script tag is not a form submittable tag", scriptTag.isFormSubmittable());
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag preTag = Tag.valueOf("pre");
 assertTrue("pre tag preserves whitespace", preTag.preserveWhitespace());
 }

 @Test
 public void testPreserveWhitespaceTagWithEmptyText() {
 Tag preTagEmpty = new Tag("pre").setEmpty(true);
 assertTrue("pre tag with empty text preserves whitespace", preTagEmpty.preserveWhitespace());
 }

 @Test
 public void testIsFormListedTextareaTag() {
 Tag textareaTag = Tag.valueOf("textarea");
 assertTrue("textarea tag is a form listed tag", textareaTag.isFormListed());
 }

 @Test
 public void testIsFormSubmittableInputTag() {
 Tag inputTag = new Tag("input").setFormSubmit(true);
 assertTrue("input tag is a form submittable tag", inputTag.isFormSubmittable());
 }
}