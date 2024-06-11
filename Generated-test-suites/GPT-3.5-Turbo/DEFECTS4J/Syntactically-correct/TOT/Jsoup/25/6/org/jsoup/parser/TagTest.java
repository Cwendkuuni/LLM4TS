import org.jsoup.parser.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TagTest {

    private Tag tag;

    @BeforeEach
    public void setUp() {
        tag = new Tag("div");
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag knownTag = Tag.valueOf("p");
        Assertions.assertTrue(knownTag.isKnownTag());

        Tag unknownTag = Tag.valueOf("unknown");
        Assertions.assertFalse(unknownTag.isKnownTag());
    }

    @Test
    public void testIsBlock() {
        Assertions.assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Assertions.assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Assertions.assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Assertions.assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Assertions.assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Assertions.assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Assertions.assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Assertions.assertTrue(Tag.isKnownTag("div"));
        Assertions.assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Assertions.assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        tag.setSelfClosing();
        Assertions.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag sameTag = new Tag("div");
        Assertions.assertEquals(tag, sameTag);

        Tag differentTag = new Tag("p");
        Assertions.assertNotEquals(tag, differentTag);
    }

    @Test
    public void testHashCode() {
        Tag sameTag = new Tag("div");
        Assertions.assertEquals(tag.hashCode(), sameTag.hashCode());

        Tag differentTag = new Tag("p");
        Assertions.assertNotEquals(tag.hashCode(), differentTag.hashCode());
    }

    @Test
    public void testToString() {
        Assertions.assertEquals("div", tag.toString());
    }

    @Test
    public void testBlockTags() {
        String[] blockTags = {
                "html", "head", "body", "frameset", "script", "noscript", "style", "meta", "link", "title", "frame",
                "noframes", "section", "nav", "aside", "hgroup", "header", "footer", "p", "h1", "h2", "h3", "h4", "h5", "h6",
                "ul", "ol", "pre", "div", "blockquote", "hr", "address", "figure", "figcaption", "form", "fieldset", "ins",
                "del", "dl", "dt", "dd", "li", "table", "caption", "thead", "tfoot", "tbody", "colgroup", "col", "tr", "th",
                "td", "video", "audio", "canvas", "details", "menu", "plaintext"
        };

        for (String tagName : blockTags) {
            Assertions.assertTrue(Tag.isKnownTag(tagName));
        }
    }

    @Test
    public void testInlineTags() {
        String[] inlineTags = {
                "object", "base", "font", "tt", "i", "b", "u", "big", "small", "em", "strong", "dfn", "code", "samp", "kbd",
                "var", "cite", "abbr", "time", "acronym", "mark", "ruby", "rt", "rp", "a", "img", "br", "wbr", "map", "q",
                "sub", "sup", "bdo", "iframe", "embed", "span", "input", "select", "textarea", "label", "button", "optgroup",
                "option", "legend", "datalist", "keygen", "output", "progress", "meter", "area", "param", "source", "track",
                "summary", "command", "device"
        };

        for (String tagName : inlineTags) {
            Assertions.assertTrue(Tag.isKnownTag(tagName));
        }
    }

    @Test
    public void testEmptyTags() {
        String[] emptyTags = {
                "meta", "link", "base", "frame", "img", "br", "wbr", "embed", "hr", "input", "keygen", "col", "command",
                "device"
        };

        for (String tagName : emptyTags) {
            Assertions.assertTrue(Tag.isKnownTag(tagName));
            Tag tag = Tag.valueOf(tagName);
            Assertions.assertTrue(tag.isEmpty());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        String[] formatAsInlineTags = {
                "title", "a", "p", "h1", "h2", "h3", "h4", "h5", "h6", "pre", "address", "li", "th", "td", "script", "style"
        };

        for (String tagName : formatAsInlineTags) {
            Assertions.assertTrue(Tag.isKnownTag(tagName));
            Tag tag = Tag.valueOf(tagName);
            Assertions.assertFalse(tag.formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        String[] preserveWhitespaceTags = {"pre", "plaintext", "title"};

        for (String tagName : preserveWhitespaceTags) {
            Assertions.assertTrue(Tag.isKnownTag(tagName));
            Tag tag = Tag.valueOf(tagName);
            Assertions.assertTrue(tag.preserveWhitespace());
        }
    }
}