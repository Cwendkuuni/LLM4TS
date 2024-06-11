import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test class for CombiningEvaluator.
 */
public class CombiningEvaluatorTest {

    /**
     * Test method for And evaluator.
     */
    @Test
    public void testAnd() {
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(
                new ElementMatcher() {
                    @Override
                    public boolean matches(Element element) {
                        return "div".equals(element.tagName());
                    }
                },
                new ElementMatcher() {
                    @Override
                    public boolean matches(Element element) {
                        return "class".equals(element.tagName()) && "foo".equals(element.attr("class"));
                    }
                });

        Document doc = Jsoup.parse("<html><body><div class='foo'></div></body></html>");
        Elements elements = doc.selectElements(andEvaluator.toString());
        assertTrue(elements.size() > 0);
    }

    /**
     * Test method for Or evaluator.
     */
    @Test
    public void testOr() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or();
        orEvaluator.add(new ElementMatcher() {
            @Override
            public boolean matches(Element element) {
                return "div".equals(element.tagName()) && "class".equals(element.tagName()) && "foo".equals(element.attr("class"));
            }
        });
        orEvaluator.add(new ElementMatcher() {
            @Override
            public boolean matches(Element element) {
                return "div".equals(element.tagName()) && "class".equals(element.tagName()) && "bar".equals(element.attr("class"));
            }
        });

        Document doc = Jsoup.parse("<html><body><div class='foo'></div><div class='bar'></div></body></html>");
        Elements elements = doc.selectElements(orEvaluator.toString());
        assertTrue(elements.size() >= 1);
    }

    /**
     * Test method for And with error scenario.
     */
    @Test
    public void testAndError() {
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(
                new ElementMatcher() {
                    @Override
                    public boolean matches(Element element) {
                        return "div".equals(element.tagName());
                    }
                },
                new ElementMatcher() {
                    @Override
                    public boolean matches(Element element) {
                        throw new RuntimeException("Test error");
                    }
                });

        Document doc = Jsoup.parse("<html><body><div></div></body></html>");
        Elements elements = doc.selectElements(andEvaluator.toString());
        // Test should not reach this point
    }

    /**
     * Test method for Or with error scenario.
     */
    @Test(expected = RuntimeException.class)
    public void testOrError() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or();
        orEvaluator.add(new ElementMatcher() {
            @Override
            public boolean matches(Element element) {
                throw new RuntimeException("Test error");
            }
        });
        orEvaluator.add(new ElementMatcher() {
            @Override
            public boolean matches(Element element) {
                return "div".equals(element.tagName()) && "class".equals(element.tagName()) && "foo".equals(element.attr("class"));
            }
        });

        Document doc = Jsoup.parse("<html><body><div class='foo'></div></body></html>");
        Elements elements = doc.selectElements(orEvaluator.toString());
        // Test should throw RuntimeException
    }

    /**
     * Test method for And with empty list of evaluators.
     */
    @Test
    public void testAndEmpty() {
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And();
        Document doc = Jsoup.parse("<html><body></body></html>");
        Elements elements = doc.selectElements(andEvaluator.toString());
        assertTrue(elements.isEmpty());
    }

    /**
     * Test method for Or with empty list of evaluators.
     */
    @Test
    public void testOrEmpty() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or();
        Document doc = Jsoup.parse("<html><body></body></html>");
        Elements elements = doc.selectElements(orEvaluator.toString());
        assertTrue(elements.isEmpty());
    }
}