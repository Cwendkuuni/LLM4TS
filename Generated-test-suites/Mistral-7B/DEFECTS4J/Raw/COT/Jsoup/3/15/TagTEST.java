import static org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 private Tag htmlTag;
 private Tag headTag;
 private Tag bodyTag;
 private Tag pTag;
 private Tag h1Tag;
 private Tag ulTag;
 private Tag liTag;
 private Tag imgTag;
 private Tag divTag;

 @Before
 public void setUp() {
 htmlTag = Tag.valueOf("HTML");
 headTag = Tag.valueOf("HEAD");
 bodyTag = Tag.valueOf("BODY");
 pTag = Tag.valueOf("P");
 h1Tag = Tag.valueOf("H1");
 ulTag = Tag.valueOf("UL");
 liTag = Tag.valueOf("LI");
 imgTag = Tag.valueOf("IMG");
 divTag = Tag.valueOf("DIV");
 }

 @Test
 public void testTagNameGetter() {
 // Test the getName() method of the Tag class
 assertEquals("html", htmlTag.getName());
 }

 @Test
 public void testValueOfWithKnownTagName() {
 // Test the valueOf() method with a known tag name
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testValueOfWithUnknownTagName() {
 // Test the valueOf() method with an unknown tag name
 Tag unknownTag = Tag.valueOf("XYZ");
 assertFalse(unknownTag.isBlock());
 assertTrue(unknownTag.isData());
 }

 @Test
 public void testCanContainMethod() {
 // Test the canContain() method
 assertTrue(bodyTag.canContain(pTag));
 assertTrue(pTag.canContain(imgTag));
 assertFalse(pTag.canContain(h1Tag));
 assertFalse(h1Tag.canContain(pTag));
 }

 @Test
 public void testCanContainWithEmptyTag() {
 // Test the canContain() method with empty tags
 assertTrue(pTag.canContain(imgTag));
 assertTrue(imgTag.canContain(pTag));
 assertTrue(bodyTag.canContain(imgTag));
 assertTrue(imgTag.canContain(bodyTag));
 }

 @Test
 public void testCanContainWithSelf() {
 // Test the canContain() method with the same tag
 assertFalse(pTag.canContain(pTag));
 }

 @Test
 public void testCanContainWithHeadTag() {
 // Test the canContain() method with head tag
 assertFalse(headTag.canContain(bodyTag));
 assertFalse(bodyTag.canContain(headTag));
 }

 @Test
 public void testIsBlockMethod() {
 // Test the isBlock() method
 assertTrue(htmlTag.isBlock());
 assertTrue(bodyTag.isBlock());
 assertFalse(pTag.isBlock());
 }

 @Test
 public void testCanContainBlockMethod() {
 // Test the canContainBlock() method
 assertTrue(bodyTag.canContainBlock());
 assertFalse(pTag.canContainBlock());
 }

 @Test
 public void testIsInlineMethod() {
 // Test the isInline() method
 assertFalse(htmlTag.isInline());
 assertFalse(bodyTag.isInline());
 assertTrue(pTag.isInline());
 }

 @Test
 public void testIsDataMethod() {
 // Test the isData() method
 assertFalse(htmlTag.isData());
 assertFalse(bodyTag.isData());
 assertTrue(pTag.isData());
 }

 @Test
 public void testIsEmptyMethod() {
 // Test the isEmpty() method
 assertFalse(htmlTag.isEmpty());
 assertFalse(bodyTag.isEmpty());
 assertTrue(imgTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespaceMethod() {
 // Test the preserveWhitespace() method
 assertFalse(htmlTag.preserveWhitespace());
 assertFalse(bodyTag.preserveWhitespace());
 assertFalse(pTag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParentMethod() {
 // Test the getImplicitParent() method
 assertEquals("body", bodyTag.getImplicitParent().getName());
 }

 @Test
 public void testIsValidParentMethod() {
 // Test the isValidParent() method
 assertTrue(pTag.isValidParent(bodyTag));
 assertTrue(pTag.isValidParent(pTag));
 assertFalse(pTag.isValidParent(h1Tag));
 }

 @Test(expected = AssertionError.class)
 public void testEqualsWithNullObject() {
 // Test the equals() method with a null object
 assertEquals(htmlTag, null);
 }

 @Test
 public void testEqualsWithSameObject() {
 // Test the equals() method with the same object
 assertEquals(htmlTag, htmlTag);
 }

 @Test
 public void testEqualsWithDifferentClass() {
 // Test the equals() method with a different class
 assertNotEquals(htmlTag, new Object());
 }

 @Test
 public void testEqualsWithDifferentTagName() {
 // Test the equals() method with a different tag name
 assertNotEquals(htmlTag, headTag);
 }

 @Test
 public void testEqualsWithSameTagNameButDifferentProperties() {
 // Test the equals() method with the same tag name but different properties
 Tag tag1 = new Tag("p");
 tag1.setBlock(false);
 tag1.setCanContainBlock(false);
 tag1.setCanContainInline(true);
 tag1.setOptionalClosing(false);
 tag1.setEmpty(false);
 tag1.setPreserveWhitespace(false);

 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("p");

 assertEquals(tag1, tag2);
 assertEquals(tag1, tag3);
 assertNotEquals(tag1, tag1); // test self-equality
 }

 @Test
 public void testHashCodeConsistency() {
 // Test the hashCode() method consistency
 Tag tag1 = new Tag("p");
 tag1.setBlock(false);
 tag1.setCanContainBlock(false);
 tag1.setCanContainInline(true);
 tag1.setOptionalClosing(false);
 tag1.setEmpty(false);
 tag1.setPreserveWhitespace(false);

 Tag tag2 = new Tag("p");

 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();

 // Test the hashCode() method consistency by adding a property
 tag1.setPreserveWhitespace(true);
 int hashCode3 = tag1.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testEqualsWithSamePropertiesButDifferentInstance() {
 // Test the equals() method with the same properties but different instances
 Tag tag1 = new Tag("p");
 tag1.setBlock(false);
 tag1.setCanContainBlock(false);
 tag1.setCanContainInline(true);
 tag1.setOptionalClosing(false);
 tag1.setEmpty(false);
 tag1.setPreserveWhitespace(false);

 Tag tag2 = new Tag("p");
 tag2.setBlock(false);
 tag2.setCanContainBlock(false);
 tag2.setCanContainInline(true);
 tag2.setOptionalClosing(false);
 tag2.setEmpty(false);
 tag2.setPreserveWhitespace(false);

 assertTrue(tag1.equals(tag2));
 }

 @Test(expected = AssertionError.class)
 public void testInvalidParent() {
 // Test the isValidParent() method with an invalid parent
 Tag childTag = new Tag("DIV");
 assertFalse(htmlTag.isValidParent(childTag));
 }

}