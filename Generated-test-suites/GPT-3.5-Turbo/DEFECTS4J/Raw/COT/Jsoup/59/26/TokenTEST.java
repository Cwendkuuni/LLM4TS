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
    doctype.name.append("test");
    doctype.pubSysKey = "test";
    doctype.publicIdentifier.append("test");
    doctype.systemIdentifier.append("test");
    doctype.forceQuirks = true;

    doctype.reset();

    assertEquals("", doctype.name.toString());
    assertNull(doctype.pubSysKey);
    assertEquals("", doctype.publicIdentifier.toString());
    assertEquals("", doctype.systemIdentifier.toString());
    assertFalse(doctype.forceQuirks);
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
    assertFalse(startTag.hasEmptyAttributeValue);
    assertFalse(startTag.hasPendingAttributeValue);
    assertFalse(startTag.selfClosing);
    assertNull(startTag.attributes);
  }

  @Test
  public void testEndTagReset() {
    Token.EndTag endTag = new Token.EndTag();
    endTag.tagName = "test";
    endTag.normalName = "test";
    endTag.pendingAttributeName = "test";
    endTag.pendingAttributeValue.append("test");
    endTag.pendingAttributeValueS = "test";
    endTag.hasEmptyAttributeValue = true;
    endTag.hasPendingAttributeValue = true;
    endTag.selfClosing = true;
    endTag.attributes = new Attributes();

    endTag.reset();

    assertNull(endTag.tagName);
    assertNull(endTag.normalName);
    assertNull(endTag.pendingAttributeName);
    assertEquals("", endTag.pendingAttributeValue.toString());
    assertNull(endTag.pendingAttributeValueS);
    assertFalse(endTag.hasEmptyAttributeValue);
    assertFalse(endTag.hasPendingAttributeValue);
    assertFalse(endTag.selfClosing);
    assertNull(endTag.attributes);
  }

  @Test
  public void testCommentReset() {
    Token.Comment comment = new Token.Comment();
    comment.data.append("test");
    comment.bogus = true;

    comment.reset();

    assertEquals("", comment.data.toString());
    assertFalse(comment.bogus);
  }

  @Test
  public void testCharacterReset() {
    Token.Character character = new Token.Character();
    character.data = "test";

    character.reset();

    assertNull(character.data);
  }

  @Test
  public void testEOFReset() {
    Token.EOF eof = new Token.EOF();

    eof.reset();

    assertSame(eof, eof.reset());
  }

  @Test
  public void testDoctypeGetName() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.name.append("test");
    assertEquals("test", doctype.getName());
  }

  @Test
  public void testDoctypeGetPubSysKey() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.pubSysKey = "test";
    assertEquals("test", doctype.getPubSysKey());
  }

  @Test
  public void testDoctypeGetPublicIdentifier() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.publicIdentifier.append("test");
    assertEquals("test", doctype.getPublicIdentifier());
  }

  @Test
  public void testDoctypeGetSystemIdentifier() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.systemIdentifier.append("test");
    assertEquals("test", doctype.getSystemIdentifier());
  }

  @Test
  public void testDoctypeIsForceQuirks() {
    Token.Doctype doctype = new Token.Doctype();
    assertFalse(doctype.isForceQuirks());

    doctype.forceQuirks = true;
    assertTrue(doctype.isForceQuirks());
  }

  @Test
  public void testStartTagName() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.tagName = "test";
    assertEquals("test", startTag.name());
  }

  @Test
  public void testStartTagNormalName() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.tagName = "Test";
    assertEquals("test", startTag.normalName());
  }

  @Test
  public void testStartTagIsSelfClosing() {
    Token.StartTag startTag = new Token.StartTag();
    assertFalse(startTag.isSelfClosing());

    startTag.selfClosing = true;
    assertTrue(startTag.isSelfClosing());
  }

  @Test
  public void testStartTagGetAttributes() {
    Token.StartTag startTag = new Token.StartTag();
    assertNotNull(startTag.getAttributes());
  }

  @Test
  public void testEndTagName() {
    Token.EndTag endTag = new Token.EndTag();
    endTag.tagName = "test";
    assertEquals("test", endTag.name());
  }

  @Test
  public void testCommentGetData() {
    Token.Comment comment = new Token.Comment();
    comment.data.append("test");
    assertEquals("test", comment.getData());
  }

  @Test
  public void testCharacterGetData() {
    Token.Character character = new Token.Character();
    character.data = "test";
    assertEquals("test", character.getData());
  }

  @Test
  public void testTokenTypeIsDoctype() {
    Token token = new Token() {
      @Override
      Token reset() {
        return null;
      }
    };
    assertFalse(token.isDoctype());

    Token.Doctype doctype = new Token.Doctype();
    token = doctype;
    assertTrue(token.isDoctype());
  }

  @Test
  public void testTokenTypeIsStartTag() {
    Token token = new Token() {
      @Override
      Token reset() {
        return null;
      }
    };
    assertFalse(token.isStartTag());

    Token.StartTag startTag = new Token.StartTag();
    token = startTag;
    assertTrue(token.isStartTag());
  }

  @Test
  public void testTokenTypeIsEndTag() {
    Token token = new Token() {
      @Override
      Token reset() {
        return null;
      }
    };
    assertFalse(token.isEndTag());

    Token.EndTag endTag = new Token.EndTag();
    token = endTag;
    assertTrue(token.isEndTag());
  }

  @Test
  public void testTokenTypeIsComment() {
    Token token = new Token() {
      @Override
      Token reset() {
        return null;
      }
    };
    assertFalse(token.isComment());

    Token.Comment comment = new Token.Comment();
    token = comment;
    assertTrue(token.isComment());
  }

  @Test
  public void testTokenTypeIsCharacter() {
    Token token = new Token() {
      @Override
      Token reset() {
        return null;
      }
    };
    assertFalse(token.isCharacter());

    Token.Character character = new Token.Character();
    token = character;
    assertTrue(token.isCharacter());
  }

  @Test
  public void testTokenTypeIsEOF() {
    Token token = new Token() {
      @Override
      Token reset() {
        return null;
      }
    };
    assertFalse(token.isEOF());

    Token.EOF eof = new Token.EOF();
    token = eof;
    assertTrue(token.isEOF());
  }
}