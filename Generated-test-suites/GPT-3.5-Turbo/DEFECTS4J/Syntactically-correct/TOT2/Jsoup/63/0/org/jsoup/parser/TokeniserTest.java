import org.jsoup.helper.StringUtil;
import org.jsoup.parser.*;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(new StringReader("")), new ParseErrorList());
    }

    @After
    public void tearDown() {
        tokeniser = null;
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertEquals(null, token);
    }

    @Test
    public void testEmit() {
        Token tag = tokeniser.createTagPending(true);
        tokeniser.emit(tag);

        assertEquals(tag, tokeniser.read());
    }

    @Test
    public void testConsumeCharacterReference() {
        int[] codeRef = tokeniser.consumeCharacterReference(null, false);
        assertEquals(null, codeRef);
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "This &amp; is &lt; an &quot;escaped&quot; string";
        String unescapedString = "This & is < an \"escaped\" string";

        String result = tokeniser.unescapeEntities(false);

        assertEquals(unescapedString, result);
    }

    @Test
    public void testError() {
        tokeniser.error("Test Error");
        assertTrue(tokeniser.errors.size() > 0);
    }

}