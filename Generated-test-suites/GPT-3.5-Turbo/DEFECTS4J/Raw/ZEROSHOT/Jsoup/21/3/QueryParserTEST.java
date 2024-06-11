import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div";

        Evaluator evaluator = QueryParser.parse(query);

        Assert.assertEquals(Evaluator.Tag.class, evaluator.getClass());
        Assert.assertEquals("div", ((Evaluator.Tag)evaluator).getTagName());
    }

    @Test
    public void testParse_Comma() {
        String query = "div, span";

        Evaluator evaluator = QueryParser.parse(query);

        Assert.assertEquals(Evaluator.Or.class, evaluator.getClass());
        Assert.assertEquals(2, ((Evaluator.Or)evaluator).size());
        Assert.assertEquals(Evaluator.Tag.class, ((Evaluator.Or)evaluator).get(0).getClass());
        Assert.assertEquals("div", ((Evaluator.Tag)((Evaluator.Or)evaluator).get(0)).getTagName());
        Assert.assertEquals(Evaluator.Tag.class, ((Evaluator.Or)evaluator).get(1).getClass());
        Assert.assertEquals("span", ((Evaluator.Tag)((Evaluator.Or)evaluator).get(1)).getTagName());
    }

    @Test
    public void testParse_Combinator_GreaterThan() {
        String query = "div > span";

        Evaluator evaluator = QueryParser.parse(query);

        Assert.assertEquals(Evaluator.And.class, evaluator.getClass());
        Assert.assertEquals(2, ((Evaluator.And)evaluator).size());
        Assert.assertEquals(Evaluator.Tag.class, ((Evaluator.And)evaluator).get(0).getClass());
        Assert.assertEquals("div", ((Evaluator.Tag)((Evaluator.And)evaluator).get(0)).getTagName());
        Assert.assertEquals(Evaluator.Parent.class, ((Evaluator.And)evaluator).get(1).getClass());
        Assert.assertEquals(Evaluator.Tag.class, ((Evaluator.Parent)((Evaluator.And)evaluator).get(1)).getBy().getClass());
        Assert.assertEquals("span", ((Evaluator.Tag)((Evaluator.Parent)((Evaluator.And)evaluator).get(1)).getBy()).getTagName());
    }

    @Test
    public void testParse_Combinator_Space() {
        String query = "div span";

        Evaluator evaluator = QueryParser.parse(query);

        Assert.assertEquals(Evaluator.And.class, evaluator.getClass());
        Assert.assertEquals(2, ((Evaluator.And)evaluator).size());
        Assert.assertEquals(Evaluator.Tag.class, ((Evaluator.And)evaluator).get(0).getClass());
        Assert.assertEquals("div", ((Evaluator.Tag)((Evaluator.And)evaluator).get(0)).getTagName());
        Assert.assertEquals(Evaluator.Parent.class, ((Evaluator.And)evaluator).get(1).getClass());
        Assert.assertEquals(Evaluator.Tag.class, ((Evaluator.Parent)((Evaluator.And)evaluator).get(1)).getBy().getClass());
        Assert.assertEquals("span", ((Evaluator.Tag)((Evaluator.Parent)((Evaluator.And)evaluator).get(1)).getBy()).getTagName());
    }

    @Test
    public void testParse_Combinator_Plus() {
        String query = "div + span";

        Evaluator evaluator = QueryParser.parse(query);

        Assert.assertEquals(Evaluator.And.class, evaluator.getClass());
        Assert.assertEquals(2, ((Evaluator.And)evaluator).size());
        Assert.assertEquals(Evaluator.Tag.class, ((Evaluator.And)evaluator).get(0).getClass());
        Assert.assertEquals("div", ((Evaluator.Tag)((Evaluator.And)evaluator).get(0)).getTagName());
        Assert.assertEquals(Evaluator.ImmediatePreviousSibling.class, ((Evaluator.And)evaluator).get(1).getClass());
        Assert.assertEquals(Evaluator.Tag.class, ((Evaluator.ImmediatePreviousSibling)((Evaluator.And)evaluator).get(1)).getBy().getClass());
        Assert.assertEquals("span", ((Evaluator.Tag)((Evaluator.ImmediatePreviousSibling)((Evaluator.And)evaluator).get(1)).getBy()).getTagName());
    }

    @Test
    public void testParse_Combinator_Tilde() {
        String query = "div ~ span";

        Evaluator evaluator = QueryParser.parse(query);

        Assert.assertEquals(Evaluator.And.class, evaluator.getClass());
        Assert.assertEquals(2, ((Evaluator.And)evaluator).size());
        Assert.assertEquals(Evaluator.Tag.class, ((Evaluator.And)evaluator).get(0).getClass());
        Assert.assertEquals("div", ((Evaluator.Tag)((Evaluator.And)evaluator).get(0)).getTagName());
        Assert.assertEquals(Evaluator.PreviousSibling.class, ((Evaluator.And)evaluator).get(1).getClass());
        Assert.assertEquals(Evaluator.Tag.class, ((Evaluator.PreviousSibling)((Evaluator.And)evaluator).get(1)).getBy().getClass());
        Assert.assertEquals("span", ((Evaluator.Tag)((Evaluator.PreviousSibling)((Evaluator.And)evaluator).get(1)).getBy()).getTagName());
    }

    @Test
    public void testParse_Combinator_Unknown() {
        try {
            String query = "div @ span";

            QueryParser.parse(query);

            Assert.fail("Exception should have been thrown");
        } catch (Exception e) {
            Assert.assertEquals(Selector.SelectorParseException.class, e.getClass());
            Assert.assertEquals("Unknown combinator: @", e.getMessage());
        }
    }

    @Test
    public void testParse_Has() {
        String query = "div:has(span)";

        Evaluator evaluator = QueryParser.parse(query);

        Assert.assertEquals(Evaluator.Has.class, evaluator.getClass());
        Assert.assertEquals(Evaluator.Tag.class, ((Evaluator.Has)evaluator).getBy().getClass());
        Assert.assertEquals("span", ((Evaluator.Tag)((Evaluator.Has)evaluator).getBy()).getTagName());
    }

    @Test
    public void testParse_Contains() {
        String query = "div:contains(someText)";

        Evaluator evaluator = QueryParser.parse(query);

        Assert.assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
        Assert.assertEquals("someText", ((Evaluator.ContainsText)evaluator).getText());
    }

    @Test
    public void testParse_ContainsOwn() {
        String query = "div:containsOwn(someText)";

        Evaluator evaluator = QueryParser.parse(query);

        Assert.assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
        Assert.assertEquals("someText", ((Evaluator.ContainsOwnText)evaluator).getText());
    }

    @Test
    public void testParse_Matches() {
        String query = "div:matches(regex)";

        Evaluator evaluator = QueryParser.parse(query);

        Assert.assertEquals(Evaluator.Matches.class, evaluator.getClass());
        Assert.assertEquals("regex", ((Evaluator.Matches)evaluator).getPattern().pattern());
    }

    @Test
    public void testParse_MatchesOwn() {
        String query = "div:matchesOwn(regex)";

        Evaluator evaluator = QueryParser.parse(query);

        Assert.assertEquals(Evaluator.MatchesOwn.class, evaluator.getClass());
        Assert.assertEquals("regex", ((Evaluator.MatchesOwn)evaluator).getPattern().pattern());
    }

    @Test
    public void testParse_Not() {
        String query = "div:not(span)";

        Evaluator evaluator = QueryParser.parse(query);

        Assert.assertEquals(Evaluator.Not.class, evaluator.getClass());
        Assert.assertEquals(Evaluator.Tag.class, ((Evaluator.Not)evaluator).getBy().getClass());
        Assert.assertEquals("span", ((Evaluator.Tag)((Evaluator.Not)evaluator).getBy()).getTagName());
    }
}