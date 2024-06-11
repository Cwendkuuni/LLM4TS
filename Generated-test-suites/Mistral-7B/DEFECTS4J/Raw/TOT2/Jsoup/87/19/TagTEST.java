import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.Tag;

public class TagTest {
    @Test
    public void testTagName() {
        Tag tag = new Tag("div");
        Assert.assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOfTagName() {
        Tag tag = Tag.valueOf("div");
        Assert.assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOfCaseInsensitiveTagName() {
        Tag tag = Tag.valueOf("DIV");
        Assert.assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOfUnknownTagName() {
        Tag tag = Tag.valueOf("unknown");
        Assert.assertFalse(tag.isKnownTag());
        Assert.assertEquals("unknown", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        Assert.assertTrue(tag.isBlock());

        Tag imgTag = new Tag("img");
        Assert.assertFalse(imgTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag pTag = new Tag("p");
        Assert.assertTrue(pTag.formatAsBlock());

        Tag inputTag = new Tag("input");
        Assert.assertFalse(inputTag.formatAsBlock());
    }

    @Test
    public void testIsDataTag() {
        Tag scriptTag = new Tag("script");
        Assert.assertTrue(scriptTag.isData());

        Tag divTag = new Tag("div");
        Assert.assertFalse(divTag.isData());
    }

    @Test
    public void testIsEmptyTag() {
        Tag imgTag = new Tag("img");
        Assert.assertTrue(imgTag.isEmpty());

        Tag pTag = new Tag("p");
        Assert.assertFalse(pTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag imgTag = new Tag("img");
        Assert.assertTrue(imgTag.isSelfClosing());

        Tag divTag = new Tag("div");
        Assert.assertFalse(divTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = new Tag("pre");
        Assert.assertTrue(preTag.preserveWhitespace());

        Tag pTag = new Tag("p");
        Assert.assertFalse(pTag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag inputTag = new Tag("input");
        Assert.assertTrue(inputTag.isFormListed());

        Tag divTag = new Tag("div");
        Assert.assertFalse(divTag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag inputTypeText = new Tag("input").setType("text");
        Assert.assertTrue(inputTypeText.isFormSubmittable());

        Tag divTag = new Tag("div");
        Assert.assertFalse(divTag.isFormSubmittable());
    }
}