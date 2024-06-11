package org.jsoup.select;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                Arrays.asList(
                        new ElementEvaluator("class", "foo"),
                        new ElementEvaluator("tag", "p")
                )
        );

        Element root = Element.parse("<html><body><p class='foo'></p></body></html>");
        Element node = root.selectFirst("p");

        Assert.assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndEvaluator_whenFirstConditionIsFalse_returnsFalse() {
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                Arrays.asList(
                        new ElementEvaluator("class", "bar"),
                        new ElementEvaluator("tag", "p")
                )
        );

        Element root = Element.parse("<html><body><p class='foo'></p></body></html>");
        Element node = root.selectFirst("p");

        Assert.assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndEvaluator_whenSecondConditionIsFalse_returnsFalse() {
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                Arrays.asList(
                        new ElementEvaluator("class", "foo"),
                        new ElementEvaluator("tag", "div")
                )
        );

        Element root = Element.parse("<html><body><p class='foo'></p></body></html>");
        Element node = root.selectFirst("p");

        Assert.assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrEvaluator() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(
                        new ElementEvaluator("class", "foo"),
                        new ElementEvaluator("tag", "p")
                )
        );

        orEvaluator.add(new ElementEvaluator("class", "bar"));

        Element root = Element.parse("<html><body><p class='foo'></p><p class='bar'></p></body></html>");
        Element node = root.selectFirst("p");

        Assert.assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrEvaluator_whenFirstConditionIsTrue_returnsTrue() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(
                        new ElementEvaluator("class", "foo"),
                        new ElementEvaluator("tag", "p")
                )
        );

        orEvaluator.add(new ElementEvaluator("tag", "div"));

        Element root = Element.parse("<html><body><p class='foo'></p></body></html>");
        Element node = root.selectFirst("p");

        Assert.assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrEvaluator_whenBothConditionsAreFalse_returnsFalse() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(
                        new ElementEvaluator("class", "foo"),
                        new ElementEvaluator("tag", "p")
                )
        );

        orEvaluator.add(new ElementEvaluator("tag", "div"));

        Element root = Element.parse("<html><body><p class='bar'></p></body></html>");
        Element node = root.selectFirst("p");

        Assert.assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrEvaluator_whenSecondConditionIsAdded_returnsTrue() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(
                        new ElementEvaluator("class", "foo"),
                        new ElementEvaluator("tag", "p")
                )
        );

        Element newNode = Element.parse("<html><body><p class='bar' tag='foo'></p></body></html>");
        orEvaluator.add(new ElementEvaluator("tag", "p"));

        Assert.assertTrue(orEvaluator.matches(null, newNode));
    }
}