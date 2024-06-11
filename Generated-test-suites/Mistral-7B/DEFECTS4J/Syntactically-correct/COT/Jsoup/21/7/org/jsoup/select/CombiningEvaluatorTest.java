import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFilter;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        Evaluator evaluator1 = new ElementFilter.Name("div");
        Evaluator evaluator2 = new ElementFilter.Class("classA");
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));
        Element root = Jsoup.parse("<div class='classA'><div class='classB'></div></div>");
        Element node = root.selectFirst(andEvaluator.toString());
        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndEvaluator_noMatch() {
        Evaluator evaluator1 = new ElementFilter.Name("div");
        Evaluator evaluator2 = new ElementFilter.Class("classB");
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));
        Element root = Jsoup.parse("<div class='classA'><div class='classC'></div></div>");
        Element node = root.selectFirst(andEvaluator.toString());
        assertNull(node);
        assertFalse(andEvaluator.matches(root, null));
    }

    @Test
    public void testOrEvaluator() {
        Evaluator evaluator1 = new ElementFilter.Name("div");
        Evaluator evaluator2 = new ElementFilter.Class("classA");
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        Element root = Jsoup.parse("<div class='classA'><div class='classB'></div></div>");
        Element node1 = root.selectFirst(orEvaluator.toString());
        assertTrue(node1 != null);
        Element node2 = root.selectFirst(evaluator2.toString());
        assertTrue(node2 != null);
        assertTrue(orEvaluator.matches(root, node1) || orEvaluator.matches(root, node2));
    }

    @Test
    public void testOrEvaluator_multipleMatches() {
        Evaluator evaluator1 = new ElementFilter.Name("div");
        Evaluator evaluator2 = new ElementFilter.Class("classA");
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        Element root = Jsoup.parse("<div class='classA'><div class='classB'></div><div class='classA'></div></div>");
        List<Element> nodes = root.selectList(orEvaluator.toString());
        assertEquals(2, nodes.size());
        for (Element node : nodes) {
            assertTrue(orEvaluator.matches(root, node));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrEvaluator_emptyList() {
        new CombiningEvaluator.Or(new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrEvaluator_singleEvaluator() {
        Evaluator evaluator = new ElementFilter.Name("div");
        new CombiningEvaluator.Or(Arrays.asList(evaluator));
    }
}