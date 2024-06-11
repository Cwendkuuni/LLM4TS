package org.jsoup.select.test;

import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CombiningEvaluatorTest {

    private CombiningEvaluator evaluator;
    private Element root;
    private Element node;

    public CombiningEvaluatorTest(Element root, Element node) {
        this.root = root;
        this.node = node;
    }

    @Parameterized.Parameters(name = "{index}: tests for evaluator={0}")
    public static Collection<Object[]> data() {
        Element root = Element.parse("<html><body><div id='a'><p id='b'></p></div></body></html>");
        Element nodeA = root.selectFirst("#a");
        Element nodeB = root.selectFirst("#b");

        return Arrays.asList(new Object[][] {
                {new CombiningEvaluator.And(Arrays.asList(new ElementSelector("#a"))), root, nodeA},
                {new CombiningEvaluator.And(Arrays.asList(new ElementSelector("#b"))), root, nodeB},
                {new CombiningEvaluator.And(Arrays.asList(new ElementSelector("#a"), new ElementSelector("#b"))), root, nodeB},
                {new CombiningEvaluator.Or(Arrays.asList(new ElementSelector("#a"), new ElementSelector("#b"))), root, nodeB},
                {new CombiningEvaluator.Or(Arrays.asList(new ElementSelector("#a"), new ElementSelector("#b")), new ElementSelector("#a")), root, nodeA},
                {new CombiningEvaluator.Or(new ArrayList<>(), new ElementSelector("#a")), root, nodeA},
                {new CombiningEvaluator.Or(new ArrayList<>(), new ElementSelector("#b")), root, nodeB}
        });
    }

    @Test
    public void testMatches() {
        evaluator.matches(root, node);
        if (evaluator instanceof CombiningEvaluator.And) {
            CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
            for (Evaluator e : andEvaluator.evaluators) {
                assertTrue("Evaluator " + e + " should match", e.matches(root, node));
            }
        } else if (evaluator instanceof CombiningEvaluator.Or) {
            CombiningEvaluator.Or orEvaluator = (CombiningEvaluator.Or) evaluator;
            for (Evaluator e : orEvaluator.evaluators) {
                assertTrue("Evaluator " + e + " should match at least once", evaluator.matches(root, node) || e.matches(root, node));
            }
        } else {
            assertFalse("Unexpected evaluator type", false);
        }
    }

    @Test
    public void testToString() {
        String expected = evaluator.toString();
        String actual = evaluator.getClass().getSimpleName() + "(" + evaluator.evaluators + ")";
        assertTrue("ToString should match", expected.equals(actual));
    }
}