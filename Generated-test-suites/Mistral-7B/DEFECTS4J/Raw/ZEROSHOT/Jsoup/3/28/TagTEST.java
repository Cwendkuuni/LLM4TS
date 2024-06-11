package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("HTML");
        assertEquals("html", tag.tagName);
        assertEquals(true, tag.isBlock);
        assertEquals(true, tag.canContainBlock);
        assertEquals(true, tag.canContainInline);
        assertEquals(false, tag.optionalClosing);
        assertEquals(false, tag.empty);
        assertEquals(new String[0], tag.ancestors.toArray(new String[0]));

        Tag head = new Tag("HEAD");
        Tag body = new Tag("BODY");
        Tag frameSet = new Tag("FRAMESET");

        tag.setAncestor(new String[]{"HTML"});
        head.setAncestor("HTML");
        body.setAncestor("HTML");
        frameSet.setAncestor("HTML");

        assertEquals(Arrays.asList("HTML"), tag.getImplicitParent().ancestors);
        assertEquals(Arrays.asList("HTML", "HEAD"), head.getImplicitParent().ancestors);
        assertEquals(Arrays.asList("HTML", "BODY"), body.getImplicitParent().ancestors);
        assertEquals(Arrays.asList("HTML", "FRAMESET"), frameSet.getImplicitParent().ancestors);
    }

    @Test
    public void testCanContain() {
        Tag html = new Tag("HTML");
        Tag head = new Tag("HEAD");
        Tag body = new Tag("BODY");
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");
        Tag script = new Tag("SCRIPT");
        Tag link = new Tag("LINK");
        Tag title = new Tag("TITLE");
        Tag dt = new Tag("DT");
        Tag dd = new Tag("DD");

        html.setAncestor(new String[0]);
        head.setAncestor("HTML");
        body.setAncestor("HTML");
        p.setAncestor("BODY");
        div.setAncestor("BODY");
        script.setAncestor("HEAD", "BODY");
        link.setAncestor("HEAD", "BODY").setEmpty();
        title.setAncestor("HEAD", "BODY").setContainDataOnly();
        dt.setAncestor("DL");
        dd.setAncestor("DL");

        assertTrue(html.canContain(head));
        assertTrue(html.canContain(body));
        assertTrue(html.canContain(p));
        assertTrue(html.canContain(div));
        assertFalse(html.canContain(script));
        assertFalse(html.canContain(link));
        assertFalse(html.canContain(title));
        assertFalse(html.canContain(dt));
        assertFalse(html.canContain(dd));

        assertTrue(head.canContain(script));
        assertFalse(head.canContain(link));
        assertFalse(head.canContain(title));
        assertTrue(head.canContain(body));

        assertTrue(body.canContain(p));
        assertTrue(body.canContain(div));
        assertFalse(body.canContain(script));
        assertFalse(body.canContain(link));
        assertFalse(body.canContain(title));
        assertFalse(body.canContain(dt));
        assertFalse(body.canContain(dd));

        assertTrue(p.canContain(dt));
        assertTrue(p.canContain(dd));
        assertFalse(p.canContain(script));
        assertFalse(p.canContain(link));
        assertFalse(p.canContain(title));

        assertTrue(div.canContain(dt));
        assertTrue(div.canContain(dd));
        assertFalse(div.canContain(script));
        assertFalse(div.canContain(link));
        assertFalse(div.canContain(title));

        assertTrue(script.canContain(link));
        assertFalse(script.canContain(title));
        assertFalse(script.canContain(dt));
        assertFalse(script.canContain(dd));

        assertTrue(link.canContain(title));
        assertFalse(link.canContain(dt));
        assertFalse(link.canContain(dd));
        assertFalse(link.canContain(script));

        assertTrue(title.canContain(dt));
        assertTrue(title.canContain(dd));
        assertFalse(title.canContain(script));
        assertFalse(title.canContain(link));

        assertTrue(dt.canContain(dd));
        assertFalse(dt.canContain(script));
        assertFalse(dt.canContain(link));
        assertFalse(dt.canContain(title));

        assertTrue(dd.canContain(dt));
        assertFalse(dd.canContain(script));
        assertFalse(dd.canContain(link));
        assertFalse(dd.canContain(title));
    }

    @Test
    public void testHashCodeAndEquals() {
        Tag tag1 = new Tag("HTML");
        Tag tag2 = new Tag("HTML");
        Tag tag3 = new Tag("BODY");

        assertEquals(tag1.hashCode(), tag2.hashCode());
        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("HTML");
        assertEquals("HTML", tag.toString());
    }

    @Test
    public void testValuesOf() {
        Tag html = Tag.valueOf("HTML");
        Tag head = Tag.valueOf("HEAD");
        Tag body = Tag.valueOf("BODY");

        assertEquals("HTML", html.tagName);
        assertEquals("HEAD", head.tagName);
        assertEquals("BODY", body.tagName);
    }

    @Test
    public void testEmptyListAsAncestors() {
        Tag tag = new Tag("HTML");
        tag.setAncestor(Collections.emptyList());
        assertEquals(Collections.emptyList(), tag.ancestors);
    }

    @Test
    public void testGetImplicitParent() {
        Tag html = new Tag("HTML");
        Tag head = new Tag("HEAD");
        Tag body = new Tag("BODY");

        html.setAncestor(new String[]{"HTML"});
        head.setAncestor("HTML");
        body.setAncestor("HTML");

        assertEquals(head, html.getImplicitParent());
        assertEquals(body, html.getImplicitParent());
        assertEquals(html, html.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag html = new Tag("HTML");
        Tag head = new Tag("HEAD");
        Tag body = new Tag("BODY");
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");
        Tag script = new Tag("SCRIPT");
        Tag link = new Tag("LINK");
        Tag title = new Tag("TITLE");

        html.setAncestor(new String[0]);
        head.setAncestor("HTML");
        body.setAncestor("HTML");
        p.setAncestor("BODY");
        div.setAncestor("BODY");
        script.setAncestor("HEAD", "BODY");
        link.setAncestor("HEAD", "BODY").setEmpty();
        title.setAncestor("HEAD", "BODY").setContainDataOnly();

        assertTrue(html.isValidParent(head));
        assertTrue(html.isValidParent(body));
        assertTrue(html.isValidParent(p));
        assertTrue(html.isValidParent(div));
        assertFalse(html.isValidParent(script));
        assertFalse(html.isValidParent(link));
        assertFalse(html.isValidParent(title));

        // Testing against self
        assertTrue(head.isValidParent(head));
        assertFalse(head.isValidParent(script));

        // Testing against ancestor
        assertTrue(body.isValidParent(p));
        assertFalse(body.isValidParent(script));

        // Testing against descendant
        assertTrue(head.isValidParent(title));
        assertFalse(head.isValidParent(p));

        // Testing against unrelated tag
        assertFalse(body.isValidParent(script));
    }
}