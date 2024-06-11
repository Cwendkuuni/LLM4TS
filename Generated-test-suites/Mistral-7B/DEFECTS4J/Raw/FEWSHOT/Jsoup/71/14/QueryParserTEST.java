package org.jsoup.select;

import static org.junit.Assert.*;
import org.jsoup.select.QueryParser;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class1.class2 > p#id1:not(p#id2)";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator rootEval = evaluator;

        while (rootEval instanceof CombiningEvaluator.And) {
            rootEval = ((CombiningEvaluator.And) rootEval).getLeft();
        }

        assertEquals(StructuralEvaluator.Class.class, rootEval.getClass());
        assertEquals("div", ((StructuralEvaluator.Class) rootEval).getElementName());
        assertEquals(new String[]{"class1", "class2"}, ((StructuralEvaluator.Class) rootEval).getClassNames());

        List<Evaluator> children = new ArrayList<>();
        while (evaluator != rootEval) {
            children.add(evaluator);
            evaluator = evaluator.getNextEvaluator();
        }

        assertEquals(3, children.size());

        Evaluator child1 = children.get(0);
        assertEquals(StructuralEvaluator.Descendant.class, child1.getClass());
        assertEquals("p", ((StructuralEvaluator.Descendant) child1).getElementName());

        Evaluator child2 = children.get(1);
        assertEquals(Evaluator.Id.class, child2.getClass());
        assertEquals("id1", ((Evaluator.Id) child2).getId());

        Evaluator child3 = children.get(2);
        assertEquals(StructuralEvaluator.Negation.class, child3.getClass());
        Evaluator notChild = ((StructuralEvaluator.Not) child3).getLeft();
        assertEquals(Evaluator.Id.class, notChild.getClass());
        assertEquals("id2", ((Evaluator.Id) notChild).getId());
    }

    // Test other selectors and combinators, add more test cases as needed

    @Test
    public void testParseWithAttributeSelectors() {
        String query = "input[type='text']:containsOwn('search')";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator rootEval = evaluator;
        while (rootEval instanceof CombiningEvaluator.And) {
            rootEval = ((CombiningEvaluator.And) rootEval).getLeft();
        }

        assertEquals(StructuralEvaluator.Tag.class, rootEval.getClass());
        assertEquals("input", ((StructuralEvaluator.Tag) rootEval).getElementName());

        List<Evaluator> children = new ArrayList<>();
        Evaluator currentEval = evaluator;
        while (currentEval != rootEval) {
            children.add(currentEval);
            currentEval = currentEval.getNextEvaluator();
        }

        assertEquals(3, children.size());

        Evaluator attributeEval = children.get(1);
        assertEquals(Evaluator.AttributeWithValueStarting.class, attributeEval.getClass());
        assertEquals("type", ((Evaluator.AttributeWithValueStarting) attributeEval).getAttributeName());
        assertEquals("text", ((Evaluator.AttributeWithValueStarting) attributeEval).getValue());

        Evaluator containsEval = children.get(2);
        assertEquals(Evaluator.ContainsOwnText.class, containsEval.getClass());
        assertEquals("search", ((Evaluator.ContainsOwnText) containsEval).getText());
    }

    // Test other selectors with attribute selectors, add more test cases as needed

    @Test
    public void testParseWithNthChildSelectors() {
        String query = "li:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator rootEval = evaluator;
        while (rootEval instanceof CombiningEvaluator.And) {
            rootEval = ((CombiningEvaluator.And) rootEval).getLeft();
        }

        assertEquals(StructuralEvaluator.Tag.class, rootEval.getClass());
        assertEquals("li", ((StructuralEvaluator.Tag) rootEval).getElementName());

        List<Evaluator> children = new ArrayList<>();
        Evaluator currentEval = evaluator;
        while (currentEval != rootEval) {
            children.add(currentEval);
            currentEval = currentEval.getNextEvaluator();
        }

        assertEquals(1, children.size());

        Evaluator nthChildEval = children.get(0);
        assertEquals(Evaluator.IsNthChild.class, nthChildEval.getClass());
        assertEquals(2, ((Evaluator.IsNthChild) nthChildEval).getIndex());
    }

    // Test other selectors with nth-child selectors, add more test cases as needed

    @Test
    public void testParseWithMultipleCombinators() { {
        String query = "div#container > ul > li:nth-child(2) > a[href^='https']";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator rootEval = evaluator;
        while (rootEval instanceof CombiningEvaluator.And) {
            rootEval = ((CombiningEvaluator.And) rootEval).getLeft();
        }

        assertEquals(StructuralEvaluator.Id.class, rootEval.getClass());
        assertEquals("container", ((Evaluator.Id) rootEval).getId());

        List<Evaluator> children = new ArrayList<>();
        Evaluator currentEval = evaluator;
        while (currentEval != rootEval) {
            children.add(currentEval);
            currentEval = currentEval.getNextEvaluator();
        }

        assertEquals(5, children.size());

        Evaluator descendantEval = children.get(1);
        assertEquals(StructuralEvaluator.Tag.class, descendantEval.getClass());
        assertEquals("ul", ((StructuralEvaluator.Tag) descendantEval).getElementName());

        Evaluator nthChildEval = children.get(3);
        assertEquals(Evaluator.IsNthChild.class, nthChildEval.getClass());
        assertEquals(2, ((Evaluator.IsNthChild) nthChildEval).getIndex());

        Evaluator attributeEval = children.get(4);
        assertEquals(Evaluator.AttributeWithValueStarting.class, attributeEval.getClass());
        assertEquals("href", ((Evaluator.AttributeWithValueStarting) attributeEval).getAttributeName());
        assertEquals("https", ((Evaluator.AttributeWithValueStarting) attributeEval).getValue().substring(0, 5));
    }}

    // Test other selectors with multiple combinators, add more test cases as needed
}