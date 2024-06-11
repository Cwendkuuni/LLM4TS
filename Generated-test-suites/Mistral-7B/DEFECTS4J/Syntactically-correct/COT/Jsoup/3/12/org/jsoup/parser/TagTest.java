import org.apache.commons.lang3.Validate;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TagTest {

    @Test
    public void testgetValueOf() {
        Tag tag = Tag.valueOf("p");
        Assert.assertTrue(tag.isBlock());
        Assert.assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("div");
        Assert.assertTrue(tag.isBlock());
        Assert.assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("span");
        Assert.assertFalse(tag.isBlock());
        Assert.assertTrue(tag.canContainInline());

        tag = Tag.valueOf("input");
        Assert.assertFalse(tag.isBlock());
        Assert.assertTrue(tag.canContainInline());

        tag = Tag.valueOf("unknown");
        Assert.assertFalse(tag.isBlock());
        Assert.assertTrue(tag.canContainInline());
        Assert.assertEquals("unknown", tag.getName());
    }

    @Test
    public void testCanContain() {
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");
        Tag span = Tag.valueOf("span");
        Tag input = Tag.valueOf("input");

        Assert.assertTrue(p.canContain(div));
        Assert.assertTrue(p.canContain(span));
        Assert.assertFalse(p.canContain(input));

        Assert.assertTrue(div.canContain(p));
        Assert.assertTrue(div.canContain(div));
        Assert.assertTrue(div.canContain(span));
        Assert.assertFalse(div.canContain(input));

        Assert.assertTrue(span.canContain(p));
        Assert.assertTrue(span.canContain(div));
        Assert.assertTrue(span.canContain(span));
        Assert.assertFalse(span.canContain(input));

        Assert.assertFalse(input.canContain(p));
        Assert.assertFalse(input.canContain(div));
        Assert.assertFalse(input.canContain(span));
        Assert.assertFalse(input.canContain(input));
    }

    @Test
    public void testIsData() {
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");
        Tag span = Tag.valueOf("span");
        Tag input = Tag.valueOf("input");

        Assert.assertFalse(p.isData());
        Assert.assertFalse(div.isData());
        Assert.assertFalse(span.isData());
        Assert.assertTrue(input.isData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanContainNull() {
        Tag tag = Tag.valueOf("p");
        tag.canContain(null);
    }

    @Test
    public void testIsValidParent() {
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");
        Tag body = Tag.valueOf("body");
        Tag head = Tag.valueOf("head");
        Tag frame = Tag.valueOf("frameset");

        Assert.assertTrue(p.isValidParent(p));
        Assert.assertTrue(p.isValidParent(div));
        Assert.assertTrue(p.isValidParent(body));
        Assert.assertFalse(p.isValidParent(head));
        Assert.assertFalse(p.isValidParent(frame));
        Assert.assertFalse(div.isValidParent(p));
    }

    @Test
    public void testEquals() {
        Tag p1 = Tag.valueOf("p");
        Tag p2 = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");

        Assert.assertEquals(p1, p2);
        Assert.assertNotEquals(p1, div);
    }

    @Test
    public void testHashCode() {
        Tag p1 = Tag.valueOf("p");
        Tag p2 = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");

        Assert.assertEquals(p1.hashCode(), p2.hashCode());
        Assert.assertNotEquals(p1.hashCode(), div.hashCode());
    }

    @Test
    public void testToString() {
        Tag p = Tag.valueOf("p");
        Assert.assertEquals("p", p.toString());
    }

    @Test
    public void testEmptyTags() {
        Tag img = Tag.valueOf("img");
        Tag br = Tag.valueOf("br");
        Tag map = Tag.valueOf("map");
        Tag area = Tag.valueOf("area");
        Tag ins = Tag.valueOf("ins");
        Tag del = Tag.valueOf("del");

        Assert.assertTrue(img.isEmpty());
        Assert.assertTrue(br.isEmpty());
        Assert.assertFalse(map.isEmpty());
        Assert.assertFalse(area.isEmpty());
        Assert.assertTrue(ins.isEmpty());
        Assert.assertTrue(del.isEmpty());
    }

    @Test
    public void testInvalidParentChildRelationship() {
        Tag p = Tag.valueOf("p");
        Tag p2 = Tag.valueOf("p");

        Assert.assertFalse(p.canContain(p2));
        Assert.assertFalse(p2.canContain(p));
    }

    @Test
    public void testUnknownTag() {
        Tag unknown = new Tag("unknown");
        Assert.assertFalse(unknown.isBlock());
        Assert.assertTrue(unknown.canContainInline());
        Assert.assertEquals("unknown", unknown.getName());
    }
}