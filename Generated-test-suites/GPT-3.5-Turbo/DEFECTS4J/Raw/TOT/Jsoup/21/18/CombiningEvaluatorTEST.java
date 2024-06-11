import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("html");
        Element node = new Element("div");

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new IdEvaluator("#id"));
        evaluators.add(new ClassEvaluator(".class"));
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        boolean result = and.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("html");
        Element node = new Element("div");

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new IdEvaluator("#id"));
        evaluators.add(new ClassEvaluator(".otherClass")); // this class does not match
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        boolean result = and.matches(root, node);

        Assert.assertFalse(result);
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("html");
        Element node = new Element("div");

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new IdEvaluator("#id"));
        evaluators.add(new ClassEvaluator(".class"));
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        boolean result = or.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("html");
        Element node = new Element("div");

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new IdEvaluator("#otherId")); // this id does not match
        evaluators.add(new ClassEvaluator(".otherClass")); // this class does not match
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        boolean result = or.matches(root, node);

        Assert.assertFalse(result);
    }
}