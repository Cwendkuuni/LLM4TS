import org.jsoup.select.*;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        QueryParser qp = new QueryParser("div.class");
        Evaluator result = qp.parse();
        // Add test assertions here
    }

    @Test
    public void testCombinator() {
        QueryParser qp = new QueryParser("div > span");
        qp.combinator('>');
        // Add test assertions here
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser qp = new QueryParser(":containsOwn(text)");
        String result = qp.consumeSubQuery();
        // Add test assertions here
    }

    @Test
    public void testFindElements() {
        QueryParser qp = new QueryParser(":has(p)");
        qp.findElements();
        // Add test assertions here
    }

    @Test
    public void testById() {
        QueryParser qp = new QueryParser("#id");
        qp.byId();
        // Add test assertions here
    }

    @Test
    public void testByClass() {
        QueryParser qp = new QueryParser(".class");
        qp.byClass();
        // Add test assertions here
    }

    @Test
    public void testByTag() {
        QueryParser qp = new QueryParser("a");
        qp.byTag();
        // Add test assertions here
    }

    @Test
    public void testByAttribute() {
        QueryParser qp = new QueryParser("[attr=value]");
        qp.byAttribute();
        // Add test assertions here
    }

    @Test
    public void testAllElements() {
        QueryParser qp = new QueryParser("*");
        qp.allElements();
        // Add test assertions here
    }

    @Test
    public void testIndexLessThan() {
        QueryParser qp = new QueryParser(":lt(2)");
        qp.indexLessThan();
        // Add test assertions here
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser qp = new QueryParser(":gt(2)");
        qp.indexGreaterThan();
        // Add test assertions here
    }

    @Test
    public void testIndexEquals() {
        QueryParser qp = new QueryParser(":eq(3)");
        qp.indexEquals();
        // Add test assertions here
    }

    @Test
    public void testCssNthChild() {
        QueryParser qp = new QueryParser(":nth-child(2n+1)");
        qp.cssNthChild(false, false);
        // Add test assertions here
    }

    @Test
    public void testHas() {
        QueryParser qp = new QueryParser(":has(div)");
        qp.has();
        // Add test assertions here
    }

    @Test
    public void testContains() {
        QueryParser qp = new QueryParser(":contains(text)");
        qp.contains(false);
        // Add test assertions here
    }

    @Test
    public void testContainsOwn() {
        QueryParser qp = new QueryParser(":containsOwn(text)");
        qp.contains(true);
        // Add test assertions here
    }

    @Test
    public void testContainsData() {
        QueryParser qp = new QueryParser(":containsData(data)");
        qp.containsData();
        // Add test assertions here
    }

    @Test
    public void testMatches() {
        QueryParser qp = new QueryParser(":matches(regex)");
        qp.matches(false);
        // Add test assertions here
    }

    @Test
    public void testNot() {
        QueryParser qp = new QueryParser(":not(div)");
        qp.not();
        // Add test assertions here
    }
}