package org.jsoup.select;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(
                Elements.identity(),
                new ElementSelector(".class1")
        );

        Element root = Element.parse("<html><body><div class='class1'></div></body></html>");

        Elements elements = root.select("div");

        for (Element element : elements) {
            assertTrue(andEvaluator.matches(root, element));
        }
    }

    @Test
    public void testAndEvaluator_False() {
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(
                Elements.identity(),
                new ElementSelector(".class1"),
                new ElementSelector(".class2")
        );

        Element root = Element.parse("<html><body><div class='class1'></div><div class='class2'></div></body></html>");

        Elements elements = root.select("div");

        for (Element element : elements) {
            if (element.classNames().contains("class2")) {
                assertFalse(andEvaluator.matches(root, element));
            }
        }
    }

    @Test
    public void testOrEvaluator() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(
                new ElementSelector(".class1"),
                new ElementSelector(".class2")
        );

        Element root = Element.parse("<html><body><div class='class1'></div><div class='class2'></div></body></html>");

        Elements elements = root.select("div");

        for (Element element : elements) {
            assertTrue(orEvaluator.matches(root, element) || element.classNames().contains("class1") || element.classNames().contains("class2"));
        }
    }

    @Test
    public void testOrEvaluator_True() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(
                new ElementSelector(".class1"),
                new ElementSelector(".class2"),
                new ElementSelector(".class3")
        );

        Element root = Element.parse("<html><body><div class='class1 class2'></div></body></html>");

        assertTrue(orEvaluator.matches(root, root));
    }
}