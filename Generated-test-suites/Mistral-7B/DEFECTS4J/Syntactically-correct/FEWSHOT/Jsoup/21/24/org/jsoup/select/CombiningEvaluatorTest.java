package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator_EmptyList() {
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(new ArrayList<>());
        Element root = new Element("html");
        Element node = new Element("body");
        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndEvaluator_SingleEvaluator() {
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(new ElementSelector("p"));
        Element root = new Element("html");
        Element pElement = new Element("p");
        Element node = pElement;
        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndEvaluator_MultipleEvaluators() {
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(
                Arrays.asList(new ElementSelector("p"), new AttributeSelector("class", "red")));
        Element root = new Element("html");
        Element pElement = new Element("p").addClass("red");
        Element node = pElement;
        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndEvaluator_Mismatch() {
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(
                Arrays.asList(new ElementSelector("p"), new AttributeSelector("class", "blue")));
        Element root = new Element("html");
        Element pElement = new Element("p").addClass("red");
        Element node = pElement;
        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrEvaluator_SingleEvaluator() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(new ElementSelector("p"));
        Element root = new Element("html");
        Element pElement1 = new Element("p");
        Element node1 = pElement1;
        Element pElement2 = new Element("p");
        assertTrue(orEvaluator.matches(root, node1));
        assertTrue(orEvaluator.matches(root, node2));
    }

    @Test
    public void testOrEvaluator_MultipleEvaluators() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(new ElementSelector("p"), new AttributeSelector("class", "red")));
        Element root = new Element("html");
        Element pElement1 = new Element("p");
        Element node1 = pElement1;
        Element pElement2 = new Element("p").addClass("red");
        Element node2 = pElement2;
        assertTrue(orEvaluator.matches(root, node1));
        assertTrue(orEvaluator.matches(root, node2));
    }

    @Test
    public void testOrEvaluator_MismatchWithAllEvaluators() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(new ElementSelector("p"), new AttributeSelector("class", "red")));
        Element root = new Element("html");
        Element pElement = new Element("p").addClass("blue");
        Element node = pElement;
        assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrEvaluator_AddNewEvaluator() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(new ElementSelector("p"), new AttributeSelector("class", "red")));
        orEvaluator.add(new AttributeSelector("id", "myId"));
        Element root = new Element("html");
        Element pElement = new Element("p").addClass("red").setAttributeValue("id", "myId");
        Element node = pElement;
        assertTrue(orEvaluator.matches(root, node));
    }
}