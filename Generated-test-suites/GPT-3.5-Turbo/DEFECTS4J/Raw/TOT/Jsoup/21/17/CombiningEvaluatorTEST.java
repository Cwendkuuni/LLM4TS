import org.junit.Assert;
import org.junit.Test;

import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;

import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(mockEvaluator1, mockEvaluator2));

        boolean result = andEvaluator.matches(root, node);
        Assert.assertTrue(result);
    }

    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(mockEvaluator1, mockEvaluator2));

        boolean result = andEvaluator.matches(root, node);
        Assert.assertFalse(result);
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(mockEvaluator1, mockEvaluator2));

        boolean result = orEvaluator.matches(root, node);
        Assert.assertTrue(result);
    }

    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(mockEvaluator1, mockEvaluator2));

        boolean result = orEvaluator.matches(root, node);
        Assert.assertFalse(result);
    }

    @Test
    public void testOrWithAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator mockEvaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(mockEvaluator1, mockEvaluator2));

        orEvaluator.add(mockEvaluator3);

        boolean result = orEvaluator.matches(root, node);
        Assert.assertTrue(result);
    }

    @Test
    public void testOrWithAndDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator mockEvaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(mockEvaluator1, mockEvaluator2));

        orEvaluator.add(mockEvaluator3);

        boolean result = orEvaluator.matches(root, node);
        Assert.assertFalse(result);
    }
}