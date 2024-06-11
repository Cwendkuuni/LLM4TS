package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        assertEquals(new CombiningEvaluator.And(new ArrayList<>()), parser.parse());
    }

    @Test
    public void testParseTagQuery() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("p", evaluator.toString());
    }

    @Test
    public void testParseClassQuery() {
        String query = ".className";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(".className", evaluator.toString());
    }

    @Test
    public void testParseIdQuery() {
        String query = "#idName";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("#idName", evaluator.toString());
        assertEquals("idName", evaluator.getId());
    }

    @Test
    public void testParseSimpleQuery() {
        String query = "div p";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof CombiningEvaluator.And);

        CombiningEvaluator andEvaluator = (CombiningEvaluator) rootEvaluator;
        Evaluator tagEvaluator = andEvaluator.getEvaluator(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("div", tagEvaluator.toString());

        Evaluator secondEvaluator = andEvaluator.getEvaluator(1);
        assertTrue(secondEvaluator instanceof Evaluator.Tag);
        assertEquals("p", secondEvaluator.toString());
    }

    @Test
    public void testParseQueryWithCombination() {
        String query = "p > div";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof CombiningEvaluator.And);

        CombiningEvaluator andEvaluator = (CombiningEvaluator) rootEvaluator;
        Evaluator tagEvaluator = andEvaluator.getEvaluator(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("p", tagEvaluator.toString());

        Evaluator combinatorEvaluator = andEvaluator.getEvaluator(1);
        assertTrue(combinatorEvaluator instanceof Evaluator.Combining);
        assertEquals(">", combinatorEvaluator.toString());

        Evaluator childEvaluator = andEvaluator.getEvaluator(2);
        assertTrue(childEvaluator instanceof Evaluator.Tag);
        assertEquals("div", childEvaluator.toString());

        Evaluator parentEvaluator = ((StructuralEvaluator.ImmediateParent) childEvaluator).getParentEvaluator();
        assertTrue(parentEvaluator instanceof Evaluator.Tag);
        assertEquals("p", parentEvaluator.toString());
    }

    @Test
    public void testParseQueryWithClassAndTag() {
        String query = ".class1 p.class2";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof CombiningEvaluator.And);

        CombiningEvaluator andEvaluator = (CombiningEvaluator) rootEvaluator;
        Evaluator tagEvaluator = andEvaluator.getEvaluator(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("p", tagEvaluator.toString());

        Evaluator firstClassEvaluator = andEvaluator.getEvaluator(1);
        assertTrue(firstClassEvaluator instanceof Evaluator.Class);
        assertEquals(".class1", firstClassEvaluator.toString());

        Evaluator secondClassEvaluator = andEvaluator.getEvaluator(2);
        assertTrue(secondClassEvaluator instanceof Evaluator.Class);
        assertEquals(".class2", secondClassEvaluator.toString());
    }

    @Test
    public void testParseQueryWithAttribute() {
        String query = "input[type='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof CombiningEvaluator.And);

        CombiningEvaluator andEvaluator = (CombiningEvaluator) rootEvaluator;
        Evaluator tagEvaluator = andEvaluator.getEvaluator(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("input", tagEvaluator.toString());

        Evaluator attributeEvaluator = andEvaluator.getEvaluator(1);
        assertTrue(attributeEvaluator instanceof Evaluator.Attribute);
        assertEquals("type", attributeEvaluator.toString());

        Evaluator valueEvaluator = ((Evaluator.AttributeWithValue) attributeEvaluator).getValueEvaluator();
        assertTrue(valueEvaluator instanceof Evaluator.StringValue);
        assertEquals("text", valueEvaluator.toString());
    }

    @Test
    public void testParseQueryWithAttributeValue() {
        String query = "input[value='username']";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof CombiningEvaluator.And);

        CombiningEvaluator andEvaluator = (CombiningEvaluator) rootEvaluator;
        Evaluator tagEvaluator = andEvaluator.getEvaluator(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("input", tagEvaluator.toString());

        Evaluator attributeEvaluator = andEvaluator.getEvaluator(1);
        assertTrue(attributeEvaluator instanceof Evaluator.Attribute);
        assertEquals("value", attributeEvaluator.toString());

        Evaluator valueEvaluator = ((Evaluator.AttributeWithValue) attributeEvaluator).getValueEvaluator();
        assertTrue(valueEvaluator instanceof Evaluator.StringValue);
        assertEquals("username", valueEvaluator.toString());
    }

    @Test
    public void testParseQueryWithNthChild() {
        String query = "li:nth-child(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof Evaluator.IsNthChild);
        assertEquals("li", rootEvaluator.toString());
        assertEquals(3, ((Evaluator.IsNthChild) rootEvaluator).getIndex());
    }

    @Test
    public void testParseQueryWithNthOfType() {
        String query = "li:nth-of-type(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof Evaluator.IsNthOfType);
        assertEquals("li", rootEvaluator.toString());
        assertEquals(3, ((Evaluator.IsNthOfType) rootEvaluator).getIndex());
    }

    @Test
    public void testParseQueryWithNthLastChild() {
        String query = "li:nth-last-child(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof Evaluator.IsNthLastChild);
        assertEquals("li", rootEvaluator.toString());
        assertEquals(3, ((Evaluator.IsNthLastChild) rootEvaluator).getIndex());
    }

    @Test
    public void testParseQueryWithNthLastOfType() {
        String query = "li:nth-last-of-type(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof Evaluator.IsNthLastOfType);
        assertEquals("li", rootEvaluator.toString());
        assertEquals(3, ((Evaluator.IsNthLastOfType) rootEvaluator).getIndex());
    }

    @Test
    public void testParseQueryWithPseudoClass() {
        String query = "input:checked";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof Evaluator.PseudoClass);
        assertEquals(":checked", rootEvaluator.toString());
    }

    @Test
    public void testParseQueryWithPseudoElement() {
        String query = "a:first-letter";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof Evaluator.PseudoElement);
        assertEquals(":first-letter", rootEvaluator.toString());
    }

    @Test
    public void testParseQueryWithCombiningAndNot() {
        String query = "p > div:not(.error)";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof CombiningEvaluator.And);

        CombiningEvaluator andEvaluator = (CombiningEvaluator) rootEvaluator;
        Evaluator tagEvaluator = andEvaluator.getEvaluator(0);
        assertTrue(tagEvaluator instanceof Evaluator.Tag);
        assertEquals("p", tagEvaluator.toString());

        Evaluator combinatorEvaluator = andEvaluator.getEvaluator(1);
        assertTrue(combinatorEvaluator instanceof Evaluator.Combining);
        assertEquals(">", combinatorEvaluator.toString());

        Evaluator childEvaluator = andEvaluator.getEvaluator(2);
        assertTrue(childEvaluator instanceof CombiningEvaluator.And);

        CombiningEvaluator notEvaluator = (CombiningEvaluator) childEvaluator;
        Evaluator classEvaluator = notEvaluator.getEvaluator(0);
        assertTrue(classEvaluator instanceof Evaluator.Class);
        assertEquals(".error", classEvaluator.toString());

        Evaluator tag2Evaluator = notEvaluator.getEvaluator(1);
        assertTrue(tag2Evaluator instanceof Evaluator.Tag);
        assertEquals("div", tag2Evaluator.toString());
    }

    @Test
    public void testParseQueryWithDescendantCombining() {
        String query = "div p";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof CombiningEvaluator.Descendant);
        assertEquals("div > p", rootEvaluator.toString());
    }

    @Test
    public void testParseQueryWithAdjacentSiblingCombining() {
        String query = "p + div";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof CombiningEvaluator.AdjacentSibling);
        assertEquals("p + div", rootEvaluator.toString());
    }

    @Test
    public void testParseQueryWithGeneralSiblingCombining() {
        String query = "p ~ div";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof CombiningEvaluator.GeneralSibling);
        assertEquals("p ~ div", rootEvaluator.toString());
    }

    @Test
    public void testParseQueryWithMultiplePseudoClasses() {
        String query = "input:disabled:checked";
        QueryParser parser = new QueryParser(query);
        Evaluator rootEvaluator = parser.parse();
        assertTrue(rootEvaluator instanceof CombiningEvaluator.And);

        CombiningEvaluator andEvaluator = (CombiningEvaluator) rootEvaluator;
        Evaluator combinatorEvaluator = andEvaluator.getEvaluator(0);
        assertTrue(combinatorEvaluator instanceof Evaluator.PseudoClass);
        assertEquals(":disabled", combinatorEvaluator.toString());

        Evaluator secondCombinatorEvaluator = andEvaluator.getEvaluator(1);
        assertTrue(secondCombinatorEvaluator instanceof Evaluator.PseudoClass);
        assertEquals(":checked", secondCombinatorEvaluator.toString());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div:foo";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQueryWithUnescapedCharacters() {
        String query = "div\\:foo";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQueryWithFailedConsumption() {
        String query = "div > div >div";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }
}