package org.jsoup.select;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.jsoup.helper.StringUtil.join;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        Evaluator a1 = new And(Arrays.asList(new ElementStarts("p"), new ElementHasClass("highlight")));
        Evaluator a2 = new And(Arrays.asList(new ElementHasAttr("id", "myId"), new ElementHasText("hello world")));

        List<Element> elements = Arrays.asList(
                new Element("p", "id", "myId", null, null, null, Collections.emptyList(), null),
                new Element("p", "class", "highlight", null, null, null, Collections.emptyList(), null),
                new Element("p", "id", null, null, null, null, Collections.emptyList(), null),
                new Element("p", "class", "highlight", null, null, null, Collections.emptyList(), null),
                new Element("div", null, null, null, null, null, Collections.emptyList(), null),
                new Element("p", null, null, null, null, null, Collections.emptyList(), null),
                new Element("span", null, null, null, null, null, Collections.emptyList(), null)
        );

        for (Element element : elements) {
            Assert.assertTrue("Element should match And evaluator a1", a1.matches(null, element));
            Assert.assertFalse("Element should not match And evaluator a2", a2.matches(null, element));
        }

        // Test multiple And evaluators
        Evaluator a3 = new And(Arrays.asList(a1, new ElementHasText("foo")));
        for (Element element : elements) {
            Assert.assertFalse("Element should not match And evaluator a3", a3.matches(null, element));
        }

        Element p1 = new Element("p", "id", "myId", null, null, null, Collections.emptyList(), null);
        p1.text("hello world");
        elements.add(p1);

        for (Element element : elements) {
            Assert.assertFalse("Element should not match And evaluator a3 without 'foo' text", a3.matches(null, element));
            Assert.assertTrue("Element should match And evaluator a3 with 'foo' text", a3.matches(null, new Element("p", null, null, null, null, null, Collections.emptyList(), null).setText("foo")));
        }
    }

    @Test
    public void testOrEvaluator() {
        Evaluator or1 = new Or(Arrays.asList(new ElementHasClass("highlight"), new ElementHasAttr("id", "myId")));
        Evaluator or2 = new Or(Arrays.asList(new ElementStarts("p"), new ElementClassContains("title")));

        List<Element> elements = Arrays.asList(
                new Element("p", "id", "myId", null, null, null, Collections.emptyList(), null),
                new Element("p", "class", "highlight", null, null, null, Collections.emptyList(), null),
                new Element("p", null, null, null, null, null, Collections.emptyList(), null),
                new Element("div", null, null, null, null, null, Collections.emptyList(), null),
                new Element("p", null, null, null, null, null, Collections.emptyList(), null),
                new Element("span", null, null, null, null, null, Collections.emptyList(), null)
        );

        for (Element element : elements) {
            Assert.assertFalse("Element should not match Or evaluator or1", or1.matches(null, element));
        }

        Element p1 = new Element("p", "id", "myId", null, null, null, Collections.emptyList(), null);
        p1.addClass("highlight");
        elements.add(p1);

        for (Element element : elements) {
            Assert.assertTrue("Element should match Or evaluator or1 with 'highlight' class", or1.matches(null, element));
            Assert.assertFalse("Element should not match Or evaluator or1 without 'highlight' class", or1.matches(null, new Element("p", null, null, null, null, null, Collections.emptyList(), null)));
        }

        // Test multiple Or evaluators
        Evaluator or3 = new Or(Arrays.asList(or1, new ElementHasText("foo")));
        for (Element element : elements) {
            Assert.assertFalse("Element should not match Or evaluator or3", or3.matches(null, element));
            Assert.assertTrue("Element should match Or evaluator or3 with 'foo' text", or3.matches(null, new Element("p", null, null, null, null, null, Collections.emptyList(), null).setText("foo")));
            Assert.assertTrue("Element should match Or evaluator or3 with 'highlight' class", or3.matches(null, p1));
        }

        // Test adding evaluators to Or evaluator
        Or or4 = new Or();
        or4.add(or1);
        or4.add(new ElementHasText("foo"));

        for (Element element : elements) {
            Assert.assertFalse("Element should not match Or evaluator or4", or4.matches(null, element));
            Assert.assertTrue("Element should match Or evaluator or4 with 'foo' text", or4.matches(null, new Element("p", null, null, null, null, null, Collections.emptyList(), null).setText("foo")));
            Assert.assertTrue("Element should match Or evaluator or4 with 'highlight' class", or4.matches(null, p1));
        }
    }

    @Test
    public void testToString() {
        Evaluator a = new And(Arrays.asList(new ElementClassContains("title"), new ElementHasText("hello world")));
        Assert.assertEquals("title[*] contains(text(),hello world)", a.toString());

        Evaluator o = new Or(Arrays.asList(new ElementClassContains("title"), new ElementHasText("foo")));
        Assert.assertEquals(":or title[*] contains(text(),foo)", o.toString());
    }

    @Test
    public void testAndAndOrEvaluators() {
        Evaluator and1 = new And(Arrays.asList(new ElementHasClass("highlight"), new ElementHasAttr("id", "myId")));
        Evaluator and2 = new And(Arrays.asList(new ElementHasClass("highlight"), new ElementHasAttr("id", "anotherId")));
        Evaluator or = new Or(Arrays.asList(and1, and2));

        List<Element> elements = Arrays.asList(
                new Element("p", "id", "myId", null, null, null, Collections.emptyList(), null),
                new Element("p", "class", "highlight", null, null, null, Collections.emptyList(), null),
                new Element("p", null, null, null, null, null, Collections.emptyList(), null),
                new Element("div", null, null, null, null, null, Collections.emptyList(), null),
                new Element("p", null, null, null, null, null, Collections.emptyList(), null),
                new Element("span", null, null, null, null, null, Collections.emptyList(), null)
        );

        for (Element element : elements) {
            Assert.assertFalse("Element should not match AndAndOr evaluators", or.matches(null, element));
        }

        Element p1 = new Element("p", "id", "myId", null, null, null, Collections.emptyList(), null);
        p1.addClass("highlight");
        elements.add(p1);

        Element p2 = new Element("p", "id", "anotherId", null, null, null, Collections.emptyList(), null);
        p2.addClass("highlight");
        elements.add(p2);

        for (Element element : elements) {
            Assert.assertFalse("Element should not match AndAndOr evaluators without both 'highlight' class and 'myId' or 'anotherId'", or.matches(null, element));
        }

        for (Element element : elements) {
            if (element.getClass().getSimpleName().equals("Element")) {
                element.setId("myId");
                element.addClass("highlight");
                Assert.assertTrue("Element should match AndAndOr evaluators with 'highlight' class and 'myId'", or.matches(null, element));
            }
        }

        for (Element element : elements) {
            if (element.getClass().getSimpleName().equals("Element")) {
                element.setId("anotherId");
                element.addClass("highlight");
                Assert.assertTrue("Element should match AndAndOr evaluators with 'highlight' class and 'anotherId'", or.matches(null, element));
            }
        }

        for (Element element : elements) {
            if (element.getClass().getSimpleName().equals("Element")) {
                element.setId("myId");
                Assert.assertTrue("Element should match AndAndOr evaluators with 'highlight' class and 'myId'", or.matches(null, element));
            }
        }

        for (Element element : elements) {
            if (element.getClass().getSimpleName().equals("Element")) {
                element.setId("anotherId");
                Assert.assertTrue("Element should match AndAndOr evaluators with 'highlight' class and 'anotherId'", or.matches(null, element));
            }
        }

        for (Element element : elements) {
            if (element.getClass().getSimpleName().equals("Element")) {
                element.addClass("highlight");
                Assert.assertTrue("Element should match AndAndOr evaluators with 'highlight' class", or.matches(null, element));
            }
        }

        for (Element element : elements) {
            if (element.getClass().getSimpleName().equals("Element")) {
                element.setId("myId");
                Assert.assertTrue("Element should match AndAndOr evaluators with 'highlight' class and 'myId'", or.matches(null, element));
            }
        }

        for (Element element : elements) {
            if (element.getClass().getSimpleName().equals("Element")) {
                element.setId("anotherId");
                Assert.assertTrue("Element should match AndAndOr evaluators with 'highlight' class and 'anotherId'", or.matches(null, element));
            }
        }

        for (Element element : elements) {
            if (element.getClass().getSimpleName().equals("Element")) {
                element.setId("myId");
                element.setText("foo");
                Assert.assertFalse("Element should not match AndAndOr evaluators with 'highlight' class and 'foo' text instead of 'myId'", or.matches(null, element));
            }
        }

        for (Element element : elements) {
            if (element.getClass().getSimpleName().equals("Element")) {
                element.setId("anotherId");
                element.setText("foo");
                Assert.assertFalse("Element should not match AndAndOr evaluators with 'highlight' class and 'foo' text instead of 'anotherId'", or.matches(null, element));
            }
        }

        for (Element element : elements) {
            if (element.getClass().getSimpleName().equals("Element")) {
                element.setText("foo");
                Assert.assertTrue("Element should match AndAndOr evaluators with 'foo' text", or.matches(null, element));
            }
        }

        for (Element element : elements) {
            if (element.getClass().getSimpleName().equals("Element")) {
                element.addClass("highlight");
                Assert.assertTrue("Element should match AndAndOr evaluators with 'highlight' class", or.matches(null, element));
            }
        }
    }
}