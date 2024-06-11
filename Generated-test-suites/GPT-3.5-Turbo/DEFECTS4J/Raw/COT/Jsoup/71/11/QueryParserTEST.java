import static org.junit.Assert.*;

import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evaluator).tagName());

        query = "#id";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("id", ((Evaluator.Id) evaluator).id());

        query = ".class";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("class", ((Evaluator.Class) evaluator).className());

        query = "div#id.class";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
        assertTrue(andEvaluator.evaluators().get(0) instanceof Evaluator.Tag);
        Evaluator.Tag tagEvaluator = (Evaluator.Tag) andEvaluator.evaluators().get(0);
        assertEquals("div", tagEvaluator.tagName());
        assertTrue(andEvaluator.evaluators().get(1) instanceof Evaluator.Id);
        Evaluator.Id idEvaluator = (Evaluator.Id) andEvaluator.evaluators().get(1);
        assertEquals("id", idEvaluator.id());
        assertTrue(andEvaluator.evaluators().get(2) instanceof Evaluator.Class);
        Evaluator.Class classEvaluator = (Evaluator.Class) andEvaluator.evaluators().get(2);
        assertEquals("class", classEvaluator.className());
    }
}