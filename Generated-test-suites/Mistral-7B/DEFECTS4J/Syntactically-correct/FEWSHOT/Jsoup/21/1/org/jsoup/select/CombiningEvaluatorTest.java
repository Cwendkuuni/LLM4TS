package org.jsoup.select;

import org.jsoup.helper.ElementIdentifier;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        And evaluator = new And(Arrays.asList(
                new ElementIdentifier("#test1"),
                new ElementIdentifier(".test2")
        ));

        Element root = new Element("html");
        Element node1 = new Element("body").id("test1");
        Element node2 = new Element("div").classes("test2");
        root.appendChild(node1);
        root.appendChild(node2);

        Assert.assertTrue(evaluator.matches(root, node1));
        Assert.assertTrue(evaluator.matches(root, node2));
        Assert.assertFalse(evaluator.matches(root, new Element("body")));
        Assert.assertFalse(evaluator.matches(root, new Element("div")));

        Assert.assertEquals("#test1 .test2", evaluator.toString());
    }

    @Test
    public void testAndEvaluatorMultipleEvaluators() {
        And evaluator = new And(
                new And(
                        new ElementIdentifier("#test1"),
                        new ElementIdentifier(".test2")
                ),
                new ElementIdentifier("#test3")
        );

        Element root = new Element("html");
        Element node1 = new Element("body").id("test1");
        Element node2 = new Element("div").classes("test2");
        root.appendChild(node1);
        root.appendChild(node2);

        Element node3 = new Element("body").id("test3");
        root.appendChild(node3);

        Assert.assertTrue(evaluator.matches(root, node1));
        Assert.assertTrue(evaluator.matches(root, node2));
        Assert.assertFalse(evaluator.matches(root, node3));

        Assert.assertEquals("#test1 .test2 #test3", evaluator.toString());
    }

    @Test
    public void testOrEvaluator() {
        Or evaluator = new Or(
                new ElementIdentifier("#test1"),
                new ElementIdentifier(".test2")
        );

        Element root = new Element("html");
        Element node1 = new Element("body").id("test1");
        Element node2 = new Element("div").classes("test2");
        root.appendChild(node1);
        root.appendChild(node2);

        Assert.assertTrue(evaluator.matches(root, node1));
        Assert.assertTrue(evaluator.matches(root, node2));
        Assert.assertTrue(evaluator.matches(root, new Element("body")));
        Assert.assertTrue(evaluator.matches(root, new Element("div")));

        Assert.assertEquals(":or #test1 .test2", evaluator.toString());
    }

    @Test
    public void testOrEvaluatorMultipleEvaluators() {
        Or evaluator = new Or(
                Arrays.asList(
                        new ElementIdentifier("#test1"),
                        new And(
                                new ElementIdentifier(".test2"),
                                new ElementIdentifier("#test3")
                        )
                )
        );

        Element root = new Element("html");
        Element node1 = new Element("body").id("test1");
        Element node2 = new Element("div").classes("test2");
        root.appendChild(node1);
        root.appendChild(node2);

        Element node3 = new Element("body").id("test3");
        root.appendChild(node3);

        Assert.assertTrue(evaluator.matches(root, node1));
        Assert.assertTrue(evaluator.matches(root, node2));
        Assert.assertTrue(evaluator.matches(root, node3));

        Assert.assertEquals(":or #test1 :and .test2 #test3", evaluator.toString());
    }
}