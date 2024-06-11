package org.jsoup.select;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        Evaluator e1 = new ElementSelector("#id1");
        Evaluator e2 = new ElementSelector("#id2");
        And andEvaluator = new And(Arrays.asList(e1, e2));

        Element root = new Element("html");
        Element node1 = new Element("body").add("id", "id1");
        Element node2 = new Element("body").add("id", "id2");
        root.appendChild(node1);
        root.appendChild(node2);

        assertTrue(andEvaluator.matches(root, node1));
        assertTrue(andEvaluator.matches(root, node2));
        assertFalse(andEvaluator.matches(root, new Element("body").add("id", "id3")));
    }

    @Test
    public void testOrEvaluator() {
        Evaluator e1 = new ElementSelector("#id1");
        Evaluator e2 = new ElementSelector("#id2");
        Or orEvaluator = new Or(Arrays.asList(e1, e2));

        Element root = new Element("html");
        Element node1 = new Element("body").add("id", "id1");
        Element node2 = new Element("body").add("id", "id2");
        root.appendChild(node1);
        root.appendChild(node2);

        assertTrue(orEvaluator.matches(root, node1));
        assertTrue(orEvaluator.matches(root, node2));
        assertFalse(orEvaluator.matches(root, new Element("body").add("id", "id3")));

        Or orWithThreeEvaluators = new Or(Arrays.asList(e1, e2, new ElementSelector("#id3")));
        assertTrue(orWithThreeEvaluators.matches(root, node1));
        assertTrue(orWithThreeEvaluators.matches(root, node2));
        assertTrue(orWithThreeEvaluators.matches(root, new Element("body").add("id", "id3")));
    }

    @Test
    public void testAndWithThreeEvaluators() {
        Evaluator e1 = new ElementSelector("#id1");
        Evaluator e2 = new ElementSelector("#id2");
        Evaluator e3 = new ElementSelector("#id3");
        And andEvaluator = new And(Arrays.asList(e1, e2, e3));

        Element root = new Element("html");
        Element node1 = new Element("body").add("id", "id1");
        Element node2 = new Element("body").add("id", "id2");
        Element node3 = new Element("body").add("id", "id3");
        root.appendChild(node1);
        root.appendChild(node2);
        root.appendChild(node3);

        assertFalse(andEvaluator.matches(root, node1)); // one false
        assertFalse(andEvaluator.matches(root, node2)); // one false
        assertFalse(andEvaluator.matches(root, node3)); // all false
    }
}