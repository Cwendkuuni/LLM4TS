import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder builder;
    
    @Test
    public void testDefaultSettings() {
        builder = new XmlTreeBuilder();
        assertNotNull(builder.defaultSettings());
    }
    
    @Test
    public void testInitialiseParse() {
        builder = new XmlTreeBuilder();
        builder.initialiseParse(null, null, null);
        assertNotNull(builder.stack);
        assertEquals(1, builder.stack.size());
        assertNotNull(builder.doc.outputSettings());
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testParseReaderString() {
        builder = new XmlTreeBuilder();
        assertNotNull(builder.parse(null, null));
    }
    
    @Test
    public void testParseStringString() {
        builder = new XmlTreeBuilder();
        assertNotNull(builder.parse(null, null));
    }
    
    @Test
    public void testProcessStartTag() {
        builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", null);
        assertNotNull(builder.insert(startTag));
    }
    
    @Test
    public void testProcessEndTag() {
        builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.stack.add(new Element(Tag.valueOf("tag")));
        builder.popStackToClose(endTag);
        assertTrue(builder.stack.isEmpty());
    }
    
    @Test
    public void testProcessComment() {
        builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        assertNotNull(builder.insert(comment));
    }
    
    @Test
    public void testProcessCharacter() {
        builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("character");
        assertNotNull(builder.insert(character));
    }
    
    @Test
    public void testProcessDoctype() {
        builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("doctype");
        assertNotNull(builder.insert(doctype));
    }
    
    @Test
    public void testPopStackToClose() {
        builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("tag")));
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.popStackToClose(endTag);
        assertTrue(builder.stack.isEmpty());
    }
    
    @Test
    public void testParseFragmentStringStringParser() {
        builder = new XmlTreeBuilder();
        assertNotNull(builder.parseFragment(null, null, null));
    }
    
    @Test
    public void testParseFragmentStringElementStringParser() {
        builder = new XmlTreeBuilder();
        assertNotNull(builder.parseFragment(null, null, null, null));
    }
}