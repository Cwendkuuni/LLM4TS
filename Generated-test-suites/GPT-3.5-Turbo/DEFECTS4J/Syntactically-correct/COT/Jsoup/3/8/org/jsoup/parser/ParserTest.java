import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser("", "", false);
    }

    @Test
    public void testParse() {
        Document document = parser.parse();
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
    }

    @Test
    public void testParseEndTag() {
        parser.parseEndTag();
    }

    @Test
    public void testParseStartTag() {
        parser.parseStartTag();
    }

    @Test
    public void testParseAttribute() {
        parser.parseAttribute();
    }

    @Test
    public void testParseTextNode() {
        parser.parseTextNode();
    }

    @Test
    public void testParseCdata() {
        parser.parseCdata();
    }

    @Test
    public void testAddChildToParent() {
        parser.addChildToParent(null, false);
    }

    @Test
    public void testStackHasValidParent() {
        boolean validParent = parser.stackHasValidParent(null);
        Assert.assertFalse(validParent);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        parser.popStackToSuitableContainer(null);
    }

    @Test
    public void testPopStackToClose() {
        parser.popStackToClose(null);
    }

    @Test
    public void testLast() {
        parser.last();
    }
}