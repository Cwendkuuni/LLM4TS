package org.jsoup.parser;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        assert(token.tokenType().equals("Token"));
    }
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        
        doctype.name.append("test");
        doctype.publicIdentifier.append("test");
        doctype.systemIdentifier.append("test");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assert(doctype.getName().equals(""));
        assert(doctype.getPublicIdentifier().equals(""));
        assert(doctype.getSystemIdentifier().equals(""));
        assert(doctype.isForceQuirks() == false);
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        
        startTag.tagName = "test";
        startTag.normalName = "test";
        startTag.pendingAttributeName = "test";
        startTag.pendingAttributeValue.append("test");
        startTag.pendingAttributeValueS = "test";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        startTag.reset();
        
        assert(startTag.tagName == null);
        assert(startTag.normalName == null);
        assert(startTag.pendingAttributeName == null);
        assert(startTag.pendingAttributeValue.toString().equals(""));
        assert(startTag.pendingAttributeValueS == null);
        assert(startTag.hasEmptyAttributeValue == false);
        assert(startTag.hasPendingAttributeValue == false);
        assert(startTag.selfClosing == false);
        assert(startTag.attributes == null);
    }
    
    @Test
    public void testStartTagNewAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        
        Attribute attribute = mock(Attribute.class);
        
        startTag.attributes = new Attributes();
        startTag.pendingAttributeName = "test";
        startTag.pendingAttributeValue.append("test");
        startTag.hasPendingAttributeValue = true;
        
        doReturn(attribute).when(attribute).put(any(Attribute.class));
        
        startTag.newAttribute();
        
        verify(attribute).put(any(Attribute.class));
        assert(startTag.pendingAttributeName == null);
        assert(startTag.pendingAttributeValue.toString().equals(""));
        assert(startTag.hasPendingAttributeValue == false);
        assert(startTag.attributes != null);
    }
    
    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        
        endTag.tagName = "test";
        endTag.normalName = "test";
        
        endTag.reset();
        
        assert(endTag.tagName == null);
        assert(endTag.normalName == null);
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        
        comment.data.append("test");
        comment.bogus = true;
        
        comment.reset();
        
        assert(comment.getData().equals(""));
        assert(comment.bogus == false);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        
        character.data = "test";
        
        character.reset();
        
        assert(character.data == null);
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
        
        assert(eof != null);
    }
    
    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        token.type = TokenType.Doctype;
        
        assert(token.isDoctype() == true);
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        token.type = TokenType.StartTag;
        
        assert(token.isStartTag() == true);
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        token.type = TokenType.EndTag;
        
        assert(token.isEndTag() == true);
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        token.type = TokenType.Comment;
        
        assert(token.isComment() == true);
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        token.type = TokenType.Character;
        
        assert(token.isCharacter() == true);
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        token.type = TokenType.EOF;
        
        assert(token.isEOF() == true);
    }
}