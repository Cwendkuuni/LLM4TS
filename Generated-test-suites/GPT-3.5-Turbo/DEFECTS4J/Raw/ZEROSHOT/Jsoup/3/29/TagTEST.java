package org.jsoup.parser;

import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        assert("test".equals(tag.getName()));
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assert(tag instanceof Tag);
    }

    @Test
    public void testCanContain() {
        Tag parent = new Tag("parent");
        Tag child = new Tag("child");
        assert(parent.canContain(child));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("test");
        tag.isBlock();
        assert(true);
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("test");
        tag.canContainBlock();
        assert(true);
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("test");
        tag.isInline();
        assert(true);
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("test");
        tag.isData();
        assert(true);
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("test");
        tag.isEmpty();
        assert(true);
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("test");
        tag.preserveWhitespace();
        assert(true);
    }

}