import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator.Id;
import org.jsoup.select.Evaluator.Class;
import org.jsoup.select.Evaluator.Tag;
import org.jsoup.select.Evaluator.Attribute;
import org.jsoup.select.Evaluator.AttributeWithValue;
import org.jsoup.select.Evaluator.AttributeStarting;
import org.jsoup.select.Evaluator.AttributeWithValueStarting;
import org.jsoup.select.Evaluator.AttributeWithValueContaining;
import org.jsoup.select.Evaluator.AttributeWithValueMatching;
import org.jsoup.select.Evaluator.AllElements;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexEquals;
import org.jsoup.select.Evaluator.IsNthOfType;
import org.jsoup.select.Evaluator.IsNthLastOfType;
import org.jsoup.select.Evaluator.IsNthChild;
import org.jsoup.select.Evaluator.IsNthLastChild;
import org.jsoup.select.Evaluator.IsFirstChild;
import org.jsoup.select.Evaluator.IsLastChild;
import org.jsoup.select.Evaluator.IsFirstOfType;
import org.jsoup.select.Evaluator.IsLastOfType;
import org.jsoup.select.Evaluator.IsEmpty;
import org.jsoup.select.Evaluator.IsRoot;
import org.jsoup.select.Evaluator.IsNthOfTypeOrSelf;
import org.jsoup.select.Evaluator.IsNthLastOfTypeOrSelf;
import org.jsoup.select.Evaluator.IsOnlyChild;
import org.jsoup.select.Evaluator.IsOnlyOfType;
import org.jsoup.select.StructuralEvaluator.Has;
import org.jsoup.select.StructuralEvaluator.Parent;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.PreviousSibling;
import org.jsoup.select.StructuralEvaluator.Root;
import org.jsoup.select.CombiningEvaluator.Or;
import org.jsoup.select.CombiningEvaluator.And;

public class QueryParserTest {

    @Test
    public void testParseSimpleQuery() {
        String query = "div.class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        // Check that the evaluator correctly matches an element with the given class
        TagEvaluator tagEvaluator = (TagEvaluator) evaluator;
        assertEquals("div", tagEvaluator.tagName);
        List<ClassEvaluator> classEvaluators = tagEvaluator.getClassEvaluators();
        assertEquals(1, classEvaluators.size());
        ClassEvaluator classEvaluator = classEvaluators.get(0);
        assertEquals("class1", classEvaluator.className);
    }

    @Test
    public void testParseComplexQueryWithAttributeSelector() {
        String query = "li[data-test='test']:containsOwn('Test')";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        // Check that the evaluator correctly matches an element with the given data-test attribute value and containing the given text
        StructuralEvaluator.ContainsOwnText containsOwnTextEvaluator = (StructuralEvaluator.ContainsOwnText) evaluator;
        assertEquals("Test", containsOwnTextEvaluator.getText());
        AttributeWithValue attributeWithValueEvaluator = (AttributeWithValue) containsOwnTextEvaluator.getChildren().get(0);
        assertEquals("data-test", attributeWithValueEvaluator.attributeName);
        assertEquals("test", attributeWithValueEvaluator.value);
        TagEvaluator tagEvaluator = (TagEvaluator) evaluator.getParent();
        assertEquals("li", tagEvaluator.tagName);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "li[data-test='test':contains('Test')";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @Test
    public void testParseQueryWithCombinators() {
        String query = "div > p, div > ul > li:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        // Check that the evaluator is a combining evaluator with two children
        CombiningEvaluator combiningEvaluator = (CombiningEvaluator) evaluator;
        assertEquals(2, combiningEvaluator.getChildren().size());

        // Check that the first child is a combinator with a single child that matches div > p
        CombiningEvaluator firstChild = (CombiningEvaluator) combiningEvaluator.getChildren().get(0);
        assertEquals(1, firstChild.getChildren().size());
        StructuralEvaluator.Tag tagEvaluator = (StructuralEvaluator.Tag) firstChild.getChildren().get(0);
        assertEquals("div", tagEvaluator.tagName);
        assertEquals(">", tagEvaluator.combinator);
        TagEvaluator childTagEvaluator = (TagEvaluator) tagEvaluator.getChildren().get(0);
        assertEquals("p", childTagEvaluator.tagName);

        // Check that the second child is a combinator with a single child that matches div > ul > li:first-child
        CombiningEvaluator secondChild = (CombiningEvaluator) combiningEvaluator.getChildren().get(1);
        assertEquals(1, secondChild.getChildren().size());
        StructuralEvaluator.Has hasEvaluator = (StructuralEvaluator.Has) secondChild.getChildren().get(0);
        assertEquals(1, hasEvaluator.getChildren().size());
        StructuralEvaluator.IsFirstChild firstChildEvaluator = (StructuralEvaluator.IsFirstChild) hasEvaluator.getChildren().get(0);
        TagEvaluator liTagEvaluator = (TagEvaluator) firstChildEvaluator.getChildren().get(0);
        assertEquals("li", liTagEvaluator.tagName);
        assertEquals(":", liTagEvaluator.combinator);
        StructuralEvaluator.Tag ulTagEvaluator = (StructuralEvaluator.Tag) firstChildEvaluator.getParent();
        TagEvaluator divTagEvaluator = (TagEvaluator) ulTagEvaluator.getParent();
        assertEquals("div", divTagEvaluator.tagName);
    }
}