import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        Assert.assertEquals("div", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("h1");
        Assert.assertTrue(tag.isBlock());
        Assert.assertTrue(tag.formatAsBlock());
        Assert.assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        Assert.assertTrue(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        Assert.assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
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
        Assert.assertTrue(Tag.isKnownTag("div"));
        Assert.assertFalse(Tag.isKnownTag("custom"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        Assert.assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("div");
        
        Assert.assertEquals(tag1, tag2);
        Assert.assertNotEquals(tag1, tag3);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        
        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = new Tag("h1");
        Assert.assertEquals("h1", tag.toString());
    }
    
    @Test
    public void testRegister() {
        Tag tag = Tag.register(new Tag("custom"));
        Assert.assertTrue(tag.isKnownTag());
    }

    @Test
    public void testRegisterAlreadyRegistered() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        
        Tag tag3 = Tag.register(tag1);
        Assert.assertEquals(tag1, tag3);
        Assert.assertEquals(tag2, tag3);
    }

}