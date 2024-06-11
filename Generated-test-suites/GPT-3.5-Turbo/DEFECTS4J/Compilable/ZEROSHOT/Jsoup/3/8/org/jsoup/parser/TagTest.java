package org.jsoup.parser;

import org.junit.Test;

public class TagTest {

    @Test
    public void testName() {
        Tag tag = Tag.valueOf("test");
        assert(tag.getName().equals("test"));
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("div");
        assert(tag1.getName().equals("div"));
        
        Tag tag2 = Tag.valueOf("unknown");
        assert(tag2.getName().equals("unknown"));
        
        Tag tag3 = Tag.valueOf("");
        assert(tag3.getName().equals(""));
    }

    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("div");
        Tag child1 = Tag.valueOf("span");
        Tag child2 = Tag.valueOf("h1");
        assert(parent.canContain(child1) == true);
        assert(parent.canContain(child2) == false);
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assert(tag.isBlock() == false);
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assert(tag.canContainBlock() == true);
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("div");
        assert(tag.isInline() == false);
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("div");
        assert(tag.isData() == false);
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assert(tag.isEmpty() == true);
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assert(tag.preserveWhitespace() == true);
    }
}