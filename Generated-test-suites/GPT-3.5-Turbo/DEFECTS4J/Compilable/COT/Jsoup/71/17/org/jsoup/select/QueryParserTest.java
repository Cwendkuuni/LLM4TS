package org.jsoup.select;
import org.junit.Test;
import org.jsoup.select.Evaluator;

import static org.junit.Assert.assertEquals;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Test query with a combinator
        Evaluator evaluator1 = QueryParser.parse("div p");
        assertEquals("Evaluator for 'div p' is incorrect", "And[Tag(div), Parent(Tag(p))]", evaluator1.toString());
        
        // Test query with multiple combinators
        Evaluator evaluator2 = QueryParser.parse("div > p + span");
        assertEquals("Evaluator for 'div > p + span' is incorrect", "And[Tag(div), ImmediateParent[Tag(p)], ImmediatePreviousSibling[Tag(span)]]", evaluator2.toString());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        // Test parsing an invalid query
        Evaluator evaluator = QueryParser.parse("div p > span");
    }

    @Test
    public void testParseComplexQuery() {
        // Test parsing a complex query with various selectors and pseudo selectors
        Evaluator evaluator = QueryParser.parse("div#myId > p[class=\"myClass\"]:contains('text')");
        assertEquals("Evaluator for 'div#myId > p[class=\"myClass\"]:contains('text')' is incorrect", "And[Tag(div), Id(myId), ImmediateParent[And[Tag(p), AttributeWithValue(class, myClass), ContainsText(text)]]]", evaluator.toString());
    }

    @Test
    public void testParseAttributeSelector() {
        // Test parsing a query with attribute selector
        Evaluator evaluator = QueryParser.parse("[name=value]");
        assertEquals("Evaluator for '[name=value]' is incorrect", "AttributeWithValue(name, value)", evaluator.toString());
    }

    @Test
    public void testParsePseudoSelector() {
        // Test parsing a query with pseudo selector
        Evaluator evaluator = QueryParser.parse(":first-child");
        assertEquals("Evaluator for ':first-child' is incorrect", "IsFirstChild", evaluator.toString());
    }

    @Test
    public void testParseNthChildPseudoSelector() {
        // Test parsing a query with :nth-child pseudo selector
        Evaluator evaluator = QueryParser.parse(":nth-child(2n+1)");
        assertEquals("Evaluator for ':nth-child(2n+1)' is incorrect", "IsNthChild(2, 1)", evaluator.toString());
    }

}

