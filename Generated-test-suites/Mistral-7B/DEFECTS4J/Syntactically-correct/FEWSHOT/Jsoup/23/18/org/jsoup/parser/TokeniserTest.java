package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Attribute;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Tokenizer;
import org.jsoup.parser.Tokeniser;

public class TokeniserTest {

    @Test
    public void testTokeniserBasic() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("<html>".getBytes(StandardCharsets.UTF_8));
        Tokenizer tokenizer = new Tokenizer(input);
        Token expectedToken = null;
        Token actualToken = null;

        while ((actualToken = tokenizer.read()) != null) {
            if (expectedToken == null) {
                expectedToken = actualToken;
            } else {
                assertEquals(expectedToken.type, actualToken.type);
                assertEquals(expectedToken.tagName, actualToken.tagName);
                assertEquals(expectedToken.data, actualToken.data);
                assertEquals(expectedToken.attributes.size(), actualToken.attributes.size());
                for (Attribute attr : expectedToken.attributes) {
                    Attribute actualAttr = actualToken.getAttributeByName(attr.getName());
                    assertEquals(attr.getValue(), actualAttr.getValue());
                }
                expectedToken = null;
            }
        }

        assertNull(expectedToken);
    }

    @Test
    public void testTokeniserSelfClosing() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("<img src=\"image.png\" />".getBytes(StandardCharsets.UTF_8));
        Tokenizer tokenizer = new Tokenizer(input);
        Token expectedToken = null;
        Token actualToken = null;

        while ((actualToken = tokenizer.read()) != null) {
            if (expectedToken == null) {
                expectedToken = actualToken;
            } else {
                assertEquals(expectedToken.type, actualToken.type);
                assertEquals(expectedToken.tagName, actualToken.tagName);
                assertEquals(expectedToken.data, actualToken.data);
                assertEquals(expectedToken.attributes.size(), actualToken.attributes.size());
                for (Attribute attr : expectedToken.attributes) {
                    Attribute actualAttr = actualToken.getAttributeByName(attr.getName());
                    assertEquals(attr.getValue(), actualAttr.getValue());
                }
                if (expectedToken.type == Token.TokenType.StartTag && expectedToken.tagName.equals("img")) {
                    assertTrue(expectedToken.selfClosing);
                }
                expectedToken = null;
            }
        }

        assertNull(expectedToken);
    }

    @Test
    public void testTokeniserEndTag() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("<html><body><p><img src=\"image.png\"></p></body></html>".getBytes(StandardCharsets.UTF_8));
        Tokenizer tokenizer = new Tokenizer(input);
        Token expectedToken = null;
        Token actualToken = null;

        while ((actualToken = tokenizer.read()) != null) {
            if (expectedToken == null) {
                expectedToken = actualToken;
            } else {
                assertEquals(expectedToken.type, actualToken.type);
                assertEquals(expectedToken.tagName, actualToken.tagName);
                assertEquals(expectedToken.data, actualToken.data);
                assertEquals(expectedToken.attributes.size(), actualToken.attributes.size());
                for (Attribute attr : expectedToken.attributes) {
                    Attribute actualAttr = actualToken.getAttributeByName(attr.getName());
                    assertEquals(attr.getValue(), actualAttr.getValue());
                }
                if (expectedToken.type == Token.TokenType.EndTag) {
                    assertEquals(expectedToken.tagName, actualToken.tagName);
                }
                expectedToken = null;
            }
        }

        assertNull(expectedToken);
    }

    @Test
    public void testTokeniserComment() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("<html><!-- This is a comment --></html>".getBytes(StandardCharsets.UTF_8));
        Tokenizer tokenizer = new Tokenizer(input);
        Token expectedToken = null;
        Token actualToken = null;

        while ((actualToken = tokenizer.read()) != null) {
            if (expectedToken == null) {
                expectedToken = actualToken;
            } else {
                assertEquals(expectedToken.type, actualToken.type);
                assertEquals(expectedToken.tagName, actualToken.tagName);
                assertEquals(expectedToken.data, actualToken.data);
                assertEquals(expectedToken.attributes.size(), actualToken.attributes.size());
                for (Attribute attr : expectedToken.attributes) {
                    Attribute actualAttr = actualToken.getAttributeByName(attr.getName());
                    assertEquals(attr.getValue(), actualAttr.getValue());
                }
                if (expectedToken.type == Token.TokenType.Comment) {
                    assertEquals(expectedToken.data, actualToken.data);
                }
                expectedToken = null;
            }
        }

        assertNull(expectedToken);
    }

    @Test
    public void testTokeniserDoctype() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">".getBytes(StandardCharsets.UTF_8));
        Tokenizer tokenizer = new Tokenizer(input);
        Token expectedToken = null;
        Token actualToken = null;

        while ((actualToken = tokenizer.read()) != null) {
            if (expectedToken == null) {
                expectedToken = actualToken;
            } else {
                assertEquals(expectedToken.type, actualToken.type);
                assertEquals(expectedToken.tagName, actualToken.tagName);
                assertEquals(expectedToken.data, actualToken.data);
                assertEquals(expectedToken.attributes.size(), actualToken.attributes.size());
                for (Attribute attr : expectedToken.attributes) {
                    Attribute actualAttr = actualToken.getAttributeByName(attr.getName());
                    assertEquals(attr.getValue(), actualAttr.getValue());
                }
                if (expectedToken.type == Token.TokenType.Doctype) {
                    assertEquals(expectedToken.data, actualToken.data);
                }
                expectedToken = null;
            }
        }

        assertNull(expectedToken);
    }

    @Test
    public void testTokeniserCharacterReference() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("<html>&#x26;<html>".getBytes(StandardCharsets.UTF_8));
        Tokenizer tokenizer = new Tokenizer(input);
        Token expectedToken = null;
        Token actualToken = null;

        while ((actualToken = tokenizer.read()) != null) {
            if (expectedToken == null) {
                expectedToken = actualToken;
            } else {
                assertEquals(expectedToken.type, actualToken.type);
                assertEquals(expectedToken.tagName, actualToken.tagName);
                assertEquals(expectedToken.data, actualToken.data);
                assertEquals(expectedToken.attributes.size(), actualToken.attributes.size());
                for (Attribute attr : expectedToken.attributes) {
                    Attribute actualAttr = actualToken.getAttributeByName(attr.getName());
                    assertEquals(attr.getValue(), actualAttr.getValue());
                }
                if (expectedToken.type == Token.TokenType.Character) {
                    assertEquals(expectedToken.data.charAt(0), actualToken.data.charAt(0));
                }
                expectedToken = null;
            }
        }

        assertNull(expectedToken);
    }

    @Test
    public void testTokeniserNamedCharacterReference() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("<html>&amp;<html>".getBytes(StandardCharsets.UTF_8));
        Tokenizer tokenizer = new Tokenizer(input);
        Token expectedToken = null;
        Token actualToken = null;

        while ((actualToken = tokenizer.read()) != null) {
            if (expectedToken == null) {
                expectedToken = actualToken;
            } else {
                assertEquals(expectedToken.type, actualToken.type);
                assertEquals(expectedToken.tagName, actualToken.tagName);
                assertEquals(expectedToken.data, actualToken.data);
                assertEquals(expectedToken.attributes.size(), actualToken.attributes.size());
                for (Attribute attr : expectedToken.attributes) {
                    Attribute actualAttr = actualToken.getAttributeByName(attr.getName());
                    assertEquals(attr.getValue(), actualAttr.getValue());
                }
                if (expectedToken.type == Token.TokenType.Character) {
                    assertEquals(expectedToken.data.charAt(0), '&');
                }
                expectedToken = null;
            }
        }

        assertNull(expectedToken);
    }

    @Test
    public void testTokeniserError() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("<!-- UNEXPECTED CHARACTER --><html>".getBytes(StandardCharsets.UTF_8));
        Tokenizer tokenizer = new Tokenizer(input);
        List<ParseError> expectedErrors = new ArrayList<>();
        ParseError expectedError = new ParseError(0, "Unexpected character '!' in input state [DATA]");
        expectedErrors.add(expectedError);

        Token token = tokenizer.read();
        while (token != null) {
            assertEquals(Token.TokenType.Comment, token.type);
            assertEquals("", token.tagName);
            assertEquals("UNEXPECTED CHARACTER", token.data);
            assertEquals(0, token.attributes.size());
            token = tokenizer.read();
        }

        assertEquals(1, tokenizer.errors.size());
        for (ParseError error : tokenizer.errors) {
            assertEquals(expectedError.getMessage(), error.getMessage());
            assertEquals(expectedError.getLine(), error.getLine());
            assertEquals(expectedError.getColumn(), error.getColumn());
        }
    }

    @Test
    public void testTokeniserEOF() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("<!-- UNEXPECTED CHARACTER -->".getBytes(StandardCharsets.UTF_8));
        Tokenizer tokenizer = new Tokenizer(input);
        List<ParseError> expectedErrors = new ArrayList<>();
        ParseError expectedError = new ParseError(0, "Unexpectedly reached end of file (EOF) in input state [DATA]");
        expectedErrors.add(expectedError);

        tokenizer.advance(); // consume the '!'

        assertEquals(1, tokenizer.errors.size());
        for (ParseError error : tokenizer.errors) {
            assertEquals(expectedError.getMessage(), error.getMessage());
            assertEquals(expectedError.getLine(), error.getLine());
            assertEquals(expectedError.getColumn(), error.getColumn());
        }

        assertNull(tokenizer.read());
    }
}