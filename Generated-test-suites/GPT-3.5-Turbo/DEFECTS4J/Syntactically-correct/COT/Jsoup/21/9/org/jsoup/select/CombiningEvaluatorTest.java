import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;


public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluator1, evaluator2);
        boolean result = and.matches(root, node);

        assertTrue(result);
    }

    @Test
    public void testAndNotMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluator1, evaluator2);
        boolean result = and.matches(root, node);

        assertFalse(result);
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluator1, evaluator2);
        boolean result = or.matches(root, node);

        assertTrue(result);
    }

    @Test
    public void testOrNotMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluator1, evaluator2);
        boolean result = or.matches(root, node);

        assertFalse(result);
    }

}