package org.jsoup.select;

import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "input[type='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Attribute("input", new String[]{"type"}, new String[]{"text"}));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Class("class1"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("id1"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseMultipleSelectors() {
        String query = "div.class1#id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Class("class1"));
        expectedEvaluators.add(new Evaluator.Id("id1"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseDescendantSelector() {
        String query = "div p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.Descendant(new Evaluator.Tag("div"), new Evaluator.Tag("p")));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseChildSelector() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.Child(new Evaluator.Tag("div"), new Evaluator.Tag("p")));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParsePrevSiblingSelector() {
        String query = "+p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("p")));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseAdjacentSiblingSelector() {
        String query = "p~div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.AdjacentSibling(new Evaluator.Tag("div"), new Evaluator.Tag("p")));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseNegatedSelector() {
        String query = ":not(.class1)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.Not(new Evaluator.Class("class1")));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseNthChildSelector() {
        String query = "li:nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthChild(2, 0));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseNthLastChildSelector() {
        String query = "li:nth-last-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthLastChild(2, 0));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseNthOfTypeSelector() {
        String query = "li:nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthOfType(2, 0));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseNthLastOfTypeSelector() {
        String query = "li:nth-last-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.IsNthLastOfType(2, 0));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseElementWithAttributeSelector() {
        String query = "input[type='text'][name='username']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValue(new String[]{"input"}, new String[]{"type"}, new String[]{"text"}));
        expectedEvaluators.add(new Evaluator.AttributeWithValue(new String[]{"input"}, new String[]{"name"}, new String[]{"username"}));
        Evaluator attributeEvaluator = expectedEvaluators.get(0);
        Evaluator valueEvaluator = expectedEvaluators.get(1);
        AttributeMatcher attributeMatcher = (AttributeMatcher) attributeEvaluator.getMatcher();
        AttributeMatcher valueMatcher = (AttributeMatcher) valueEvaluator.getMatcher();
        assertEquals(attributeMatcher.getAttributeName(), "type");
        assertEquals(attributeMatcher.getAttributeValue(), "text");
        assertEquals(valueMatcher.getAttributeName(), "name");
        assertEquals(valueMatcher.getAttributeValue(), "username");
        assertEquals(evaluator.getChildren().size(), 2);
        assertEquals(evaluator.getChildren().get(0), attributeEvaluator);
        assertEquals(evaluator.getChildren().get(1), valueEvaluator);
    }

    @Test
    public void testParseElementWithClassAttributeSelector() {
        String query = "div.class1.class2";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Class("class1"));
        expectedEvaluators.add(new Evaluator.Class("class2"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseElementWithIDAttributeSelector() {
        String query = "div#id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("id1"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseElementWithMultipleAttributesSelector() {
        String query = "input[type='text'][name='username'][value='password']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValue(new String[]{"input"}, new String[]{"type"}, new String[]{"text"}));
        expectedEvaluators.add(new Evaluator.AttributeWithValue(new String[]{"input"}, new String[]{"name"}, new String[]{"username"}));
        expectedEvaluators.add(new Evaluator.AttributeWithValue(new String[]{"input"}, new String[]{"value"}, new String[]{"password"}));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParsePseudoElementSelector() {
        String query = "input:checked";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.PseudoElement("input", ":checked"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = "a:hover";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.PseudoClass("a", ":hover"));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseSubSelector() {
        String query = "ul > li > a";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.Child(new Evaluator.Tag("ul"), new StructuralEvaluator.Child(new Evaluator.Tag("li"), new Evaluator.Tag("a"))));
        assertEquals(expectedEvaluators, evaluator.getChildren());
    }
}