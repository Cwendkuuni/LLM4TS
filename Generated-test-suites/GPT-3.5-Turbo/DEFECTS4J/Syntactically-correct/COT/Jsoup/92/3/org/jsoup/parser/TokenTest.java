
package org.jsoup.parser;

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
        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;
        doctype.reset();
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        tag.tagName = "tagName";
        tag.normalName = "normalName";
        tag.pendingAttributeName = "pendingAttributeName";
        tag.pendingAttributeValue.append("pendingAttributeValue");
        tag.pendingAttributeValueS = "pendingAttributeValueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();
        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tagName";
        startTag.attributes = new Attributes();
        assertEquals("<tagName>", startTag.toString());
        startTag.attributes.put("attr1", "value1");
        startTag.attributes.put("attr2", "value2");
        assertEquals("<tagName attr1=\"value1\" attr2=\"value2\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tagName";
        assertEquals("</tagName>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;
        comment.reset();
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";
        character.reset();
        assertNull(character.data);
    }

    @Test
    public void testCDataToString() {
        Token.CData cData = new Token.CData("data");
        assertEquals("<![CDATA[data]]>", cData.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        eof.reset();
        assertEquals(eof, eof.reset());
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

        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
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

        Token.StartTag startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());
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

        Token.EndTag endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());
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

        Token.Comment comment = new Token.Comment();
        assertTrue(comment.isComment());
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

        Token.Character character = new Token.Character();
        assertTrue(character.isCharacter());
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

        Token.CData cData = new Token.CData("data");
        assertTrue(cData.isCData());
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

        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

}
