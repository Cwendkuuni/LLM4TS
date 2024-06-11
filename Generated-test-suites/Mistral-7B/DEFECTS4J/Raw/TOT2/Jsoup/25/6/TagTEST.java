import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag unknownTag = Tag.valueOf("unknown");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());
 assertNotEquals("p", divTag.getName());
 assertNotEquals("p", unknownTag.getName());
 assertEquals(unknownTag, unknownTag); // check for new generic tag
 }

 @Test
 public void testTagProperties() {
 // Tests for basic properties
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 // isBlock
 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());

 // formatAsBlock
 assertTrue(pTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());

 // canContainBlock
 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());

 // isInline
 assertFalse(pTag.isInline());
 assertFalse(divTag.isInline());

 // isData
 assertFalse(pTag.isData());
 assertFalse(divTag.isData());

 // isEmpty
 assertFalse(pTag.isEmpty());
 assertFalse(divTag.isEmpty());

 // isSelfClosing
 assertFalse(pTag.isSelfClosing());
 assertFalse(divTag.isSelfClosing());

 // isKnownTag
 assertFalse(pTag.isKnownTag());
 assertFalse(divTag.isKnownTag());

 // edge cases
 Tag emptyTag = Tag.valueOf("meta");
 Tag scriptTag = Tag.valueOf("script");
 Tag preTag = Tag.valueOf("pre");

 // isEmpty
 assertTrue(emptyTag.isEmpty());
 assertFalse(scriptTag.isEmpty());

 // preserveWhitespace
 assertTrue(preTag.preserveWhitespace());
 assertFalse(scriptTag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfNull() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfEmpty() {
 Tag tag = Tag.valueOf("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfWhitespace() {
 Tag tag = Tag.valueOf(" ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfInvalidTagName() {
 Tag tag = Tag.valueOf("tagName1234567890_");
 }
}