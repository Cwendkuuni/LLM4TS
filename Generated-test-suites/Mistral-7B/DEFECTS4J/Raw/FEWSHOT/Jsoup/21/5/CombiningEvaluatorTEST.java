package org.jsoup.select;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        Evaluator e1 = ElementStartsWith.class.newInstance();
        Evaluator e2 = ElementHasClass.class.newInstance();

        And andEvaluator = new And(Arrays.asList(e1, e2));

        Element root = new Element("html");
        Elements elements = new Elements(root);
        elements.add(new Element("body").addClass("class1").text("Test"));
        elements.add(new Element("body").text("Test2"));

        for (Element node : elements) {
            boolean matches = andEvaluator.matches(root, node);
            if (node.text().equals("Test")) {
                Assert.assertTrue(matches);
            } else {
                Assert.assertFalse(matches);
            }
        }
    }

    @Test
    public void testOrEvaluator() {
        Evaluator e1 = ElementStartsWith.class.newInstance();
        Evaluator e2 = ElementHasClass.class.newInstance();
        Evaluator e3 = ElementHasAttr.class.newInstance();

        Or orEvaluator = new Or();
        orEvaluator.add(new And(Arrays.asList(e1, e2)));
        orEvaluator.add(new And(Arrays.asList(e3)));

        Element root = new Element("html");
        Elements elements = new Elements(root);
        elements.add(new Element("body").addClass("class1").text("Test").attr("data-test", "true"));
        elements.add(new Element("body").text("Test2").attr("data-test", "false"));

        for (Element node : elements) {
            boolean matches = orEvaluator.matches(root, node);
            if (node.text().equals("Test") || node.attr("data-test").equals("true")) {
                Assert.assertTrue(matches);
            } else {
                Assert.assertFalse(matches);
            }
        }
    }

    @Test
    public void testAndWithEmptyList() {
        And andEvaluator = new And(Collections.emptyList());
        Element root = new Element("html");
        Elements elements = new Elements(root);
        for (Element node : elements) {
            boolean matches = andEvaluator.matches(root, node);
            Assert.assertTrue(matches);
        }
    }

    @Test
    public void testOrWithEmptyList() {
        Or orEvaluator = new Or(Collections.emptyList());
        Element root = new Element("html");
        Elements elements = new Elements(root);
        for (Element node : elements) {
            boolean matches = orEvaluator.matches(root, node);
            Assert.assertTrue(matches);
        }
    }
}