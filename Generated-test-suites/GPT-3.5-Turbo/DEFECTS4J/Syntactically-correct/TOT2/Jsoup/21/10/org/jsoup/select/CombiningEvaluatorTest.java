package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("div"),
                new Evaluator.Id("testId")
        );

        Element root = new Element("body");
        Element node = new Element("div").attr("id", "testId");

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndToString() {
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("div"),
                new Evaluator.Id("testId")
        );

        assertEquals("Tag div , Id testId", andEvaluator.toString());
    }

    @Test
    public void testOrMatches() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(new Evaluator.Tag("div"), new Evaluator.Id("testId"))
        );

        Element root = new Element("body");
        Element node = new Element("p").attr("id", "testId");

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrToString() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(new Evaluator.Tag("div"), new Evaluator.Id("testId"))
        );

        assertEquals(":or[Tag div, Id testId]", orEvaluator.toString());
    }

}