import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 private static Tag htmlTag, headTag, bodyTag, pTag, h1Tag, ulTag, liTag, dtTag, ddTag;

 @BeforeClass
 static public void setup() {
 htmlTag = Tag.valueOf("HTML");
 headTag = Tag.valueOf("HEAD");
 bodyTag = Tag.valueOf("BODY");
 pTag = Tag.valueOf("P");
 h1Tag = Tag.valueOf("H1");
 ulTag = Tag.valueOf("UL");
 liTag = Tag.valueOf("LI");
 dtTag = Tag.getValueOf("DT");
 ddTag = Tag.getValueOf("DD");
 }

 @Test
 public void testTagValuesAreEqual() {
 Tag tag1 = Tag.valueOf("P");
 Tag tag2 = Tag.valueOf("P");
 Tag tag3 = Tag.valueOf("DIV");

 assertEquals(tag1, tag2);
 assertNotEquals(tag1, tag3);
 }

 @Test
 public void testTagCanContain() {
 // can contain p within body
 assertTrue(bodyTag.canContain(pTag));

 // cannot contain h1 within p
 assertFalse(pTag.canContain(h1Tag));

 // can contain li within ul
 assertTrue(ulTag.canContain(liTag));

 // cannot contain dt within dd
 assertFalse(ddTag.canContain(dtTag));
 }

 @Test
 public void testTagIsBlock() {
 // p is a block tag
 assertTrue(pTag.isBlock());

 // h1 is a block tag
 assertTrue(h1Tag.isBlock());

 // dt and dd are not block tags
 assertFalse(dtTag.isBlock());
 assertFalse(ddTag.isBlock());
 }

 @Test
 public void testTagIsData() {
 // p is not a data only tag
 assertFalse(pTag.isData());

 // script is a data only tag
 assertTrue(Tag.valueOf("SCRIPT").isData());
 }

 @Test
 public void testTagIsEmpty() {
 // img is an empty tag
 assertTrue(pTag.isEmpty());

 // p is not an empty tag
 assertFalse(pTag.isEmpty());
 }

 @Test
 public void testTagPreserveWhitespace() {
 // pre is a tag that preserves whitespace
 assertTrue(pTag.preserveWhitespace());

 // p is not a tag that preserves whitespace
 assertFalse(pTag.preserveWhitespace());
 }

 @Test(expected = NullPointerException.class)
 public void testCanContainNullTag() {
 // testing for NPE when trying to call canContain with null tag
 Tag nullTag = null;
 bodyTag.canContain(nullTag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainSameTag() {
 // testing for IAE when trying to call canContain with the same tag
 bodyTag.canContain(bodyTag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainAncestorTag() {
 // testing for IAE when trying to call canContain with an ancestor tag
 headTag.canContain(bodyTag);
 }

}