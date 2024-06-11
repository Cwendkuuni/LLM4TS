package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.select.Evaluator;
import org.jsoup.select.StructuralEvaluator.And;
import org.jsoup.select.StructuralEvaluator.Has;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.Not;
import org.jsoup.select.StructuralEvaluator.Or;
import org.jsoup.select.StructuralEvaluator.Parent;
import org.jsoup.select.StructuralEvaluator.PreviousSibling;
import org.jsoup.select.TagName;
import org.jsoup.select.TokenQueue;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertNull(evaluator);
    }

    @Test
    public void testParse_TagName() {
        Evaluator evaluator = QueryParser.parse("div");
        Evaluator tagEvaluator = new Evaluator.Tag(TagName.DIV.toString());
        assertEquals(tagEvaluator, evaluator);
    }

    @Test
    public void testParse_IdSelector() {
        Evaluator evaluator = QueryParser.parse("#id");
        Evaluator idEvaluator = new Evaluator.Id("id");
        assertEquals(idEvaluator, evaluator);
    }

    @Test
    public void testParse_ClassSelector() {
        Evaluator evaluator = QueryParser.parse(".class");
        Evaluator classEvaluator = new Evaluator.Class("class".trim().toLowerCase());
        assertEquals(classEvaluator, evaluator);
    }

    @Test
    public void testParse_SimpleSelector() {
        Evaluator evaluator = QueryParser.parse("p#id.class>div.class");
        And andEvaluator = new And();
        andEvaluator.add(new Evaluator.Tag(TagName.P.toString()));
        andEvaluator.add(new Evaluator.Id("id"));
        andEvaluator.add(new Evaluator.Class("class".trim().toLowerCase()));
        andEvaluator.add(new Evaluator.Tag(TagName.DIV.toString()));
        andEvaluator.add(new Evaluator.Class("class".trim().toLowerCase()));
        assertEquals(andEvaluator, evaluator);
    }

    @Test
    public void testParse_CombinedSelectors() {
        Evaluator evaluator = QueryParser.parse("p#id > div.class, .class1.class2~ .class3");
        Or orEvaluator = new Or();
        CombiningEvaluator and1 = new And();
        and1.add(new Evaluator.Tag(TagName.P.toString()));
        and1.add(new Evaluator.Id("id"));
        and1.add(new Evaluator.Tag(TagName.DIV.toString()));
        and1.add(new Evaluator.Class("class".trim().toLowerCase()));
        orEvaluator.add(and1);
        CombiningEvaluator and2 = new And();
        and2.add(new Evaluator.Class("class1".trim().toLowerCase()));
        and2.add(new Evaluator.Class("class2".trim().toLowerCase()));
        CombiningEvaluator siblings = new And();
        siblings.add(new Evaluator.Class("class3".trim().toLowerCase()));
        siblings.add(new StructuralEvaluator.PreviousSibling());
        and2.add(siblings);
        orEvaluator.add(and2);
        assertEquals(orEvaluator, evaluator);
    }

    @Test
    public void testParse_AttributeSelectors() {
        Evaluator evaluator = QueryParser.parse("[href='http://example.com']");
        Evaluator attributeEvaluator = new Evaluator.AttributeWithValue("href", "http://example.com");
        assertEquals(attributeEvaluator, evaluator);
    }

    @Test
    public void testParse_MultipleAttributeSelectors() {
        Evaluator evaluator = QueryParser.parse("[href='http://example.com'][title='Test']");
        And andEvaluator = new And();
        andEvaluator.add(new Evaluator.AttributeWithValue("href", "http://example.com"));
        andEvaluator.add(new Evaluator.AttributeWithValue("title", "Test"));
        assertEquals(andEvaluator, evaluator);
    }

    @Test
    public void testParse_PseudoClassSelectors() {
        Evaluator evaluator = QueryParser.parse("p:first");
        Has hasEvaluator = new Has(new StructuralEvaluator.FirstChild());
        assertEquals(hasEvaluator, evaluator);
    }

    @Test
    public void testParse_PseudoElementSelectors() {
        Evaluator evaluator = QueryParser.parse("p:first-child > ul > li:first-child > a:link");
        And andEvaluator = new And();
        StructuralEvaluator firstChild = new StructuralEvaluator.FirstChild();
        andEvaluator.add(new Evaluator.Tag(TagName.P.toString()));
        andEvaluator.add(firstChild);
        StructuralEvaluator ul = new StructuralEvaluator.Tag(TagName.UL.toString());
        StructuralEvaluator li = new StructuralEvaluator.Tag(TagName.LI.toString());
        StructuralEvaluator link = new StructuralEvaluator.PseudoClass("link");
        andEvaluator.add(new Evaluator.Tag(ul));
        andEvaluator.add(new StructuralEvaluator.Child(li));
        andEvaluator.add(link);
        assertEquals(andEvaluator, evaluator);
    }

    @Test
    public void testParse_NegatedSelectors() {
        Evaluator evaluator = QueryParser.parse("p:not(#id)");
        Not notEvaluator = new Not(new Evaluator.Id("id"));
        Evaluator tagEvaluator = new Evaluator.Tag(TagName.P.toString());
        assertEquals(new And(tagEvaluator, notEvaluator), evaluator);
    }
}