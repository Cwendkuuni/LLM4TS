import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.ParseException;

import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseBasic() {
        String query = "div.class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<String> expectedClasses = new ArrayList<>();
        expectedClasses.add("class1");

        StructuralEvaluator.Class classEvaluator = (StructuralEvaluator.Class) evaluator;
        List<StructuralEvaluator.Class> classes = classEvaluator.getClasses();

        assertEquals(expectedClasses, classes);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div#class1=1";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @Test
    public void testParseWithCombinator() {
        String query = "div > p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        Evaluator childEvaluator = andEvaluator.getLeftmostEvaluator();
        assertEquals(StructuralEvaluator.Tag.class, childEvaluator.getClass());
        assertEquals("div", ((StructuralEvaluator.Tag) childEvaluator).getTagName());

        Evaluator nextEvaluator = andEvaluator.getRightmostEvaluator();
        assertEquals(StructuralEvaluator.Tag.class, nextEvaluator.getClass());
        assertEquals("p", ((StructuralEvaluator.Tag) nextEvaluator).getTagName());
    }

    @Test
    public void testParseWithAttribute() {
        String query = "input[type='text']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Evaluator attributeEvaluator = evaluator;
        while (attributeEvaluator.getNextSibling() != null) {
            attributeEvaluator = attributeEvaluator.getNextSibling();
        }

        Evaluator attributeWithValueEvaluator = (Evaluator.AttributeWithValue) attributeEvaluator;
        assertEquals("type", attributeWithValueEvaluator.getAttributeName());
        assertEquals("text", attributeWithValueEvaluator.getValue());
    }

    @Test
    public void testParseWithCombinatorsAndPseudoSelectors() {
        String query = "div.class1 > p:first-child:has(input[type='text'])";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        Evaluator childEvaluator = andEvaluator.getLeftmostEvaluator();
        assertEquals(StructuralEvaluator.Tag.class, childEvaluator.getClass());
        assertEquals("div", ((StructuralEvaluator.Tag) childEvaluator).getTagName());

        Evaluator nextEvaluator = andEvaluator.getRightmostEvaluator();
        assertEquals(StructuralEvaluator.Class.class, nextEvaluator.getClass());
        StructuralEvaluator.Class classEvaluator = (StructuralEvaluator.Class) nextEvaluator;
        List<String> classes = classEvaluator.getClasses();
        assertEquals(1, classes.size());
        assertEquals("class1", classes.get(0));

        nextEvaluator = nextEvaluator.getNextSibling();
        assertEquals(StructuralEvaluator.Tag.class, nextEvaluator.getClass());
        assertEquals("p", ((StructuralEvaluator.Tag) nextEvaluator).getTagName());

        nextEvaluator = nextEvaluator.getNextSibling();
        StructuralEvaluator.Has hasEvaluator = (StructuralEvaluator.Has) nextEvaluator;
        Evaluator childOfHasEvaluator = hasEvaluator.getChild();
        assertEquals(StructuralEvaluator.Tag.class, childOfHasEvaluator.getClass());
        assertEquals("input", ((StructuralEvaluator.Tag) childOfHasEvaluator).getTagName());

        nextEvaluator = nextEvaluator.getNextSibling();
        Evaluator attributeEvaluator = (Evaluator.Attribute) nextEvaluator;
        assertEquals("type", attributeEvaluator.getAttributeName());

        nextEvaluator = nextEvaluator.getNextSibling();
        assertEquals(StructuralEvaluator.Value.class, nextEvaluator.getClass());
        StructuralEvaluator.Value valueEvaluator = (StructuralEvaluator.Value) nextEvaluator;
        assertEquals("text", valueEvaluator.getValue());
    }
}