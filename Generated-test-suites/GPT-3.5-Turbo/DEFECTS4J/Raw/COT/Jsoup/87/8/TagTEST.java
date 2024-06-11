
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        // Arrange
        Tag tag = new Tag("div");
        
        // Act
        String name = tag.getName();
        
        // Assert
        assertEquals("div", name);
    }

    @Test
    public void testValueOf() {
        // Arrange
        ParseSettings settings = ParseSettings.preserveCase;
        
        // Act
        Tag tag = Tag.valueOf("p", settings);
        
        // Assert
        assertTrue(tag.isBlock());
    }

    @Test
    public void testIsBlock() {
        // Arrange
        Tag tag = new Tag("div");
        
        // Act
        boolean isBlock = tag.isBlock();
        
        // Assert
        assertTrue(isBlock);
    }

    @Test
    public void testFormatAsBlock() {
        // Arrange
        Tag tag = new Tag("div");
        
        // Act
        boolean formatAsBlock = tag.formatAsBlock();
        
        // Assert
        assertTrue(formatAsBlock);
    }

    @Test
    public void testCanContainBlock() {
        // Arrange
        Tag tag = new Tag("div");
        
        // Act
        boolean canContainBlock = tag.canContainBlock();
        
        // Assert
        assertTrue(canContainBlock);
    }

    @Test
    public void testIsInline() {
        // Arrange
        Tag tag = new Tag("span");
        
        // Act
        boolean isInline = tag.isInline();
        
        // Assert
        assertTrue(isInline);
    }

    @Test
    public void testIsData() {
        // Arrange
        Tag tag = new Tag("img");
        
        // Act
        boolean isData = tag.isData();
        
        // Assert
        assertFalse(isData);
    }

    @Test
    public void testIsEmpty() {
        // Arrange
        Tag tag = new Tag("img");
        
        // Act
        boolean isEmpty = tag.isEmpty();
        
        // Assert
        assertFalse(isEmpty);
    }

    @Test
    public void testIsSelfClosing() {
        // Arrange
        Tag tag = new Tag("br");
        
        // Act
        boolean isSelfClosing = tag.isSelfClosing();
        
        // Assert
        assertTrue(isSelfClosing);
    }

    @Test
    public void testIsKnownTag() {
        // Arrange
        String tagName = "p";
        
        // Act
        boolean isKnownTag = Tag.isKnownTag(tagName);
        
        // Assert
        assertTrue(isKnownTag);
    }

    @Test
    public void testPreserveWhitespace() {
        // Arrange
        Tag tag = new Tag("pre");
        
        // Act
        boolean preserveWhitespace = tag.preserveWhitespace();
        
        // Assert
        assertTrue(preserveWhitespace);
    }

    @Test
    public void testIsFormListed() {
        // Arrange
        Tag tag = new Tag("input");
        
        // Act
        boolean isFormListed = tag.isFormListed();
        
        // Assert
        assertTrue(isFormListed);
    }

    @Test
    public void testIsFormSubmittable() {
        // Arrange
        Tag tag = new Tag("input");
        
        // Act
        boolean isFormSubmittable = tag.isFormSubmittable();
        
        // Assert
        assertTrue(isFormSubmittable);
    }

    @Test
    public void testSetSelfClosing() {
        // Arrange
        Tag tag = new Tag("img");
        
        // Act
        tag.setSelfClosing();
        
        // Assert
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        // Arrange
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        
        // Act
        boolean equals = tag1.equals(tag2);
        
        // Assert
        assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        // Arrange
        Tag tag = new Tag("div");
        
        // Act
        int hashCode = tag.hashCode();
        
        // Assert
        assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        // Arrange
        Tag tag = new Tag("div");
        
        // Act
        String str = tag.toString();
        
        // Assert
        assertNotNull(str);
    }

}

