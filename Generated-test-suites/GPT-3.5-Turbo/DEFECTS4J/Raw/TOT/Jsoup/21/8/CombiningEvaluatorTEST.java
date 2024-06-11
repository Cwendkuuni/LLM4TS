package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {

    private static final String HTML = "<html><body><div id=\"container\"></div><span class=\"highlight\"></span></body></html>";
    private Document document;

    @Before
    public void setUp() {
        document = Jsoup.parse(HTML);
    }

    @Test
    public void testAndMatches() {
        Element root = document.select("html").first();
        Element node = document.select("div.highlight").first();

        Evaluator eval1 = new AttributeEvaluator.AttributeWithValue("id", "container");
        Evaluator eval2 = new AttributeEvaluator.AttributeWithValue("class", "highlight");

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(eval1, eval2);

        Assert.assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndNotMatches() {
        Element root = document.select("html").first();
        Element node = document.select("div.highlight").first();

        Evaluator eval1 = new AttributeEvaluator.AttributeWithValue("id", "container");
        Evaluator eval2 = new AttributeEvaluator.AttributeWithValue("class", "highlighted");

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(eval1, eval2);

        Assert.assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = document.select("html").first();
        Element node = document.select("div.highlight").first();

        Evaluator eval1 = new AttributeEvaluator.AttributeWithValue("id", "container");
        Evaluator eval2 = new AttributeEvaluator.AttributeWithValue("class", "highlight");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(eval1, eval2);

        Assert.assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrNotMatches() {
        Element root = document.select("html").first();
        Element node = document.select("div.highlight").first();

        Evaluator eval1 = new AttributeEvaluator.AttributeWithValue("id", "container");
        Evaluator eval2 = new AttributeEvaluator.AttributeWithValue("class", "highlighted");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(eval1, eval2);

        Assert.assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrSingleEvaluatorMatches() {
        Element root = document.select("html").first();
        Element node = document.select("div.highlight").first();

        Evaluator eval1 = new AttributeEvaluator.AttributeWithValue("id", "container");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(eval1);

        Assert.assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrNoEvaluatorsMatch() {
        Element root = document.select("html").first();
        Element node = document.select("div.highlight").first();

        Evaluator eval1 = new AttributeEvaluator.AttributeWithValue("id", "notContainer");
        Evaluator eval2 = new AttributeEvaluator.AttributeWithValue("class", "highlighted");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(eval1, eval2);

        Assert.assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrAddEvaluator() {
        Element root = document.select("html").first();
        Element node = document.select("div.highlight").first();

        Evaluator eval1 = new AttributeEvaluator.AttributeWithValue("id", "container");
        Evaluator eval2 = new AttributeEvaluator.AttributeWithValue("class", "highlight");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(eval1);
        orEvaluator.add(eval2);

        Assert.assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrToString() {
        Evaluator eval1 = new AttributeEvaluator.AttributeWithValue("id", "container");
        Evaluator eval2 = new AttributeEvaluator.AttributeWithValue("class", "highlight");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(eval1, eval2);

        Assert.assertEquals(":or[" + StringUtil.join(new ArrayList<>(List.of(eval1, eval2)), " ") + "]", orEvaluator.toString());
    }

    @Test
    public void testAndToString() {
        Evaluator eval1 = new AttributeEvaluator.AttributeWithValue("id", "container");
        Evaluator eval2 = new AttributeEvaluator.AttributeWithValue("class", "highlight");

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(eval1, eval2);

        Assert.assertEquals(StringUtil.join(new ArrayList<>(List.of(eval1, eval2)), " "), andEvaluator.toString());
    }

}