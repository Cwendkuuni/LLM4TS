package org.jsoup.select;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;

import static org.jsoup.parser.QueryParser.combinators;
import static org.jsoup.parser.QueryParser.AttributeEvals;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsEmpty);
    }

    @Test
    public void testParse_SimpleTag() {
        QueryParser parser = new QueryParser("p");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("p", evaluator.toString());
    }

    @Test
    public void testParse_TagWithClass() {
        QueryParser parser = new QueryParser("p.class1");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator tagEval = ((CombiningEvaluator.And) evaluator).getEvaluators().get(0);
        assertTrue(tagEval instanceof Evaluator.Tag);
        assertEquals("p", tagEval.toString());
        Evaluator classEval = ((CombiningEvaluator.And) evaluator).getEvaluators().get(1);
        assertTrue(classEval instanceof Evaluator.Class);
        assertEquals("class1", classEval.toString());
    }

    @Test
    public void testParse_TagWithId() {
        QueryParser parser = new QueryParser("#id1");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("id1", evaluator.toString());
    }

    @Test
    public void testParse_MultiElements() {
        QueryParser parser = new QueryParser("p, div.class1");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
        Evaluator pEval = ((CombiningEvaluator.Or) evaluator).getEvaluators().get(0);
        assertTrue(pEval instanceof Evaluator.Tag);
        assertEquals("p", pEval.toString());
        Evaluator divEval = ((CombiningEvaluator.Or) evaluator).getEvaluators().get(1);
        assertTrue(divEval instanceof CombiningEvaluator.And);
        Evaluator tagEval = ((CombiningEvaluator.And) divEval).getEvaluators().get(0);
        assertTrue(tagEval instanceof Evaluator.Tag);
        assertEquals("div", tagEval.toString());
        Evaluator classEval = ((CombiningEvaluator.And) divEval).getEvaluators().get(1);
        assertTrue(classEval instanceof Evaluator.Class);
        assertEquals("class1", classEval.toString());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_UnknownCombinator() {
        QueryParser parser = new QueryParser("p ~! x");
        parser.parse();
    }

    @Test
    public void testParse_AttributeSelector() {
        QueryParser parser = new QueryParser("p[title=Test]");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("p", evaluator.toString());
        Evaluator attributeEval = ((Evaluator.Attribute) evaluator).getAttribute();
        assertEquals("title", attributeEval.toString());
        assertEquals("Test", ((Evaluator.AttributeWithValue) attributeEval).getValue().toString());
    }

    @Test
    public void testParse_AttributeWithValueStarting() {
        QueryParser parser = new QueryParser("p[title^='Test']");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
        assertEquals("p", evaluator.toString());
        Evaluator attributeEval = ((Evaluator.AttributeWithValueStarting) evaluator).getAttribute();
        assertEquals("title", attributeEval.toString());
        assertEquals("Test", ((Evaluator.AttributeWithValueStarting) evaluator).getValue().toString());
    }

    @Test
    public void testParse_AttributeWithValueEnding() {
        QueryParser parser = new QueryParser("p[title$='Test']");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
        assertEquals("p", evaluator.toString());
        Evaluator attributeEval = ((Evaluator.AttributeWithValueEnding) evaluator).getAttribute();
        assertEquals("title", attributeEval.toString());
        assertEquals("Test", ((Evaluator.AttributeWithValueEnding) evaluator).getValue().toString());
    }

    @Test
    public void testParse_AttributeWithValueContaining() {
        QueryParser parser = new QueryParser("p[title*='Test']");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
        assertEquals("p", evaluator.toString());
        Evaluator attributeEval = ((Evaluator.AttributeWithValueContaining) evaluator).getAttribute();
        assertEquals("title", attributeEval.toString());
        assertEquals("Test", ((Evaluator.AttributeWithValueContaining) evaluator).getValue().toString());
    }

    @Test
    public void testParse_AttributeWithValueMatching() {
        QueryParser parser = new QueryParser("p[title~='Test']");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
        assertEquals("p", evaluator.toString());
        Evaluator attributeEval = ((Evaluator.AttributeWithValueMatching) evaluator).getAttribute();
        assertEquals("title", attributeEval.toString());
        Pattern regex = ((Evaluator.AttributeWithValueMatching) evaluator).getValue();
        assertTrue(regex.matcher("TestTestTest").find());
        assertTrue(regex.matcher("TestOtherTest").find());
        assertTrue(regex.matcher("OtherTestTest").find());
        assertFalse(regex.matcher("OtherOther").find());
    }

    @Test
    public void testParse_PseudoClass_FirstChild() {
        QueryParser parser = new QueryParser("p:first-child");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsFirstChild);
        assertEquals("p", evaluator.toString());
    }

    @Test
    public void testParse_PseudoClass_LastChild() {
        QueryParser parser = new QueryParser("p:last-child");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsLastChild);
        assertEquals("p", evaluator.toString());
    }

    @Test
    public void testParse_PseudoClass_FirstOfType() {
        QueryParser parser = new QueryParser("p:first-of-type");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsFirstOfType);
        assertEquals("p", evaluator.toString());
    }

    @Test
    public void testParse_PseudoClass_LastOfType() {
        QueryParser parser = new QueryParser("p:last-of-type");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsLastOfType);
        assertEquals("p", evaluator.toString());
    }

    @Test
    public void testParse_PseudoClass_OnlyChild() {
        QueryParser parser = new QueryParser("p:only-child");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsOnlyChild);
        assertEquals("p", evaluator.toString());
    }

    @Test
    public void testParse_PseudoClass_OnlyOfType() {
        QueryParser parser = new QueryParser("p:only-of-type");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsOnlyOfType);
        assertEquals("p", evaluator.toString());
    }

    @Test
    public void testParse_PseudoClass_Empty() {
        QueryParser parser = new QueryParser("p:empty");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsEmpty);
        assertEquals("p", evaluator.toString());
    }

    @Test
    public void testParse_PseudoClass_Root() {
        QueryParser parser = new QueryParser(":root");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsRoot);
    }

    @Test
    public void testParse_PseudoClass_NthChild() {
        QueryParser parser = new QueryParser("p:nth-child(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        assertEquals("p", evaluator.toString());
        assertEquals(2, ((Evaluator.IsNthChild) evaluator).getIndex());
    }

    @Test
    public void testParse_PseudoClass_NthLastChild() {
        QueryParser parser = new QueryParser("p:nth-last-child(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
        assertEquals("p", evaluator.toString());
        assertEquals(2, ((Evaluator.IsNthLastChild) evaluator).getIndex());
    }

    @Test
    public void testParse_PseudoClass_NthOfType() {
        QueryParser parser = new QueryParser("p:nth-of-type(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthOfType);
        assertEquals("p", evaluator.toString());
        assertEquals(2, ((Evaluator.IsNthOfType) evaluator).getIndex());
    }

    @Test
    public void testParse_PseudoClass_NthLastOfType() {
        QueryParser parser = new QueryParser("p:nth-last-of-type(2)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
        assertEquals("p", evaluator.toString());
        assertEquals(2, ((Evaluator.IsNthLastOfType) evaluator).getIndex());
    }

    @Test
    public void testParse_PseudoClass_Has() {
        QueryParser parser = new QueryParser("p:has(.class1)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        assertEquals("p", evaluator.toString());
        Evaluator childEvaluator = ((StructuralEvaluator.Has) evaluator).getChildEvaluator();
        assertTrue(childEvaluator instanceof Evaluator.Class);
        assertEquals("class1", childEvaluator.toString());
    }

    @Test
    public void testParse_PseudoClass_ContainsText() {
        QueryParser parser = new QueryParser("p:contains(Test)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        assertEquals("p", evaluator.toString());
        assertEquals("Test", ((Evaluator.ContainsText) evaluator).getText());
    }

    @Test
    public void testParse_PseudoClass_ContainsOwnText() {
        QueryParser parser = new QueryParser("p:containsOwn(Test)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
        assertEquals("p", evaluator.toString());
        assertEquals("Test", ((Evaluator.ContainsOwnText) evaluator).getText());
    }

    @Test
    public void testParse_PseudoClass_ContainsData() {
        QueryParser parser = new QueryParser("p:containsData(data)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.ContainsData);
        assertEquals("p", evaluator.toString());
        assertEquals("data", ((Evaluator.ContainsData) evaluator).getData());
    }

    @Test
    public void testParse_PseudoClass_Matches() {
        QueryParser parser = new QueryParser("p:matches(.*Test.*)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Matches);
        assertEquals("p", evaluator.toString());
        Pattern regex = ((Evaluator.Matches) evaluator).getRegex();
        assertTrue(regex.matcher("TestTestTest").find());
        assertTrue(regex.matcher("TestOtherTest").find());
        assertTrue(regex.matcher("OtherTestTest").find());
        assertFalse(regex.matcher("OtherOther").find());
    }

    @Test
    public void testParse_PseudoClass_MatchesOwn() {
        QueryParser parser = new QueryParser("p:matchesOwn(.*Test.*)");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.MatchesOwn);
        assertEquals("p", evaluator.toString());
        Pattern regex = ((Evaluator.MatchesOwn) evaluator).getRegex();
        assertTrue(regex.matcher("TestTestTest").find());
        assertTrue(regex.matcher("TestOtherTest").find());
        assertTrue(regex.matcher("OtherTestTest").find());
        assertFalse(regex.matcher("OtherOther").find());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidSelector() {
        QueryParser parser = new QueryParser("p:not:not:not(.class1)");
        parser.parse();
    }
}