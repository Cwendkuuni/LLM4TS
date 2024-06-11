
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        assert(tag.getName().equals("p"));
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assert(tag.getName().equals("p"));
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assert(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assert(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assert(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        assert(!tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assert(!tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("p");
        assert(!tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("p");
        assert(!tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        boolean isKnownTag = Tag.isKnownTag("p");
        assert(isKnownTag);
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("p");
        assert(!tag.preserveWhitespace());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assert(tag1.equals(tag2));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assert(tag1.hashCode() == tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        assert(tag.toString().equals("p"));
    }
}
