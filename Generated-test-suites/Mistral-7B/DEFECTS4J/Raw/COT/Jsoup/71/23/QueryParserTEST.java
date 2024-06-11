import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        QueryParser parser = new QueryParser("#elementId");
        Evaluator evaluator = parser.parse();
        assertInstanceOf(Evaluator.Id.class, evaluator);
        assertEquals("elementId", evaluator.asId().getId());

        parser = new QueryParser(".className");
        evaluator = parser.parse();
        assertInstanceOf(Evaluator.Class.class, evaluator);
        assertEquals("className", evaluator.asClass().getClassName());

        parser = new QueryParser("tagName");
        evaluator = parser.parse();
        assertInstanceOf(Evaluator.Tag.class, evaluator);
        assertEquals("tagName", evaluator.asTag().getTagName());

        parser = new QueryParser("[attribute='value']");
        evaluator = parser.parse();
        assertInstanceOf(Evaluator.Attribute.class, evaluator);
        assertEquals("attribute", evaluator.asAttribute().getKey());
        assertEquals("value", evaluator.asAttribute().getValue());

       parser = new QueryParser("tagName[attribute]");
        evaluator = parser.parse();
        assertInstanceOf(Evaluator.Attribute.class, evaluator);
        assertEquals("tagName", evaluator.asTag().getTagName());
        assertEquals("attribute", evaluator.asAttribute().getKey());

        parser = new QueryParser("tagName[attribute='value']");
        evaluator = parser.parse();
        assertInstanceOf(Evaluator.AttributeWithValue.class, evaluator);
        assertEquals("tagName", evaluator.asTag().getTagName());
        assertEquals("attribute", evaluator.asAttributeWithValue().getKey());
        assertEquals("value", evaluator.asAttributeWithValue().getValue());

        parser = new QueryParser("tagName[attribute~='value']");
        evaluator = parser.parse();
        assertInstanceOf(Evaluator.AttributeWithValueMatching.class, evaluator);
        assertEquals("tagName", evaluator.asTag().getTagName());
        assertEquals(Pattern.compile("value"), evaluator.asAttributeWithValueMatching().getPattern());

        parser = new QueryParser("tagName:first-child");
        evaluator = parser.parse();
        assertInstanceOf(Evaluator.IsFirstChild.class, evaluator);

        parser = new QueryParser("tagName:last-child");
        evaluator = parser.parse();
        assertInstanceOf(Evaluator.IsLastChild.class, evaluator);

        parser = new QueryParser(".class1, .class2");
        evaluator = parser.parse();
        assertInstanceOf(CombiningEvaluator.Or.class, evaluator);
        assertTrue(((CombiningEvaluator.Or) evaluator).size() > 0);
        assertEquals(Evaluator.class, evaluator.getClass());

        parser = new QueryParser("tagName > tagName2");
        evaluator = parser.parse();
        assertInstanceOf(CombiningEvaluator.And.class, evaluator);
        CombiningEvaluator and = (CombiningEvaluator) evaluator;
        assertEquals(StructuralEvaluator.ImmediateParent.class, and.getCondition(0).getClass());
        assertEquals(Evaluator.class, and.getCondition(1).getClass());

        parser = new QueryParser("tagName ~ tagName2");
        evaluator = parser.parse();
        assertInstanceOf(CombiningEvaluator.And.class, evaluator);
        and = (CombiningEvaluator) evaluator;
        assertEquals(StructuralEvaluator.PreviousSibling.class, and.getCondition(0).getClass());
        assertEquals(Evaluator.class, and.getCondition(1).getClass());

        parser = new QueryParser("tagName + tagName2");
        evaluator = parser.parse();
        assertInstanceOf(CombiningEvaluator.And.class, evaluator);
        and = (CombiningEvaluator) evaluator;
        assertEquals(StructuralEvaluator.ImmediatePreviousSibling.class, and.getCondition(0).getClass());
        assertEquals(Evaluator.class, and.getCondition(1).getClass());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseWithInvalidQuery() {
        new QueryParser("invalidQuery");
    }
}