import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

private Tag pTag;
private Tag imgTag;
private Tag scriptTag;
private Tag inputTag;

@Test
public void testTagConstruction() {
pTag = new Tag("p");
Assert.assertEquals("p", pTag.getName());
}

@Test
public void testTagValueOf() {
pTag = Tag.valueOf("p");
Assert.assertEquals("p", pTag.getName());

imgTag = Tag.valueOf("img");
Assert.assertEquals("img", imgTag.getName());

scriptTag = Tag.valueOf("script");
Assert.assertEquals("script", scriptTag.getName());

inputTag = Tag.setValueOf("input");
Assert.assertEquals("input", inputTag.getName());
}

@Test
public void testIsBlockMethod() {
pTag = new Tag("p");
Assert.assertTrue(pTag.isBlock());

imgTag = new Tag("img");
Assert.assertFalse(imgTag.isBlock());
}

@Test
public void testFormatAsBlockMethod() {
pTag = new Tag("p");
Assert.assertTrue(pTag.formatAsBlock());

imgTag = new Tag("img");
Assert.assertFalse(imgTag.formatAsBlock());
}

@Test
public void testCanContainInlineMethod() {
pTag = new Tag("p");
Assert.assertTrue(pTag.canContainInline());

imgTag = new Tag("img");
Assert.assertFalse(imgTag.canContainInline());
}

@Test
public void testIsEmptyMethod() {
imgTag = new Tag("img");
Assert.assertFalse(imgTag.isEmpty());

Tag emptyImageTag = new Tag("img").setEmpty();
Assert.assertTrue(emptyImageTag.isEmpty());
}

@Test
public void testIsSelfClosingMethod() {
imgTag = new Tag("img");
Assert.assertFalse(imgTag.isSelfClosing());

Tag selfClosingImgTag = new Tag("img").setSelfClosing();
Assert.assertTrue(selfClosingImgTag.isSelfClosing());
}

@Test
public void testIsKnownTag() {
Assert.assertTrue(Tag.isKnownTag("p"));
Assert.assertFalse(Tag.isKnownTag("foo"));
}

@Test
public void testPreserveWhitespaceMethod() {
scriptTag = new Tag("script");
Assert.assertFalse(scriptTag.preserveWhitespace());

Tag preTag = new Tag("pre");
Assert.assertTrue(preTag.preserveWhitespace());
}

@Test
public void testIsFormListedMethod() {
inputTag = new Tag("input");
Assert.assertTrue(inputTag.isFormListed());
}

@Test
public void testIsFormSubmittableMethod() {
inputTag = new Tag("input");
Assert.assertTrue(inputTag.isFormSubmittable());
}

@Test(expected = RuntimeException.class)
public void testExceptionWithInvalidTag() {
Tag invalidTag = new Tag("invalidTag");
}

@Test
public void testEqualsMethod() {
pTag = new Tag("p");
imgTag = new Tag("img");
scriptTag = new Tag("script");
inputTag = new Tag("input");

Assert.assertNotEquals(pTag, imgTag);
Assert.assertNotEquals(pTag, scriptTag);
Assert.assertNotEquals(pTag, inputTag);

Tag samePTag = new Tag("p");
Assert.assertEquals(pTag, samePTag);

Tag sameInputTag = new Tag("input");
Assert.assertEquals(inputTag, sameInputTag);
}

@Test
public void testHashCodeMethod() {
pTag = new Tag("p");
imgTag = new Tag("img");
scriptTag = new Tag("script");
inputTag = new Tag("input");

Assert.assertNotEquals(pTag.hashCode(), imgTag.hashCode());
Assert.assertNotEquals(pTag.hashCode(), scriptTag.hashCode());
Assert.assertNotEquals(pTag.hashCode(), inputTag.hashCode());

Tag samePTag = new Tag("p");
Assert.assertEquals(pTag.hashCode(), samePTag.hashCode());

Tag sameInputTag = new Tag("input");
Assert.assertEquals(inputTag.hashCode(), sameInputTag.hashCode());
}

@Test
public void testToStringMethod() {
pTag = new Tag("p");
Assert.assertEquals("p", pTag.toString());
}

@Test
public void testBlockTags() {
for (String tagName : Tag.blockTags) {
Tag tag = Tag.valueOf(tagName);
Assert.assertTrue(tag.isBlock());
}
}

@Test
public void testInlineTags() {
for (String tagName : Tag.inlineTags) {
Tag tag = Tag.valueOf(tagName);
Assert.assertFalse(tag.isBlock());
}
}

@Test
public void testEmptyTags() {
for (String tagName : Tag.emptyTags) {
Tag tag = Tag.valueOf(tagName);
Assert.assertTrue(tag.isEmpty());
}
}

@Test
public void testFormatAsInlineTags() {
for (String tagName : Tag.formatAsInlineTags) {
Tag tag = Tag.valueOf(tagName);
Assert.assertFalse(tag.formatAsBlock());
}
}

@Test
public void testPreserveWhitespaceTags() {
for (String tagName : Tag.preserveWhitespaceTags) {
Tag tag = Tag.valueOf(tagName);
Assert.assertTrue(tag.preserveWhitespace());
}
}

@Test
public void testFormListedTags() {
for (String tagName : Tag.formListedTags) {
Tag tag = Tag.valueOf(tagName);
Assert.assertTrue(tag.isFormListed());
}
}

@Test
public void testFormSubmitTags() {
for (String tagName : Tag.formSubmitTags) {
Tag tag = Tag.valueOf(tagName);
Assert.assertTrue(tag.isFormSubmittable());
}
}
}