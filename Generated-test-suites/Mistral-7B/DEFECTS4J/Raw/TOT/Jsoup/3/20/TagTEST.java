package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag unknownTag = Tag.valueOf("UnknownTag");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());
 assertNotSame(pTag, divTag);
 assertTrue(pTag.equals(Tag.valueOf("p")));
 assertFalse(pTag.equals(Tag.valueOf("div")));
 assertFalse(pTag.equals(unknownTag));
 }

 @Test
 public void testCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag h1Tag = Tag.valueOf("h1");

 // p can contain div because p is a block tag and div is an inline tag
 assertTrue(pTag.canContain(divTag));

 // h1 cannot contain p because h1 is a block tag and p is a block tag
 assertFalse(h1Tag.canContain(pTag));

 // p can contain p because p is a block tag and p is a block tag
 assertTrue(pTag.canContain(pTag));
 }

 @Test
 public void testIsData() {
 Tag scriptTag = Tag.valueOf("script");
 Tag metaTag = Tag.valueOf("meta");

 // scriptTag is a data only tag
 assertTrue(scriptTag.isData());

 // metaTag is not a data only tag
 assertFalse(metaTag.isData());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag nullTag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag emptyTag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag invalidTag = new Tag("& Tag");
 }

 @Test
 public void testEqualsAndHashcode() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 // pTags are equal because they have the same tag name
 assertEquals(pTag1, pTag2);

 // pTag and divTag are not equal because they have different tag names
 assertNotEquals(pTag1, divTag);

 // hashcodes should match for equal objects
 int pTagHashcode1 = pTag1.hashCode();
 int pTagHashcode2 = pTag2.hashCode();
 assertEquals(pTagHashcode1, pTagHashcode2);

 // hashcodes should be different for non-equal objects
 int pTagHashcode3 = pTag1.hashCode();
 int divTagHashcode = divTag.hashCode();
 assertNotEquals(pTagHashcode1, divTagHashcode);
 }

 @Test
 public void testToString() {
 Tag pTag = Tag.valueOf("p");
 assertEquals("p", pTag.toString());
 }
}