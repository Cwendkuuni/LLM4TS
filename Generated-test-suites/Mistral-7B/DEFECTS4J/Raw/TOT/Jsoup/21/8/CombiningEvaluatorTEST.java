package org.junit.Test;

import org.jsoup.*;
import static org.jsoup.helper.W3C.*;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testCombiningEvaluator() {
        Element root = W3C.parse("<html><body><div id='match1' class='class1 class2'></div><div id='match2' class='class2 class3'></div></body></html>");

        // And Test
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(
                new ElementEvaluator(Elements.withClass("class1")),
                new ElementEvaluator(Elements.withTag("div"))
        ));
        assertFalse(andEvaluator.matches(root, root.getElementsByTag("p").first()));

        // Or Test
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(
                new ElementEvaluator(Elements.withClass("class1")),
                new ElementEvaluator(Elements.withTag("div"))
        ));
        assertTrue(orEvaluator.matches(root, root.getElementById("match1")));
        assertTrue(orEvaluator.matches(root, root.getElementById("match2")));

        // Error Test
        CombiningEvaluator.Or errorOrEvaluator = new CombiningEvaluator.Or(Arrays.asList(
                new ElementEvaluator(Elements.withClass("class3")), // non-existent class
                new ElementEvaluator(Elements.withTag("div"))
        ));
        assertFalse(errorOrEvaluator.matches(root, root.getElementById("match1")));
        assertFalse(errorOrEvaluator.matches(root, root.getElementById("match2")));
    }
}