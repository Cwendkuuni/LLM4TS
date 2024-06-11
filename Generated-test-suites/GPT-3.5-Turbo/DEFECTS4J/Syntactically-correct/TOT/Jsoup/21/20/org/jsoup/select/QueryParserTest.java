import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse_WithEmptyQuery_ReturnsNull() {
        String query = "";
        Evaluator result = QueryParser.parse(query);
        assertNull(result);
    }

    @Test
    public void testParse_WithCombinator_ReturnsRootEvaluator() {
        String query = ">div";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) result;
        Evaluator firstEvaluator = andEvaluator.getEvaluators().get(0);
        assertTrue(firstEvaluator instanceof Evaluator.Tag);
        Evaluator.Tag tagEvaluator = (Evaluator.Tag) firstEvaluator;
        assertEquals("div", tagEvaluator.tagName());
    }

    @Test
    public void testParse_WithMultipleCombinators_ReturnsCorrectEvaluatorList() {
        String query = ">div span.cls";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) result;

        assertTrue(andEvaluator.getEvaluators().get(0) instanceof Evaluator.Tag);
        Evaluator.Tag tagEvaluator1 = (Evaluator.Tag) andEvaluator.getEvaluators().get(0);
        assertEquals("div", tagEvaluator1.tagName());

        assertTrue(andEvaluator.getEvaluators().get(1) instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator2 = (CombiningEvaluator.And) andEvaluator.getEvaluators().get(1);

        assertTrue(andEvaluator2.getEvaluators().get(0) instanceof Evaluator.Tag);
        Evaluator.Tag tagEvaluator2 = (Evaluator.Tag) andEvaluator2.getEvaluators().get(0);
        assertEquals("span", tagEvaluator2.tagName());

        assertTrue(andEvaluator2.getEvaluators().get(1) instanceof Evaluator.Class);
        Evaluator.Class classEvaluator = (Evaluator.Class) andEvaluator2.getEvaluators().get(1);
        assertEquals("cls", classEvaluator.className());
    }

    @Test
    public void testParse_WithOrCombinator_ReturnsOrEvaluator() {
        String query = "div, span";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof CombiningEvaluator.Or);
        CombiningEvaluator.Or orEvaluator = (CombiningEvaluator.Or) result;

        assertTrue(orEvaluator.getEvaluators().get(0) instanceof Evaluator.Tag);
        Evaluator.Tag tagEvaluator1 = (Evaluator.Tag) orEvaluator.getEvaluators().get(0);
        assertEquals("div", tagEvaluator1.tagName());

        assertTrue(orEvaluator.getEvaluators().get(1) instanceof Evaluator.Tag);
        Evaluator.Tag tagEvaluator2 = (Evaluator.Tag) orEvaluator.getEvaluators().get(1);
        assertEquals("span", tagEvaluator2.tagName());
    }

    @Test
    public void testParse_WithUnknownCombinator_ThrowsSelectorParseException() {
        String query = "@div span";
        try {
            QueryParser.parse(query);
            fail("SelectorParseException exception not thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown combinator: @", e.getMessage());
        }
    }

    @Test
    public void testParse_WithValueAttribute_ReturnsAttributeWithValueEvaluator() {
        String query = "[class=value]";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.AttributeWithValue);
        Evaluator.AttributeWithValue attributeWithValueEvaluator = (Evaluator.AttributeWithValue) result;
        assertEquals("class", attributeWithValueEvaluator.key());
        assertEquals("value", attributeWithValueEvaluator.value());
    }

    @Test
    public void testParse_WithAttributeStarting_ReturnsAttributeStartingEvaluator() {
        String query = "[class^=val]";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.AttributeStarting);
        Evaluator.AttributeStarting attributeStartingEvaluator = (Evaluator.AttributeStarting) result;
        assertEquals("class", attributeStartingEvaluator.key());
        assertEquals("val", attributeStartingEvaluator.valuePrefix());
    }

    @Test
    public void testParse_WithAttributeWithValueEnding_ReturnsAttributeWithValueEndingEvaluator() {
        String query = "[class$=ue]";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.AttributeWithValueEnding);
        Evaluator.AttributeWithValueEnding attributeWithValueEndingEvaluator = (Evaluator.AttributeWithValueEnding) result;
        assertEquals("class", attributeWithValueEndingEvaluator.key());
        assertEquals("ue", attributeWithValueEndingEvaluator.valueSuffix());
    }

    @Test
    public void testParse_WithAttributeWithValueContaining_ReturnsAttributeWithValueContainingEvaluator() {
        String query = "[src*=image]";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.AttributeWithValueContaining);
        Evaluator.AttributeWithValueContaining attributeWithValueContainingEvaluator = (Evaluator.AttributeWithValueContaining) result;
        assertEquals("src", attributeWithValueContainingEvaluator.key());
        assertEquals("image", attributeWithValueContainingEvaluator.valueSubString());
    }

    @Test
    public void testParse_WithAttributeWithValueMatching_ReturnsAttributeWithValueMatchingEvaluator() {
        String query = "[class~=value]";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.AttributeWithValueMatching);
        Evaluator.AttributeWithValueMatching attributeWithValueMatchingEvaluator = (Evaluator.AttributeWithValueMatching) result;
        assertEquals("class", attributeWithValueMatchingEvaluator.key());
        assertEquals("value", attributeWithValueMatchingEvaluator.pattern().toString());
    }

    @Test
    public void testParse_WithInvalidAttributeQuery_ThrowsSelectorParseException() {
        String query = "[class*value]";
        try {
            QueryParser.parse(query);
            fail("SelectorParseException exception not thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse attribute query '[class*value]': unexpected token at '*value]'", e.getMessage());
        }
    }

    @Test
    public void testParse_WithId_ReturnsIdEvaluator() {
        String query = "#id";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.Id);
        Evaluator.Id idEvaluator = (Evaluator.Id) result;
        assertEquals("id", idEvaluator.id());
    }

    @Test
    public void testParse_WithClass_ReturnsClassEvaluator() {
        String query = ".className";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.Class);
        Evaluator.Class classEvaluator = (Evaluator.Class) result;
        assertEquals("className", classEvaluator.className());
    }

    @Test
    public void testParse_WithTag_ReturnsTagEvaluator() {
        String query = "div";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
        assertTrue(result instanceof Evaluator.Tag);
        Evaluator.Tag tagEvaluator = (Evaluator.Tag) result;
        assertEquals("div", tagEvaluator.tagName());
    }

    @Test
    public void testParse_WithInvalidQuery_ThrowsSelectorParseException() {
        String query = "$div";
        try {
            QueryParser.parse(query);
            fail("SelectorParseException exception not thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query '$div': unexpected token at '$div'", e.getMessage());
        }
    }

    // Add more test cases to cover all methods and edge cases

}