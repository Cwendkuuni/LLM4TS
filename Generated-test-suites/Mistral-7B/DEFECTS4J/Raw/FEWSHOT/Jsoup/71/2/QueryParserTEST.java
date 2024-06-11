package org.jsoup.select;

import static org.junit.Assert.*;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div > p:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;

        assertTrue(and.size() == 2);

        Evaluator firstChildEvaluator = and.get(0);
        assertTrue(firstChildEvaluator instanceof StructuralEvaluator.ImmediateChild);
        StructuralEvaluator.ImmediateChild immediateChild = (StructuralEvaluator.ImmediateChild) firstChildEvaluator;
        assertEquals("div", immediateChild.getParentTagName());
        assertEquals("p", immediateChild.getChildTagName());

        Evaluator firstOfTypeEvaluator = and.get(1);
        assertTrue(firstOfTypeEvaluator instanceof Evaluator.IsFirstOfType);
        assertEquals("p", ((Evaluator.IsFirstOfType) firstOfTypeEvaluator).getTagName());
    }

    @Test
    public void testParseWithAttributes() {
        String query = "div[data-test='foo']:not([class='bar'])";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;

        Evaluator hasAttributeEvaluator = and.get(0);
        assertTrue(hasAttributeEvaluator instanceof Evaluator.HasAttribute);
        Evaluator.HasAttribute hasAttribute = (Evaluator.HasAttribute) hasAttributeEvaluator;
        assertEquals("div", hasAttribute.getParentTagName());
        assertEquals("data-test", hasAttribute.getAttributeName());
        assertEquals("foo", hasAttribute.getAttributeValue());

        Evaluator notAttributeEvaluator = and.get(1);
        assertTrue(notAttributeEvaluator instanceof StructuralEvaluator.Not);
        StructuralEvaluator.Not not = (StructuralEvaluator.Not) notAttributeEvaluator;
        assertTrue(not.getChild() instanceof Evaluator.Class);
        assertEquals("bar", ((Evaluator.Class) not.getChild()).getClassValue());
    }

    @Test
    public void testParseWithPseudoClass() {
        String query = "input:checked";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof Evaluator.PseudoClassChecker);
        assertEquals("input", ((Evaluator.PseudoClassChecker) evaluator).getTagName());
        assertEquals("checked", ((Evaluator.PseudoClassChecker) evaluator).getPseudoClass());
    }

    @Test
    public void testParseWithNthChild() {
        String query = "li:nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        Evaluator isNthChild = (Evaluator.IsNthChild) evaluator;
        assertEquals("li", isNthChild.getTagName());
        assertEquals(2, isNthChild.getIndex());
    }

    @Test
    public void testParseWithNthOfType() {
        String query = "li:nth-of-type(1)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof Evaluator.IsNthOfType);
        Evaluator isNthOfType = (Evaluator.IsNthOfType) evaluator;
        assertEquals("li", isNthOfType.getTagName());
        assertEquals(1, isNthOfType.getIndex());
    }

    @Test
    public void testParseWithNthLastChild() {
        String query = "li:nth-last-child(1)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
        Evaluator isNthLastChild = (Evaluator.IsNthLastChild) evaluator;
        assertEquals("li", isNthLastChild.getTagName());
        assertEquals(1, isNthLastChild.getIndex());
    }

    @Test
    public void testParseWithNthLastOfType() {
        String query = "li:nth-last-of-type(1)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
        Evaluator isNthLastOfType = (Evaluator.IsNthLastOfType) evaluator;
        assertEquals("li", isNthLastOfType.getTagName());
        assertEquals(1, isNthLastOfType.getIndex());
    }

    @Test
    public void testParseWithID() {
        String query = "#myID";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("myID", ((Evaluator.Id) evaluator).getId());
    }

    @Test
    public void testParseWithClass() {
        String query = ".myClass";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("myClass", ((Evaluator.Class) evaluator).getClassValue());
    }

    @Test
    public void testParseWithTag() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("p", ((Evaluator.Tag) evaluator).getTagName());
    }

    @Test
    public void testParseWithMultipleSelectors() {
        String query = "div > p:first-child, li:nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof CombiningEvaluator.Or);
        CombiningEvaluator.Or or = (CombiningEvaluator.Or) evaluator;

        assertTrue(or.size() == 2);

        Evaluator firstChildEvaluator = or.get(0);
        assertTrue(firstChildEvaluator instanceof StructuralEvaluator.ImmediateChild);
        StructuralEvaluator.ImmediateChild immediateChild = (StructuralEvaluator.ImmediateChild) firstChildEvaluator;
        assertEquals("div", immediateChild.getParentTagName());
        assertEquals("p", immediateChild.getChildTagName());

        Evaluator nthChildEvaluator = or.get(1);
        assertTrue(nthChildEvaluator instanceof Evaluator.IsNthChild);
        Evaluator isNthChild = (Evaluator.IsNthChild) nthChildEvaluator;
        assertEquals("li", isNthChild.getTagName());
        assertEquals(2, isNthChild.getIndex());
    }

    @Test
    public void testParseWithAttributeSelectors() {
        String query = "div[data-test='foo'][class='bar']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;

        Evaluator hasAttributeEvaluator = and.get(0);
        assertTrue(hasAttributeEvaluator instanceof Evaluator.HasAttribute);
        Evaluator.HasAttribute hasAttribute = (Evaluator.HasAttribute) hasAttributeEvaluator;
        assertEquals("div", hasAttribute.getParentTagName());
        assertEquals("data-test", hasAttribute.getAttributeName());
        assertEquals("foo", hasAttribute.getAttributeValue());

        Evaluator hasClassEvaluator = and.get(1);
        assertTrue(hasClassEvaluator instanceof Evaluator.Class);
        assertEquals("bar", ((Evaluator.Class) hasClassEvaluator).getClassValue());
    }

    @Test
    public void testParseWithNegatedAttributeSelectors() {
        String query = "div:not([data-test='foo']):not([class='bar'])";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;

        Evaluator notHasAttributeEvaluator = and.get(0);
        assertTrue(notHasAttributeEvaluator instanceof StructuralEvaluator.Not);
        StructuralEvaluator.Not notHasAttribute = (StructuralEvaluator.Not) notHasAttributeEvaluator;
        assertTrue(notHasAttribute.getChild() instanceof Evaluator.HasAttribute);
        Evaluator.HasAttribute hasAttribute = (Evaluator.HasAttribute) notHasAttribute.getChild();
        assertEquals("div", hasAttribute.getParentTagName());
        assertEquals("data-test", hasAttribute.getAttributeName());
        assertEquals("foo", hasAttribute.getAttributeValue());

        Evaluator notHasClassEvaluator = and.get(1);
        assertTrue(notHasClassEvaluator instanceof Evaluator.Class);
        assertEquals("bar", ((Evaluator.Class) notHasClassEvaluator).getClassValue());
    }

    @Test
    public void testParseWithCombinators() {
        String query = "div > p:not(.foo)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;

        Evaluator childEvaluator = and.get(0);
        assertTrue(childEvaluator instanceof StructuralEvaluator.ImmediateChild);
        StructuralEvaluator.ImmediateChild immediateChild = (StructuralEvaluator.ImmediateChild) childEvaluator;
        assertEquals("div", immediateChild.getParentTagName());
        assertEquals("p", immediateChild.getChildTagName());

        Evaluator notClassEvaluator = and.get(1);
        assertTrue(notClassEvaluator instanceof StructuralEvaluator.Not);
        StructuralEvaluator.Not notClass = (StructuralEvaluator.Not) notClassEvaluator;
        assertTrue(notClass.getChild() instanceof Evaluator.Class);
        assertEquals("foo", ((Evaluator.Class) notClass.getChild()).getClassValue());
    }
}