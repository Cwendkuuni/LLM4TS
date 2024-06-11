package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        Assert.assertEquals("p", tag.getName());
    }
    
    @Test
    public void testIsBlock() {
        Tag blockTag = new Tag("div");
        Assert.assertEquals(true, blockTag.isBlock());
        
        Tag inlineTag = new Tag("span");
        Assert.assertEquals(false, inlineTag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag blockTag = new Tag("div");
        Assert.assertEquals(true, blockTag.formatAsBlock());
        
        Tag inlineTag = new Tag("span");
        Assert.assertEquals(false, inlineTag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag blockTag = new Tag("div");
        Assert.assertEquals(true, blockTag.canContainBlock());
        
        Tag inlineTag = new Tag("span");
        Assert.assertEquals(false, inlineTag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag blockTag = new Tag("div");
        Assert.assertEquals(false, blockTag.isInline());
        
        Tag inlineTag = new Tag("span");
        Assert.assertEquals(true, inlineTag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = new Tag("div");
        Assert.assertEquals(false, tag.isData());
        
        Tag dataTag = new Tag("br");
        Assert.assertEquals(true, dataTag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("div");
        Assert.assertEquals(false, tag.isEmpty());
        
        Tag emptyTag = new Tag("img");
        Assert.assertEquals(true, emptyTag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("div");
        Assert.assertEquals(false, tag.isSelfClosing());
        
        Tag selfClosingTag = new Tag("img");
        Assert.assertEquals(true, selfClosingTag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        Tag knownTag = Tag.valueOf("div");
        Assert.assertEquals(true, knownTag.isKnownTag());
        
        Tag unknownTag = Tag.valueOf("custom");
        Assert.assertEquals(false, unknownTag.isKnownTag());
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("div");
        Assert.assertEquals(false, tag.preserveWhitespace());
        
        Tag wsTag = new Tag("pre");
        Assert.assertEquals(true, wsTag.preserveWhitespace());
    }
    
    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("div");
        Assert.assertEquals(false, tag.isFormListed());
        
        Tag listedTag = new Tag("input");
        Assert.assertEquals(true, listedTag.isFormListed());
    }
    
    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("div");
        Assert.assertEquals(false, tag.isFormSubmittable());
        
        Tag submittableTag = new Tag("input");
        Assert.assertEquals(true, submittableTag.isFormSubmittable());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        Assert.assertEquals(tag1, tag2);
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        Assert.assertEquals("div", tag.toString());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        Assert.assertEquals(tag, Tag.valueOf("div", ParseSettings.preserveCase));
        Assert.assertEquals(tag, Tag.valueOf("DIV", ParseSettings.preserveCase));
    }
    
    @Test
    public void testIsKnownTag() {
        Assert.assertEquals(true, Tag.isKnownTag("div"));
        Assert.assertEquals(false, Tag.isKnownTag("custom"));
    }
}