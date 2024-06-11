
package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.junit.Test;
import static junit.framework.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And();
        evaluator.add(new Evaluator.Tag("div"));
        evaluator.add(new Evaluator.Attribute("class", "content"));
        evaluator.add(new Evaluator.ContainsText("example"));

        Document doc = Jsoup.parse("<div class=\"content\">This is an example text.</div>");
        Element element = doc.selectFirst("div");

        assertTrue(evaluator.matches(doc, element));
    }

    @Test
    public void testAndNotMatches() {
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And();
        evaluator.add(new Evaluator.Tag("div"));
        evaluator.add(new Evaluator.Attribute("class", "content"));
        evaluator.add(new Evaluator.ContainsText("example"));

        Document doc = Jsoup.parse("<div class=\"wrapper\">This is not an example text.</div>");
        Element element = doc.selectFirst("div");

        assertFalse(evaluator.matches(doc, element));
    }

    @Test
    public void testOrMatches() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or();
        evaluator.add(new Evaluator.Tag("div"));
        evaluator.add(new Evaluator.Attribute("class", "content"));

        Document doc = Jsoup.parse("<div>This is a div element.</div>");
        Element element = doc.selectFirst("div");

        assertTrue(evaluator.matches(doc, element));
    }

    @Test
    public void testOrNotMatches() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or();
        evaluator.add(new Evaluator.Tag("div"));
        evaluator.add(new Evaluator.Attribute("class", "content"));

        Document doc = Jsoup.parse("<span>This is not a div element.</span>");
        Element element = doc.selectFirst("span");

        assertFalse(evaluator.matches(doc, element));
    }

    @Test
    public void testMultipleAndOrMatches() {
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And();
        andEvaluator.add(new Evaluator.Tag("div"));
        andEvaluator.add(new Evaluator.Attribute("class", "content"));

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();
        orEvaluator.add(andEvaluator);
        orEvaluator.add(new Evaluator.ContainsText("example"));

        Document doc = Jsoup.parse("<div class=\"content\">This is an example text.</div>");
        Element element = doc.selectFirst("div");

        assertTrue(orEvaluator.matches(doc, element));
    }

    @Test
    public void testMultipleAndOrNotMatches() {
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And();
        andEvaluator.add(new Evaluator.Tag("div"));
        andEvaluator.add(new Evaluator.Attribute("class", "content"));

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();
        orEvaluator.add(andEvaluator);
        orEvaluator.add(new Evaluator.ContainsText("example"));

        Document doc = Jsoup.parse("<span class=\"content\">This is an example text.</span>");
        Element element = doc.selectFirst("span");

        assertFalse(orEvaluator.matches(doc, element));
    }
    
    @Test
    public void testNestedCombiningEvaluator() {
        CombiningEvaluator.Or orEvaluator1 = new CombiningEvaluator.Or();
        orEvaluator1.add(new Evaluator.Tag("div"));
        orEvaluator1.add(new Evaluator.Attribute("class", "content"));
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And();
        andEvaluator.add(new Evaluator.Tag("p"));
        andEvaluator.add(new Evaluator.Attribute("class", "text"));
        
        CombiningEvaluator.Or orEvaluator2 = new CombiningEvaluator.Or();
        orEvaluator2.add(orEvaluator1);
        orEvaluator2.add(andEvaluator);
        
        Document doc = Jsoup.parse("<p class=\"text\">This is a paragraph.</p>");
        Element element = doc.selectFirst("p");
        
        assertTrue(orEvaluator2.matches(doc, element));
    }

}
