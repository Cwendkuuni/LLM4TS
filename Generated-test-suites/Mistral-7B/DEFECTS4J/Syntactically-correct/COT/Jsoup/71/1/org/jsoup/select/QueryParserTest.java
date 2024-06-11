import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class1 > p.class2";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertNotNull(evaluator);

        if (evaluator instanceof CombiningEvaluator.And) {
            Assert.assertEquals(2, ((CombiningEvaluator.And) evaluator).getChildren().size());
        } else {
            Assert.fail("Expected AND evaluator, but got " + evaluator.getClass().getName());
        }

        StructuralEvaluator.ImmediateParent parentEval = (StructuralEvaluator.ImmediateParent) ((CombiningEvaluator.And) evaluator).getChildren().get(0);
        Assert.assertEquals(Evaluator.Tag.class, parentEval.getClass());
        Assert.assertEquals("div", ((Evaluator.Tag) parentEval).getName());

        StructuralEvaluator.Class classEval = (StructuralEvaluator.Class) ((CombiningEvaluator.And) evaluator).getChildren().get(1);
        Assert.assertEquals("class1", classEval.getName());

        query = "p > .class2";
        parser = new QueryParser(query);
        evaluator = parser.parse();

        Assert.assertNotNull(evaluator);

        if (evaluator instanceof CombiningEvaluator.And) {
            Assert.assertEquals(2, ((CombiningEvaluator.And) evaluator).getChildren().size());
        } else {
            Assert.fail("Expected AND evaluator, but got " + evaluator.getClass().getName());
        }

        parentEval = (StructuralEvaluator.ImmediateParent) ((CombiningEvaluator.And) evaluator).getChildren().get(0);
        Assert.assertEquals(Evaluator.Tag.class, parentEval.getClass());
        Assert.assertEquals("p", ((Evaluator.Tag) parentEval).getName());

        StructuralEvaluator.Class classEval2 = (StructuralEvaluator.Class) ((CombiningEvaluator.And) evaluator).getChildren().get(1);
        Assert.assertEquals("class2", classEval2.getName());
    }

    @Test
    public void testParseWithId() {
        String query = "#id";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertNotNull(evaluator);
        Assert.assertEquals(Evaluator.Id.class, evaluator.getClass());
        Assert.assertEquals("id", ((Evaluator.Id) evaluator).getName());
    }

    @Test
    public void testParseWithClass() {
        String query = ".class";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertNotNull(evaluator);
        Assert.assertEquals(Evaluator.Class.class, evaluator.getClass());
        Assert.assertEquals("class", ((Evaluator.Class) evaluator).getName());
    }

    @Test
    public void testParseWithTag() {
        String query = "tag";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertNotNull(evaluator);
        Assert.assertEquals(Evaluator.Tag.class, evaluator.getClass());
        Assert.assertEquals("tag", ((Evaluator.Tag) evaluator).getName());
    }

    @Test
    public void testParseWithAttribute() {
        String query = "[attr]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertNotNull(evaluator);
        Assert.assertEquals(Evaluator.Attribute.class, evaluator.getClass());
        Assert.assertNull(((Evaluator.Attribute) evaluator).getValue());
        Assert.assertEquals("attr", ((Evaluator.Attribute) evaluator).getName());
    }

    @Test
    public void testParseWithAttributeValue() {
        String query = "[attr=value]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertNotNull(evaluator);
        Assert.assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
        Assert.assertEquals("attr", ((Evaluator.AttributeWithValue) evaluator).getName());
        Assert.assertEquals("value", ((Evaluator.AttributeWithValue) evaluator).getValue());
    }

    @Test
    public void testParseWithMultipleCombinators() {
        String query = "div.class1 > p.class2 > span.class3";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertNotNull(evaluator);

        if (evaluator instanceof CombiningEvaluator.And) {
            Assert.assertEquals(3, ((CombiningEvaluator.And) evaluator).getChildren().size());
        } else {
            Assert.fail("Expected AND evaluator, but got " + evaluator.getClass().getName());
        }

        StructuralEvaluator.ImmediateParent parent1 = (StructuralEvaluator.ImmediateParent) ((CombiningEvaluator.And) evaluator).getChildren().get(0);
        Assert.assertEquals(Evaluator.Tag.class, parent1.getClass());
        Assert.assertEquals("div", ((Evaluator.Tag) parent1).getName());

        StructuralEvaluator.Class class1 = (StructuralEvaluator.Class) ((CombiningEvaluator.And) evaluator).getChildren().get(1);
        Assert.assertEquals("class1", class1.getName());

        StructuralEvaluator.ImmediateParent parent2 = (StructuralEvaluator.ImmediateParent) ((CombiningEvaluator.And) evaluator).getChildren().get(2);
        Assert.assertEquals(Evaluator.Tag.class, parent2.getClass());
        Assert.assertEquals("p", ((Evaluator.Tag) parent2).getName());

        StructuralEvaluator.Class class2 = (StructuralEvaluator.Class) ((CombiningEvaluator.And) evaluator).getChildren().get(3);
        Assert.assertEquals("class2", class2.getName());

        StructuralEvaluator.ImmediateParent parent3 = (StructuralEvaluator.ImmediateParent) ((CombiningEvaluator.And) evaluator).getChildren().get(4);
        Assert.assertEquals(Evaluator.Tag.class, parent3.getClass());
        Assert.assertEquals("span", ((Evaluator.Tag) parent3).getName());

        StructuralEvaluator.Class class3 = (StructuralEvaluator.Class) ((CombiningEvaluator.And) evaluator).getChildren().get(5);
        Assert.assertEquals("class3", class3.getName());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseWithInvalidSelector() {
        String query = "div.class[invalid]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        // This test should fail with a Selector.SelectorParseException
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseWithInvalidCombinator() {
        String query = "div.class1 ^ p.class2";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        // This test should fail with a Selector.SelectorParseException
    }

    @Test
    public void testParseWithPseudoClasses() {
        String query = "p:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertNotNull(evaluator);
        Assert.assertEquals(Evaluator.IsFirstChild.class, evaluator.getClass());
    }

    @Test
    public void testParseWithPseudoElements() {
        String query = "p:first-of-type";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertNotNull(evaluator);
        Assert.assertEquals(Evaluator.IsFirstOfType.class, evaluator.getClass());
    }

    @Test
    public void testParseWithPseudoClassesAndElements() {
        String query = "p:first-child.class";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        Assert.assertNotNull(evaluator);
        if (evaluator instanceof CombiningEvaluator.And) {
            Assert.assertEquals(2, ((CombiningEvaluator.And) evaluator).getChildren().size());
        } else {
            Assert.fail("Expected AND evaluator, but got " + evaluator.getClass().getName());
        }

        StructuralEvaluator.IsFirstChild firstChild = (StructuralEvaluator.IsFirstChild) ((CombiningEvaluator.And) evaluator).getChildren().get(0);
        Assert.assertEquals(Evaluator.IsFirstChild.class, firstChild.getClass());

        StructuralEvaluator.Class classEval = (StructuralEvaluator.Class) ((CombiningEvaluator.And) evaluator).getChildren().get(1);
        Assert.assertEquals("class", classEval.getName());
    }
}