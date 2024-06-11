import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

 // Test case for valueOf() method
 @Test
 public void testValueOf() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 Tag unknownTag = Tag.valueOf("unknown");
 assertNotEquals("p", unknownTag.getName());
 }

 // Test case for canContain() method
 @Test
 public void testCanContain() {
 Tag parentTag = new Tag("div");
 Tag childTag1 = new Tag("p");
 Tag childTag2 = new Tag("div");

 // Test case for block tag containing inline tag
 parentTag.canContain(childTag1);
 assertTrue(parentTag.canContain(childTag1));

 // Test case for inline tag containing inline tag
 childTag1.canContain(childTag2);
 assertFalse(childTag1.canContain(childTag2));

 // Test case for parent tag containing itself
 parentTag.canContain(parentTag);
 assertFalse(parentTag.canContain(parentTag));

 // Test case for empty tag
 Tag emptyTag = new Tag("img");
 emptyTag.setEmpty();
 assertTrue(emptyTag.isEmpty());
 assertFalse(emptyTag.canContain(childTag1));

 // Test case for data only tag
 Tag dataOnlyTag = new Tag("script");
 dataOnlyTag.setContainDataOnly();
 assertTrue(dataOnlyTag.isData());
 assertFalse(dataOnlyTag.canContainInline());

 // Test case for head tag
 Tag headTag = new Tag("head");
 assertFalse(headTag.canContain(childTag1));
 assertFalse(headTag.canContain(childTag2));
 assertTrue(headTag.canContain(new Tag("title")));
 assertTrue(headTag.canContain(new Tag("meta")));

 // Test case for dt and dd in dl
 Tag dtTag = new Tag("dt");
 Tag ddTag = new Tag("dd");
 assertFalse(dtTag.canContain(ddTag));
 assertFalse(ddTag.canContain(dtTag));
 }

 // Test case for isBlock(), isInline(), isData(), and isEmpty() methods
 @Test
 public void testTagProperties() {
 Tag pTag = new Tag("p");
 Tag divTag = new Tag("div");
 Tag imgTag = new Tag("img");
 Tag spanTag = new Tag("span");

 // Test case for block tag
 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());

 // Test case for inline tag
 assertFalse(imgTag.isBlock());
 assertTrue(spanTag.isInline());

 // Test case for data only tag
 assertTrue(imgTag.isData());
 assertFalse(pTag.isData());

 // Test case for empty tag
 assertTrue(imgTag.isEmpty());
 assertFalse(pTag.isEmpty());
 }

 // Test case for preserveWhitespace() method
 @Test
 public void testPreserveWhitespace() {
 Tag preTag = new Tag("pre");
 preTag.setPreserveWhitespace();
 assertTrue(preTag.preserveWhitespace());

 // Test case for non-preserveWhitespace tag
 Tag pTag = new Tag("p");
 assertFalse(pTag.preserveWhitespace());
 }
}