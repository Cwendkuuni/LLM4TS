package org.jsoup.select.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

import static org.jsoup.helper.StringUtil.isNumeric;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Empty query should return null", null, evaluator);
    }

    @Test
    public void testParseSimpleTagQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Tag query should match div elements", Evaluator.class, evaluator.getClass());
        StructuralEvaluator tagEvaluator = (StructuralEvaluator) evaluator;
        List<Evaluator> children = tagEvaluator.getChildren();
        Assert.assertEquals("Tag query should have no children", 0, children.size());
    }

    @Test
    public void testParseClassQuery() {
        String query = ".class1";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Class query should match elements with class 'class1'", Evaluator.class, evaluator.getClass());
        Evaluator classEvaluator = (Evaluator.Class) evaluator;
        String className = classEvaluator.getClassString();
        Assert.assertEquals("Class query should have class name 'class1'", "class1", className);
    }

    @Test
    public void testParseIdQuery() {
        String query = "#id1";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("ID query should match element with ID 'id1'", Evaluator.Id.class, evaluator.getClass());
        Evaluator idEvaluator = (Evaluator.Id) evaluator;
        String id = idEvaluator.getId();
        Assert.assertEquals("ID query should have ID 'id1'", "id1", id);
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "[title='Test']";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Attribute query should match elements with 'title' attribute value 'Test'", Evaluator.Attribute.class, evaluator.getClass());
        Evaluator attributeEvaluator = (Evaluator.Attribute) evaluator;
        String attributeName = attributeEvaluator.getAttributeName();
        String attributeValue = attributeEvaluator.getAttributeValue();
        Assert.assertEquals("Attribute query should have attribute name 'title'", "title", attributeName);
        Assert.assertEquals("Attribute query should have attribute value 'Test'", "Test", attributeValue);
    }

    @Test
    public void testParseComplexQuery() {
        String query = "div.class1 #id1 > ul > li:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals("Complex query should match the second <li> element inside <ul> that is a child of <div> with class 'class1' and ID 'id1'", CombiningEvaluator.class, evaluator.getClass());
        CombiningEvaluator combiningEvaluator = (CombiningEvaluator) evaluator;
        List<Evaluator> children = combiningEvaluator.getChildren();
        Evaluator lastChild = children.get(children.size() - 1);
        StructuralEvaluator nthChildEvaluator = (StructuralEvaluator) lastChild;
        StructuralEvaluator liEvaluator = (StructuralEvaluator) nthChildEvaluator.getChildren().get(0);
        Assert.assertEquals("Complex query should match the second <li> element inside <ul>", Evaluator.IsNthChild.class, liEvaluator.getClass());
        IsNthChild isNthChildEvaluator = (IsNthChild) liEvaluator;
        int index = isNthChildEvaluator.getIndex();
        Assert.assertEquals("Complex query should match the second <li> element inside <ul>", 2, index);

        Evaluator parentDivEvaluator = children.get(children.size() - 2);
        StructuralEvaluator classDivEvaluator = (StructuralEvaluator) parentDivEvaluator;
        String className = classDivEvaluator.getClassString();
        Assert.assertEquals("Complex query should match <div> with class 'class1'", "class1", className);

        Evaluator parentUlEvaluator = children.get(children.size() - 3);
        StructuralEvaluator tagUlEvaluator = (StructuralEvaluator) parentUlEvaluator;
        Assert.assertEquals("Complex query should match <ul>", Evaluator.Tag.class, tagUlEvaluator.getClass());
        String tagName = tagUlEvaluator.getTagName();
        Assert.assertEquals("Complex query should match <ul>", "ul", tagName);

        Evaluator parentDiv = children.get(0);
        StructuralEvaluator classDiv = (StructuralEvaluator) parentDiv;
        Assert.assertEquals("Complex query should match <div> with class 'class1'", Evaluator.Class.class, classDiv.getClass());
        String class1 = ((Evaluator.Class) classDiv).getClassString();
        Assert.assertEquals("Complex query should match <div> with class 'class1'", "class1", class1);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = ":invalid";
        try {
            QueryParser.parse(query);
            fail("Invalid query should throw SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown selector type: :invalid", e.getMessage());
        }
    }

    @Test
    public void testParseQueryWithInvalidIndex() {
        String query = "li:nth-child(-1)";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Invalid index in nth-child query should throw SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Invalid index in nth-child query", e.getMessage());
        }
    }

    @Test
    public void testParseQueryWithInvalidAttributeSelector() {
        String query = "[title^=Test]"; // missing '=' sign
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Missing '=' sign in attribute selector should throw SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unexpected token: [ in attribute selector", e.getMessage());
        }
    }
}