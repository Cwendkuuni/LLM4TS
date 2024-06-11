package org.jsoup.parser;

import static org.junit.Assert.*;
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
    
    assertEquals("Token", token.tokenType());
  }

  @Test
  public void testDoctypeReset() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.name.append("test");
    doctype.publicIdentifier.append("test");
    doctype.systemIdentifier.append("test");
    doctype.forceQuirks = true;
    
    doctype.reset();
    
    assertEquals("", doctype.name.toString());
    assertEquals("", doctype.publicIdentifier.toString());
    assertEquals("", doctype.systemIdentifier.toString());
    assertEquals(false, doctype.forceQuirks);
  }

  @Test
  public void testDoctypeGetters() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.name.append("test");
    doctype.publicIdentifier.append("test");
    doctype.systemIdentifier.append("test");
    doctype.forceQuirks = true;
    
    assertEquals("test", doctype.getName());
    assertEquals("test", doctype.getPublicIdentifier());
    assertEquals("test", doctype.getSystemIdentifier());
    assertEquals(true, doctype.isForceQuirks());
  }

  @Test
  public void testAttributeReset() {
    Token.Tag tag = new Token.Tag() {
        @Override
        Tag reset() {
            return null;
        }
    };
    tag.tagName = "test";
    tag.normalName = "test";
    tag.pendingAttributeName = "test";
    tag.pendingAttributeValue.append("test");
    tag.pendingAttributeValueS = "test";
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
    assertEquals(false, tag.hasEmptyAttributeValue);
    assertEquals(false, tag.hasPendingAttributeValue);
    assertEquals(false, tag.selfClosing);
    assertNull(tag.attributes);
  }

  @Test
  public void testAttributeMethods() {
    Token.Tag tag = new Token.Tag() {
        @Override
        Tag reset() {
            return null;
        }
    };
    tag.tagName = "test";
    tag.normalName = "test";
    tag.pendingAttributeName = "test";
    tag.pendingAttributeValue.append("test");
    tag.pendingAttributeValueS = "test";
    tag.hasEmptyAttributeValue = true;
    tag.hasPendingAttributeValue = true;
    tag.selfClosing = true;
    tag.attributes = new Attributes();
    
    tag.newAttribute();
    
    assertNull(tag.pendingAttributeName);
    assertFalse(tag.hasEmptyAttributeValue);
    assertFalse(tag.hasPendingAttributeValue);
    assertEquals("", tag.pendingAttributeValue.toString());
    assertNull(tag.pendingAttributeValueS);
  }

  @Test
  public void testTagName() {
    Token.Tag tag = new Token.Tag() {
        @Override
        Tag reset() {
            return null;
        }
    };
    tag.tagName = "test";
    tag.normalName = "test";
    
    assertEquals("test", tag.name());
    assertEquals("test", tag.normalName());
    
    tag.name("test2");
    
    assertEquals("test2", tag.tagName);
    assertEquals("test2", tag.normalName);
  }

  @Test
  public void testAttributeValue() {
    Token.Tag tag = new Token.Tag() {
        @Override
        Tag reset() {
            return null;
        }
    };
    tag.ensureAttributeValue();
    
    assertFalse(tag.hasPendingAttributeValue);
    assertNull(tag.pendingAttributeValueS);
    
    tag.pendingAttributeValueS = "test";
    tag.ensureAttributeValue();
    
    assertTrue(tag.hasPendingAttributeValue);
    assertEquals("", tag.pendingAttributeValueS);
    assertEquals("", tag.pendingAttributeValue.toString());
    
    tag.appendAttributeValue("append");
    
    assertFalse(tag.hasPendingAttributeValue);
    assertNull(tag.pendingAttributeValueS);
    assertEquals("append", tag.pendingAttributeValue.toString());
  }

  @Test
  public void testEmptyAttributeValue() {
    Token.Tag tag = new Token.Tag() {
        @Override
        Tag reset() {
            return null;
        }
    };
    tag.hasEmptyAttributeValue = true;
    
    tag.setEmptyAttributeValue();
    
    assertEquals(false, tag.hasEmptyAttributeValue);
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
    
    assertNull(startTag.tagName);
    assertNull(startTag.normalName);
    assertNull(startTag.pendingAttributeName);
    assertEquals("", startTag.pendingAttributeValue.toString());
    assertNull(startTag.pendingAttributeValueS);
    assertEquals(false, startTag.hasEmptyAttributeValue);
    assertEquals(false, startTag.hasPendingAttributeValue);
    assertEquals(false, startTag.selfClosing);
    assertNull(startTag.attributes);
  }

  @Test
  public void testStartTagToString() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.tagName = "test";
    startTag.normalName = "test";
    startTag.attributes = new Attributes();
    
    assertEquals("<test>", startTag.toString());
    
    startTag.attributes.put("attr1", "value1");
    
    assertEquals("<test attr1=\"value1\">", startTag.toString());
  }

  @Test
  public void testEndTagToString() {
    Token.EndTag endTag = new Token.EndTag();
    endTag.tagName = "test";
    
    assertEquals("</test>", endTag.toString());
  }

  @Test
  public void testCommentReset() {
    Token.Comment comment = new Token.Comment();
    comment.data.append("test");
    comment.bogus = true;
    
    comment.reset();
    
    assertEquals("", comment.data.toString());
    assertEquals(false, comment.bogus);
  }

  @Test
  public void testCommentGetData() {
    Token.Comment comment = new Token.Comment();
    comment.data.append("test");
    
    assertEquals("test", comment.getData());
  }

  @Test
  public void testCommentToString() {
    Token.Comment comment = new Token.Comment();
    comment.data.append("test");
    
    assertEquals("<!--test-->", comment.toString());
  }

  @Test
  public void testCharacterReset() {
    Token.Character character = new Token.Character();
    character.data = "test";
    
    character.reset();
    
    assertNull(character.data);
  }

  @Test
  public void testCharacterGetData() {
    Token.Character character = new Token.Character();
    character.data = "test";
    
    assertEquals("test", character.getData());
  }

  @Test
  public void testCharacterToString() {
    Token.Character character = new Token.Character();
    character.data = "test";
    
    assertEquals("test", character.toString());
  }

  @Test
  public void testEOFReset() {
    Token.EOF eof = new Token.EOF();
    
    eof.reset();
    
    assertNotNull(eof);
  }

}