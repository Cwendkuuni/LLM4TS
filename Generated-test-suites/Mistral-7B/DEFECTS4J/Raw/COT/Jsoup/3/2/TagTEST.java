import org.apache.commons.lang3.Validate;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagTest {

    @Test
    public void testgetValueOf() {
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals("p", tag.getName());

        tag = Tag.valueOf("div");
        Assert.assertEquals("div", tag.getName());

        tag = Tag.valueOf("UNKNOWN");
        Assert.assertNotEquals("UNKNOWN", tag.getName());
        Assert.assertTrue(tag.isData());
    }

    @Test
    public void testCanContain() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag scriptTag = Tag.valueOf("script");

        Assert.assertTrue(pTag.canContain(pTag));
        Assert.assertTrue(pTag.canContain(divTag));
        Assert.assertTrue(pTag.canContain(imgTag));
        Assert.assertTrue(pTag.canContain(scriptTag));
        Assert.assertTrue(divTag.canContain(pTag));
        Assert.assertTrue(divTag.canContain(divTag));
        Assert.assertTrue(divTag.canContain(imgTag));
        Assert.assertTrue(divTag.canContain(scriptTag));
        Assert.assertFalse(pTag.canContain(pTag)); // cannot contain self
        Assert.assertFalse(imgTag.canContain(imgTag)); // cannot contain self
        Assert.assertFalse(scriptTag.canContain(scriptTag)); // cannot contain self
    }

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag scriptTag = Tag.valueOf("script");

        Assert.assertTrue(pTag.isBlock());
        Assert.assertTrue(divTag.isBlock());
        Assert.assertFalse(imgTag.isBlock());
        Assert.assertFalse(scriptTag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag scriptTag = Tag.valueOf("script");

        Assert.assertTrue(pTag.canContainBlock());
        Assert.assertTrue(divTag.canContainBlock());
        Assert.assertFalse(imgTag.canContainBlock());
        Assert.assertFalse(scriptTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag scriptTag = Tag.valueOf("script");

        Assert.assertFalse(pTag.isInline());
        Assert.assertFalse(divTag.isInline());
        Assert.assertFalse(imgTag.isInline());
        Assert.assertFalse(scriptTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag scriptTag = Tag.valueOf("script");

        Assert.assertFalse(pTag.isData());
        Assert.assertFalse(divTag.isData());
        Assert.assertFalse(imgTag.isData());
        Assert.assertTrue(scriptTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag scriptTag = Tag.valueOf("script");
        Tag emptyTag = Tag.valueOf("img");
        emptyTag.setEmpty();

        Assert.assertFalse(pTag.isEmpty());
        Assert.assertFalse(divTag.isEmpty());
        Assert.assertFalse(imgTag.isEmpty());
        Assert.assertFalse(scriptTag.isEmpty());
        Assert.assertTrue(emptyTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pTag = Tag.valueOf("p");
        Tag preTag = Tag.valueOf("pre");
        preTag.setPreserveWhitespace();

        Assert.assertFalse(pTag.preserveWhitespace());
        Assert.assertTrue(preTag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag bodyTag = Tag.valueOf("body");

        pTag.setAncestors(Arrays.asList(bodyTag, divTag, pTag));

        Assert.assertEquals(divTag.getName(), pTag.getImplicitParent().getName());
    }

    @Test
    public void testIsValidParent() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag bodyTag = Tag.valueOf("body");

        Assert.assertTrue(pTag.isValidParent(divTag));
        Assert.assertFalse(divTag.isValidParent(pTag));
        Assert.assertTrue(bodyTag.isValidParent(pTag));
    }

    @Test(expected = AssertionError.class)
    public void testEquals() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        Assert.assertEquals(pTag1, pTag2);
        Assert.assertNotEquals(pTag1, divTag);

        pTag1.setAncestor("body");
        pTag2.setAncestor("head");

        Assert.assertNotEquals(pTag1, pTag2);
    }

    @Test
    public void testHashCode() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        Map<Tag, Integer> map = new HashMap<>();

        map.put(pTag1, 1);
        map.put(pTag2, 2);
        map.put(divTag, 3);

        Tag newPTag = new Tag("p");
        newPTag.setAncestor("body");

        map.put(newPTag, 4);

        List<Tag> tags = Arrays.asList(pTag1, pTag2, divTag, newPTag);

        for (int i = 0; i < tags.size(); i++) {
            Tag tag1 = tags.get(i);
            for (int j = i + 1; j < tags.size(); j++) {
                Tag tag2 = tags.get(j);
                int tag1HashCode = tag1.hashCode();
                int tag2HashCode = tag2.hashCode();
                Assert.assertNotEqual(tag1HashCode, tag2HashCode);
                Assert.assertEquals(tag1, tag1);
                Assert.assertEquals(tag2, tag2);
                Assert.assertEquals(tag1.hashCode(), tag1HashCode);
                Assert.assertEquals(tag2.hashCode(), tag2HashCode);
            }
        }
    }

    @Test
    public void testToString() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        Assert.assertEquals("p", pTag.toString());
        Assert.assertEquals("div", divTag.toString());
    }

    @Test(expected = AssertionError.class)
    public void testUnknownTagName() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTagName() {
        Tag.valueOf("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagNameWithNullCharacters() {
        Tag.valueOf("tag\u0000name");
    }

    @Test
    public void testCreateBlock() {
        Tag htmlTag = new Tag("HTML");
        Tag headTag = new Tag("HEAD");
        Tag bodyTag = new Tag("BODY");
        Tag frameSetTag = new Tag("FRAMESET");

        htmlTag.setAncestor(new String[0]);
        headTag.setAncestor(htmlTag);
        bodyTag.setAncestor(htmlTag);
        frameSetTag.setAncestor(htmlTag);

        Tag scriptTag = new Tag("SCRIPT");
        scriptTag.setAncestor(headTag, bodyTag).setContainDataOnly();
        Tag noScriptTag = new Tag("NOSCRIPT");
        noScriptTag.setAncestor(headTag, bodyTag);
        Tag styleTag = new Tag("STYLE");
        styleTag.setAncestor(headTag, bodyTag).setContainDataOnly();
        Tag metaTag = new Tag("META");
        metaTag.setAncestor(headTag, bodyTag).setEmpty();
        Tag linkTag = new Tag("LINK").setAncestor(headTag, bodyTag).setEmpty();
        Tag objectTag = new Tag("OBJECT");
        objectTag.setAncestor(headTag, bodyTag);
        Tag titleTag = new Tag("TITLE").setAncestor(headTag, bodyTag).setContainDataOnly();
        Tag baseTag = new Tag("BASE").setAncestor(headTag, bodyTag).setEmpty();

        Tag frameTag = new Tag("FRAME").setAncestor(frameSetTag).setEmpty();
        Tag noFramesTag = new Tag("NOFRAMES").setAncestor(frameSetTag).setContainDataOnly();

        List<Tag> tags = Arrays.asList(htmlTag, headTag, bodyTag, frameSetTag, scriptTag, noScriptTag, styleTag, metaTag, linkTag, objectTag, titleTag,
                baseTag, frameTag, noFramesTag);

        for (Tag tag : tags) {
            Assert.assertNotNull(tag);
            Assert.assertEquals(tag.getClass().getSimpleName(), tag.getName());
            System.out.println(tag.getName());
        }
    }

    @Test
    public void testCreateInline() {
        Tag fontstyleTag = new Tag("FONT");
        Tag ttTag = new Tag("TT");
        Tag iTag = new Tag("I");
        Tag bTag = new Tag("B");
        Tag bigTag = new Tag("BIG");
        Tag smallTag = new Tag("SMALL");

        fontstyleTag.isBlock = false;
        fontstyleTag.canContainBlock = false;

        ttTag.setAncestor(fontstyleTag);
        iTag.setAncestor(fontstyleTag);
        bTag.setAncestor(fontstyleTag);
        bigTag.setAncestor(fontstyleTag);
        smallTag.setAncestor(fontstyleTag);

        List<Tag> tags = Arrays.asList(fontstyleTag, ttTag, iTag, bTag, bigTag, smallTag);

        for (Tag tag : tags) {
            Assert.assertNotNull(tag);
            Assert.assertEquals(tag.getClass().getSimpleName(), tag.getName());
            System.out.println(tag.getName());
        }
    }
}