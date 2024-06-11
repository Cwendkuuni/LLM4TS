import org.junit.Test;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.Doctype;
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
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "testTag";
        tag.normalName = "testTag";
        tag.pendingAttributeName = "testAttribute";
        tag.pendingAttributeValue.append("testValue");
        tag.pendingAttributeValueS = "testValue";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new org.jsoup.nodes.Attributes();
        
        tag.reset();
        
        assert(tag.tagName == null);
        assert(tag.normalName == null);
        assert(tag.pendingAttributeName == null);
        assert(tag.pendingAttributeValue.length() == 0);
        assert(tag.pendingAttributeValueS == null);
        assert(tag.hasEmptyAttributeValue == false);
        assert(tag.hasPendingAttributeValue == false);
        assert(tag.selfClosing == false);
        assert(tag.attributes == null);
    }
    
    @Test
    public void testStartTagNameAttr() {
        Token.StartTag tag = new Token.StartTag();
        org.jsoup.nodes.Attributes attrs = new org.jsoup.nodes.Attributes();
        attrs.put("testAttribute", "testValue");
        tag.nameAttr("testTag", attrs);
        
        assert(tag.tagName.equals("testTag"));
        assert(tag.normalName.equals("testtag"));
        assert(tag.attributes.size() == 1);
        assert(tag.attributes.get("testAttribute").equals("testValue"));
    }
    
    @Test
    public void testTagToString() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "testTag";
        tag.attributes = new org.jsoup.nodes.Attributes();
        tag.attributes.put("testAttribute", "testValue");
        
        assert(tag.toString().equals("<testTag testAttribute=\"testValue\">"));
    }
    
    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("testData");
        
        assert(character.getData().equals("testData"));
        assert(character.toString().equals("testData"));
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        eof.reset();
        
        assert(eof.type == Token.TokenType.EOF);
    }
    
    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.Doctype;
        
        assert(token.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        assert(doctype.equals(doctype.asDoctype()));
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.StartTag;
        
        assert(token.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        assert(startTag.equals(startTag.asStartTag()));
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.EndTag;
        
        assert(token.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        assert(endTag.equals(endTag.asEndTag()));
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.Comment;
        
        assert(token.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        
        assert(comment.equals(comment.asComment()));
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.Character;
        
        assert(token.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        
        assert(character.equals(character.asCharacter()));
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.EOF;
        
        assert(token.isEOF());
    }
    
    @Test
    public void testTokenTypeEnum() {
        assert(Token.TokenType.Doctype.equals(Token.TokenType.valueOf("Doctype")));
        assert(Token.TokenType.StartTag.equals(Token.TokenType.valueOf("StartTag")));
        assert(Token.TokenType.EndTag.equals(Token.TokenType.valueOf("EndTag")));
        assert(Token.TokenType.Comment.equals(Token.TokenType.valueOf("Comment")));
        assert(Token.TokenType.Character.equals(Token.TokenType.valueOf("Character")));
        assert(Token.TokenType.EOF.equals(Token.TokenType.valueOf("EOF")));
    }
}