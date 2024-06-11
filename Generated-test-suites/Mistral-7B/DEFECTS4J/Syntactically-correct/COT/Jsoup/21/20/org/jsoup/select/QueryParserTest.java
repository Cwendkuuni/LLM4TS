import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator.Id;
import org.jsoup.select.Evaluator.Class;
import org.jsoup.select.Evaluator.Tag;
import org.jsoup.select.Evaluator.Attribute;
import org.jsoup.select.Evaluator.AttributeWithValue;
import org.jsoup.select.Evaluator.AttributeWithValueNot;
import org.jsoup.select.Evaluator.AttributeWithValueStarting;
import org.jsoup.select.Evaluator.AttributeWithValueEnding;
import org.jsoup.select.Evaluator.AttributeWithValueContaining;
import org.jsoup.select.Evaluator.AttributeWithValueMatching;
import org.jsoup.select.Evaluator.AllElements;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexEquals;
import org.jsoup.select.StructuralEvaluator.Root;
import org.jsoup.select.StructuralEvaluator.Has;
import org.jsoup.select.StructuralEvaluator.And;
import org.jsoup.select.StructuralEvaluator.Or;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.Parent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.PreviousSibling;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class1 > div.class2";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        // Basic functionality test
        assertTrue(evaluator instanceof And);
        And andEvaluator = (And) evaluator;
        assertEquals(2, andEvaluator.children.size());

        assertTrue(andEvaluator.children.get(0) instanceof ImmediateParent);
        ImmediateParent parentEvaluator = (ImmediateParent) andEvaluator.children.get(0);
        assertEquals("div", parentEvaluator.getLocalName());

        assertTrue(andEvaluator.children.get(1) instanceof And);
        And innerAndEvaluator = (And) andEvaluator.children.get(1);
        assertEquals(2, innerAndEvaluator.children.size());

        assertTrue(innerAndEvaluator.children.get(0) instanceof Tag);
        Tag tagEvaluator = (Tag) innerAndEvaluator.children.get(0);
        assertEquals("div", tagEvaluator.getLocalName());

        assertTrue(innerAndEvaluator.children.get(1) instanceof Class);
        Class classEvaluator = (Class) innerAndEvaluator.children.get(1);
        assertEquals("class2", classEvaluator.getValue());
    }

    // Edge cases and exception handling tests go here

}