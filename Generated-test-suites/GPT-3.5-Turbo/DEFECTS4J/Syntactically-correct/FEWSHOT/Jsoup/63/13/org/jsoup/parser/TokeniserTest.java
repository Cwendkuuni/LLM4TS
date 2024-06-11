
import org.junit.*;
import static org.junit.Assert.*;

public class TokeniserTest {
    
    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.emit(null);
        assertFalse(tokeniser.isEmitPending);
        assertNull(tokeniser.emitPending);

        tokeniser.emit(new Token.StartTag());
        assertTrue(tokeniser.isEmitPending);
        assertNotNull(tokeniser.emitPending);

        tokeniser.emit("test");
        assertTrue(tokeniser.isEmitPending);
        assertNotNull(tokeniser.emitPending);

        tokeniser.emit('c');
        assertTrue(tokeniser.isEmitPending);
        assertNotNull(tokeniser.emitPending);

        tokeniser.emit(new int[]{1, 2, 3});
        assertTrue(tokeniser.isEmitPending);
        assertNotNull(tokeniser.emitPending);
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        // Test empty reader
        assertNull(tokeniser.consumeCharacterReference(null, true));

        // Test reader with additional allowed character
        tokeniser.reader = new CharacterReader("a");
        assertNull(tokeniser.consumeCharacterReference('a', true));

        // Test reader matching notCharRefCharsSorted
        tokeniser.reader = new CharacterReader("\t");
        assertNull(tokeniser.consumeCharacterReference(null, true));
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        // Test start tag
        Token.Tag tag = tokeniser.createTagPending(true);
        assertTrue(tag instanceof Token.StartTag);
        assertNotNull(tokeniser.tagPending);

        // Test end tag
        tag = tokeniser.createTagPending(false);
        assertTrue(tag instanceof Token.EndTag);
        assertNotNull(tokeniser.tagPending);
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        // Test emitting start tag
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.name("test");
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isEmitPending);
        assertNotNull(tokeniser.emitPending);

        // Test emitting end tag
        tag = tokeniser.createTagPending(false);
        tag.name("test");
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isEmitPending);
        assertNotNull(tokeniser.emitPending);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        // Test empty lastStartTag
        assertFalse(tokeniser.isAppropriateEndTagToken());

        // Test matching lastStartTag
        tokeniser.lastStartTag = "test";
        assertTrue(tokeniser.isAppropriateEndTagToken());

        // Test non-matching lastStartTag
        tokeniser.lastStartTag = "other";
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        // Test empty lastStartTag
        assertNull(tokeniser.appropriateEndTagName());

        // Test non-empty lastStartTag
        tokeniser.lastStartTag = "test";
        assertEquals("test", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void eofError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.characterReferenceError("error");
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testErrorWithString() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.error("error");
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        // Test currentNode is null
        assertTrue(tokeniser.currentNodeInHtmlNS());

        // Test currentNode namespace is HTML
        // todo: implement namespaces correctly
        /*
        Element currentNode = new Element("test", "HTML");
        tokeniser.currentNode = currentNode;
        assertTrue(tokeniser.currentNodeInHtmlNS());

        // Test currentNode namespace is not HTML
        currentNode = new Element("test", "other");
        tokeniser.currentNode = currentNode;
        assertFalse(tokeniser.currentNodeInHtmlNS());
        */
    }

    @Test
    public void testUnescapeEntities() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        // Test empty reader
        assertEquals("", tokeniser.unescapeEntities(true));

        // Test reader with entities
        tokeniser.reader = new CharacterReader("&lt;&#60;&#x3c;");
        assertEquals("<<<", tokeniser.unescapeEntities(true));
    }
}
