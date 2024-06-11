import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = new ArrayList<>();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseSimpleSelector() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("div"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseSelectorWithId() {
        String query = "#myId";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("#myId"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseSelectorWithClass() {
        String query = ".myClass";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector(".myClass"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseSelectorWithTag() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("p"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseSelectorWithAttribute() {
        String query = "[href]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("[href]"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseSelectorWithAttributeValue() {
        String query = "[href='http://example.com']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("[href='http://example.com']"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseSelectorWithMultipleAttributes() {
        String query = "input[type='text'][name='username']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("input[type='text'][name='username']"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseSelectorWithDescendantSelector() {
        String query = "div ul li";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("div > ul > li"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseSelectorWithCombinator() {
        String query = "div > p + ul";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("div > p"), new Selector("div > p + ul"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div [=href]"; // missing attribute name
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQueryWithInvalidCombinator() {
        String query = "div > x"; // x is not a valid combinator
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @Test
    public void testParseQueryWithNthChild() {
        String query = "li:nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("li:nth-child(2)"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseQueryWithNthLastChild() {
        String query = "li:nth-last-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("li:nth-last-child(2)"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseQueryWithNthOfType() {
        String query = "li:nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("li:nth-of-type(2)"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseQueryWithNthLastOfType() {
        String query = "li:nth-last-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("li:nth-last-of-type(2)"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseQueryWithAdjacentSibling() {
        String query = "li > ul > li + li";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("li > ul > li"), new Selector("li > ul > li + li"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseQueryWithGeneralSibling() {
        String query = "li > ul > li ~ li";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("li > ul > li"), new Selector("li > ul > li ~ li"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseQueryWithAttributeContains() {
        String query = "a[href*='example']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("a[href*='example']"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseQueryWithAttributeStartsWith() {
        String query = "a[href^='http']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("a[href^='http']"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseQueryWithAttributeEndsWith() {
        String query = "a[href$='.pdf']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("a[href$='.pdf']"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseQueryWithAttributeIs() {
        String query = "input[type='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("input[type='text']"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseQueryWithAttributeIsNot() {
        String query = "input:not([type='text'])";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        List<Selector> actualResult = evaluator.toStringList();
        List<Selector> expectedResult = List.of(new Selector("input:not([type='text'])"));
        Assert.assertEquals(expectedResult, actualResult);
    }
}