import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {
    private Element root;
    private Element node;

    @Before
    public void setUp() {
        root = new Element();
        node = new Element();
    }

    @Test
    public void testAndMatches() {
        Evaluator s1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator s2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        CombiningEvaluator.And and = new CombiningEvaluator.And(Arrays.asList(s1, s2));
        boolean result = and.matches(root, node);
        Assert.assertEquals(true, result);
    }
    
    @Test
    public void testAndNotMatches() {
        Evaluator s1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator s2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        
        CombiningEvaluator.And and = new CombiningEvaluator.And(Arrays.asList(s1, s2));
        boolean result = and.matches(root, node);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testAndToString() {
        Evaluator s1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
            
            @Override
            public String toString() {
                return "s1";
            }
        };
        Evaluator s2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
            
            @Override
            public String toString() {
                return "s2";
            }
        };

        CombiningEvaluator.And and = new CombiningEvaluator.And(Arrays.asList(s1, s2));
        String result = and.toString();
        Assert.assertEquals("s1 s2", result);
    }

    @Test
    public void testOrMatches() {
        Evaluator s1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        Evaluator s2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(s1, s2));
        boolean result = or.matches(root, node);
        Assert.assertEquals(true, result);
    }
    
    @Test
    public void testOrNotMatches() {
        Evaluator s1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        Evaluator s2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(s1, s2));
        boolean result = or.matches(root, node);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testOrToString() {
        Evaluator s1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
            
            @Override
            public String toString() {
                return "s1";
            }
        };
        Evaluator s2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
            
            @Override
            public String toString() {
                return "s2";
            }
        };

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(s1, s2));
        String result = or.toString();
        Assert.assertEquals(":or[s1, s2]", result);
    }
}