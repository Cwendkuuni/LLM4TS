package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertNull(token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        assertNull(doctype.reset().getName());
        assertNull(doctype.reset().getPubSysKey());
        assertNull(doctype.reset().getPublicIdentifier());
        assertNull(doctype.reset().getSystemIdentifier());
        assertFalse(doctype.reset().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        assertNull(tag.reset().name());
        assertNull(tag.reset().normalName());
        assertNull(tag.reset().getAttributes());
        assertFalse(tag.reset().isSelfClosing());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.reset().nameAttr("", null).name());
        assertNull(startTag.reset().nameAttr("", null).normalName());
        assertNull(startTag.reset().nameAttr("", null).getAttributes());
        assertFalse(startTag.reset().nameAttr("", null).isSelfClosing());
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        assertNull(endTag.reset().name());
        assertNull(endTag.reset().normalName());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        assertNull(comment.reset().getData());
        assertFalse(comment.reset().bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        assertNull(character.reset().getData());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        assertNull(character.data(""));
    }

    @Test
    public void testCData() {
        Token.CData cData = new Token.CData("");
        assertEquals("<![CDATA[]]>", cData.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        assertSame(eof, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertFalse(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals(doctype, doctype.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertFalse(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertEquals(startTag, startTag.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertFalse(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertEquals(endTag, endTag.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertFalse(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        assertEquals(comment, comment.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertFalse(token.isCharacter());
    }

    @Test
    public void testIsCData() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertFalse(token.isCData());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        assertEquals(character, character.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertFalse(token.isEOF());
    }

    public void testToken() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertNotNull(token);
    }

    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertNotNull(doctype);
    }

    public void testTag() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        assertNotNull(tag);
    }

    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertNotNull(startTag);
    }

    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertNotNull(endTag);
    }

    public void testComment() {
        Token.Comment comment = new Token.Comment();
        assertNotNull(comment);
    }

    public void testCharacter() {
        Token.Character character = new Token.Character();
        assertNotNull(character);
    }

    public void testCData() {
        Token.CData cData = new Token.CData("");
        assertNotNull(cData);
    }

    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertNotNull(eof);
    }
}