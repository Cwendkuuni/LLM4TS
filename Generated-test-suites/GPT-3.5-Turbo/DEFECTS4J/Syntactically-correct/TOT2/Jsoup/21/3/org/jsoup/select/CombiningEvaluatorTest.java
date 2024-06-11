package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element rootElement = new Element("root");
        Element childElement = new Element("child");
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("root"), new Evaluator.Tag("child"));
        assertTrue(andEvaluator.matches(rootElement, childElement));
    }

    @Test
    public void testAndDoesNotMatch() {
        Element rootElement = new Element("root");
        Element childElement = new Element("wrongChild");
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("root"), new Evaluator.Tag("child"));
        assertFalse(andEvaluator.matches(rootElement, childElement));
    }

    @Test
    public void testOrMatches() {
        Element rootElement = new Element("root");
        Element childElement = new Element("child");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(new Evaluator.Tag("root"), new Evaluator.Tag("child"));
        assertTrue(orEvaluator.matches(rootElement, childElement));
    }

    @Test
    public void testOrDoesNotMatch() {
        Element rootElement = new Element("root");
        Element childElement = new Element("wrongChild");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(new Evaluator.Tag("div"), new Evaluator.Tag("span"));
        assertFalse(orEvaluator.matches(rootElement, childElement));
    }

}