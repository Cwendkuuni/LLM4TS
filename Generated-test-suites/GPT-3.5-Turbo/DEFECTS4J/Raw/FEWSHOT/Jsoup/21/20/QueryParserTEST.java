import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        String query = "div > p + a";
        assertEquals(new StructuralEvaluator.And(
                            new StructuralEvaluator.And(
                                new Evaluator.Tag("div"),
                                new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("p"))
                            ),
                            new StructuralEvaluator.ImmediatePreviousSibling(new Evaluator.Tag("a"))
                        ),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testCombiningEvaluatorOr() {
        String query = "div, p, a";
        assertEquals(new CombiningEvaluator.Or(
                            new Evaluator.Tag("div"),
                            new CombiningEvaluator.Or(
                                new Evaluator.Tag("p"),
                                new Evaluator.Tag("a")
                            )
                        ),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testCombiningEvaluatorParent() {
        String query = "div p";
        assertEquals(new CombiningEvaluator.And(
                            new Evaluator.Tag("div"),
                            new StructuralEvaluator.Parent(new Evaluator.Tag("p"))
                        ),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testCombiningEvaluatorImmediateParent() {
        String query = "div > p";
        assertEquals(new CombiningEvaluator.And(
                            new Evaluator.Tag("div"),
                            new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("p"))
                        ),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testCombiningEvaluatorImmediatePreviousSibling() {
        String query = "p + a";
        assertEquals(new CombiningEvaluator.And(
                            new Evaluator.Tag("p"),
                            new StructuralEvaluator.ImmediatePreviousSibling(new Evaluator.Tag("a"))
                        ),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testCombiningEvaluatorPreviousSibling() {
        String query = "p ~ a";
        assertEquals(new CombiningEvaluator.And(
                            new Evaluator.Tag("p"),
                            new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("a"))
                        ),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorId() {
        String query = "#id";
        assertEquals(new Evaluator.Id("id"),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorClass() {
        String query = ".class";
        assertEquals(new Evaluator.Class("class"),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorTag() {
        String query = "div";
        assertEquals(new Evaluator.Tag("div"),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorAttribute() {
        String query = "[name]";
        assertEquals(new Evaluator.Attribute("name"),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorAttributeWithValue() {
        String query = "[name=value]";
        assertEquals(new Evaluator.AttributeWithValue("name", "value"),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorAttributeWithValueNot() {
        String query = "[name!=value]";
        assertEquals(new Evaluator.AttributeWithValueNot("name", "value"),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorAttributeWithValueStarting() {
        String query = "[name^=value]";
        assertEquals(new Evaluator.AttributeWithValueStarting("name", "value"),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorAttributeWithValueEnding() {
        String query = "[name$=value]";
        assertEquals(new Evaluator.AttributeWithValueEnding("name", "value"),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorAttributeWithValueContaining() {
        String query = "[name*=value]";
        assertEquals(new Evaluator.AttributeWithValueContaining("name", "value"),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorAttributeWithValueMatching() {
        String query = "[name~=value]";
        assertEquals(new Evaluator.AttributeWithValueMatching("name", Pattern.compile("value")),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorAllElements() {
        String query = "*";
        assertEquals(new Evaluator.AllElements(),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorIndexLessThan() {
        String query = ":lt(3)";
        assertEquals(new Evaluator.IndexLessThan(3),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorIndexGreaterThan() {
        String query = ":gt(3)";
        assertEquals(new Evaluator.IndexGreaterThan(3),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorIndexEquals() {
        String query = ":eq(3)";
        assertEquals(new Evaluator.IndexEquals(3),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorHas() {
        String query = ":has(p)";
        assertEquals(new StructuralEvaluator.Has(new Evaluator.Tag("p")),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorContains() {
        String query = ":contains(text)";
        assertEquals(new Evaluator.ContainsText("text"),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorContainsOwn() {
        String query = ":containsOwn(text)";
        assertEquals(new Evaluator.ContainsOwnText("text"),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorMatches() {
        String query = ":matches(regex)";
        assertEquals(new Evaluator.Matches(Pattern.compile("regex")),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorMatchesOwn() {
        String query = ":matchesOwn(regex)";
        assertEquals(new Evaluator.MatchesOwn(Pattern.compile("regex")),
                     QueryParser.parse(query));
    }
    
    @Test
    public void testEvaluatorNot() {
        String query = ":not(p)";
        assertEquals(new StructuralEvaluator.Not(new Evaluator.Tag("p")),
                     QueryParser.parse(query));
    }
}