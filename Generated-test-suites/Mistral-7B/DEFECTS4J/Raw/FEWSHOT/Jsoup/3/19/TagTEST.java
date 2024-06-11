package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag html = Tag.valueOf("HTML");
        Tag head = html.setValue("HEAD");
        Tag body = html.setValue("BODY");
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");

        assertEquals("HTML", html.getName());
        assertEquals("HEAD", head.getName());
        assertEquals("BODY", body.getName());
        assertEquals("P", p.getName());
        assertEquals("DIV", div.getName());
    }

    @Test
    public void testCanContain() {
        Tag html = Tag.valueOf("HTML");
        Tag head = html.setValue("HEAD");
        Tag body = html.setValue("BODY");
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");

        assertTrue(body.canContain(p));
        assertTrue(head.canContain(p));
        assertTrue(body.canContain(div));
        assertTrue(head.canContain(div));
        assertTrue(html.canContain(head));
        assertTrue(html.canContain(body));
        assertFalse(body.canContain(body));
        assertFalse(p.canContain(body));

        List<Tag> invalidChildren = Arrays.asList(p, div);
        for (Tag child : invalidChildren) {
            for (Tag parent : Arrays.asList(body, head, html)) {
                assertFalse(parent.canContain(child));
            }
        }
    }

    @Test
    public void testAncestors() {
        Tag html = Tag.valueOf("HTML");
        Tag head = html.setValue("HEAD");
        Tag body = html.setValue("BODY");
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");

        assertEquals(new ArrayList<>(), p.ancestors);
        assertEquals(Arrays.asList(html, body), body.ancestors);
        assertEquals(Arrays.asList(html, head, body), head.ancestors);

        Tag child = body.setValue("CHILD");
        assertEquals(Arrays.asList(html, body), child.ancestors);

        Tag grandchild = child.setValue("GRANDCHILD");
        assertEquals(Arrays.asList(html, body, child), grandchild.ancestors);
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag html1 = Tag.valueOf("HTML");
        Tag html2 = Tag.valueOf("HTML");
        Tag p1 = Tag.valueOf("P");
        Tag p2 = Tag.valueOf("P");

        assertEquals(html1, html2);
        assertEquals(html1.hashCode(), html2.hashCode());
        assertNotEquals(html1, p1);
        assertNotEquals(html1, null);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test(expected = NullPointerException.class)
    public void testNullChild() {
        Tag parent = Tag.valueOf("PARENT");
        parent.canContain(null);
    }

    @Test
    public void testToString() {
        Tag html = Tag.valueOf("HTML");
        Tag head = html.setValue("HEAD");
        Tag body = html.setValue("BODY");
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");

        assertEquals("HTML", html.toString());
        assertEquals("HEAD", head.toString());
        assertEquals("BODY", body.toString());
        assertEquals("P", p.toString());
        assertEquals("DIV", div.toString());
    }
}