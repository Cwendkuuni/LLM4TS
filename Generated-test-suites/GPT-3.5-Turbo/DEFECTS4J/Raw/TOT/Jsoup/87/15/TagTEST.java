import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import org.junit.*;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        Assert.assertEquals("div", tag.getName());
    }
    
    @Test
    public void testValueOfWithTagNameAndSettings() {
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals("p", tag.getName());
        Assert.assertTrue(tag.isBlock());
    }
    
    @Test
    public void testValueOfWithTagname() {
        Tag tag = Tag.valueOf("h1");
        Assert.assertEquals("h1", tag.getName());
        Assert.assertTrue(tag.isBlock());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("table");
        Assert.assertFalse(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        Assert.assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("p");
        Assert.assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = new Tag("span");
        Assert.assertTrue(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = new Tag("span");
        Assert.assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        Assert.assertTrue(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("img");
        Assert.assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.isKnownTag());
    }
    
    @Test
    public void testIsKnownTagWithTagName() {
        Assert.assertTrue(Tag.isKnownTag("p"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("textarea");
        Assert.assertTrue(tag.preserveWhitespace());
    }
    
    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("textarea");
        Assert.assertFalse(tag.isFormListed());
    }
    
    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("input");
        Assert.assertTrue(tag.isFormSubmittable());
    }
}