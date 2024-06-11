import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CombiningEvaluatorTest {

    @Test
    public void testAndConstructorWithEvaluators() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementSelector("p"));
        evaluators.add(new ElementSelector("div"));

        And andEvaluator = new And(evaluators);

        Element root = new Element("html");

        Element p = new Element("p");
        Element div = new Element("div");

        root.appendChild(p);
        root.appendChild(div);

        assertTrue(andEvaluator.matches(root, p));
        assertTrue(andEvaluator.matches(root, div));
        assertFalse(andEvaluator.matches(root, new Element("ul")));
    }

    @Test
    public void testAndConstructorWithSingleEvaluator() {
        And andEvaluator = new And(new ElementSelector("p"));

        Element root = new Element("html");
        Element p = new Element("p");

        root.appendChild(p);

        assertTrue(andEvaluator.matches(root, p));
        assertFalse(andEvaluator.matches(root, new Element("ul")));
    }

    @Test
    public void testOrConstructorWithEvaluators() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new ElementSelector("p"));
        evaluators.add(new ElementSelector("div"));

        Or orEvaluator = new Or(evaluators);

        Element root = new Element("html");

        Element p = new Element("p");
        Element div = new Element("div");
        Element ul = new Element("ul");

        root.appendChild(p);
        root.appendChild(div);
        root.appendChild(ul);

        assertTrue(orEvaluator.matches(root, p));
        assertTrue(orEvaluator.matches(root, div));
        assertFalse(orEvaluator.matches(root, ul));
    }

    @Test
    public void testOrAddEvaluator() {
        Or orEvaluator = new Or();

        orEvaluator.add(new ElementSelector("p"));

        Element root = new Element("html");
        Element p = new Element("p");

        root.appendChild(p);

        assertTrue(orEvaluator.matches(root, p));
        assertFalse(orEvaluator.matches(root, new Element("ul")));

        orEvaluator.add(new ElementSelector("div"));

        Element div = new Element("div");

        root.appendChild(div);

        assertTrue(orEvaluator.matches(root, p));
        assertTrue(orEvaluator.matches(root, div));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrEmptyConstructor() {
        Or orEvaluator = new Or();

        Element root = new Element("html");

        assertFalse(orEvaluator.matches(root, new Element("p")));

        orEvaluator.add(new ElementSelector("div"));

        Element div = new Element("div");

        root.appendChild(div);

        assertTrue(orEvaluator.matches(root, div));

        orEvaluator.add(null);
    }
}