package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        Evaluator evaluator = QueryParser.parse("");
        assertNull(evaluator);
    }

    @Test
    public void testParseRootQuery() {
        String query = "#root";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator root = new Evaluator.Id("root");
        assertEquals(root, evaluator);
    }

    @Test
    public void testParseSimpleTagQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator tag = new Evaluator.Tag("div");
        assertEquals(tag, evaluator);
    }

    @Test
    public void testParseComplexQuery() {
        String query = "#header > ul > li.active > a[href='/about']";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("header"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("ul")));
        expectedEvaluators.add(new StructuralEvaluator.Child(new Evaluator.Class("active")));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("a")));
        expectedEvaluators.add(new Evaluator.AttributeWithValue("href", "/about"));

        Evaluator[] evaluators = evaluator.toArray();
        for (Evaluator evaluator1 : evaluators) {
            Evaluator expectedEvaluator = null;
            for (Evaluator expectedEvaluator1 : expectedEvaluators) {
                if (evaluator1.getClass() == expectedEvaluator1.getClass()) {
                    expectedEvaluator = expectedEvaluator1;
                    break;
                }
            }
            assertEquals(expectedEvaluator, evaluator1);
        }
    }

    @Test
    public void testParseComplexQueryWithAttributeValueContaining() {
        String query = "input[name*='password']";
        Evaluator evaluator = QueryParser.parse(query);

        Evaluator expectedEvaluator = new Evaluator.AttributeWithValueContaining("name", "password");
        assertEquals(expectedEvaluator.getClass(), evaluator.getClass());
    }

    @Test
    public void testParseComplexQueryWithPseudoClasses() {
        String query = "ul.menu li:hover";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Class("menu"));
        expectedEvaluators.add(new StructuralEvaluator.Child(new Evaluator.Tag("li")));
        expectedEvaluators.add(new StructuralEvaluator.PseudoClass("hover"));

        Evaluator[] evaluators = evaluator.toArray();
        for (Evaluator evaluator1 : evaluators) {
            Evaluator expectedEvaluator = null;
            for (Evaluator expectedEvaluator1 : expectedEvaluators) {
                if (evaluator1.getClass() == expectedEvaluator1.getClass()) {
                    expectedEvaluator = expectedEvaluator1;
                    break;
                }
            }
            assertEquals(expectedEvaluator, evaluator1);
        }
    }

    @Test
    public void testParseComplexQueryWithMultipleCombinators() {
        String query = "h1 > ul > li:first-child > a:containsOwn('home')";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("h1"));
        expectedEvaluators.add(new StructuralEvaluator.Child(new Evaluator.Tag("ul")));
        expectedEvaluators.add(new StructuralEvaluator.Child(new Evaluator.Class("li")) {
            @Override
            public boolean matches(Element element) {
                return element.childNodes().get(0) != null;
            }
        });
        expectedEvaluators.add(new Evaluator.ContainsOwnText("home"));

        Evaluator[] evaluators = evaluator.toArray();
        for (Evaluator evaluator1 : evaluators) {
            Evaluator expectedEvaluator = null;
            for (Evaluator expectedEvaluator1 : expectedEvaluators) {
                if (evaluator1.getClass() == expectedEvaluator1.getClass()) {
                    expectedEvaluator = expectedEvaluator1;
                    break;
                }
            }
            assertEquals(expectedEvaluator, evaluator1);
        }
    }

    @Test
    public void testParseComplexQueryWithNestedPseudoClasses() {
        String query = "ul > li.active:hover > a.external";
        Evaluator evaluator = QueryParser.parse(query);

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new StructuralEvaluator.Child(new Evaluator.Class("li")) {
            @Override
            public boolean matches(Element element) {
                return element.hasClass("active");
            }
        });
        expectedEvaluators.add(new StructuralEvaluator.PseudoClass("hover"));
        expectedEvaluators.add(new Evaluator.Class("external"));

        Evaluator[] evaluators = evaluator.toArray();
        for (Evaluator evaluator1 : evaluators) {
            Evaluator expectedEvaluator = null;
            for (Evaluator expectedEvaluator1 : expectedEvaluators) {
                if (evaluator1.getClass() == expectedEvaluator1.getClass()) {
                    expectedEvaluator = expectedEvaluator1;
                    break;
                }
            }
            assertEquals(expectedEvaluator, evaluator1);
        }
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div:not(>ul)";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQueryWithUnsupportedCombator() {
        String query = "div~:not(ul)";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQueryWithUnclosedBrackets() {
        String query = "div[class=(red green]";
        QueryParser.parse(query);
    }
}