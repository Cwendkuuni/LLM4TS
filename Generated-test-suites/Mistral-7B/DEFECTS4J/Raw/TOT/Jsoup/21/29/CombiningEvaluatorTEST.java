
package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        // Test case for And evaluator
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(
                new ElementSelector("class", "testClass1"),
                new ElementSelector("tag", "p")
        );
        Elements elements = new Elements(new Document(""));
        elements.add(new Element("html").add("body").add(new Element("p").addClass("testClass1")));
        elements.add(new Element("html").add("body").add(new Element("p").addClass("testClass2")));

        List<Element> matches = andEvaluator.select(elements, new Element("body"));

        assertEquals(1, matches.size());
        assertEquals("testClass1", matches.get(0).className());
    }

    @Test
    public void testOrEvaluator() {
        // Test case for Or evaluator
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or();
        orEvaluator.add(new ElementSelector("tag", "p"));
        orEvaluator.add(new ElementSelector("tag", "h1"));

        Elements elements = new Elements(new Document(""));
        elements.add(new Element("html").add("body").add(new Element("p")));
        elements.add(new Element("html").add("body").add(new Element("h1")));
        elements.add(new Element("html").add("body").add(new Element("p").setText("Test")));

        List<Element> matches = orEvaluator.select(elements, new Element("body"));

        assertEquals(3, matches.size());
        assertTrue(matches.contains(elements.get(0)));
        assertTrue(matches.contains(elements.get(1)));
        assertTrue(matches.contains(elements.get(2)));
    }

    // Add more test cases for other methods and scenarios if necessary

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        // Test case for IllegalArgumentException
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(null);
    }
}
