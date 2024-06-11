import static org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @org.junit.Test
    public void testParse() {
        String query = "div.class1 > div.class2 > p#id1";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.Root());
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Class("class1"));
        expectedEvaluators.add(new CombiningEvaluator.And(
                new StructuralEvaluator.ImmediateChild(),
                new Evaluator.Tag("div"),
                new Evaluator.Class("class2")
        ));
        expectedEvaluators.add(new Evaluator.Id("id1"));

        assertEquals(expectedEvaluators.size(), evaluator.size());
        for (int i = 0; i < evaluator.size(); i++) {
            assertEquals(expectedEvaluators.get(i).getClass(), evaluator.get(i).getClass());
        }
    }

    @org.junit.Test
    public void testParseWithCombiner() {
        String query = "div#id1 > div.class1 ~ div.class2";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.Root());
        expectedEvaluators.add(new Evaluator.Id("id1"));
        expectedEvaluators.add(new CombiningEvaluator.And(
                new StructuralEvaluator.ImmediateParent(),
                new Evaluator.Tag("div"),
                new Evaluator.Class("class1")
        ));
        expectedEvaluators.add(new StructuralEvaluator.PreviousSibling(
                new Evaluator.Tag("div"),
                new Evaluator.Class("class2")
        ));

        assertEquals(expectedEvaluators.size(), evaluator.size());
        for (int i = 0; i < evaluator.size(); i++) {
            assertEquals(expectedEvaluators.get(i).getClass(), evaluator.get(i).getClass());
        }
    }

    @org.junit.Test
    public void testParseWithComplexQuery() {
        String query = "div.class1 > div.class2 > div > div.class3 > p:contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.Root());
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Class("class1"));
        expectedEvaluators.add(new CombiningEvaluator.And(
                new StructuralEvaluator.ImmediateChild(),
                new Evaluator.Tag("div"),
                new Evaluator.Class("class2")
        ));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Class("class3"));
        expectedEvaluators.add(new Evaluator.ContainsText("text"));

        assertEquals(expectedEvaluators.size(), evaluator.size());
        for (int i = 0; i < evaluator.size(); i++) {
            assertEquals(expectedEvaluators.get(i).getClass(), evaluator.get(i).getClass());
        }
    }

    @org.junit.Test
    public void testParseWithAttributeQuery() {
        String query = "input[type='text']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("input"));
        expectedEvaluators.add(new AttributeEvaluator("type", "text"));

        assertEquals(expectedEvaluators.size(), evaluator.size());
        for (int i = 0; i < evaluator.size(); i++) {
            assertEquals(expectedEvaluators.get(i).getClass(), evaluator.get(i).getClass());
        }
    }

    @org.junit.Test
    public void testParseWithPseudoClass() {
        String query = "a:link";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("a"));
        expectedEvaluators.add(new PseudoClassEvaluator("link"));

        assertEquals(expectedEvaluators.size(), evaluator.size());
        for (int i = 0; i < evaluator.size(); i++) {
            assertEquals(expectedEvaluators.get(i).getClass(), evaluator.get(i).getClass());
        }
    }

    @org.junit.Test
    public void testParseWithPseudoElement() {
        String query = "ul > li:first-child > a:first-child:link:visited";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new StructuralEvaluator.Root());
        expectedEvaluators.add(new Evaluator.Tag("ul"));
        expectedEvaluators.add(new Evaluator.Tag("li"));
        expectedEvaluators.add(new PseudoElementEvaluator("first-child"));
        expectedEvaluators.add(new Evaluator.Tag("a"));
        expectedEvaluators.add(new PseudoClassEvaluator("link"));
        expectedEvaluators.add(new PseudoClassEvaluator("visited"));

        assertEquals(expectedEvaluators.size(), evaluator.size());
        for (int i = 0; i < evaluator.size(); i++) {
            assertEquals(expectedEvaluators.get(i).getClass(), evaluator.get(i).getClass());
        }
    }

    @org.junit.Test
    public void testParseWithPseudoElementAndAttribute() {
        String query = "input:checked:disabled";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("input"));
        expectedEvaluators.add(new PseudoElementEvaluator("checked"));
        expectedEvaluators.add(new PseudoClassEvaluator("disabled"));

        assertEquals(expectedEvaluators.size(), evaluator.size());
        for (int i = 0; i < evaluator.size(); i++) {
            assertEquals(expectedEvaluators.get(i).getClass(), evaluator.get(i).getClass());
        }
    }

    @org.junit.Test(expected = Selector.SelectorParseException.class)
    public void testParseWithInvalidQuery() {
        String query = "div#id1 > div.class1 > div > div.class3 > p:contains(text):not(p:contains(text))";
        QueryParser.parse(query);
    }

    @org.junit.Test
    public void testParseWithNegativeIndex() {
        String query = "div:eq(-1)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Index(-1));

        assertEquals(expectedEvaluators.size(), evaluator.size());
        for (int i = 0; i < evaluator.size(); i++) {
            assertEquals(expectedEvaluators.get(i).getClass(), evaluator.get(i).getClass());
        }
    }

    @org.junit.Test
    public void testParseWithInvalidCombiner() {
        String query = "div#id1 > div.class1 > div > div.class3 > p:contains(text):not(p:contains(text)):foo";
        try {
            QueryParser.parse(query);
            fail();
        } catch (Selector.SelectorParseException e) {
            // expected
        }
    }
}

class PseudoClassEvaluator extends Evaluator {

    private final String pseudoClass;

    public PseudoClassEvaluator(String pseudoClass) {
        this.pseudoClass = pseudoClass;
    }

    @Override
    public boolean matches(Element element) {
        return element.getClass().getName().contains(pseudoClass);
    }

    @Override
    public String toString() {
        return pseudoClass;
    }
}

class PseudoElementEvaluator extends Evaluator {

    private final String pseudoElement;

    public PseudoElementEvaluator(String pseudoElement) {
        this.pseudoElement = pseudoElement;
    }

    @Override
    public boolean matches(Element element) {
        return pseudoElement.equals(element.tagName()) && element.className().isEmpty();
    }

    @Override
    public String toString() {
        return pseudoElement;
    }
}