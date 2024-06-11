import org.apache.commons.lang3.Validate;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        tag = Tag.valueOf("DIV");
        assertEquals("div", tag.getName());
        tag = Tag.valueOf("UNKNOWN");
        assertNotNull(tag);
        assertEquals("UNKNOWN", tag.getName());
    }

    @Test
    public void testTagCanContain() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag headTag = Tag.valueOf("head");
        Tag pParent = Tag.valueOf("p");
        Tag divParent = Tag.valueOf("div");

        assertTrue(pTag.canContain(pTag));
        assertTrue(pTag.canContain(imgTag));
        assertTrue(pTag.canContain(divTag));
        assertFalse(pTag.canContain(headTag));
        assertFalse(pTag.canContain(pParent));

        assertTrue(divTag.canContain(pTag));
        assertTrue(divTag.canContain(imgTag));
        assertTrue(divTag.canContain(divTag));
        assertFalse(divTag.canContain(headTag));
        assertFalse(divTag.canContain(divParent));

        assertTrue(imgTag.canContain(pTag));
        assertTrue(imgTag.canContain(imgTag));
        assertFalse(imgTag.canContain(headTag));
        assertFalse(imgTag.canContain(divParent));

        assertTrue(headTag.canContain(pTag));
        assertTrue(headTag.canContain(imgTag));
        assertFalse(headTag.canContain(divTag));
        assertFalse(headTag.canContain(headTag));

        assertFalse(pParent.canContain(pTag));
        assertFalse(pParent.canContain(imgTag));
        assertFalse(pParent.canContain(divTag));
        assertFalse(pParent.canContain(headTag));
        assertTrue(pParent.canContain(pTag));
    }

    @Test
    public void testTagIsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        assertTrue(pTag.isBlock());
        assertTrue(divTag.isBlock());
    }

    @Test
    public void testTagIsData() {
        Tag pTag = Tag.valueOf("p");
        Tag scriptTag = Tag.valueOf("script");

        assertFalse(pTag.isData());
        assertTrue(scriptTag.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        Tag brTag = Tag.valueOf("br");

        assertFalse(imgTag.isEmpty());
        assertTrue(brTag.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag pTag = Tag.valueOf("p");
        Tag preTag = Tag.valueOf("pre");

        assertFalse(pTag.preserveWhitespace());
        assertTrue(preTag.preserveWhitespace());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagNullChild() {
        Tag parentTag = Tag.valueOf("div");
        Tag childTag = null;
        assertFalse(parentTag.canContain(childTag));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagNullTagName() {
        Tag tag = new Tag("");
        Validate.testIllegalArgument(null, "tagName is marked non-null but is null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagEmptyTagName() {
        Tag tag = new Tag("");
        Validate.testIllegalArgument(null, "tagName is marked non-empty but is empty");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagNullAncestors() {
        Tag tag = new Tag("tag");
        tag.ancestors = null;
        Validate.testIllegalArgument(null, "ancestors is marked non-null but is null");
    }

    @Test
    public void testTagGetImplicitParent() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag bodyTag = Tag.valueOf("body");

        assertEquals(bodyTag, pTag.getImplicitParent());
        assertEquals(divTag, pTag.getImplicitParent());
    }

    @Test
    public void testTagIsValidParent() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag bodyTag = Tag.valueOf("body");
        Tag childDivTag = new Tag("div");
        childDivTag.ancestors = new ArrayList<>(Arrays.asList(bodyTag, pTag, divTag));

        assertTrue(pTag.isValidParent(childDivTag));
        assertFalse(pTag.isValidParent(pTag));
    }

    @Test
    public void testTagEquals() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag1 = Tag.valueOf("div");
        Tag divTag2 = new Tag("div");

        assertTrue(pTag1.equals(pTag2));
        assertTrue(pTag1.equals(pTag1));
        assertFalse(pTag1.equals(divTag1));
        assertFalse(pTag1.equals(divTag2));
    }

    @Test
    public void testTagHashCode() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag1 = Tag.valueOf("div");
        Tag divTag2 = new Tag("div");

        assertEquals(pTag1.hashCode(), pTag2.hashCode());
        assertNotEquals(pTag1.hashCode(), divTag1.hashCode());
        assertNotEquals(pTag1.hashCode(), divTag2.hashCode());
    }

    @Test
    public void testTagToString() {
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.toString());
    }

    @Test
    public void testTagCreateBlock() {
        Tag htmlTag = Tag.createBlock("HTML");
        Tag headTag = Tag.createBlock("HEAD");
        Tag bodyTag = Tag.createBlock("BODY");

        assertEquals("HTML", htmlTag.getName());
        assertEquals("HEAD", headTag.getName());
        assertEquals("BODY", bodyTag.getName());
    }

    @Test
    public void testTagCreateInline() {
        Tag fontStyleTag = Tag.createInline("FONT");
        Tag ttTag = Tag.createInline("TT");
        Tag iTag = Tag.createInline("I");

        assertEquals("FONT", fontStyleTag.getName());
        assertEquals("TT", ttTag.getName());
        assertEquals("I", iTag.getName());
    }

    @Test
    public void testTagRegister() {
        Tag pTag = Tag.register(new Tag("p"));
        assertEquals("p", pTag.getName());
        Tag divTag = Tag.register(new Tag("div"));
        assertEquals("div", divTag.getName());
    }

    @Test
    public void testTagSetContainInlineOnly() {
        Tag pTag = Tag.valueOf("p");
        pTag.setContainInlineOnly();
        assertFalse(pTag.canContainBlock());
        assertTrue(pTag.canContainInline());
    }

    @Test
    public void testTagSetContainDataOnly() {
        Tag scriptTag = Tag.valueOf("script");
        scriptTag.setContainDataOnly();
        assertFalse(scriptTag.canContainBlock());
        assertFalse(scriptTag.canContainInline());
        assertTrue(scriptTag.preserveWhitespace());
    }

    @Test
    public void testTagSetEmpty() {
        Tag imgTag = Tag.valueOf("img");
        imgTag.setEmpty();
        assertFalse(imgTag.canContainBlock());
        assertFalse(imgTag.canContainInline());
        assertTrue(imgTag.isEmpty());
    }

    @Test
    public void testTagSetOptionalClosing() {
        Tag pTag = Tag.valueOf("p");
        pTag.setOptionalClosing();
        assertTrue(pTag.optionalClosing);
    }

    @Test
    public void testTagSetPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        preTag.setPreserveWhitespace();
        assertTrue(preTag.preserveWhitespace);
    }

    @Test
    public void testTagSetAncestor() {
        Tag divTag = Tag.valueOf("div");
        List<Tag> ancestorList = Arrays.asList(Tag.valueOf("body"), Tag.valueOf("html"));
        divTag.setAncestor(ancestorList.toArray(new String[ancestorList.size()]));
        List<Tag> ancestors = divTag.getAncestors();
        assertEquals(ancestors.size(), 2);
        assertEquals(ancestors.get(0).getName(), "html");
        assertEquals(ancestors.get(1).getName(), "body");
    }
}