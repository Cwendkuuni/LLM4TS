package org.jsoup.select;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.jsoup.helper.StringUtil.join;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.CombiningEvaluator.And;
import org.jsoup.select.CombiningEvaluator.Or;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAnd() {
        And andEvaluator1 = new And(Arrays.asList(
                new ElementStartsEvaluator("p"),
                new ElementClassEvaluator("red")
        ));

        And andEvaluator2 = new And(
                new ElementStartsEvaluator("p"),
                new ElementClassEvaluator("blue")
        );

        Document doc = new org.jsoup.Jsoup().parse("<p class='red'><p class='red blue'>Test</p></p>");

        Elements elements = doc.select(join(andEvaluator1, " "));
        assertTrue(elements.size() == 1);
        assertEquals(doc.select("p.red").get(0), elements.get(0));

        elements = doc.select(join(andEvaluator2, " "));
        assertTrue(elements.size() == 0);

        elements = doc.select(join(andEvaluator1, " ", andEvaluator2));
        assertTrue(elements.size() == 1);
        assertEquals(doc.select("p.red.blue").get(0), elements.get(0));
    }

    @Test
    public void testOr() {
        Or orEvaluator1 = new Or(
                new ElementStartsEvaluator("p"),
                new ElementClassEvaluator("red")
        );

        Or orEvaluator2 = new Or(
                new ElementStartsEvaluator("p"),
                new ElementClassEvaluator("blue")
        );

        Document doc = new org.jsoup.Jsoup().parse("<p class='red'><p class='red blue'>Test</p></p>");

        Elements elements = doc.select(join(orEvaluator1, " "));
        assertTrue(elements.size() == 2);
        assertTrue(elements.get(0) == doc.select("p.red").get(0) || elements.get(0) == doc.select("p").get(0));
        assertTrue(elements.get(1) == doc.select("p.red").get(0) || elements.get(1) == doc.select("p").get(1));

        elements = doc.select(join(orEvaluator2, " "));
        assertTrue(elements.size() == 2);
        assertTrue(elements.get(0) == doc.select("p.red").get(0) || elements.get(0) == doc.select("p.blue").get(0));
        assertTrue(elements.get(1) == doc.select("p.red").get(0) || elements.get(1) == doc.select("p.blue").get(1));

        Or orEvaluator3 = new Or(
                new And(Arrays.asList(
                        new ElementStartsEvaluator("p"),
                        new ElementClassEvaluator("red")
                )),
                new And(Arrays.asList(
                        new ElementStartsEvaluator("p"),
                        new ElementClassEvaluator("blue")
                ))
        );

        elements = doc.select(join(orEvaluator3, " "));
        assertTrue(elements.size() == 2);
        assertTrue(elements.get(0) == doc.select("p.red").get(0) || elements.get(0) == doc.select("p.blue").get(0));
        assertTrue(elements.get(1) == doc.select("p.red").get(0) || elements.get(1) == doc.select("p.blue").get(1));

        Or orEvaluator4 = new Or();
        orEvaluator4.add(new ElementStartsEvaluator("p"));
        orEvaluator4.add(new ElementClassEvaluator("red"));

        elements = doc.select(join(orEvaluator4, " "));
        assertTrue(elements.size() == 2);
        assertTrue(elements.get(0) == doc.select("p.red").get(0) || elements.get(0) == doc.select("p").get(0));
        assertTrue(elements.get(1) == doc.select("p.red").get(0) || elements.get(1) == doc.select("p").get(1));
    }

    @Test
    public void testNestedOrAnd() {
        And andEvaluator1 = new And(
                new ElementStartsEvaluator("p"),
                new ElementClassEvaluator("red")
        );

        And andEvaluator2 = new And(
                new ElementStartsEvaluator("p"),
                new ElementClassEvaluator("blue")
        );

        Or orEvaluator1 = new Or(Arrays.asList(
                andEvaluator1, andEvaluator2
        ));

        Or orEvaluator2 = new Or(
                new ElementClassEvaluator("class:green"),
                orEvaluator1
        );

        Document doc = new org.jsoup.Jsoup().parse("<p class='red'><p class='red blue'>Test</p></p>");

        Elements elements = doc.select(join(orEvaluator2, " "));
        assertTrue(elements.size() == 2);
        assertTrue(elements.get(0) == doc.select("p.red").get(0) || elements.get(0) == doc.select("p.blue").get(0) || elements.get(0) == doc.select("p.green").get(0));
        assertTrue(elements.get(1) == doc.select("p.red").get(0) || elements.get(1) == doc.select("p.blue").get(1) || elements.get(1) == doc.select("p.green").get(1));
    }
}