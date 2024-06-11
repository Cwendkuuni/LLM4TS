import org.jsoup.parser.Tag;
import org.junit.Test;

public class TagTest {

    @Test
    public void testName() {
        Tag tag = new Tag("div");
        assert tag.getName().equals("div");
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assert !tag.isBlock();
        assert tag.canContainBlock();
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        assert tag.isBlock();
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("div");
        assert tag.formatAsBlock();
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        assert tag.canContainBlock();
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("div");
        assert !tag.isInline();
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("div");
        assert !tag.isData();
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("div");
        assert !tag.isEmpty();
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("div");
        assert !tag.isSelfClosing();
    }

    @Test
    public void testIsKnownTag() {
        assert Tag.isKnownTag("p");
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("div");
        assert !tag.preserveWhitespace();
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("div");
        tag.setSelfClosing();
        assert tag.isSelfClosing();
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assert tag1.equals(tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("div");
        assert tag.hashCode() == "div".hashCode();
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assert tag.toString().equals("div");
    }

    @Test
    public void testRegister() {
        Tag tag = new Tag("div");
        tag.register(tag);
        assert Tag.isKnownTag("div");
    }
}