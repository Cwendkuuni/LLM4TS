
package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CombiningEvaluatorTest {

    private Document doc;

    @Before
    public void setUp() {
        // Create a sample HTML document for testing
        doc = new Document("");
        Element root = new Element(Tag.valueOf("html"), "");
        doc.appendChild(root);
        Element body = new Element(Tag.valueOf("body"), "");
        root.appendChild(body);
        Element div1 = new Element(Tag.valueOf("div"), "");
        div1.attr("class", "class1");
        body.appendChild(div1);
        Element div2 = new Element(Tag.valueOf("div"), "");
        div2.attr("class", "class2 class3");
        body.appendChild(div2);
        Element div3 = new Element(Tag.valueOf("div"), "");
        div3.attr("class", "class1 class2 class3");
        body.appendChild(div3);
        Element p = new Element(Tag.valueOf("p"), "");
        p.attr("class", "para");
        body.appendChild(p);
        Element span = new Element(Tag.valueOf("span"), "");
        span.attr("class", "span1 span2");
        body.appendChild(span);
        Element a = new Element(Tag.valueOf("a"), "");
        a.attr("href", "http://www.example.com");
        body.appendChild(a);
    }

    @After
    public void tearDown() {
        // Cleanup after each test
        doc = null;
    }

    @Test
    public void testAndMatches() {
        Element root = doc.select("html").first();
        Element node = doc.select("div.class1.class2.class3").first();
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(
                new Evaluator.Class("class1"),
                new Evaluator.Class("class2"),
                new Evaluator.Class("class3")
        );

        boolean result = evaluator.matches(root, node);

        assertEquals(true, result);
    }

    @Test
    public void testAndNotMatches() {
        Element root = doc.select("html").first();
        Element node = doc.select("div.class1.class2.class3").first();
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(
                new Evaluator.Class("class1"),
                new Evaluator.Class("missingclass"),
                new Evaluator.Class("class3")
        );

        boolean result = evaluator.matches(root, node);

        assertEquals(false, result);
    }

    @Test
    public void testOrMatches() {
        Element root = doc.select("html").first();
        Element node = doc.select("div.class1.class2.class3").first();
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(
                new Evaluator.Class("class1"),
                new Evaluator.Class("class2"),
                new Evaluator.Class("missingclass")
        );

        boolean result = evaluator.matches(root, node);

        assertEquals(true, result);
    }

    @Test
    public void testOrNotMatches() {
        Element root = doc.select("html").first();
        Element node = doc.select("div.class1.class2.class3").first();
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(
                new Evaluator.Class("missing1"),
                new Evaluator.Class("missing2"),
                new Evaluator.Class("missingclass")
        );

        boolean result = evaluator.matches(root, node);

        assertEquals(false, result);
    }
}
