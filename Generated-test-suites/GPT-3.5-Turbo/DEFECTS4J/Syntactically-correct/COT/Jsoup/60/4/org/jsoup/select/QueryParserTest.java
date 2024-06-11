import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class QueryParserTest {

    private QueryParser queryParser;

    @Before
    public void setUp() {
        queryParser = new QueryParser("");
    }

    @Test
    public void testParse() {
        Evaluator evaluator = queryParser.parse("testQuery");
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        queryParser.combinator('>');
        queryParser.combinator(',');
        queryParser.combinator('+');
        queryParser.combinator('~');
        queryParser.combinator(' ');
    }

    @Test
    public void testConsumeSubQuery() {
        String subQuery = queryParser.consumeSubQuery();
        assertEquals("", subQuery);
    }

    @Test
    public void testFindElements() {
        queryParser.findElements();
    }

    @Test
    public void testById() {
        queryParser.byId();
    }

    @Test
    public void testByClass() {
        queryParser.byClass();
    }

    @Test
    public void testByTag() {
        queryParser.byTag();
    }

    @Test
    public void testByAttribute() {
        queryParser.byAttribute();
    }

    @Test
    public void testAllElements() {
        queryParser.allElements();
    }

    @Test
    public void testIndexLessThan() {
        queryParser.indexLessThan();
    }

    @Test
    public void testIndexGreaterThan() {
        queryParser.indexGreaterThan();
    }

    @Test
    public void testIndexEquals() {
        queryParser.indexEquals();
    }

    @Test
    public void testCssNthChild() {
        queryParser.cssNthChild(false, false);
        queryParser.cssNthChild(true, false);
        queryParser.cssNthChild(false, true);
        queryParser.cssNthChild(true, true);
    }

    @Test
    public void testHas() {
        queryParser.has();
    }

    @Test
    public void testContains() {
        queryParser.contains(false);
        queryParser.contains(true);
    }

    @Test
    public void testContainsData() {
        queryParser.containsData();
    }

    @Test
    public void testMatches() {
        queryParser.matches(false);
        queryParser.matches(true);
    }

    @Test
    public void testNot() {
        queryParser.not();
    }
}