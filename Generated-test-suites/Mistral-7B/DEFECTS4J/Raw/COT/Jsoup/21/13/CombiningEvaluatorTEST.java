package org.jsoup.select.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import org.jsoup.helper.ElementFinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Test class for the CombiningEvaluator class.
 */
public class CombiningEvaluatorTest {

    @Test
    public void testAndConstructor() {
        // Test And constructor with Collection of Evaluators
        Collection<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementFinder("#element1"));
        evaluators.add(new ElementFinder("#element2"));
        And and = new And(evaluators);

        // Test And constructor with Evaluator array
        Evaluator[] evaluatorsArray = new Evaluator[]{new ElementFinder("#element1"), new ElementFinder("#element2")};
        And andArray = new And(evaluatorsArray);

        // Check that both instances have the same evaluators
        Assert.assertArrayEquals(and.evaluators.toArray(), andArray.evaluators.toArray());
    }

    @Test
    public void testAndMatches() {
        Element root = new Element("html");
        Element element1 = new Element("body").attr("id", "element1");
        Element element2 = new Element("div").attr("id", "element2");
        root.appendChild(element1);
        root.appendChild(element2);

        And and = new And(Arrays.asList(new ElementFinder("#element1"), new ElementFinder("#element2")));

        Assert.assertTrue(and.matches(root, root));
    }

    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("html");

        Assert.assertFalse(new And(Arrays.asList(new ElementFinder("#nonExistentElement"), new ElementFinder("#element2"))).matches(root, root));
    }

    @Test
    public void testOrConstructor() {
        // Test Or constructor with Collection of Evaluators
        Collection<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementFinder("#element1"));
        evaluators.add(new ElementFinder("#element2"));
        Or or = new Or(evaluators);

        // Test Or constructor with single Evaluator
        Or orSingle = new Or(new ElementFinder("#element1"));

        // Test Or constructor with multiple Evaluators
        Or orMultiple = new Or(Arrays.asList(new ElementFinder("#element1"), new ElementFinder("#element2")));

        // Check that all instances have the same evaluators
        Assert.assertEquals(or.evaluators, orSingle.evaluators);
        Assert.assertEquals(or.evaluators, orMultiple.evaluators);
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("html");
        Element element1 = new Element("body").attr("id", "element1");
        Element element2 = new Element("div").attr("id", "element2");
        root.appendChild(element1);
        root.appendChild(element2);

        Or or = new Or(Arrays.asList(new ElementFinder("#element1"), new ElementFinder("#element2")));

        Assert.assertTrue(or.matches(root, root));
        Assert.assertTrue(or.matches(root, element1));
        Assert.assertTrue(or.matches(root, element2));
    }

    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("html");
        Element element3 = new Element("body").attr("id", "element3");
        root.appendChild(element3);

        Or or = new Or(Arrays.asList(new ElementFinder("#element1"), new ElementFinder("#element2")));

        Assert.assertFalse(or.matches(root, element3));
    }
}