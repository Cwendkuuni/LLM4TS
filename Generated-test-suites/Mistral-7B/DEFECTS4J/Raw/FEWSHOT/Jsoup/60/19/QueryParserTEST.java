package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelectors() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator).getTagName());

        query = "div#id1";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("id1", ((Evaluator.Id) evaluator).getId());

        query = "div.class1.class2";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        List<String> classes = new ArrayList<>();
        classes.add("class1");
        classes.add("class2");
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(classes, ((Evaluator.Class) evaluator).getClasses());

        query = "div[data-foo=bar]";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("data-foo", ((Evaluator.Attribute) evaluator).getAttributeName());
        assertEquals("bar", ((Evaluator.Attribute) evaluator).getAttributeValue());
    }

    @Test
    public void testParseComplexSelectors() {
        String query = "ul > li";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> andComponents = new ArrayList<>();
        andComponents.add(new Evaluator.Tag("ul"));
        andComponents.add(new StructuralEvaluator.ImmediateChild(Evaluator.Type.ELEMENT, "li"));
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(andComponents, ((CombiningEvaluator.And) evaluator).getComponents());

        query = "ul > li > .class1";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> andComponents2 = new ArrayList<>();
        andComponents2.add(new Evaluator.Tag("ul"));
        andComponents2.add(new StructuralEvaluator.ImmediateChild(Evaluator.Type.ELEMENT, "li"));
        andComponents2.add(new Evaluator.Class("class1"));
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(andComponents2, ((CombiningEvaluator.And) evaluator).getComponents());

        query = "ul > li > .class1:contains(text)";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        List<Evaluator> andComponents3 = new ArrayList<>();
        andComponents3.add(new Evaluator.Tag("ul"));
        andComponents3.add(new StructuralEvaluator.ImmediateChild(Evaluator.Type.ELEMENT, "li"));
        andComponents3.add(new Evaluator.Class("class1"));
        andComponents3.add(new Evaluator.ContainsText("text"));
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertEquals(andComponents3, ((CombiningEvaluator.And) evaluator).getComponents());
    }

    @Test
    public void testParsePseduoClasses() {
        String query = "ul:has(li > a)";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
        assertEquals(Evaluator.Type.ELEMENT, ((StructuralEvaluator.Has) evaluator).getChildEvaluator().getType());
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("ul", ((Evaluator.Tag) evaluator).getTagName());

        query = "li:first-child";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.IsFirstChild);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getParentType());

        query = "li:last-child";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.IsLastChild);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getParentType());

        query = "li:nth-child(2)";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getParentType());
        assertEquals(2, ((Evaluator.IsNthChild) evaluator).getNth());

        query = "li:nth-child(odd)";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.IsNthChild);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getParentType());
        assertEquals(1, ((Evaluator.IsNthChild) evaluator).getNth() % 2);

        query = "li:not(:first-child)";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
        assertTrue(evaluator instanceof Evaluator.IsFirstChild);
    }

    @Test
    public void testParseAttributeSelectors() {
        String query = "li[data-foo=bar]";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("data-foo", ((Evaluator.Attribute) evaluator).getAttributeName());
        assertEquals("bar", ((Evaluator.Attribute) evaluator).getAttributeValue());
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getParentType());

        query = "li[data-foo]";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("data-foo", ((Evaluator.Attribute) evaluator).getAttributeName());
        assertNull(((Evaluator.Attribute) evaluator).getAttributeValue());
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getParentType());

        query = "li[data-foo^=bar]";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.AttributeStarting);
        assertEquals("data-foo", ((Evaluator.AttributeStarting) evaluator).getAttributeName());
        assertEquals("bar", ((Evaluator.AttributeStarting) evaluator).getValuePrefix());
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getParentType());

        query = "li[data-foo$=bar]";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.AttributeEnding);
        assertEquals("data-foo", ((Evaluator.AttributeEnding) evaluator).getAttributeName());
        assertEquals("bar", ((Evaluator.AttributeEnding) evaluator).getValueSuffix());
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getParentType());

        query = "li[data-foo*=bar]";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.AttributeContaining);
        assertEquals("data-foo", ((Evaluator.AttributeContaining) evaluator).getAttributeName());
        assertEquals("bar", ((Evaluator.AttributeContaining) evaluator).getValuePrefix());
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getParentType());

        query = "li[data-foo~=bar]";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.AttributeMatching);
        assertEquals("data-foo", ((Evaluator.AttributeMatching) evaluator).getAttributeName());
        assertEquals(Pattern.compile("\\b" + Pattern.quote("bar") + "\\b"), ((Evaluator.AttributeMatching) evaluator).getPattern());
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getParentType());
    }

    @Test
    public void testParseComplexAttributeSelectors() {
        String query = "li[data-foo^='bar' and data-foo$='baz']";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.And);
        List<Evaluator> andComponents = new ArrayList<>();
        andComponents.add(new Evaluator.AttributeStarting("data-foo", "bar"));
        andComponents.add(new Evaluator.AttributeEnding("data-foo", "baz"));
        assertTrue(evaluator instanceof Evaluator.And);
        assertEquals(andComponents, ((Evaluator.And) evaluator).getComponents());

        query = "li[data-foo~='bar' and data-foo~='baz']";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.And);
        List<Evaluator> andComponents2 = new ArrayList<>();
        andComponents2.add(new Evaluator.AttributeMatching("data-foo", Pattern.compile("\\b" + Pattern.quote("bar") + "\\b|\\b" + Pattern.quote("baz") + "\\b")));
        assertTrue(evaluator instanceof Evaluator.And);
        assertEquals(andComponents2, ((Evaluator.And) evaluator).getComponents());

        query = "li[data-foo*='bar' and data-foo*='baz']";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.And);
        List<Evaluator> andComponents3 = new ArrayList<>();
        andComponents3.add(new Evaluator.AttributeContaining("data-foo", "bar"));
        andComponents3.add(new Evaluator.AttributeContaining("data-foo", "baz"));
        assertTrue(evaluator instanceof Evaluator.And);
        assertEquals(andComponents3, ((Evaluator.And) evaluator).getComponents());

        query = "li[data-foo='bar' and not([data-foo])]";
        evaluator = QueryParser.parse(query);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertTrue(evaluator instanceof Evaluator.And);
        List<Evaluator> andComponents4 = new ArrayList<>();
        andComponents4.add(new Evaluator.Attribute("data-foo", "bar"));
        andComponents4.add(new StructuralEvaluator.Not(Evaluator.Type.ATTRIBUTE));
        assertTrue(evaluator instanceof Evaluator.And);
        assertEquals(andComponents4, ((Evaluator.And) evaluator).getComponents());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelectors() {
        String query = "li[data-foo=bar]#id1";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelectors2() {
        String query = "li:nth-child(1, 2)";
        QueryParser.parse(query);
    }
}