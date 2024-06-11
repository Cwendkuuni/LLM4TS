package org.jsoup.select;

import static org.junit.Assert.*;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class1 > div.class2";
        QueryParser qp = new QueryParser(query);
        Evaluator eval = qp.parse();
        // Add more assertions based on the structure of the Evaluator tree
        assertTrue(eval instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEval = (CombiningEvaluator.And) eval;
        assertEquals(2, andEval.getEvaluators().size());
        Evaluator firstEval = andEval.getEvaluators().get(0);
        assertTrue(firstEval instanceof StructuralEvaluator.Tag);
        StructuralEvaluator.Tag tagEval = (StructuralEvaluator.Tag) firstEval;
        assertEquals("div", tagEval.getTagName());
        assertTrue(tagEval.getClassAttributes().isEmpty());
        Evaluator secondEval = andEval.getEvaluators().get(1);
        assertTrue(secondEval instanceof CombiningEvaluator.And);
        CombiningEvaluator.And childAndEval = (CombiningEvaluator.And) secondEval;
        assertEquals(2, childAndEval.getEvaluators().size());
        Evaluator childFirstEval = childAndEval.getEvaluators().get(0);
        assertTrue(childFirstEval instanceof StructuralEvaluator.Tag);
        StructuralEvaluator.Tag childTagEval = (StructuralEvaluator.Tag) childFirstEval;
        assertEquals("div", childTagEval.getTagName());
        List<String> childClassAttributes = childTagEval.getClassAttributes();
        assertEquals(1, childClassAttributes.size());
        assertEquals("class2", childClassAttributes.get(0));
        Evaluator childSecondEval = childAndEval.getEvaluators().get(1);
        assertTrue(childSecondEval instanceof StructuralEvaluator.Parent);
        StructuralEvaluator.Parent parentEval = (StructuralEvaluator.Parent) childSecondEval;
        assertEquals(firstEval, parentEval.getChildEvaluator());
    }

    @Test
    public void testParseWithCombinator() {
        String query = "div > div + div";
        QueryParser qp = new QueryParser(query);
        Evaluator eval = qp.parse();
        // Add more assertions based on the structure of the Evaluator tree
        assertTrue(eval instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEval = (CombiningEvaluator.And) eval;
        assertEquals(3, andEval.getEvaluators().size());
        Evaluator firstEval = andEval.getEvaluators().get(0);
        assertTrue(firstEval instanceof StructuralEvaluator.Tag);
        StructuralEvaluator.Tag tagEval = (StructuralEvaluator.Tag) firstEval;
        assertEquals("div", tagEval.getTagName());
        assertTrue(tagEval.getClassAttributes().isEmpty());
        Evaluator secondEval = andEval.getEvaluators().get(1);
        assertTrue(secondEval instanceof StructuralEvaluator.ImmediatePreviousSibling);
        StructuralEvaluator.ImmediatePreviousSibling siblingEval = (StructuralEvaluator.ImmediatePreviousSibling) secondEval;
        assertEquals(firstEval, siblingEval.getPreviousSiblingEvaluator());
        Evaluator thirdEval = andEval.getEvaluators().get(2);
        assertTrue(thirdEval instanceof StructuralEvaluator.Tag);
        StructuralEvaluator.Tag thirdTagEval = (StructuralEvaluator.Tag) thirdEval;
        assertEquals("div", thirdTagEval.getTagName());
        assertTrue(thirdTagEval.getClassAttributes().isEmpty());
    }

    @Test
    public void testParseWithAttribute() {
        String query = "div[class='class1']";
        QueryParser qp = new QueryParser(query);
        Evaluator eval = qp.parse();
        // Add more assertions based on the structure of the Evaluator tree
        assertTrue(eval instanceof Evaluator.Attribute);
        Evaluator attributeEval = (Evaluator.Attribute) eval;
        assertEquals("class", attributeEval.getAttributeName());
        List<String> attributeValues = attributeEval.getAttributeValues();
        assertEquals(1, attributeValues.size());
        assertEquals("class1", attributeValues.get(0));
    }

    @Test
    public void testParseWithPseudoClass() {
        String query = "div:first-child";
        QueryParser qp = new QueryParser(query);
        Evaluator eval = qp.parse();
        // Add more assertions based on the structure of the Evaluator tree
        assertTrue(eval instanceof Evaluator.IsFirstChild);
    }

    @Test
    public void testParseWithID() {
        String query = "#id1";
        QueryParser qp = new QueryParser(query);
        Evaluator eval = qp.parse();
        // Add more assertions based on the structure of the Evaluator tree
        assertTrue(eval instanceof Evaluator.Id);
        Evaluator idEval = (Evaluator.Id) eval;
        assertEquals("id1", idEval.getId());
    }

    @Test
    public void testParseWithClass() {
        String query = ".class1";
        QueryParser qp = new QueryParser(query);
        Evaluator eval = qp.parse();
        // Add more assertions based on the structure of the Evaluator tree
        assertTrue(eval instanceof Evaluator.Class);
        Evaluator classNameEval = (Evaluator.Class) eval;
        assertEquals("class1", classNameEval.getClassAttribute());
    }

    @Test
    public void testParseWithTag() {
        String query = "p";
        QueryParser qp = new QueryParser(query);
        Evaluator eval = qp.parse();
        // Add more assertions based on the structure of the Evaluator tree
        assertTrue(eval instanceof Evaluator.Tag);
        Evaluator tagEval = (Evaluator.Tag) eval;
        assertEquals("p", tagEval.getTagName());
    }

    @Test
    public void testParseWithCombiningSelector() {
        String query = "div > p, div > ul";
        QueryParser qp = new QueryParser(query);
        Evaluator eval = qp.parse();
        // Add more assertions based on the structure of the Evaluator tree
        assertTrue(eval instanceof CombiningEvaluator.Or);
        CombiningEvaluator.Or orEval = (CombiningEvaluator.Or) eval;
        assertEquals(2, orEval.getEvaluators().size());
        Evaluator firstEval = orEval.getEvaluators().get(0);
        assertTrue(firstEval instanceof StructuralEvaluator.Tag);
        StructuralEvaluator.Tag firstTagEval = (StructuralEvaluator.Tag) firstEval;
        assertEquals("div", firstTagEval.getTagName());
        assertTrue(firstTagEval.getClassAttributes().isEmpty());
        Evaluator secondEval = orEval.getEvaluators().get(1);
        assertTrue(secondEval instanceof StructuralEvaluator.Tag);
        StructuralEvaluator.Tag secondTagEval = (StructuralEvaluator.Tag) secondEval;
        assertEquals("ul", secondTagEval.getTagName());
        assertTrue(secondTagEval.getClassAttributes().isEmpty());
    }

    @Test
    public void testParseWithNthChildSelector() {
        String query = "li:nth-child(3)";
        QueryParser qp = new QueryParser(query);
        Evaluator eval = qp.parse();
        // Add more assertions based on the structure of the Evaluator tree
        assertTrue(eval instanceof Evaluator.IsNthChild);
        Evaluator nthChildEval = (Evaluator.IsNthChild) eval;
        assertEquals(3, nthChildEval.getNthChildIndex());
    }

    @Test
    public void testParseWithNthLastChildSelector() {
        String query = "li:nth-last-child(3)";
        QueryParser qp = new QueryParser(query);
        Evaluator eval = qp.parse();
        // Add more assertions based on the structure of the Evaluator tree
        assertTrue(eval instanceof Evaluator.IsNthLastChild);
        Evaluator nthLastChildEval = (Evaluator.IsNthLastChild) eval;
        assertEquals(3, nthLastChildEval.getNthLastChildIndex());
    }

    @Test
    public void testParseWithNthOfTypeSelector() {
        String query = "li:nth-of-type(3)";
        QueryParser qp = new QueryParser(query);
        Evaluator eval = qp.parse();
        // Add more assertions based on the structure of the Evaluator tree
        assertTrue(eval instanceof Evaluator.IsNthOfType);
        Evaluator nthOfTypeEval = (Evaluator.IsNthOfType) eval;
        assertEquals(3, nthOfTypeEval.getNthOfTypeIndex());
    }

    @Test
    public void testParseWithNthLastOfTypeSelector() {
        String query = "li:nth-last-of-type(3)";
        QueryParser qp = new QueryParser(query);
        Evaluator eval = qp.parse();
        // Add more assertions based on the structure of the Evaluator tree
        assertTrue(eval instanceof Evaluator.IsNthLastOfType);
        Evaluator nthLastOfTypeEval = (Evaluator.IsNthLastOfType) eval;
        assertEquals(3, nthLastOfTypeEval.getNthLastOfTypeIndex());
    }
}