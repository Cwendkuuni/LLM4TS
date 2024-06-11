package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        String html = "<div><script>alert('XSS')</script><p>Clean</p></div>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        String expected = "<html>\n <head></head>\n <body>\n  <div>\n   \n   <p>Clean</p>\n  </div>\n </body>\n</html>";
        Assert.assertThat(cleanDocument.html(), is(expected));
    }

    @Test
    public void testIsValid() {
        String html = "<div><script>alert('XSS')</script><p>Clean</p></div>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertThat(isValid, is(false));
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        List<Node> sourceChildren = new ArrayList<>();
        sourceChildren.add(new Element(Tag.valueOf("p"), "").text("Hello"));
        sourceChildren.add(new Element(Tag.valueOf("h1"), "").text("Heading"));
        sourceChildren.add(new Element(Tag.valueOf("script"), "").text("alert('XSS')"));

        sourceChildren.forEach(source::appendChild);

        int numDiscarded = cleaner.copySafeNodes(source, dest);
        Assert.assertThat(numDiscarded, is(1));
        Assert.assertThat(dest.children().size(), is(2));
        Assert.assertThat(dest.child(0).tagName(), is("p"));
        Assert.assertThat(dest.child(1).tagName(), is("h1"));
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attributes().put("id", "test");
        sourceEl.attributes().put("class", "test-class");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;

        Assert.assertThat(dest.tagName(), is("div"));
        Assert.assertThat(dest.attributes().size(), is(1));
        Assert.assertThat(dest.attributes().hasKey("id"), is(true));
        Assert.assertThat(dest.attributes().get("id"), is("test"));

        Attributes enforcedAttrs = Whitelist.basic().getEnforcedAttributes("div");
        Assert.assertThat(dest.attributes().size(), is(sourceEl.attributes().size() + enforcedAttrs.size()));
    }
}