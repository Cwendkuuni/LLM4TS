
package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attributes;

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

        assertNull(token.type);
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

        assertEquals(doctype.name.toString(), "");
        assertNull(doctype.pubSysKey);
        assertEquals(doctype.publicIdentifier.toString(), "");
        assertEquals(doctype.systemIdentifier.toString(), "");
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
        assertEquals(tag.pendingAttributeValue.toString(), "");
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        startTag.nameAttr("name", attributes);

        String expected = "<name key=\"value\">";
        String actual = startTag.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testStartTagToStringWithoutAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("name", null);

        String expected = "<name>";
        String actual = startTag.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("name");

        String expected = "</name>";
        String actual = endTag.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();

        comment.data.append("data");
        comment.bogus = true;

        comment.reset();

        assertEquals(comment.data.toString(), "");
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();

        character.data("data");

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testCDataToString() {
        Token.CData cData = new Token.CData("data");

        String expected = "<![CDATA[data]]>";
        String actual = cData.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertNotNull(eof);
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
    public void testIsEOF() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        assertFalse(token.isEOF());
    }
}
