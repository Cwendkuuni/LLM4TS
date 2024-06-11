
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class QueryParserTest {

    @Test
    public void testParse_EmptyQuery_ReturnsTrue() {
        //Arrange
        String query = "";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.AllElements);
    }

    @Test
    public void testParse_ElementQuery_ReturnsCorrectEvaluator() {
        //Arrange
        String query = "div";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator).tagName());
    }

    @Test
    public void testParse_IdQuery_ReturnsCorrectEvaluator() {
        //Arrange
        String query = "#myId";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("myId", ((Evaluator.Id) evaluator).id());
    }

    @Test
    public void testParse_ClassQuery_ReturnsCorrectEvaluator() {
        //Arrange
        String query = ".myClass";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("myClass", ((Evaluator.Class) evaluator).className());
    }

    @Test
    public void testParse_AttributeQuery_ReturnsCorrectEvaluator() {
        //Arrange
        String query = "[attr]";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.Attribute);
        assertEquals("attr", ((Evaluator.Attribute) evaluator).key());
    }

    @Test
    public void testParse_CombinatorChild_ReturnsCorrectEvaluator() {
        //Arrange
        String query = "div > span";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.And);
        assertTrue(((Evaluator.And) evaluator).evaluators().get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) ((Evaluator.And) evaluator).evaluators().get(0)).tagName());
        assertTrue(((Evaluator.And) evaluator).evaluators().get(1) instanceof Evaluator.ImmediateParent);
        assertTrue(((Evaluator.ImmediateParent) ((Evaluator.And) evaluator).evaluators().get(1)).parentEvaluator() instanceof Evaluator.Tag);
        assertEquals("span", ((Evaluator.Tag) ((Evaluator.ImmediateParent) ((Evaluator.And) evaluator).evaluators().get(1)).parentEvaluator()).tagName());
    }

    @Test
    public void testParse_CombinatorDescendant_ReturnsCorrectEvaluator() {
        //Arrange
        String query = "div span";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.And);
        assertTrue(((Evaluator.And) evaluator).evaluators().get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) ((Evaluator.And) evaluator).evaluators().get(0)).tagName());
        assertTrue(((Evaluator.And) evaluator).evaluators().get(1) instanceof Evaluator.Parent);
        assertTrue(((Evaluator.Parent) ((Evaluator.And) evaluator).evaluators().get(1)).parentEvaluator() instanceof Evaluator.Tag);
        assertEquals("span", ((Evaluator.Tag) ((Evaluator.Parent) ((Evaluator.And) evaluator).evaluators().get(1)).parentEvaluator()).tagName());
    }

    @Test
    public void testParse_CombinatorSibling_ReturnsCorrectEvaluator() {
        //Arrange
        String query = "div + span";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.And);
        assertTrue(((Evaluator.And) evaluator).evaluators().get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) ((Evaluator.And) evaluator).evaluators().get(0)).tagName());
        assertTrue(((Evaluator.And) evaluator).evaluators().get(1) instanceof Evaluator.ImmediatePreviousSibling);
        assertTrue(((Evaluator.ImmediatePreviousSibling) ((Evaluator.And) evaluator).evaluators().get(1)).previousSiblingEvaluator() instanceof Evaluator.Tag);
        assertEquals("span", ((Evaluator.Tag) ((Evaluator.ImmediatePreviousSibling) ((Evaluator.And) evaluator).evaluators().get(1)).previousSiblingEvaluator()).tagName());
    }

    @Test
    public void testParse_CombinatorGeneralSibling_ReturnsCorrectEvaluator() {
        //Arrange
        String query = "div ~ span";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.And);
        assertTrue(((Evaluator.And) evaluator).evaluators().get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) ((Evaluator.And) evaluator).evaluators().get(0)).tagName());
        assertTrue(((Evaluator.And) evaluator).evaluators().get(1) instanceof Evaluator.PreviousSibling);
        assertTrue(((Evaluator.PreviousSibling) ((Evaluator.And) evaluator).evaluators().get(1)).previousSiblingEvaluator() instanceof Evaluator.Tag);
        assertEquals("span", ((Evaluator.Tag) ((Evaluator.PreviousSibling) ((Evaluator.And) evaluator).evaluators().get(1)).previousSiblingEvaluator()).tagName());
    }

    @Test
    public void testParse_PseudoSelectorHas_ReturnsCorrectEvaluator() {
        //Arrange
        String query = ":has(span)";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.Has);
        assertTrue(((Evaluator.Has) evaluator).hasEvaluator() instanceof Evaluator.Tag);
        assertEquals("span", ((Evaluator.Tag) ((Evaluator.Has) evaluator).hasEvaluator()).tagName());
    }

    @Test
    public void testParse_PseudoSelectorContains_ReturnsCorrectEvaluator() {
        //Arrange
        String query = ":contains(text)";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.ContainsText);
        assertEquals("text", ((Evaluator.ContainsText) evaluator).text());
    }

    @Test
    public void testParse_PseudoSelectorContainsOwn_ReturnsCorrectEvaluator() {
        //Arrange
        String query = ":containsOwn(text)";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
        assertEquals("text", ((Evaluator.ContainsOwnText) evaluator).text());
    }

    @Test
    public void testParse_PseudoSelectorMatches_ReturnsCorrectEvaluator() {
        //Arrange
        String query = ":matches(regex)";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.Matches);
        assertEquals("regex", ((Evaluator.Matches) evaluator).pattern().pattern());
    }

    @Test
    public void testParse_PseudoSelectorMatchesOwn_ReturnsCorrectEvaluator() {
        //Arrange
        String query = ":matchesOwn(regex)";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.MatchesOwn);
        assertEquals("regex", ((Evaluator.MatchesOwn) evaluator).pattern().pattern());
    }

    @Test
    public void testParse_PseudoSelectorNot_ReturnsCorrectEvaluator() {
        //Arrange
        String query = ":not(span)";

        //Act
        Evaluator evaluator = QueryParser.parse(query);

        //Assert
        assertTrue(evaluator instanceof Evaluator.Not);
        assertTrue(((Evaluator.Not) evaluator).outerEvaluator() instanceof Evaluator.Tag);
        assertEquals("span", ((Evaluator.Tag) ((Evaluator.Not) evaluator).outerEvaluator()).tagName());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_InvalidQuery_ThrowsException() {
        //Arrange
        String query = "[attr";

        //Act
        QueryParser.parse(query);
    }

}
