
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Selector;

import java.util.List;

import static org.junit.Assert.*;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        String query = "div#test > p[class=example], span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div#test > [class=example]";
        Evaluator evaluator = QueryParser.parse(query);
    }
    
    @Test
    public void testParseWithCombinators() {
        String query = "div#test > p, span + p, div ~ p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithWhitespaceCombinator() {
        String query = "div#test p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithCommaCombinator() {
        String query = "div#test, p, span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithAttributeSelectors() {
        String query = "div[class=example], p[id*=test], span[data-name^=john]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithMultipleAttributeSelectors() {
        String query = "div[class=example][data-name=test], p[id*=test]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithIdSelector() {
        String query = "#test";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithClassSelector() {
        String query = ".example";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithTagSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithWildcardNamespaceSelector() {
        String query = "*|example";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithNamespaceSelector() {
        String query = "example|test";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithPseudoSelectors() {
        String query = "div:first-child, p:last-child, span:nth-child(3), div:empty";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithComplexSelectors() {
        String query = "div#test > p.class:not(span), span + p:nth-child(2n+1), div ~ p:last-child";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
}

