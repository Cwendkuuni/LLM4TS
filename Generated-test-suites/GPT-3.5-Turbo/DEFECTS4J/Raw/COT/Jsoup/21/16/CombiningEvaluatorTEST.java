package org.jsoup.select;

import org.jsoup.nodes.Element;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And();
        Element root = new Element("root");
        Element node = new Element("node");
      
        assertEquals(false, evaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or();
        Element root = new Element("root");
        Element node = new Element("node");
      
        assertEquals(false, evaluator.matches(root, node));
    }

    @Test
    public void testOrAdd() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or();
        evaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        Element root = new Element("root");
        Element node = new Element("node");
      
        assertEquals(true, evaluator.matches(root, node));
    }

}