import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Parser;
import org.jsoup.select.QueryParser;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attributes;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class QueryParserTest {

    @Test
    public void testParseSimpleTag() {
        String query = "div";
        Evaluator result = QueryParser.parse(query);
        StructuralEvaluator tagEvaluator = (StructuralEvaluator) result;
        List<TagEvaluator> tagList = tagEvaluator.getTags();
        assertEquals(1, tagList.size());
        assertEquals("div", tagList.get(0).getTagName());
    }

    @Test
    public void testParseTagWithClass() {
        String query = "div.class1";
        Evaluator result = QueryParser.parse(query);
        AndEvaluator andEvaluator = (AndEvaluator) result;
        List<Evaluator> andChildren = andEvaluator.getChildren();
        assertEquals(2, andChildren.size());
        TagEvaluator tagEvaluator = (TagEvaluator) andChildren.get(0);
        assertEquals("div", tagEvaluator.getTagName());
        ClassEvaluator classEvaluator = (ClassEvaluator) andChildren.get(1);
        assertEquals("class1", classEvaluator.getClassValue());
    }

    @Test
    public void testParseTagWithId() {
        String query = "#id1";
        Evaluator result = QueryParser.parse(query);
        IdEvaluator idEvaluator = (IdEvaluator) result;
        assertEquals("id1", idEvaluator.getId());
    }

    @Test
    public void testParseTagWithMultipleClasses() {
        String query = "div.class1.class2";
        Evaluator result = QueryParser.parse(query);
        AndEvaluator andEvaluator = (AndEvaluator) result;
        List<Evaluator> andChildren = andEvaluator.getChildren();
        assertEquals(2, andChildren.size());
        TagEvaluator tagEvaluator = (TagEvaluator) andChildren.get(0);
        assertEquals("div", tagEvaluator.getTagName());
        AndEvaluator classAndEvaluator = (AndEvaluator) andChildren.get(1);
        List<ClassEvaluator> classChildren = classAndEvaluator.getChildren();
        assertEquals(2, classChildren.size());
        ClassEvaluator class1Evaluator = (ClassEvaluator) classChildren.get(0);
        assertEquals("class1", class1Evaluator.getClassValue());
        ClassEvaluator class2Evaluator = (ClassEvaluator) classChildren.get(1);
        assertEquals("class2", class2Evaluator.getClassValue());
    }

    @Test
    public void testParseTagWithAttribute() {
        String query = "div[title]";
        Evaluator result = QueryParser.parse(query);
        AndEvaluator andEvaluator = (AndEvaluator) result;
        List<Evaluator> andChildren = andEvaluator.getChildren();
        assertEquals(2, andChildren.size());
        TagEvaluator tagEvaluator = (TagEvaluator) andChildren.get(0);
        assertEquals("div", tagEvaluator.getTagName());
        AttributeEvaluator attributeEvaluator = (AttributeEvaluator) andChildren.get(1);
        assertEquals("title", attributeEvaluator.getAttributeName());
    }

    @Test
    public void testParseTagWithAttributeValue() {
        String query = "div[title='Test']";
        Evaluator result = QueryParser.parse(query);
        AndEvaluator andEvaluator = (AndEvaluator) result;
        List<Evaluator> andChildren = andEvaluator.getChildren();
        assertEquals(2, andChildren.size());
        TagEvaluator tagEvaluator = (TagEvaluator) andChildren.get(0);
        assertEquals("div", tagEvaluator.getTagName());
        AttributeWithValueEvaluator attributeEvaluator = (AttributeWithValueEvaluator) andChildren.get(1);
        assertEquals("title", attributeEvaluator.getAttributeName());
        assertEquals("Test", attributeEvaluator.getAttributeValue());
    }

    @Test
    public void testParseTagWithMultipleAttributes() {
        String query = "div[title='Test'][class='class1']";
        Evaluator result = QueryParser.parse(query);
        AndEvaluator andEvaluator = (AndEvaluator) result;
        List<Evaluator> andChildren = andEvaluator.getChildren();
        assertEquals(2, andChildren.size());
        TagEvaluator tagEvaluator = (TagEvaluator) andChildren.get(0);
        assertEquals("div", tagEvaluator.getTagName());
        AndEvaluator attributeAndEvaluator = (AndEvaluator) andChildren.get(1);
        List<Evaluator> attributeChildren = attributeAndEvaluator.getChildren();
        assertEquals(2, attributeChildren.size());
        AttributeWithValueEvaluator titleEvaluator = (AttributeWithValueEvaluator) attributeChildren.get(0);
        assertEquals("title", titleEvaluator.getAttributeName());
        assertEquals("Test", titleEvaluator.getAttributeValue());
        AttributeEvaluator classEvaluator = (AttributeEvaluator) attributeChildren.get(1);
        assertEquals("class", classEvaluator.getAttributeName());
        assertEquals("class1", classEvaluator.getAttributeValue());
    }

    @Test
    public void testParseTagWithMultipleSelectors() {
        String query = "div.class1, div.class2";
        Evaluator result = QueryParser.parse(query);
        OrEvaluator orEvaluator = (OrEvaluator) result;
        List<Evaluator> orChildren = orEvaluator.getChildren();
        assertEquals(2, orChildren.size());
        TagEvaluator tagEvaluator1 = (TagEvaluator) orChildren.get(0);
        assertEquals("div", tagEvaluator1.getTagName());
        ClassEvaluator classEvaluator1 = (ClassEvaluator) orChildren.get(1);
        assertEquals("class1", classEvaluator1.getClassValue());
    }

    @Test
    public void testParseTagWithDescendantSelector() {
        String query = "div > p";
        Evaluator result = QueryParser.parse(query);
        CombiningEvaluator combiningEvaluator = (CombiningEvaluator) result;
        List<Evaluator> children = combiningEvaluator.getChildren();
        assertEquals(2, children.size());
        TagEvaluator tagEvaluator1 = (TagEvaluator) children.get(0);
        assertEquals("div", tagEvaluator1.getTagName());
        ImmediateParentEvaluator immediateParentEvaluator = (ImmediateParentEvaluator) children.get(1);
        assertEquals("p", immediateParentEvaluator.getTargetTagName());
    }

    @Test
    public void testParseTagWithSiblingSelector() {
        String query = "div + p";
        Evaluator result = QueryParser.parse(query);
        CombiningEvaluator combiningEvaluator = (CombiningEvaluator) result;
        List<Evaluator> children = combiningEvaluator.getChildren();
        assertEquals(2, children.size());
        TagEvaluator tagEvaluator1 = (TagEvaluator) children.get(0);
        assertEquals("div", tagEvaluator1.getTagName());
        ImmediatePreviousSiblingEvaluator immediatePreviousSiblingEvaluator = (ImmediatePreviousSiblingEvaluator) children.get(1);
        assertEquals("p", immediatePreviousSiblingEvaluator.getTargetTagName());
    }

    @Test
    public void testParseTagWithPseudoClassSelector() {
        String query = "div:first-child";
        Evaluator result = QueryParser.parse(query);
        TagEvaluator tagEvaluator = (TagEvaluator) result;
        ImmediateFirstChildEvaluator immediateFirstChildEvaluator = (ImmediateFirstChildEvaluator) tagEvaluator.getAdditionalEvaluator();
        assertEquals("div", immediateFirstChildEvaluator.getTargetTagName());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div[title='Test']:not(#id1";
        Evaluator result = QueryParser.parse(query);
    }
}