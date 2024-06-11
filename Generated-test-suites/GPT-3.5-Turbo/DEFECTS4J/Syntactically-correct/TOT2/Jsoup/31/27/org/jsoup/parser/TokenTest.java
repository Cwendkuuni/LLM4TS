import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Doctype();
        assertEquals("Doctype", token.tokenType());
    }
    
    @Test
    public void testGetName() {
        Doctype doctype = new Doctype();
        doctype.name.append("TestName");
        assertEquals("TestName", doctype.getName());
    }
    
    // Add test cases for other methods
    
    @Test
    public void testEmptyNameInTag() {
        Tag tag = new StartTag();
        assertEquals("", tag.name());
    }
    
    @Test
    public void testNullValuesForAttributesInStartTag() {
        StartTag startTag = new StartTag();
        assertNull(startTag.getAttributes());
    }
    
    @Test
    public void testNullDataInCharacter() {
        Character character = new Character(null);
        assertNull(character.getData());
    }
}