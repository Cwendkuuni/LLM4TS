import org.jsoup.nodes.Document;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.name("div");
        tokeniser.emitTagPending();
        Token nextToken = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, nextToken.type);
        assertEquals("div", ((Token.StartTag) nextToken).tagName);
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Data);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.transition(TokeniserState.TagName);
        tokeniser.advanceTransition(TokeniserState.TagValue);
        assertEquals(TokeniserState.TagValue, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&#65;");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        int[] codeRef = tokeniser.consumeCharacterReference(null, false);
        assertNotNull(codeRef);
        assertEquals(65, codeRef[0]);
    }

    @Test
    public void testUnescapeEntities() {
        CharacterReader reader = new CharacterReader("This is an &amp; example");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        String unescapedString = tokeniser.unescapeEntities(false);
        assertEquals("This is an & example", unescapedString);
    }

    // Add more test cases for other methods as needed

}