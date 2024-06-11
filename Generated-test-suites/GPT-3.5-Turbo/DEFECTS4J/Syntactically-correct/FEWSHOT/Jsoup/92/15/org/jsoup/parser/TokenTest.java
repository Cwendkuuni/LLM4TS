package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import static org.jsoup.internal.Normalizer.lowerCase;

/**
 * Parse tokens for the Tokeniser.
 */
abstract class TokenTest {
    
    static final class DoctypeTest extends Token {
    
        public void testName() {
            Doctype doctype = new Doctype();
            doctype.name("HTML");
            assertEquals("HTML", doctype.name());
            assertEquals("html", doctype.normalName());
        }
        
        public void testPubSysKey() {
            Doctype doctype = new Doctype();
            doctype.pubSysKey("PUBLIC");
            assertEquals("PUBLIC", doctype.pubSysKey());
        }
        
        public void testPublicIdentifier() {
            Doctype doctype = new Doctype();
            doctype.publicIdentifier("abc");
            assertEquals("abc", doctype.publicIdentifier());
        }
        
        public void testSystemIdentifier() {
            Doctype doctype = new Doctype();
            doctype.systemIdentifier("xyz");
            assertEquals("xyz", doctype.systemIdentifier());
        }
        
        public void testForceQuirks() {
            Doctype doctype = new Doctype();
            doctype.forceQuirks(true);
            assertEquals(true, doctype.isForceQuirks());
        }
        
    }
    
    static abstract class TagTest extends Token {
        
        public void testName() {
            Tag tag = new Tag();
            tag.name("p");
            assertEquals("p", tag.name());
            assertEquals("p", tag.normalName());
        }
        
        public void testSelfClosing() {
            Tag tag = new Tag();
            tag.selfClosing(true);
            assertEquals(true, tag.isSelfClosing());
        }
        
        public void testAttributes() {
            Tag tag = new Tag();
            Attributes attributes = new Attributes();
            attributes.put("id", "123");
            tag.attributes(attributes);
            assertEquals(attributes, tag.getAttributes());
        }
        
    }
    
    static final class StartTagTest extends TagTest {
        
        public void testToString() {
            StartTag startTag = new StartTag();
            startTag.nameAttr("p", new Attributes());
            assertEquals("<p>", startTag.toString());
        }
    }
    
    static final class EndTagTest extends TagTest {
        
        public void testToString() {
            EndTag endTag = new EndTag();
            endTag.name("p");
            assertEquals("</p>", endTag.toString());
        }
    }
    
    static final class CommentTest extends Token {
        
        public void testData() {
            Comment comment = new Comment();
            comment.data("Lorem ipsum");
            assertEquals("Lorem ipsum", comment.getData());
        }
        
        public void testBogus() {
            Comment comment = new Comment();
            comment.bogus(true);
            assertEquals(true, comment.isBogus());
        }
        
        public void testToString() {
            Comment comment = new Comment();
            comment.data("Lorem ipsum");
            assertEquals("<!--Lorem ipsum-->", comment.toString());
        }
    }
    
    static final class CharacterTest extends Token {
        
        public void testData() {
            Character character = new Character();
            character.data("Lorem ipsum");
            assertEquals("Lorem ipsum", character.getData());
        }
        
    }
    
    static final class CDataTest extends CharacterTest {
        
        public void testToString() {
            CData cdata = new CData("Lorem ipsum");
            assertEquals("<![CDATA[Lorem ipsum]]>", cdata.toString());
        }
        
    }
    
    static final class EOFTest extends Token {
        
        public void testReset() {
            EOF eof = new EOF();
            assertEquals(eof, eof.reset());
        }
        
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("org.jsoup.parser.TokenTest");
    }
}