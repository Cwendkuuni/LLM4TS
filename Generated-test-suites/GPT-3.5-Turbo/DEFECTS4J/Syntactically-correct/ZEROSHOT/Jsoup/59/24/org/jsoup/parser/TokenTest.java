package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {
  
  @Test
  public void testTokenType() {
    Token token = new Token() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertEquals(TokenType.Doctype, token.type);
  }
  
  @Test
  public void testTokenTypeDoctype() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.reset();
    
    assertEquals("", doctype.getName());
    assertEquals(null, doctype.getPubSysKey());
    assertEquals("", doctype.getPublicIdentifier());
    assertEquals("", doctype.getSystemIdentifier());
    assertEquals(false, doctype.isForceQuirks());
  }
  
  @Test
  public void testTokenAttribute() {
    Token.Tag tag = new Token.Tag() {
      @Override
      Tag reset() {
        return this;
      }
    };
    
    assertEquals(null, tag.tagName);
    assertEquals(null, tag.normalName);
    assertEquals(null, tag.pendingAttributeName);
    assertEquals("", tag.pendingAttributeValue.toString());
    assertEquals(null, tag.pendingAttributeValueS);
    assertEquals(false, tag.hasEmptyAttributeValue);
    assertEquals(false, tag.hasPendingAttributeValue);
    assertEquals(false, tag.selfClosing);
    assertEquals(null, tag.attributes);
  }
  
  @Test
  public void testTokenStartTag() {
    Token.StartTag startTag = new Token.StartTag() {
      @Override
      Tag reset() {
        return this;
      }
    };
    
    assertEquals("", startTag.name());
    assertEquals("", startTag.normalName());
    assertEquals("", startTag.toString());
  }
  
  @Test
  public void testTokenEndTag() {
    Token.EndTag endTag = new Token.EndTag() {
      @Override
      Tag reset() {
        return this;
      }
    };
    
    assertEquals("", endTag.name());
    assertEquals("", endTag.toString());
  }
  
  @Test
  public void testTokenComment() {
    Token.Comment comment = new Token.Comment() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertEquals("", comment.getData());
    assertEquals("", comment.toString());
  }
  
  @Test
  public void testTokenCharacter() {
    Token.Character character = new Token.Character() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertEquals(null, character.getData());
    assertEquals("", character.toString());
  }
  
  @Test
  public void testTokenEOF() {
    Token.EOF eof = new Token.EOF() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertSame(eof, eof.reset());
  }
  
  @Test
  public void testIsDoctype() {
    Token token = new Token() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertFalse(token.isDoctype());
  }
  
  @Test
  public void testAsDoctype() {
    Token token = new Token() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertNotSame(token, token.asDoctype());
  }
  
  @Test
  public void testIsStartTag() {
    Token token = new Token() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertFalse(token.isStartTag());
  }
  
  @Test
  public void testAsStartTag() {
    Token token = new Token() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertNotSame(token, token.asStartTag());
  }
  
  @Test
  public void testIsEndTag() {
    Token token = new Token() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertFalse(token.isEndTag());
  }
  
  @Test
  public void testAsEndTag() {
    Token token = new Token() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertNotSame(token, token.asEndTag());
  }
  
  @Test
  public void testIsComment() {
    Token token = new Token() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertFalse(token.isComment());
  }
  
  @Test
  public void testAsComment() {
    Token token = new Token() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertNotSame(token, token.asComment());
  }
  
  @Test
  public void testIsCharacter() {
    Token token = new Token() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertFalse(token.isCharacter());
  }
  
  @Test
  public void testAsCharacter() {
    Token token = new Token() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertNotSame(token, token.asCharacter());
  }
  
  @Test
  public void testIsEOF() {
    Token token = new Token() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertFalse(token.isEOF());
  }
  
  @Test
  public void testTokenDoctypeReset() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.reset();
    
    assertEquals("", doctype.name.toString());
    assertEquals(null, doctype.pubSysKey);
    assertEquals("", doctype.publicIdentifier.toString());
    assertEquals("", doctype.systemIdentifier.toString());
    assertEquals(false, doctype.forceQuirks);
  }
  
  @Test
  public void testTokenTagReset() {
    Token.Tag tag = new Token.Tag() {
      @Override
      Tag reset() {
        return this;
      }
    };
    tag.reset();
    
    assertEquals(null, tag.tagName);
    assertEquals(null, tag.normalName);
    assertEquals(null, tag.pendingAttributeName);
    assertEquals("", tag.pendingAttributeValue.toString());
    assertEquals(null, tag.pendingAttributeValueS);
    assertEquals(false, tag.hasEmptyAttributeValue);
    assertEquals(false, tag.hasPendingAttributeValue);
    assertEquals(false, tag.selfClosing);
    assertEquals(null, tag.attributes);
  }
  
  @Test
  public void testTokenStartTagReset() {
    Token.StartTag startTag = new Token.StartTag() {
      @Override
      Tag reset() {
        return this;
      }
    };
    startTag.reset();
    
    assertEquals("", startTag.name());
    assertEquals("", startTag.normalName());
  }
  
  @Test
  public void testTokenEndTagReset() {
    Token.EndTag endTag = new Token.EndTag() {
      @Override
      Tag reset() {
        return this;
      }
    };
    endTag.reset();
    
    assertEquals("", endTag.name());
  }
  
  @Test
  public void testTokenCommentReset() {
    Token.Comment comment = new Token.Comment() {
      @Override
      Token reset() {
        return this;
      }
    };
    comment.reset();
    
    assertEquals("", comment.data.toString());
    assertEquals(false, comment.bogus);
  }
  
  @Test
  public void testTokenCharacterReset() {
    Token.Character character = new Token.Character() {
      @Override
      Token reset() {
        return this;
      }
    };
    character.reset();
    
    assertEquals(null, character.data);
  }
  
  @Test
  public void testTokenEOFReset() {
    Token.EOF eof = new Token.EOF() {
      @Override
      Token reset() {
        return this;
      }
    };
    
    assertSame(eof, eof.reset());
  }
  
}