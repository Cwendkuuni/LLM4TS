package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Document doc = Jsoup.parse("<body><div id='1' class='class1'></div><div id='2' class='class2'></div></body>");
        Element root = doc.select("body").first();

        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(new Evaluator.Id("1"), new Evaluator.Class("class1"));

        assertTrue(evaluator.matches(root, root.selectFirst("div")));
    }

    @Test
    public void testAndDoesNotMatch() {
        Document doc = Jsoup.parse("<body><div id='1' class='class1'></div><div id='2' class='class2'></div></body>");
        Element root = doc.select("body").first();

        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(new Evaluator.Id("1"), new Evaluator.Class("class2"));

        assertFalse(evaluator.matches(root, root.selectFirst("div")));
    }

    @Test
    public void testAndWithEmptyEvaluators() {
        Document doc = Jsoup.parse("<body><div id='1' class='class1'></div><div id='2' class='class2'></div></body>");
        Element root = doc.select("body").first();

        CombiningEvaluator.And evaluator = new CombiningEvaluator.And();

        assertTrue(evaluator.matches(root, root.selectFirst("div")));
    }

    @Test
    public void testOrMatches() {
        Document doc = Jsoup.parse("<body><div id='1' class='class1'></div><div id='2' class='class2'></div></body>");
        Element root = doc.select("body").first();

        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(new Evaluator.Id("1"), new Evaluator.Class("class2"));

        assertTrue(evaluator.matches(root, root.selectFirst("div")));
    }

    @Test
    public void testOrDoesNotMatch() {
        Document doc = Jsoup.parse("<body><div id='1' class='class1'></div><div id='2' class='class2'></div></body>");
        Element root = doc.select("body").first();

        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(new Evaluator.Id("3"), new Evaluator.Class("class3"));

        assertFalse(evaluator.matches(root, root.selectFirst("div")));
    }

    @Test
    public void testOrWithEmptyEvaluators() {
        Document doc = Jsoup.parse("<body><div id='1' class='class1'></div><div id='2' class='class2'></div></body>");
        Element root = doc.select("body").first();

        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or();

        assertFalse(evaluator.matches(root, root.selectFirst("div")));
    }

    @Test
    public void testOrWithNestedAnd() {
        Document doc = Jsoup.parse("<body><div id='1' class='class1'></div><div id='2' class='class2'></div></body>");
        Element root = doc.select("body").first();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Id("1"));
        evaluators.add(new Evaluator.Class("class2"));

        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(evaluators);

        assertFalse(evaluator.matches(root, root.selectFirst("div")));
    }
}