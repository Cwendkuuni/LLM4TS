import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Evaluator;
import org.jsoup.parser.StructuralEvaluator;
import org.jsoup.parser.CombiningEvaluator;
import org.jsoup.parser.And;
import org.jsoup.parser.Or;
import org.jsoup.parser.Tag;
import org.jsoup.parser.Id;
import org.jsoup.parser.Class;
import org.jsoup.parser.Attribute;
import org.jsoup.parser.AttributeWithValue;
import org.jsoup.parser.AttributeWithValueStarting;
import org.jsoup.parser.AttributeWithValueEnding;
import org.jsoup.parser.AttributeWithValueContaining;
import org.jsoup.parser.IsFirstChild;
import org.jsoup.parser.IsLastChild;
import org.jsoup.parser.IsFirstOfType;
import org.jsoup.parser.IsLastOfType;
import org.jsoup.parser.IsEmpty;
import org.jsoup.parser.IsRoot;
import org.jsoup.parser.IsNthChild;
import org.jsoup.parser.IsNthLastChild;
import org.jsoup.parser.IsNthOfType;
import org.jsoup.parser.IsNthLastOfType;
import org.jsoup.parser.AllElements;
import org.jsoup.parser.ElementFilter;
import org.jsoup.parser.Has;
import org.jsoup.parser.ContainsText;
import org.jsoup.parser.ContainsOwnText;
import org.jsoup.parser.ContainsData;
import org.jsoup.parser.Matches;
import org.jsoup.parser.MatchesOwn;
import org.jsoup.parser.Not;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals(new And(new AllElements()), evaluator);
    }

    @Test
    public void testParseTagQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Tag tag = new Tag("div");
        assertEquals(tag, evaluator);
    }

    @Test
    public void testParseClassQuery() {
        String query = ".class";
        Evaluator evaluator = QueryParser.parse(query);
        Class classEvaluator = new Class("class");
        assertEquals(classEvaluator, evaluator);
    }

    @Test
    public void testParseIdQuery() {
        String query = "#id";
        Evaluator evaluator = QueryParser.parse(query);
        Id idEvaluator = new Id("id");
        assertEquals(idEvaluator, evaluator);
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "[attr=value]";
        Evaluator evaluator = QueryParser.parse(query);
        AttributeWithValue attributeEvaluator = new AttributeWithValue("attr", "value");
        assertEquals(attributeEvaluator, evaluator);
    }

    @Test
    public void testParseAttributeStartingQuery() {
        String query = "[attr^=value]";
        Evaluator evaluator = QueryParser.parse(query);
        AttributeWithValueStarting attributeStartingEvaluator = new AttributeWithValueStarting("attr", "value");
        assertEquals(attributeStartingEvaluator, evaluator);
    }

    @Test
    public void testParseAttributeEndingQuery() {
        String query = "[attr$=value]";
        Evaluator evaluator = QueryParser.parse(query);
        AttributeWithValueEnding attributeEndingEvaluator = new AttributeWithValueEnding("attr", "value");
        assertEquals(attributeEndingEvaluator, evaluator);
    }

    @Test
    public void testParseAttributeContainingQuery() {
        String query = "[attr*=value]";
        Evaluator evaluator = QueryParser.parse(query);
        AttributeWithValueContaining attributeContainingEvaluator = new AttributeWithValueContaining("attr", "value");
        assertEquals(attributeContainingEvaluator, evaluator);
    }

    @Test
    public void testParseDefaultAttributeQuery() {
        String query = "[attr]";
        Evaluator evaluator = QueryParser.parse(query);
        Attribute attributeEvaluator = new Attribute("attr");
        assertEquals(attributeEvaluator, evaluator);
    }

    @Test
    public void testParseCombinedQuery() {
        String query = "div.class";
        Evaluator evaluator = QueryParser.parse(query);
        And combinator = new And(new Tag("div"), new Class("class"));
        assertEquals(combinator, evaluator);
    }

    @Test
    public void testParseCombinedWithIDQuery() {
        String query = "div#id.class";
        Evaluator evaluator = QueryParser.parse(query);
        And combinator = new And(new Tag("div"), new Id("id"), new Class("class"));
        assertEquals(combinator, evaluator);
    }

    @Test
    public void testParseCombinedWithAttributeQuery() {
        String query = "div[attr=value].class";
        Evaluator evaluator = QueryParser.parse(query);
        And combinator = new And(new Tag("div"), new AttributeWithValue("attr", "value"), new Class("class"));
        assertEquals(combinator, evaluator);
    }

    @Test
    public void testParseCombinedWithAttributeStartingQuery() {
        String query = "div[attr^=value].class";
        Evaluator evaluator = QueryParser.parse(query);
        And combinator = new And(new Tag("div"), new AttributeWithValueStarting("attr", "value"), new Class("class"));
        assertEquals(combinator, evaluator);
    }

    @Test
    public void testParseCombinedWithAttributeEndingQuery() {
        String query = "div[attr$=value].class";
        Evaluator evaluator = QueryParser.parse(query);
        And combinator = new And(new Tag("div"), new AttributeWithValueEnding("attr", "value"), new Class("class"));
        assertEquals(combinator, evaluator);
    }

    @Test
    public void testParseCombinedWithAttributeContainingQuery() {
        String query = "div[attr*=value].class";
        Evaluator evaluator = QueryParser.parse(query);
        And combinator = new And(new Tag("div"), new AttributeWithValueContaining("attr", "value"), new Class("class"));
        assertEquals(combinator, evaluator);
    }

    @Test
    public void testParseCombinedWithMultipleAttributesQuery() {
        String query = "div[attr1=value1][attr2=value2].class";
        Evaluator evaluator = QueryParser.parse(query);
        And combinator = new And(new Tag("div"), new AttributeWithValue("attr1", "value1"), new AttributeWithValue("attr2", "value2"), new Class("class"));
        assertEquals(combinator, evaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidCombinatorQuery() {
        String query = "div%class";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseMissingCombinatorQuery() {
        String query = "divclass";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseMissingElementQuery() {
        String query = "class";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseMissingAttributeQuery() {
        String query = "div[attr]";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseEmptyAttributeValueQuery() {
        String query = "div[attr]";
        QueryParser.parse(query);
    }

    @Test
    public void testParseDescendantQuery() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        StructuralEvaluator.Descendant descendant = new StructuralEvaluator.Descendant("p", "div");
        assertEquals(descendant, evaluator);
    }

    @Test
    public void testParseSiblingQuery() {
        String query = "div ~ p";
        Evaluator evaluator = QueryParser.parse(query);
        StructuralEvaluator.PreviousSibling previousSibling = new StructuralEvaluator.PreviousSibling("p", "div");
        assertEquals(previousSibling, evaluator);
    }

    @Test
    public void testParseChildQuery() {
        String query = "div > p:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        StructuralEvaluator.ImmediateChild immediateChild = new StructuralEvaluator.ImmediateChild("p", "div");
        IsFirstChild firstChild = new IsFirstChild();
        And childAndFirstChild = new And(immediateChild, firstChild);
        assertEquals(childAndFirstChild, evaluator);
    }

    @Test
    public void testParsePrevSiblingQuery() {
        String query = "div ~ p:first-child";
        Evaluator evaluator = QueryParser.parse(query);
        StructuralEvaluator.PreviousSibling previousSibling = new StructuralEvaluator.PreviousSibling("p", "div");
        IsFirstChild firstChild = new IsFirstChild();
        And previousSiblingAndFirstChild = new And(previousSibling, firstChild);
        assertEquals(previousSiblingAndFirstChild, evaluator);
    }

    @Test
    public void testParseNthChildQuery() {
        String query = "div:nth-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        IsNthChild nthChild = new IsNthChild(2);
        assertEquals(nthChild, evaluator);
    }

    @Test
    public void testParseNthLastChildQuery() {
        String query = "div:nth-last-child(2)";
        Evaluator evaluator = QueryParser.parse(query);
        IsNthLastChild nthLastChild = new IsNthLastChild(2);
        assertEquals(nthLastChild, evaluator);
    }

    @Test
    public void testParseNthOfTypeQuery() {
        String query = "div:nth-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);
        IsNthOfType nthOfType = new IsNthOfType(2);
        assertEquals(nthOfType, evaluator);
    }

    @Test
    public void testParseNthLastOfTypeQuery() {
        String query = "div:nth-last-of-type(2)";
        Evaluator evaluator = QueryParser.parse(query);
        IsNthLastOfType nthLastOfType = new IsNthLastOfType(2);
        assertEquals(nthLastOfType, evaluator);
    }

    @Test
    public void testParseHasQuery() {
        String query = "div:has(p)";
        Evaluator evaluator = QueryParser.parse(query);
        Has has = new Has(new Tag("p"));
        assertEquals(has, evaluator);
    }

    @Test
    public void testParseContainsTextQuery() {
        String query = "div:contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        ContainsText containsText = new ContainsText("text");
        assertEquals(containsText, evaluator);
    }

    @Test
    public void testParseContainsOwnTextQuery() {
        String query = "div:containsOwn(text)";
        Evaluator evaluator = QueryParser.parse(query);
        ContainsOwnText containsOwnText = new ContainsOwnText("text");
        assertEquals(containsOwnText, evaluator);
    }

    @Test
    public void testParseContainsDataQuery() {
        String query = "div:containsData(data)";
        Evaluator evaluator = QueryParser.parse(query);
        ContainsData containsData = new ContainsData("data");
        assertEquals(containsData, evaluator);
    }

    @Test
    public void testParseMatchesQuery() {
        String query = "div:matches(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        Matches matches = new Matches(Pattern.compile(query.substring(7, query.length() - 1)));
        assertEquals(matches, evaluator);
    }

    @Test
    public void testParseMatchesOwnQuery() {
        String query = "div:matchesOwn(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        MatchesOwn matchesOwn = new MatchesOwn(Pattern.compile(query.substring(11, query.length() - 1)));
        assertEquals(matchesOwn, evaluator);
    }

    @Test
    public void testParseNotQuery() {
        String query = "div:not(p)";
        Evaluator evaluator = QueryParser.parse(query);
        Not not = new Not(new Tag("p"));
        assertEquals(not, evaluator);
    }
}