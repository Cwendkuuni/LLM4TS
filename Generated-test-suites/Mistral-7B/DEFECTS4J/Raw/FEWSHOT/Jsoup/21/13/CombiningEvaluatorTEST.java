package org.jsoup.select;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.helper.StringUtil.join;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAnd() {
        Evaluator e1 = new ElementSelector("#id1");
        Evaluator e2 = new ClassSelector("class1");
        And and = new And(Arrays.asList(e1, e2));

        Element element = new Element("div", "id", "id1");
        element.addClass("class1");

        Assert.assertTrue(and.matches(null, element));
    }

    @Test
    public void testAndMultipleElements() {
        Evaluator e1 = new ElementSelector("#id1");
        Evaluator e2 = new ClassSelector("class1");
        And and = new And(Arrays.asList(e1, e2));

        List<Element> elements = new ArrayList<>();
        elements.add(new Element("div", "id", "id1"));
        elements.add(new Element("div", "class", "class1"));

        for (Element element : elements) {
            Assert.assertTrue(and.matches(null, element));
        }
    }

    @Test
    public void testAndNoMatch() {
        Evaluator e1 = new ElementSelector("#id1");
        Evaluator e2 = new ClassSelector("class2");
        And and = new And(Arrays.asList(e1, e2));

        Element element = new Element("div", "id", "id2");

        Assert.assertFalse(and.matches(null, element));
    }

    @Test
    public void testOr() {
        Evaluator e1 = new ElementSelector("#id1");
        Evaluator e2 = new ClassSelector("class1");
        Or or = new Or(Arrays.asList(e1, e2));

        Element element1 = new Element("div", "id", "id1");
        Element element2 = new Element("div", "class", "class1");

        Assert.assertTrue(or.matches(null, element1));
        Assert.assertTrue(or.matches(null, element2));
    }

    @Test
    public void testOrMultipleElements() {
        Evaluator e1 = new ElementSelector("#id1");
        Evaluator e2 = new ClassSelector("class1");
        Evaluator e3 = new AttributeSelector("data-test", "test");
        Or or = new Or(Arrays.asList(e1, e2, e3));

        List<Element> elements = new ArrayList<>();
        elements.add(new Element("div", "id", "id1"));
        elements.add(new Element("div", "class", "class1"));
        elements.add(new Element("div", "data-test", "test"));

        for (Element element : elements) {
            Assert.assertTrue(or.matches(null, element));
        }
    }

    @Test
    public void testOrNoMatch() {
        Evaluator e1 = new ElementSelector("#id1");
        Evaluator e2 = new ClassSelector("class1");
        Or or = new Or(Arrays.asList(e1, e2));

        Element element = new Element("div", "id", "id2");

        Assert.assertFalse(or.matches(null, element));
    }

    @Test
    public void testOrNestedAnd() {
        Evaluator e1 = new ElementSelector("#id1");
        Evaluator e2 = new ClassSelector("class1");
        Evaluator e3 = new ClassSelector("class2");
        And and = new And(Arrays.asList(e1, e2));
        Or or = new Or(Arrays.asList(and, e3));

        Element element = new Element("div", "id", "id1");
        element.addClass("class1");
        element.addClass("class3");

        Assert.assertTrue(or.matches(null, element));
    }

    @Test
    public void testOrNestedOr() {
        Evaluator e1 = new ElementSelector("#id1");
        Evaluator e2 = new ClassSelector("class1");
        Or or1 = new Or(Arrays.asList(e1, e2));
        Evaluator e3 = new ClassSelector("class2");
        Or or2 = new Or(Arrays.asList(or1, e3));

        Element element = new Element("div", "id", "id1");
        element.addClass("class1");
        element.removeClass("class2");

        Assert.assertFalse(or2.matches(null, element));
    }
}