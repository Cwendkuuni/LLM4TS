package org.jsoup.parser;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.setTrackErrors(false);
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // test cases for read() method
                {new Token.StartTag("tag"), Arrays.asList(new Token.StartTag("tag"))},
                {new Token.Character("character data"), Arrays.asList(new Token.Character("character data"))},

                // test cases for emit() method
                {new Token.StartTag("tag"), Arrays.asList(new Token.StartTag("tag"))},
                {new Token.Character("character data"), Arrays.asList(new Token.Character("character data"))},

                // test cases for createTagPending() method
                {true, new Token.StartTag()},
                {false, new Token.EndTag()},

                // test cases for emitTagPending() method
                {new Token.StartTag(), new Token.StartTag()},
                {new Token.EndTag(), new Token.EndTag()},

                // test cases for createCommentPending() method
                {new Token.Comment(), new Token.Comment()},

                // test cases for emitCommentPending() method
                {new Token.Comment(), new Token.Comment()},

                // test cases for createDoctypePending() method
                {new Token.Doctype(), new Token.Doctype()},

                // test cases for emitDoctypePending() method
                {new Token.Doctype(), new Token.Doctype()}
        });
    }

    @Test
    public void testTokeniserOperations() {
        // Arrange
        Token expectedToken = null;
        Token actualToken = null;

        // Act
        // test read() method
        actualToken = tokeniser.read();

        // Assert
        assertEquals(expectedToken, actualToken);

        // Act
        // test emit() method
        tokeniser.emit(expectedToken);

        // Assert
        actualToken = tokeniser.read();
        assertEquals(expectedToken, actualToken);

        // Act
        // test createTagPending() method
        Token.Tag expectedTag = (Token.Tag) expectedToken;
        Token.Tag actualTag = tokeniser.createTagPending(true);

        // Assert
        assertEquals(expectedTag.getClass(), actualTag.getClass());

        // Act
        // test emitTagPending() method
        tokeniser.emitTagPending();

        // Assert
        actualToken = tokeniser.read();
        assertEquals(expectedToken, actualToken);

        // Act
        // test createCommentPending() method
        tokeniser.createCommentPending();
        expectedToken = new Token.Comment();
        expectedToken.appendData("comment data");
        tokeniser.commentPending.appendData("comment data");

        // Assert
        assertEquals(expectedToken.getData(), tokeniser.commentPending.getData());

        // Act
        // test emitCommentPending() method
        tokeniser.emitCommentPending();

        // Assert
        actualToken = tokeniser.read();
        assertEquals(expectedToken, actualToken);

        // Act
        // test createDoctypePending() method
        tokeniser.createDoctypePending();
        expectedToken = new Token.Doctype();
        expectedToken.setName("doctype name");
        tokeniser.doctypePending.setName("doctype name");

        // Assert
        assertEquals(expectedToken.getName(), tokeniser.doctypePending.getName());

        // Act
        // test emitDoctypePending() method
        tokeniser.emitDoctypePending();

        // Assert
        actualToken = tokeniser.read();
        assertEquals(expectedToken, actualToken);
    }
}