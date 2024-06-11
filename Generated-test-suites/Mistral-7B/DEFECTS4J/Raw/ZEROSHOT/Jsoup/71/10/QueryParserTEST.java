package org.jsoup.select;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser queryParser = new QueryParser("");
        Evaluator evaluator = queryParser.parse();
        assertNull(evaluator);
    }

    @Test
    public void testParseSingleTagSelector() {
        QueryParser queryParser = new QueryParser("div");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(Evaluator.Tag.class, evaluator);
        Evaluator.Tag tagEvaluator = (Evaluator.Tag) evaluator;
        assertEquals("div", tagEvaluator.tagName);
    }

    @Test
    public void testParseClassSelector() {
        QueryParser queryParser = new QueryParser(".class-name");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(Evaluator.Class.class, evaluator);
        Evaluator.Class classEvaluator = (Evaluator.Class) evaluator;
        assertEquals("class-name", classEvaluator.className);
    }

    @Test
    public void testParseIDSelector() {
        QueryParser queryParser = new QueryParser("#id-name");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(Evaluator.Id.class, evaluator);
        Evaluator.Id idEvaluator = (Evaluator.Id) evaluator;
        assertEquals("id-name", idEvaluator.id);
    }

    @Test
    public void testParseAttributeSelector() {
        QueryParser queryParser = new QueryParser("[data-test='test']");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(Evaluator.Attribute.class, evaluator);
        Evaluator.Attribute attributeEvaluator = (Evaluator.Attribute) evaluator;
        assertEquals("data-test", attributeEvaluator.attributeName);
        assertEquals("test", attributeEvaluator.attributeValue);
    }

    @Test
    public void testParseCombinedSelectors() {
        QueryParser queryParser = new QueryParser("div.class-name#id-name.other-class");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        List<Evaluator> evaluators = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(3, evaluators.size());
        Evaluator tagEvaluator = evaluators.get(0);
        assertInstanceOf(Evaluator.Tag.class, tagEvaluator);
        Evaluator.Tag tagEvaluatorClass = (Evaluator.Tag) tagEvaluator;
        assertEquals("div", tagEvaluatorClass.tagName);
        Evaluator classEvaluator = evaluators.get(1);
        assertInstanceOf(Evaluator.Class.class, classEvaluator);
        Evaluator.Class classEvaluatorClass = (Evaluator.Class) classEvaluator;
        assertEquals("class-name", classEvaluatorClass.className);
        Evaluator idEvaluator = evaluators.get(2);
        assertInstanceOf(Evaluator.Id.class, idEvaluator);
        Evaluator.Id idEvaluatorID = (Evaluator.Id) idEvaluator;
        assertEquals("id-name", idEvaluatorID.id);
    }

    @Test
    public void testParseCombinedSelectorsWithCombinator() {
        QueryParser queryParser = new QueryParser("div > .class-name");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        List<Evaluator> evaluators = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(2, evaluators.size());
        Evaluator tagEvaluator = evaluators.get(0);
        assertInstanceOf(Evaluator.Tag.class, tagEvaluator);
        Evaluator.Tag tagEvaluatorTag = (Evaluator.Tag) tagEvaluator;
        assertEquals("div", tagEvaluatorTag.tagName);
        Evaluator classEvaluator = evaluators.get(1);
        assertInstanceOf(Evaluator.Class.class, classEvaluator);
        Evaluator.Class classEvaluatorClass = (Evaluator.Class) classEvaluator;
        assertEquals("class-name", classEvaluatorClass.className);
        assertInstanceOf(StructuralEvaluator.ImmediateParent.class, classEvaluator.getChildEvaluator());
    }

    @Test
    public void testParseCombinedSelectorsWithCombinatorAndAttribute() {
        QueryParser queryParser = new QueryParser("div > .class-name[data-test='test']");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        List<Evaluator> evaluators = ((CombiningEvaluator.And) evaluator).getChildren();
        assertEquals(3, evaluators.size());
        Evaluator tagEvaluator = evaluators.get(0);
        assertInstanceOf(Evaluator.Tag.class, tagEvaluator);
        Evaluator.Tag tagEvaluatorTag = (Evaluator.Tag) tagEvaluator;
        assertEquals("div", tagEvaluatorTag.tagName);
        Evaluator classEvaluator = evaluators.get(1);
        assertInstanceOf(Evaluator.Class.class, classEvaluator);
        Evaluator.Class classEvaluatorClass = (Evaluator.Class) classEvaluator;
        assertEquals("class-name", classEvaluatorClass.className);
        Evaluator attributeEvaluator = evaluators.get(2);
        assertInstanceOf(Evaluator.Attribute.class, attributeEvaluator);
        Evaluator.Attribute attributeEvaluatorAttribute = (Evaluator.Attribute) attributeEvaluator;
        assertEquals("data-test", attributeEvaluatorAttribute.attributeName);
        assertEquals("test", attributeEvaluatorAttribute.attributeValue);
        assertInstanceOf(StructuralEvaluator.ImmediateParent.class, classEvaluator.getChildEvaluator());
    }

    @Test
    public void testParsePseudoSelector() {
        QueryParser queryParser = new QueryParser(":contains(test)");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(Evaluator.ContainsText.class, evaluator);
        Evaluator.ContainsText containsTextEvaluator = (Evaluator.ContainsText) evaluator;
        assertEquals("test", containsTextEvaluator.searchText);
    }

    @Test
    public void testParsePseudoSelectorWithOwnAttribute() {
        QueryParser queryParser = new QueryParser(":containsOwn(test)");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(Evaluator.ContainsOwnText.class, evaluator);
        Evaluator.ContainsOwnText containsOwnTextEvaluator = (Evaluator.ContainsOwnText) evaluator;
        assertEquals("test", containsOwnTextEvaluator.searchText);
    }

    @Test
    public void testParsePseudoSelectorWithDataAttribute() {
        QueryParser queryParser = new QueryParser(":containsData(data-test='test')");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(Evaluator.ContainsData.class, evaluator);
        Evaluator.ContainsData containsDataEvaluator = (Evaluator.ContainsData) evaluator;
        assertEquals("data-test", containsDataEvaluator.attributeName);
        assertEquals("test", containsDataEvaluator.searchText);
    }

    @Test
    public void testParsePseudoSelectorWithMatchesAttribute() {
        QueryParser queryParser = new QueryParser(":matches(.*test.*)");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(Evaluator.Matches.class, evaluator);
        Evaluator.Matches matchesEvaluator = (Evaluator.Matches) evaluator;
        assertEquals(Pattern.compile(".*test.*"), matchesEvaluator.regexPattern);
    }

    @Test
    public void testParsePseudoSelectorWithMatchesOwnAttribute() {
        QueryParser queryParser = new QueryParser(":matchesOwn(.*test.*)");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(Evaluator.MatchesOwn.class, evaluator);
        Evaluator.MatchesOwn matchesOwnEvaluator = (Evaluator.MatchesOwn) evaluator;
        assertEquals(Pattern.compile(".*test.*"), matchesOwnEvaluator.regexPattern);
    }

    @Test
    public void testParsePseudoSelectorWithNot() {
        QueryParser queryParser = new QueryParser(":not(.class-name)");
        Evaluator evaluator = queryParser.parse();
        assertNotNull(evaluator);
        assertInstanceOf(StructuralEvaluator.Not.class, evaluator);
        StructuralEvaluator.Not notEvaluator = (StructuralEvaluator.Not) evaluator;
        assertInstanceOf(Evaluator.Class.class, notEvaluator.getChildEvaluator());
        Evaluator.Class classEvaluator = (Evaluator.Class) notEvaluator.getChildEvaluator();
        assertEquals("class-name", classEvaluator.className);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        QueryParser queryParser = new QueryParser("*");
        queryParser.parse();
    }
}