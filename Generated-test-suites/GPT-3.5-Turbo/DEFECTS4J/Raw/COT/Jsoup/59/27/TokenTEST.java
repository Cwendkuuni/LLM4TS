import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;

public class TokenTest {
    
    // initialize Token object for testing
    Token token;
    
    @Before
    public void setUp() {
        token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
    }
    
    @Test
    public void testTokenType() {
        assert(token.tokenType().equals(token.getClass().getSimpleName()));
    }
    
    // Test cases for Doctype class
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("W3C");
        doctype.systemIdentifier.append("SYSTEM");
        doctype.forceQuirks = true;

        doctype.reset();
        
        assert(doctype.name.length() == 0);
        assert(doctype.pubSysKey == null);
        assert(doctype.publicIdentifier.length() == 0);
        assert(doctype.systemIdentifier.length() == 0);
        assert(!doctype.forceQuirks);
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");

        assert(doctype.getName().equals("HTML"));
    }
    
    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.pubSysKey = "PUBLIC";

        assert(doctype.getPubSysKey().equals("PUBLIC"));
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("W3C");

        assert(doctype.getPublicIdentifier().equals("W3C"));
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("SYSTEM");

        assert(doctype.getSystemIdentifier().equals("SYSTEM"));
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        assert(doctype.isForceQuirks());
    }
    
    // Test cases for Tag class
    
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        
        tag.tagName = "tag";
        tag.normalName = "TAG";
        tag.pendingAttributeName = "attr";
        tag.pendingAttributeValue.append("value");
        tag.pendingAttributeValueS = "valueS";
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
        assert(!tag.hasEmptyAttributeValue);
        assert(!tag.hasPendingAttributeValue);
        assert(!tag.selfClosing);
        assert(tag.attributes == null);
    }
    
    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.Tag() {
            @Override
            void newAttribute() {
                return;
            }
        };
        
        tag.pendingAttributeName = "attr";
        tag.pendingAttributeValue.append("value");
        tag.pendingAttributeValueS = "valueS";
        tag.hasPendingAttributeValue = true;
        
        tag.newAttribute();
        
        assert(tag.attributes != null);
        assert(tag.pendingAttributeName == null);
        assert(tag.pendingAttributeValue.length() == 0);
        assert(tag.pendingAttributeValueS == null);
        assert(!tag.hasPendingAttributeValue);
    }
    
    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.Tag() {
            @Override
            void newAttribute() {
                return;
            }
            
            @Override
            void finaliseTag() {
                return;
            }
        };
        
        tag.pendingAttributeName = "attr";
        
        tag.finaliseTag();
        
        assert(tag.pendingAttributeName == null);
    }
    
    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag() {
            @Override
            String name() {
                return "TAG";
            }
        };
        
        assert(tag.name().equals("TAG"));
    }
    
    @Test
    public void testNormalName() {
        Token.Tag tag = new Token.Tag() {
            @Override
            String normalName() {
                return "tag";
            }
        };
        
        assert(tag.normalName().equals("tag"));
    }
    
    @Test
    public void testIsSelfClosing() {
        Token.Tag tag = new Token.Tag() {
            @Override
            boolean isSelfClosing() {
                return true;
            }
        };
        
        assert(tag.isSelfClosing());
    }
    
    // Test cases for Comment class
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Comment");
        comment.bogus = true;

        comment.reset();
        
        assert(comment.data.length() == 0);
        assert(!comment.bogus);
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Comment");

        assert(comment.getData().equals("Comment"));
    }
    
    // Test cases for Character class
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "Character";

        character.reset();
        
        assert(character.data == null);
    }
    
    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("Character");

        assert(character.getData().equals("Character"));
    }
    
    // Test cases for EOF class
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();
        
        assert(eof == eof.reset());
    }
}