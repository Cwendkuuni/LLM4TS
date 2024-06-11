package org.jsoup.parser;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.Tag;

public class TagTest {

    @Test
    public void testTag_getName() {
        String tagName = "div";
        Tag tag = Tag.valueOf(tagName);
        String expectedName = "div";
        String actualName = tag.getName();
        Assert.assertEquals(expectedName, actualName);
    }

    @Test
    public void testTag_isBlock() {
        String tagName = "div";
        Tag tag = Tag.valueOf(tagName);
        boolean expectedIsBlock = true;
        boolean actualIsBlock = tag.isBlock();
        Assert.assertEquals(expectedIsBlock, actualIsBlock);
    }

    @Test
    public void testTag_isInline() {
        String tagName = "span";
        Tag tag = Tag.valueOf(tagName);
        boolean expectedIsInline = false;
        boolean actualIsInline = tag.isInline();
        Assert.assertEquals(expectedIsInline, actualIsInline);
    }

    @Test
    public void testTag_isData() {
        String tagName = "script";
        Tag tag = Tag.valueOf(tagName);
        boolean expectedIsData = true;
        boolean actualIsData = tag.isData();
        Assert.assertEquals(expectedIsData, actualIsData);
    }

    @Test
    public void testTag_isSelfClosing() {
        String tagName = "img";
        Tag tag = Tag.valueOf(tagName);
        boolean expectedSelfClosing = true;
        boolean actualSelfClosing = tag.isSelfClosing();
        Assert.assertEquals(expectedSelfClosing, actualSelfClosing);
    }

    @Test
    public void testTag_isKnownTag() {
        String tagName = "div";
        boolean expectedIsKnownTag = true;
        boolean actualIsKnownTag = Tag.isKnownTag(tagName);
        Assert.assertEquals(expectedIsKnownTag, actualIsKnownTag);
    }
}