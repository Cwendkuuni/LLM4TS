package org.jsoup.select;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CombiningEvaluatorTest {

    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;

    @Before
    public void setUp() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        andEvaluator = new CombiningEvaluator.And(evaluators);

        evaluators.clear();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        orEvaluator = new CombiningEvaluator.Or(evaluators);
    }

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        Assert.assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndToString() {
        Assert.assertEquals(" and ", andEvaluator.toString());
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        Assert.assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrToString() {
        Assert.assertEquals(":or[ true, false ]", orEvaluator.toString());
    }
}