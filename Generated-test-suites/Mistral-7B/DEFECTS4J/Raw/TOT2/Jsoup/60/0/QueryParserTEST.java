import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Expected empty Evaluator tree", evaluator, new Evaluator.Empty());
    }

    @Test
    public void testParseTagSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue("Expected Evaluator for tag selector 'div'", evaluator instanceof Evaluator.Tag);
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue("Expected Evaluator for class selector '.class1'", evaluator instanceof Evaluator.Class);
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id1";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue("Expected Evaluator for id selector '#id1'", evaluator instanceof Evaluator.Id);
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[title='test']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue("Expected Evaluator for attribute selector '[title='test']'", evaluator instanceof Evaluator.Attribute);
    }

    @Test
    public void testParseComplexSelector() {
        String query = "div.class1#id1 > ul > li[title='test']:contains(hello)";
        Evaluator evaluator = QueryParser.parse(query);
        // add assertions for the structure of the Evaluator tree here
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = "div#id1 > ul > #id1"; // id selector cannot be used multiple times in the same query
        Evaluator evaluator = QueryParser.parse(query);
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.class1, ul li";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue("Expected Evaluator for combined selectors 'div.class1, ul li'", evaluator instanceof Evaluator.Or);
    }

    @Test
    public void testParseDescendantSelector() {
        String query = "div > ul > li";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue("Expected Evaluator for descendant selector 'div > ul > li'", evaluator instanceof Evaluator.Descendant);
    }

    @Test
    public void testParseAdjacentSiblingSelector() {
        String query = "div > ul > li + p";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue("Expected Evaluator for adjacent sibling selector 'div > ul > li + p'", evaluator instanceof Evaluator.AdjacentSibling);
    }

    @Test
    public void testParseGeneralSiblingSelector() {
        String query = "div > ul > li ~ p";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue("Expected Evaluator for general sibling selector 'div > ul > li ~ p'", evaluator instanceof Evaluator.GeneralSibling);
    }

    @Test
    public void testParsePseudoClasses() {
        String query = "a:link, a:visited";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue("Expected Evaluator for pseudo-classes 'a:link, a:visited'", evaluator instanceof Evaluator.PseudoClass);
    }

    @Test
    public void testParsePseudoElements() {
        String query = "p::first-line";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue("Expected Evaluator for pseudo-element 'p::first-line'", evaluator instanceof Evaluator.PseudoElement);
    }

    @Test
    public void testParseNthChildSelectors() {
        String query = "li:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue("Expected Evaluator for nth-child selector 'li:nth-child(2)'", evaluator instanceof Evaluator.IsNthChild);

        String query2 = "li:nth-last-child(2)";
        Evaluator evaluator2 = QueryParser.parse(query2);
        Assert.assertTrue("Expected Evaluator for nth-last-child selector 'li:nth-last-child(2)'", evaluator2 instanceof Evaluator.IsNthLastChild);
    }

    @Test
    public void testParseAttributeValues() {
        String query = "input[type='text']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertTrue("Expected Evaluator for attribute selector 'input[type='text']'", evaluator instanceof Evaluator.Attribute);

        String query2 = "input[type='text'][value='username']";
        Evaluator evaluator2 = QueryParser.parse(query2);
        Assert.assertTrue("Expected Evaluator for attribute selector 'input[type='text'][value='username']'", evaluator2 instanceof Evaluator.AttributeWithValue);
    }
}