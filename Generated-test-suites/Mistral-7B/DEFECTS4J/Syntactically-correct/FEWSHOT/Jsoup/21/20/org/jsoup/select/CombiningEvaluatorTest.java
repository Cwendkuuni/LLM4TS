package org.jsoup.select;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.jsoup.nodes.Element;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        CombiningEvaluator.And andEvaluator1 = new CombiningEvaluator.And(Arrays.asList(
                new ElementSelector("p"),
                new AttributeSelector("class", "red")
        ));

        Element element1 = new Element("p");
        element1.className = "red";
        Element element2 = new Element("p");
        element2.className = "blue";

        assertTrue(andEvaluator1.matches(null, element1));
        assertFalse(andEvaluator1.matches(null, element2));

        CombiningEvaluator.And andEvaluator2 = new CombiningEvaluator.And(Arrays.asList(
                new ElementSelector("p"),
                new AttributeSelector("class", "blue")
        ));

        assertTrue(andEvaluator2.matches(null, element2));
        assertFalse(andEvaluator2.matches(null, element1));

        CombiningEvaluator.And andEvaluator3 = new CombiningEvaluator.And(
                new ElementSelector("div"),
                new CombiningEvaluator.And(
                        new AttributeSelector("class", "a"),
                        new AttributeSelector("class", "b")
                )
        );

        Element elementWithDivAB = new Element("div");
        elementWithDivAB.className = "a b";
        assertTrue(andEvaluator3.matches(null, elementWithDivAB));
        assertFalse(andEvaluator3.matches(null, new Element("div")));
    }

    @Test
    public void testOrEvaluator() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(
                        new ElementSelector("p"),
                        new AttributeSelector("class", "red")
                )
        );

        orEvaluator.add(new AttributeSelector("class", "blue"));

        Element element1 = new Element("p");
        element1.className = "red";
        Element element2 = new Element("p");
        element2.className = "blue";

        assertTrue(orEvaluator.matches(null, element1));
        assertTrue(orEvaluator.matches(null, element2));

        CombiningEvaluator.Or orEvaluator2 = new CombiningEvaluator.Or(
                Arrays.asList(
                        new AttributeSelector("class", "red"),
                        new AttributeSelector("class", "blue")
                )
        );

        Element element3 = new Element("p");
        element3.className = "green";
        assertFalse(orEvaluator2.matches(null, element3));
        assertTrue(orEvaluator2.matches(null, element1));
        assertTrue(orEvaluator2.matches(null, element2));

        Element elementWithDivA = new Element("div");
        elementWithDivA.className = "a";
        Element elementWithDivB = new Element("div");
        elementWithDivB.className = "b";

        CombiningEvaluator.Or orEvaluator3 = new CombiningEvaluator.Or(
                Arrays.asList(
                        new ElementSelector("div"),
                        new CombiningEvaluator.And(
                                new AttributeSelector("class", "a"),
                                new AttributeSelector("class", "b")
                        )
                )
        );

        assertTrue(orEvaluator3.matches(null, elementWithDivA));
        assertTrue(orEvaluator3.matches(null, elementWithDivB));
    }
}