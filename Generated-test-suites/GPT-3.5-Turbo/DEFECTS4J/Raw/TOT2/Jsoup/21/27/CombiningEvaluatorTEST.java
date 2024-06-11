import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return node.tagName().equals("div");
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return node.classNames().contains("class1");
                    }
                }
        ));

        assertTrue(evaluator.matches(root, node));
    }

    @Test
    public void testAndNotMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return node.tagName().equals("div");
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return node.classNames().contains("class2");
                    }
                }
        ));

        assertFalse(evaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return node.tagName().equals("div");
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return node.classNames().contains("class2");
                    }
                }
        ));

        assertTrue(evaluator.matches(root, node));
    }

    @Test
    public void testOrNotMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return node.tagName().equals("span");
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return node.classNames().contains("class3");
                    }
                }
        ));

        assertFalse(evaluator.matches(root, node));
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}