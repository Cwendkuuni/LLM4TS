package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag tag = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("P");
 Tag tag3 = Tag.valueOf("DIV");
 Tag genericTag = Tag.valueOf("UNKNOWN_TAG");

 assertEquals("p", tag.getName());
 assertEquals("p", tag2.getName());
 assertEquals("div", tag3.getName());
 assertNotSame(tag, genericTag);
 }

 @Test
 public void testCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag headingTag = Tag.valueOf("h1");
 Tag bodyTag = Tag.valueOf("body");
 Tag scriptTag = Tag.valueOf("script");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.canContain(new Tag("br")));
 assertTrue(pTag.canContain(new Tag("a")));
 assertTrue(headingTag.canContain(new Tag("p")));
 assertTrue(headingTag.canContain(new Tag("h1")));
 assertFalse(pTag.canContain(new Tag("p")));
 assertFalse(headingTag.canContain(new Tag("script")));
 assertFalse(bodyTag.canContain(new Tag("script")));
 assertTrue(divTag.canContain(new Tag("p")));
 assertTrue(divTag.canContain(new Tag("div")));
 }

 @Test
 public void testIsValidParent() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag bodyTag = Tag.valueOf("body");
 Tag scriptTag = Tag.valueOf("script");
 Tag headTag = Tag.valueOf("head");

 Tag childWithNoAncestors = new Tag("CHILD_WITHOUT_ANCESTORS");
 Tag childWithParent = new Tag("CHILD_WITH_PARENT").setAncestor(pTag);

 assertTrue(pTag.isValidParent(childWithNoAncestors));
 assertTrue(pTag.isValidParent(bodyTag));
 assertTrue(pTag.isValidParent(scriptTag));
 assertFalse(pTag.isValidParent(headTag));
 assertFalse(pTag.isValidParent(childWithParent));
 }
}