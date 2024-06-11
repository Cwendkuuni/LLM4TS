package org.jsoup.select;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.junit.Test;

public class CombiningEvaluatorTest {
    @Test
    public void testAndEvaluator() {
        CombiningEvaluator.And andEvaluator1 = new CombiningEvaluator.And(Arrays.asList(
                new ElementStartsEvaluator("div"),
                new ElementClassEvaluator("class", "selected")
        ));

        Element root = Elements.parseHtml("<html><body><div class='selected'></div></body></html>").first();
        assertTrue(andEvaluator1.matches(root, root));

        Element invalidRoot = Elements.parseHtml("<html><body><p class='selected'></p></body></html>").first();
        assertFalse(andEvaluator1.matches(invalidRoot, invalidRoot));

        // Test with multiple evaluators
        CombiningEvaluator.And andEvaluator2 = new CombiningEvaluator.And(Arrays.asList(
                new ElementStartsWithTextEvaluator("h1", "Hello"),
                new ElementHasAttrEvaluator("id", "elementID")
        ));

        Element validElement = Elements.parseHtml("<html><body><h1 id='elementID'>Hello World</h1></body></html>").first();
        assertTrue(andEvaluator2.matches(root, validElement));

        Element invalidElement = Elements.parseHtml("<html><body><h1 id='anotherID'>Hello World</h1></body></html>").first();
        assertFalse(andEvaluator2.matches(root, invalidElement));
    }

    @Test
    public void testOrEvaluator() {
        CombiningEvaluator.Or orEvaluator1 = new CombiningEvaluator.Or(Arrays.asList(
                new ElementStartsEvaluator("div"),
                new ElementClassEvaluator("class", "selected")
        ));

        Element root = Elements.parseHtml("<html><body><div class='selected'></div></body></html>").first();
        assertTrue(orEvaluator1.matches(root, root));

        Element invalidRoot = Elements.parseHtml("<html><body><p class='selected'></p></body></html>").first();
        assertTrue(orEvaluator1.matches(root, invalidRoot));

        // Test with multiple evaluators
        CombiningEvaluator.Or orEvaluator2 = new CombiningEvaluator.Or(Arrays.asList(
                new ElementStartsWithTextEvaluator("h1", "Hello"),
                new ElementClassEvaluator("class", "highlighted")
        ));

        Element validElement1 = Elements.parseHtml("<html><body><h1 class='highlighted'>Hello World</h1></body></html>").first();
        assertTrue(orEvaluator2.matches(root, validElement1));

        Element validElement2 = Elements.parseHtml("<html><body><h1 class='highlighted selected'>Hello World</h1></body></html>").first();
        assertTrue(orEvaluator2.matches(root, validElement2));

        Element invalidElement = Elements.parseHtml("<html><body><h1 id='elementID'>Hello World</h1></body></html>").first();
        assertFalse(orEvaluator2.matches(root, invalidElement));

        CombiningEvaluator.Or orEvaluator3 = new CombiningEvaluator.Or();
        orEvaluator3.add(new ElementStartsEvaluator("div"));
        orEvaluator3.add(new ElementClassEvaluator("class", "selected"));

        Element validElementWithMultipleConditions = Elements.parseHtml("<html><body><div class='selected'></div></body></html>").first();
        assertTrue(orEvaluator3.matches(root, validElementWithMultipleConditions));

        Element invalidElementWithMultipleConditions = Elements.parseHtml("<html><body><p class='selected'></p></body></html>").first();
        assertTrue(orEvaluator3.matches(root, invalidElementWithMultipleConditions));
    }
}