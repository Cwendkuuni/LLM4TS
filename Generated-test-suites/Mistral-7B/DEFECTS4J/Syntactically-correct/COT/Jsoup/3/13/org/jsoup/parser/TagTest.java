import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 private Tag htmlTag;
 private Tag bodyTag;
 private Tag pTag;
 private Tag imgTag;
 private Tag headTag;
 private Tag scriptTag;
 private Tag linkTag;
 private Tag titleTag;
 private Tag dtTag;
 private Tag ddTag;
 private Tag ulTag;
 private Tag olTag;
 private Tag trTag;
 private Tag thTag;
 private Tag tdTag;

 @Before
 public void setup() {
 htmlTag = Tag.valueOf("html");
 bodyTag = Tag.valueOf("body");
 pTag = Tag.valueOf("p");
 imgTag = Tag.valueOf("img");
 headTag = Tag.getValueOf("HEAD");
 scriptTag = Tag.valueOf("script");
 linkTag = Tag.valueOf("link");
 titleTag = Tag.valueOf("title");
 dtTag = Tag.valueOf("dt");
 ddTag = Tag.valueOf("dd");
 ulTag = Tag.valueOf("ul");
 olTag = Tag.valueOf("ol");
 trTag = Tag.valueOf("tr");
 thTag = Tag.valueOf("th");
 tdTag = Tag.valueOf("td");
 }

 @Test
 public void testTagNameGetMethod() {
 Tag tag = new Tag("test");
 assertEquals("test", tag.getName());
 }

 @Test
 public void testTagValueOfMethod() {
 assertEquals(htmlTag, Tag.valueOf("HTML"));
 assertEquals(bodyTag, Tag.getValueOf("BODY"));
 }

 @Test
 public void testCanContainMethodWithValidChild() {
 assertTrue(pTag.canContain(imgTag));
 }

 @Test
 public void testCanContainMethodWithInvalidChild() {
 assertFalse(pTag.canContain(pTag));
 assertFalse(pTag.canContain(htmlTag));
 assertFalse(headTag.canContain(scriptTag));
 assertFalse(bodyTag.canContain("script"));
 }

 @Test(expected = NullPointerException.class)
 public void testCanContainMethodWithNullChild() {
 pTag.canContain(null);
 }

 @Test
 public void testIsBlockMethod() {
 assertTrue(htmlTag.isBlock());
 assertTrue(bodyTag.isBlock());
 assertFalse(pTag.isBlock());
 }

 @Test
 public void testIsInlineMethod() {
 assertFalse(htmlTag.isInline());
 assertFalse(bodyTag.isInline());
 assertTrue(pTag.isInline());
 }

 @Test
 public void testIsDataMethod() {
 assertFalse(htmlTag.isData());
 assertFalse(bodyTag.isData());
 assertFalse(pTag.isData());
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testIsEmptyMethod() {
 assertFalse(htmlTag.isEmpty());
 assertFalse(bodyTag.isEmpty());
 assertTrue(imgTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespaceMethod() {
 assertFalse(htmlTag.preserveWhitespace());
 assertFalse(bodyTag.preserveWhitespace());
 assertFalse(pTag.preserveWhitespace());
 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParentMethod() {
 assertEquals(bodyTag, pTag.getImplicitParent());
 }

 @Test
 public void testIsValidParentMethod() {
 assertTrue(pTag.isValidParent(imgTag));
 }

 @Test
 public void testEqualsMethod() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertEquals(tag1, tag2);
 }

 @Test
 public void testHashCodeMethod() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToStringMethod() {
 assertEquals("p", pTag.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAncestorWithNullArray() {
 pTag.setAncestor(null);
 }

 @Test
 public void testSetAncestorWithValidArray() {
 Tag html = Tag.valueOf("HTML");
 Tag head = Tag.getValueOf("HEAD");
 Tag body = Tag.getValueOf("BODY");
 List<Tag> ancestorList = Arrays.asList(html, head, body);
 pTag.setAncestor(ancestorList.toArray(new Tag[ancestorList.size()]));
 assertTrue(html.isValidParent(pTag));
 assertTrue(head.isValidParent(pTag));
 assertTrue(body.isValidParent(pTag));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetAncestorWithOutOfBoundsIndex() {
 Tag html = Tag.valueOf("HTML");
 Tag body = Tag.getValueOf("BODY");
 Tag[] ancestors = new Tag[2];
 ancestors[0] = html;
 ancestors[1] = body;
 pTag.setAncestor(ancestors);
 pTag.setAncestor(1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAncestorWithNegativeIndex() {
 Tag html = Tag.valueOf("HTML");
 Tag body = Tag.getValueOf("BODY");
 Tag[] ancestors = new Tag[2];
 ancestors[0] = html;
 ancestors[1] = body;
 pTag.setAncestor(ancestors);
 pTag.setAncestor(-1);
 }

 @Test
 public void testEqualsWithSameInstance() {
 Tag tag = new Tag("p");
 assertEquals(tag, tag);
 }

 @Test
 public void testEqualsWithDifferentTag() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 assertFalse(tag1.equals(tag2));
 }

 @Test
 public void testEqualsWithDifferentObjects() {
 Tag tag1 = new Tag("p");
 Object obj = new Object();
 assertFalse(tag1.equals(obj));
 }

 @Test
 public void testHashCodeWithSameInstance() {
 Tag tag = new Tag("p");
 int hashCode1 = tag.hashCode();
 Tag tag2 = tag;
 int hashCode2 = tag2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testHashCodeWithDifferentTags() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testHashCodeWithDifferentObjects() {
 Tag tag = new Tag("p");
 Object obj = new Object();
 int hashCode1 = tag.hashCode();
 int hashCode2 = obj.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testCanContainDLWithDTAndDD() {
 List<Tag> dlChildren = new ArrayList<>();
 dlChildren.add(dtTag);
 dlChildren.add(ddTag);
 olTag.setAncestor(Tag.valueOf("BODY"));
 assertEquals(olTag, dlTag.getImplicitParent());
 assertTrue(dlTag.canContain(dtTag));
 assertTrue(dtTag.canContain(ddTag));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainWithInvalidTag() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 assertTrue(ulTag.canContain(trTag)); // should throw IllegalArgumentException
 }

 @Test
 public void testCanContainWithEmptyTag() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 assertTrue(ulTag.canContain(imgTag));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainWithSelf() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 assertFalse(ulTag.canContain(ulTag));
 }

 @Test
 public void testCanContainWithDescendant() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 tdTag.setAncestor(trTag);
 trTag.setAncestor(ulTag);
 assertTrue(ulTag.canContain(tdTag));
 }

 @Test
 public void testCanContainWithDescendantAndSelf() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 tdTag.setAncestor(trTag);
 trTag.setAncestor(ulTag);
 assertFalse(ulTag.canContain(ulTag));
 assertTrue(ulTag.canContain(tdTag));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainWithInvalidAncestor() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 headTag.setAncestor(Tag.getValueOf("HEAD"));
 assertFalse(ulTag.isValidParent(headTag));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainWithInvalidAncestor2() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 Tag invalidAncestor = new Tag("invalidTag");
 assertFalse(ulTag.isValidParent(invalidAncestor));
 }

 @Test
 public void testCanContainWithMultipleAncestors() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 liTag.setAncestor(ulTag);
 olTag.setAncestor(liTag);
 assertTrue(ulTag.canContain(olTag));
 }

 @Test
 public void testIsValidParentWithSelf() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 assertTrue(ulTag.isValidParent(ulTag));
 }

 @Test
 public void testIsValidParentWithDescendant() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 liTag.setAncestor(ulTag);
 assertTrue(ulTag.isValidParent(liTag));
 }

 @Test
 public void testIsValidParentWithAncestor() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 liTag.setAncestor(ulTag);
 headTag.setAncestor(Tag.getValueOf("HEAD"));
 assertFalse(ulTag.isValidParent(headTag));
 }

 @Test
 public void testIsValidParentWithInvalidDescendant() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 liTag.setAncestor(ulTag);
 Tag invalidTag = new Tag("invalidTag");
 assertFalse(ulTag.isValidParent(invalidTag));
 }

 @Test
 public void testIsValidParentWithValidAncestor() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 liTag.setAncestor(ulTag);
 headTag.setAncestor(Tag.getValueOf("HEAD"));
 headTag.setAncestor(ulTag);
 assertTrue(ulTag.isValidParent(headTag));
 }

 @Test
 public void testIsValidParentWithMultipleAncestors() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 liTag.setAncestor(ulTag);
 olTag.setAncestor(liTag);
 ulTag2.setAncestor(Tag.valueOf("BODY"));
 liTag2.setAncestor(ulTag2);
 olTag2.setAncestor(liTag2);
 assertTrue(ulTag.isValidParent(olTag2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsValidParentWithInvalidTag() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 assertFalse(ulTag.isValidParent(new Object()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsValidParentWithNullTag() {
 ulTag.setAncestor(Tag.valueOf("BODY"));
 assertFalse(ulTag.isValidParent(null));
 }

}